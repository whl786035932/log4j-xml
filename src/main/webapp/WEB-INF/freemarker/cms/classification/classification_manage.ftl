<link rel="stylesheet" href="${(project.staticDomain)!}/libs/zTree/zTreeStyle/zTreeStyle.css" type="text/css">
<link href="${ (project.staticDomain)! }/libs/loading/css/loading.css" rel="stylesheet">
<link rel="stylesheet" type="text/css" href="${ (project.staticDomain)! }/css/classification/classification_manage.css">

 <!-- page content -->
<div class="right_col" role="main">
  <div class="">
    <div class="page-title">
      <div class="title_left">
        <h3>分类管理 </h3>
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
                  	
                   <div>
                      <div class="col-sm-2 mail_list_column" style="padding: 10px 17px;">
		                  <div class="x_title">
		                    <h2>分类树<small></small></h2>
		                    <div class="clearfix"></div>
		                  </div>
                        <!--<button id="compose" class="btn btn-sm btn-primary btn-block" type="button"><i class="fa fa-tree"></i> 分类树</button>-->
                        <div class="zTreeDemoBackground left">
							<ul id="columnTree" class="ztree"  style="height:550px;overflow:auto"></ul>
							
						</div>
                      </div>
                      <!-- /MAIL LIST -->

                      <!-- CONTENT MAIL -->
                      <div class="col-sm-10 mail_view" id="basic">
					  		<div class="x_panel">
                  <div class="x_title">
                    <h2>基本信息 <small></small></h2>
                    <div class="clearfix"></div>
                  </div>
                  <div class="x_content">
                    <br />
                    <form id="demo_form" data-parsley-validate class="form-horizontal form-label-left">

                      <div class="form-group">
                        <label class="control-label col-md-3 col-sm-3 col-xs-12" for="first-name"> <span class="required" style="color:red">*</span>分类名
                        </label>
                        <div class="col-md-6 col-sm-6 col-xs-12">
                          <input type="text" id="name" name="name" required="required" attr="" class="form-control col-md-7 col-xs-12">
                          <input type="hidden" id="id" name="id">
                          <span id="name-required" class="requireds check_name" style="color:red"></span>
                        </div>
                      </div>
                      <div class="form-group">
	                        <label class="control-label col-md-3 col-sm-3 col-xs-12" for="first-name"><span class="required"></span>别名 
	                        </label>
	                        <div class="col-md-6 col-sm-6 col-xs-12">
	                          <input type="text" id="alias" name="alias" required="required" attr="" class="form-control col-md-7 col-xs-12">
	                          <span id="alias_required" class="requireds check_alias" style="color:red"></span>
	                        </div>
                    	</div>
                      <div class="form-group">
                        <label class="control-label col-md-3 col-sm-3 col-xs-12">状态</label>
                        <div class="col-md-6 col-sm-6 col-xs-12" style="padding-top: 5px;">
                          <div id="gender" class="btn-group" data-toggle="buttons">
                          	<input type="radio" name="status" id="statusNy" value="1"> 启用 &nbsp;
                          	<input type="radio" name="status" id="statusNn" value="0"> 禁用
                          </div>
                        </div>
                      </div>
                      <div class="form-group">
                        <label class="control-label col-md-3 col-sm-3 col-xs-12">类型</label>
                        <div class="col-md-6 col-sm-6 col-xs-12" style="padding-top: 5px;">
                          <div id="gender" class="btn-group" data-toggle="buttons">
                          	<input type="radio" name="type" id="typeNn" value="0"> 普通 &nbsp;
                          	<input type="radio" name="type" id="typeNy" value="1"> 专题 &nbsp;
                          	<input type="radio" name="type" id="typeNyd" value="2"> 多级 &nbsp;
                          </div>
                        </div>
                      </div>
                      <div class="form-group">
	                        <label class="control-label col-md-3 col-sm-3 col-xs-12">编辑</label>
	                        <div class="col-md-6 col-sm-6 col-xs-12" style="padding-top: 5px;">
	                          <div id="gender" class="btn-group" data-toggle="buttons">
	                          	<input type="radio" name="edit" id="edit_y" value="1"> 可编辑 &nbsp;
	                          	<input type="radio" name="edit" id="edit_n" value="0"> 不可编辑
	                          </div>
	                        </div>
                    	</div>
                    	<div class="form-group">
	                        <label class="control-label col-md-3 col-sm-3 col-xs-12">删除</label>
	                        <div class="col-md-6 col-sm-6 col-xs-12" style="padding-top: 5px;">
	                          <div id="gender" class="btn-group" data-toggle="buttons">
	                          	<input type="radio" name="del" id="del_y" value="1"> 可删除 &nbsp;
	                          	<input type="radio" name="del" id="del_n" value="0"> 不可删除
	                          </div>
	                        </div>
                    	</div>
                    	<div class="form-group">
	                        <label class="control-label col-md-3 col-sm-3 col-xs-12">推荐</label>
	                        <div class="col-md-6 col-sm-6 col-xs-12" style="padding-top: 5px;">
	                          <div id="gender" class="btn-group" data-toggle="buttons">
	                          	<input type="radio" name="recommend" id="recommend_n" value="0"> 不推荐 &nbsp;
	                          	<input type="radio" name="recommend" id="recommend_y" value="1"> 推荐
	                          </div>
	                        </div>
                    	</div>
                       <div class="form-group">
                       	<label class="control-label col-md-3 col-sm-3 col-xs-12">图标</label>
                       	<div class="col-md-6 col-sm-6 col-xs-12" id="icon">
                       	<div id="gender" class="image view view-first">
                       		<img style="width: 100%; display: block;" src="" id="change_icon" alt="image" name="image"/>
	                      	<input type="hidden" name="icon"/>
                       	</div>
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
<div class="modal fade" id="addClassification" tabindex="-1" role="dialog"  aria-hidden="true">
	<div class="modal-dialog">
		<div class="modal-content">
			<div class="modal-header">
	        	<h4 class="modal-title" id="myModalLabel">添加分类</h4>
			</div>
        	<div class="modal-body clearfix">
		    	<div class="x_content">
		    	<br />
                	<form id="demo-form2" data-parsley-validate class="form-horizontal form-label-left">

                    	<div class="form-group">
	                        <label class="control-label col-md-3 col-sm-3 col-xs-12" for="first-name"><span class="required">*</span>分类名 
	                        </label>
	                        <div class="col-md-6 col-sm-6 col-xs-12">
	                          <input type="text" id="name-add" name="name_add" required="required" attr="" class="form-control col-md-7 col-xs-12">
	                          <input type="hidden" id="id_add" name="id_add">
	                          <input type="hidden" id="level_add" name="level_add">
	                          <input type="hidden" id="rgt_add" name="rgt_add">
	                          <span id="name_add_required" class="requireds_add check_name_add" style="color:red"></span>
	                        </div>
                    	</div>
                    	<div class="form-group">
	                        <label class="control-label col-md-3 col-sm-3 col-xs-12" for="first-name"><span class="required"></span>别名 
	                        </label>
	                        <div class="col-md-6 col-sm-6 col-xs-12">
	                          <input type="text" id="alias-add" name="alias_add" required="required" attr="" class="form-control col-md-7 col-xs-12">
	                          <span id="alias_add_required" class="requireds_add check_alias_add" style="color:red"></span>
	                        </div>
                    	</div>
                     	<div class="form-group">
	                        <label class="control-label col-md-3 col-sm-3 col-xs-12">状态</label>
	                        <div class="col-md-6 col-sm-6 col-xs-12" style="padding-top: 5px;">
	                          <div id="gender" class="btn-group" data-toggle="buttons">
	                          	<input type="radio" name="status_add" checked value="1"> 启用 &nbsp;
	                          	<input type="radio" name="status_add" value="0"> 禁用
	                          </div>
	                        </div>
                    	</div>
                    	<div class="form-group">
	                        <label class="control-label col-md-3 col-sm-3 col-xs-12">类型</label>
	                        <div class="col-md-6 col-sm-6 col-xs-12" style="padding-top: 5px;">
	                          <div id="gender" class="btn-group" data-toggle="buttons">
	                          	<input type="radio" name="type_add" checked value="0"> 普通 &nbsp;
	                          	<input type="radio" name="type_add" value="1"> 专题 &nbsp;
	                          	<input type="radio" name="type_add" value="2"> 多级
	                          </div>
	                        </div>
                    	</div>
                    	<div class="form-group">
	                        <label class="control-label col-md-3 col-sm-3 col-xs-12">编辑</label>
	                        <div class="col-md-6 col-sm-6 col-xs-12" style="padding-top: 5px;">
	                          <div id="gender" class="btn-group" data-toggle="buttons">
	                          	<input type="radio" name="edit_add" checked value="1"> 可编辑 &nbsp;
	                          	<input type="radio" name="edit_add" value="0"> 不可编辑
	                          </div>
	                        </div>
                    	</div>
                    	<div class="form-group">
	                        <label class="control-label col-md-3 col-sm-3 col-xs-12">删除</label>
	                        <div class="col-md-6 col-sm-6 col-xs-12" style="padding-top: 5px;">
	                          <div id="gender" class="btn-group" data-toggle="buttons">
	                          	<input type="radio" name="del_add" checked value="1"> 可删除 &nbsp;
	                          	<input type="radio" name="del_add" value="0"> 不可删除
	                          </div>
	                        </div>
                    	</div>
                    	<div class="form-group">
	                        <label class="control-label col-md-3 col-sm-3 col-xs-12">推荐</label>
	                        <div class="col-md-6 col-sm-6 col-xs-12" style="padding-top: 5px;">
	                          <div  class="btn-group" data-toggle="buttons">
	                          	<input type="radio" name="recommend_add" checked value="0"> 不推荐 &nbsp;
	                          	<input type="radio" name="recommend_add" value="1"> 推荐
	                          </div>
	                        </div>
                    	</div>
                    </form>
                 </div><!--x_content-->
			</div><!--modal-body-->
			<div class="modal-footer">
	        	<button class="btn btn-default" id="add_cancle" type="reset">取消</button>
				<button type="button" id="add_save" class="btn btn-primary check_name_button_add">确定</button>
	    	</div>
		</div><!--modal-content-->
	</div><!--modal-dialog-->
</div><!--contentsAddColumnModal-->
<!---添加节点-->
<!-- 修改图标-->
<div class="modal fade posterSelect" tabindex="-1" role="dialog" aria-hidden="true" id="addPoster">
    <div class="modal-dialog modal-lg">
      <div class="modal-content">
        <div class="modal-header">
          <button type="button" id="close_pb" class="close closePoster"><span aria-hidden="true">×</span>
          </button>
          <h4 class="modal-title" id="myModalLabel">修改图标</h4>
        </div>
        <div class="modal-body">
          	<form id="add_form" class="form-horizontal form-label-left" data-parsley-validate="" enctype="multipart/form-data" method="post">
              <div class="form-group">
                <label for="middle-name" class="control-label col-md-3 col-sm-3 col-xs-12"><span class="required">*</span>图标</label>
                <div class="col-md-6 col-sm-6 col-xs-12">
                    <input name="poster_file" type="file" required class="form-control"/>
                </div>
                <span id="poster_required" class="requireds" style="color:red"></span>
              </div>
            </form>
        </div>
        <div class="modal-footer">
          <button type="button" class="btn btn-default" id="cancle_poster">取消</button>
          <button type="button" class="btn btn-primary" id="add_sure">确定</button>
        </div>

      </div>
    </div>
</div>
<!-- 修改图标-->
        
<script type="text/javascript">
	var base = $("#base").attr("href");
	var treeJson = '${(treeJson)!}';
	var nodes = null;
	if(treeJson!=null&&treeJson!=''&&treeJson!=""){
		nodes = JSON.parse(treeJson);
	}
</script>
<script src="${ (project.staticDomain)! }/libs/loading/js/loading.js"></script>
<script src="${ (project.staticDomain)! }/js/classification/classification_manage.js"></script>
<script src="${ (project.staticDomain)! }/libs/zTree/jquery.ztree.core-3.5.js"></script>
<script src="${ (project.staticDomain)! }/libs/zTree/jquery.ztree.excheck-3.5.js"></script>
<script src="${ (project.staticDomain)! }/libs/zTree/jquery.ztree.exedit-3.5.js"></script>
<script src="${ (project.staticDomain)! }/libs/zTree/jquery.ztree.exhide-3.5.js"></script>
<script src="${ (project.staticDomain)! }/libs/jquery-form/jquery-form-20131225.min.js"></script>
