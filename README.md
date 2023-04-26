# 搜客-聚合搜索平台项目

> 作者：[猫十二懿](https://github.com/kongshier)


## 项目简介

基于 Vue 3 + Spring Boot + Elastic Stack 的一站式聚合搜索平台，也是简化版的企业级搜索中台。

对用户来说，使用该平台，可以在同一个页面集中搜索出不同来源、不同类型的内容，提升用户的检索效率和搜索体验。

对企业来说，当企业内部有多个项目的数据都存在搜索需求时，无需针对每个项目单独开发搜索功能，可以直接将各项目的数据源接入搜索中台，从而提升开发效率、降低系统维护成本。

## 项目系统架构
![image-20230410193659648](https://user-images.githubusercontent.com/94662685/230899037-edddf871-0763-4743-a0d6-5b367fb511ad.png)


### 技术选型

#### 前端

1. Vue 3
2. Ant Design Vue 组件库
3. 页面状态同步


#### 后端

1. Spring Boot 2.7 框架 + springboot-init 脚手架
2. MySQL 数据库（8.x 版本）
3. Elastic Stack
4. Elasticsearch 搜索引擎（重点）
5. Logstash 数据管道
6. Kibana 数据可视化
7. 数据抓取（jsoup、HttpClient 爬虫）
8. 离线
9. 实时
10. 设计模式
11. 门面模式
12. 适配器模式
13. 注册器模式
14. 数据同步（4 种同步方式）
15. 定时
16. 双写
17. Logstash
18. Canal
19. JMeter 压力测试


## 搜索文章

![image-20230410090901569](https://user-images.githubusercontent.com/94662685/230899202-7266499d-ef2f-43bc-aebe-519a0be0bf11.png)

## 搜索图片

![image-20230410193549909](https://user-images.githubusercontent.com/94662685/230899181-ed715d71-af56-42bd-9548-a7cb31999cf0.png)

## 搜索用户
![image-20230410193603060](https://user-images.githubusercontent.com/94662685/230899168-9886c42a-7876-4424-8fcd-9b7ee660eb0a.png)


先运行ES，再启动项目


