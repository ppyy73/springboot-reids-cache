package com.pp.redis.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import com.pp.redis.entry.User;
import com.pp.redis.mapper.UserMapper;
import com.pp.redis.service.RedisCacheService;

/**
 * 
 * @author pp
 * @Date 2019年2月13日下午3:53:35
 * @Description 缓存操作
 *
 */
@Service
public class RedisCacheServiceImpl implements RedisCacheService {

	@Autowired
	private UserMapper userMapper;

	/**
	 * 	将查询到的对象放入到缓存中
	 * 	第一次查询 从数据库中查询 并显示查询所消耗的时间 
	 * 	第二次 直接从缓存中查询 不显示查询时间
	 */
	@Override
	@Cacheable(value = "user", key = "#id")
	public User getById(int id) {
		System.out.println(System.currentTimeMillis());
		User user = userMapper.selectById(id);
		System.out.println(System.currentTimeMillis());
		return user;
	}

	/**
	 * 	更新信息，并将缓存中的信息一并更新
	 * 	如果缓存中没有对象，会默认把对象添加到缓存中
	 * 	其中：CachePut 和 Cacheable 中
	 * 	value 和 key 必须完全一样
	 */
	@Override
	@CachePut(value = "user", key = "#users.getId()")
	public User update(User users) {
		System.out.println("调用更新方法");
		int i = userMapper.updateById(users);
		if (i > 0) {
			User user = userMapper.selectById(users.getId());
			return user;
		} else {
			return null;
		}
	}

	/**
	 *	 退出 清空所有缓存
	 */
	@Override
	@CacheEvict(value = "user", allEntries = true)
	public void exit() {
		System.out.println("退出");
	}
	/**
	 * 	删除对象 并在缓存中删除相应的对象
	 */
	@Override
	@CacheEvict(value = "user", key = "#id")
	public int deleteById(int id) {
		System.out.println("调用删除方法");
		int i = userMapper.deleteById(id);
		return i;
	}
}
