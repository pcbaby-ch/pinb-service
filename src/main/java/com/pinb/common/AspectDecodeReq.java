package com.pinb.common;

import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.Base64;
import java.util.Date;

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
import com.pinb.util.DateUtil;
import com.pinb.util.IpUtils;
import com.pinb.util.RSAUtil;

/**
 * 解密请求报文切面管理
 * 
 * @author chenzhao @date Apr 29, 2019
 */
@Aspect
@Component
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
				log.debug("#请求报文-解密前,#[{}]:[{}]", params[i], args[i]);
				String oldReqStr = null;
				if (args[i] instanceof String) {
					oldReqStr = (String) args[i];
				} else {
					throw new ServiceException(String.format("#参数解密失败，参数%s非字符串类型", params[i]));
				}
				args[i] = new String(Base64.getDecoder().decode(decode(oldReqStr).getBytes()));
				log.info("#请求报文-解密后,#[{}]:[{}]", params[i], args[i]);
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

	/**
	 * 
	 * @author chenzhao @date Jun 11, 2019
	 * @param args
	 */
	public static void main(String[] args) {
		String str = "5ZKMNA==";
//			str=addIndexChar(0, str, 'A');
//			str=addIndexChar(2, str, 'A');
//			str=addIndexChar(4, str, 'A');
//			str=addIndexChar(6, str, 'A');
//			str=addIndexChar(8, str, 'A');
//			str=addIndexChar(10, str, 'A');
//			str=addIndexChar(12, str, 'A');
//			str=addIndexChar(14, str, 'A');
		str = encode(str);
		System.out.println("#encode：" + str);
//			str=removeIndexChar(1, str);
//			str=removeIndexChar(2, str);
//			str=removeIndexChar(3, str);
//			str=removeIndexChar(4, str);
//			str=removeIndexChar(5, str);
//			str=removeIndexChar(6, str);
//			str=removeIndexChar(7, str);
//			str=removeIndexChar(8, str);
		str = decode(
				"e0y6J1w1c1m29129aW5jZSI6IuS4iua1t+W4giIsImNpdHkiOiLkuIrmtbfluIIiLCJsYXRpdHVkZSI6MzEuMzA0OTgsImxvbmdpdHVkZSI6MTIxLjUxMzI1LCJwYWdlIjoxLCJyb3dzIjo2fQ==");
		System.out.println("#decode：" + str);
	}

	/**
	 * @author chenzhao @date Jun 11, 2019
	 * @param str
	 * @return
	 */
	private static String decode(String str) {
		for (int i = 0; i < 8; i++) {
			str = removeIndexChar(i + 1, str);
		}
		return str;
	}

	/**
	 * @author chenzhao @date Jun 11, 2019
	 * @param str
	 * @return
	 */
	private static String encode(String str) {
		String saltCode = DateUtil.dfyyyyMMddhhmmss.format(new Date()).substring(4, 12);
		System.out.println("#saltCode:" + saltCode);
		for (int i = 0; i < 8; i++) {
			str = addIndexChar(i * 2, str, saltCode.charAt(i));
		}
		return str;
	}

}