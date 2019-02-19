<link rel="stylesheet" href="${(project.staticDomain)!}/libs/bootstrap-multiselect/css/bootstrap-multiselect.css">
 <link rel="stylesheet" href="${(project.staticDomain)!}/libs/zTree/zTreeStyle/zTreeStyle.css" type="text/css">
<link rel="stylesheet" href="${(project.staticDomain)!}/css/task/index.css">
<link rel="stylesheet" href="${(project.staticDomain!)}/libs/parsley.js-2.7.2/parsley.css"">
 <!-- page content -->
<div class="right_col" role="main">
<div class="page-title">
      <div class="title_left">
        	<h3 >APK管理 / <span id="title_apk"> </span></h3>
      </div>
      <div class="title_right">
	        <div class="col-md-5 col-sm-5 col-xs-12 form-group pull-right top_search">
	          <div class="input-group">
	            
	          </div>
	        </div>
      </div>
 </div>

  <div class="clearfix">
	    <div class="clearfix"></div>
		    <div class="row">
				 <div class="col-md-12 col-sm-12 col-xs-12">
		                <div class="x_panel">
			                  <div class="x_content">
				                  <div class="operate" style="text-align:right">
										<a class="btn btn-primary"  data-status="2" id="add_user" style="display:inline;"><i class="fa fa-cloud-upload"></i>上传</a>
				                    </div>
				                    <table id="table" class="table table-hover table-striped table-bordered dt-responsive nowrap" cellspacing="0" width="100%">
								        <thead>
								            <tr>
								            	<th>主版本号</th>
								            	<th>子版本号</th>
								            	<th>APK入口</th>
								            	<th>MD5</th>
								            	<th>文件大小（bytes）</th>
								            	<th>文件名称</th>
								            	<th>强制升级</th>
								            	<th>描述</th>
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
          	<form id="add_form" class="form-horizontal form-label-left" data-parsley-validate="" enctype="multipart/form-data" method="post">
          	  <input name="type" id="modal-type" type="hidden" class="form-control"/>
		      <div class="form-group">
			        <label for="mainVersion" class="control-label col-md-3 col-sm-3 col-xs-12"><span class="required">*</span>主版本号：</label>
			        <div class="col-md-6 col-sm-6 col-xs-12">
			            <input name="mainVersion" id="mainVersion" type="text" class="form-control" required data-parsley-type="number"/>
			        </div>
		      </div>
		      
		     <div class="form-group">
			        <label for="childVersion" class="control-label col-md-3 col-sm-3 col-xs-12"><span class="required">*</span>子版本号：</label>
			        <div class="col-md-6 col-sm-6 col-xs-12">
			            <input name="childVersion" id="childVersion" type="text" class="form-control" required data-parsley-type="integer"/>
			        </div>
		      </div>
		      
              <div class="form-group">
	                <label for="apk_file" class="control-label col-md-3 col-sm-3 col-xs-12"><span class="required">*</span>压缩文件：</label>
	                <div class="col-md-6 col-sm-6 col-xs-12">
	                    <input name="apk_file" type="file" required class="form-control" required data-parsley-pattern=".*(.zip|.ZIP)$"/>
	                    <span>只支持.zip格式</span>
	                </div>
	               
              </div>
              
               <div class="form-group">
			        <label for="md5" class="control-label col-md-3 col-sm-3 col-xs-12"><span class="required">*</span>文件MD5：</label>
			        <div class="col-md-6 col-sm-6 col-xs-12">
			            <input name="md5" id="md5" type="text" class="form-control" required/>
			        </div>
		      </div>
		      
		       <div class="form-group">
			        <label for="entry" class="control-label col-md-3 col-sm-3 col-xs-12"><span class="required">*</span>APK主入口：</label>
			        <div class="col-md-6 col-sm-6 col-xs-12">
			            <input name="entry" id="entry" type="text" class="form-control" required value="main.qml"/>
			        </div>
		      </div>
		      
		      <div class="form-group">
			        <label for="force" class="control-label col-md-3 col-sm-3 col-xs-12">强制升级：</label>
			        <div class="col-md-6 col-sm-6 col-xs-12">
			            <input name="force" id="force" type="checkbox"  style="width:16px;height:16px;"/>
			        </div>
		      </div>
              
		      <div class="form-group">
	                <label for="description" class="control-label col-md-3 col-sm-3 col-xs-12">描述：</label>
	                <div class="col-md-6 col-sm-6 col-xs-12">
	                    <textarea name="description" id="description" type="text" class="form-control" ></textarea>
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
<script>
	  	var userDisableFlag = false;
	  	var userEditFlag = false;
	  	var userEditPasswordFlag = false;
       	var userType = 0;	
       	<#if type??>
       		var type = "${type}";
       		$("#modal-type").val(type);
       		$("#title_apk").text("测试版本");
       		if(type==1){
	       		$("#title_apk").text("发布版本");
       		}
       	</#if>
</script>
<script src="${ (project.staticDomain)! }/libs/loading/js/loading.js"></script>
<script src="${ (project.staticDomain)! }/libs/bootstrap-multiselect/js/bootstrap-multiselect.js"></script>
<script src="${ (project.staticDomain)! }/libs/zTree/jquery.ztree.core-3.5.js"></script>
<script src="${ (project.staticDomain)! }/libs/zTree/jquery.ztree.excheck-3.5.js"></script>	
<script src="${ (project.staticDomain)! }/libs/jquery-form/jquery-form-20131225.min.js"></script>
<script src="${ (project.staticDomain)! }/libs/parsley.js-2.7.2/parsley.min.js"></script>
<script src="${ (project.staticDomain)! }/libs/parsley.js-2.7.2/i18n/zh_cn.js"></script>
<script src="${ (project.staticDomain)! }/js/apkmanager/index.js"></script>