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
import com.pinb.entity.GroupBar;
import com.pinb.mapper.GroubActivityCache;
import com.pinb.mapper.GroubaOrderMapper;
import com.pinb.mapper.GroupBarMapper;
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
	GroupBarMapper groupBarMapper;
	@Autowired
	UserMapper userMapper;
	@Autowired
	GroubActivityCache groubActivityCache;

	private int[] groubaSize = { 2, 3, 5, 6, 8, 9 };

	/**
	 * 消费成功-消息通知
	 * 
	 * @author chenzhao @date Jun 6, 2019
	 * @param goodsName
	 * @param groubAddress
	 * @param orderTrace
	 */
	@Async("getThreadPoolTaskExecutor")
	public void wxMsgSend4Consumed(String goodsName, String refUserWxOpenid, int groubSize, String refGroubTrace,
			String intime, String formId) {
		String templateId = PropertiesUtils.getProperty("msgTemplate4Consumed",
				"DmXkTUIeCZjkREgS1uvizW7TRjCWExd-5OC_pUALQgU");
		// #团购名称 取货地址 参与时间
		log.info("#异步消费成功通知开始，#通知用户:[{}]", refUserWxOpenid);
		JSONObject data = new JSONObject();
		JSONObject value = new JSONObject();
		value.put("value", goodsName + "-拼团");
		data.put("keyword1", value.clone());
		value.put("value", intime);
		data.put("keyword2", value.clone());
		value.put("value", groubaSize[groubSize]);
		data.put("keyword3", value.clone());
		WxApiService.templateSend(templateId, refUserWxOpenid, formId, refGroubTrace, data);
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
	public void wxMsgSend4Joined(String goodsName, String groubaDiscountAmount, String refGroubTrace,
			String orderTrace) {
		String templateId = PropertiesUtils.getProperty("msgTemplate4Joined",
				"8QB4bYZYGLqYlk5lia0DIG5PeomZUy9eVOQY8UWVF1Y");
		// #活动名称 团长 成团人数
		List<GroubaOrder> groubOrders = groubaOrderMapper.select(orderTrace, null, null, null, null);
		log.info("#成团处理>>>>>>>>>>>>>> B4");
		log.info("#异步成团通知开始，#通知用户数:[{}]", groubOrders.size());
		GroupBar groupBar = groupBarMapper.selectOne(null, refGroubTrace);
		log.debug("#查询关联店铺:[{}]", JSONObject.toJSON(groupBar));
		for (int i = 0; i < groubOrders.size(); i++) {
			GroubaOrder order = groubOrders.get(i);
			log.debug("#通知用户:[{}]", order.getRefUserWxOpenid());
			JSONObject data = new JSONObject();
			JSONObject value = new JSONObject();
			value.put("value", goodsName + "-拼团");
			data.put("keyword1", value.clone());
			value.put("value", "立省" + groubaDiscountAmount + "元");
			data.put("keyword2", value.clone());
			value.put("value", groupBar.getGroubAddress());
			data.put("keyword3", value);
			String openid = StringUtils.isEmpty(order.getRefUserWxOpenid()) ? order.getRefUserWxUnionid()
					: order.getRefUserWxOpenid();
			WxApiService.templateSend(templateId, openid, order.getFormId(), order.getRefGroubTrace(), data);
		}
	}

}
