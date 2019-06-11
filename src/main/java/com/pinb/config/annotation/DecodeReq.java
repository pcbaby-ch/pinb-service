/**
 * 
 */
package com.pinb.config.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 解密请求报文
 * 
 * @author chenzhao @date Apr 29, 2019
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ ElementType.PARAMETER })
public @interface DecodeReq {
	/**
	 * 是否验签，默认不验签
	 * 
	 * @author chenzhao @date Apr 29, 2019
	 * @return
	 */
	boolean isCheckSign() default false;

}
