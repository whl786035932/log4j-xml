$(function(){
	var base = $("#base").attr("href");
    /** 初始化所有组件 */
    initComponents();

    /** 监听所有event事件 */
    addEventListener();
    
    /**
     * 组装时间控件
     */
    function initComponents(){
    	initTree();
    }

    /**
     * 组装event监听
     */
    function addEventListener(){
    	$("#saveMenu").click(saveMenu);
    	$("#operationMenu").click(operationMenu);
    	
    }

   function initTree(){
	   if(nodes == null || nodes.length <= 0 ){
			$(".zTreeDemoBackground").append("<div class='left' id='createNodeDiv'>"
				+"<button type='button' class='btn btn-primary' id='createRootNode' data-toggle='modal' data-target='#addMenu'><i class='fa fa-plus-square-o'></i> 创建根目录</button>"
				+"</div>");
		}else{
			initMenuTree("menuTree");
		}
    }
   
   function initMenuTree(menuId){
//	   console.log(nodes);
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
		$.fn.zTree.init($("#" + menuId), setting, nodes);
		var tree = $.fn.zTree.getZTreeObj("menuTree");
		var root = tree.getNodeByParam("pId", null, null);
		tree.selectNode(root);
		writeForm(root);
		tree.expandAll(true);
   }
   
   function addHoverDom(treeId, treeNode) {
		var sObj = $("#" + treeNode.tId + "_span");
		// 如果之前有按钮，这直接返回
		if ($("#startBtn_" + treeNode.tId).length > 0
				|| $("#addBtn_" + treeNode.tId).length > 0
				|| $("#removeBtn_" + treeNode.tId).length > 0) {
			return;
		}
		var addStr = "";
			addStr += "<span class='button add' id='addBtn_" + treeNode.tId
					+ "' title='新增' onfocus='this.blur();'></span>";

		if (treeNode.isParent ==false) {
			addStr += "<span class='button remove' id='removeBtn_" + treeNode.tId
					+ "' title='删除' onfocus='this.blur();'></span>";
		}
		sObj.after(addStr);

		$("#addBtn_" + treeNode.tId).bind("click", function() {
			toSaveNode(treeId, treeNode);
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
   
	function setFontCss(treeId, treeNode) {
//		return treeNode.status == 0 ? {color:"#b7a5a5"} : {};
	};
	
	/**
	 * 单击节点
	 */
	function zTreeOnClick(event, treeId, treeNode) {
		writeForm(treeNode);
	}
	
	function beforeDrag(treeId, treeNodes){
	     return true;	
	}
	
	/**
	 * 拖拽前验证
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
		var oldPid=treeNodes[0].pId;
        var  targetPid=targetNode.pId;
        if(oldPid != targetPid){
        	warning("只能在同级移动");
	        return false;
        }
        
        if(moveType == "inner"){
        	warning("只能在同级移动");
	        return false;
        }
        
	}
	
	/**
	 * 拖拽
	 */
	function onDrop(event, treeId, treeNodes, targetNode, moveType, isCopy){
		 var pId = targetNode.pId;
		 var tree = $.fn.zTree.getZTreeObj("menuTree");
		 var parentNode = tree.getNodeByParam("id",pId,null);
		 var changeNode = new Array();
		 var children = parentNode.children;
		 for(i  in children){
			 changeNode.push(children[i].id);
		 }
		 $.ajax({
				type : 'PUT',
				contentType : 'application/json;charset=UTF-8',
				dataType : 'json',
				cache : false,
				url : base + "/menus/order/"+pId,
				data:JSON.stringify(changeNode),
				success : function(json) {
					if (json.statusCode == 200) {
						window.location.href = base + "/menus"
					} else {
						error();
					}
				},
				error:function(){
					error();
				}
			});
		 
		 return true;
	}
	
	/**
	 * 添加菜单
	 */
	function saveMenu(){
		if($('#save_form').parsley().validate()){
			var data = {
					name:$("#name").val(),
					url:$("#url").val(),
					sequence:0,
					parent:$("#parent").val(),
					menuClass:$("#menuClass").val()
			};
			$.ajax({
				type : 'POST',
				dataType : 'json',
				data : data,
				url : base + '/menus',
				success : function(json) {
					if (json.statusCode == !200) {
						warning("添加失败");
					} else {
						location.href=base+"/menus";
						$("#addMenu").modal("hide");
					}
				},
				error:function(){
					error();
				}
			});
		}
	}
	
	function operationMenu(){
		if($('#operation_form').parsley().validate()){
			var id = $("#menu_id").val();
			if(id == ""){
				warning("id主键不能为空");
				return;
			}
			var data = {
					name:$("#menu_name").val(),
					url:$("#menu_url").val(),
					menuClass:$("#menu_class").val()
			};
			$.ajax({
				type : 'POST',
				dataType : 'json',
				data : data,
				url : base + '/menus/update/'+id,
				success : function(json) {
					if (json.statusCode == !200) {
						warning("修改失败");
					} else {
						location.href=base+"/menus";
					}
				},
				error:function(){
					error();
				}
			});
			
		}
	}
	
	/**
	 * 弹出添加窗口
	 */
	function toSaveNode(treeId, treeNode){
		$("#save_form").parsley().reset()
		$("#save_form")[0].reset();
		$("#parent").val(treeNode.id);
		$("#addMenu").modal("show");
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
	
	/**
	 * 删除菜单
	 */
	function removeNode(treeId, treeNode) {
		var id = treeNode.id;
		// 分类名
		var objButton=$(this);
		$.ajax({
			type : 'PUT',
			contentType : 'application/json;charset=UTF-8',
			dataType : 'json',
			cache : false,
			url : base + "/menus/delete/"+id,
			beforeSend:function(){//触发ajax请求开始时执行
				 objButton.attr('disabled',true);//改变提交按钮上的文字并将按钮设置为不可点击
			},
			success : function(json) {
				if (json.statusCode == 200) {
					window.location.href = base + "/menus"
				} else {
					error();
				}
			},
			complete:function(){//ajax请求完成时执行
				objButton.attr('disabled',false);//改变提交按钮上的文字并将按钮设置为可以点击
			}
		});
	}
	
	function writeForm(treeNode){
		$("#operation_form")[0].reset();
		$("#menu_id").val(treeNode.id);
		$("#menu_name").val(treeNode.name);
		$("#menu_url").val(treeNode.url);
		$("#menu_class").val(treeNode.menuClass);
	}
	
});
