package Redis.防止重复提交方案;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
* 防止重复提交<br/>
* 使用token和上送参数计算MD5作为缓存key，<b>注意上送值为pojo类的话应有toString()方法<b/>
*
* @author 无陵
* @since 1.0.0
*/
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface Resubmit {
    /**
    * 过期时间，默认2秒
    */
    int timeout() default 2;

    /**
    * 重复提交警告信息，默认为"您的操作过于频繁"
    */
    String message() default "您的操作过于频繁!";
}