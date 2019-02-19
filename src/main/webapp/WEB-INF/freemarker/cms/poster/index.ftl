<link href="${ (project.staticDomain)! }/css/poster/poster.css" rel="stylesheet">
<!-- page content -->
<div class="right_col" role="main">
  <div class="">
    <div class="page-title">
      <div class="title_left">
        <h3> 海报列表 </h3>
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
          	<div class="search">
				  <div class="form-group" style="display:inline-block;>
				    <label class="control-label" for="source">来源：</label>
				    <select class="form-control" id="source" style="display:inline-block;width:200px">
				    	<option value="">全部</option>
				    	<#list sources! as source>
				    		<option value="${(source.name)! }">${source.name }</option>
				    	</#list>
				    </select>
				  </div>
				  <a class="btn btn-primary" data-toggle="modal" data-target=".bs-example-modal-lg" style="float:right;display:none;" id="upload"  ><i class="fa fa-plus-square-o"></i> 上传</a>
			 </div>
           	 <div class="search-br"></div>
	      		
            <div class="row" id="posters">
              
            </div>
            <!-- 分页 -->
            <div class="text-center">
            	<ul class="pagination" id="pagination"></ul>
            </div>
            
          </div>
        </div>
      </div>
    </div>
  </div>
</div>
<!-- /page content -->

<!-- modal begin-->
<div class="modal fade bs-example-modal-lg" tabindex="-1" role="dialog" aria-hidden="true">
    <div class="modal-dialog modal-lg">
      <div class="modal-content">

        <div class="modal-header">
          <button type="button" class="close" data-dismiss="modal"><span aria-hidden="true">×</span>
          </button>
          <h4 class="modal-title" id="myModalLabel">添加海报</h4>
        </div>
        <div class="modal-body">
          	<form id="add_form" class="form-horizontal form-label-left" data-parsley-validate="" enctype="multipart/form-data" method="post" action="${base}/posters/photo">
              <div class="form-group">
                <label for="middle-name" class="control-label col-md-3 col-sm-3 col-xs-12"><span class="required" style="color:red">*</span>海报 </label>
                <div class="col-md-6 col-sm-6 col-xs-12">
                    <input name="poster_file" type="file" required class="form-control"/>
                    <span id="poster_required" class="requireds required" style="color:red"></span>
                </div>
                
              </div>
              <div class="form-group">
                <label class="control-label col-md-3 col-sm-3 col-xs-12">标题</label>
                <div class="col-md-6 col-sm-6 col-xs-12">
                  <input class="form-control" name="description" placeholder="添加标题..." id="description"></input>
               	  <span id="desc_required" class="requireds" style="color:red"></span>
                </div>
              </div>
            </form>
        </div>
        <div class="modal-footer">
          <button type="button" class="btn btn-default" data-dismiss="" id="cancle_sure">取消</button>
          <button type="button" class="btn btn-primary" id="add_sure">确定</button>
        </div>

      </div>
    </div>
</div>

<script>
	   var checkedArray =[];
	   var retryFlag = false;
       var userType = 0;	
	   <#if user??>
	      	<#if  user.type??>
       		userType = "${user.type}";
       		</#if>
	      
	       if( userType == "1"){
      	   		$("#upload").css("display","inline");
      	   }else{
	       //初始化将测试集包含的用例存在数组里面
		       <#if  user.operations??>
		            <#list  user.operations as item>
		                 var operationCode = "${item.code}";
		                if(operationCode == "posters.upload" ){
		                 	$("#upload").css("display","inline");
		                }
		                 
		            </#list>
		       </#if>
		   }
	   </#if>
</script>
<!-- modal end-->
<script src="${ (project.staticDomain)! }/libs/twbs-pagination/jquery.twbsPagination.js" type="text/javascript"></script>
<script src="${ (project.staticDomain)! }/libs/jquery-form/jquery-form-20131225.min.js"></script>
<script src="${ (project.staticDomain)! }/js/poster/index.js" type="text/javascript"></script>

