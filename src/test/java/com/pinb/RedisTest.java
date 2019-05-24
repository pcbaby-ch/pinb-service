/**
 * 
 */
package com.pinb;

import com.pinb.config.RedisPool;

/**
 * @author chenzhao @date May 24, 2019
 */
public class RedisTest {

	public static void main(String[] args) {
		System.out.println(RedisPool.hgetall("billService_phoneOperatorCode_136"));
	}
}
