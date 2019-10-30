<%--
  Created by IntelliJ IDEA.
  User: Administrator
  Date: 2019/10/29
  Time: 15:08
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Title</title>
</head>
<script src="https://apps.bdimg.com/libs/jquery/2.1.4/jquery.min.js">
</script>
<script>
    $(function () {
        $("#btn").click(function () {
            $.ajax({
                url: "${pageContext.request.contextPath}/a2",
                success: function (data) {
                    // console.log(data)
                    var html = "";

                    for (let i = 0; i < data.length; i++) {
                        html+="<tr>"+
                            "<td>"+data[i].name+"</td>"+
                            "<td>"+data[i].age+"</td>"+
                            "<td>"+data[i].sex+"</td>"+
                            "</tr>"
                    }

                    $("#content").html(html);
                }
            });

        })
    })

</script>
<body>

<button id="btn">加载数据</button>
<table>
    <thead>
    <tr>
        <td>姓名</td>
        <td>年龄</td>
        <td>性别</td>
    </tr>
    </thead>
    <tbody id="content">
    <%--后台加载--%>
    </tbody>
</table>


</body>
</html>
