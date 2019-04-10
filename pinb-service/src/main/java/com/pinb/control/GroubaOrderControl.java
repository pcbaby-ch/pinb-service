/**
 * 
 */
package com.pinb.control;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


/**
 * @author chenzhao @date Apr 10, 2019
 */
@RestController
@RequestMapping("groubaOrder")
public class GroubaOrderControl {
	
	private static final Logger log = LoggerFactory.getLogger(GroubaOrderControl.class);
	
	@RequestMapping("orderSelect")
	public Object orderSelect() {
		return "hello world";
	}

}
