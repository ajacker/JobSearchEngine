# JobSearchEngine
ElasticSearch+springboot+webmagic

## 技术栈
Elasticsearch+Springboot+WebMagic+redis+bootstrap+docker

## 主要功能
### 爬虫模块
- 分布式
- 多线程
- 代理池
- 监视面板/控制面板
- 定时任务

### 搜索引擎
- 复合查询
- 范围查询
- 分页查询
- 结果排序
- 节点状态监控

## docker
使用docker-compse完成一键式部署，使用到的services：
- registry.cn-hangzhou.aliyuncs.com/ajacker/elasticsearch-iksmart-apline:7.4.2或ajacker/elasticsearch-iksmart-apline:7.4.2（[项目链接](https://github.com/ajacker/docker-elasticsearch-iksmart)）
- jhao104/proxy_pool （代理池）
- redis:alpine
