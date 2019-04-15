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

	@RequestMapping("select")
	public Object select(@RequestBody GroubaOrder groubaOrder)  {
		return groubaOrderService.select(groubaOrder);
	}

	@RequestMapping("add")
	public Object add(@RequestBody GroubaOrder groubaOrder)  {

		return groubaOrderService.add(groubaOrder);
	}

	@RequestMapping("update")
	public Object update(@RequestBody GroubaOrder groubaOrder) {
		return groubaOrderService.update(groubaOrder);
	}

}
