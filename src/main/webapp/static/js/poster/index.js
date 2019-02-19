$(function(){
	var base = $("#base").attr("href");
	
    /** 初始化所有组件 */
    initComponents();

    /** 监听所有event事件 */
    addEventListener();
    
    /**
     * 组装时间控件
     */
    function initComponents()
    {
    	initPosters(0);
    }

    /**
     * 组装event监听
     */
    function addEventListener()
    {
    	$("#add_sure").click(addSure);
    	$("#source").change(search);
    	$("#cancle_sure").click(hideModel);
    }
    /**
     * 隐藏上传框
     */
    function hideModel(){
    	$("input[name='poster_file']").val("");
    	$("#description").val("");
    	$("#poster_required").text("");
    	$('.bs-example-modal-lg').modal('hide');
    }
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
    										'<img style="width: 100%; display: block;" src="'+poster.url+'"/>'+
    										/*'<div class="mask no-caption">'+
    											'<div class="tools tools-bottom">'+
    												'<a href="#"><i class="fa fa-link"></i></a>'+
    												'<a href="#"><i class="fa fa-pencil"></i></a>'+
    												'<a href="#"><i class="fa fa-times"></i></a>'+
    											'</div>'+
    										'</div>'+*/
    									 '</div>'+
    									 '<div class="caption">'+
    									 	'<span>'+poster.width+' x '+poster.height+'</span><span class="float-right">'+poster.size+'</span>'+
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
    			
    		},
    		error:function(error){
    			error();
    		}
    	});
    }
 
    function search(){
    	initPosters(0);
    }
    
    function addSure(){
    	var file = $("input[name='poster_file']").val();
    	var point = file.lastIndexOf("."); 
    	var type = file.substr(point);
    	if(type!=".jpg"&&type!=".png"&&type!=".JPG"&&type!=".PNG"){ 
             $("#poster_required").text("文件类型错误,请使用jpg或png");
             return false;
        }
    	var desc = $("#description").val();
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
					hideModel();
					initPosters(0);
 				}else{
 					hideModel();
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
