## Autodidact社区
## 实现的功能和技术
- 采用 SpringMVC架构处理请求，业务处理，以及Thymeleaf模板引擎实现页面显示。
- 采用 OpenResty(Nginx + LUA 升级版) 配合 redis + lua 进行限流防刷
- 基于 RocketMQ 发送事务消息保证原子性
- 采用 Cookie存储验证码、浏览历史信息，以及Session存储用户数据。
- 采用拦截器调用 Session存储用户信息和消息未读数的更新。
- 基于 jQuery和ajax实现和服务端异步传输数据来发送和校验验证码
- 基于 MyBatis实现对数据库的增删改查(已改成MySQL)。
- 基于 BootStrap、CSS 优化页面
- 调用阿里云、QQ 接口以及 Github接口实现第三方登录和编写 Markdown和上传图片
- 采用Git将项目上传到 Github


## 快速运行
1. 安装必备工具  
JDK，Maven，OpenResty，Redis，RocketMQ
2. OpenResty配置：resources-> script
3. 运行打包命令
```sh
mvn package
```
4. 运行项目  
```sh
java -jar target/community-0.0.1-SNAPSHOT.jar
```
5. 访问项目
```
http://localhost:8887
```


## 资料
[OpenResty 安装](https://blog.csdn.net/qq_21040559/article/details/122942568)
[Spring 文档](https://spring.io/guides)    
[Spring Web](https://spring.io/guides/gs/serving-web-content/)   
[es](https://elasticsearch.cn/explore)    
[Github deploy key](https://developer.github.com/v3/guides/managing-deploy-keys/#deploy-keys)    
[Bootstrap](https://v3.bootcss.com/getting-started/)    
[Github OAuth](https://developer.github.com/apps/building-oauth-apps/creating-an-oauth-app/)    
[Spring](https://docs.spring.io/spring-boot/docs/2.0.0.RC1/reference/htmlsingle/#boot-features-embedded-database-support)    
[菜鸟教程](https://www.runoob.com/mysql/mysql-insert-query.html)    
[Thymeleaf](https://www.thymeleaf.org/doc/tutorials/3.0/usingthymeleaf.html#setting-attribute-values)    
[Spring Dev Tool](https://docs.spring.io/spring-boot/docs/2.0.0.RC1/reference/htmlsingle/#using-boot-devtools)  
[Spring MVC](https://docs.spring.io/spring/docs/5.0.3.RELEASE/spring-framework-reference/web.html#mvc-handlermapping-interceptor)  
[Markdown 插件](http://editor.md.ipandao.com/)   
[UFfile SDK](https://github.com/ucloud/ufile-sdk-java)  
[Count(*) VS Count(1)](https://mp.weixin.qq.com/s/Rwpke4BHu7Fz7KOpE2d3Lw)  

## 工具
[Git](https://git-scm.com/download)   
[Visual Paradigm](https://www.visual-paradigm.com)    
[Flyway](https://flywaydb.org/getstarted/firststeps/maven)  
[Lombok](https://www.projectlombok.org)    
[ctotree](https://www.octotree.io/)   
[Table of content sidebar](https://chrome.google.com/webstore/detail/table-of-contents-sidebar/ohohkfheangmbedkgechjkmbepeikkej)    
[One Tab](https://chrome.google.com/webstore/detail/chphlpgkkbolifaimnlloiipkdnihall)    
[Live Reload](https://chrome.google.com/webstore/detail/livereload/jnihajbhpnppcggbcgedagnkighmdlei/related)  
[Postman](https://chrome.google.com/webstore/detail/coohjcphdfgbiolnekdpbcijmhambjff)



