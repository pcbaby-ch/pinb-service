/**
 * 
 */
package com.pinb.service;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.alibaba.fastjson.JSONObject;
import com.pinb.entity.GroubaOrder;
import com.pinb.entity.User;
import com.pinb.mapper.GroubActivityCache;
import com.pinb.mapper.GroubaOrderMapper;
import com.pinb.mapper.UserMapper;
import com.pinb.util.PropertiesUtils;

/**
 * 消息推送
 * 
 * @author chenzhao @date May 31, 2019
 */
@Service
public class MsgSendService {

	private static final Logger log = LoggerFactory.getLogger(MsgSendService.class);

	@Autowired
	GroubaOrderMapper groubaOrderMapper;
	@Autowired
	UserMapper userMapper;
	@Autowired
	GroubActivityCache groubActivityCache;

	/**
	 * 成团消息通知
	 * 
	 * @author chenzhao @date May 31, 2019
	 * @param orderTrace
	 */
	@Async("getThreadPoolTaskExecutor")
	public void wxMsgSend4Joined(String goodsName, String leaderUserTrace, int groubSize, String orderTrace) {
		String templateId = PropertiesUtils.getProperty("msgTemplate", "8QB4bYZYGLqYlk5lia0DIG5PeomZUy9eVOQY8UWVF1Y");
		List<GroubaOrder> groubOrders = groubaOrderMapper.select(orderTrace, null, null, null, null);
		log.info("#异步成团通知开始，#通知用户数:[{}]", groubOrders.size());
		User learderUser = userMapper.selectOne(leaderUserTrace, null);
		log.debug("#查询团长:[{}]", learderUser.getNickname());
		for (int i = 0; i < groubOrders.size(); i++) {
			GroubaOrder order = groubOrders.get(i);
			log.debug("#通知用户:[{}]", order.getRefUserWxOpenid());
			JSONObject data = new JSONObject();
			data.put("keyword1", goodsName + "-拼团");
			data.put("keyword2", learderUser.getNickname());
			data.put("keyword3", groubSize);
			String openid = StringUtils.isEmpty(order.getRefUserWxOpenid()) ? order.getRefUserWxUnionid()
					: order.getRefUserWxOpenid();
			WxApiService.templateSend(templateId, openid, data);
		}
	}

}
