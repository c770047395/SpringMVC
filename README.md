## SpringMVC的执行原理
![](https://blog.kuangstudy.com/usr/uploads/2019/10/2905833713.png)

![](https://blog.kuangstudy.com/usr/uploads/2019/10/63825124.png)

1. DispatcherServlet表示前置的控制器，即我们在web.xml中配置的唯一一个，是整个SpringMVC的控制中心，用户发出的请求都需要经过DispatcherServlet
- 假设请求的url为：http://localhost:8080/SrpingMVC/hello
- 如上url拆分成三部分：
   - http://localhost:8080服务器的域名
   - SpringMVC部署在服务器上的web站点
   - hello表示控制器
- 通过分析，如上的url表示为：请求位于服务器http://localhost:8080上的SpringMVC站点的hello控制器

2. HandlerMapping为处理器映射。DispatcherServlet调用HandlerMapping，HandlerMapping根据请求url查找Handler（这个HandlerMapping在Spring配置文件中通过bean标签生成）。

3. HandlerExcution表示具体的Handler，其主要作用是根据url查找控制器，如上url被查找的控制器为：hello。

4. HandlerExection将解析后的信息传递给DispatcherServlet，如解析控制器映射等。

5. HandlerAdapter表示处理器适配器，其按照特定的规则去执行Handler。

6. Handler让具体的Controller执行。

7. Controller将具体的执行结果信息返回给HandlerAdapter，如ModelAndView。（MVVM中使用ViewModel）

8. HandlerAdapter将视图逻辑名或模型传递给DispatcherServlet。

9. DispatcherServlet调用视图解析器（ViewResolver，在spring配置文件中也配置了）来解析HandlerAdapter传递的逻辑视图名。

10. 视图解析器将解析的逻辑视图名传给DispatcherServlet。

11. DispatcherServlet根据视图解析器解析的视图结果，调用具体的视图。

12. 最终呈现给用户。


