$(function(){
	/** 初始化所有组件 */
	initComponents();
    /** 监听所有event事件 */
    addEventListeners();
})

/**
 * 组装event监听
 */
function addEventListeners()
{
   $("#add_channel").click(add);
   $("#add_cancle").click(hidden);
   $("#add_save").click(addSave);
   $("#edit-save").click(editSave);
}
function initComponents() {
	if(nodes == null | getJsonLength(nodes) <= 0 ){
		$(".zTreeDemoBackground").append("<div class='left'id='createNodeDiv'>"
			+"<div><label class='control-label'for='createRootNodeMsg'>暂无操作树</label></div>"
			+"<button type='button' class='btn btn-primary' id='createRootNode'><i class='fa fa-plus-square-o'></i> 创建根目录</button>"
			+"</div>");
		$("#createRootNode").bind("click", toAddNode);
		$("#columnTree").hide();
		$("#currentDiv").hide();
		$("#operate").hide();
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
}

//判断json属性
function getJsonLength(jsonData) {
	
	var jsonLength = 0;
	for ( var item in jsonData) {
		jsonLength++;

	}
	return jsonLength;

}
//初始化操作树
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
				isCopy : false,
				isMove : false,
				prev : false,
				next : false,
				inner : false
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
	console.log("moveType=" + moveType);
	console.log("isCopy=" + isCopy);
	var targetId = targetNode.id;// 目标点id
	var sourceId = treeNodes[0].id;// 源id
	var data = {
		"sourceId" : sourceId,
		"targetId" : targetId,
		"moveType" : moveType
	};
	$.ajax({
		type : 'POST',
		contentType : 'application/json;charset=UTF-8',
		dataType : 'json',
		data : JSON.stringify(data),
		cache : false,
		url : base + '/operation/saveDrop',
		success : function(json) {
			if (json.statusCode == !200) {
				warning("拖拽失败");
			} else {
				window.location.href = base + "/operation/index"
			}
		}
	});

}
//隐藏按钮(鼠标移开节点上触发)
function removeHoverDom(treeId, treeNode) {

	$("#addBtn_" + treeNode.tId).unbind().remove();
	$("#editBtn_" + treeNode.tId).unbind().remove();
	$("#removeBtn_" + treeNode.tId).unbind().remove();
};

function setFontCss(treeId, treeNode) {
	return treeNode.status == 0 ? {color:"#b7a5a5"} : {};
};

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
	if (treeNode.pId == null || treeNode.pId=="0") {
		addStr += "<span class='button add' id='addBtn_" + treeNode.tId
				+ "' title='新增' onfocus='this.blur();'></span>";
	}else{
		addStr += "<span class='button add' id='addBtn_" + treeNode.tId
		+ "' title='新增' onfocus='this.blur();'></span>";
		addStr += "<span class='button remove' id='removeBtn_" + treeNode.tId
		+ "' title='删除' onfocus='this.blur();'></span>";
	}

	//if (treeNode.pId!=null&&treeNode.pId>0) {
//		addStr += "<span class='button remove' id='removeBtn_" + treeNode.tId
//				+ "' title='删除' onfocus='this.blur();'></span>";
	//}
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

//隐藏按钮(鼠标移开节点上触发)
function removeHoverDom(treeId, treeNode) {
	$("#addBtn_" + treeNode.tId).unbind().remove();
	$("#removeBtn_" + treeNode.tId).unbind().remove();
};
// 添加节点
function toAddNode(treeId, treeNode) {
	if(treeNode!=null){
		$("#id_add").val(treeNode.id);// 当前id
	}
	$(".requireds_add").text("");
	$("input[name='add_name']").val("");
	$("#add_save").attr("disabled",false);
	$('#addChannel').modal('show');
}

/**
 * 删除节点.
 */
function toRemoveNode(treeId, treeNode){
	$.Zebra_Dialog("确定删除操作"+treeNode.name+"吗？", {
		'type' : 'warning',
		'title' : '提示',
		'buttons' : ["取消", "确定"],
		'onClose' : function(caption) {
			var option = (caption != '' ? '"' + caption + '"'
					: 'nothing');
			if (option == "\"确定\"") {
				deleteOperation(treeId, treeNode);
			}
		}
	});
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
	}else{
		 var treeObj = $.fn.zTree.getZTreeObj("columnTree");
		    //返回一个根节点
		 var node = treeObj.getNodesByFilter(function (node) { return node.level == 0 }, true);
		 id  = node.id;

	}
	$.ajax({
		type : "POST",
		url : base + "/operation/info?id=" + id,
		async : false,
		processData : false,
		success : function(json) {
				// 页面赋值
				$("#name").val(json.operation.name);
				$("#id").val(json.operation.id);
				$("#code").val(json.operation.code);
				$("#edit_url").val(json.operation.url);
				var permissions = json.permissions;
				var selectedPermisson= json.operation.permission;
				
				for(var i=0;i<permissions.length;i++){
            		var permission = permissions[i];
            		var permissonId = permission.id;
            		var contain = (selectedPermisson==permissonId);
            		if(contain){
            			
            			$("#permision_type").append('<option  selected value="'+permission.id+'">'+permission.typeName+'</option>');
            		}else{
            			$("#permision_type").append('<option value="'+permission.id+'">'+permission.typeName+'</option>');
            			
            		}
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





/**
 * 删除分类
 */
function deleteOperation(id, node) {
	$.ajax({
		type : "get",
		url : base + "/operation/delete?id=" + node.id,
		success : function(data) {
			window.location.href = base + "/operation/index";
		},
		error : function() {
			warning(warn);
		}
	});

}

/**
 * 添加分类
 */
function add() {
	$('#addChannel').modal('show');
}
/**
 * 创建跟节点.
 */
function toCreateRootNode() {
	$('#addChannel').modal('show');
}
/**
 * 隐藏添加
 */
function hidden(){
	$('#addChannel').modal('hide');
	$("#add_name").val("");
	$(".requireds_add").text("");
	$("#statusNy").prop('checked', true);
	
}
/**
 * 编辑保存
 */
function editSave(){
	var query = new Object();
	// 节点id
	var id = $("input[name='id']").val();
	query["id"] = $("input[name='id']").val();
	// 节点名称
	var name = $("input[name='name']").val();
	
	query["name"] = name;
	if (name == null || name == "") {
		$("#name-required").text("名称不能为空");
		return;
	}
	
	
	var code = $("input[name='code']").val();
	query["code"] = code;
	
//	var permission = $("#permision_type").val();
//	query["permission"] = permission;
	var url =$("#edit_url").val();
	query["url"] = url;
	
	// 图标
	$.ajax({
		type : 'POST',
		contentType : 'application/json;charset=UTF-8',
		dataType : 'json',
		data : JSON.stringify(query),
		cache : false,
		url : base + '/operation/edit',
		success : function(json) {
			if (json.statusCode == 200) {
				var message = json.message;
				window.location.href = base + "/operation/index";
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

//替换名称中的特殊字符为空
function stripscript(value) {
    var pattern = new RegExp("[`\"~!@#$%^&*()=|{}':;'+-,.\\[\\].<>/?~！@#￥%《》’-……&*（）——|{}+-【】‘；：”“'。，、？]") 
    var rs = ""; 
    for (var i = 0; i < value.length; i++) {
        rs = rs+value.substr(i, 1).replace(pattern, ''); 
    } 
    return rs;
}
/**
 * 添加保存
 */
function addSave() {
	var query = new Object();
	query["parentTId"] = $("#id_add").val();
	var name = $("input[name='add_name']").val();// 名称
	if (name == null || name == "") {
		$("#add_name_required").text("名称不能为空");
		return;
	}
	query["name"] = name;
	
	var code = $("input[name='add_code']").val();// 名称
	if (code == null || code == "") {
		$("#add_code_required").text("操作编码不能为空");
		return;
	}
	query["code"] = code;
	
	var url = $("input[name='add_url']").val();// 名称
//	if (url == null || url == "") {
//		$("#add_url_required").text("拦截url不能为空");
//		return;
//	}
	query["url"] = url;
	
	var permission = $("#add_permision_type").val();
	query["permission"] = permission;
	
	var objButton=$(this);
	$.ajax({
		type : 'POST',
		contentType : 'application/json;charset=UTF-8',
		dataType : 'json',
		data : JSON.stringify(query),
		cache : false,
		url : base + '/operation/save',
		beforeSend:function(){//触发ajax请求开始时执行
			 objButton.attr('disabled',true);//改变提交按钮上的文字并将按钮设置为不可点击
			},
		success : function(json) {
			hidden();
			if (json.statusCode == 200) {
				window.location.href = base + "/operation/index";
			} else {
				$.Zebra_Dialog("添加失败!", {
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



