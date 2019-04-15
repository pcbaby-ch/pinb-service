/**
 * 
 */
package com.pinb.control;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.pinb.entity.GroubActivity;
import com.pinb.service.GroubActivityService;

/**
 * 
 * @author chenzhao @date Apr 15, 2019
 *
 */
@RestController
@RequestMapping("groubActivity")
public class GroubActivityControl {

	@Autowired
	private GroubActivityService groubActivityService;

	@RequestMapping("select")
	public Object select(@RequestBody GroubActivity groubActivity)  {
		return groubActivityService.select(groubActivity);
	}

	@RequestMapping("add")
	public Object add(@RequestBody GroubActivity groubActivity)  {

		return groubActivityService.add(groubActivity);
	}

	@RequestMapping("update")
	public Object update(@RequestBody GroubActivity groubActivity) {
		return groubActivityService.update(groubActivity);
	}

}
