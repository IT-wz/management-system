package com.itheima.web.aspect;

import com.alibaba.dubbo.config.annotation.Reference;
import com.itheima.domain.system.SysLog;
import com.itheima.service.system.SysLogService;
import com.itheima.web.controller.BaseController;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestMapping;

import java.lang.reflect.Method;
import java.util.Date;
import java.util.UUID;

//@Aspect
//@Component
public class WriteLog extends BaseController {

    @Reference
    private SysLogService sysLogService;

    @Around("execution(* com.itheima.web.controller.*.*.*(..))")
    public Object log(ProceedingJoinPoint proceedingJoinPoint) throws Throwable {
        //记录日志
        SysLog sysLog = new SysLog();
        //容易
        sysLog.setId(UUID.randomUUID().toString());
        sysLog.setUserName(getLoginUser().getUserName());
        sysLog.setIp(request.getRemoteAddr());
        sysLog.setTime(new Date());
        sysLog.setCompanyId(getCompanyId());
        sysLog.setCompanyName(getCompanyName());

        //从切点上获取方法名称和切点方法上的注解中name
        MethodSignature methodSignature = (MethodSignature) proceedingJoinPoint.getSignature();
        Method method = methodSignature.getMethod();

        String methodName = method.getName();//方法名
        sysLog.setMethod(methodName);

        //获取方法的注解
        if (method.isAnnotationPresent(RequestMapping.class)) {
            RequestMapping annotation = method.getAnnotation(RequestMapping.class);
            sysLog.setAction(annotation.name());
        }

        //保存
        sysLogService.save(sysLog);

        //执行原方法
        return proceedingJoinPoint.proceed();
    }

}
