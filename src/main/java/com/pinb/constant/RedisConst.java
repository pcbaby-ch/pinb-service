package com.pinb.constant;

/**
 * redis key 记录常量类（防止key冲突）
 * 
 * @author chenzhao @date Apr 9, 2019
 */
public class RedisConst {
	// ## 流水号序列，增长因子key ##################################################/
	public static final String groupBarTrace = "pinbService_groupBarTrace";
	public static final String groubActivityTrace = "pinbService_groubActivityTrace";
	public static final String groubaOrderTrace = "pinbService_groubaOrderTrace";
	// ## DB数据缓存key
	public static final String GroubActivityCache = "GroubActivityCache_";
	public static final String GroupBarCache = "GroupBarCache_";
	public static final String UserCache = "UserCache_";

}
