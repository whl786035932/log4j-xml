<link href="${ (project.staticDomain)! }/css/index/index.css" rel="stylesheet">
<!-- page content -->
<div class="right_col" role="main">
  <div class="">
	     <div class="row top_tiles">
              <div class="animated flipInY col-lg-3 col-md-3 col-sm-6 col-xs-12">
                <div class="tile-stats">
                  <div class="icon"><i class="fa fa-pie-chart"></i></div>
                  <div class="count">${(taskStatistics.totalNumber)! "0"} <span>条</span></div>
                  <h3>今日入库总数</h3>
                </div>
              </div>
              <div class="animated flipInY col-lg-3 col-md-3 col-sm-6 col-xs-12">
                <div class="tile-stats">
                  <div class="icon"><i class="fa fa-check-square-o"></i></div>
                  <div class="count">${(taskStatistics.successNumber)! "0"} <span>条</span></div>
                  <h3>成功</h3>
                </div>
              </div>
              <div class="animated flipInY col-lg-3 col-md-3 col-sm-6 col-xs-12">
                <div class="tile-stats">
                  <div class="icon"><i class="fa fa-spinner"></i></div>
                  <div class="count">${(taskStatistics.waitingNumber)! "0"} <span>条</span></div>
                  <h3>进行中</h3>
                </div>
              </div>
              <div class="animated flipInY col-lg-3 col-md-3 col-sm-6 col-xs-12">
                <div class="tile-stats">
                  <div class="icon"><i class="fa fa-warning"></i></div>
                  <div class="count">${(taskStatistics.failureNumber)! "0"} <span>条</span></div>
                  <h3>失败</h3>
                </div>
              </div>
         </div>
	
	    <div class="row">
		 <div class="col-md-12 col-sm-12 col-xs-12">
                <div class="x_panel">
                  <div class="x_content">
                  
					   <div class="search-area clearfix">
				    	<div class="form-horizontal clearfix">
					    	<div class="form-group">
					        	<label class="control-label col-md-2 col-sm-2 col-xs-12">统计时间：</label>
						        <div class="control-div date-div">
						        	<input type="text" class="form-control" id="insertedBegin" placeholder="" onClick="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss'})">
		        					<input type="text" class="form-control" id="insertedEnd" placeholder="" onClick="WdatePicker({dateFmt:'yyyy-MM-dd  HH:mm:ss'})">
						        </div>
					    	</div>
					    	<div class="form-group" style="margin-left: 35px;">
					        	<button type="button" class="btn btn-default" id="search" style="color: #8d8f92;"><i class="fa fa-search"></i> 查找</button>
					    	</div>
				    	</div>
				    </div>

		            <div class="search-br"></div>
                  
                    <table id="table" class="table table-hover table-striped table-bordered dt-responsive nowrap" cellspacing="0" width="100%">
				        <thead>
				            <tr>
				            	<th>统计时间</th>
				            	<th>总数</th>
				            	<th>成功</th>
				                <th>进行中</th>
				                <th>失败</th>
				                <th width="10%">重复&nbsp;(不计入总数)</th>
				                <th width="10%">创建时间</th>
				                <th width="10%">修改时间</th>
				            </tr>
				        </thead>
				    </table>
                  </div>
                </div>
              </div>
      </div>
		

	<#if (user.type)! == 1 >
	    <div class="row">
	      <div class="col-md-12">
	        <div class="x_panel">
	          <div class="x_content">
	        		<!-- 提示信息 -->
					<div class="alert alert-warning" role="alert">
				      <button type="button" class="close" data-dismiss="alert" aria-label="Close"><span aria-hidden="true">×</span></button>
				      <strong>提示!</strong> 对用户权限进行操作时，被操作的用户需要退出系统，重新登录方可生效！！！
				    </div>
		      		<section id="web-application">
						<h3>您好：${(user.username)!"游客" }</h3>
						<div class="text-center">
							<h1>欢迎登录《${ (project.title)! }》！</h1>
							<#if user.type == 1>
								<div>
									<h2>系统首次部署，请先创建系统菜单</h2>
									<a href="/menus" class="btn btn-warning">创建系统菜单</a>
								</div>
							</#if>
						</div>
	                 </section>
	                 <h5 class="text-right">上次登录时间：${(user.last_login_time)! }</h5>
	          </div>
	        </div>
	      </div>
	    </div>
    </#if>

    
  </div>
</div>
<!-- /page content -->

<!-- 重复model -->
<div class="modal fade" tabindex="-1" role="dialog" aria-labelledby="myModalLabel"  id="taskStatisticsModel">
  <div class="modal-dialog modal-lg" role="document">
    <div class="modal-content">
      <div class="modal-header">
        <button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span></button>
        <h4 class="modal-title" id="myModalLabel">重复数</h4>
      </div>
      <div class="modal-body" id="my-model">
        <ol id="repeatDataOl">
        	
        </ol>
      </div>
      <div class="modal-footer">
        <button type="button" class="btn btn-default" data-dismiss="modal">关闭</button>
      </div>
    </div>
  </div>
</div>

<script src="${ (project.staticDomain)! }/js/index/index.js" type="text/javascript"></script>