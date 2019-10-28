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

## HelloSpringMVC的实现
1. 新建一个项目后，修改web.xml，添加DispatcherServlet
```xml
<?xml version="1.0" encoding="UTF-8"?>
<web-app xmlns="http://xmlns.jcp.org/xml/ns/javaee"
         xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
         xsi:schemaLocation="http://xmlns.jcp.org/xml/ns/javaee http://xmlns.jcp.org/xml/ns/javaee/web-app_4_0.xsd"
         version="4.0">

<!--    配置DsipacherServlet：这个是SpringMVC的核心；请求分发器，前端控制器-->
    <servlet>
        <servlet-name>springmvc</servlet-name>
        <servlet-class>org.springframework.web.servlet.DispatcherServlet</servlet-class>
        <!--DispatcherServlet要绑定Spring的配置文件-->

        <init-param>
            <param-name>contextConfigLocation</param-name>
            <param-value>classpath:springmvc-servlet.xml</param-value>
        </init-param>
        <!-- 启动级别：1-->
        <load-on-startup>1</load-on-startup>
    </servlet>

    <servlet-mapping>
        <servlet-name>springmvc</servlet-name>
        <url-pattern>/</url-pattern>
    </servlet-mapping>

</web-app>
```

2. 在配置文件中引入HandlerMapping（处理器映射器）、HandlerAdapter（处理器适配器）以及ViewResolover（视图解析器）
```xml
<?xml version="1.0" encoding="UTF-8"?>
<beans xmlns="http://www.springframework.org/schema/beans"
       xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
       xsi:schemaLocation="http://www.springframework.org/schema/beans http://www.springframework.org/schema/beans/spring-beans.xsd">

    <!--处理器映射器-->
    <bean class="org.springframework.web.servlet.handler.BeanNameUrlHandlerMapping"/>
    <!--处理器适配器-->
    <bean class="org.springframework.web.servlet.mvc.SimpleControllerHandlerAdapter"/>
    <!--视图解析器:模板引擎 Thymeleaf Freemarker-->
    <bean class="org.springframework.web.servlet.view.InternalResourceViewResolver" id="internalResourceViewResolver">
<!--        前缀-->
        <property name="prefix" value="/WEB-INF/jsp/"/>
<!--        后缀-->
        <property name="suffix" value=".jsp"/>
    </bean>


</beans>
```

3. 编写Controller类（调用接口方式）
```java
public class HelloController implements Controller {
    public ModelAndView handleRequest(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) throws Exception {
        ModelAndView mv = new ModelAndView();

        //业务代码
        String result = "HelloSpringMVC";

        mv.addObject("msg",result);

        //视图跳转
        mv.setViewName("test");


        return mv;
    }
}
```

4. 将Controller注册到Spring配置文件中
```xml
<bean id="/hello" class="com.cp.controller.HelloController"/>
```

这样就完成了一个SpringMVC的配置，此时我们在访问项目地址后加上/hello就会让对应的Controller处理，但是这样配置还是有些麻烦，所以工作中我们一般不这样配置Controller，而是使用更方便的注解进行配置

## 注解实现SpringMVC
1. 首先还是要编写web.xml文件，与之前的相同
```xml
<?xml version="1.0" encoding="UTF-8"?>
<web-app xmlns="http://xmlns.jcp.org/xml/ns/javaee"
         xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
         xsi:schemaLocation="http://xmlns.jcp.org/xml/ns/javaee http://xmlns.jcp.org/xml/ns/javaee/web-app_4_0.xsd"
         version="4.0">
    <servlet>
        <servlet-name>SpringMVC</servlet-name>
        <servlet-class>org.springframework.web.servlet.DispatcherServlet</servlet-class>
        <init-param>
            <param-name>contextConfigLocation</param-name>
            <param-value>classpath:springmvc-servlet.xml</param-value>
        </init-param>
        <load-on-startup>1</load-on-startup>
    </servlet>

    <servlet-mapping>
        <servlet-name>SpringMVC</servlet-name>
        <url-pattern>/</url-pattern>
    </servlet-mapping>
</web-app>
```

2. 编写springmvc-servlet.xml配置文件
```xml
<?xml version="1.0" encoding="UTF-8"?>
<beans xmlns="http://www.springframework.org/schema/beans"
       xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
       xmlns:context="http://www.springframework.org/schema/context"
       xmlns:mvc="http://www.springframework.org/schema/mvc"
       xsi:schemaLocation="http://www.springframework.org/schema/beans
        http://www.springframework.org/schema/beans/spring-beans.xsd
        http://www.springframework.org/schema/context
        https://www.springframework.org/schema/context/spring-context.xsd
        http://www.springframework.org/schema/mvc
        https://www.springframework.org/schema/mvc/spring-mvc.xsd">

    <!-- 自动扫描包，让指定包下的注解生效,由IOC容器统一管理 -->
    <context:component-scan base-package="com.cp.controller"/>
    <!-- 让Spring MVC不处理静态资源 -->
    <mvc:default-servlet-handler />
    <!--
    支持mvc注解驱动
        在spring中一般采用@RequestMapping注解来完成映射关系
        要想使@RequestMapping注解生效
        必须向上下文中注册DefaultAnnotationHandlerMapping
        和一个AnnotationMethodHandlerAdapter实例
        这两个实例分别在类级别和方法级别处理。
        而annotation-driven配置帮助我们自动完成上述两个实例的注入。
     -->
    <mvc:annotation-driven />

    <!-- 视图解析器 -->
    <bean class="org.springframework.web.servlet.view.InternalResourceViewResolver"
          id="internalResourceViewResolver">
        <!-- 前缀 -->
        <property name="prefix" value="/WEB-INF/jsp/" />
        <!-- 后缀 -->
        <property name="suffix" value=".jsp" />
    </bean>

</beans>
```
HandlerMapping（处理器映射器）、HandlerAdapter（处理器适配器），ViewResolver（视图解析器）是SpringMVC中不可或缺的部分，我们除了手动去注入以外，还可以使用``<mvc:annotation-driven />``自动添加HandlerMapping以及HandlerAdapter，通过``    <context:component-scan base-package="com.cp.controller"/>``开启注解支持，这在spring中就使用过了。除此之外，还需要使用``    <mvc:default-servlet-handler />``让SpringMVC不会处理静态资源

这样一来，SpringMVC的配置文件就变得十分简洁：
```xml
<?xml version="1.0" encoding="UTF-8"?>
<beans xmlns="http://www.springframework.org/schema/beans"
       xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
       xmlns:context="http://www.springframework.org/schema/context"
       xmlns:mvc="http://www.springframework.org/schema/mvc"
       xsi:schemaLocation="http://www.springframework.org/schema/beans
        http://www.springframework.org/schema/beans/spring-beans.xsd
        http://www.springframework.org/schema/context
        https://www.springframework.org/schema/context/spring-context.xsd
        http://www.springframework.org/schema/mvc
        https://www.springframework.org/schema/mvc/spring-mvc.xsd">


    <context:component-scan base-package="com.cp.controller"/>
    <mvc:default-servlet-handler />
    <mvc:annotation-driven />

    <!-- 视图解析器 -->
    <bean class="org.springframework.web.servlet.view.InternalResourceViewResolver"
          id="internalResourceViewResolver">
        <!-- 前缀 -->
        <property name="prefix" value="/WEB-INF/jsp/" />
        <!-- 后缀 -->
        <property name="suffix" value=".jsp" />
    </bean>

</beans>
```

3. 编写Controller（使用注解方式）
```java
@Controller
public class HelloController {

    @RequestMapping("/hello")
    public String hello(Model model){
        // 封装数据
        model.addAttribute("msg","hello,SpringMVCAnnotation");
        return "hello"; //会被视图解析器处理
    }

}

```
   - 使用@Controller注解将类注册为Controller
   - 通过``return "hello"``指定我们要返回的view页面
   - 通过model参数实现值的传递，2、3两步相当于之前的ModelAndView返回给HandlerAdapter，拿给DispatcherServlet解析
   - 使用@RequestMapping("/hello")注解将方法与url绑定，当访问项目下的/hello时，HandlerMapping就能找到这个方法


## SpringMVC接收参数

### 一般参数
在参数以普通GET或者POST方式传来时，我们在方法上添加参数即可接收（注意方法中的参数名要与传递的参数名相同）
例如：
- 前端请求：http://localhost:8080/hello?a=1&b=2
- Controller处理：
```java
@Controller
public class RestfulController {
    @RequestMapping("/add")
    public String test1(int a,int b, Model model){
        int res = a+b;
        model.addAttribute("msg","结果为"+res);
        return "hello";
    }
}
```

### RESTful风格
- 前端请求：http://localhost:8080/hello/1/2
- Controller处理：
```java
@Controller
public class RestfulController {
    @RequestMapping(value = "/add/{a}/{b}", method = RequestMethod.GET)
    public String test1(@PathVariable int a,@PathVariable int b, Model model){
        int res = a+b;
        model.addAttribute("msg","结果为"+res);
        return "hello";
    }
}
```

此时，这个Controller只能处理GET方式的请求。（ps：要在参数前面加@PathVariable代表是路径参数，可以将url与参数对应）
常见的RESTful请求类型有：
- GET：用于查询
- POST：用于新增
- PUT：用于更新
- DELETE：用于删除
对应的RequestMapping有：
- @GetMapping
- @PostMapping
- @PutMapping
- @DeleteMapping

此时我们可以这样加注解：
```java
@Controller
public class RestfulController {
//    @RequestMapping(value = "/add/{a}/{b}", method = RequestMethod.GET)
    @GetMapping("/add/{a}/{b}")    
    public String test1(@PathVariable int a,@PathVariable int b, Model model){
        int res = a+b;
        model.addAttribute("msg","结果为"+res);
        return "hello";
    }
}
```
这样与之前使用RequestMapping的效果完全相同，但是代码简化了很多