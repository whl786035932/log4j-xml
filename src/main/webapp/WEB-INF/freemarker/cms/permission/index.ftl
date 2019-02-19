<link rel="stylesheet" href="${(project.staticDomain)!}/css/task/index.css">
 <!-- page content -->
<div class="right_col" role="main">
<div class="page-title">
      <div class="title_left">
        <h3>权限管理 </h3>
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
						<!--<a class="btn btn-primary" id="add_classify">添加</a>
						<a class="btn btn-primary"  data-status="2" id="add_permission"><i class="fa fa-cloud-upload"></i>添加</a>
                  		<a class="btn btn-danger foright"  id="delete" style="margin-right:0"><i class="fa fa-remove"></i> 删除</a>
						-->
                    </div>
                    <table id="table" class="table table-hover table-striped table-bordered dt-responsive nowrap" cellspacing="0" width="100%">
				        <thead>
				            <tr>
				            	<th><input type="checkbox" id="chk_all" name="chk_all"></th>
				            	<th>类型</th>
				            	<th>功能</th>
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
                <label for="middle-name" class="control-label col-md-3 col-sm-3 col-xs-12">类型</label>
                <div class="col-md-6 col-sm-6 col-xs-12">
                   <select id="type" class="form-control" name="type"  >
                        <option value="">选择类型</option>
                        <option value="1">菜单</option>
                        <option value="2">功能操作</option>
                        <option value="3">分类</option>
                    </select>
                    <span id="nickname_required" class="requireds" style="color:red"></span>
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



<script src="${ (project.staticDomain)! }/libs/loading/js/loading.js"></script>
<script src="${ (project.staticDomain)! }/js/permission/index.js"></script>