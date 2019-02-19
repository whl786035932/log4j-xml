<link rel="stylesheet" href="${(project.staticDomain)!}/libs/bootstrap-multiselect/css/bootstrap-multiselect.css">
 <link rel="stylesheet" href="${(project.staticDomain)!}/libs/zTree/zTreeStyle/zTreeStyle.css" type="text/css">
<link rel="stylesheet" href="${(project.staticDomain)!}/css/task/index.css">
 <!-- page content -->
<div class="right_col" role="main">
<div class="page-title">
      <div class="title_left">
        <h3>用户管理 </h3>
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
    <div class="search-area clearfix">
    	<div class="form-horizontal clearfix">
    		<div class="form-group col-25">
	        	<label class="control-label" for="username">用户名</label>
		        <div class="control-div">
		        	<input type="text" id="username" class="form-control">
		        </div>
	    	</div>
	    	<div class="form-group col-25">
	        	<label class="control-label" for="nickname">昵称</label>
		        <div class="control-div">
		        	<input type="text" id="nickname" class="form-control">
		        </div>
	    	</div>
	    	
	    	<div class="form-group col-25">
	        	<label class="control-label" for="status">状态</label>
		        <div class="control-div">
		        	<select id="status" class="form-control">
                        <option value="">全部</option>
                        <option value="1">启用</option>
                        <option value="0">禁用</option>
                    </select>
		        </div>
	    	</div>
	    	 <button type="button" class="btn btn-primary" id="search"><i class="fa fa-search"></i> 查找</button>
	    	
    	</div>
    </div>

    <div class="clearfix"></div>

    <div class="row">
		 <div class="col-md-12 col-sm-12 col-xs-12">
                <div class="x_panel">
                  <div class="x_content" style="padding:0">
                  <div class="operate" style="text-align:right">
						<a class="btn btn-primary"  data-status="2" id="add_user" style="display:none;"><i class="fa fa-cloud-upload"></i>添加</a>
                  		<a class="btn btn-danger foright"  id="delete" style="margin-right:0;display:none;"><i class="fa fa-remove"></i> 删除</a>
                    </div>
                    <table id="table" class="table table-hover table-striped table-bordered dt-responsive nowrap" cellspacing="0" width="100%">
				        <thead>
				            <tr>
				            	<th><input type="checkbox" id="chk_all" name="chk_all"></th>
				            	<th>用户名</th>
				            	<th>昵称</th>
				            	<th>角色</th>
				                <th>创建时间</th>
				                <th>修改时间</th>
				                <th>状态</th>
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
                <label for="middle-name" class="control-label col-md-2 col-sm-2"><span class="required">*</span>用户名</label>
                <div class="col-md-9 col-sm-9">
                	<input name="id" id="id" type="hidden" required class="form-control"/>
                    <input name="username" id="modal-username" type="text" class="form-control"/>
                     <span id="username_required" class="requireds" style="color:red"></span>
                </div>
              </div>
              
               <div class="form-group">
                <label for="middle-name" class="control-label col-md-2 col-sm-2">分类</label>
                <div class="col-md-9 col-sm-9">
					<input name ="classificationStr" id="classificationStr" class="form-control" type="hidden"/>
					<input name="classificationName"  type="text" class="form-control" AUTOCOMPLETE="off"  id="classification-input" readonly/>   	
					
					<div class="showDiv" style="overflow:auto;">			
						<ul id="classifyTree" class="ztree" style="margin-top:0;  height: 200px;"></ul>	
					</div>
                </div>
              </div>
              
              <div class="form-group">
                <label for="middle-name" class="control-label col-md-2 col-sm-2">角色</label>
                <div class="col-md-9 col-sm-9">
                   <select id="role" class="form-control" name="role"  multiple="multiple">
                    </select>
                    <span id="nickname_required" class="requireds" style="color:red"></span>
                </div>
              </div>
              
               <div class="form-group">
                <label for="middle-name" class="control-label col-md-2 col-sm-2">昵称</label>
                <div class="col-md-9 col-sm-9">
                    <input name="nickname" id="modal-nickname" type="text" class="form-control"/>
                    <span id="nickname_required" class="requireds" style="color:red"></span>
                </div>
              </div>
              
               <div class="form-group">
                <label for="middle-name" class="control-label col-md-2 col-sm-2"><span class="required">*</span>密码</label>
                <div class="col-md-9 col-sm-9">
                    <input name="password" id="password" type="password" class="form-control"/>
                    <span id="password_required" class="requireds" style="color:red"></span>
                </div>
              </div>
              
              <div class="form-group">
                <label for="middle-name" class="control-label col-md-2 col-sm-2"><span class="required">*</span>确认密码</label>
                <div class="col-md-9 col-sm-9">
                    <input name="repassword" id="repassword" type="password" class="form-control"/>
                    <span id="repassword_required" class="requireds" style="color:red"></span>
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
                <label for="middle-name" class="control-label col-md-2 col-sm-2"><span class="required">*</span>用户名</label>
                <div class="col-md-9 col-sm-9">
                	<input name="id" id="updateId" type="hidden" required class="form-control"/>
                    <input name="username" id="modal-update-username" type="text" class="form-control"/>
                     <span id="update_username_required" class="requireds" style="color:red"></span>
                </div>
              </div>
              
              <div class="form-group">
                <label for="middle-name" class="control-label col-md-2 col-sm-2">分类</label>
                <div class="col-md-9 col-sm-9">
					<input name ="classificationStr" id="classificationStrUpdate" class="form-control" type="hidden"/>
					<input name="classificationName" class="form-control"  type="text" AUTOCOMPLETE="off"  id="classification-input-update" readonly style = "border-bottom:0px;"/>   	
					
					<div class="showDiv" id="showDivUpdate" style="overflow:auto;">			
						<ul id="classifyTreeUpdate" class="ztree" style="margin-top:0;  height: 200px;"></ul>	
					</div>
                </div>
              </div>
              
              <div class="form-group">
                <label for="middle-name" class="control-label col-md-2 col-sm-2">角色</label>
                <div class="col-md-9 col-sm-9">
                   <select id="update_role" class="form-control" name="role"  multiple="multiple">
                        <option value="">选择角色</option>
                    </select>
                    <span id="nickname_required" class="requireds" style="color:red"></span>
                </div>
              </div>
              
              <div class="form-group">
                <label for="middle-name" class="control-label col-md-2 col-sm-2">昵称</label>
                <div class="col-md-9 col-sm-9">
                    <input name="nickname" id="modal-update-nickname" type="text" class="form-control"/>
                    <span id="update_nickname_required" class="requireds" style="color:red"></span>
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


<!-- update password end-->
<div class="modal fade bs-example-modal-lg" tabindex="-1" role="dialog" aria-hidden="true" id="updatePasswordModel">
    <div class="modal-dialog modal-lg">
      <div class="modal-content">

        <div class="modal-header">
          <button type="button" class="close" data-dismiss="modal"><span aria-hidden="true">×</span>
          </button>
          <h4 class="modal-title" id="updatePasswordModalLabel"></h4>
        </div>
        <div class="modal-body">
          	<form id="update_password_form" class="form-horizontal form-label-left" data-parsley-validate="" enctype="multipart/form-data" method="post" >
              <div class="form-group">
                <label for="middle-name" class="control-label col-md-3 col-sm-3 col-xs-12"><span class="required">*</span>密码</label>
                <div class="col-md-6 col-sm-6 col-xs-12">
                  	<input name="id" id="update_password_id" type="hidden" required class="form-control"/>
                    <input name="password" id="update_password" type="password" class="form-control"/>
                    <span id="update_password_required" class="requireds" style="color:red"></span>
                    <br>
                </div>
              </div>
              
              <div class="form-group">
                <label for="middle-name" class="control-label col-md-3 col-sm-3 col-xs-12"><span class="required">*</span>确认密码</label>
                <div class="col-md-6 col-sm-6 col-xs-12">
                    <input name="repassword" id="update_repassword" type="password" class="form-control"/>
                    <span id="update_repassword_required" class="requireds" style="color:red"></span>
                    <br>
                </div>
              </div>
              
            </form>
        </div>
        <div class="modal-footer">
          <button type="button" class="btn btn-default" data-dismiss="modal">取消</button>
          <button type="button" class="btn btn-primary" id="update_password_sure">确定</button>
        </div>

      </div>
    </div>
</div>  <!--update password end-->

<script>
	  	var userDisableFlag = false;
	  	var userEditFlag = false;
	  	var userEditPasswordFlag = false;
	  	 
       	var userType = 0;	
       //初始化将测试集包含的用例存在数组里面
      <#if user??>
      	   	<#if  user.type??>
       			userType = "${user.type}";
       		</#if>
      	   if( userType == "1"){
      	   		$("#add_user").css("display","inline");
      	   		$("#delete").css("display","inline");
      	   		userDisableFlag = true;
      	   		userEditFlag = true;
      	   		userEditPasswordFlag = true;
      	   }else{
	      	   	<#if  user.operations??>
		            <#list  user.operations as item>
		                 var operationCode = "${item.code}";
		                if(operationCode == "user.add" ){
		                 	$("#add_user").css("display","inline");
		                }else if(operationCode == "user.delete" ){
		                	$("#delete").css("display","inline");
		                }else if(operationCode == "user.disable"){
		                	userDisableFlag = true;
		                }else if(operationCode == "user.edit" ){
		                	userEditFlag = true;
		                }else if(operationCode == "user.editPassword" ){
		                	userEditPasswordFlag = true;
		                }
		                 
		            </#list>
		       </#if>
      	   }
	       
	</#if>
	
</script>
<script src="${ (project.staticDomain)! }/libs/loading/js/loading.js"></script>
<script src="${ (project.staticDomain)! }/libs/bootstrap-multiselect/js/bootstrap-multiselect.js"></script>
<script src="${ (project.staticDomain)! }/libs/zTree/jquery.ztree.core-3.5.js"></script>
<script src="${ (project.staticDomain)! }/libs/zTree/jquery.ztree.excheck-3.5.js"></script>	
<script src="${ (project.staticDomain)! }/js/user/index.js"></script>