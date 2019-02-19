$(function() {
	initComponents();
	/** 监听所有event事件 */
	addEventListenerByEdit();
	var table;
	var top1=$("#operate").offset().top;
	var width = "";
	

function initComponents() {
	initMetadataTree("columnTree");
	$("#columnTree").show();
	$("#currentDiv").show();
	initDataTable();// 初始化表格
	setZtreeHeight();
}


/**
 * 监听事件
 */
function addEventListenerByEdit() {
	$("#chk_all").click(checkAll);
	//$("#add_classify").click(addContent);// 分类添加内容
	$("#remove_classify").click(removeContent);// 移出分类
	$("#search").click(search);// 查找
	$("#upper_shelve").click(upOrdown);// 上架
	$("#un_shelve").click(upOrdown);// 下架
	$("#delete").click(deleteContent);// 删除
	$("#recommendManage").click(recommendManage);//置顶管理
	//$(".delete_recommend").click(deleteRecommend);// 删除置顶
	$("body").delegate(".delete_recommend","click",deleteRecommend);
	
	$(".change_recommend").click(changeRecommend);// 替换置顶
	$("#change_recommend_submit").click(saveChange);//保存替换置顶
	$("#recommend_submit").click(saveDrop);//保存拖动置顶
	
	$("#addClassify").click(addClassify); //加入分类
	$("#add_submit").click(addSubmit);
	$(window).scroll(operateFixed);
};

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

function setZtreeHeight(){
	$("#columnTree").height(document.body.clientHeight);
}

// 初始化栏目树
function initMetadataTree(treeId) {
//	console.log(nodes);
	var setting = {
		view : {
			selectedMulti : false,
			fontCss : setFontCss
		},
		edit : {},
		data : {
			keep : {
				parent : true
			},
			key : {},
			simpleData : {
				enable : true
			}
		},
		callback : {
			onClick : zTreeOnClick
		}
	};
	$.fn.zTree.init($("#" + treeId), setting, nodes);
	var tree = $.fn.zTree.getZTreeObj("columnTree");
	var first = tree.getNodeByParam("pId", null, null);
	tree.selectNode(first);
	tree.expandAll(true);
	// 设置初始选中分类id
	if (first != null) {
		$("#classi_id").val(first.id);
	}
}

// 敲击分类节点获取节点内容
function zTreeOnClick(event, treeId, treeNode) {
	// 获取当前节点id并隐藏于页面
	var id = treeNode.id;
	$("#classi_id").val(id);
	// 获取数据刷新页面
	search();
}

/**
 * 搜索
 */
function search() {
//	table.draw();
	var oTable = $("#table").dataTable(); //table1为表格的id
	var tableSetings=oTable.fnSettings();  
	var paging_length=tableSetings._iDisplayLength;//当前每页显示多少  
	var page_start=tableSetings._iDisplayStart;//当前页开始  
	var page=(page_start / paging_length); //得到页数值  比页码小1
	oTable.fnPageChange(page);//加载跳转
	$("#chk_all").prop("checked", false);
}
/**
 * 初始化表格
 */
function initDataTable() {
	table=$('#table').DataTable( {
		"bProcessing": true,//加载中开启
		"bServerSide": true,
		"bSort": false,
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
            { "data": null ,"width":"8%"},
            { "data": null ,"width":"8%"},
            { "data": null,"width":"5%"}
        ],
        "bFilter" : false,// 搜索栏
        "oLanguage": language,
        "rowCallback": function( row, data, index ) {
        	
        	$('td:eq(0)', row).html("<input name='chk_list' id='"+data.cid+"' type='checkbox' value='"+data.cid+"'/>")
        	
        	if(data.images.length > 0){
        		if(data.images[0].url != null){
        			var image = '<div class="content-div"><img src="'+data.images[0].url+'"  onerror="this.src='+"'/static/images/error.png'"+'" title="'+data.images[0].url+'"  class="img-rounded content-image" /></div>';
        			if(data.recommend > 0 )//推荐
        				$('td:eq(1)', row).html(image+"&nbsp;&nbsp;"+"<div class='content-div'><div><span><strong>"+data.title+"</strong></span><span class='Rflag'>置顶</span><br><span>来源频道："+data.sourceChannel+"</span><br><span>来源栏目："+data.sourceColumn+"</span><br><span>节目时长："+sec_to_time(Math.floor(data.duration/1000))+"</span><div></div>");
        			else{
        				$('td:eq(1)', row).html(image+"&nbsp;&nbsp;"+"<div class='content-div'><div><span><strong>"+data.title+"</strong></span><br><span>来源频道："+data.sourceChannel+"</span><br><span>来源栏目："+data.sourceColumn+"</span><br><span>节目时长："+sec_to_time(Math.floor(data.duration/1000))+"</span><div></div>");
        			}
        		}else{
        			if(data.recommend > 0 )//推荐
        				$('td:eq(1)', row).html("<div class='content-div'><img src='/static/images/empty.png' title='"+data.images[0].url+"' class='img-rounded content-image'/></div>&nbsp;&nbsp;"+"<div class='content-div'><div><span><strong>"+data.title+"</strong></span><span class='Rflag'>置顶</span><br><span>来源频道："+data.sourceChannel+"</span><br><span>来源栏目："+data.sourceColumn+"</span><br><span>节目时长："+sec_to_time(Math.floor(data.duration/1000))+"</span><div></div>");
        			else{
        				$('td:eq(1)', row).html("<div class='content-div'><img src='/static/images/empty.png' title='"+data.images[0].url+"' class='img-rounded content-image'/></div>&nbsp;&nbsp;"+"<div class='content-div'><div><span><strong>"+data.title+"</strong></span><br><span>来源频道："+data.sourceChannel+"</span><br><span>来源栏目："+data.sourceColumn+"</span><br><span>节目时长："+sec_to_time(Math.floor(data.duration/1000))+"</span><div></div>");
        			}
        		}
            }else{
            	if(data.recommend > 0 )//推荐
            		$('td:eq(1)', row).html("<div class='content-div'><img src='/static/images/empty.png'  class='img-rounded content-image'/></div>&nbsp;&nbsp;"+"<div class='content-div'><div><span><strong>"+data.title+"</strong></span><span class='Rflag'>置顶</span><br><span>来源频道："+data.sourceChannel+"</span><br><span>来源栏目："+data.sourceColumn+"</span><br><span>节目时长："+sec_to_time(Math.floor(data.duration/1000))+"</span><div></div>");
            	else
            		$('td:eq(1)', row).html("<div class='content-div'><img src='/static/images/empty.png'  class='img-rounded content-image'/></div>&nbsp;&nbsp;"+"<div class='content-div'><div><span><strong>"+data.title+"</strong></span><br><span>来源频道："+data.sourceChannel+"</span><br><span>来源栏目："+data.sourceColumn+"</span><br><span>节目时长："+sec_to_time(Math.floor(data.duration/1000))+"</span><div></div>");
            }
        	
        	var classifications = "";
        	for(c in data.classifs){
        		classifications +="<div style='line-height: 24px;'><a  name='removeClassification' data-content-id="+data.cid+" data-classification-id="+data.classifs[c].classificationId+" data-toggle='tooltip' data-placement='left' title='移出分类'><i class='fa fa-remove'></i></a>"+data.classifs[c].classificationName+"</div>";
        	}
        	$('td:eq(3)',row).html("<div class='classification'>"+classifications+"</div>");
        	
        	if(data.recommend > 0 ){
//        		$('td:eq(1)', row).html(data.title+"<span class='Rflag'>置顶</span>");
        		$('td:eq(7)', row).html("<a href='javascript:void(0)' style='margin-left:0' name='update-content' data-content-id="+data.cid+" data-toggle='tooltip' data-placement='left' title='编辑'>"+
						"<i class='glyphicon glyphicon-edit'></i></a>");
        	}else{
//        		$('td:eq(1)', row).html(data.title);
        		$('td:eq(7)', row).html("<a href='javascript:void(0)' style='margin-left:0' name='update-content' data-content-id="+data.cid+" data-toggle='tooltip' data-placement='left' title='编辑'>"+
						"<i class='glyphicon glyphicon-edit'></i></a>"+
					"<a href='javascript:void(0)' name='recommend' data-content-id="+data.id+" data-toggle='tooltip' data-placement='left' title='置顶'>"+
						"<i class='fa fa-upload'></i></a>");
        	}
        	
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
        	
        	var tatus="";
        	 switch (parseInt(data.status)) {
	             case (0):
	            	 tatus="<span class='label label-warning'>已删除</span>";
	                 break;
	             case (1):
	            	 tatus="<span class='label label-info'>未上架</span>";
	                 break;
	             case (2):
	            	 tatus="<span class='label label-success'>已上架</span>";
	                 break;
	             case (3):
	            	 tatus="<span class='label label-success'>数据不合法(未入存储)</span>";
	                 break;
	             case (4):
	            	 tatus="<span class='label label-success'>已删除存储</span>";
	                 break;
	             default:
	            	 tatus="<span class='label label-danger'>未知状态</span>";
	             	break;
            }
        	 
        	 $('td:eq(6)', row).html(tatus);
        	 
        	 $("a[name='update-content']",row).click(editContent);
        	 $("a[name='recommend']",row).click(recommendContent);
        	 $("a[name='removeClassification']",row).click(removeClassification);
         },
			
         "fnServerParams": function (data) {
             data.push(
                     { "name": "title", "value": $("#title").val() },
                     { "name": "status", "value": $("#shelve-status").val()},
                     { "name": "classid", "value": $("#classi_id").val()}
                     
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
         "sAjaxSource": base+"/classification/ajax",
         "fnServerData" : function(sSource, data, fnCallback) {
                $.ajax({
                    "type" : 'post',
                    "url" : base+"/classification/ajax",
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

function setFontCss(treeId, treeNode) {
	return treeNode.status == 0 ? {color:"#b7a5a5"} : {};
};
/**
 * 取消全选
 * 
 */
function unACheck(){
	$('#chk_all').prop("checked", false);
}
/**
 * 全选
 */
function checkAll() {
	var isThis = $(this);
	if (isThis.is(':checked')) {
		$('input[name="chk_list"]').prop("checked", this.checked);
	} else {
		$('input[name="chk_list"]').prop("checked", false);
	}
}
/**
 * 获取选中内容
 * @returns {Array}
 */
function getChecks() {
	var contentIds = new Array();
	$("input[name='chk_list']:checked").each(function() {
		contentIds.push($(this).val());
	})
	return contentIds;
}
/**
 * 将内容移出分类
 */
function removeContent() {
	//当前节点id
	var classid = $("#classi_id").val();
	if (classid == null || classid=="") {
		information("请选择节点");
		return;
	}
	// 获取选中内容id
	var contentIds = getChecks();
	if (contentIds == null || contentIds.length==0) {
		warning("至少选择一条内容！");
		return;
	}
	$.Zebra_Dialog("确定将所选内容移出此分类吗？", {
		'type' : 'question',
		'title' : '提示',
		'buttons' : ["取消", "确定"],
		'onClose' : function(caption) {
			var option = (caption != '' ? '"' + caption + '"'
					: 'nothing');
			if (option == "\"确定\"") {
				remove(classid, contentIds);
			}
		}
	});
}

function remove(classid, contentIds) {
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
				information(json.message);
				unACheck();
				search();// 刷新页面
			} else {
				warning(json.message);
			}
		}
	});
	
};

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
	var contentIds = getChecks();
	if (contentIds.length<=0) { 
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
			            $.ajax({
			        		type: "get",
			                url: base+"/classification/"+status+"/upOrdown?ids="+contentIds,
			                success: function(data){
			                	information(data.message);
			                	unACheck();
			                	search();
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
 * 删除
 */
function deleteContent(){
	
	var contentIds = getChecks();
	if (contentIds<=0) { 
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
			            console.log(contentIds)
			            $.ajax({
			        		type: "get",
			                url: base+"/classification/delete?ids="+contentIds,
			                success: function(data){
			                	unACheck();
			                	search();
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
 * 编辑
 */
function editContent(){
	var obj=$(this);
	var content_id=obj.data("content-id");
	window.open(base+"/contents/"+content_id, "_blank");
}
/**
 * 置顶
 */
function recommendContent(){
	var id = $(this).attr("data-content-id");
	// 设置要置顶id
	if(id!=null){
		$("#change_id").val(id);
	}
	var cid = $("#classi_id").val();
	 $.ajax({
 		type: "get",
         url: base+"/classification/getRecommoned?id="+cid,
         success: function(data){
        	 $("#recommendItem").modal("show");
        	 var url = base+"/static/images/postadd.png";
        	 if(data.statusCode==200){
        		 var dto3 = data.data.dto3;
				 if (dto3 != null) {
					 //dto1 = JSON.parse(dto1);
					 if(dto3.url!=null){
						 url = dto3.url;
					 }
					 $("#add_img_1").attr("src",url);
					 $("#add_img_1").parent().find("input[name='id']").val(dto3.id);
					 $("#add_img_1").parent().find("input[name='oldId']").val(dto3.id);
					 $("#add_img_1").parent().parent().find(".caption").find(".wh").text(dto3.wh);
					 $("#add_img_1").parent().parent().find(".caption").find(".size").text(dto3.size);
					 $("#add_img_1").parent().parent().find(".description").find("span").text(dto3.title);
					 url = base+"/static/images/postadd.png";// 还原
				 }else{
					 url = base+"/static/images/postadd.png";// 还原
					 $("#add_img_1").attr("src",url);
					 $("#add_img_1").parent().find("input[name='id']").val("");
					 $("#add_img_1").parent().find("input[name='oldId']").val("");
					 $("#add_img_1").parent().parent().find(".caption").find(".wh").text("");
					 $("#add_img_1").parent().parent().find(".caption").find(".size").text("");
					 $("#add_img_1").parent().parent().find(".description").find("span").text("");
				 }
				 var dto2 = data.data.dto2;
				 if (dto2 != null) {
					 //dto2 = JSON.parse(dto2);
					 if(dto2.url!=null){
						 url = dto2.url;
					 }
					 $("#add_img_2").attr("src",url);
					 $("#add_img_2").parent().find("input[name='id']").val(dto2.id);
					 $("#add_img_2").parent().find("input[name='oldId']").val(dto2.id);
					 $("#add_img_2").parent().parent().find(".caption").find(".wh").text(dto2.wh);
					 $("#add_img_2").parent().parent().find(".caption").find(".size").text(dto2.size);
					 $("#add_img_2").parent().parent().find(".description").find("span").text(dto2.title);
					 url = base+"/static/images/postadd.png";// 还原
				 }else{
					 url = base+"/static/images/postadd.png";// 还原
					 $("#add_img_2").attr("src",url);
					 $("#add_img_2").parent().find("input[name='id']").val("");
					 $("#add_img_2").parent().find("input[name='oldId']").val("");
					 $("#add_img_2").parent().parent().find(".caption").find(".wh").text("");
					 $("#add_img_2").parent().parent().find(".caption").find(".size").text("");
					 $("#add_img_2").parent().parent().find(".description").find("span").text("");
				 }
				 var dto1 = data.data.dto1;
				 if (dto1 != null) {
					 //dto3 = JSON.parse(dto3);
					 if(dto1.url!=null){
						 url = dto1.url;
					 }
					 $("#add_img_3").attr("src",url);
					 $("#add_img_3").parent().find("input[name='id']").val(dto1.id);
					 $("#add_img_3").parent().find("input[name='oldId']").val(dto1.id);
					 $("#add_img_3").parent().parent().find(".caption").find(".wh").text(dto1.wh);
					 $("#add_img_3").parent().parent().find(".caption").find(".size").text(dto1.size);
					 $("#add_img_3").parent().parent().find(".description").find("span").text(dto1.title);
					 url = base+"/static/images/postadd.png";// 还原
				 }else{
					 url = base+"/static/images/postadd.png";// 还原
					 $("#add_img_3").attr("src",url);
					 $("#add_img_3").parent().find("input[name='id']").val("");
					 $("#add_img_3").parent().find("input[name='oldId']").val("");
					 $("#add_img_3").parent().parent().find(".caption").find(".wh").text("");
					 $("#add_img_3").parent().parent().find(".caption").find(".size").text("");
					 $("#add_img_3").parent().parent().find(".description").find("span").text("");
				 }
        	 }
         },
         error: function(){
         	warning("置顶数据获取失败!");
         }
 	});
}
function recommendManage(){
	var id = $("#classi_id").val();
	$.ajax({
 		type: "get",
 		url: base+"/classification/getRecommoned?id="+id,
         success: function(data){
        	 $("#recommend").modal("show");
        	 $("#recommendPosters .col-md-4 .image .mask").remove();
        	 var url = base+"/static/images/default.jpg";
        	 if(data.statusCode==200){
        		 var dto1 = data.data.dto3;
				 if (dto1 != null) {
					 //dto1 = JSON.parse(dto1);
					 if(dto1.url!=null){
						 url = dto1.url;
					 }
					 $("#recommendPosters .col-md-4:nth-child(1) img").attr("src",url);
					 $("#recommendPosters .col-md-4:nth-child(1) input[name='id']").val(dto1.id);
					 $("#recommendPosters .col-md-4:nth-child(1) input[name='oldId']").val(dto1.id);
					 $("#recommendPosters .col-md-4:nth-child(1) span.wh").text(dto1.wh);
					 $("#recommendPosters .col-md-4:nth-child(1) span.size").text(dto1.size);
					 $("#recommendPosters .col-md-4:nth-child(1) div.description").find("span").text(dto1.title);
					 url = base+"/static/images/default.jpg";// 还原
					 var html = '<div class="mask" style="display: block;"><div class="tools tools-bottom"><a href="javascript:void(0);" class="delete_recommend">删除置顶</a></div></div>';
					 $("#recommendPosters .col-md-4:nth-child(1) .image").append(html);
				 }else{
					 url = base+"/static/images/default.jpg";// 还原 
					 $("#recommendPosters .col-md-4:nth-child(1) img").attr("src",url);
					 $("#recommendPosters .col-md-4:nth-child(1) input[name='id']").val("");
					 $("#recommendPosters .col-md-4:nth-child(1) input[name='oldId']").val("");
					 $("#recommendPosters .col-md-4:nth-child(1) span.wh").text("");
					 $("#recommendPosters .col-md-4:nth-child(1) span.size").text("");
					 $("#recommendPosters .col-md-4:nth-child(1) div.description").find("span").text("");
				 }
				 var dto2 = data.data.dto2;
				 if (dto2 != null) {
					 if(dto2.url!=null){
						 url = dto2.url;
					 }
					 
					 $("#recommendPosters .col-md-4:nth-child(2) img").attr("src",url);
					 $("#recommendPosters .col-md-4:nth-child(2) input[name='id']").val(dto2.id);
					 $("#recommendPosters .col-md-4:nth-child(2) input[name='oldId']").val(dto2.id);
					 $("#recommendPosters .col-md-4:nth-child(2) span.wh").text(dto2.wh);
					 $("#recommendPosters .col-md-4:nth-child(2) span.size").text(dto2.size);
					 $("#recommendPosters .col-md-4:nth-child(2) div.description").find("span").text(dto2.title);
					 url = base+"/static/images/default.jpg";// 还原
					 var html = '<div class="mask" style="display: block;"><div class="tools tools-bottom"><a href="javascript:void(0);" class="delete_recommend">删除置顶</a></div></div>';
					 $("#recommendPosters .col-md-4:nth-child(2) .image").append(html);
				 }else{
					 url = base+"/static/images/default.jpg";// 还原 
					 $("#recommendPosters .col-md-4:nth-child(2) img").attr("src",url);
					 $("#recommendPosters .col-md-4:nth-child(2) input[name='id']").val("");
					 $("#recommendPosters .col-md-4:nth-child(2) input[name='oldId']").val("");
					 $("#recommendPosters .col-md-4:nth-child(2) span.wh").text("");
					 $("#recommendPosters .col-md-4:nth-child(2) span.size").text("");
					 $("#recommendPosters .col-md-4:nth-child(2) div.description").find("span").text("");
				 }
				 var dto3 = data.data.dto1;
				 if (dto3 != null) {
					 if(dto3.url!=null){
						 url = dto3.url;
					 }
					 $("#recommendPosters .col-md-4:nth-child(3) img").attr("src",url);
					 $("#recommendPosters .col-md-4:nth-child(3) input[name='id']").val(dto3.id);
					 $("#recommendPosters .col-md-4:nth-child(3) input[name='oldId']").val(dto3.id);
					 $("#recommendPosters .col-md-4:nth-child(3) span.wh").text(dto3.wh);
					 $("#recommendPosters .col-md-4:nth-child(3) span.size").text(dto3.size);
					 $("#recommendPosters .col-md-4:nth-child(3) div.description").find("span").text(dto3.title);
					 url = base+"/static/images/default.jpg";// 还原
					 var html = '<div class="mask" style="display: block;"><div class="tools tools-bottom"><a href="javascript:void(0);" class="delete_recommend">删除置顶</a></div></div>';
					 $("#recommendPosters .col-md-4:nth-child(3) .image").append(html);
				 }else{
					 url = base+"/static/images/default.jpg";// 还原 
					 $("#recommendPosters .col-md-4:nth-child(3) img").attr("src",url);
					 $("#recommendPosters .col-md-4:nth-child(3) input[name='id']").val("");
					 $("#recommendPosters .col-md-4:nth-child(3) input[name='oldId']").val("");
					 $("#recommendPosters .col-md-4:nth-child(3) span.wh").text("");
					 $("#recommendPosters .col-md-4:nth-child(3) span.size").text("");
					 $("#recommendPosters .col-md-4:nth-child(3) div.description").find("span").text("");
				 }
        	 }
         },
         error: function(){
         	warning("置顶数据获取失败!");
         }
 	});
}

/**
 * 删除置顶
 */
function deleteRecommend() {
	var id = $(this).parent().parent().parent().find("input[name='id']").val();
	if(id==null||id==""){
		$("#recommend").modal("hide");
     	warning("此位置无内容!");
		return;
	}
	$.ajax({
 		type: "post",
         url: base+"/classification/removeRecommed?id="+id,
         success: function(data){
        	 $("#recommend").modal("hide");
        	 if(data.statusCode==200){
        		 search();
        	 }else{
        		 warning("删除置顶失败!");
        	 }
         },
         error: function(){
        	$("#recommend").modal("hide");
         	warning("删除置顶失败!");
         }
 	});
}
/**
 * 替换置顶.
 */
function changeRecommend() {
	var id = $("#change_id").val();
	var id1=$("#id_1").val();
	var id2=$("#id_2").val();
	var id3=$("#id_3").val();
	if (id == id1 || id == id2 || id == id3) {
		$("#recommendItem").modal("hide");
		$.Zebra_Dialog("当前内容不能重复替换", {
			'type' : 'information',
			'title' : "提示",
			'buttons' : [ "确定" ],
			'onClose' : function() {
				$("#recommendItem").modal("show");
			}
		});
		return;
	}
	var now = $(this);
	$.ajax({
		type : "post",
		url : base + "/classification/get?id=" + id,
		success : function(data) {
			// $("#recommend").modal("hide");
			if (data.statusCode == 200) {
				var dto = data.data.dto;
				if (dto != null) {
					var url = base+"/static/images/postadd.png";// 还原
					if(dto.url!=null){
						url = dto.url;
					}
					now.parent().parent().parent().find("img").attr("src",url);
					now.parent().parent().parent().find("input[name='id']").val(dto.id);
					now.parent().parent().parent().parent().find(".caption").find(".wh").text(dto.wh);
					now.parent().parent().parent().parent().find(".caption").find(".size").text(dto.size);
					now.parent().parent().parent().parent().find(".description").find("span").text(dto.title);
				} else {
					$("#recommendItem").modal("hide");
					warning("替换置顶失败!");
				}
			} else {
				$("#recommendItem").modal("hide");
				warning("替换置顶失败!");
			}
		},
		error : function() {
			$("#recommendItem").modal("hide");
			warning("替换置顶失败!");
		}
	});
}

function saveChange(){
	var list = [];
	var query1 = null;
	var id1 = $("#id_1").val();
	if(id1!=null){
		query1=new Object();
		query1["id"] = id1;
		query1["oldId"] = $("#oldId_1").val();
		query1["recommoned"] = 3;
	}
	if(query1!=null){
		list.push(query1);
	}
	var query2 = null;
	var id2 = $("#id_2").val();
	if(id2!=null){
		query2=new Object();
		query2["id"] = id2;
		query2["oldId"] = $("#oldId_2").val();
		query2["recommoned"] = 2;
	}
	if(query2!=null){
		list.push(query2);
	}
	var query3 = null;
	var id3 = $("#id_3").val();
	if(id3!=null){
		query3=new Object();
		query3["id"] = id3;
		query3["oldId"] = $("#oldId_3").val();
		query3["recommoned"] = 1;
	}
	if(query3!=null){
		list.push(query3);
	}
	if(list.length==0){
		$("#recommend").modal("hide");
		return;
	}
	var query=new Object();
	query["dtos"] = list;
	var objButton=$(this);
	$.ajax({
		type : 'POST',
		contentType : 'application/json;charset=UTF-8',
		dataType : 'json',
		data : JSON.stringify(query),
		cache : false,
		url : base + '/classification/updateRecommoned',
		beforeSend:function(){//触发ajax请求开始时执行
			 objButton.attr('disabled',true);//改变提交按钮上的文字并将按钮设置为不可点击
			},
		success : function(json) {
			if (json.statusCode == 200) {
				$("#recommendItem").modal("hide");
				search();
			} else {
				$("#recommendItem").modal("hide");
				warning("替换置顶失败!");
			}
		},
        complete:function(){//ajax请求完成时执行
            objButton.attr('disabled',false);//改变提交按钮上的文字并将按钮设置为可以点击
            $("#recommendItem").modal("hide");
        }
	});
	
}
/**
*保存拖到置顶
*/
function saveDrop(){
	var id1 = $("#recommendPosters .col-md-4:nth-child(1) input[name='id']").val();
	var id2 = $("#recommendPosters .col-md-4:nth-child(2) input[name='id']").val();
	var id3 = $("#recommendPosters .col-md-4:nth-child(3) input[name='id']").val();
	console.log(id1);
	console.log(id2);
	console.log(id3);
	var list = [];
	var query1 = null;
	if(id1!=null){
		query1=new Object();
		query1["id"] = id1;
		query1["recommoned"] = 3;
	}
	if(query1!=null){
		list.push(query1);
	}
	var query2 = null;
	if(id2!=null){
		query2=new Object();
		query2["id"] = id2;
		query2["recommoned"] = 2;
	}
	if(query2!=null){
		list.push(query2);
	}
	var query3 = null;
	if(id3!=null){
		query3=new Object();
		query3["id"] = id3;
		query3["recommoned"] = 1;
	}
	if(query3!=null){
		list.push(query3);
	}
	var query=new Object();
	query["dtos"] = list;
	var objButton=$(this);
	$.ajax({
		type : 'POST',
		contentType : 'application/json;charset=UTF-8',
		dataType : 'json',
		data : JSON.stringify(query),
		cache : false,
		url : base + '/classification/updateDrap',
		beforeSend:function(){//触发ajax请求开始时执行
			 objButton.attr('disabled',true);//改变提交按钮上的文字并将按钮设置为不可点击
			},
		success : function(json) {
			if (json.statusCode == 200) {
				$("#recommend").modal("hide");
				search();
			} else {
				$("#recommend").modal("hide");
				warning("置顶信息保存失败!");
			}
		},
        complete:function(){//ajax请求完成时执行
            objButton.attr('disabled',false);//改变提交按钮上的文字并将按钮设置为可以点击
            $("#recommend").modal("hide");
        }
	});
}
});