### RPC(Remote Procedure Call)
- 它是一种设计模式
- 为了完成应用间的调用
- 以方法形式调用符合抽象方式
- 包括传输协议和序列化协议
- 传输协议包括HTTP、gRPC等
- 序列化协议包括JSON、Protobuf、Hessian
- 通过动态代理实现无感知调用
### Http协议和Dubbo协议
- dubbo和Http都是一种应用层协议
- dubbo框架除了dubbo协议也可以使用http协议
- dubbo框架包括Transporter(Netty)、Serialization(Dubbo/JSON)、Dispatcher(All)、ThreadPool四个部分
- Dubbo缺省协议采用单一长连接和NIO异步通讯，适合于小数据量大并发的服务调用，以及服务消费者机器数远大于服务提供者机器数的情况
- Dubbo缺省协议不适合传送大数据量的服务，比如传文件，传视频等，除非请求量很低。
#### 为什么采用异步单一长连接?
- 因为服务的现状大都是服务提供者少，通常只有几台机器，而服务的消费者多，可能整个网站都在访问该服务，比如Morgan的提供者只有6台提供者，却有上百台消费者，每天有1.5亿次调用，如果采用常规的hessian服务，服务提供者很容易就被压跨，通过单一连接，保证单一消费者不会压死提供者，长连接，减少连接握手验证等，并使用异步IO，复用线程池，防止C10K问题。
### 负载均衡方案
- **集中式负载均衡方案**
服务端和客户端之间有一个独立的负载均衡器进行注册和转发。
- **进程内负载均衡方案**
该方案将负载均衡处理功能以库的方式整合到服务消费者应用中，也称为客户端负载均衡方案，各个客户端自行获取服务器列表并进行转发。
- **主机独立负载均衡方案**
### Fegin
- 为了实现微服务之间的声明式调用
- 扫描@FeignClient注解的类，做动态增强并注册到IOC中，被调用时进行远程访问并返回结果，实现RPC式调用。
- 底层是HTTP协议调用
- 使用的是JDK Proxy代理模式
- InvocationHandler(调用处理器)负责转发调用MethodHandler
- MethodHandler负责封装方法进行调用，使用HttpTemplate调用，并完成序列化和反序列化
- fegin使用ribbon实现负载均衡
### Ribbon
- Ribbon 是一个基于 HTTP 和 TCP 客户端的负载均衡器它可以在客户端配置 *ribbonServerList*（服务端列表），然后轮询请求以实现均衡负载它在联合 Eureka 使用时ribbonServerList 会被 *DiscoveryEnabledNIWSServerList* 重写，扩展成从 Eureka 注册中心获取服务端列表同时它也会用 NIWSDiscoveryPing 来取代 IPing，它将职责委托给 Eureka 来确定服务端是否已经启动。
- *@LoadBalanced*打在RestTemplate上
- ribbon会基于某种规则去转发Load Balancer注册的所有机器
- 提供了多种策略，例如轮询、随机、根据响应时间加权等
### Hystrix
- 用作熔断流量控制，为了防止某个微服务的故障消耗掉整个系统所有微服务的连接资源
- Hystrix具备了服务降级、服务熔断、线程隔离、请求缓存、请求合并以及服务监控等强大功能
- ribbon负责重试，hystrix负责超时，关系：*Hystrix的超时时间=Ribbon的重试次数(包含首次) * (ribbon.ReadTimeout + ribbon.ConnectTimeout)*
### 
### 参考资料
[Feign Ribbon Hystrix 三者关系](https://www.cnblogs.com/crazymakercircle/p/11664812.html)
[springCloud负载均衡Ribbon和Feign的区别](https://www.cnblogs.com/xiaofeng-fu/p/12119125.html)
[Feign原理 （图解）](https://www.cnblogs.com/crazymakercircle/p/11965726.html)
[SpringCloud微服务架构篇4：负载均衡Ribbon与调用Feign](https://zhuanlan.zhihu.com/p/143544718)
[Dubbo协议介绍](https://www.jianshu.com/p/32edaf9736a2)
[通俗易懂 RPC、REST、Dubbo、HTTP、RMI 的区别与联系](https://zhuanlan.zhihu.com/p/66311537)