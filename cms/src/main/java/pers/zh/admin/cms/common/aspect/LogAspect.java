package pers.zh.admin.cms.common.aspect;

import com.alibaba.fastjson.JSON;
import org.apache.shiro.SecurityUtils;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import pers.zh.admin.cms.common.annotation.LogAon;
import pers.zh.admin.cms.common.utlis.HttpContextUtils;
import pers.zh.admin.cms.common.utlis.IPUtils;
import pers.zh.admin.cms.sys.entity.Log;
import pers.zh.admin.cms.sys.entity.User;
import pers.zh.admin.cms.sys.service.LogService;

import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Method;

/**
 * Created by zenghang.
 * Date: 2020/1/7
 * Time: 20:30
 */
@Aspect
@Component
public class LogAspect {

    @Autowired
    private LogService logService;

    @Pointcut("@annotation(pers.zh.admin.cms.common.annotation.LogAon)")
    public void logPointCut() {

    }

    @Around("logPointCut()")
    public Object around(ProceedingJoinPoint point) throws Throwable {
        long beginTime = System.currentTimeMillis();
        //执行方法
        Object result = point.proceed();
        //执行时长(毫秒)
        long time = System.currentTimeMillis() - beginTime;

        //保存日志
        saveSysLog(point, time);

        return result;
    }

    private void saveSysLog(ProceedingJoinPoint joinPoint, long time) {
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        Method method = signature.getMethod();

        Log log = new Log();
        LogAon logAon = method.getAnnotation(LogAon.class);
        if (logAon != null) {
            //注解上的描述
            log.setOperation(logAon.value());
        }

        //请求的方法名
        String className = joinPoint.getTarget().getClass().getName();
        String methodName = signature.getName();
        log.setMethod(className + "." + methodName + "()");

        //请求的参数
        Object[] args = joinPoint.getArgs();
        try {
            String params = JSON.toJSONString(args[0]);
            log.setParams(params);
        } catch (Exception ignored) {

        }

        //获取request
        HttpServletRequest request = HttpContextUtils.getHttpServletRequest();
        //设置IP地址
        log.setIp(IPUtils.getIpAddr(request));

        //用户名
        String username = ((User) SecurityUtils.getSubject().getPrincipal()).getUserName();
        log.setUsername(username);

        log.setTime(time);
        //保存系统日志
        logService.save(log);
    }
}
