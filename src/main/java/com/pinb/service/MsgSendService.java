/**
 * 
 */
package com.pinb.service;

import java.util.Date;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.alibaba.fastjson.JSONObject;
import com.pinb.entity.GroubaOrder;
import com.pinb.entity.GroupBar;
import com.pinb.entity.User;
import com.pinb.mapper.GroubActivityCache;
import com.pinb.mapper.GroubaOrderMapper;
import com.pinb.mapper.GroupBarMapper;
import com.pinb.mapper.UserMapper;
import com.pinb.util.DateUtil;
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
	GroupBarMapper groupBarMapper;
	@Autowired
	UserMapper userMapper;
	@Autowired
	GroubActivityCache groubActivityCache;

	private int[] groubaSize = { 6, 8, 9 };

	/**
	 * 消费成功-消息通知
	 * 
	 * @author chenzhao @date Jun 6, 2019
	 * @param goodsName
	 * @param groubAddress
	 * @param orderTrace
	 */
	@Async("getThreadPoolTaskExecutor")
	public void wxMsgSend4Consumed(String goodsName, String groubTrace, String orderTrace, String formId) {
		String templateId = PropertiesUtils.getProperty("msgTemplate4Consumed",
				"DmXkTUIeCZjkREgS1uvizW7TRjCWExd-5OC_pUALQgU");
		// #团购名称 取货地址 参与时间
		List<GroubaOrder> groubOrders = groubaOrderMapper.select(orderTrace, null, null, null, null);
		GroupBar groub = groupBarMapper.selectOne(null, groubTrace);
		log.info("#异步消费成功通知开始，#通知用户数:[{}]", groubOrders.size());
		for (int i = 0; i < groubOrders.size(); i++) {
			GroubaOrder order = groubOrders.get(i);
			log.debug("#通知用户:[{}]", order.getRefUserWxOpenid());
			JSONObject data = new JSONObject();
			JSONObject value = new JSONObject();
			value.put("value", goodsName + "-拼团");
			data.put("keyword1", value.clone());
			value.put("value", groub.getGroubAddress());
			data.put("keyword2", value.clone());
			value.put("value", DateUtil.dfyyyy_MM_ddhhmmss.format(new Date()));
			data.put("keyword3", value);
			String openid = StringUtils.isEmpty(order.getRefUserWxOpenid()) ? order.getRefUserWxUnionid()
					: order.getRefUserWxOpenid();
			WxApiService.templateSend(templateId, openid, formId, order.getRefGroubTrace(), data);
		}
	}

	/**
	 * 成团-消息通知
	 * 
	 * @author chenzhao @date Jun 6, 2019
	 * @param goodsName
	 * @param leaderUserTrace
	 * @param groubSize
	 * @param orderTrace
	 * @param formId
	 */
	@Async("getThreadPoolTaskExecutor")
	public void wxMsgSend4Joined(String goodsName, String leaderUserTrace, int groubSize, String orderTrace) {
		String templateId = PropertiesUtils.getProperty("msgTemplate4Joined",
				"8QB4bYZYGLqYlk5lia0DIG5PeomZUy9eVOQY8UWVF1Y");
		// #活动名称 团长 成团人数
		List<GroubaOrder> groubOrders = groubaOrderMapper.select(orderTrace, null, null, null, null);
		log.info("#异步成团通知开始，#通知用户数:[{}]", groubOrders.size());
		User learderUser = userMapper.selectOne(leaderUserTrace, null);
		log.debug("#查询团长:[{}]", learderUser.getNickname());
		for (int i = 0; i < groubOrders.size(); i++) {
			GroubaOrder order = groubOrders.get(i);
			log.debug("#通知用户:[{}]", order.getRefUserWxOpenid());
			JSONObject data = new JSONObject();
			JSONObject value = new JSONObject();
			value.put("value", goodsName + "-拼团");
			data.put("keyword1", value.clone());
			value.put("value", learderUser.getNickname());
			data.put("keyword2", value.clone());
			value.put("value", groubaSize[groubSize]);
			data.put("keyword3", value);
			String openid = StringUtils.isEmpty(order.getRefUserWxOpenid()) ? order.getRefUserWxUnionid()
					: order.getRefUserWxOpenid();
			WxApiService.templateSend(templateId, openid, order.getFormId(), order.getRefGroubTrace(), data);
		}
	}

}
