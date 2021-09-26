#### 双亲委派机制的作用
- **classLoader**它是用来加载 Class 的，它相当于类的命名空间，起到了类隔离的作用。
- 字节码加密技术就是依靠定制 ClassLoader 来实现
- BootstrapClassLoader、ExtensionClassLoader、AppClassLoader
- 双亲委派：AppClassLoader 在加载一个未知的类名时会先让ExtensionClassLoader去加载，ExtensionClassLoader也会尝试先让BootstrapClassLoader去加载，当都加载不了才会去搜索classpath。
- AppClassLoader 可以由 ClassLoader 类提供的静态方法 getSystemClassLoader() 得到，它就是我们所说的「系统类加载器」
- 每个Class对象的内部都有一个 classLoader 字段来标识自己是由哪个ClassLoader加载的
- 当 parent 字段是 null 时就表示它的父加载器是「根加载器」
- 当需要加载一个未知的类时，会优先使用调用者的ClassLoader去尝试加载
- Class.forName的原理是 mysql 驱动的 Driver 类里有一个静态代码块，它会在 Driver 类被加载的时候执行。
- forName 方法同样也是使用调用者 Class 对象的 ClassLoader 来加载目标类
- 使用 ClassLoader 可以解决钻石依赖问题。不同版本的软件包使用不同的 ClassLoader 来加载，位于不同 ClassLoader 中名称一样的类实际上是不同的类。

[老大难的 Java ClassLoader 再不理解就老了](https://zhuanlan.zhihu.com/p/51374915)
#### spring的加载过程
##### springboot的启动过程
[SpringBoot启动流程是怎样的？](https://juejin.cn/post/6895341123816914958)
##### bean的加载过程
- bean的读取：bean扫描 -> bean封装 -> bean注册 -> bean增强
![bean加载图](https://img-blog.csdnimg.cn/20210221174830700.png?x-oss-process=image/watermark,type_ZmFuZ3poZW5naGVpdGk,shadow_10,text_aHR0cHM6Ly9ibG9nLmNzZG4ubmV0L2E3NDUyMzM3MDA=,size_16,color_FFFFFF,t_70)
[Spring的Bean加载流程](https://blog.csdn.net/a745233700/article/details/113840727)
##### Cglib和JDK Proxy有什么不同
- Proxy是JDK自带的功能
- Proxy需要实现InvocationHandler接口，然后调用invoke方法，调用newProxyInstance类实现动态代理
- CGLib代理无需继承接口，它会直接继承代理类并重写它进行增强，所以final类型没法代理
- CGLib直接操作字节码生成代理类，效率比proxy要高（它通过反射创建）
- Spring AOP会根据你是否实现InvocationHandler接口来决定使用何种代理模式
##### IOC和AOP
- Inversion of Control，将原本需要自己去*new*出来的对象和类调用关系，变成了交给spring帮你去管理（@service、@controller、@component），你只管取就行（@resource）。
#### mq的死信队列
- 没有被及时消费的消息存放的队列
- 消息被拒绝（basic.reject/ basic.nack）并且不再重新投递 requeue=false
- TTL(time-to-live) 消息超时未消费
- 达到最大队列长度
- 消息变成死信后，会被重新投递（publish）到另一个交换机上（Exchange）
- 监听该队列就可以被重新消费
- 可以使用死信队列监听订单支付时间过期，避免大量定时任务或者轮询数据库
#### docker的linux内部实现原理
#### spring是如何实现事务隔离的
#### threadLocal的内部实现
#### synchronize和lock的区别
#### springcloud有哪些组成
#### hystrix如何实现限流和熔断
#### redisson是如何实现分布式锁的
#### redis如何进行性能优化
[一些比较好的Redis 性能优化思路总结](https://mp.weixin.qq.com/s/mf9ZmQDrp7PCnxVFIDaWFQ)
#### 内部类为什么要使用静态内部类