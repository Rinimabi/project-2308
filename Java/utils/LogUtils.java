package Java.utils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.dtyunxi.util.ReflectionUtils;
import org.apache.commons.collections4.CollectionUtils;

import java.util.Collection;

/**
 * @Author: 世镜
 * @ClassName: LogUtils
 * @Date: 2021/3/11 21:57
 * @Description:
 */
public class LogUtils {


    /**
     * 组装参数日志内容
     *
     * @param obj 对象
     * @param fields 字段列表
     * @return
     */
    public static String buildLogContent(Object obj, String... fields){
        if (obj == null || fields == null || fields.length <= 0){
            return "";
        }
        StringBuilder sb = new StringBuilder();
        for (String field:fields) {
            Object fieldValue = ReflectionUtils.getFieldValue(obj, field);
            if (fieldValue == null) {
                continue;
            }
            sb.append(", " + field + ":" + fieldValue);
        }
        if (sb.length() <= 0) {
            return "";
        }
        return sb.substring(2);
    }


    /**
     * 组装参数日志内容
     * 返回对象的json字符串内容
     *
     * @param obj 对象
     * @return
     */
    public static String buildLogContent(Object obj){
        if (obj == null){
            return "";
        }
        return JSON.toJSONString(obj, SerializerFeature.WriteDateUseDateFormat);
    }

    /**
     * 组装参数日志内容
     * 返回集合的json字符串内容
     *
     * @param coll 集合
     * @return
     */
    public static String buildLogContent(Collection coll){
        if (CollectionUtils.isEmpty(coll)){
            return "";
        }
        return JSON.toJSONString(coll, SerializerFeature.WriteDateUseDateFormat);
    }

    /**
     * 处理显示的值,具有保密性的值需要特殊处理，不能直接log到日志文件中
     *
     * @author qiujun
     * @date 2020/2/8
     * @param key - 显示key，如参数名phone
     * @param value - 要打印的内容，如13800138000
     * @return 加密后的内容
     */
    public static String dealShowValue(String key, String value) {
        String lowerName = key.toLowerCase();

        if (lowerName.indexOf("password") >= 0) {// 完全隐藏密码
            value = "***";
        } else if (lowerName.indexOf("accesskey") >= 0 || lowerName.indexOf("accesssecret") >= 0
                || lowerName.indexOf("secret") >= 0 || lowerName.indexOf("alipay") >= 0 || lowerName.indexOf("wechat") >= 0
                || lowerName.indexOf("phone") >= 0
                || lowerName.indexOf("passport") >= 0 || lowerName.indexOf("mobile") >= 0 || lowerName.indexOf("account") >= 0) {
            // 隐藏部分值
            value = hidePassport(value);
        }
        return value;
    }

    /**
     * 隐藏 通行证 的输出，避免通行证直接显示在日志里
     *
     * @param passport 通行证
     * @return 返回隐藏了部分字符的带 *** 的字符串
     */
    public static String hidePassport(String passport) {
        if (org.apache.commons.lang3.StringUtils.isEmpty(passport)) {
            return passport;
        }
        if (passport.length() <= 1) {
            return passport + "***";
        }
        if (passport.length() <= 4) {
            // 和 length=1 时相同，避免直接猜测出来
            return passport.substring(0, 1) + "***";
        }
        if (passport.length() <= 6)
            return passport.substring(0, 2) + "***" + passport.substring(passport.length() - 1);

        return passport.substring(0, 3) + "***" + passport.substring(passport.length() - 3);
    }
}
