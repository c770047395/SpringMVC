<%--
  Created by IntelliJ IDEA.
  User: Administrator
  Date: 2019/10/29
  Time: 14:16
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
  <head>
    <title>$Title$</title>
    <script src="https://apps.bdimg.com/libs/jquery/2.1.4/jquery.min.js">
    </script>
    <script>
      function a() {
        $.ajax({
          url:"${pageContext.request.contextPath}/a1",
          data:{
            "name" : $("#username").val()
          },
          success:function (res) {
            alert(res)
          }
        })
      }
    </script>
  </head>
  <body>
  用户名：<input type="text" id="username" onblur="a()">
  </body>
</html>
