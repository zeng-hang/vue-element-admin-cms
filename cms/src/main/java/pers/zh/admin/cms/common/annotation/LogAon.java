package pers.zh.admin.cms.common.annotation;

import java.lang.annotation.*;

/**
 * Created by zenghang.
 * Date: 2020/1/7
 * Time: 20:29
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface LogAon {
    String value() default "";
}
