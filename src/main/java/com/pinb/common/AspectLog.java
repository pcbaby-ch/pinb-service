package com.pinb.common;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.alibaba.fastjson.JSONObject;

/**
 * AOP 日志记录
 * 
 * @author chen.zhao @DATE: May 16, 2018
 */
@Aspect
@Component
public class AspectLog {

	private Logger log = LoggerFactory.getLogger(AspectLog.class);

	@Pointcut("execution(* com.pinb.control..*.*(..))") // 要处理的方法，包名+类名+方法名
	public void cut() {
	}

	@Around("cut()")
	public Object around(ProceedingJoinPoint pjp) throws Throwable {
		long startTime = System.currentTimeMillis();
		String method = pjp.getSignature().getDeclaringTypeName() + "." + pjp.getSignature().getName();
		Object obj = pjp.proceed();
		log.info(">>>method proceedInterval:[{}],,[{}]", System.currentTimeMillis() - startTime, method);
		return obj;
	}

	@AfterReturning(returning = "obj", pointcut = "cut()")
	public void doAfterReturning(Object obj) {
		log.debug(">>>method return:[{}]", JSONObject.toJSON(obj));
	}
}