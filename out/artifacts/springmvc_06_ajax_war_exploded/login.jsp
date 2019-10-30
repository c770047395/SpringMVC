<%--
  Created by IntelliJ IDEA.
  User: Administrator
  Date: 2019/10/29
  Time: 18:37
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Title</title>
    <script src="https://apps.bdimg.com/libs/jquery/2.1.4/jquery.min.js">
    </script>
    <script>
        function o1() {
            $.ajax({
                url:"${pageContext.request.contextPath}/a3",
                data : {"name":document.getElementById("name").value},
                success:function(data) {
                    console.log(data)
                    if(data==="ok"){
                        $("#userInfo").css("color","green")
                    }else{
                        $("#userInfo").css("color","red")
                    }
                    $("#userInfo").text(data)
                }
            })
        }
        function o2() {
            $.ajax({
                url: "${pageContext.request.contextPath}/a3",
                data:{"pwd":$("#password").val()},
                success:function(data){
                    if(data==="ok"){
                        $("#pwdInfo").css("color","green")
                    }else{
                        $("#pwdInfo").css("color","red")
                    }
                    $("#pwdInfo").text(data)
                }
            })
        }
    </script>
</head>
<body>
<p>
    用户名：<input type="text" id="name" onblur="o1()">
    <span id="userInfo"></span>
</p>

<p>
    密码：<input type="text" id="password" onblur="o2()">
    <span id="pwdInfo"></span>
</p>
</body>
</html>
