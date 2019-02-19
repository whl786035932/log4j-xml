<link rel="stylesheet" href="${(project.staticDomain)!}/libs/bootstrap-tokenfield/css/bootstrap-tokenfield.min.css">
<link rel="stylesheet" href="${(project.staticDomain)!}/css/content/edit.css">

 <!-- page content -->
<div class="right_col" role="main">
	<div class="page-title">
       <div class="title_left">
         <h3>内容编辑</h3>
       </div>

       <div class="title_right">
         <div class="col-md-5 col-sm-5 col-xs-12 form-group pull-right top_search">
           <div class="input-group">
             
           </div>
         </div>
       </div>
     </div>
  <div class="clearfix" style="background-color: #fff;">
    	<form class="form-horizontal form-label-left input_mask" id="content_form" data-parsley-validate enctype="multipart/form-data" action="" onsubmit="return false">
    		<input type="hidden" class="form-control" name="id" value="${(content.id)!}">
    		<#if (orignalPosters)??>
	    		<#list orignalPosters as poster>
	    			<input type="hidden" class="form-control" name="orignalPosters" value="${(poster)!}">
	    		</#list>
	    	</#if>
    		<div class="operate">
    			<a class="btn btn-primary" id="editBtn">编辑</a>
    			<input type="submit" class="btn btn-primary" value="保存" id="saveBtn" disabled="disabled" />
    			<!--<a class="btn btn-primary" href="javascript:history.go(-1)">返回</a>-->
    		</div>
    		<div class="form-group">
    			
	            <label class="control-label"><span class="required">*</span>标题：</label>
	            <div class="col-md-6 col-sm-6 col-xs-12">
	            	<input type="text" class="form-control readOnly contentItem" disabled="disabled" name="title" value="${(content.title)!}" data-parsley-required="true" data-parsley-required-message="正题名不可为空" data-parsley-maxlength="50" data-parsley-maxlength-message="正题名不可超过50位">
	            </div>
        	</div>
        	<div class="form-group tags-group">
	            <label class="control-label">标签：</label>
	            <div class="col-md-6 col-sm-6 col-xs-12">
	            	<input type="text" class="form-control readOnly contentItem" id="tags" name="tags"/>
	            	
	            </div>
        	</div>
        	<div class="form-group">
	            <label class="control-label"><span class="required">*</span>标题首字母：</label>
	            <div class="col-md-6 col-sm-6 col-xs-12">
	            	<input type="text" class="form-control readOnly contentItem" disabled="disabled" name="titleAbbr" value="${(content.titleAbbr)!}" data-parsley-required="true" data-parsley-pattern="^[a-zA-Z]+$" data-parsley-pattern-message="请输入英文首字母" data-parsley-required-message="正题名首字母不可为空">
	            </div>
        	</div>
        	
        	<div class="form-group">
	            <label class="control-label">内容描述：</label>
	            <div class="col-md-6 col-sm-6 col-xs-12">
	            	<textarea class="form-control readOnly contentItem" style="resize: none;" disabled="disabled" name="description" data-parsley-maxlength="500" data-parsley-maxlength-message="内容描述不可超过500位">${(content.description)!}</textarea>
	            </div>
        	</div>
        	<div class="form-group">
	            <label class="control-label">播出时间：</label>
	            <div class="col-md-6 col-sm-6 col-xs-12">
	            	<input type="text" class="form-control readOnly contentItem" disabled="disabled" name="publishTime" onClick="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss'})" value="${(content.publishTime)!}">
	            </div>
        	</div>
        	<div class="form-group poster-group">
	            <label class="control-label">海报：</label>
	            <#list content.imagesN as image>
	            	<div class="col-md-2 col-sm-2 col-xs-12">
		            	<div class="thumbnail" style="pointer-events: none;">
	                      <div class="image view view-first">
	                        <img style="width: 100%; display: block;" src="${(image.url)!}" id="img${image_index}" alt="image" name="image">
	                        <input type="hidden" name="imgId" value="${(image.id)!}"/>
	                        <div class="mask">
	                          <p></p>
	                          <div class="tools tools-bottom">
	                            <a href="javascript:void(0);" class="change_poster">更换</a>
	                          </div>
	                        </div>
	                      </div>
	                      <div class="caption">
	                        <span>${(image.width)!}</span> x <span>${(image.height)!}</span>
	                        <span class="status">${(image.size)!}</span>
	                      </div>
	                      <div class="description">
	                        <span>${(image.description)!}</span>
	                      </div>
	                    </div>
	            	</div>
	            </#list>
	            <#if (emptyImg)??>
	            	<#list emptyImg as image>
	            	<div class="col-md-2 col-sm-2 col-xs-12">
		            	<div class="thumbnail" style="pointer-events: none;">
	                      <div class="image view view-first">
	                        <img style="width: 100%; display: block;" src="${(project.staticDomain!)}/images/postadd.png" class="change_poster" id="img${image_index+5}" alt="image" name="image"/>
	                      	<input type="hidden" name="imgId"/>
	                      </div>
	                      <div class="caption">
	                      </div>
	                      <div class="description">
	                      </div>
	                    </div>
	            	</div>
	            	</#list>
	            </#if>
        	 </div>
	         <#list content.movies! as movie>
	            <div class="form-group">
		            <label class="control-label">视频：</label>
		            <div class="col-md-6 col-sm-6 col-xs-12">
		            	<span class="form-control" style="background-color:#eeeeee;height:auto">${(movie.url)!}</span>
		            	<!--<input type="text" class="form-control contentItem" disabled="disabled" value="${(movie.url)!}">-->
		            </div>
        		</div>
	         </#list>
	         <input type="hidden" name="source" value="${(content.source)!}">
	         <input type="hidden" name="duration" value="${(content.movies[0].duration)!}">
	         <input type="hidden" name="insertedAt" value="${(content.insertedAt)!}">
    	</form>
    	<hr>
    	<div class="details">
	    	<div class="detail1">
	    		<label>内容来源：</label>
	    		<span>${(content.source)!}</span>
	    	</div>
	    	<!--<div class="detail1">
	    		<label>CDN反馈时间：</label>
	    		<span>${(content.source)!}</span>
	    	</div>-->
	    	<div class="detail1">
	    		<label>时长：</label>
	    		<span id="durationShow">${(content.movies[0].duration)!}</span>
	    	</div>
	    	<div class="detail1">
	    		<label>入站时间：</label>
	    		<span>${(content.insertedAt)!}</span>
	    	</div>
    	
    	<div class="detail2">
    		<label class="control-label">所属分类：</label>
    		<div class="classify col-md-6 col-sm-6 col-xs-12">
    		<#list content.classifications! as classification>
    			<div>
    				<span>${(classification.hierarchyName)!}</span>
    			</div>
    		</#list>
    		
    		</div>
    	</div>
    	</div>
  </div>
</div>
<!-- /page content -->
<!-- add_poster -->
<div class="modal fade bs-example-modal-lg" id="add_poster" tabindex="-1" role="dialog" aria-hidden="true">
    <div class="modal-dialog modal-lg">
      <div class="modal-content">

        <div class="modal-header">
          <button type="button" class="close" data-dismiss="modal"><span aria-hidden="true">×</span>
          </button>
          <h4 class="modal-title" id="myModalLabel">选择海报</h4>
        </div>
        <div class="modal-body">
          	<div class="" role="main">
  <div class="">
    <div class="clearfix"></div>

    <div class="row">
      <div class="col-md-12">
        <div class="x_panel">
          <div class="x_content">
          	<div class="search">
				  <div class="form-group" style="display:inline-block;">
				    <label class="control-label" for="source">来源：</label>
				    <select class="form-control" id="source" style="display:inline-block;width:200px">
				    </select>
				  </div>
		      		<a class="btn btn-primary" id="up_img" data-toggle="modal" data-target="" style="float:right"><i class="fa fa-plus-square-o"></i> 上传</a>
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
<div class="modal fade posterSelect" tabindex="-1" role="dialog" aria-hidden="true" id="addPoster">
    <div class="modal-dialog modal-lg">
      <div class="modal-content">

        <div class="modal-header">
          <button type="button" class="close closePoster"><span aria-hidden="true">×</span>
          </button>
          <h4 class="modal-title" id="myModalLabel">添加海报</h4>
        </div>
        <div class="modal-body">
          	<form id="add_form" class="form-horizontal form-label-left" data-parsley-validate="" enctype="multipart/form-data" method="post">
              <div class="form-group">
                 <label for="middle-name" class="control-label col-md-3 col-sm-3 col-xs-12"><span class="required">*</span>海报</label>
                <div class="col-md-6 col-sm-6 col-xs-12">
                    <input name="poster_file" type="file" required class="form-control"/>
                    <span id="poster_required" class="requireds" style="color:red"></span>
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
          <button type="button" class="btn btn-default" id="cancle_poster">取消</button>
          <button type="button" class="btn btn-primary" id="add_sure">确定</button>
        </div>

      </div>
    </div>
</div>
        </div>

      </div>
    </div>
</div>
<!-- /add_poster -->
<script src="${ (project.staticDomain)! }/libs/bootstrap-tokenfield/bootstrap-tokenfield.min.js"></script>
<script src="${ (project.staticDomain)! }/libs/jquery-form/jquery-form-20131225.min.js"></script>
<script src="${ (project.staticDomain)! }/libs/twbs-pagination/jquery.twbsPagination.js" type="text/javascript"></script>
<script src="${ (project.staticDomain)! }/js/content/edit.js"></script>
<script type="text/javascript">
	currentTags = '${(content.tags)!}';
	base = $("#base").attr("href");
</script>
