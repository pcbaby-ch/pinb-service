package com.pinb;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.pinb.mapper.GroubaOrderMapper;
import com.pinb.service.GroubaOrderService;

/**
 * 
 */

/**
 * @author chenzhao @date Apr 9, 2019
 */
@SpringBootTest
@RunWith(SpringRunner.class)
//@Ignore
public class GroubaOrderTest {
	
	@Autowired
	private GroubaOrderService groubaOrderService;
	
	@Test
	public void orderAdd()	{
		System.out.println("sdf");
	}

}
