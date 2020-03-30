package pers.zh.admin.cms.common.exception;

import org.apache.shiro.authz.AuthorizationException;
import org.apache.shiro.authz.UnauthenticatedException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import pers.zh.admin.cms.common.utlis.R;

/**
 * Created by zenghang.
 * Date: 2020/1/7
 * Time: 17:26
 */
@RestControllerAdvice
public class CustomExceptionHandler {
    private Logger logger = LoggerFactory.getLogger(getClass());

    /**
     * 处理自定义异常
     */
    @ExceptionHandler(CustomException.class)
    public R handleRRException(CustomException e) {
        R r = new R();
        r.put("code", e.getCode());
        r.put("msg", e.getMessage());

        return r;
    }

    @ExceptionHandler(UnauthenticatedException.class)
    public R handleAuthorizationException(UnauthenticatedException e) {
        logger.error(e.getMessage(), e);
        return R.error("未登录，请登录后再进行相关操作");
    }

    @ExceptionHandler(AuthorizationException.class)
    public R handleAuthorizationException(AuthorizationException e) {
        logger.error(e.getMessage(), e);
        return R.error("没有权限，请联系管理员授权");
    }

    @ExceptionHandler(Exception.class)
    public R handleException(Exception e) {
        logger.error(e.getMessage(), e);
        return R.error();
    }

}
