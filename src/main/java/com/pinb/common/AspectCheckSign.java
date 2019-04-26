package com.pinb.common;

import java.util.Base64;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.alibaba.fastjson.JSONObject;

/**
 * 签名校验
 * 
 * @author chenzhao @date Apr 12, 2019
 */
@Aspect
@Component
public class AspectCheckSign {

	private Logger log = LoggerFactory.getLogger(AspectCheckSign.class);

	@Pointcut("@within(com.pinb.config.annotation.CheckSign)") // 匹配加注解的类/方法
	public void checkSign() {
	}

	@Around("checkSign()")
	public Object around(ProceedingJoinPoint pjp) throws Throwable {
		long startTime = System.currentTimeMillis();
		String method = pjp.getSignature().getDeclaringTypeName() + "." + pjp.getSignature().getName();
		log.debug(">>>check api sign,#method:[{}],#arge:[{}]", method, JSONObject.toJSON(pjp.getArgs()));
		// #校验签名
		// {"base":{"userWxUnionid":"sdlfkjsdlfjxcjj2esdfjlxcj","userWxOpenid":"sdf23cbegdfg345dfbv45eyt34ve"},"data":{},"sign":"sdfger33445sdfgw"}
		log.info(">>>api proceedInterval,#api[{}],#interval:[{}]", method, System.currentTimeMillis() - startTime);
		// #go on
		Object obj = pjp.proceed();
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