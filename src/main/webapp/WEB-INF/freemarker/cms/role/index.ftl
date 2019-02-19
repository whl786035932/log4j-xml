<link rel="stylesheet" href="${(project.staticDomain)!}/libs/zTree/zTreeStyle/zTreeStyle.css" type="text/css">
<link rel="stylesheet" href="${(project.staticDomain)!}/css/task/index.css">
 <!-- page content -->
<div class="right_col" role="main">
<div class="page-title">
      <div class="title_left">
        <h3>角色管理 </h3>
      </div>

      <div class="title_right">
        <div class="col-md-5 col-sm-5 col-xs-12 form-group pull-right top_search">
          <div class="input-group">
            
          </div>
        </div>
      </div>
    </div>

    <div class="clearfix"></div>
  <div class="clearfix">

    <div class="clearfix"></div>

    <div class="row">
		 <div class="col-md-12 col-sm-12 col-xs-12">
                <div class="x_panel">
                  <div class="x_content">
                  <div class="operate" style="text-align:right">
						<!--<a class="btn btn-primary" id="add_classify">添加</a>-->
						<a class="btn btn-primary"  data-status="2" id="add_role" style="display:none;"><i class="fa fa-cloud-upload"></i>添加</a>
                  		<a class="btn btn-danger foright"  id="delete" style="margin-right:0;display:none;"><i class="fa fa-remove"></i> 删除</a>
                    </div>
                    <table id="table" class="table table-hover table-striped table-bordered dt-responsive nowrap" cellspacing="0" width="100%">
				        <thead>
				            <tr>
				            	<th><input type="checkbox" id="chk_all" name="chk_all"></th>
				            	<th>名称</th>
				            	<th>描述</th>
				                <th>创建时间</th>
				                <th>修改时间</th>
				                <th>操作</th>
				            </tr>
				        </thead>
				    </table>
                  </div>
                </div>
              </div>
    </div>
  </div>
</div>

<!-- /page content -->
<!-- add   modal begin-->
<div class="modal fade bs-example-modal-lg" tabindex="-1" role="dialog" aria-hidden="true" id="addModel">
    <div class="modal-dialog modal-lg">
      <div class="modal-content">

        <div class="modal-header">
          <button type="button" class="close" data-dismiss="modal"><span aria-hidden="true">×</span>
          </button>
          <h4 class="modal-title" id="myModalLabel"></h4>
        </div>
        <div class="modal-body">
          	<form id="add_form" class="form-horizontal form-label-left" data-parsley-validate="" enctype="multipart/form-data" method="post" >
              <div class="form-group">
                <label for="middle-name" class="control-label col-md-3 col-sm-3 col-xs-12"><span class="required">*</span>名称</label>
                <div class="col-md-6 col-sm-6 col-xs-12">
                	<input name="id" id="id" type="hidden" required class="form-control"/>
                    <input name="name" id="modal-name" type="text" class="form-control"/>
                     <span id="name_required" class="requireds" style="color:red"></span>
                </div>
              </div>
              
              <div class="form-group">
                <label for="middle-name" class="control-label col-md-3 col-sm-3 col-xs-12">描述</label>
                <div class="col-md-6 col-sm-6 col-xs-12">
                    <textarea name="description" id="modal-description" type="text" class="form-control"  style="resize:none" ></textarea>
                    <br>
                </div>
              </div>
              
            </form>
        </div>
        <div class="modal-footer">
          <button type="button" class="btn btn-default" data-dismiss="modal">取消</button>
          <button type="button" class="btn btn-primary" id="add_sure">确定</button>
        </div>

      </div>
    </div>
</div>  <!--add modal end-->

<!--update modal begin-->
<div class="modal fade bs-example-modal-lg" tabindex="-1" role="dialog" aria-hidden="true" id="updateModel">
    <div class="modal-dialog modal-lg">
      <div class="modal-content">
		
        <div class="modal-header">
          <button type="button" class="close" data-dismiss="modal"><span aria-hidden="true">×</span>
          </button>
          <h4 class="modal-title" id="updateMyModalLabel"></h4>
        </div>
        <div class="modal-body">
          	<form id="update_form" class="form-horizontal form-label-left" data-parsley-validate="" enctype="multipart/form-data" method="post" >
              <div class="form-group">
                <label for="middle-name" class="control-label col-md-3 col-sm-3 col-xs-12"><span class="required">*</span>名称</label>
                <div class="col-md-6 col-sm-6 col-xs-12">
                	<input name="id" id="updateId" type="hidden" required class="form-control"/>
                    <input name="name" id="modal-update-name" type="text" class="form-control"/>
                     <span id="update_name_required" class="requireds" style="color:red"></span>
                </div>
              </div>
              <div class="form-group">
                <label for="middle-name" class="control-label col-md-3 col-sm-3 col-xs-12">描述</label>
                <div class="col-md-6 col-sm-6 col-xs-12">
                    <textarea name="description" id="modal-update-description" type="text" class="form-control"  style="resize:none" ></textarea>
                    <span id="update_ndescription_required" class="requireds" style="color:red"></span>
                    <br>
                </div>
              </div>
            </form>
        </div>
        <div class="modal-footer">
          <button type="button" class="btn btn-default" data-dismiss="modal">取消</button>
          <button type="button" class="btn btn-primary" id="update_sure">确定</button>
        </div>

      </div>
    </div>
</div> <!--update modal end-->

<!-- 分配权限 begin-->
<div class="modal fade" id="add_permission" tabindex="-1" role="dialog" aria-labelledby="classify_myModalLabel">
 <input type="hidden" id="role_permissionId"/>
  <div class="modal-dialog modal-lg" role="document">
    <div class="modal-content">
	      <div class="modal-header">
	        <button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span></button>
	        <h4 class="modal-title" id="operation_myModalLabel">分配权限</h4>
	      </div>
	      <div class="modal-body">
	      	<div class="row">
		      	<div class="col-md-6">
		      		<span class="label label-primary">操作权限</span>
			      	<input type="hidden" id="operationIds"></input>
			      	<ul id="operationTree" class="ztree" style="height: 400px;overflow: auto;"></ul>
		      	</div>
		      	
		      	<div class="col-md-6">
		      		<span class="label label-success">菜单权限</span>
		      		<input type="hidden" id="menuIds"></input>
	      			<ul id="menuTree" class="ztree" style="height: 400px;overflow: auto;"></ul>
		      	</div>
	      	</div>
	      </div>
	      <div class="modal-footer">
	        <button type="button" class="btn btn-default" data-dismiss="modal">取消</button>
	        <button type="button" class="btn btn-primary" id="update_permisson_submit" data-dismiss="modal">确定</button>
	      </div>
    </div><!--modal-operation-->
</div>
</div>
<!--分配权限 modal end-->


<script src="${ (project.staticDomain)! }/libs/loading/js/loading.js"></script>
<script src="${ (project.staticDomain)! }/js/role/index.js"></script>
<script src="${ (project.staticDomain)! }/libs/zTree/jquery.ztree.core-3.5.js"></script>
<script src="${ (project.staticDomain)! }/libs/zTree/jquery.ztree.excheck-3.5.js"></script>
<script src="${ (project.staticDomain)! }/libs/zTree/jquery.ztree.exedit-3.5.js"></script>
<script src="${ (project.staticDomain)! }/libs/zTree/jquery.ztree.exhide-3.5.js"></script>
<script>
	  	var roleEditFlag = false;
	  	var updatePermissionFlag = false;
	  	var userType = 0;	
       //初始化将测试集包含的用例存在数组里面
      <#if user??>
      	  	<#if  user.type??>
       		userType = "${user.type}";
       		</#if>
      	  
      	   if( userType == "1"){
      	   		$("#add_role").css("display","inline");
      	   		$("#delete").css("display","inline");
      	   		roleEditFlag = true;
      	   		updatePermissionFlag = true;
      	   }else{
      	   	 <#if user.operations??>
	            <#list  user.operations as item>
	                 var operationCode = "${item.code}";
	                if(operationCode == "role.add" ){
	                 	$("#add_role").css("display","inline");
	                }else if(operationCode == "role.delete" ){
	                	$("#delete").css("display","inline");
	                }else if(operationCode == "role.edit" ){
	                	roleEditFlag = true;
	                }else if(operationCode == "role.updatePermission" ){
	                	updatePermissionFlag = true;
	                }
	                 
	            </#list>
	         </#if>
      	   }
	      
	 </#if>
	
</script>