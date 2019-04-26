/**
 * 
 */
package com.pinb.config.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 往control请求参数中put用户ip
 * 
 * @author chenzhao @date Apr 17, 2019
 *
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ ElementType.PARAMETER })
public @interface PutIp2Req {

}
