$(function(){
	var table;
	var base = $("#base").attr("href");
	var top1=$("#operate").offset().top;
    /** 初始化所有组件 */
    initComponents();

    /** 监听所有event事件 */
    addEventListener();
    
    /**
     * 组装时间控件
     */
    function initComponents()
    {
    	initDataTable();
    }

    /**
     * 组装event监听
     */
    function addEventListener()
    {
    	$("#search").click(search);
    	$("#shelve").click(upOrdown);
    	$("#unShelve").click(upOrdown);
    	$("#addClassify").click(addClassify);
    	$("#delete").click(deleteContent);
    	$("#chk_all").click(checkAll);
    	$("#add_submit").click(addSubmit);
    	$("#exportExcel").click(exportExcel);// 导出Excel
    	var  width = "";
    	$(window).scroll(operateFixed);
    }
    function operateFixed(){
    	var  win_top=$(this).scrollTop();
    	var  top=$("#operate").offset().top;
    	
    	
    	if(win_top>=top){
    		$("#operate").css("width",width);
    		$("#operate").addClass("sfixed");
    	}
    	if(win_top<top1){
    		$("#operate").removeClass("sfixed");
    		width = $("#operate").css("width");
    	}
    	
    };
    /**
     * 搜索
     */
    function search(){
    	var oTable = $("#table").dataTable(); //table1为表格的id
		var tableSetings=oTable.fnSettings();  
		var paging_length=tableSetings._iDisplayLength;//当前每页显示多少  
		var page_start=tableSetings._iDisplayStart;//当前页开始  
		var page=(page_start / paging_length); //得到页数值  比页码小1
		oTable.fnPageChange(page);//加载跳转
    	$("#chk_all").prop("checked", false);
    }
    /**
     * 删除
     */
    function deleteContent(){
    	if ($("input[name='chk_list']:checked").length<=0) { 
    		warning("至少选择一条内容！");
			 return false;
		 }else{
			 $.Zebra_Dialog("确定删除吗？", {
				    'type':     'warning',
				    'title':    '提示',
				    'buttons':  ['取消', '确定'],
				    'onClose':  function(caption) {
				    	var option=(caption != '' ? '"' + caption + '"' : 'nothing');
				        if("\"确定\""==option){
				        	var ids=[];
				            $("input[name='chk_list']:checkbox").each(function(){ 
				                if($(this).prop("checked")){
				            		ids.push($(this).val());
				                }
				            });
				            console.log(ids)
				            $.ajax({
				        		type: "get",
				                url: base+"/contents/delete?ids="+ids,
				                success: function(data){
				                	//location.reload();
				                	search();
				                	$("#chk_all").prop("checked", false);
				                },
				                error: function(){
				                	warning(warn);
				                }
				        	});
				            
				        }else{
				        	return;
				        }
				    }
				});
		 }
    }
    /**
     * 上下架
     */
    function upOrdown(){
		var status=$(this).data("status");
    	console.log(status);
    	var title = "";
    	var warn = "";
    	if(status == 2){
    		title = "确定要上架吗？";
    		warn = "上架失败";
    	}else{
    		title = "确定要下架吗？";
    		warn = "下架失败";
    	}
    	if ($("input[name='chk_list']:checked").length<=0) { 
    		warning("至少选择一条内容！");
			 return false;
		 }else{
			 $.Zebra_Dialog(title, {
				    'type':     'question',
				    'title':    '请确认',
				    'buttons':  ['取消', '确定'],
				    'onClose':  function(caption) {
				    	var option=(caption != '' ? '"' + caption + '"' : 'nothing');
				        if("\"确定\""==option){
				        	var ids=[];
				            $("input[name='chk_list']:checkbox").each(function(){ 
				                if($(this).prop("checked")){
				            		ids.push($(this).val());
				                }
				            });
				            $.ajax({
				        		type: "get",
				                url: base+"/contents/"+status+"/upOrdown?ids="+ids,
				                success: function(data){
				                	information(data.message);
				                	search();
				                	$("#chk_all").prop("checked", false);
				                },
				                error: function(){
				                	warning(warn);
				                }
				        	});
				            
				        }else{
				        	return;
				        }
				    }
				});
		 }
    }
    /**
     * 加入分类
     */
    function addClassify(){
    	if ($("input[name='chk_list']:checked").length<=0) { 
    		warning("至少选择一条内容！");
			 return false;
		 }else{
			 var ids=[];
	         $("input[name='chk_list']:checkbox").each(function(){ 
	         	if($(this).prop("checked")){
	            	ids.push($(this).val());
	         	}
	         });
	         $("#ids").val(ids);
			 
		    	var setting = {
	    			view : {
	    				fontCss : setFontCss
	    			},
					check: {
						enable: true,
						chkboxType:{ "Y" : "", "N" : "" }
					},
					data: {
						simpleData: {
							enable: true
						},
						keep : {
							parent : true
						},
						key : {},
					}
				};
		    	$.ajax({
		    		type: "POST",
		            url: base+"/classification/getTree",
		            success: function(data){
		            	$("#add_classify").modal("show");
		            	var zNodes = JSON.parse(data.data.tree);
		            	$.fn.zTree.init($("#classifyTree"), setting, zNodes);
		            	var treeObj = $.fn.zTree.getZTreeObj("classifyTree");
		            	treeObj.expandAll(true);
		            },
		            error: function(){
		            	$("#loading").hide();
		            	$("#search_artist_button").show();
		            	error();
		            }
		    	});
		 }
    	
    	
    }
    
    function setFontCss(treeId, treeNode) {
    	return treeNode.status == 0 ? {color:"#b7a5a5"} : {};
    };
    
    function addSubmit(){
    	var treeObj = $.fn.zTree.getZTreeObj("classifyTree");
    	var checkedNodes = treeObj.getCheckedNodes();
    	var classifyArr = [];
    	for(var i=0;i<checkedNodes.length;i++){
    		classifyArr.push(checkedNodes[i].id);
    	}
    	if (classifyArr.length<=0) { 
    		$(".classifyInfo").show();
			 return false;
		 }else{
			$(".classifyInfo").hide();
			var cIds = $("#ids").val();
	    	console.log(classifyArr)
	    	console.log(cIds)
	    	$.ajax({
	    		type: "POST",
	            url: base+"/contents/addClassification?cfIds="+classifyArr+"&cIds="+cIds,
	            success: function(data){
	            	confirmation("加入分类成功");
	            	search();
	            	
	            },
	            error: function(){
	            	$("#loading").hide();
	            	$("#search_artist_button").show();
	            	error();
	            }
	    	});
		 }
    	
    }
    /**
     * 列表展示
     */
    function initDataTable(){
    	table=$('#table').on('order.dt',
		        function() {
    				$("#chk_all").prop("checked", false);
		        }).on('search.dt',
		        function() {
		        	$("#chk_all").prop("checked", false);
		        }).on('page.dt',
		        function() {
		        	$("#chk_all").prop("checked", false);
		    }).DataTable( {
    		"bProcessing": true,//加载中开启
    		"bServerSide": true,
    		"bSort": true,
    		"bDeferRender": true,//是否启用延迟加载：当你使用AJAX数据源时，可以提升速度。
    		"aLengthMenu": [20, 25,30,100,200,500],
    		"scrollY": "900px",
    		"scrollCollapse": "true",
            "columns": [
            	{ "data":null},
                { "data": "title" },     
                { "data": "publishTime" },
                { "data": null },
                { "data": "insertedAt" },
                { "data": null },
                { "data": "status" },
                { "data":null}
            ],
            "bFilter" : false,// 搜索栏
            "oLanguage": language,
            "rowCallback": function( row, data, index ) {
            	
            	$('td:eq(0)', row).html("<input name='chk_list' id='"+data.id+"' type='checkbox' value='"+data.id+"' data-content-status="+data.status+">")
            	
            	if(data.images.length > 0){
            		if(data.images[0].url != null){
            			var image = '<div class="content-div"><img src="'+data.images[0].url+'"  onerror="this.src='+"'/static/images/error.png'"+'" title="'+data.images[0].url+'"  class="img-rounded content-image" /></div>';
            			$('td:eq(1)', row).html(image+"&nbsp;&nbsp;"+"<div class='content-div'><div><span><strong>"+data.title+"</strong></span><br><span>来源频道："+data.sourceChannel+"</span><br><span>来源栏目："+data.sourceColumn+"</span><br><span>节目时长："+sec_to_time(Math.floor(data.duration/1000))+"</span><div></div>");
            		}else
            			$('td:eq(1)', row).html("<div class='content-div'><img src='/static/images/empty.png' title='"+data.images[0].url+"' class='img-rounded content-image'/></div>&nbsp;&nbsp;"+"<div class='content-div'><div><span><strong>"+data.title+"</strong></span><br><span>来源频道："+data.sourceChannel+"</span><br><span>来源栏目："+data.sourceColumn+"</span><br><span>节目时长："+sec_to_time(Math.floor(data.duration/1000))+"</span><div></div>");
	            }else{
	            	$('td:eq(1)', row).html("<div class='content-div'><img src='/static/images/empty.png'  class='img-rounded content-image'/></div>&nbsp;&nbsp;"+"<div class='content-div'><div><span><strong>"+data.title+"</strong></span><br><span>来源频道："+data.sourceChannel+"</span><br><span>来源栏目："+data.sourceColumn+"</span><br><span>节目时长："+sec_to_time(Math.floor(data.duration/1000))+"</span><div></div>");
	            }
            	
            	var classifications = "";
            	for(c in data.classifs){
            		classifications +="<div style='line-height: 24px;'><a  name='removeClassification' data-content-id="+data.id+" data-classification-id="+data.classifs[c].classificationId+" data-toggle='tooltip' data-placement='left' title='移出分类'><i class='fa fa-remove'></i></a>"+data.classifs[c].classificationName+"</div>";
            	}
            	$('td:eq(3)',row).html("<div class='classification'>"+classifications+"</div>");
            	
            	switch(data.cdnSyncStatus){
					case 0:
						$('td:eq(5)', row).html("<span class='label label-info'>未同步</span>");
					  break;
					case 1:
						$('td:eq(5)', row).html("<span class='label label-success'>已同步</span>");
					  break;
					case 2:
						$('td:eq(5)', row).html("<span class='label label-warning'>同步失败</span>");
					  break;
					case 3:
						$('td:eq(5)', row).html("<span class='label label-info'>进行中</span>");
					  break;
					default:
						$('td:eq(5)', row).html("<span class='label label-danger'>未知状态</span>");
				}
            	
            	switch(data.status){
					case 0:
						$('td:eq(6)', row).html("<span class='label label-warning'>已删除</span>");
					  break;
					case 1:
						$('td:eq(6)', row).html("<span class='label label-info'>未上架</span>");
					  break;
					case 2:
						$('td:eq(6)', row).html("<span class='label label-success'>已上架</span>");
					  break;
					case 3:
						$('td:eq(6)', row).html("<span class='label label-success'>数据不合法(未入存储)</span>");
					  break;
					case 4:
						$('td:eq(6)', row).html("<span class='label label-success'>已删除存储</span>");
					  break;
					default:
						$('td:eq(6)', row).html("<span class='label label-danger'>未知状态</span>");
				}
            
            	if(contentEditFlag){
            		$('td:eq(7)', row).html("<a href='javascript:void(0)' style='display:inline;' name='update-content' data-content-id="+data.id+" data-toggle='tooltip' data-placement='left' title='编辑'>"+
            				"<i class='glyphicon glyphicon-edit'></i>"+
            		"</a>");
            	}else{
            		$('td:eq(7)', row).html("<a href='javascript:void(0)' style='display:none;'  name='update-content' data-content-id="+data.id+" data-toggle='tooltip' data-placement='left' title='编辑'>"+
            				"<i class='glyphicon glyphicon-edit'></i>"+
            		"</a>");
            	}
                
                $('[data-toggle="tooltip"]',row).tooltip();
                $("a[name='update-content']",row).click(editContent);
                $("a[name='removeClassification']",row).click(removeClassification);
             },
             "fnServerParams": function ( data ) {
                 data.push(
                         { "name": "title", "value": $("#title").val() },
                         { "name": "publishTimeBegin", "value": $("#play-start").val() },
                         { "name": "publishTimeEnd", "value": $("#play-end").val() },
                         { "name": "insertedBegin", "value": $("#site-start").val() },
                         { "name": "insertedEnd", "value": $("#site-end").val() },
                         { "name": "status", "value": $("#shelve-status").val() },
                         { "name": "sourceChannel", "value": $("#sourceChannel").val() },
                         { "name": "sourceColumn", "value": $("#sourceColumn").val() }
                         
                 );
	         },
	         fnDrawCallback: function(table) {
					$("#table_paginate").append("<div style='display: inline;margin-left: 10px;'>  到第 <input style='height:28px;width:40px;' class='margin text-center' id='changePage' type='text'> 页  <a class='btn btn-default'  href='javascript:void(0);' id='dataTable-btn'>确认</a></div>");
					var oTable = $("#table").dataTable();
					$('#dataTable-btn').click(function(e) {
						if($("#changePage").val() && $("#changePage").val() > 0) {
							var redirectpage = $("#changePage").val() - 1;
						} else {
							var redirectpage = 0;
						}
						oTable.fnPageChange(redirectpage);
					});
				},
	         "aaSorting": [[ 4, "desc" ]],
	         "columnDefs":[
	        	 {"orderable":false,"targets":[0,3,5,6,7]}
	         ],
	         "sAjaxSource": base+"/contents/ajax",
	         "fnServerData" : function(sSource, data, fnCallback) {
	                $.ajax({
	                    "type" : 'post',
	                    "url" : sSource,
	                    "dataType" : "json",
	                    "data" : {
	                        data : JSON.stringify(data)
	                    },
	                    "success" : function(resp) {
	                        fnCallback(resp);
	                    }
	                });

	          }
    	
        } );
    }
    /**
     * 编辑
     */
    function editContent(){
		var obj=$(this);
		var content_id=obj.data("content-id");
		window.open(base+"/contents/"+content_id, "_blank");
	}
    
    /**
     * 移出分类提示
     */
    function removeClassification(){
		var obj=$(this);
		var content_id=obj.data("content-id");
		var contentIds = new Array();
		contentIds.push(content_id);
		var classification_id=obj.data("classification-id");
		$.Zebra_Dialog("确定将所选内容移出此分类吗？", {
			'type' : 'question',
			'title' : '提示',
			'buttons' : ["取消", "确定"],
			'onClose' : function(caption) {
				var option = (caption != '' ? '"' + caption + '"' : 'nothing');
				if (option == "\"确定\"") {
					remove(classification_id, contentIds,obj);
				}
			}
		});
	}
    
    /**
     * 移出分类
     */
    function remove(classid, contentIds,obj) {
    	var data = {
    		"classid" : classid,
    		"contentIds" : contentIds
    	}
    	$.ajax({
    		type : 'POST',
    		contentType : 'application/json;charset=UTF-8',
    		dataType : 'json',
    		data : JSON.stringify(data),
    		cache : false,
    		url : base + "/classification/removeContent",
    		success : function(json) {
    			if (json.statusCode == 200) {
    				obj.parent().remove();
    				information(json.message);
    				search();// 刷新页面
    			} else {
    				warning(json.message);
    			}
    		}
    	});
    	
    }
 // 导出Excel
	function exportExcel() {
		$.Zebra_Dialog("确认导出Excel数据?", {
		    'type':     'question',
		    'title':    '请确认',
		    'buttons':  ['取消', '确定'],
		    'onClose':  function(caption) {
		    	var option=(caption != '' ? '"' + caption + '"' : 'nothing');
		        if("\"确定\""==option){
		        	var data = [];
		   		 data.push(
		                    { "name": "title", "value": $("#title").val() },
		                    { "name": "publishTimeBegin", "value": $("#play-start").val() },
		                    { "name": "publishTimeEnd", "value": $("#play-end").val() },
		                    { "name": "insertedBegin", "value": $("#site-start").val() },
		                    { "name": "insertedEnd", "value": $("#site-end").val() },
		                    { "name": "status", "value": $("#shelve-status").val() },
		                    { "name": "sourceChannel", "value": $("#sourceChannel").val() },
		                    { "name": "sourceColumn", "value": $("#sourceColumn").val() }
		                    
		            );
		   		 console.log("查询参数为:"+JSON.stringify(data));
		   		window.location.href=base + "/contents/exportExcel?data="+JSON.stringify(data); 
		        }else{
		        	return;
		        }
		    }
		});
	}
});
