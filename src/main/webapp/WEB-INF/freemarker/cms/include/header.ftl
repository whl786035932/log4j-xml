<!-- begin -->
<div class="col-md-3 left_col">
 	<div class="left_col scroll-view">
        <div class="navbar nav_title" style="border: 0;">
          <a href="${base}/index" class="site_title"><i class="fa fa-home"></i> <span>CMS</span></a>
        </div>
        <div class="clearfix"></div>
        <!-- menu profile quick info -->
        <!-- /menu profile quick info -->
        <br />
        <!-- sidebar menu -->
        <div id="sidebar-menu" class="main_menu_side hidden-print main_menu">
          <div class="menu_section">
            <ul class="nav side-menu">
            <!--  
             <li class="<#if requestPath! == 'contents'>current-page</#if>"><a href="${base}/contents"><i class="fa fa-reorder"></i> 内容管理 </a></li>
             <li><a><i class="fa fa-sitemap"></i> 分类管理 <span class="fa fa-chevron-down"></span></a>
                <ul class="nav child_menu">
                    <li><a href="${base}/classification/classificationManage">分类管理</a></li>
                    <li><a href="${base}/classification/arrangement">分类编排</a></li>
                </ul>
              </li>
              <li><a href="${base}/posters"><i class="fa fa-image"></i> 海报管理 </a></li>
              <li><a href="${base}/tasks/list"><i class="fa fa-database"></i> 入站管理 </a></li>
                
              <li><a><i class="fa fa-gears"></i> 系统管理 <span class="fa fa-chevron-down"></span></a>
                <ul class="nav child_menu">
                    <li><a href="${base}/list">用户管理</a></li>
                    <li><a href="${base}/role/list">角色编排</a></li>
                    <li><a href="${base}/permission/list">权限管理</a></li>
                    <li><a href="${base}/menus">菜单管理</a></li>
                    <li><a href="${base}/operation/index">操作管理</a></li>
                </ul>
              </li>  
            -->  
              <#list user.menus as menu>
              		<#list menu.children as child>
              			<#if child.children?? && (child.children?size <= 0)> 
					 		<li class=""><a href="${base}/${child.url}"><i class="${(child.menuClass)! }"></i> ${child.name } </a></li>
					 	<#else>
					 		 <li><a><i class="${(child.menuClass)!}"></i> ${child.name } <span class="fa fa-chevron-down"></span></a>
				                <ul class="nav child_menu">
				                	<#list child.children as child1>
				                    	<li><a href="${base}/${child1.url}">${child1.name}</a></li>
				                    </#list>
				                </ul>
				              </li>  
					 	</#if>
					</#list>
              </#list>
              
            </ul>
          </div>

        </div>
        <!-- /sidebar menu -->

        <!-- /menu footer buttons -->
        <div class="sidebar-footer hidden-small">
          <a data-toggle="tooltip" data-placement="top" title="主版本">
          	${(version)!?substring(0,2)}
          </a>
          <a data-toggle="tooltip" data-placement="top" title="中版本">
            ${(version)!?substring(3,4)}
          </a>
          <a data-toggle="tooltip" data-placement="top" title="小版本">
             ${(version)!?substring(5,6)}
          </a>
          <a data-toggle="tooltip" data-placement="top" title="退出" href="login.html">
            <span class="glyphicon glyphicon-off" aria-hidden="true"></span>
          </a>
        </div>
        <!-- /menu footer buttons -->
      </div>
  </div>
<div>
<!-- menu end --> 

<!-- top navigation -->
<div class="top_nav clearfix">
  <div class="nav_menu" style="margin-bottom:0">
    <nav>
      <div class="nav toggle">
        <a id="menu_toggle"><i class="fa fa-bars"></i></a>
      </div>
      <ul class="nav navbar-nav navbar-right">
        <li class="">
          <a href="javascript:;" class="user-profile dropdown-toggle" data-toggle="dropdown" aria-expanded="false">
           欢迎： ${(user.username)! } &nbsp;<span class=" fa fa-angle-down"></span>
          </a>
          <ul class="dropdown-menu dropdown-usermenu pull-right">
          	<li><a  id="updateCrrentPassword"><i class="fa fa fa-wrench pull-right"></i>修改密码</a></li>
            <li><a href="${base}/logout" id="logOut"><i class="fa fa-sign-out pull-right"></i>退出</a></li>
          </ul>
        </li>
      </ul>
    </nav>
  </div>
</div>
<!-- /top navigation -->

<!-- update password end-->
<div class="modal fade" tabindex="-1" role="dialog" aria-hidden="true" id="updateCrrentPasswordModel">
    <div class="modal-dialog modal-lg">
      <div class="modal-content">

        <div class="modal-header">
          <button type="button" class="close" data-dismiss="modal"><span aria-hidden="true">×</span>
          </button>
          <h4 class="modal-title">修改密码</h4>
        </div>
        <div class="modal-body">
          	<form id="update_current_password_form" class="form-horizontal form-label-left" data-parsley-validate="" enctype="multipart/form-data" method="post" >
              <div class="form-group">
                <label for="middle-name" class="control-label col-md-3 col-sm-3 col-xs-12"><span class="required">*</span>密码</label>
                <div class="col-md-6 col-sm-6 col-xs-12">
                  	<input name="currentId" id="update_current_password_id" type="hidden" required class="form-control" value="${(user.id)!"" }"/>
                    <input name="currentPassword" id="update_current_password" type="password" class="form-control"/>
                    <span id="update_current_password_required" class="requireds" style="color:red"></span>
                    <br>
                </div>
              </div>
              
              <div class="form-group">
                <label for="middle-name" class="control-label col-md-3 col-sm-3 col-xs-12"><span class="required">*</span>确认密码</label>
                <div class="col-md-6 col-sm-6 col-xs-12">
                    <input  id="update_current_repassword" type="password" class="form-control"/>
                    <span id="update_current_repassword_required" class="requireds" style="color:red"></span>
                    <br>
                </div>
              </div>
              
            </form>
        </div>
        <div class="modal-footer">
          <button type="button" class="btn btn-default" data-dismiss="modal">取消</button>
          <button type="button" class="btn btn-primary" id="update_current_password_sure">确定</button>
        </div>

      </div>
    </div>
</div>  <!--update password end-->
<script src="${ (project.staticDomain)! }/js/include/header.js"></script>