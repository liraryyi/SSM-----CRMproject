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
<link href="jquery/bootstrap-datetimepicker-master/css/bootstrap-datetimepicker.min.css" type="text/css" rel="stylesheet" />

	<!--插件的加入顺序 先有jquery》bootstrap》bootstrap的日历插件-->
<script type="text/javascript" src="jquery/jquery-1.11.1-min.js" ></script>
<script type="text/javascript" src="jquery/bootstrap_3.3.0/js/bootstrap.min.js" ></script>
<script type="text/javascript" src="jquery/bootstrap-datetimepicker-master/js/bootstrap-datetimepicker.js"  ></script>
<script type="text/javascript" src="jquery/bootstrap-datetimepicker-master/locale/bootstrap-datetimepicker.zh-CN.js" ></script>
	<!--分页查询的插件-->
	<link rel="stylesheet" type="text/css" href="jquery/bs_pagination/jquery.bs_pagination.min.css">
	<script type="text/javascript" src="jquery/bs_pagination/jquery.bs_pagination.min.js"></script>
	<script type="text/javascript" src="jquery/bs_pagination/en.js"></script>

<script type="text/javascript">

	$(function(){

		//为”创建“按钮绑定事件，点击打开模态窗口
		$("#addBtn").click(function (){


			//createActivityModal模态窗口打开后，所有者下拉栏显示数据库中的用户姓名
			$.ajax({

				url:"workbench/activity/getUserList.do",
				data :{
				},
				type:"get",
				dataType : "json",
				success :function (data){

					/**
					 * 这里data拿到的数据是含有用户对象的数组
					 * data
					 *   【{用户1}, {2}, {3}...】
					 */

					var html = "<option></option>";

					//遍历出来的每一个n，就是每一个user对象
					$.each(data,function (i,n){
						html += "<option value='"+n.id+"'>"+n.name+"</option>";
					})

					/**
					 * 取得当前登录的用户的id，放在下拉框的默认选项中
					 * 可以使用el表达式，但是js中的el表达式一定要套用在字符串中
					 */

					$("#create-marketActivityOwner").html(html);

					var id = "${user.id}"
					$("#create-marketActivityOwner").val(id);

				}
			})


			/**
			 * 操作模态窗口的方式:
			 *   需要操作窗口的jquery对象，调用model方法，为该方法传递参数 show：打开模态窗口   hide：关闭模态窗口
			 */
			$("#createActivityModal").modal("show");

		})

		//为”修改“按钮绑定事件，点击打开模态窗口
		$("#updateBtn").click(function (){

			//修改时应该只能修改选中的唯一一个复选框的信息
			var $xz = $("input[name = subSelectBox]:checked");
			if ($xz.length == 0){
				alert("请选择您想修改的活动")
			}else if ($xz.length > 1){
				alert("不能同时修改多个活动的信息")
			}else {

				//editActivityModal模态窗口打开后，所有者下拉栏显示数据库中的用户姓名
				$.ajax({

					url:"workbench/activity/getUserList.do",
					data :{
					},
					type:"get",
					dataType : "json",
					success :function (data){

						/**
						 * 这里data拿到的数据是含有用户对象的数组
						 * data
						 *   【{用户1}, {2}, {3}...】
						 */

						var html = "<option></option>";

						//遍历出来的每一个n，就是每一个user对象
						$.each(data,function (i,n){
							html += "<option value='"+n.id+"'>"+n.name+"</option>";
						})

						/**
						 * 取得当前登录的用户的id，放在下拉框的默认选项中
						 * 可以使用el表达式，但是js中的el表达式一定要套用在字符串中
						 */

						$("#edit-marketActivityOwner").html(html);

						var id = "${user.id}"
						$("#edit-marketActivityOwner").val(id);

					}
				})

				//editActivityModal模态窗口打开后，将该Activity中的数据查询出来
				$.ajax({

					url:"workbench/activity/getActivityList.do",
					data :{
						"id": $xz.val(),
					},
					type:"get",
					dataType : "json",
					success :function (data){

						$("#edit-marketActivityName").val(data.name);
						$("#edit-startTime").val(data.startDate);
						$("#edit-endTime").val(data.endDate);
						$("#edit-cost").val(data.cost);
						$("#edit-describe").val(data.description);
					}
				})

				$("#editActivityModal").modal("show");
			}
		})

		//为更新按钮绑定事件，点击将已经修改的信息保存到数据库中
		$("#edit-updateBtn").click(function (){

			var $xz = $("input[name = subSelectBox]:checked");
			$.ajax({

				url:"workbench/activity/updateActivity.do",
				data :{
					"edit-id":$xz.val(),
					"edit-marketActivityOwner": $.trim($("#edit-marketActivityOwner").val()),   //创建市场活动的人
					"edit-marketActivityName": $.trim($("#edit-marketActivityName").val()),     //创建的市场活动的名称
					"edit-startTime": $.trim($("#edit-startTime").val()),                       //市场活动开始时间
					"edit-endTime":$.trim($("#edit-endTime").val()),                            //市场活动结束时间
					"edit-cost":$.trim($("#edit-cost").val()),                                  //市场活动费用
					"edit-describe":$.trim($("#edit-describe").val())          //市场活动描述
				},
				type:"post",
				dataType : "json",
				success :function (data){

					//传过来的数据success
					if (data.success){
						$("#activityUpdateForm")[0].reset();

						$("#editActivityModal").modal("hide");

						//更新后，原本在哪一页，应该回到哪一页
						/*
						bootstrap的插件
						pageList($("#activityPage").bs_pagination('getOption', 'currentPage')
								,$("#activityPage").bs_pagination('getOption', 'rowsPerPage'));

						操作后停留在当前页面
						$("#activityPage").bs_pagination('getOption', 'currentPage')

						操作后维持已经设置好的每页展现的记录数
						$("#activityPage").bs_pagination('getOption', 'rowsPerPage')
						*/
						pageList($("#activityPage").bs_pagination('getOption', 'currentPage')
								,$("#activityPage").bs_pagination('getOption', 'rowsPerPage'));
					}else {
						alert("保存失败")
					}
				}
			})
		})

		//为”查询按钮绑定事件，点击查询“
        //点击查询的同时将搜索框中数据保存到隐藏域中
		$("#getActivityListBtn").click(function (){

            $("#hidden-name").val($.trim($("#search-name").val()));
            $("#hidden-owner").val($.trim($("#search-owner").val()));
            $("#hidden-startDate").val($.trim($("#search-startTime").val()));
            $("#hidden-endDate").val($.trim($("#search-endTime").val()))

			pageList(1, 3);
		})

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

		//为创建模态窗口的保存按钮添加事件，实现信息的保存
		$("#saveBtn").click(function (){
			//使用ajax请求传输数据实现页面的局部刷新
			$.ajax({

				url:"workbench/activity/saveActivity.do",
				data :{
					"create-marketActivityOwner": $.trim($("#create-marketActivityOwner").val()),   //创建市场活动的人
					"create-marketActivityName": $.trim($("#create-marketActivityName").val()),     //创建的市场活动的名称
					"create-startTime": $.trim($("#create-startTime").val()),                       //市场活动开始时间
					"create-endTime":$.trim($("#create-endTime").val()),                            //市场活动结束时间
			        "create-cost":$.trim($("#create-cost").val()),                                  //市场活动费用
			        "create-describe":$.trim($("#create-describe").val())          //市场活动描述
				},
				type:"post",
				dataType : "json",
				success :function (data){

					//后台传输一个boolean值表示信息是否存储成功，true表示成功，false表示失败
					//如果数据存储成功，刷新下面的active页面，清空表单中的数据,并关闭模态窗口
					if (data.success){

						//需要先将jquery对象转换为dom对象，再执行操作
						$("#activityAddForm")[0].reset();

						$("#createActivityModal").modal("hide");

						pageList(1,$("#activityPage").bs_pagination('getOption', 'rowsPerPage'));
					}else{
						//如果数据存储失败，弹出一个弹框提示
						alert("市场活动保存失败！");
					}
				}
			})
		})

        //为selectBox选择框绑定事件，点击全选下面的选择框
        $("#selectBox").click(function (){
            $("input[name = subSelectBox]").prop("checked",this.checked);
        })

        //为subSelectBox选择框绑定事件，当subSelectBox全部为checked的时候，将selectBox状态变为checked
        /*$("#subSelectBox").click(function (){
            alert(123);
        })
        而上面这种方法是不行的，这是因为动态生成的元素，是不能够以普通绑定事件的形式来进行操作

        动态生成的元素，需要以on方法的形式来触发事件

        语法：
            $(需要绑定元素的有效的外层元素).on(绑定事件的方式，需要绑定的元素的jquery对象，回调函数)
        */
        $("#activityList").on("click",$("input[name = subSelectBox]"),function (){

            $("#selectBox").prop("checked",$("input[name = subSelectBox]").length == $("input[name = subSelectBox]:checked").length);
        })

		//为delete按钮绑定事件，点击按复选框（subSelectBox）的checked状态删除查询数据
		$("#deleteBtn").click(function (){

			//找到复选框中所有checked的jquery对象
			var $xz = $("input[name = subSelectBox]:checked");

			if ($xz.length == 0){
				alert("请选择需要删除的记录");
			}else {

				//url:workbench/activity/delete.do?id=xxx&id=xxx
				//拼接参数
				var param = "";

				//遍历$xz中的每一个dom对象遍历出来，取其value值》取得需要删除的记录的id
				for (var i = 0; i < $xz.length; i++) {

					param += "id=" + $($xz[i]).val();
					if (i < $xz.length - 1){
						param += "&"
					}
				}
				alert(param)
				if (confirm("是否打算删除该数据")){
					$.ajax({

						url:"workbench/activity/delete.do",
						data : param,
						type:"post",
						dataType : "json",
						success :function (data){
							//需要传递出来的信息：success：true or false
							if (data.success){
								//删除成功后刷新当前页
								pageList($("#activityPage").bs_pagination('getOption', 'currentPage')
										,$("#activityPage").bs_pagination('getOption', 'rowsPerPage'));
							}else {
								alert("数据删除失败")
							}
						}
					})
				}
			}
		})

		//进入当前页面时自动刷新
		pageList(1, 3)
	});

	/**
	 * 对于所有关系型数据库，做前端的分页相关操作的基础组件就是pageNo和pageSize
	 * pageNo:页码
	 * pageSize：每页展现的记录数
	 *
	 * pageList方法:发出请求到后台，从后台取得最新的市场活动信息列表数据
	 *             通过响应回来的数据，局部刷新市场活动信息列表
	 *
	 * 我们在哪些情况下，需要调用pageList方法（什么时候需要刷新市场活动方法）
	 * 1.点击左侧菜单中"市场活动"超链接的时候，需要刷新市场活动列表，调用pageList方法
	 * 2.点击添加，修改，删除后
	 * 3.点击查询按钮后
	 * 4.点击分页组件的时候
	 */
	function pageList(pageNo, pageSize){

		//将全选框和复选框中checked状态干掉
		$("#selectBox").prop("checked",false);

        //使用ajax进行局部刷新前，将隐藏域中保存的值赋给搜索框中的值
        $("#search-name").val($.trim($("#hidden-name").val()));
        $("#search-owner").val($.trim($("#hidden-owner").val()));
        $("#search-startTime").val($.trim($("#hidden-startDate").val()));
        $("#search-endTime").val($.trim($("#hidden-endDate").val()))

		$.ajax({

			url:"workbench/activity/pageList.do",
			data :{

				//传参--做分页的相关参数
				"pageNo":pageNo,
				"pageSize":pageSize,
				//传参--查询的相关条件
				"name":$.trim($("#search-name").val()),
				"owner":$.trim($("#search-owner").val()),
				"startTime":$.trim($("#search-startTime").val()),
				"endTime":$.trim($("#search-endTime").val())
			},
			type:"get",
			dataType : "json",
			success :function (data){

				/**
				 * 前端需要的参数：市场活动信息列表
				 * {{市场活动1}，{2}，{3}}
				 * 一会分页插件需要的，查询出来的总记录数
				 * {“total”：100}
				 * {“total：100，“datalist”：[{市场活动1}，{2}，{3}]}
				 */
				var html = "";

				//每一个n就是一个市场活动对象
				$.each(data.list,function (i,n){
				html +='<tr class="active">';
				html +='	<td><input type="checkbox" name="subSelectBox" value="'+n.id+'" /></td>';
				html +='	<td><a style="text-decoration: none; cursor: pointer;" onclick="window.location.href=\'workbench/activity/detail.do?id='+n.id+'\';">'+n.name+'</a></td>';
				html +='	<td>'+n.owner+'</td>';
				html +='	<td>'+n.startDate+'</td>';
				html +='	<td>'+n.endDate+'</td>';
				html +='</tr>';

				})

				$("#activityList").html(html);

				//计算总页数
				var totalPages = data.total%pageSize ==0?data.total/pageSize:data.total/pageSize+1;

				//分页组件
				$("#activityPage").bs_pagination({
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
						pageList(data.currentPage , data.rowsPerPage);
					}
				});

			}
		})
	}


</script>
</head>
<body>

    <!--用隐藏域储存搜索框中填写的信息-->
    <input type="hidden" id="hidden-name"/>
    <input type="hidden" id="hidden-owner"/>
    <input type="hidden" id="hidden-startDate"/>
    <input type="hidden" id="hidden-endDate"/>

	<!-- 创建市场活动的模态窗口 -->
	<div class="modal fade" id="createActivityModal" role="dialog">
		<div class="modal-dialog" role="document" style="width: 85%;">
			<div class="modal-content">
				<div class="modal-header">
					<button type="button" class="close" data-dismiss="modal">
						<span aria-hidden="true">×</span>
					</button>
					<h4 class="modal-title" id="myModalLabel1">创建市场活动</h4>
				</div>
				<div class="modal-body">
				
					<form class="form-horizontal" role="form" id="activityAddForm">
					
						<div class="form-group">
							<label for="create-marketActivityOwner" class="col-sm-2 control-label">所有者<span style="font-size: 15px; color: red;">*</span></label>
							<div class="col-sm-10" style="width: 300px;">
								<select class="form-control" id="create-marketActivityOwner">

								</select>
							</div>
                            <label for="create-marketActivityName" class="col-sm-2 control-label">名称<span style="font-size: 15px; color: red;">*</span></label>
                            <div class="col-sm-10" style="width: 300px;">
                                <input type="text" class="form-control" id="create-marketActivityName">
                            </div>
						</div>
						
						<div class="form-group">
							<label for="create-startTime" class="col-sm-2 control-label">开始日期</label>
							<div class="col-sm-10" style="width: 300px;">
								<input type="text" class="form-control time" id="create-startTime">
							</div>
							<label for="create-endTime" class="col-sm-2 control-label">结束日期</label>
							<div class="col-sm-10" style="width: 300px;">
								<input type="text" class="form-control time" id="create-endTime">
							</div>
						</div>
                        <div class="form-group">

                            <label for="create-cost" class="col-sm-2 control-label">成本</label>
                            <div class="col-sm-10" style="width: 300px;">
                                <input type="text" class="form-control" id="create-cost">
                            </div>
                        </div>
						<div class="form-group">
							<label for="create-describe" class="col-sm-2 control-label">描述</label>
							<div class="col-sm-10" style="width: 81%;">
								<textarea class="form-control" rows="3" id="create-describe"></textarea>
							</div>
						</div>
						
					</form>
					
				</div>
				<div class="modal-footer">
					<!--data-dismiss = “modal” 的意思是指关闭模态窗口-->
					<button type="button" class="btn btn-default" data-dismiss="modal">关闭</button>
					<button type="button" class="btn btn-primary" id="saveBtn">保存</button>
				</div>
			</div>
		</div>
	</div>
	<!-- 修改市场活动的模态窗口 -->
	<div class="modal fade" id="editActivityModal" role="dialog">
		<div class="modal-dialog" role="document" style="width: 85%;">
			<div class="modal-content">
				<div class="modal-header">
					<button type="button" class="close" data-dismiss="modal">
						<span aria-hidden="true">×</span>
					</button>
					<h4 class="modal-title" id="myModalLabel2">修改市场活动</h4>
				</div>
				<div class="modal-body">
				
					<form class="form-horizontal" role="form" id="activityUpdateForm">
					
						<div class="form-group">
							<label for="edit-marketActivityOwner" class="col-sm-2 control-label">所有者<span style="font-size: 15px; color: red;">*</span></label>
							<div class="col-sm-10" style="width: 300px;">
								<select class="form-control" id="edit-marketActivityOwner">

								</select>
							</div>
                            <label for="edit-marketActivityName" class="col-sm-2 control-label">名称<span style="font-size: 15px; color: red;">*</span></label>
                            <div class="col-sm-10" style="width: 300px;">
                                <input type="text" class="form-control" id="edit-marketActivityName">
                            </div>
						</div>

						<div class="form-group">
							<label for="edit-startTime" class="col-sm-2 control-label">开始日期</label>
							<div class="col-sm-10" style="width: 300px;">
								<input type="text" class="form-control" id="edit-startTime">
							</div>
							<label for="edit-endTime" class="col-sm-2 control-label">结束日期</label>
							<div class="col-sm-10" style="width: 300px;">
								<input type="text" class="form-control" id="edit-endTime">
							</div>
						</div>
						
						<div class="form-group">
							<label for="edit-cost" class="col-sm-2 control-label">成本</label>
							<div class="col-sm-10" style="width: 300px;">
								<input type="text" class="form-control" id="edit-cost">
							</div>
						</div>
						
						<div class="form-group">
							<label for="edit-describe" class="col-sm-2 control-label">描述</label>
							<div class="col-sm-10" style="width: 81%;">
								<!--
								     关于文本域textarea
								        1.一定是要以标签对的形式来呈现，正常状态下标签需要紧紧的挨着
								        2.textarea虽然以标签对的形式呈现，但是他也是属于表单元素范畴
								          所有的对于textarea的取值和赋值操作，应该统一使用val()方法
								-->
								<textarea class="form-control" rows="3" id="edit-describe"></textarea>
							</div>
						</div>
						
					</form>
					
				</div>
				<div class="modal-footer">
					<button type="button" class="btn btn-default" data-dismiss="modal">关闭</button>
					<button type="button" class="btn btn-primary" id="edit-updateBtn">更新</button>
				</div>
			</div>
		</div>
	</div>
	
	
	
	
	<div>
		<div style="position: relative; left: 10px; top: -10px;">
			<div class="page-header">
				<h3>市场活动列表</h3>
			</div>
		</div>
	</div>
	<div style="position: relative; top: -20px; left: 0px; width: 100%; height: 100%;">
		<div style="width: 100%; position: absolute;top: 5px; left: 10px;">
		
			<div class="btn-toolbar" role="toolbar" style="height: 80px;">
				<form class="form-inline" role="form" style="position: relative;top: 8%; left: 5px;">
				  
				  <div class="form-group">
				    <div class="input-group">
				      <div class="input-group-addon">名称</div>
				      <input class="form-control" type="text" id="search-name">
				    </div>
				  </div>
				  
				  <div class="form-group">
				    <div class="input-group">
				      <div class="input-group-addon">所有者</div>
				      <input class="form-control" type="text" id="search-owner">
				    </div>
				  </div>


				  <div class="form-group">
				    <div class="input-group">
				      <div class="input-group-addon">开始日期</div>
					  <input class="form-control" type="text" id="search-startTime" />
				    </div>
				  </div>
				  <div class="form-group">
				    <div class="input-group">
				      <div class="input-group-addon">结束日期</div>
					  <input class="form-control" type="text" id="search-endTime">
				    </div>
				  </div>

				  <button type="button" id="getActivityListBtn" class="btn btn-default" >查询</button>
				</form>
			</div>
			<div class="btn-toolbar" role="toolbar" style="background-color: #F7F7F7; height: 50px; position: relative;top: 5px;">
				<div class="btn-group" style="position: relative; top: 18%;">
					<!--
					      点击创建按钮，观察两个属性和属性值

					      data-toggle = "model": 表示触发该按钮，将要打开一个模态窗口

					      data-target = "#createActivityModal":表示要打开哪一个模态窗口

					      目前这2个模态窗口是以属性和属性值的方式写在button元素中，用来打开模态窗口，
					      问题：没有办法对按钮的功能进行扩充，写死了。

					      实际的项目开发中，这样的操作应该通过js代码来操作

					-->
				  <button type="button" class="btn btn-primary" id = "addBtn"><span class="glyphicon glyphicon-plus"></span> 创建</button>
				  <button type="button" class="btn btn-default" id = "updateBtn"><span class="glyphicon glyphicon-pencil"></span> 修改</button>
				  <button type="button" class="btn btn-danger"  id = "deleteBtn"><span class="glyphicon glyphicon-minus"></span> 删除</button>
				</div>
				
			</div>
			<div style="position: relative;top: 10px;">
				<table class="table table-hover">
					<thead>
						<tr style="color: #B3B3B3;">
							<td><input type="checkbox" id="selectBox" /></td>
							<td>名称</td>
                            <td>所有者</td>
							<td>开始日期</td>
							<td>结束日期</td>
						</tr>
					</thead>
					<tbody id="activityList">
					<!--tbody里是我们需要查询到的数据-->
						<%--<tr class="active">
							<td><input type="checkbox" /></td>
							<td><a style="text-decoration: none; cursor: pointer;" onclick="window.location.href='detail.html';">发传单</a></td>
                            <td>zhangsan</td>
							<td>2020-10-10</td>
							<td>2020-10-20</td>
						</tr>
                        <tr class="active">
                            <td><input type="checkbox" /></td>
                            <td><a style="text-decoration: none; cursor: pointer;" onclick="window.location.href='detail.html';">发传单</a></td>
                            <td>zhangsan</td>
                            <td>2020-10-10</td>
                            <td>2020-10-20</td>
                        </tr>--%>
					</tbody>
				</table>
			</div>

			<!--分页查询的表单-->
			<div style="height: 50px; position: relative;top: 30px;">
				<div id="activityPage"></div>
			</div>
			
		</div>
		
	</div>
</body>
</html>