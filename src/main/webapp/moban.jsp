<%--
  Created by IntelliJ IDEA.
  User: xcw
  Date: 2021/6/16
  Time: 11:27
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%
    String basePath = request.getScheme() + "://" + request.getServerName() + ":" +
            request.getServerPort() + request.getContextPath() + "/";
%>

<html>
<head>
    <base href="<%=basePath%>">
    <title>Title</title>
</head>
<body>

$.ajax({

url:"",
data :{

},
type:"",
dataType : "json",
success :function (data){

}
})

</body>
</html>