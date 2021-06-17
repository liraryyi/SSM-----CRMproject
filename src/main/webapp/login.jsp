<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%
String basePath = request.getScheme() + "://" + request.getServerName() + ":" +
request.getServerPort() + request.getContextPath() + "/";
%>
<!DOCTYPE html>
<html>
<head>
	<base href="<%=basePath%>">
<meta charset="UTF-8">
<link href="jquery/bootstrap_3.3.0/css/bootstrap.min.css" type="text/css" rel="stylesheet" />
<script type="text/javascript" src="jquery/jquery-1.11.1-min.js"></script>
<script type="text/javascript" src="jquery/bootstrap_3.3.0/js/bootstrap.min.js"></script>
	<script>
		$(function (){

			//页面加载完毕后，清空用户文本框
			$("#loginAct").val("");

			//页面加载完毕后，让用户的文本框自动获得焦点
			$("#loginAct").focus();

			//登录按钮绑定事件，进行登陆操作
			$("#submitBtn").click(function (){
				login();
			})

			//键盘的回车键绑定事件，进行登陆操作
			$(window).keydown(function (){
				if (event.keyCode ==13){
					login();
				}
			})
		})

		//登录方法,注意普通方法应该写在$(function(){})的外面
		function login() {
			//账号，密码不能为空
			//$.trim():去掉文本中的左右空格
			var loginAct = $.trim($("#loginAct").val());
			var loginPwd = $.trim($("#loginPwd").val());

			if (loginAct == "" || loginPwd == ""){
				$("#msg").html("账号密码不能为空")
				return false;
			}

			//页面局部刷新 ajax
			$.ajax({

				//url:传递到后台的地址
				url:"settings/user/login.do",
				//data:传递给后台的数据
				data :{
					"loginAck":loginAct,
					"loginPwd":loginPwd
				},
				type:"post",
				dataType : "json",
				success :function (data){
					/**
					 * data:传过来的数据json
					 * {"success":true/false  , "msg":"错误的种类"}
					 */
					if (data.success){
						window.location.href = "workbench/index.html";
					} else {
						$("#msg").html(data.msg);
					}
				}
			})
		}

	</script>
</head>
<body>
	<div style="position: absolute; top: 0px; left: 0px; width: 60%;">
		<img src="image/IMG_7114.JPG" style="width: 100%; height: 90%; position: relative; top: 50px;">
	</div>
	<div id="top" style="height: 50px; background-color: #3C3C3C; width: 100%;">
		<div style="position: absolute; top: 5px; left: 0px; font-size: 30px; font-weight: 400; color: white; font-family: 'times new roman'">CRM &nbsp;<span style="font-size: 12px;">&copy;2017&nbsp;动力节点</span></div>
	</div>
	
	<div style="position: absolute; top: 120px; right: 100px;width:450px;height:400px;border:1px solid #D5D5D5">
		<div style="position: absolute; top: 0px; right: 60px;">
			<div class="page-header">
				<h1>登录</h1>
			</div>
			<form action="workbench/index.html" class="form-horizontal" role="form">
				<div class="form-group form-group-lg">
					<div style="width: 350px;">
						<input class="form-control" type="text" placeholder="用户名" id = "loginAct">
					</div>
					<div style="width: 350px; position: relative;top: 20px;">
						<input class="form-control" type="password" placeholder="密码" id="loginPwd">
					</div>
					<div class="checkbox"  style="position: relative;top: 30px; left: 10px;" >
						
							<span id="msg" style="color: #ac2925"></span>
						
					</div>
					<!--button在form表单中，默认type是submit-->
					<button id="submitBtn" type="button" class="btn btn-primary btn-lg btn-block"  style="width: 350px; position: relative;top: 45px;">登录</button>
				</div>
			</form>
		</div>
	</div>
</body>
</html>