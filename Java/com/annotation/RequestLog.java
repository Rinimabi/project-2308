package Java.com.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 将接口参数写入到表中<br/>
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface RequestLog {
    /**
     * 外系统编码
     */
    String outSysNo();

    /**
     * 接口编码
     */
    String apiCode();

    /**
     * 标识哪一个参数作为关联bizCode
     */
    String[] bizCode() default "code";

    /**
     * 标识哪一个参数作为关联bizId
     */
    String[] bizId() default "id";
}