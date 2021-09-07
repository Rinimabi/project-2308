### Servlet和SpringIOC
- servlet是一个接口，一种JAVA规定的标准，主要包括**destroy()、init()、server()**等接口。
- servlet需要通过servlet容器进行部署，这就是tomcat。
- tomcat负责监听端口，分发请求到指定的servlet对象进行处理。
- 平常用的HttpServlet实际上实现了service()方法并对post、get等请求进行了判断分发。
- 所以继承HttpServlet要求你重写doGet或者doPost至少一个方法，让父类知道你要干嘛。
- spring的入口是**DispatcherServlet**，是一个servlet（配置文件有体现）。
- DispatcherServlet拦截了所有的请求，然后分发给对应的controller处理。
- DispatcherServlet提供了静态资源的访问方法（servlet拦截所有请求无法访问静态资源）。
- servlet映射规则由tomcat中的Mapper类进行分发。
- Mapper包含七大servlet，负责处理各种请求。
- DefaultServlet处理静态资源，InvokerServlet处理servlet，JspServlet处理JSP文件。

推荐看一下[自己实现一个SpringIOC](https://blog.csdn.net/qq_36582604/article/details/82630667)。