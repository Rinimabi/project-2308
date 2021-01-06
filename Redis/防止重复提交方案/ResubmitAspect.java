package Redis.防止重复提交方案;

import com.item.api.annotation.Resubmit;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.util.DigestUtils;
 
import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Method;
 
@Aspect
@Component("ResubmitAspect")
public class ResubmitAspect {
 
    private static Logger logger = LoggerFactory.getLogger(ResubmitAspect.class);
 
    @Resource
    HttpServletRequest request;
    @Resource
    ICacheService cacheService; //包装好的redis操作类，替换掉即可
 
    @Around("@annotation(com.item.api.annotation.Resubmit)")
    public Object around(ProceedingJoinPoint point) throws Throwable {
        String token = request.getHeader("Access-Token");
        MethodSignature ms = (MethodSignature) point.getSignature();
        Method method = ms.getMethod();
        int timeout = method.getAnnotation(Resubmit.class).timeout();
        String message = method.getAnnotation(Resubmit.class).message();
        StringBuilder md5Builder = new StringBuilder(token);
        if (point.getArgs() != null) {
            for (Object obj : point.getArgs()) {
                md5Builder.append(obj.toString());
            }
        }
        String md5String = DigestUtils.md5DigestAsHex(md5Builder.toString().getBytes());
        logger.info("Access-Token" + token + ", Timeout: " + timeout + ", Resubmit cache key (md5Code) : " + md5String);
        String cache = cacheService.getCache("resubmit", md5String, String.class);
        if (cache != null) {
            throw new BizException(message);
        }
        cacheService.setCache("resubmit", md5String, "", timeout);
        Object object = point.proceed();
        //cacheService.delCache(md5Builder.toString());
        return object;
    }
}