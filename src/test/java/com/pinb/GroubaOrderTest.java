package com.pinb;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;

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
	
	public static void main(String[] args) throws UnsupportedEncodingException {
		String encodeStr="err_msg=%25BC%25D3%25D3%25CD%25BF%25A8%25B3%25E4%25D6%25B5%25B4%25CE%25CA%25FD%25B3%25AC%25CF%25DE&ordersuccesstime=20190523171840&sign=C179C31F3ECBAA3420EC23559253F7C7&sporder_id=T2019052300000270&userid=A1408596&ret_code=9&serial_no=%5Bnull%5D";
		System.out.println(new URLDecoder().decode(new URLDecoder().decode(encodeStr, "gbk"), "gbk"));;
	}

}
