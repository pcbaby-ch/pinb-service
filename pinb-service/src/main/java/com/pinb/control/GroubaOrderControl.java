/**
 * 
 */
package com.pinb.control;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.pinb.entity.GroubaOrder;
import com.pinb.service.GroubaOrderService;

/**
 * @author chenzhao @date Apr 10, 2019
 */
@RestController
@RequestMapping("groubaOrder")
public class GroubaOrderControl {

	private static final Logger log = LoggerFactory.getLogger(GroubaOrderControl.class);

	@Autowired
	private GroubaOrderService groubaOrderService;

	@RequestMapping("orderSelect")
	public Object orderSelect() {
		return "hello world";
	}

	@RequestMapping("orderSelect")
	public Object orderAdd(@RequestBody GroubaOrder groubaOrder) throws Exception {
		
		return groubaOrderService.orderAdd(groubaOrder);
	}

	@RequestMapping("orderSelect")
	public Object orderUpdate() {
		return "hello world";
	}

}
