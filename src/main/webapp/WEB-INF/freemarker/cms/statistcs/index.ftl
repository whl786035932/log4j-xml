<link rel="stylesheet" href="${(project.staticDomain)!}/libs/zTree/zTreeStyle/zTreeStyle.css" type="text/css">
<link rel="stylesheet" href="${(project.staticDomain)!}/css/statistcs/index.css">
 <!-- page content -->
<div class="right_col" role="main">
	<div class="page-title">
    	<div class="title_left">
        	<h3>入库统计 </h3>
    	</div>
	</div><!--page-title-->

	<div class="row clearfix">
		<div class="col-md-12 col-sm-12 col-xs-12">
         	<div class="x_panel">
            	<div class="x_content">
            		<div class="searchArea clearfix">
                		<div class="form-group chooseDay col-md-3 col-sm-3 col-xs-12">
				        	<label class="control-label">统计日期：</label>
					        <input type="text" class="form-control" id="chooseDay" placeholder="" onClick="WdatePicker({dateFmt:'yyyy-MM-dd',maxDate:'%y-%M-%d',onpicked:function(dp){change(this,dp);}})">
					    </div>
					    <div class="form-group col-md-1 col-sm-1 col-xs-12" style="width:calc(25% - 107px)">
					    </div>
					    <div class="form-group col-md-3 col-sm-3 col-xs-12">
				        	<label class="control-label">开始日期：</label>
					        <input type="text" class="form-control" id="startDay" placeholder="" onClick="WdatePicker({dateFmt:'yyyy-MM-dd',maxDate:'%y-%M-%d'})">
					    </div>
					    <div class="form-group col-md-3 col-sm-3 col-xs-12">
				        	<label class="control-label">结束日期：</label>
					        <input type="text" class="form-control" id="endDay" placeholder="" onClick="WdatePicker({dateFmt:'yyyy-MM-dd',maxDate:'%y-%M-%d'})">
					    </div>
					    	<a class="btn btn-primary" id="exportExcel" data-status="1"><i class="fa fa-cloud-download"></i> 导出Excel</a>
					</div>
                    <div id="statisticTree" class="clearfix">
                    
                    </div>
				</div><!--x_content-->
			</div><!--x_panel-->
		</div><!--col-md-12-->
	</div><!--row-->
</div><!-- /page content -->

<script src="${ (project.staticDomain)! }/libs/ECharts/echarts.min.js"></script>
<script src="${ (project.staticDomain)! }/js/statistcs/index.js"></script>
<script>
	
</script>
