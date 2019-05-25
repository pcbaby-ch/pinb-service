package com.pinb.common;

import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.Base64;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import com.alibaba.fastjson.JSONObject;
import com.pinb.config.annotation.DecodeReq;
import com.pinb.config.annotation.PutIp2Req;
import com.pinb.util.IpUtils;
import com.pinb.util.RSAUtil;

/**
 * 解密请求报文切面管理
 * 
 * @author chenzhao @date Apr 29, 2019
 */
//@Aspect
//@Component
public class AspectDecodeReq {

	private Logger log = LoggerFactory.getLogger(AspectDecodeReq.class);

	@Pointcut("execution(* com.pinb.control..*.*(..))") // 匹配加注解的类/方法
	public void cut() {
	}

	@Around("cut()")
	public Object around(ProceedingJoinPoint pjp) throws Throwable {
		long startTime = System.currentTimeMillis();
		Method method = ((MethodSignature) pjp.getSignature()).getMethod();
		String methodName = pjp.getSignature().getDeclaringTypeName() + "." + pjp.getSignature().getName();
		log.debug(">>>开始解密请求报文,#method:[{}]", methodName);
		Object[] args = pjp.getArgs();
		Parameter[] params = method.getParameters();
		for (int i = 0; i < args.length; i++) {

			Parameter parameter = params[i];
			if (parameter.isAnnotationPresent(DecodeReq.class)) {
				log.debug("#注解参数-解密前,[{}]:[{}]", params[i], args[i]);
				String oldReqStr = args[i] + "";
				args[i] = RSAUtil.decrypt(oldReqStr);
				log.debug("#注解参数-解密后,[{}]:[{}]", params[i], args[i]);
			} else {
				log.debug("#未注解arg,[{}]:[{}]", params[i], args[i]);
			}
		}
		log.info(">>>解密完成 proceedInterval,#api[{}],#interval:[{}]", methodName, System.currentTimeMillis() - startTime);
		// #go on
		Object obj = pjp.proceed(args);
		return obj;
	}

	public static String base64(String str) {
		str = "1234567890";

		return null;
	}

	public static String addIndexChar(int index, String str, char c) {
		if (index < 0 || index > str.length()) {
			throw new ServiceException(String.format("#index:[%s]，越界，#str:[%s]", index, str));
		}
		return str.substring(0, index + 1) + c + str.substring(index + 1, str.length());
	}

	public static String removeIndexChar(int index, String str) {
		if (index < 0 || index >= str.length()) {
			throw new ServiceException(String.format("#index:[%s]，越界，#str:[%s]", index, str));
		}
		return str.substring(0, index) + str.substring(index + 1, str.length());
	}

	public static void main(String[] args) {
		System.out.println( addIndexChar(1+1, addIndexChar(0, "123456789", 'a'), 'b'));//"1a2b3c45678d90"
		System.out.println(addIndexChar(2 + 2, addIndexChar(1+1, addIndexChar(0, "123456789", 'a'), 'b'), 'c'));//"1a2b3c45678d90"
		System.out.println(addIndexChar(9 + 3, addIndexChar(2 + 2, addIndexChar(1+1, addIndexChar(0, "1234567890", 'a'), 'b'), 'c'), 'd'));//"1a2b3c45678d90"
		System.out.println(removeIndexChar(2 - 2+1, removeIndexChar(1 - 1+1, removeIndexChar(0+1, "1a2b3c4567890d"))));
	}

}