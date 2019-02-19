$(function() {
	initComponents();
	/** 监听所有event事件 */
	addEventListenerByEdit();
});
var add_true = false;
var edit_true = true;
// 监听事件
function addEventListenerByEdit() {
	$("#edit-save").click(saveTreeNodeMessage);
	$("#add_save").click(addTreeNodeMessage);
	$("#add_cancle").click(addCancleTreeNodeMessage);
	$("#change_icon").click(changeIcon);
	$("#cancle_poster").click(hideAdd);// 隐藏上传
	$("#close_pb").click(hideAdd);// 隐藏上传
	$("#add_sure").click(addSure);// 确定上传
	//$("#name").blur(checkName);// 验证分类名
	//$("#name-add").blur(checkName);// 验证分类名
	$("#add_save").attr("disabled",false);
}

function setZtreeHeight(){
	$("#columnTree").height(document.body.clientHeight);
}

/**
 * 隐藏上传
 */
function hideAdd(){
	$("input[name='poster_file']").val("");
	$("#poster_required").text("");
	$('.posterSelect').modal('hide');
}
function initComponents() {
	if(nodes == null | getJsonLength(nodes) <= 0 ){
		$(".zTreeDemoBackground").append("<div class='left'id='createNodeDiv'>"
			+"<div><label class='control-label'for='createRootNodeMsg'>暂无分类树</label></div>"
			+"<button type='button' class='btn btn-primary' id='createRootNode'><i class='fa fa-plus-square-o'></i> 创建根目录</button>"
			+"</div>");
		$("#createRootNode").bind("click",toCreateRootNode);
		$("#columnTree").hide();
		$("#currentDiv").hide();
		$("#change_icon").attr("src",base+"/static/images/postadd-old.png");
	}else{
		if($("#createNodeDiv").length > 0){
			$("#createNodeDiv").remove();
		}
		initMetadataTree("columnTree");
		$("#columnTree").show();
		$("#currentDiv").show();
		// 初始化详情
		toEditNode(null);
	}
	
	setZtreeHeight();
}
/**
 * 创建跟节点.
 */
function toCreateRootNode() {
	$("#addClassification").modal("show");
}
// 判断json属性
function getJsonLength(jsonData) {
	var jsonLength = 0;
	for ( var item in jsonData) {
		jsonLength++;

	}
	return jsonLength;

}
// 初始化栏目树
function initMetadataTree(treeId) {
	console.log(nodes);
	var setting = {
		view : {
			addHoverDom : addHoverDom,
			removeHoverDom : removeHoverDom,
			selectedMulti : false,
			fontCss : setFontCss
		},
		edit : {
			enable : true,
			showRemoveBtn : false,
			showRenameBtn : false,
			drag : {
				isCopy : true,
				isMove : true,
				prev : true,
				next : true,
				inner : true
			}
		},
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
			onClick : zTreeOnClick,
			beforeDrag:beforeDrag,
			beforeDrop:beforeDrop,
			onDrop:onDrop
		}
	};
	$.fn.zTree.init($("#" + treeId), setting, nodes);
	var tree = $.fn.zTree.getZTreeObj("columnTree");
	var root = tree.getNodeByParam("pId", null, null);
	tree.selectNode(root);
	tree.expandAll(true);
}
/**
 * 开始拖拽
 */ 
function beforeDrag(treeId, treeNodes){
	 console.log("开启拖拽");
     return true;	
}
/**
 * 可以拖拽
 */
function beforeDrop(treeId, treeNodes, targetNode, moveType, isCopy) {
	if(parseInt(treeNodes[0].editable)==0){
		$.Zebra_Dialog("不允许拖拽本节点", {
			type : 'warning',
			title : '警告'
		});

		return false;
	}
	if (treeNodes[0].level == 0) {
		$.Zebra_Dialog("不允许拖拽根节点", {
			type : 'warning',
			title : '警告'
		});

		return false;
	}
	 if(targetNode.pId==null){
		 $.Zebra_Dialog("不允许拖拽到根节点", {
	    	    type: 'warning',
	    	    title: '警告'
	    	});
		 return false;
	 }
}
/**
 * 拖拽完毕.
 */
function onDrop(event, treeId, treeNodes, targetNode, moveType, isCopy){
	 console.log("拖拽完毕");
   saveDrop(event, treeId, treeNodes, targetNode, moveType, isCopy);
   return true;
     
}
/**
 * 拖拽完成保存数据.
 */
function saveDrop(event, treeId, treeNodes, targetNode, moveType, isCopy) {
	console.log("event=" + event);
	console.log("treeId=" + treeId);
	console.log("treeNodes=" + treeNodes);
	// console.log("targetNode=" + targetNode.name);
	console.log("moveType=" + moveType);
	console.log("isCopy=" + isCopy);
	if (moveType == null) {
		return;
	}
	var id = treeNodes[0].id;// 源id
	//var nrgt = treeNodes[0].rgt;// 源右点
	var tid = targetNode.id;// 目标id
	//var trgt = targetNode.rgt;// 目标右点
	//var mid = targetNode.id;// 目标点id
	var data = {
		"id" : id,
		"tid" : tid,
//		"tlft" : tlft,
//		"trgt" : trgt,
//		"mid" : mid,
		"moveType" : moveType
	};
	$.ajax({
		type : 'POST',
		contentType : 'application/json;charset=UTF-8',
		dataType : 'json',
		data : JSON.stringify(data),
		cache : false,
		url : base + '/classification/saveDrop',
		success : function(json) {
			if (json.statusCode == !200) {
				warning("拖拽失败");
			} else {
				window.location.href = base
						+ "/classification/classificationManage"
				// initMetadataTree("columnTree");
			}
		}
	});

}

/**
 * 刷新当前选择节点的父节点
*/
function refreshParentNode(treeNodes) {
	var treeObj = $.fn.zTree.getZTreeObj("columnTree");
	treeObj.refresh();
}
/**
 * 敲击分类节点获取节点内容
 * 
 */
function zTreeOnClick(event, treeId, treeNode) {
	$(".requireds").text("");
	toEditNode(treeNode);
}
function toEditNode(treeNode) {
	var id = "";
	if (treeNode != null) {
		id = treeNode.id;
	}
	$.ajax({
		type : "POST",
		url : base + "/classification/getClassificationDetail?id=" + id,
		async : false,
		processData : false,
		success : function(json) {
			if (json.statusCode == 200) {
				// 页面赋值
				$("#name").val(json.data.name);
				$("#name").attr("attr",json.data.id);
				$("#id").val(json.data.id);
				$("#alias").val(json.data.alias);
				if (parseInt(json.data.status) == 0) {
					$("#statusNn").prop('checked', true);
					$("#statusNy").prop('checked', false);
				} else if(parseInt(json.data.status) == 1) {
					$("#statusNy").prop('checked', true);
					$("#statusNn").prop('checked', false);
				}
				if (parseInt(json.data.type) == 0) {
					$("#typeNn").prop('checked', true);
					$("#typeNy").prop('checked', false);
					$("#typeNyd").prop('checked', false);
				} else if (parseInt(json.data.type) == 1){
					$("#typeNy").prop('checked', true);
					$("#typeNn").prop('checked', false);
					$("#typeNyd").prop('checked', false);
				} else{
					$("#typeNy").prop('checked', false);
					$("#typeNn").prop('checked', false);
					$("#typeNyd").prop('checked', true);
				}
				if (parseInt(json.data.editable) == 0) {
					$("#edit_n").prop('checked', true);
					$("#edit_y").prop('checked', false);
					//$('#demo_form').find('input,button').prop('readonly',true);
					$('#demo_form').find('input,button').prop('disabled',true);

				} else if(parseInt(json.data.editable) == 1) {
					$("#edit_y").prop('checked', true);
					$("#edit_n").prop('checked', false);
					//$('#demo_form').find('input,button').prop('readonly',false);
					$('#demo_form').find('input,button').prop('disabled',false);
				}
				if (parseInt(json.data.deletable) == 0) {
					$("#del_n").prop('checked', true);
					$("#del_y").prop('checked', false);
					$('#del_n').prop('disabled',true);
					$('#del_y').prop('disabled',true);
				} else if(parseInt(json.data.deletable) == 1) {
					$("#del_y").prop('checked', true);
					$("#del_n").prop('checked', false);
				}
				if (null == json.data.recommend || parseInt(json.data.recommend) == 0) {
					$("#recommend_n").prop('checked', true);
					$("#recommend_y").prop('checked', false);
				} else if(parseInt(json.data.recommend) == 1) {
					$("#recommend_y").prop('checked', true);
					$("#recommend_n").prop('checked', false);
				}
				
				if (parseInt(json.data.code) == 1) {
					$("#change_icon").attr("src",base+"/static/images/postadd-old.png");
				} else {
					$("#change_icon").attr("src",json.data.icon);
				}
				$("input[name='icon']").val(json.data.icon);
			} else {
				$.Zebra_Dialog(json.message, {
					'type' : 'information',
					'title' : "提示",
					'buttons' : [ "确定" ]
				});
			}
		},
		error : function() {
			$.Zebra_Dialog("节点基本信息获取失败", {
				'type' : 'error',
				'title' : "提示",
				'buttons' : [ "确定" ]
			});
		}
	});
}
//替换名称中的特殊字符为空
function stripscript(value) {
	var pattern = new RegExp(/^[A-Za-z0-9\u4e00-\u9fa5]+$/);
	if (pattern.test(value)) {
        return false;
    }
    return true;
}
// 保存节点信息
function saveTreeNodeMessage() {
	var query = new Object();
	// 节点id
	var id = $("input[name='id']").val();
	query["id"] = $("input[name='id']").val();
	// 节点名称
	var name = $("input[name='name']").val();
	
	if (name == null || name == "") {
		$("#name-required").text("分类名不能为空");
		return;
	}
	if(stripscript(name)){
		$("#name-required").text("分类名称不能特殊字符!");
		return;
	}
	query["name"] =name;
	// 状态
	var status = $('input:radio[name="status"]:checked').val();
	query["status"] = status;
	// 类型
	var type = $('input:radio[name="type"]:checked').val();
	query["type"] = type;
	// 图标
	var icon = $("input[name='icon']").val();
	query["icon"] = icon;
	// 是否可编辑
	var editable = $('input:radio[name="edit"]:checked').val();
	query["editable"] = editable;
	// 是否可删除
	var deletable = $('input:radio[name="del"]:checked').val();
	query["deletable"] = deletable;
	// 是否推荐
	var recommend = $('input:radio[name="recommend"]:checked').val();
	query["recommend"] = recommend;
	var alias = $("input[name='alias']").val();
	query["alias"] = alias;
	if (alias == null || alias == "") {
		$.ajax({
			type : 'POST',
			contentType : 'application/json;charset=UTF-8',
			dataType : 'json',
			data : JSON.stringify(query),
			cache : false,
			url : base + '/classification/saveClassificationManage',
			success : function(json) {
				if (json.statusCode == 200) {
					var message = json.message;
					window.location.href = base + "/classification/classificationManage";
				} else {
					$.Zebra_Dialog(json.message + "更新失败!", {
						'type' : 'information',
						'title' : "提示",
						'buttons' : [ "确定" ],
						'onClose' : function() {
						}
					});
				}
			}
		});
	}else{
		$.ajax({
		type : 'POST',
		contentType : 'application/json;charset=UTF-8',
		dataType : 'json',
		data : JSON.stringify(query),
		cache : false,
		url : base + "/classification/checkName",
		success : function(json) {
			if (json.statusCode != 200) {
				$("#alias_required").text("分类别名已存在");
				return;
			}else{
				$.ajax({
					type : 'POST',
					contentType : 'application/json;charset=UTF-8',
					dataType : 'json',
					data : JSON.stringify(query),
					cache : false,
					url : base + '/classification/saveClassificationManage',
					success : function(json) {
						if (json.statusCode == 200) {
							var message = json.message;
							window.location.href = base + "/classification/classificationManage";
						} else {
							$.Zebra_Dialog(json.message + "更新失败!", {
								'type' : 'information',
								'title' : "提示",
								'buttons' : [ "确定" ],
								'onClose' : function() {
								}
							});
						}
					}
				});
			}
		}
	});
	}
}

function addHoverDom(treeId, treeNode) {
	var sObj = $("#" + treeNode.tId + "_span");

	// 如果之前有按钮，这直接返回
	if ($("#startBtn_" + treeNode.tId).length > 0
			|| $("#addBtn_" + treeNode.tId).length > 0
			|| $("#editBtn_" + treeNode.tId).length > 0
			|| $("#removeBtn_" + treeNode.tId).length > 0
			|| $("#stopBtn_" + treeNode.tId).length > 0
			|| $("#syncBtn_" + treeNode.tId).length > 0
			|| $("#syncFailedBtn_" + treeNode.tId).length > 0) {

		return;
	}
	var addStr = "";
	if (treeNode.status == 1 || treeNode.status == 0) {
		addStr += "<span class='button add' id='addBtn_" + treeNode.tId
				+ "' title='新增' onfocus='this.blur();'></span>";
	}

	if (treeNode.pId!=null && treeNode.deletable==1) {
		addStr += "<span class='button remove' id='removeBtn_" + treeNode.tId
				+ "' title='删除' onfocus='this.blur();'></span>";
	}
	sObj.after(addStr);

	$("#addBtn_" + treeNode.tId).bind("click", function() {
		toAddNode(treeId, treeNode);
		return false;
	});

	$("#removeBtn_" + treeNode.tId).bind("click", function() {
		toRemoveNode(treeId, treeNode);
		return false;
	});

};

// 隐藏按钮(鼠标移开节点上触发)
function removeHoverDom(treeId, treeNode) {

	$("#addBtn_" + treeNode.tId).unbind().remove();
	$("#editBtn_" + treeNode.tId).unbind().remove();
	$("#removeBtn_" + treeNode.tId).unbind().remove();
};
// 添加节点
function toAddNode(treeId, treeNode) {
	$("#id_add").val(treeNode.id);// 当前id
	$("#level_add").val(treeNode.level);// 当前左值
	//$("#rgt_add").val(treeNode.rgt);// 当前左值
	$(".requireds_add").text("");
	$("input[name='name_add']").val("");
	$("input[name='alias_add']").val("");
	//$("input[name=status_add]:eq(0)").prop("checked",'checked');
	$("#add_save").attr("disabled",false);
	$("#addClassification").modal("show");
}
// 保存添加节点信息
function addTreeNodeMessage() {
	var query = new Object();
	var query1 = new Object();
	// 获取当前id
	query["pId"] = $("input[name='id_add']").val();
	// 获取当前左值
	query["level"] = $("input[name='level_add']").val();
	// 分类名
	var name = $("input[name='name_add']").val();
	if (name == null || $.trim(name) == "") {
		$("#name_add_required").text("分类名称不能为空!");
		return;
	}
	if(stripscript(name)){
		$("#name_add_required").text("分类名称不能特殊字符!");
		return;
	}
	query1["name"] = name;
	query["name"] = name;
	// 状态
	var status = $('input:radio[name="status_add"]:checked').val();
	query["status"] = status;
	// 类型
	var type = $('input:radio[name="type_add"]:checked').val();
	query["type"] = type;
	// 是否可编辑
	var editable = $('input:radio[name="edit_add"]:checked').val();
	query["editable"] = editable;
	// 是否可删除
	var deletable = $('input:radio[name="del_add"]:checked').val();
	query["deletable"] = deletable;
	// 是否推荐
	var recommend = $('input:radio[name="recommend_add"]:checked').val();
	query["recommend"] = recommend;
	var alias = $("input[name='alias_add']").val();
	query["alias"] = alias;
	var objButton=$(this);
	if (alias == null || alias == "") {
		$.ajax({
			type : 'POST',
			contentType : 'application/json;charset=UTF-8',
			dataType : 'json',
			data : JSON.stringify(query),
			cache : false,
			url : base + "/classification/addClassificationManage",
			beforeSend:function(){//触发ajax请求开始时执行
				 objButton.attr('disabled',true);//改变提交按钮上的文字并将按钮设置为不可点击
				},
				success : function(json) {
				if (json.statusCode == 200) {
					window.location.href = base
							+ "/classification/classificationManage"
				} else {
					$.Zebra_Dialog(json.message, {
						'type' : 'information',
						'title' : "提示",
						'buttons' : [ "确定" ],
						'onClose' : function() {
						}
					});
				}
			},
	        complete:function(){//ajax请求完成时执行
	            objButton.attr('disabled',false);//改变提交按钮上的文字并将按钮设置为可以点击
	        }

		});
	}else {
		$.ajax({
		type : 'POST',
		contentType : 'application/json;charset=UTF-8',
		dataType : 'json',
		data : JSON.stringify(query),
		cache : false,
		url : base + "/classification/checkName",
		success : function(json) {
			if (json.statusCode != 200) {
				$("#alias_add_required").text("分类别名已存在");
				return;
			}else{
				$.ajax({
					type : 'POST',
					contentType : 'application/json;charset=UTF-8',
					dataType : 'json',
					data : JSON.stringify(query),
					cache : false,
					url : base + "/classification/addClassificationManage",
					beforeSend:function(){//触发ajax请求开始时执行
						 objButton.attr('disabled',true);//改变提交按钮上的文字并将按钮设置为不可点击
						},
						success : function(json) {
						if (json.statusCode == 200) {
							window.location.href = base
									+ "/classification/classificationManage"
						} else {
							$.Zebra_Dialog(json.message, {
								'type' : 'information',
								'title' : "提示",
								'buttons' : [ "确定" ],
								'onClose' : function() {
								}
							});
						}
					},
			        complete:function(){//ajax请求完成时执行
			            objButton.attr('disabled',false);//改变提交按钮上的文字并将按钮设置为可以点击
			        }

				});
			}
		}
	});
	}

}
/**
 * 取消添加
 */
function addCancleTreeNodeMessage() {
	$("#id_add").val("");// 当前id
	$("#level_add").val("");// 当前左值
	$("#rgt_add").val("");// 当前右值
	$(".requireds_add").text("");
	$("#addClassification").modal("hide");
}

/**
 * 删除节点.
 */
function toRemoveNode(treeId, treeNode){
	$.Zebra_Dialog("确定删除分类"+treeNode.name+"吗？", {
		'type' : 'warning',
		'title' : '提示',
		'buttons' : ["取消", "确定"],
		'onClose' : function(caption) {
			var option = (caption != '' ? '"' + caption + '"'
					: 'nothing');
			if (option == "\"确定\"") {
				removeNode(treeId, treeNode);
			}
		}
	});
}

function removeNode(treeId, treeNode) {
	var query = new Object();
	// 获取当前id
	query["id"] = treeNode.id;
	// 获取当前左值
	query["lft"] = treeNode.lft;
	// 获取当前右值
	query["rgt"] = treeNode.rgt;
	// 分类名
	var objButton=$(this);
	$.ajax({
		type : 'POST',
		contentType : 'application/json;charset=UTF-8',
		dataType : 'json',
		data : JSON.stringify(query),
		cache : false,
		url : base + "/classification/removeClassificationManage",
		beforeSend:function(){//触发ajax请求开始时执行
			 objButton.attr('disabled',true);//改变提交按钮上的文字并将按钮设置为不可点击
			},
		success : function(json) {
			if (json.statusCode == 200) {
				window.location.href = base + "/classification/classificationManage"
				var message = json.message;
			} else {
				$.Zebra_Dialog(json.message, {
					'type' : 'information',
					'title' : "提示",
					'buttons' : [ "确定" ],
					'onClose' : function() {
					}
				});
			}
		},
		complete:function(){//ajax请求完成时执行
			objButton.attr('disabled',false);//改变提交按钮上的文字并将按钮设置为可以点击
		}
	});
}
function setFontCss(treeId, treeNode) {
	return treeNode.status == 0 ? {color:"#b7a5a5"} : {};
};
function classificationLoading(){
	$('body').loading({
		loadingWidth:240,
		title:'请稍等!',
		name:'classificationLoading',
		discription:'正在与站点同步中...',
		direction:'column',
		type:'origin',
		originDivWidth:40,
		originDivHeight:40,
		originWidth:6,
		originHeight:6,
		smallLoading:false,
		loadingMaskBg:'rgba(0,0,0,0.2)'
	});
}



function changeIcon(){
	$("#poster_required").text("");
	$('#addPoster').modal('show');
}

function addSure(){
	var file = $("input[name='poster_file']").val();
	var point = file.lastIndexOf("."); 
	var type = file.substr(point);
	if(type!=".jpg"&&type!=".png"&&type!=".JPG"&&type!=".PNG"){ 
         $("#poster_required").text("文件类型错误,请使用jpg或png");
         return false;
    }
	$("#add_form").ajaxSubmit({
	 	url: base + '/classification/photo',
	    type: 'POST',
	    cache: false,
	    dataType:'json',
	    processData: false,
		success: function(result) {  
			if (result.statusCode == 100000) {
				hideAdd();// 隐藏
				// 设置图片url
				var url = result.url;
				$("#change_icon").attr("src",url);
				$("input[name='icon']").val(url);
				}else{
					hideAdd();// 隐藏
					$.Zebra_Dialog(result.message, {
					    type: 'error',
					    title: '上传失败',
					    buttons:  ['确定'],
					});
				}
	  }
 });
}

/**
 * 验证不重名.
 */
function checkName() {
	var name = $(this).val();
	var id = $(this).attr("attr");
	var query = new Object();
	// 获取当前id
	query["name"] = name;
	query["id"] = id;
	$.ajax({
		type : 'POST',
		contentType : 'application/json;charset=UTF-8',
		dataType : 'json',
		data : JSON.stringify(query),
		cache : false,
		url : base + "/classification/checkName",
		success : function(json) {
			if (json.statusCode != 200) {
				if (id == null || id == "") {
					$(".check_name_add").text("分类名已存在");
					add_true = false;
				} else {
					$(".check_name").text("分类名已存在");
					edit_true = false;
				}
			}else{
				if (id == null || id == "") {
					$(".check_name_add").text("");
					add_true = true;
				} else {
					$(".check_name").text("");
					edit_true = true;
				}
			}
		}
	});
}
