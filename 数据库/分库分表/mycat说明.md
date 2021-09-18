推荐读一下[MyCat指南](http://www.mycat.org.cn/document/mycat-definitive-guide.pdf)，里面对数据库分片、mysql的分库分表、处理方式都作了很好的分析。

## 1.如何搭建主从数据库和读写分离？
按照[CentOS下mysql多实例操作指南](https://blog.51cto.com/13799042/2126621)构建主从数据库后，再按照[MySQL主从复制](https://www.jianshu.com/p/4541a68d16d7)进行从库的slave配置，需要注意：
```ini
MASTER_USER='root',
MASTER_PASSWORD='123456',
MASTER_LOG_FILE='**mysql-bin.000007**',
MASTER_LOG_POS=**1539**;
```
这两个参数要与主库一致，主库通过**show master status**命令查询。还有用户密码，是主库授权给别人访问的用户密码，而不是主库登录用户的用户密码。

## 1.如何使用MyCat配置读写分离和分片？
参考[MyCat学习笔记](https://www.jianshu.com/p/c8973edc550a)。
关于分片规则，都可以看官网的指南，里面讲得非常清楚。比如**固定分片 hash 算法**：
```ini
	<function name="func1" class="io.mycat.route.function.PartitionByLong">
		<property name="partitionCount">2,1</property>
		<property name="partitionLength">256,512</property>
	</function>
```
这里将三个分片配置为2:1，对应数量256,512，存储数据的时候就会按照这个比例和数目去取模，0-256存在第一个库，256-512存第二个库，成倍数就按倍数取，以此类推。