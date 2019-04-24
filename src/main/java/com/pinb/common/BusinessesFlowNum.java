package com.pinb.common;

import java.util.Date;
import java.util.concurrent.locks.ReentrantLock;

import com.pinb.config.RedisPool;
import com.pinb.enums.RespCode;
import com.pinb.util.DateUtil;


/**
 * 业务流水号，生成服务
 * 
 * @author chen.zhao @DATE: Aug 7, 2018
 */
public class BusinessesFlowNum {

	static ReentrantLock lock = new ReentrantLock();

	/**
	 * 
	 * @author chenzhao @date Nov 28, 2018
	 * @param prefix     流水号前缀
	 * @param flowNumKey 流水号redis缓存key {不同流水号使用不同key，防止重复}
	 * @return
	 * @throws Exception
	 */
	@SuppressWarnings("deprecation")
	public static String getNum(String prefix, String flowNumKey)  {
		Long flowNum = 1L;
		Date dt = new Date();
		dt.setHours(23);
		dt.setMinutes(59);
		dt.setSeconds(59);

		try {
			lock.lock();
			if (RedisPool.exists(flowNumKey)) {
				flowNum = RedisPool.incr(flowNumKey);
				if (new Date().getHours() != 23) {//除23点不更新，其它时间更新redis超时时间{防止缓存超时时间穿透到第2天}
					RedisPool.pexpireAt(flowNumKey, dt.getTime());
				}
			} else {
//				RedisPool.set(redisNumKey, Integer.parseInt(DateUtil.calcInterval(new Date(),dt)/1000+""), 1);
				RedisPool.set(flowNumKey, 1);
				RedisPool.pexpireAt(flowNumKey, dt.getTime());
			}
		} catch (Exception e) {
			throw new ServiceException("#redis服务异常，获取流水号失败");
		} finally {
			lock.unlock();
		}
		return prefix + DateUtil.dfyyyyMMdd.format(new Date()) + String.format("%08d", flowNum);
	}

}
