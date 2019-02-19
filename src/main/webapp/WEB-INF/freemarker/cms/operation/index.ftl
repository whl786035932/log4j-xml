<link rel="stylesheet" type="text/css" href="${ (project.staticDomain)! }/css/operation/index.css">
<link rel="stylesheet" href="${(project.staticDomain)!}/libs/zTree/zTreeStyle/zTreeStyle.css" type="text/css">
 <!-- page content -->
<div class="right_col" role="main">
  <div class="">
    <div class="page-title">
      <div class="title_left">
        <h3>操作管理 </h3>
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
                  	<!--提示框-->
                  	<div class="alert alert-warning" role="alert">
					      <button type="button" class="close" data-dismiss="alert" aria-label="Close"><span aria-hidden="true">×</span></button>
					      <strong>提示!</strong> <P>请确保操作树的操作编码对应以下code值！</P>
					      <P>默认操作(叶子)设置：</P>
					      <ol>
					      		<li><strong>操作 </strong>(编码：#，拦截url：无)；
					      			<ol>
					      				<li><strong>内容管理</strong> (编码：#，拦截url：无)；
					      					<ol>
					      						<li><strong>内容上架</strong> (编码：contents.shelve，拦截url：无)；</li>
					      						<li><strong>内容下架</strong> (编码：contents.unShelve，拦截url：无)；</li>
					      						<li><strong>内容删除</strong> (编码：contents.delete，拦截url：无)；</li>
					      						<li><strong>内容加入分类</strong> (编码：contents.addClassify，拦截url：无)；</li>
					      						<li><strong>内容编辑</strong> (编码：contents.edit，拦截url：无)；</li>
					      						<li><strong>导出Excel</strong> (编码：contents.exportExcel，拦截url：无)；</li>
					      						
					      					</ol>
					      				</li>
					      				<li><strong>海报管理</strong> (编码：#，拦截url：无)；
					      					<ol>
					      						<li><strong>上传海报</strong> (编码：posters.upload，拦截url：无)；</li>
					      					</ol>
					      				</li>
					      				<li><strong>入站管理</strong> (编码：#，拦截url：无)；
					      					<ol>
					      						<li><strong>入站管理-重试</strong> (编码：station.retry，拦截url：无)；</li>
					      						<li><strong>入站管理-删除</strong> (编码：station.delete，拦截url：无)；</li>
					      					</ol>
					      				</li>
					      				<li><strong>系统管理</strong> (编码：#，拦截url：无)；
					      					<ol>
					      						<li><strong>用户管理</strong> (编码：#，拦截url：无)；
					      							<ol>
					      								<li><strong>添加用户</strong> (编码：user.add，拦截url：无)；</li>
					      								<li><strong>修改用户密码</strong> (编码：user.editPassword，拦截url：无)；</li>
					      								<li><strong>用户编辑</strong> (编码：user.edit，拦截url：无)；</li>
					      								<li><strong>用户禁用启用</strong> (编码：user.disable，拦截url：无)；</li>
					      								<li><strong>删除用户</strong> (编码：user.delete，拦截url：无)；</li>
					      							</ol>
					      						</li>
					      						<li><strong>角色管理</strong> (编码：#，拦截url：无)；
					      							<ol>
					      								<li><strong>角色编辑</strong> (编码：role.edit，拦截url：无)；</li>
					      								<li><strong>角色-分配权限</strong> (编码：role.updatePermission，拦截url：无)；</li>
					      								<li><strong>角色-删除</strong> (编码：role.delete，拦截url：无)；</li>
					      								<li><strong>角色-添加</strong> (编码：role.add，拦截url：无)；</li>
					      							</ol>
					      						</li>
					      					</ol>
					      				</li>
					      				<li><strong>分类管理</strong> (编码：#，拦截url：无)；
					      					<ol>
					      						<li><strong>分类编排</strong> (编码：#，拦截url：无)；
					      							<ol>
					      								<li><strong>分类编排-删除</strong> (编码：classification.deleteContent，拦截url：无)；</li>
					      							</ol>
					      						</li>
					      					</ol>
					      				</li>
					      			</ol>
					      		</li>
					      </ol>
					      
		      				<P>
		      					设置后退出系统，重新登录即可生效！
		      				</P>
					  </div>
                  	<!--提示框-->
                   <div>
                      <div class="col-sm-3 mail_list_column" style="padding: 10px 17px;">
		                  <div class="x_title">
		                    <h2>操作树<small></small></h2>
		                    <div class="clearfix"></div>
		                  </div>
                        <!--<button id="compose" class="btn btn-sm btn-primary btn-block" type="button"><i class="fa fa-tree"></i>操作树</button>-->
                        <div class="zTreeDemoBackground left">
							<ul id="columnTree" class="ztree"  style="height:550px;overflow:auto"></ul>
							
						</div>
                      </div>
                      <!-- /MAIL LIST -->

                      <!-- CONTENT MAIL -->
                      <div class="col-sm-9 mail_view" id="basic">
					  		<div class="x_panel">
                  <div class="x_title">
                    <h2>基本信息 <small></small></h2>
                    <div class="clearfix"></div>
                  </div>
                  <div class="x_content">
                    <br />
                    <form id="demo-form2" data-parsley-validate class="form-horizontal form-label-left">

                      <div class="form-group">
                        <label class="control-label col-md-3 col-sm-3 col-xs-12" for="first-name"> <span class="required" style="color:red">*</span>名称
                        </label>
                        <div class="col-md-6 col-sm-6 col-xs-12">
                          <input type="text" id="name" name="name" required="required" attr="" class="form-control col-md-7 col-xs-12">
                          <input type="hidden" id="id" name="id">
                          <span id="name-required" class="requireds check_name" style="color:red"></span>
                        </div>
                      </div>
                      
                       <div class="form-group">
                        <label class="control-label col-md-3 col-sm-3 col-xs-12" for="code"> <span class="required" style="color:red">*</span>操作编码
                        </label>
                        <div class="col-md-6 col-sm-6 col-xs-12">
                          <input type="text" id="code" name="code" required="required" attr="" class="form-control col-md-7 col-xs-12">
                          <span id="code-required" class="requireds check_name" style="color:red"></span>
                        </div>
                      </div>
                      <!--
                      <div class="form-group">
                        <label class="control-label col-md-3 col-sm-3 col-xs-12" for="permision_type"> <span class="required" style="color:red">*</span>权限分类
                        </label>
                        <div class="col-md-6 col-sm-6 col-xs-12">
                         <select id="permision_type" class="form-control">
	                        <option value="">选择分类</option>
	                    </select>
                          <span id="code-required" class="requireds check_name" style="color:red"></span>
                        </div>
                      </div>
                      -->
                      
                      <div class="form-group">
	                        <label class="control-label col-md-3 col-sm-3 col-xs-12"> 拦截url</label>
	                        <div class="col-md-6 col-sm-6 col-xs-12" style="padding-top: 5px;">
	                          	<input type="text" name="url" id="edit_url" value="" class="form-control">
	                          	<span id="url_required" class="requireds_add check_name" style="color:red"></span> 
	                        </div>
                     </div>
                      
                      
                      <div class="ln_solid"></div>
                      <div class="form-group">
                        <div class=" col-md-9 col-sm-9 col-xs-12" style="text-align: center;">
                          <button type="button" id="edit-save" class="btn btn-primary check_name_button"><i class="fa fa-save"></i> 保存</button>
                        </div>
                      </div>

                    </form>
                  </div>
                </div>
                      </div>
                      <!-- /CONTENT MAIL -->
                    </div>
                  </div>
                </div>
              </div>
    </div>
  </div>
</div>

<!---添加节点-->
<div class="modal fade" id="addChannel" tabindex="-1" role="dialog"  aria-hidden="true">
	<div class="modal-dialog">
		<div class="modal-content">
			<div class="modal-header">
	        	<h4 class="modal-title" id="myModalLabel">添加操作</h4>
			</div>
        	<div class="modal-body clearfix">
		    	<div class="x_content">
		    	<br />
                	<form id="demo-form2" class="form-horizontal form-label-left" action="">
                     	<div class="form-group">
	                        <label class="control-label col-md-3 col-sm-3 col-xs-12"> <span class="required" style="color:red">*</span>名称</label>
	                        <div class="col-md-6 col-sm-6 col-xs-12" style="padding-top: 5px;">
	                          	<input type="text" name="add_name" id="add_name" value="" class="form-control">
	                          	<input type="hidden" name="id_add" id="id_add" value="">
	                          	
	                          	<span id="add_name_required" class="requireds_add check_name" style="color:red"></span> 
	                        </div>
                    	</div>
                    	
                    	<div class="form-group">
	                        <label class="control-label col-md-3 col-sm-3 col-xs-12"> <span class="required" style="color:red">*</span>编码</label>
	                        <div class="col-md-6 col-sm-6 col-xs-12" style="padding-top: 5px;">
	                          	<input type="text" name="add_code" id="add_code" value="" class="form-control">
	                          	<span id="add_code_required" class="requireds_add check_name" style="color:red"></span> 
	                        </div>
                    	</div>
                    	
                    	<div class="form-group">
	                        <label class="control-label col-md-3 col-sm-3 col-xs-12"> 拦截url</label>
	                        <div class="col-md-6 col-sm-6 col-xs-12" style="padding-top: 5px;">
	                          	<input type="text" name="add_url" id="add_url" value="" class="form-control">
	                          	<span id="add_url_required" class="requireds_add check_name" style="color:red"></span> 
	                        </div>
                    	</div>
                    	
                    <!--
                    <div class="form-group">
                        <label class="control-label col-md-3 col-sm-3 col-xs-12" for="permision_type"> <span class="required" style="color:red">*</span>权限分类
                        </label>
                        <div class="col-md-6 col-sm-6 col-xs-12">
                         <select id="add_permision_type" class="form-control">
	                        <option value="">选择分类</option>
	                        <#list permissons as permission>
	                     	   <option value="${permission.id}">${permission.typeName}</option>
	                        </#list>
	                    </select>
                          <span id="code-required" class="requireds check_name" style="color:red"></span>
                        </div>
                      </div>
                      -->
                      
                  </form>
                 </div><!--x_content-->
			</div><!--modal-body-->
			<div class="modal-footer">
	        	<button class="btn btn-default" id="add_cancle" type="reset">取消</button>
				<button type="button" id="add_save" class="btn btn-primary button_add">确定</button>
	    	</div>
		</div><!--modal-content-->
	</div><!--modal-dialog-->
</div><!--contentsAddColumnModal-->


<script type="text/javascript">
	var base = $("#base").attr("href");
	var treeJson = '${(treeJson)!}';
	var nodes = null;
	if(treeJson!=null&&treeJson!=''&&treeJson!=""){
		nodes = JSON.parse(treeJson);
	}
	console.log(nodes);
</script>
<script src="${ (project.staticDomain)! }/js/operation/index.js"></script>
<script src="${ (project.staticDomain)! }/libs/zTree/jquery.ztree.core-3.5.js"></script>
<script src="${ (project.staticDomain)! }/libs/zTree/jquery.ztree.excheck-3.5.js"></script>
<script src="${ (project.staticDomain)! }/libs/zTree/jquery.ztree.exedit-3.5.js"></script>
<script src="${ (project.staticDomain)! }/libs/zTree/jquery.ztree.exhide-3.5.js"></script>
<script src="${ (project.staticDomain)! }/libs/jquery-form/jquery-form-20131225.min.js"></script>
