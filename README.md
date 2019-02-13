# springboot-reids-cache
使用redis注解形式，做缓存
## 开发工具
- sts 4.x
- maven
- mybatis-plus
- jdk 1.8
- redis 
- springboot 2.x
## 介绍
  springboot 2.x 整合 redis 做缓存处理。主要包括
  - 查询，直接将数据存入到缓存中
  - 更新，如果数据已经存在于缓存中，则更新缓存中对应的数据，若不存在 ，则将更新后的数据放入缓存中
  - 删除，将数据库中的数据删除成功后，再将缓存中的数据删除
  - 退出，清空所有缓存
  
