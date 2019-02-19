<link rel="stylesheet" href="${(project.staticDomain)!}/css/task/index.css">
 <!-- page content -->
<div class="right_col" role="main">
<div class="page-title">
      <div class="title_left">
        <h3>入站管理 </h3>
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
	        	<label class="control-label" for="title">题名</label>
		        <div class="control-div">
		        	<input type="text" id="title" class="form-control">
		        </div>
	    	</div>
	    	<div class="form-group col-37">
	        	<label class="control-label col-md-2 col-sm-2 col-xs-12">播放日期</label>
		        <div class="control-div date-div">
		        	<input type="text" class="form-control" id="publishTimeBegin" placeholder="" onClick="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss'})">
		        	<input type="text" class="form-control" id="publishTimeEnd" placeholder="" onClick="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss'})">
		        </div>
	    	</div>
	    	<div class="form-group col-37" style="margin-left: 35px;">
	        	<label class="control-label col-md-2 col-sm-2 col-xs-12">入站时间</label>
		        <div class="control-div date-div">
		        	<input type="text" class="form-control" id="insertedBegin" placeholder="" onClick="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss'})">
		        	<input type="text" class="form-control" id="insertedEnd" placeholder="" onClick="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss'})">
		        </div>
	    	</div>
	    	
    	</div>
    	<div class="form-horizontal clearfix">
    		<div class="form-group col-25">
	        	<label class="control-label" for="shelve-status">入站状态</label>
		        <div class="control-div">
		        	<select id="status" class="form-control">
                        <option value="">全部</option>
                        <option value="0">进行中</option>
                        <option value="1">成功</option>
                        <option value="2">失败</option>
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
						<!--<a class="btn btn-primary" id="add_classify">添加</a>-->
						<a class="btn btn-primary"  data-status="2" id="batch_retrycdn" style="display:none;"><i class="fa fa-cloud-upload" ></i> 重试</a>
                  		<a class="btn btn-danger foright"  id="delete" style="margin-right:0;display:none;" ><i class="fa fa-remove" ></i> 删除</a>
                    </div>
                    <table id="table" class="table table-hover table-striped table-bordered dt-responsive nowrap" cellspacing="0" width="100%">
				        <thead>
				            <tr>
				            	<th><input type="checkbox" id="chk_all" name="chk_all"></th>
				            	<th>正题名</th>
				            	<th>cdn回调</th>
				                <th>播出时间</th>
				                <th>创建时间</th>
				                <th>CDN反馈时间</th>
				                <th>任务状态</th>
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

<!-- 热加入分类Modal -->
<div class="modal fade" id="add_classify" tabindex="-1" role="dialog" aria-labelledby="myModalLabel">
  <div class="modal-dialog" role="document">
    <div class="modal-content">
      <div class="modal-header">
        <button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span></button>
        <h4 class="modal-title" id="myModalLabel">分类操作</h4>
      </div>
      <div class="modal-body">
      	<ul id="classifyTree" class="ztree"></ul>
      </div>
      <div class="modal-footer">
        <button type="button" class="btn btn-default" data-dismiss="modal">取消</button>
        <button type="button" class="btn btn-primary" id="fire_submit">确定</button>
      </div>
    </div>
  </div>
</div>

<!-- /page content -->

<script src="${ (project.staticDomain)! }/libs/loading/js/loading.js"></script>
<script src="${ (project.staticDomain)! }/js/task/index.js"></script>
<script>
		var retryFlag = false;
       //初始化将测试集包含的用例存在数组里面
       	var userType = 0;	
	      <#if user??>
	      	<#if  user.type??>
       			userType = "${user.type}";
       		</#if>
	      	 
	      	 if( userType == "1"){
      	   		 $("#batch_retrycdn").css("display","inline");
		         retryFlag = true;
				 $("a[name='retry-content']").css("display","inline");
				 $("#delete").css("display","inline");
      	    }else{
      	    	<#if user.operations??>
		            <#list  user.operations as item>
		                var operationCode = "${item.code}";
		                if(operationCode == "station.retry" ){
		                 	 $("#batch_retrycdn").css("display","inline");
		                 	 retryFlag = true;
				             $("a[name='retry-content']").css("display","inline");
		                }else if(operationCode == "station.delete" ){
		             	   		$("#delete").css("display","inline");
		                }
		            </#list>
		       </#if>
      	    }
		       
		 </#if>
</script>