package Java.com.aspect;

import com.alibaba.fastjson.JSONObject;
import org.apache.commons.lang3.StringUtils;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.stereotype.Component;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;

import javax.annotation.Resource;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Aspect
@Component("RequestLogAspect")
public class RequestLogAspect {

    private static Logger logger = LoggerFactory.getLogger(RequestLogAspect.class);

    @Resource
    private ICommonService commonService;
    @Resource
    private ICommonRequestLogService commonRequestLogService;
    @Resource
    private DataSourceTransactionManager dataSourceTransactionManager;

    @Around("@annotation(com.skyworth.center.order.biz.annotation.RequestLog)")
    public Object around(ProceedingJoinPoint point) throws Throwable {
        long entityId = 0L;
        String entityCode = "";
        MethodSignature ms = (MethodSignature) point.getSignature();
        Method method = ms.getMethod();
        Integer outSysNo = method.getAnnotation(RequestLog.class).outSysNo().getValue();
        String apiCode = method.getAnnotation(RequestLog.class).apiCode().getValue();
        String[] bizIds = method.getAnnotation(RequestLog.class).bizId();
        String[] bizCodes = method.getAnnotation(RequestLog.class).bizCode();
        JSONObject jsonObject = new JSONObject();
        if (point.getArgs() != null) {
            String[] parameterNames = ((MethodSignature) point.getSignature()).getParameterNames();
            for (int i = 0; i < point.getArgs().length; i++) {
                jsonObject.put(parameterNames[i], point.getArgs()[i]);
            }
            String jsonString = jsonObject.toJSONString();
            if (StringUtils.isNotBlank(jsonString)) {
                for (String bizId : bizIds) {
                    List<String> stringList = this.matchJsonValue(jsonString, bizId);
                    if (stringList.size() > 0) {
                        try {
                            entityId = Long.parseLong(stringList.get(0));
                        } catch (NumberFormatException ignored) {

                        }
                        break;
                    }
                }
                for (String bizCode : bizCodes) {
                    List<String> stringList = this.matchJsonValue(jsonString, bizCode);
                    if (stringList.size() > 0) {
                        entityCode = stringList.get(0);
                    }
                }
            }
            logger.info("入参：" + jsonString);
        }
        TransactionDefinition definition = new DefaultTransactionDefinition(TransactionDefinition.PROPAGATION_REQUIRES_NEW);
        TransactionStatus status = dataSourceTransactionManager.getTransaction(definition);
        try {
            Object proceed = point.proceed();
            commonRequestLogService.addRequestLog(commonService.getRequestLogNo(), outSysNo, apiCode, entityId, entityCode, OutSysEnum.Result.SUCCESS.getValue(), jsonObject, proceed);
            dataSourceTransactionManager.commit(status);
            return proceed;
        } catch (Exception e) {
            commonRequestLogService.addRequestLog(commonService.getRequestLogNo(), outSysNo, apiCode, entityId, entityCode, OutSysEnum.Result.FAIR.getValue(), jsonObject, e.getMessage());
            dataSourceTransactionManager.commit(status);
            throw e;
        }
    }

    /**
     * 正则表达式 查找匹配json字符串对应属性的值
     *
     * @param str        字符串
     * @param patternStr 字符串
     * @return 字符集
     */
    private List<String> matchJsonValue(String str, String patternStr) {
        List<String> aimList = new ArrayList<>();
        Pattern pattern = Pattern.compile(patternStr + RegexUtil.JSON_KEY);
        Matcher matcher = pattern.matcher(str);
        while (matcher.find()) {
            aimList.add(matcher.group(1));
        }
        return aimList;
    }
}