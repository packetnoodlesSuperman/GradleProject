package com.bob.aspectj;

import android.view.View;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;

import java.util.Calendar;


/**
 * @author xhb
 * @desc AspectJ 非侵入式 支持编译期和加载时代码注入，不影响性能。
 */
@Aspect
public class AspectBinding {

    //语法：execution(@注解 访问权限 返回值的类型 包名.函数名(参数))
    @Pointcut("execution(@com.dandelion.my.aspect.TimeTrace * *(..))")
    public void myPointCut(){ }

    @Pointcut("execution(@com.dandelion.my.aspect.SingleClick * *(..))")
    public void singleClickPointCut(){ }


    //单击事件
    @Around("singleClickPointCut()")
    public void dealSingleClickPoint(ProceedingJoinPoint joinPoint) throws Throwable {
        View view=null;
        for (Object arg: joinPoint.getArgs()) {
            if (arg instanceof View) view= ((View) arg);
        }
        if (view!=null){
            Object tag=view.getTag(view.getId());
            long lastClickTime= (tag!=null)? (long) tag :0;
            long currentTime = Calendar.getInstance().getTimeInMillis();
            if (currentTime - lastClickTime > 500) {
                joinPoint.proceed();
            }
            view.setTag(view.getId(), currentTime);
        }
    }

    //埋点 方法前
    @Before("myPointCut()")
    public void onActivityMethodBefore(JoinPoint point) throws Throwable {
        MethodSignature methodSignature = (MethodSignature) point.getSignature();
        // 获取注解
        TimeTrace annotation = methodSignature.getMethod().getAnnotation(TimeTrace.class);
        String value = annotation.value();
    }

    //埋点 方法后
    @After("myPointCut()")
    public void onActivityMethodAfter(JoinPoint point) throws Throwable {
        MethodSignature methodSignature = (MethodSignature) point.getSignature();
        // 获取注解
        TimeTrace annotation = methodSignature.getMethod().getAnnotation(TimeTrace.class);
        String value = annotation.value();
    }


}