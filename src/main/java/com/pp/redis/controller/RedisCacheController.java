package com.pp.redis.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.pp.redis.entry.User;
import com.pp.redis.service.RedisCacheService;

/**
 * 
 * @author pp
 * @Date 2019年2月13日下午4:02:31
 * @Description
 *
 */
@RestController
public class RedisCacheController {

	@Autowired
	private RedisCacheService redisCacheService;

	@RequestMapping("/u2/{id}")
	public User demo2(@PathVariable Integer id) {
		User user = redisCacheService.getById(id);
		return user;
	}

	@PutMapping("/update")
	public User update() {
		User user = new User();
		user.setId(12);
		user.setName("ppyy1234");
		return redisCacheService.update(user);
	}

	@RequestMapping("/exit")
	public void exit() {
		redisCacheService.exit();
	}

	@DeleteMapping("/delete/{id}")
	public int delete(@PathVariable Integer id) {
		int i = redisCacheService.deleteById(id);
		return i;
	}
}
