package com.pp.redis.service;

import com.pp.redis.entry.User;

public interface RedisCacheService {

	User getById(int id);

	User update(User users);
	
	void exit();
	
	int deleteById(int id);
}
