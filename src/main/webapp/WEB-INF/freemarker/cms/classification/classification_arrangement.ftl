<link rel="stylesheet" href="${(project.staticDomain)!}/libs/zTree/zTreeStyle/zTreeStyle.css" type="text/css">
<link rel="stylesheet" type="text/css" href="${ (project.staticDomain)! }/css/classification/classification_arrangement.css">
 <!-- page content -->
<div class="right_col" role="main">
	<div class="page-title">
		<div class="title_left">
			<h3>分类编排 </h3>
		</div>
		<div class="title_right">
			<div class="col-md-5 col-sm-5 col-xs-12 form-group pull-right top_search">
				<div class="input-group">
				  
				</div>
			</div>
		</div>
	</div>
	
	<div class="row">
		<div class="col-md-12">
			<div class="x_panel">
				<div class="x_content">
				<div class="row">
					<div class="col-sm-2 mail_list_column">
						<div class="zTreeDemoBackground left">
							<ul id="columnTree" class="ztree"  style="height:550px;overflow:auto"></ul>
						</div>
					</div>
					
					<div class="col-sm-10 mail_view" id="basic">
							<div class="search-area clearfix" style="padding:0px">
								<div class="form-horizontal clearfix">
									<div class="form-group col-md-5 col-sm-5 col-xs-12" style="padding-left:23px">
										<label class="control-label" for="title">题名</label>
										<div class="col-md-9 col-sm-9 col-xs-12" style="width:calc(100% - 100px)">
											<input type="text" id="title" class="form-control">
											<input type="hidden" id="classi_id" class="form-control">
										</div>
									</div>
									<div class="form-group col-md-5 col-sm-5 col-xs-12">
										<label class="control-label" for="shelve-status">上架状态</label>
										<div class="col-md-9 col-sm-9 col-xs-12" style="width:calc(100% - 100px)">
											<select id="shelve-status" class="form-control">
												<option value="-1">全部</option>
												<option value="1">未上架</option>
												<option value="2">已上架</option>
											</select>
										</div>
									</div>
									<button type="button" class="btn btn-primary" id="search"><i class="fa fa-search"></i> 查找</button>
								</div>
							</div>
						
							<div class="row">
								<div class="col-md-12 col-sm-12 col-xs-12">
									<div class="x_panel">
										<div class="x_content">
											<div class="operate" id="operate">
												<a class="btn btn-success" id="remove_classify"><i class="fa fa-times-circle"></i> 移出分类</a>
												<a class="btn btn-success" id="addClassify"><i class="fa fa-check-square"></i> 加入分类</a>
												<a class="btn btn-primary"  data-status="2" id="upper_shelve"><i class="fa fa-cloud-upload"></i> 上架</a>
												<a class="btn btn-warning"  data-status="1" id="un_shelve"><i class="fa fa-cloud-download"></i> 下架</a>
												<a class="btn btn-danger foright"  id="delete" style="display:none;"><i class="fa fa-remove"></i> 删除</a>
												<a class="btn btn-primary foright" id="recommendManage"><i class="fa fa-upload"></i> 置顶管理</a>
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
														<th>上架状态</th>
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
				</div>
			</div>
		</div>
	</div>
</div>

<!-- 加入分类Modal -->
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
    
<!--置顶管理弹框-->
<div class="modal fade bs-example-modal-lg" id="recommend" tabindex="-1" role="dialog" aria-hidden="true">
	<div class="modal-dialog modal-lg">
		<div class="modal-content">
			<div class="modal-header">
				<button type="button" class="close" data-dismiss="modal"><span aria-hidden="true">×</span></button>
				<h4 class="modal-title" id="myModalLabel">置顶管理</h4>
			</div>
			<div class="modal-body">
				<div class="" role="main">
					<div class="">
						<div class="clearfix"></div>
	
						<div class="row">
							<div class="col-md-12">
								<div class="x_panel">
									<div class="x_content" id="recommendPosters">
				
	            						<div class="row" id="posters">
	              							<div class="col-md-4 col-sm-4 col-xs-12" ondrop="drop(event,this)" ondragover="allowDrop(event)" draggable="true" ondragstart="drag(event, this)">
								            	<div class="thumbnail">
							                    	<div class="image view view-first">
							                        	<img style="width: 100%; display: block;" src="${(project.staticDomain!)}/images/postadd.png" class="change_poster" id="img_1" alt="image" name="image"/>
							                      		<input type="hidden" name="id"/>
							                      		<input type="hidden" name="oldId"/>
							                    	</div>
							                    	<div class="caption">
							                    		<span class="wh"></span>
							                    		<span class="size"></span>
							                     	</div>
							                     	<div class="description">
							                     		<span></span>
							                     	</div>
							                    </div><!--thumbnail-->
							            	</div>
							            	<div class="col-md-4 col-sm-4 col-xs-12" ondrop="drop(event,this)" ondragover="allowDrop(event)" draggable="true" ondragstart="drag(event, this)">
							            	<input type="hidden" name="nowId" id="now_id"/>
								            	<div class="thumbnail">
							                    	<div class="image view view-first">
							                        	<img style="width: 100%; display: block;" src="${(project.staticDomain!)}/images/postadd.png" class="change_poster" id="img_2" alt="image" name="image"/>
							                      		<input type="hidden" name="id"/>
							                      		<input type="hidden" name="oldId"/>
							                    	</div>
							                    	<div class="caption">
							                    		<span class="wh"></span>
							                    		<span class="size"></span>
							                     	</div>
							                     	<div class="description">
							                     		<span></span>
							                     	</div>
							                    </div><!--thumbnail-->
							            	</div>
							            	<div class="col-md-4 col-sm-4 col-xs-12" ondrop="drop(event,this)" ondragover="allowDrop(event)" draggable="true" ondragstart="drag(event, this)">
								            	<div class="thumbnail">
							                    	<div class="image view view-first">
							                        	<img style="width: 100%; display: block;" src="${(project.staticDomain!)}/images/postadd.png" class="change_poster" id="img_3" alt="image" name="image"/>
							                      		<input type="hidden" name="id"/>
							                      		<input type="hidden" name="oldId"/>
							                    	</div>
							                    	<div class="caption">
							                    		<span class="wh"></span>
							                    		<span class="size"></span>
							                     	</div>
							                     	<div class="description">
							                     		<span></span>
							                     	</div>
							                    </div><!--thumbnail-->
							            	</div>
	            						</div><!--posters-->
	            						<div class="row">
					    					<div class="col-md-4 col-sm-4 col-xs-12 position" id="">置顶1</div>
					    					<div class="col-md-4 col-sm-4 col-xs-12 position">置顶2</div>
					    					<div class="col-md-4 col-sm-4 col-xs-12 position">置顶3</div>
					    				</div>
									</div><!--x_content-->
								</div><!--x_panel-->
							</div><!--col-md-12-->
	    				</div><!--row-->
	    				
					</div>
				</div>
			</div><!--modal-body-->
			<div class="modal-footer">
		        <button type="button" class="btn btn-default" data-dismiss="modal">取消</button>
		        <button type="button" class="btn btn-primary" id="recommend_submit" data-dismiss="modal">确定</button>
	      	</div>
		</div><!--modal-content-->
    </div><!--modal-dialog-->
</div><!--modal-->

<!--内容置顶-->
<div class="modal fade bs-example-modal-lg" id="recommendItem" tabindex="-1" role="dialog" aria-hidden="true">
	<div class="modal-dialog modal-lg">
		<div class="modal-content">
			<div class="modal-header">
				<button type="button" class="close" data-dismiss="modal"><span aria-hidden="true">×</span></button>
				<h4 class="modal-title" id="myModalLabel">内容置顶</h4>
			</div>
			<div class="modal-body">
				<div class="" role="main">
					<div class="">
						<div class="clearfix"></div>
	
						<div class="row">
							<div class="col-md-12">
								<div class="x_panel">
									<div class="x_content">
				
	            						<div class="row" id="posters">
	              							<div class="col-md-4 col-sm-4 col-xs-12">
								            	<div class="thumbnail">
							                    	<div class="image view view-first">
							                        	<img style="width: 100%; display: block;" src="${(project.staticDomain!)}/images/postadd.png" class="add_change_poster" id="add_img_1" alt="image" name="image"/>
							                      		<input type="hidden" name="id" id="id_1"/>
							                      		<input type="hidden" name="oldId" id="oldId_1"/>
							                      		<input type="hidden" name="recommend" value="3" id="recommend_1"/>
							                      		<div class="mask" style="display:block">
								                        	<div class="tools tools-bottom">
								                        		<a href="javascript:void(0);" class="change_recommend">替换</a>
								                        	</div>
								                        </div>
							                    	</div>
							                    	<div class="caption">
							                    		<span class="wh"></span>
							                    		<span class="size"></span>
							                     	</div>
							                     	<div class="description">
							                     		<span></span>
							                     	</div>
							                    </div>
							            	</div>
							            	<div class="col-md-4 col-sm-4 col-xs-12">
							            	<input type="hidden" name="nowId" id="now_id"/>
								            	<div class="thumbnail">
							                    	<div class="image view view-first">
							                        	<img style="width: 100%; display: block;" src="${(project.staticDomain!)}/images/postadd.png" class="add_change_poster" id="add_img_2" alt="image" name="image"/>
							                      		<input type="hidden" name="id" id="id_2"/>
							                      		<input type="hidden" name="oldId" id="oldId_2"/>
							                      		<input type="hidden" name="recommend" value="2" id="recommend_2"/>
							                      		<div class="mask" style="display:block">
								                        	<div class="tools tools-bottom">
								                        		<a href="javascript:void(0);" class="change_recommend">替换</a>
								                        	</div>
								                        </div>
							                    	</div>
							                    	<div class="caption">
							                    		<span class="wh"></span>
							                    		<span class="size"></span>
							                     	</div>
							                     	<div class="description">
							                     		<span></span>
							                     	</div>
							                    </div>
							            	</div>
							            	<div class="col-md-4 col-sm-4 col-xs-12">
								            	<div class="thumbnail">
							                    	<div class="image view view-first">
							                        	<img style="width: 100%; display: block;" src="${(project.staticDomain!)}/images/postadd.png" class="add_change_poster" id="add_img_3" alt="image" name="image"/>
							                      		<input type="hidden" name="id" id="id_3"/>
							                      		<input type="hidden" name="oldId" id="oldId_3"/>
							                      		<input type="hidden" name="recommend" value="1" id="recommend_3"/>
							                      		<div class="mask" style="display:block">
								                        	<div class="tools tools-bottom">
								                        		<a href="javascript:void(0);" class="change_recommend">替换</a>
								                        	</div>
								                        </div>
							                    	</div>
							                    	<div class="caption">
							                    		<span class="wh"></span>
							                    		<span class="size"></span>
							                     	</div>
							                     	<div class="description">
							                     		<span></span>
							                     	</div>
							                    </div>
							            	</div>
	            						</div><!--posters-->
	            						<div class="row">
					    					<div class="col-md-4 col-sm-4 col-xs-12 position" id="">置顶1</div>
					    					<div class="col-md-4 col-sm-4 col-xs-12 position">置顶2</div>
					    					<div class="col-md-4 col-sm-4 col-xs-12 position">置顶3</div>
					    					<input type="hidden" id="change_id">
					    				</div>
									</div><!--x_content-->
								</div><!--x_panel-->
							</div><!--col-md-12-->
	    				</div><!--row-->
	    				
					</div>
				</div>
			</div><!--modal-body-->
			<div class="modal-footer">
		        <button type="button" class="btn btn-default" data-dismiss="modal">取消</button>
		        <button type="button" class="btn btn-primary" id="change_recommend_submit" data-dismiss="modal">确定</button>
	      	</div>
		</div><!--modal-content-->
    </div><!--modal-dialog-->
</div><!--modal-->
<script type="text/javascript">
	var base = $("#base").attr("href");
	var treeJson = '${(treeJson)!}';
	var nodes = null;
	if(treeJson!=null&&treeJson!=''&&treeJson!=""){
		nodes = JSON.parse(treeJson);
	}
	function allowDrop(ev){  
		ev.preventDefault();  
	}
	  
	var srcdiv = null;  
	function drag(ev,divdom){  
		srcdiv=divdom;  
		ev.dataTransfer.setData("text/html",divdom.innerHTML);  
	}  
	  
	function drop(ev,divdom){  
		ev.preventDefault();  
		if(srcdiv != divdom){  
			srcdiv.innerHTML = divdom.innerHTML;  
			divdom.innerHTML=ev.dataTransfer.getData("text/html");  
		}  
	}  
</script>
<script src="${ (project.staticDomain)! }/js/classification/classification_arrangement.js"></script>
<script src="${ (project.staticDomain)! }/libs/zTree/jquery.ztree.core-3.5.js"></script>
<script src="${ (project.staticDomain)! }/libs/zTree/jquery.ztree.excheck-3.5.js"></script>
<script src="${ (project.staticDomain)! }/libs/zTree/jquery.ztree.exedit-3.5.js"></script>
<script src="${ (project.staticDomain)! }/libs/zTree/jquery.ztree.exhide-3.5.js"></script>
<script>
	  	var classifyRemoveContentFlag = false;
       //初始化将测试集包含的用例存在数组里面
      <#if user??>
      	  	<#if  user.type??>
       			userType = "${user.type}";
       		</#if>
      	   if( userType == "1"){
      	   		$("#delete").css("display","inline");
      	   		classifyRemoveContentFlag = true;
      	   }else{
      	   	 <#if user.operations??>
	            <#list  user.operations as item>
	                var operationCode = "${item.code}";
	                if(operationCode == "classification.deleteContent" ){
	                	$("#delete").css("display","inline");
	                }
	            </#list>
	         </#if>
      	   }
	 </#if>
</script>
