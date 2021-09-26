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
##### bean的加载过程
##### 如何实现一个starter
##### cglib和porxy有什么不同
##### IOC和AOP
- Inversion of Control，将原本需要自己去*new*出来的对象和类调用关系，变成了交给spring帮你去管理（@service、@controller、@component），你只管取就行（@resource）。
#### mq的死信队列
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