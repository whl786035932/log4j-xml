 <link rel="stylesheet" href="${(project.staticDomain)!}/libs/zTree/zTreeStyle/zTreeStyle.css" type="text/css">
<link rel="stylesheet" href="${(project.staticDomain)!}/css/content/index.css">
 <!-- page content -->
<div class="right_col" role="main">
     <div class="page-title">
       <div class="title_left">
         <h3>内容列表</h3>
       </div>

       <div class="title_right">
         <div class="col-md-5 col-sm-5 col-xs-12 form-group pull-right top_search">
           <div class="input-group">
             
           </div>
         </div>
       </div>
     </div>
  <div class="clearfix">
  <form id="listForm">
    <div class="search-area clearfix">
    	<div class="form-horizontal clearfix">
    		<div class="form-group col-25">
	        	<label class="control-label" for="title">标题</label>
		        <div class="control-div">
		        	<input type="text" id="title" class="form-control">
		        </div>
	    	</div>
	    	<div class="form-group col-37">
	        	<label class="control-label col-md-2 col-sm-2 col-xs-12">播放日期</label>
		        <div class="control-div date-div">
		        	<input type="text" class="form-control" id="play-start" placeholder="" onClick="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss'})">
		        	<input type="text" class="form-control" id="play-end" placeholder="" onClick="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss'})">
			    </div>
	    	</div>
	    	<div class="form-group col-37" style="margin-left: 35px;">
	        	<label class="control-label col-md-2 col-sm-2 col-xs-12">入站时间</label>
		        <div class="control-div date-div">
		        	<input type="text" class="form-control" id="site-start" placeholder="" onClick="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss'})">
		        	<input type="text" class="form-control" id="site-end" placeholder="" onClick="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss'})">
			    </div>
	    	</div>
	    	
    	</div>
    	<div class="form-horizontal clearfix">
    		<div class="form-group col-25">
	        	<label class="control-label" for="shelve-status">状态</label>
		        <div class="control-div">
		        	<select id="shelve-status" class="form-control">
                        <option value="">全部</option>
                        <option value="0">已删除</option>
                        <option value="1">未上架</option>
                        <option value="2">已上架</option>
                        <option value="3">数据不合法</option>
                        <option value="4">已删除存储</option>
                    </select>
		        </div>
	    	</div>
	    	<div class="form-group col-37">
	        	<label class="control-label col-md-2 col-sm-2 col-xs-12" for="content-source">来源频道</label>
		        <div class="control-div">
		        	<input type="text" class="form-control" id="sourceChannel"/>
		        </div>
	    	</div>
	    	<div class="form-group col-37" style="margin-left: 35px;">
	        	<label class="control-label col-md-2 col-sm-2 col-xs-12" for="content-source">来源栏目</label>
		        <div class="control-div">
		        	<input type="text" class="form-control" id="sourceColumn"/>
		        </div>
	    	</div>
		    <button type="button" class="btn btn-primary" id="search"><i class="fa fa-search"></i> 查找</button>
    	</div>
    </div>
</form>
    <div class="clearfix"></div>

    <div class="row">
		 <div class="col-md-12 col-sm-12 col-xs-12">
                <div class="x_panel">
                  <div class="x_content">
                  	<div class="operate" id="operate">
                  		<a class="btn btn-primary" id="shelve" data-status="2"  style="display:none;"><i class="fa fa-cloud-upload"></i> 上架</a>
                  		<a class="btn btn-warning" id="unShelve" data-status="1" style="display:none;"><i class="fa fa-cloud-download"></i> 下架</a>
                  		<a class="btn btn-success" id="addClassify" style="display:none;"><i class="fa fa-check-square"></i> 加入分类</a>
                  		<a class="btn btn-primary" id="exportExcel" data-status="1" style="display:none;"><i class="fa fa-cloud-download"></i> 导出Excel</a>
                  		<a class="btn btn-danger" style="float: right;margin-right:0;display:none;" id="delete" ><i class="fa fa-remove"></i> 删除</a>
                    </div>
                    <table id="table" class="table table-hover table-striped table-bordered dt-responsive nowrap" cellspacing="0" width="100%">
				        <thead>
				            <tr>
				            	<th width="2%"><input type="checkbox" id="chk_all" name="chk_all"></th>
				            	<th>标题/内容信息</th>
				                <th>播放日期</th>
				                <th>所属分类</th>
				                <th>入站时间</th>
				                <th>CDN状态</th>
				                <th>状态</th>
				                <th>查看</th>
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
        <h4 class="modal-title" id="myModalLabel">加入分类</h4>
      </div>
      <div class="modal-body">
      	<input type="hidden" id="ids"></input>
      	<ul id="classifyTree" class="ztree" style="height: 400px;overflow: auto;"></ul>
      </div>
      <div class="modal-footer">
      	<div class="classifyInfo">至少选择一个分类！</div>
        <button type="button" class="btn btn-default" data-dismiss="modal">取消</button>
        <button type="button" class="btn btn-primary" id="add_submit" data-dismiss="modal">确定</button>
      </div>
    </div>
  </div>
</div>

<!-- /page content -->
<script src="${ (project.staticDomain)! }/libs/zTree/jquery.ztree.core-3.5.js"></script>
<script src="${ (project.staticDomain)! }/libs/zTree/jquery.ztree.excheck-3.5.js"></script>	
<script src="${ (project.staticDomain)! }/js/content/index.js"></script>
<script>
	  	var contentEditFlag = false;
	  	var userType = 0;
       //初始化将测试集包含的用例存在数组里面
       <#if user??>
       		<#if  user.type??>
       			userType = "${user.type}";
       		</#if>
       	  if( userType == "1"){
      	   		$("#shelve").css("display","inline-block");
      	   		$("#unShelve").css("display","inline-block");
      	   		$("#delete").css("display","inline-block");
      	   		$("#addClassify").css("display","inline-block" );
      	   		$("#exportExcel").css("display","inline-block" );
      	   		contentEditFlag = true;
      	   }else{
       		  <#if  user.operations??>
	            <#list  user.operations as item>
	                 var operationCode = "${item.code}";
	                if(operationCode == "contents.shelve" ){
	                 	$("#shelve").css("display","inline-block");
	                }else if(operationCode == "contents.unShelve" ){
	                	$("#unShelve").css("display","inline-block");
	                }else if(operationCode == "contents.delete" ){
	                	$("#delete").css("display","inline-block");
	                }else if(operationCode == "contents.addClassify" ){
	                	$("#addClassify").css("display","inline-block" );
	                }else if(operationCode == "contents.edit" ){
	                	contentEditFlag = true;
	                }else if(operationCode == "contents.exportExcel" ){
	                	$("#exportExcel").css("display","inline-block" );
	                }
	            </#list>
     	 	  </#if>
     	   }
       </#if>
</script>