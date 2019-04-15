/**
 * 
 */
package com.pinb.control;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.pinb.entity.GroupBar;
import com.pinb.service.GroupBarService;

/**
 * 
 * @author chenzhao @date Apr 15, 2019
 *
 */
@RestController
@RequestMapping("groupBar")
public class GroupBarControl {

	@Autowired
	private GroupBarService groupBarService;

	@RequestMapping("select")
	public Object select(@RequestBody GroupBar groupBar)  {
		return groupBarService.select(groupBar);
	}

	@RequestMapping("add")
	public Object add(@RequestBody GroupBar groupBar)  {

		return groupBarService.add(groupBar);
	}

	@RequestMapping("update")
	public Object update(@RequestBody GroupBar groupBar) {
		return groupBarService.update(groupBar);
	}

}
