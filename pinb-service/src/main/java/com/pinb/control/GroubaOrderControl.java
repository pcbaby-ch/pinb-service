/**
 * 
 */
package com.pinb.control;

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

	@Autowired
	private GroubaOrderService groubaOrderService;

	@RequestMapping("orderSelect")
	public Object orderSelect(@RequestBody GroubaOrder groubaOrder)  {
		return groubaOrderService.orderSelect(groubaOrder);
	}

	@RequestMapping("orderAdd")
	public Object orderAdd(@RequestBody GroubaOrder groubaOrder)  {

		return groubaOrderService.orderAdd(groubaOrder);
	}

	@RequestMapping("orderUpdate")
	public Object orderUpdate() {
		return "hello world";
	}

}
