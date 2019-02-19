$(function(){
    /** 初始化所有组件 */
    initComponents();

    /** 监听所有event事件 */
    addEventListener();
    
    var imageId = "";
    var posterUrl ="";
    
    /**
     * 组装时间控件
     */
    function initComponents()
    {
    	initTags();
    	initCurrentTags();
    	$('#content_form').parsley();
    	$(".change_poster").click(addPoster);
    	posterListener();
    	convertDurationShow();
    }

    /**
     * 组装event监听
     */
    function addEventListener()
    {
    	$("#editBtn").click(edit);
    	//$("#saveBtn").click(save);
    	$(".closePoster").click(closePoster);
    	 $('#content_form').parsley().on('field:validated', function() {
    		    var ok = $('.parsley-error').length === 0;
    		    $('.bs-callout-info').toggleClass('hidden', !ok);
    		    $('.bs-callout-warning').toggleClass('hidden', ok);
    		  })
    		  .on('form:submit', function() {
    			  console.log($("#content_form").serialize());
    			  $.ajax({
              		type: "post",
                      url: base+"/contents/update",
                      data:$("#content_form").serialize(),
                      success: function(data){
                      	$.Zebra_Dialog("更新成功", {
                      	    type: 'confirmation',
                      	    title: '提示',
                      	    buttons:  ['确定'],
                      	    onClose : function(caption) {
                      	    	//window.location.reload();
              				}
                      	});
                      },
                      error: function(){
                      	$.Zebra_Dialog('更新失败', {
                      	    type: 'error',
                      	    title: 'Error',
                      	    buttons:  ['确定'],
                      	});
                      }
              	});
    		  });
    }
    function closePoster(){
    	$('#addPoster').modal('hide');
    }
    function initDataTable(){
    	
    	
    }
    function initTags(){
    	$('#tags').tokenfield();
    }
    
    function convertDurationShow(){
    	var duration = $("#durationShow").html();
    	$("#durationShow").html(sec_to_time(Math.floor(duration/1000))+"&nbsp;&nbsp;&nbsp;("+duration+"毫秒)");
    }
    
    function initCurrentTags(){
    	console.log(currentTags)
    	//currentTags = JSON.parse(currentTags);
    	$('#tags').tokenfield('setTokens',currentTags);
    	$(".tokenfield input").attr("disabled","disabled");
    	$(".tokenfield").css("background-color","#eee");
    	$(".tokenfield .token .close").css("display","none");
    }
    /**
     * 编辑
     */
    function edit(){
    	$(".readOnly").removeAttr("disabled"); 
    	$("#saveBtn").removeAttr("disabled"); 
    	$("#tags").css("background-color","#fff");
    	$(".tokenfield input").removeAttr("disabled"); 
    	$(".tokenfield .token .close").css("display","inline-block");
    	$(".tokenfield").css("background-color","#fff");
    	$(".thumbnail").css("pointer-events","auto");
    }
    /**
     * 保存
     */
    function save(){
    	if(typeof($("#saveBtn").attr("disabled")) == "undefined"){
    		$('#content_form').parsley().on('form:success', function() {
            	$.ajax({
            		type: "post",
                    url: base+"/contents/update",
                    data:$("#content_form").serialize(),
                    success: function(data){
                    	$.Zebra_Dialog("更新成功", {
                    	    type: 'confirmation',
                    	    title: '提示',
                    	    buttons:  ['确定'],
                    	    onClose : function(caption) {
                    	    	window.location.reload();
            				}
                    	});
                    },
                    error: function(){
                    	$.Zebra_Dialog('更新失败', {
                    	    type: 'error',
                    	    title: 'Error',
                    	    buttons:  ['确定'],
                    	});
                    }
            	});
            });



    		
    	}else{
    		return false;
    	}
    	
    }
	 /**
	   * 更换/添加海报
	   */
	function addPoster() {
		// 清空select
		$("#source").empty();
		$.ajax({
			type : 'POST',
			contentType : 'application/json;charset=UTF-8',
			dataType : 'json',
			cache : false,
			url :base+'/contents/getSource',
			success : function(json) {
				var data = json.data.data;
				$("#source").append("<option value=''>全部</option>");
				for(var i=0;i<data.length;i++){
					$("#source").append("<option value='"+data[i].name+"'>"+data[i].name+"</option>");
				}
				$("#add_poster").modal("show");
				// 初始化页poster页面
				initPosters(0);
			}
		});
		// 获取当前图片id
		imageId = $(this).parent().parent().parent().find('img').attr("id");
	}
	/**
	 * 获取来源信息.
	 */
	function initPagination(page){
    	$('#pagination').twbsPagination('destroy');
    	pagObj = $('#pagination').twbsPagination({
            totalPages: page.count,
            startPage: page.index+1,
            next:"下一页",
            prev:"上一页",
            first:"首页",
            last:"尾页",
            onPageClick: function (event, page) {
                console.info(page + ' (from options)');
            }
        }).on('page', function (event, page) {
            console.info(page + ' (from event listening)');
            initPosters(page-1);
        });
    }
    
    function initPosters(index){
    	$.ajax({
    		dataType:"json",
    		type:"get",
    		data:{"index":index,"source":$("#source").val()},
    		url:base+"/posters/ajax",
    		success:function(result){
    			var page = result.data.page;
    			var posters = "";
    			for(var i in result.data.posters){
    				var poster = result.data.posters[i];
    				//var posterSize = (parseInt(poster.size)/1024).toFixed(2);
    				posters  += '<div class="col-md-55">'+
    								'<div class="thumbnail">'+
    									'<div class="image view view-first">'+
    										'<img style="width: 100%; display: block;" class="add_images" src="'+poster.url+'" attrdec="'+poster.description+'" attrwi="'+poster.width+'" attrhi="'+poster.height+'" attrsi="'+poster.size+'" attrid="'+poster.id+'"/>'+
    										/*'<div class="mask no-caption">'+
    											'<div class="tools tools-bottom">'+
    					
    											'</div>'+
    										'</div>'+*/
    									 '</div>'+
    									 '<div class="caption">'+
    									 	'<span>'+poster.width+'x'+poster.height+'</span><span class="float-right">'+poster.size+'</span>'+
    									 '</div>'+
    									 '<div class="description">'+
	 									 	'<span title='+poster.description+'>'+poster.description+'</span>'+
	 									 '</div>'+
    									'</div>'+
    								'</div>'+
    							'</div>';
    			}
    			$("#posters").empty();
    			$("#posters").append(posters);
    			
    			initPagination(page);
    			$(".add_images").click(getPostUrl);
    			// 监听事件
				//posterListener();
    			
    		},
    		error:function(error){
    			error();
    		}
    	});
    }
    /**
     * 添加海报事件监听
     */
    function posterListener(){
    	//$(".add_images").click(getPostUrl);
    	$("#source").change(search);// 来源查询
    	$("#add_sure").click(addSure);// 确定上传
    	$("#up_img").click(showAdd);// 展示上传
    	$("#cancle_poster").click(hideAdd);// 隐藏上传
    }
    /**
     * 显示上传
     */
    function showAdd(){
    	$('.posterSelect').modal('show');
    }
    /**
     * 隐藏上传
     */
    function hideAdd(){
    	$("input[name='poster_file']").val("");
    	$("#description").val("");
    	$("#poster_required").text("");
    	$('.posterSelect').modal('hide');
    }
    /**
     * 获取海报url
     */
	function getPostUrl() {
		var url = $(this).attr("src");
		var wi = $(this).attr("attrwi");// 宽度
		var hi = $(this).attr("attrhi");// 高度
		var si = $(this).attr("attrsi");// 大小
		var id = $(this).attr("attrid");// id
		var dec = $(this).attr("attrdec");// id
		if (url != null) {
			posterUrl = url;
		}
		var timestamp = (new Date()).valueOf();
		if (posterUrl != null && posterUrl != "") {
			$("#" + imageId).attr("src", posterUrl);
			//$("#" + imageId).parent().children(".mask").remove();
			$("#" + imageId).parent().append(
							"<div class='mask'><p></p><div class='tools tools-bottom'><a href='javascript:void(0);' class='change_poster"+timestamp+"'><i class='fa fa-link'>更换</i></a></div></div>");
			$("#" + imageId).parent().parent().find(".caption").html("");
			$("#" + imageId).parent().parent().find(".description").html("");
			$("#" + imageId).parent().parent().find(".caption").append('<span>'+wi+'</span> x <span>'+hi+'</span><span class="status">'+si+'</span>');
			$("#" + imageId).parent().parent().find(".description").append('<span>'+dec+'</span>');
			$(".change_poster"+timestamp).click(addPoster);
			//设置id值
			$("#" + imageId).parent().find("input").val(id);
			//('<input type="hidden" name="imgId" value="'+id+'"/>');
		}
		$("#add_poster").modal("hide");

	}
    function search(){
    	initPosters(0);
    }
    function addSure(){
    	var file = $("input[name='poster_file']").val();
    	var point = file.lastIndexOf("."); 
    	var type = file.substr(point);
    	var desc = $("#description").val();
    	if(type!=".jpg"&&type!=".png"&&type!=".JPG"&&type!=".PNG"){ 
             $("#poster_required").text("文件类型错误,请使用jpg或png！");
             return false;
        }
    	if(desc.length > 20){
    		$("#desc_required").text("标题不能超过20字！");
    		return false;
    	}
    	$("#add_form").ajaxSubmit({
		 	url: base+'/posters/photo',
		    type: 'POST',
		    cache: false,
		    dataType:'json',
		    processData: false,
			success: function(result) {  
				if (result.statusCode == 100000) {
					hideAdd();// 隐藏
					initPosters(0);
 				}else{
 					hideAdd();// 隐藏
 					$("#add_poster").modal('hide');// 海报页面隐藏
 					$.Zebra_Dialog(result.message, {
 					    type: 'error',
 					    title: '上传失败',
 					    buttons:  ['确定'],
 					});
 				}
		  }
	 });
    }
});
