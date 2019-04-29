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
				String oldReqStr=args[i]+"";
				args[i]=RSAUtil.decrypt(oldReqStr);
				log.debug("#注解参数-解密后,[{}]:[{}]", params[i], args[i]);
			} else {
				log.debug("#未注解arg,[{}]:[{}]", params[i], args[i]);
			}
		}
		log.info(">>>解密完成 proceedInterval,#api[{}],#interval:[{}]", methodName,
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