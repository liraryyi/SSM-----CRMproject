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
<link href="jquery/bootstrap-datetimepicker-master/css/bootstrap-datetimepicker.min.css" type="text/css" rel="stylesheet" />

<script type="text/javascript" src="jquery/jquery-1.11.1-min.js"></script>
<script type="text/javascript" src="jquery/bootstrap_3.3.0/js/bootstrap.min.js"></script>
<script type="text/javascript" src="jquery/bootstrap-datetimepicker-master/js/bootstrap-datetimepicker.js"></script>
<script type="text/javascript" src="jquery/bootstrap-datetimepicker-master/locale/bootstrap-datetimepicker.zh-CN.js"></script>
	<!--分页查询的插件-->
	<link rel="stylesheet" type="text/css" href="jquery/bs_pagination/jquery.bs_pagination.min.css">
	<script type="text/javascript" src="jquery/bs_pagination/jquery.bs_pagination.min.js"></script>
	<script type="text/javascript" src="jquery/bs_pagination/en.js"></script>
<script type="text/javascript">

	$(function(){

		//点击查询按钮，实现分页查询
		$("#searchBtn").click(function (){

			//点击查询的同时将搜索栏中的值保存到隐藏域中
			$("#hidden-owner").val($("#owner").val());
			$("#hidden-name").val($("#name").val());
			$("#hidden-customerName").val($("#customerName").val());
			$("#hidden-stage").val($("#stage").val());
			$("#hidden-type").val($("#type").val());
			$("#hidden-source").val($("#source").val());
			$("#hidden-contactsName").val($("#contactsName").val());

			updatePage(1,3);
		})

		updatePage(1,3);
	});

	//分页查询页面
	function updatePage(pageNo, pageSize){

		<!--查询之前将隐藏域中的信息赋给搜索框-->
		$("#owner").val($.trim($("#hidden-owner").val()));
		$("#name").val($.trim($("#hidden-name").val()));
		$("#customerName").val($.trim($("#hidden-customerName").val()));
		$("#stage").val($.trim($("#hidden-stage").val()));
		$("#type").val($.trim($("#hidden-type").val()));
		$("#source").val($.trim($("#hidden-source").val()));
		$("#contactsName").val($.trim($("#hidden-contactsName").val()));

		$.ajax({

			url:"workbench/transaction/getTransactionList.do",
			data :{

				/**
				 * 分析一下传入的值
				 * pageNo:页码
				 * pageSize:每页展现的记录数
				 *
				 * 查询框中的
				 * 所有者：owner 是name 也需要联表查询
				 * 名称:name
				 * 客户名称:?
				 * 阶段:stage
				 * 类型:type
				 * 联系人名称:? 表中只有联系人的id，需要联表查询
				 */

				"pageNo":pageNo,
				"pageSize":pageSize,
				"owner":$.trim($("#owner").val()),
				"name":$.trim($("#name").val()),
				"customerName":$.trim($("#customerName").val()),
				"stage":$.trim($("#stage").val()),
				"type":$.trim($("#type").val()),
				"source":$.trim($("#source").val()),
				"contactsName":$.trim($("#contactsName").val())

			},
			type:"get",
			dataType : "json",
			success :function (data){

				//传回来的参数，查找得到的transaction对象 data.list
				//还有一个是分页插件所需要的total，即查询到的总记录数 data.total
				var html = "";

				//这里的每一个n就是一个transaction对象
				$.each(data.list,function (i,n){
				html +='<tr class="active">';
				html +='	<td><input type="checkbox" '+n.id+'/></td>';
				html +='	<td><a style="text-decoration: none; cursor: pointer;" onclick="window.location.href=\'workbench/transaction/detail.do?id='+n.id+'\';">'+n.name+'</a></td>';
				html +='	<td>'+n.customerId+'</td>';
				html +='	<td>'+n.stage+'</td>';
				html +='	<td>'+n.type+'</td>';
				html +='	<td>'+n.owner+'</td>';
				html +='	<td>'+n.source+'</td>';
				html +='	<td>'+n.contactsId+'</td>';
				html +='</tr>';
			    })

				$("#transactionBody").html(html);

				//计算总页数
				var totalPages = data.total%pageSize ==0?data.total/pageSize:data.total/pageSize+1;

				//分页组件  transactionPage:分页查询的表单
				$("#transactionPage").bs_pagination({
					currentPage: pageNo, // 页码
					rowsPerPage: pageSize, // 每页显示的记录条数
					maxRowsPerPage: 20, // 每页最多显示的记录条数
					totalPages: totalPages, // 总页数》需要计算
					totalRows: data.total, // 总记录条数

					visiblePageLinks: 3, // 显示几个卡片

					showGoToPage: true,
					showRowsPerPage: true,
					showRowsInfo: true,
					showRowsDefaultInfo: true,

					//回调函数在点击分页组件的时候触发
					onChangePage : function(event, data){
						updatePage(data.currentPage , data.rowsPerPage);
					}
				});
			}
		})
	}

</script>
</head>
<body>

	
	
	<div>
		<div style="position: relative; left: 10px; top: -10px;">
			<div class="page-header">
				<h3>交易列表</h3>
			</div>
		</div>
	</div>

	<!--用隐藏域储存搜索框中填写的信息-->
	<input type="hidden" id="hidden-owner"/>
	<input type="hidden" id="hidden-name"/>
	<input type="hidden" id="hidden-customerName"/>
	<input type="hidden" id="hidden-stage"/>
	<input type="hidden" id="hidden-type"/>
	<input type="hidden" id="hidden-source"/>
	<input type="hidden" id="hidden-contactsName"/>

	<div style="position: relative; top: -20px; left: 0px; width: 100%; height: 100%;">
	
		<div style="width: 100%; position: absolute;top: 5px; left: 10px;">
		
			<div class="btn-toolbar" role="toolbar" style="height: 80px;">
				<form class="form-inline" role="form" style="position: relative;top: 8%; left: 5px;">
				  
				  <div class="form-group">
				    <div class="input-group">
				      <div class="input-group-addon">所有者</div>
				      <input class="form-control" type="text" id="owner">
				    </div>
				  </div>
				  
				  <div class="form-group">
				    <div class="input-group">
				      <div class="input-group-addon">名称</div>
				      <input class="form-control" type="text" id="name">
				    </div>
				  </div>
				  
				  <div class="form-group">
				    <div class="input-group">
				      <div class="input-group-addon">客户名称</div>
				      <input class="form-control" type="text" id="customerName">
				    </div>
				  </div>
				  
				  <br>
				  
				  <div class="form-group">
				    <div class="input-group">
				      <div class="input-group-addon">阶段</div>
					  <select class="form-control" id="stage">
					  	<option></option>
						  <c:forEach items="${stage}" var="s">
							  <option value="${s.value}">${s.text}</option>
						  </c:forEach>
					  </select>
				    </div>
				  </div>
				  
				  <div class="form-group">
				    <div class="input-group">
				      <div class="input-group-addon">类型</div>
					  <select class="form-control" id="type">
					  	<option></option>
						  <c:forEach items="${transactionType}" var="t">
							  <option value="${t.value}">${t.text}</option>
						  </c:forEach>
					  </select>
				    </div>
				  </div>
				  
				  <div class="form-group">
				    <div class="input-group">
				      <div class="input-group-addon">来源</div>
				      <select class="form-control" id="source">
						  <option></option>
						  <c:forEach items="${source}" var="so">
							  <option value="${so.value}">${so.text}</option>
						  </c:forEach>
						</select>
				    </div>
				  </div>
				  
				  <div class="form-group">
				    <div class="input-group">
				      <div class="input-group-addon">联系人名称</div>
				      <input class="form-control" type="text" name="contactsName">
				    </div>
				  </div>
				  
				  <button type="button" class="btn btn-default" id="searchBtn">查询</button>
				  
				</form>
			</div>
			<div class="btn-toolbar" role="toolbar" style="background-color: #F7F7F7; height: 50px; position: relative;top: 10px;">
				<div class="btn-group" style="position: relative; top: 18%;">
				  <button type="button" class="btn btn-primary" onclick="window.location.href='workbench/transaction/getUserList.do';"><span class="glyphicon glyphicon-plus"></span> 创建</button>
				  <button type="button" class="btn btn-default" onclick="window.location.href='workbench/transaction/edit.jsp';"><span class="glyphicon glyphicon-pencil"></span> 修改</button>
				  <button type="button" class="btn btn-danger"><span class="glyphicon glyphicon-minus"></span> 删除</button>
				</div>
				
				
			</div>
			<div style="position: relative;top: 10px;">
				<table class="table table-hover">
					<thead>
						<tr style="color: #B3B3B3;">
							<td><input type="checkbox" /></td>
							<td>名称</td>
							<td>客户名称</td>
							<td>阶段</td>
							<td>类型</td>
							<td>所有者</td>
							<td>来源</td>
							<td>联系人名称</td>
						</tr>
					</thead>
					<tbody id="transactionBody">
<%--						<tr>
							<td><input type="checkbox" /></td>
							<td><a style="text-decoration: none; cursor: pointer;" onclick="window.location.href='detail.jsp';">动力节点-交易01</a></td>
							<td>动力节点</td>
							<td>谈判/复审</td>
							<td>新业务</td>
							<td>zhangsan</td>
							<td>广告</td>
							<td>李四</td>
						</tr>
                        <tr class="active">
                            <td><input type="checkbox" /></td>
                            <td><a style="text-decoration: none; cursor: pointer;" onclick="window.location.href='detail.jsp';">动力节点-交易01</a></td>
                            <td>动力节点</td>
                            <td>谈判/复审</td>
                            <td>新业务</td>
                            <td>zhangsan</td>
                            <td>广告</td>
                            <td>李四</td>
                        </tr>--%>
					</tbody>
				</table>
			</div>
			
			<div style="height: 50px; position: relative;top: 20px;">
				<div id="transactionPage"></div>
			</div>
			
		</div>
		
	</div>
</body>
</html>