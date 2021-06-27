<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
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


<link href="jquery/bootstrap-datetimepicker-master/css/bootstrap-datetimepicker.min.css" type="text/css" rel="stylesheet" />
<script type="text/javascript" src="jquery/bootstrap-datetimepicker-master/js/bootstrap-datetimepicker.js"></script>
<script type="text/javascript" src="jquery/bootstrap-datetimepicker-master/locale/bootstrap-datetimepicker.zh-CN.js"></script>


<script type="text/javascript">
	$(function(){
		$("#isCreateTransaction").click(function(){
			if(this.checked){
				$("#create-transaction2").show(200);
			}else{
				$("#create-transaction2").hide(200);
			}
		});

		//模态窗口日历栏中加入日历插件
		/**
		 * 插件加入可能导致乱码问题
		 * 注意：
		 * 1.jsp文件头部：charset=UTF-8
		 * 2.引入的js文件的编码格式为utf-8
		 * 3.File》Settings》Editor》File Encodings  --utf-8
		 * 4.Tomcat服务器设置中 ：VM option：-Dfile.encoding=UTF-8
		 */
		$(".time").datetimepicker({
			minView: "month",
			language:  'zh-CN',
			format: 'yyyy-mm-dd',
			autoclose: true,
			todayBtn: true,
			pickerPosition: "bottom-left"
		});

		//在搜索市场活动时，点击回车键触发搜索
		$("#searchText").keydown(function (event){

			if (event.keyCode == 13){

				updateActivityList();
				return false;
			}
		})

		//在搜索市场活动时，点击搜索图标触发搜索
		$("#searchBtn").click(function (){
			updateActivityList()
		})

		//为搜索市场活动的模态窗口中的提交按钮绑定事件
		$("#subActivityBtn").click(function (){

			//取得选中的市场活动的id
			var $xz = $("input[name= activity]:checked");
			var id = $xz.val();

			//取得选中市场活动的名字
			var name = $("#"+id).html();

			//将取得的市场活动id放在隐藏域中
			$("#activityId").val(id);

			//将选中的市场活动的名字放到市场活动源中
			$("#activity").val(name);

			//关闭模态窗口
			$("#searchActivityModal").modal("hide");
		})

		//为线索转换按钮绑定事件，点击转换线索
		$("#convertBtn").click(function (){

			//根据为客户创建交易是否打勾来判断转换
			if ($("#isCreateTransaction").prop("checked")){

				//提交表单
				$("#tranForm").submit();
			}else {

				window.location.href="workbench/clue/convert.do?clueId=${param.id}";
			}
		})
	});

	//模糊查询搜索市场活动
	function updateActivityList(){

		$.ajax({

			url:"workbench/clue/getAllActivityListByClueId.do",
			data :{
				"name":$.trim($("#searchText").val())
			},
			type:"get",
			dataType : "json",
			success :function (data){

				var html = "";
				$.each(data,function (i,n){
				html +='<tr>';
				html +='<td><input type="radio" name="activity" value="'+n.id+'" /></td>';
				html +='<td id="'+n.id+'">'+n.name+'</td>';
				html +='<td>'+n.startDate+'</td>';
				html +='<td>'+n.endDate+'</td>';
				html +='<td>'+n.owner+'</td>';
				html +='</tr>';
				})
				$("#activityBody").html(html);
			}
		})
	}
</script>

</head>
<body>
	
	<!-- 搜索市场活动的模态窗口 -->
	<div class="modal fade" id="searchActivityModal" role="dialog" >
		<div class="modal-dialog" role="document" style="width: 90%;">
			<div class="modal-content">
				<div class="modal-header">
					<button type="button" class="close" data-dismiss="modal">
						<span aria-hidden="true">×</span>
					</button>
					<h4 class="modal-title">搜索市场活动</h4>
				</div>
				<div class="modal-body">
					<div class="btn-group" style="position: relative; top: 18%; left: 8px;">
						<form class="form-inline" role="form">
						  <div class="form-group has-feedback" id="searchBtn">
						    <input type="text" id="searchText" class="form-control" style="width: 300px;" placeholder="请输入市场活动名称，支持模糊查询">
						    <span class="glyphicon glyphicon-search form-control-feedback"></span>
						  </div>
						</form>
					</div>
					<table id="activityTable" class="table table-hover" style="width: 900px; position: relative;top: 10px;">
						<thead>
							<tr style="color: #B3B3B3;">
								<td></td>
								<td>名称</td>
								<td>开始日期</td>
								<td>结束日期</td>
								<td>所有者</td>
								<td></td>
							</tr>
						</thead>
						<tbody id="activityBody">
							<%--<tr>
								<td><input type="radio" name="activity"/></td>
								<td>发传单</td>
								<td>2020-10-10</td>
								<td>2020-10-20</td>
								<td>zhangsan</td>
							</tr>
							<tr>
								<td><input type="radio" name="activity"/></td>
								<td>发传单</td>
								<td>2020-10-10</td>
								<td>2020-10-20</td>
								<td>zhangsan</td>
							</tr>--%>
						</tbody>
					</table>
				</div>
				<div class="modal-footer">
					<button type="button" class="btn btn-default" data-dismiss="modal">取消</button>
					<button type="button" class="btn btn-primary" id="subActivityBtn">提交</button>
				</div>
			</div>
		</div>
	</div>

	<div id="title" class="page-header" style="position: relative; left: 20px;">
		<h4>转换线索 <small>${param.fullname}${param.appellation}-${param.company}</small></h4>
	</div>
	<div id="create-customer" style="position: relative; left: 40px; height: 35px;">
		新建客户：${param.company}
	</div>
	<div id="create-contact" style="position: relative; left: 40px; height: 35px;">
		新建联系人：${param.fullname}${param.appellation}
	</div>
	<div id="create-transaction1" style="position: relative; left: 40px; height: 35px; top: 25px;">
		<input type="checkbox" id="isCreateTransaction" name="xz"/>
		为客户创建交易
	</div>
	<div id="create-transaction2" style="position: relative; left: 40px; top: 20px; width: 80%; background-color: #F7F7F7; display: none;" >
	
		<form id="tranForm" action="workbench/clue/convert.do" method="post">

			<!--
			form表单传输的数据格式
			workbench/clue/convert.do?clueId=xxx&money=xxx&name=xxx&date=xxx&stage=xxx&activityId=xxx&flag=xxx

			总共的数据： clueId,
			           money,
			           name,
			           date,
			           stage,
			           activityId,
			           flag
			-->

			<!--添加隐藏文本域，加上clueId-->
			<input type="hidden" name="clueId" value="${param.id}"/>

		  <div class="form-group" style="width: 400px; position: relative; left: 20px;">
		    <label for="amountOfMoney">金额</label>
		    <input type="text" class="form-control" id="amountOfMoney" name="money">
		  </div>
		  <div class="form-group" style="width: 400px;position: relative; left: 20px;">
		    <label for="tradeName">交易名称</label>
		    <input type="text" class="form-control" id="tradeName" name="name">
		  </div>
		  <div class="form-group" style="width: 400px;position: relative; left: 20px;">
		    <label for="expectedClosingDate">预计成交日期</label>
		    <input type="text" class="form-control time" id="expectedClosingDate" name="date">
		  </div>
		  <div class="form-group" style="width: 400px;position: relative; left: 20px;">
		    <label for="stage">阶段</label>
		    <select id="stage"  class="form-control" name="stage">
		    	<option></option>
				<c:forEach items="${stage}" var="s">
					<option value="${s.value}">${s.text}</option>
				</c:forEach>
		    </select>
		  </div>
		  <div class="form-group" style="width: 400px;position: relative; left: 20px;">
		    <label for="activity">市场活动源&nbsp;&nbsp;<a href="javascript:void(0);" data-toggle="modal" data-target="#searchActivityModal" style="text-decoration: none;"><span class="glyphicon glyphicon-search"></span></a></label>
		    <input type="text" class="form-control" id="activity" placeholder="点击上面搜索" readonly>
			  <!--用隐藏域来保存市场活动的Id-->
			  <input type="hidden" id="activityId" name="activityId">

			  <!--用隐藏域再传递一个flag，确认是从form表单里发出的信息-->
			  <input type="hidden" name="flag" value="a">
		  </div>
		</form>
		
	</div>
	
	<div id="owner" style="position: relative; left: 40px; height: 35px; top: 50px;">
		记录的所有者：<br>
		<b>${param.owner}</b>
	</div>
	<div id="operation" style="position: relative; left: 40px; height: 35px; top: 100px;">
		<input class="btn btn-primary" type="button" value="转换" id="convertBtn">
		&nbsp;&nbsp;&nbsp;&nbsp;
		<input class="btn btn-default" type="button" value="取消">
	</div>
</body>
</html>