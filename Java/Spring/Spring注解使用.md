##### @ConditionalOnExpression
条件注解允许我们将条件指定为有效的 SpEL 表达式，根据 SpEL 表达式的计算结果来判断条件是否匹配。在 application.properties 文件中添加一些配置信息，然后在 @Configuration 类上面使用 *@ConditionalOnExpression* 注解通过 SpEL 表达式来获取这些配置信息，然后进行判断。这样就可以做一些差异化的bean注入。
[@ConditionalOnExpression 注解](https://www.hxstrive.com/subject/spring_boot.htm?id=477)

##### @RefreshScope
##### @ConfigurationProperties(prefix = "a.b")
##### @ConfigurationProperties(prefix = "a.b")