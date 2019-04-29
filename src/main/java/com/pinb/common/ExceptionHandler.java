package com.pinb.common;

import java.io.IOException;
import java.net.SocketTimeoutException;
import java.util.HashMap;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.servlet.HandlerExceptionResolver;
import org.springframework.web.servlet.ModelAndView;

import com.alibaba.fastjson.JSONObject;
import com.pinb.enums.RespCode;

/**
 * 
 * @author chen.zhao (chenzhao) @DATE: Jan 9, 2018
 */
@Component
public class ExceptionHandler implements HandlerExceptionResolver {
	private static final Logger LOGGER = LoggerFactory.getLogger(ExceptionHandler.class);

	@Value("${businessError:false}")
	private String businessError;
	@Value("${systemError:false}")
	private String systemError;
	@Autowired
	private EmailService mailService;

	@Override
	public ModelAndView resolveException(HttpServletRequest request, HttpServletResponse response, Object handler,
			Exception ex) {
		HashMap<String, Object> result = new HashMap<>();
		if (ex instanceof ServiceException) {
			result.put("retCode", ((ServiceException) ex).getCode());
			result.put("retMsg",
					(StringUtils.isEmpty(((ServiceException) ex).getMsg()) ? ((ServiceException) ex).getMessage()
							: ((ServiceException) ex).getMsg()));
			if (!StringUtils.isEmpty(((ServiceException) ex).getData())) {
				result.put("data", JSONObject.parse(((ServiceException) ex).getData()));
			}
			if ("10000".equals(((ServiceException) ex).getCode())) {
				LOGGER.info("业务处理成功");
			} else {
				LOGGER.debug("业务异常："
						+ (StringUtils.isEmpty(((ServiceException) ex).getMsg()) ? ((ServiceException) ex).getMessage()
								: ((ServiceException) ex).getMsg()),
						ex);
				LOGGER.info("业务异常："
						+ (StringUtils.isEmpty(((ServiceException) ex).getMsg()) ? ((ServiceException) ex).getMessage()
								: ((ServiceException) ex).getMsg()));
				errorNotifications(ex);
			}
		} else if (ex instanceof SocketTimeoutException) {
			LOGGER.error("#与客户端通讯异常：{}", ex.getMessage());
		} else {
			result.put("retCode", RespCode.END.getCode());
			result.put("retMsg", RespCode.END.getMsg());
			LOGGER.error(RespCode.END.getMsg(), ex);
			errorNotifications(ex);
		}
		response.setContentType(MediaType.APPLICATION_JSON_VALUE);
		response.setCharacterEncoding("UTF-8");
		response.setHeader("Cache-Control", "no-cache, must-revalidate");
		try {
			response.getWriter().write(JSONObject.toJSONString(result));
		} catch (IOException e) {
			LOGGER.error("#与客户端通讯异常：" + e.getMessage(), e);
		}

		return new ModelAndView();
	}

	public void errorNotifications(Exception e) {
		if (e instanceof ServiceException) {
			if ("true".equals(businessError)) {
				mailService.sendSimpleMail("979191434@qq.com", "业务异常告警",
						(StringUtils.isEmpty(((ServiceException) e).getMsg()) ? ((ServiceException) e).getMessage()
								: ((ServiceException) e).getMsg()));
			}
		} else {
			if ("true".equals(systemError)) {
				mailService.sendSimpleMail("979191434@qq.com", "系统异常告警", e.getMessage());
			}
		}
	}
}
