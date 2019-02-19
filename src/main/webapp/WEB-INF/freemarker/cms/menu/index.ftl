<link rel="stylesheet" href="${(project.staticDomain)!}/libs/zTree/zTreeStyle/zTreeStyle.css" type="text/css">
<link rel="stylesheet" type="text/css" href="${ (project.staticDomain)! }/css/menu/index.css">
<div class="right_col" role="main">
	<div class="">
		<div class="page-title">
		  <div class="title_left">
			<h3>菜单管理 </h3>
		  </div>
		  <div class="title_right">
			<div class="col-md-5 col-sm-5 col-xs-12 form-group pull-right top_search">
			  <div class="input-group">
				
			  </div>
			</div>
		  </div>
		</div>
		<div class="clearfix"></div>
		<div class="row">
			<div class="col-md-12">
				<div class="x_panel">
					<div class="x_content">	
						<!-- 提示信息 -->
						<div class="alert alert-warning" role="alert">
					      <button type="button" class="close" data-dismiss="alert" aria-label="Close"><span aria-hidden="true">×</span></button>
					      <strong>提示!</strong> <P>菜单支持排序功能，但是只支持同级排序；对菜单进行操作时，请确保菜单路径能够访问！</P>
					      <P>默认菜单(叶子)设置：</P>
					      <ol>
					      		<li><strong>菜单</strong> (路径：#，样式：无)；
					      				<ol>
							      				<li><strong>首页管理</strong> (路径：index，样式：fa fa-home)；</li>
							      				<li><strong>内容管理</strong> (路径：contents，样式：fa fa-reorder)；</li>
							      				<li>
							      						<strong>分类管理</strong> (路径：#，样式：fa fa-group)；</li>
								      					<ol>
											      				<li><strong>分类管理</strong> (路径：classification/classificationManage，样式：无)；</li>
											      				<li><strong>分类编排</strong> (路径：classification/arrangement，样式：无)；</li>
								      					</ol>
							      				</li>
							      				<li><strong>海报管理</strong> (路径：posters，样式：fa fa-image)；</li>
							      				<li><strong>入站管理</strong> (路径：tasks/list，样式：fa fa-database)；</li>
							      				<li>
							      						<strong>APK管理</strong> (路径：#，样式：fa fa-desktop)；
							      						<ol>
											      				<li><strong>发布版本</strong> (路径：apkmanager/release，样式：无)；</li>
											      				<li><strong>测试版本</strong> (路径：apkmanager/test，样式：无)；</li>
								      					</ol>
							      				</li>
							      				<li>
							      					   <strong>系统管理</strong> (路径：#，样式：fa fa-gears)
							      					   <ol>
							      					   		<li><strong>用户管理</strong> (路径：list，样式：无)；</li>
							      					   		<li><strong>角色管理</strong> (路径：role/list，样式：无)；</li>
							      					   		<li><strong>权限管理</strong> (路径：permission/list，样式：无)；</li>
							      					   		<li><strong>菜单管理</strong> (路径：menus，样式：无)；</li>
							      					   		<li><strong>操作管理</strong> (路径：operation/index，样式：无)；</li>
							      					   </ol>
							      				</li>
							      				<li>
							      					   <strong>统计管理</strong> (路径：#，样式：fa fa-bar-chart-o)
							      					   <ol>
							      					   		<li><strong>入库统计</strong> (路径：instorageStatistics，样式：无)；</li>
							      					   </ol>
							      				</li>
				      				</ol>
					      		</li>
					      </ol>
					      
		      				<P>
		      					设置后退出系统，重新登录即可生效！
		      				</P>
					    </div>
					    
						<div>
							<div class="col-sm-3 mail_list_column" style="padding: 10px 17px;">
								<div class="x_title">
								   <h2>菜单<small></small></h2>
								   <div class="clearfix"></div>
								</div>
								<div class="zTreeDemoBackground left">
									<ul id="menuTree" class="ztree"  style="overflow:auto;"></ul>
								</div>
							</div>
						
							<div class="col-sm-9 mail_view" id="basic">
								<div class="x_panel">
									<div class="x_title">
										<h2>基本信息 <small></small></h2>
										<div class="clearfix"></div>
									</div>
									<div class="x_content">
											
											<form class="form-horizontal" id="operation_form">
												  <div class="form-group">
													    <label for="name" class="col-sm-2 control-label"><strong class="red">*</strong>名称：</label>
													    <div class="col-sm-8">
													    	<input type="hidden"  id="menu_id">
													      	<input type="text" class="form-control" id="menu_name" placeholder="请输入名称..."  data-parsley-required="true" data-parsley-required-message="名称不可为空" data-parsley-maxlength="10" data-parsley-maxlength-message="名称不可超过10位">
													    </div>
												  </div>
												  <div class="form-group">
													    <label for="url" class="col-sm-2 control-label"><strong class="red">*</strong>路径：</label>
													    <div class="col-sm-8">
													      <input type="text" class="form-control" id="menu_url" placeholder="请输入访问地址..." data-parsley-required="true" data-parsley-required-message="地址不可为空">
													      <span>默认路径&nbsp;&nbsp;#</span>
													    </div>
												  </div>
												  
												   <div class="form-group">
													    <label for="url" class="col-sm-2 control-label">样式：</label>
													    <div class="col-sm-8">
													      <input type="text" class="form-control" id="menu_class"  placeholder="请输入样式..." >
													    </div>
												  </div>
												  
												  <div class="form-group">
												    <div class="col-sm-offset-2 col-sm-10">
												      <button type="button" class="btn btn-default" id="operationMenu">确定</button>
												    </div>
												  </div>
												  
											</form>
											
									</div>
								</div>
							</div> 
						</div>
					</div>
				</div>
			</div>
		</div>
	</div>
</div>

<!--添加菜单 Modal -->
<div class="modal fade" id="addMenu" tabindex="-1" role="dialog" aria-labelledby="myModalLabel">
  <div class="modal-dialog" role="document">
    <div class="modal-content">
      <div class="modal-header">
        <button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span></button>
        <h4 class="modal-title" id="myModalLabel">创建菜单</h4>
      </div>
      <div class="modal-body">
      		
      		<form class="form-horizontal" id="save_form">
				  <div class="form-group">
				    <label for="name" class="col-sm-2 control-label"><strong class="red">*</strong>名称：</label>
				    <div class="col-sm-10">
				    	<input type="hidden"  id="parent" name = "parent">
				      	<input type="text" class="form-control" id="name" name = "name" placeholder="请输入名称..." data-parsley-required="true" data-parsley-required-message="名称不可为空" data-parsley-maxlength="10" data-parsley-maxlength-message="名称不可超过10位">
				    </div>
				  </div>
				  <div class="form-group">
				    <label for="url" class="col-sm-2 control-label"><strong class="red">*</strong>路径：</label>
				    <div class="col-sm-10">
				      <input type="text" class="form-control" id="url" name = "url" value="#"  placeholder="请输入访问地址..." data-parsley-required="true" data-parsley-required-message="地址不可为空">
				      <span>默认路径&nbsp;&nbsp;#</span>
				    </div>
				  </div>
				   <div class="form-group">
				    <label for="url" class="col-sm-2 control-label">样式：</label>
				    <div class="col-sm-10">
				      <input type="text" class="form-control" id = "menuClass"  placeholder="请输入样式..." >
				    </div>
				  </div>
			</form>
      		
      </div>
      <div class="modal-footer">
        <button type="button" class="btn btn-default" data-dismiss="modal">取消</button>
        <button type="button" class="btn btn-primary" id="saveMenu">确定</button>
      </div>
    </div>
  </div>
</div>
        
<script type="text/javascript">
	var treeJson = '${(menus)!}';
	var nodes = null;
	if(treeJson != null && treeJson != '' && treeJson !=""){
		nodes = JSON.parse(treeJson);
	}
</script>

<script src="${ (project.staticDomain)! }/libs/zTree/jquery.ztree.core-3.5.js"></script>
<script src="${ (project.staticDomain)! }/libs/zTree/jquery.ztree.excheck-3.5.js"></script>
<script src="${ (project.staticDomain)! }/libs/zTree/jquery.ztree.exedit-3.5.js"></script>
<script src="${ (project.staticDomain)! }/libs/zTree/jquery.ztree.exhide-3.5.js"></script>
<script src="${ (project.staticDomain)! }/js/menu/index.js"></script>