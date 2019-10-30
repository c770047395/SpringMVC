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

如果方法中的名字与前端传来的参数名不相同，我们可以通过@RequestParam注解标记
```java
//前端参数与接收参数不同，使用@RequestParam("xx")注解  ps：最好前端接收的参数都使用
//localhost：8080/user/t1?username=xxxx
@GetMapping("t2")
public String test1(@RequestParam("username") String name, Model model){
    //1.接收前端参数
    System.out.println("get param:"+name);
    //2.将返回的结果传递给前端
    model.addAttribute("msg",name);

    return "hello";
}
```

如果前端传递的是一个对象表单（例如这里使用的是user表单，则可以直接用这个对象当作方法的入参）
```java
//前端接受的参数是一个对象
@RequestMapping("/t3")
public String test2(User user, Model model){
    //1.接收前端参数
    System.out.println(user);
    //2.将返回的结果传递给前端
    model.addAttribute("msg",user);
    return "hello";
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

## SpringMVC结果返回
在使用servlet的时候，我们可以获取HttpServletRequest和HttpServletResponse进行转发与重定向，在SpringMVC中也有转发与重定向：
### 使用HttpServletRequest和HttpServletResponse进行转发与重定向
```java
    //使用Servlet原生API进行转发&重定向
    @RequestMapping("/m1/t1")
    public void test(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession();
        System.out.println(session.getId());
        session.setAttribute("msg","helloooooooo");
//        return("hello");
        request.getRequestDispatcher("/WEB-INF/jsp/hello.jsp").forward(request,response);
//        response.sendRedirect("/index.jsp");
    }
```
这样就实现了在不使用视图解析器的情况下进行转发与重定向

### 使用SpringMVC无视图解析器进行转发与重定向
```java
//SpringMVC不使用视图解析器时，默认调用原生ServletAPI进行转发与重定向
//return ("forward:/xxx/xxx/")转发
//return ("redirect:/XXX/XXX/")重定向
@RequestMapping("/m1/t2")
public String test1(Model model){
    model.addAttribute("msg","helllllll");
//    return("forward:/WEB-INF/jsp/hello.jsp");
    return("redirect:/WEB-INF/jsp/hello.jsp");
}
```
### 使用SpringMVC并且使用视图解析器时
```java
//SpringMVC使用视图解析器时
//return ("xxx")转发
//return ("redirect:/XXX.jsp")重定向
@RequestMapping("/m1/t3")
public String test2(Model model){
    model.addAttribute("msg","xxxxxxxx");
//        return("redirect:/index.jsp");
    return("redirect:/m1/t4");
}

@RequestMapping("/m1/t4")
public String test3(Model model){
    model.addAttribute("msg","重定向页面");
    return("hello");
}
```
转发只要输入文件名就会自动加上前后缀，重定向则需要输入redirect:来辨别这是一个重定向（ps：重定向不会进行拼接，所以要使用绝对路径）

返回的结果我们一般封装在Model中，与Model类似的还有ModelAndView以及ModelMap
- Model：精简版的ModelMap，几乎都用这个
- ModelMap：继承了LinkedHashMap，有LinkedHashMap的特性
- ModelAndView：在继承Controller接口时使用，在储存数据的同时也将设置返回视图逻辑

## 乱码问题解决
在web.xml中配置过滤器即可，SpringMVC中自带了过滤器
```xml
<filter>
    <filter-name>encoding</filter-name>
    <filter-class>org.springframework.web.filter.CharacterEncodingFilter</filter-class>
    <init-param>
        <param-name>encoding</param-name>
        <param-value>utf-8</param-value>
    </init-param>
</filter>
<filter-mapping>
    <filter-name>encoding</filter-name>
    <url-pattern>/*</url-pattern>
</filter-mapping>
```

## 使用Json

### jackson的使用
1. 引入jar包
```xml
<dependency>
    <groupId>com.fasterxml.jackson.core</groupId>
    <artifactId>jackson-databind</artifactId>
    <version>2.10.0</version>
</dependency>
```

2. 解决中文乱码问题

   - 在RequestMapping中加入produces参数
    ```java
    @RequestMapping(value = "/j1", produces = "application/json;charset=utf-8")//解决乱码
    //    @ResponseBody //加了这个注解或者使用RestController就不会走视图解析器，会直接返回一个字符串
    public String json1() throws JsonProcessingException {
    
        ObjectMapper mapper = new ObjectMapper();
        User user = new User("陈鹏",3,"男");
    
        String string = mapper.writeValueAsString(user);
        return(string);
    }
    ```
   - 在springmvc配置文件中加入以下配置：
    ```xml
    <mvc:annotation-driven>
        <mvc:message-converters register-defaults="true">
            <bean class="org.springframework.http.converter.StringHttpMessageConverter">
                <constructor-arg value="UTF-8"/>
            </bean>
            <bean class="org.springframework.http.converter.json.MappingJackson2HttpMessageConverter">
                <property name="objectMapper">
                    <bean class="org.springframework.http.converter.json.Jackson2ObjectMapperFactoryBean">
                        <property name="failOnEmptyBeans" value="false"/>
                    </bean>
                </property>
            </bean>
        </mvc:message-converters>
    </mvc:annotation-driven>
    ```

3. 编写jackson工具类
```java
public class JsonUtil {

    public static String getJson(Object object){
        return getJson(object,"yyyy-MM-dd HH:mm:ss");
    }
    public static String getJson(Object object,String format){
        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS,false);
        SimpleDateFormat sdf = new SimpleDateFormat(format);
        mapper.setDateFormat(sdf);
        try {
            return mapper.writeValueAsString(object);
        } catch (JsonProcessingException e) {
            e.printStackTrace();

        }
        return null;
    }
}
```

4. 将控制器注册为RestController或者在每个类上都加上@ResponseBody注解（ps：推荐第一种，因为使用RESTful之后前后端分离，返回的都是数据）
```java
@RestController
//@Controller
public class UserController {
//    @RequestMapping(value = "/j1", produces = "application/json;charset=utf-8")//解决乱码
    @RequestMapping("/j1")//在配置文件中的annotation-driven配置解决乱码
//    @ResponseBody //加了这个注解或者使用RestController就不会走视图解析器，会直接返回一个字符串
    public String json1() throws JsonProcessingException {

        ObjectMapper mapper = new ObjectMapper();
        User user = new User("陈鹏",3,"男");

        String string = mapper.writeValueAsString(user);
        return(string);
    }
    @RequestMapping("/j2")
    public String json2() throws JsonProcessingException {

        ObjectMapper mapper = new ObjectMapper();
        List<User> userList = new ArrayList<User>();
        User user1 = new User("陈鹏2",3,"男");
        User user2 = new User("陈鹏3",3,"男");
        User user3 = new User("陈鹏4",3,"男");
        User user4 = new User("陈鹏5",3,"男");
        userList.add(user1);
        userList.add(user2);
        userList.add(user3);
        userList.add(user4);
        String string = mapper.writeValueAsString(userList);
        return(string);
    }

    @RequestMapping("/j3")
    public String json3() throws JsonProcessingException {

        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS,false);
        Date date = new Date();

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        mapper.setDateFormat(sdf);

//        String string = mapper.writeValueAsString(sdf.format(date));
        String string = mapper.writeValueAsString(date);
        return(string);
    }

    @RequestMapping("/j4")
    public String json4()  {

        User cp = new User("cpasp",1,"nan");
        Date date = new Date();
        return JsonUtil.getJson(date);

    }



}

```
其中， json1、2、3为没有使用工具类的，json4使用了工具类。

### fastjson的使用
fastjson相当于我们封装后的jackson，比较容易上手
1. 引入jar包
```xml
<dependency>
    <groupId>com.alibaba</groupId>
    <artifactId>fastjson</artifactId>
    <version>1.2.60</version>
</dependency>
```
2. 直接就可以使用了
```java
@RequestMapping("/j5")
public String json5()  {

    List<User> userList = new ArrayList<User>();
    User user1 = new User("陈鹏2",3,"男");
    User user2 = new User("陈鹏3",3,"男");
    User user3 = new User("陈鹏4",3,"男");
    User user4 = new User("陈鹏5",3,"男");
    userList.add(user1);
    userList.add(user2);
    userList.add(user3);
    userList.add(user4);
    String string = JSON.toJSONString(userList);
    return string;
}
```
注：
- json转对象用JSON.parseObjct(json)

## 拦截器
SpringMVC中提供了拦截器接口，继承HandlerInterceptor接口即可配置一个拦截器
HandlerInterceptor提供了三个方法：
- preHandler ：在拦截方法前执行，通过返回值判断是否执行下一步，返回值为true则继续执行，返回值为false则拦截
- postHandler ： 在拦截方法后执行（一般用于日志）
- afterCompletion ： 渲染后处理（一般用于日志）
拦截器通过AOP的思想实现横切拦截请求
!()[https://img-blog.csdnimg.cn/20190701162842270.png?x-oss-process=image/watermark,type_ZmFuZ3poZW5naGVpdGk,shadow_10,text_aHR0cHM6Ly9ibG9nLmNzZG4ubmV0L3FxXzQwMzIzMjU2,size_16,color_FFFFFF,t_70]

**例子**

```java
public class MyInterceptor implements HandlerInterceptor {
    // return true 执行下一个拦截器，放行
    // return false 不执行下一个拦截器
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        System.out.println("处理前====================");
        return true;
    }

    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
        System.out.println("处理后====================");

    }

    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        System.out.println("清理====================");

    }
}
```
```xml
<mvc:interceptors>
    <mvc:interceptor>
        <mvc:mapping path="/**"/>
        <bean class="com.cp.config.MyInterceptor"/>
    </mvc:interceptor>
</mvc:interceptors>
```
这样会将所有的请求都拦截，如果想拦截特定的请求
```xml
<mvc:mapping path="/main/**"/>
```
即可拦截有/main的所有请求

## 文件上传

1. 编写jsp页面
```jsp
  <form action="/upload" enctype="multipart/form-data" method="post">
    <input type="file" name="file">
    <input type="submit" value="upload">
  </form>
```
2. springmvc中配置文件上传
```xml
<!--文件上传配置-->
<bean id="multipartResolver"  class="org.springframework.web.multipart.commons.CommonsMultipartResolver">
    <!-- 请求的编码格式，必须和jSP的pageEncoding属性一致，以便正确读取表单的内容，默认为ISO-8859-1 -->
    <property name="defaultEncoding" value="utf-8"/>
    <!-- 上传文件大小上限，单位为字节（10485760=10M） -->
    <property name="maxUploadSize" value="10485760"/>
    <property name="maxInMemorySize" value="40960"/>
</bean>
```
3. 编写controller
```java
@RequestMapping("/upload")
public String fileUpload(@RequestParam("file") CommonsMultipartFile file , HttpServletRequest request) throws IOException {

    //获取文件名 : file.getOriginalFilename();
    String uploadFileName = file.getOriginalFilename();

    //如果文件名为空，直接回到首页！
    if ("".equals(uploadFileName)){
        return "redirect:/index.jsp";
    }
    System.out.println("上传文件名 : "+uploadFileName);

    //上传路径保存设置
    String path = request.getServletContext().getRealPath("/upload");
    //如果路径不存在，创建一个
    File realPath = new File(path);
    if (!realPath.exists()){
        realPath.mkdir();
    }
    System.out.println("上传文件保存地址："+realPath);

    InputStream is = file.getInputStream(); //文件输入流
    OutputStream os = new FileOutputStream(new File(realPath,uploadFileName)); //文件输出流

    //读取写出
    int len=0;
    byte[] buffer = new byte[1024];
    while ((len=is.read(buffer))!=-1){
        os.write(buffer,0,len);
        os.flush();
    }
    os.close();
    is.close();
    return "redirect:/index.jsp";
}

@RequestMapping("/upload2")
public String  fileUpload2(@RequestParam("file") CommonsMultipartFile file, HttpServletRequest request) throws IOException {

    //上传路径保存设置
    String path = request.getServletContext().getRealPath("/upload");
    File realPath = new File(path);
    if (!realPath.exists()){
        realPath.mkdir();
    }
    //上传文件地址
    System.out.println("上传文件保存地址："+realPath);

    //通过CommonsMultipartFile的方法直接写文件（注意这个时候）
    file.transferTo(new File(realPath +"/"+ file.getOriginalFilename()));

    return "redirect:/index.jsp";
}
```
upload1与upload2都可以实现