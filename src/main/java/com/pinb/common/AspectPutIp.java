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
import com.pinb.config.annotation.PutIp2Req;
import com.pinb.util.IpUtils;

/**
 * 往control请求参数中put用户ip
 * 
 * @author chenzhao @date Apr 17, 2019
 *
 */
//@Aspect
//@Component
public class AspectPutIp {

	private Logger log = LoggerFactory.getLogger(AspectPutIp.class);

	@Pointcut("execution(* com.pinb.control..*.*(..))") // 匹配加注解的类/方法
	public void putIpCut() {
	}

	@Around("putIpCut()")
	public Object around(ProceedingJoinPoint pjp) throws Throwable {
		long startTime = System.currentTimeMillis();
		Method method = ((MethodSignature) pjp.getSignature()).getMethod();
		String methodName = pjp.getSignature().getDeclaringTypeName() + "." + pjp.getSignature().getName();
		log.debug(">>>PutIp,#method:[{}]", methodName);
		Object[] args = pjp.getArgs();
		Parameter[] params = method.getParameters();
		for (int i = 0; i < args.length; i++) {

			Parameter parameter = params[i];
			if (parameter.isAnnotationPresent(PutIp2Req.class)) {
				log.debug("#注解参数putIp前,[{}]:[{}]", params[i], args[i]);
				ServletRequestAttributes sra = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
				JSONObject argJson = JSONObject.parseObject(args[i] + "");
				argJson.put("userIp", IpUtils.getIpFromRequest(sra.getRequest()));
				args[i] = argJson + "";
				log.debug("#注解参数putIp后,[{}]:[{}]", params[i], args[i]);
			} else {
				log.debug("#未注解arg,[{}]:[{}]", params[i], args[i]);
			}
		}
		log.info(">>>PutIp proceedInterval,#api[{}],#interval:[{}]", methodName,
				System.currentTimeMillis() - startTime);
		// #go on
		Object obj = pjp.proceed(args);
		return obj;
	}

	public static String base64(String str) {
		String strBase64 = Base64.getEncoder().encodeToString(str.getBytes());
		System.out.println("#str加密后:" + strBase64);
		byte[] bytes = Base64.getDecoder().decode(strBase64);
		System.out.println("#str加解密后:" + new String(bytes));

		return null;
	}

	public static void main(String[] args) {
		base64("撒旦解放了手机发了几位老人家");
	}

}