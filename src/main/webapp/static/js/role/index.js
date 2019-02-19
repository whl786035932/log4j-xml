$(function(){
	var  table;
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
    	initDataTable();
    }

    /**
     * 组装event监听
     */
    function addEventListener()
    {
    	$("#chk_all").click(checkAll);
    	$("#search").click(search);
    	$("#delete").click(deleteRoles);// 删除
    	
      	$("#add_role").click(addRole);
    	$("#add_sure").click(addSure); //添加用户的确定键
    	$("#update_sure").click(updateSure);
    	$("#update_permisson_submit").click(updatePermissionSubmit);
    }
    	
    /**
     * 搜索
     */
    function search(){
    	table.draw();
    }
   
    function initDataTable(){
    	table=$('#table').DataTable( {
    		"bProcessing": true,//加载中开启
    		"bServerSide": true,
    		"bSort": false,
    		"bDeferRender": true,//是否启用延迟加载：当你使用AJAX数据源时，可以提升速度。
    		"aLengthMenu": [20, 25,30,100,200,500],
            "columns": [
            	{ "data":null},
                { "data": "name"},
                { "data": "description"},
                { "data": "inserted_at" },
                { "data": "updated_at" },
                { "data":null}
               
            ],
            "bFilter" : false,// 搜索栏
            "oLanguage": language,
            "rowCallback": function( row, data, index ) {
            	
        	
				$('td:eq(0)', row).html("<input name='chk_list'   id='"+data.id+"' type='checkbox' value='"+data.id+"'/>");
				var editHTML = "<a style='margin-left:10px;display:none;' href='javascript:void(0);' name='edit-role'   data-role-id="+data.id+"            data-toggle='tooltip' data-placement='left' title='编辑'>"+
				"<i class='glyphicon glyphicon-edit'></i></a>";
				var updatePermissionHtml = "<a style='margin-left:10px;display:none;' href='javascript:void(0);' name='edit-permission'   data-role-id="+data.id+"            data-toggle='tooltip' data-placement='left' title='分配权限'>"+
				"<i class='glyphicon glyphicon-tower'></i></a>";
				
				if(roleEditFlag){
					editHTML = "<a style='margin-left:10px;' href='javascript:void(0);' name='edit-role'   data-role-id="+data.id+"            data-toggle='tooltip' data-placement='left' title='编辑'>"+
					"<i class='glyphicon glyphicon-edit'></i></a>";
				}
				if(updatePermissionFlag){
					updatePermissionHtml = "<a style='margin-left:10px;' href='javascript:void(0);' name='edit-permission'   data-role-id="+data.id+"            data-toggle='tooltip' data-placement='left' title='分配权限'>"+
					"<i class='glyphicon glyphicon-tower'></i></a>";
				}
//				$('td:eq(5)', row).html("<a style='margin-left:10px;' href='javascript:void(0);' name='edit-role'   data-role-id="+data.id+"            data-toggle='tooltip' data-placement='left' title='编辑'>"+
//				"<i class='glyphicon glyphicon-edit'></i></a>"+
//				"<a style='margin-left:10px;' href='javascript:void(0);' name='edit-permission'   data-role-id="+data.id+"            data-toggle='tooltip' data-placement='left' title='分配权限'>"+
//				"<i class='glyphicon glyphicon-tower'></i></a>"
//				);
				
				$('td:eq(5)', row).html(editHTML+updatePermissionHtml);
                $('[data-toggle="tooltip"]',row).tooltip();
                $('[data-toggle="tooltip"]',row).tooltip();
                $("a[name='edit-role']",row).click(updateRole);
                $("a[name='edit-permission']",row).click(updatePermission);
             },
          
 			
             "fnServerParams": function ( data ) {
                 data.push(
                         
                 );
	         },
	         "sAjaxSource": base+"/role/ajax",
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
    
    
    
    function addRole(){
    	$("#id").val("");
    	$("#addModel").modal("show");
    	$(".requireds").text("");// 提示置空
    	$("#myModalLabel").html("添加角色");
    	$('#add_form')[0].reset();
    	
    	//请求所有的角色
    }
    
    function updateRole(){
    	var roleId= $(this).data("role-id");
    	
    	
    	$.ajax({
            "type" : 'get',
            "url" : base+"/role/info?id="+roleId,
            "dataType" : "json",
            "success" : function(data) {
            	$("#updateModel").modal("show");
            	$(".requireds").text("");// 提示置空
            	$("#updateMyModalLabel").html("修改角色");
            	$('#update_form')[0].reset();
            	$("#updateId").val(data.id); 
            	$("#modal-update-name").val(data.name);
            	$("#modal-update-description").val(data.description);
            	
            	
            },
            "error":function(){
            	error();
            }
        });
    	
    	
    	
    }
    
    
    
    
    
    
    function addSure(){
    	var name = $("input[name='name']").val();
    	if(name == null || name == ""){
    		$("#username_required").text("名称不能为空");
    		return ;
    	}
    	if($('#add_form').parsley().validate()){
    		var url = base+"/role/add";
        	$.ajax({ 
    			"url":url,
    			"data":$('#add_form').serialize(),
    			"type":"POST",
    			"success":function(resp){
    				$("#addModel").modal("hide");
    				if(resp.statusCode == 200)
    					table.draw();
    				else{
    					warning(resp.message);
    				}
    				$("#id").val("");
    			},
    			"error":function(){
    				$("#id").val("");
    				error();
    			}
    		});
        	$("#addModel").modal("hide");
		}
    }
    
    
    
    function updateSure(){
    	var name = $("#modal-update-name").val();
    	if(name == null || name == ""){
    		$("#update_name_required").text("名称不能为空");
    		return ;
    	}
    	
    	
    	
    	if($('#update_form').parsley().validate()){
  			url = base+"/role/update"
    		var ddd=$('#update_form').serialize();
  			console.log(ddd);
        	$.ajax({ 
    			"url":url,
    			"data":$('#update_form').serialize(),
    			"type":"POST",
    			"success":function(resp){
    				$("#addModel").modal("hide");
    				if(resp.statusCode == 200)
    					table.draw();
    				else{
    					warning(resp.message);
    				}
    				$("#updateId").val("");
    			},
    			"error":function(){
    				$("#updateId").val("");
    				error();
    			}
    		});
        	$("#updateModel").modal("hide");
		}
    }
    
    
    
    /**
     * 批量删除
     */
    function deleteRoles(){
    	
    	var userIds = getChecks();
    	if (userIds<=0) { 
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
    			            $.ajax({
    			        		type: "get",
    			                url: base+"/role/delete?ids="+userIds,
    			                success: function(data){
    			                	unACheck();
    			                	search();
    			                },
    			                error: function(){
    			                	warning("删除失败");
    			                }
    			        	});
    			            
    			        }else{
    			        	return;
    			        }
    			    }
    			});
    	 }
    	 
    }
    
    //分配权限
    function updatePermission(){
    	var obj = $(this);
    	var roleId = obj.data("role-id");
    	
    	$.ajax({
            "type" : 'get',
            "url" : base+"/role/permissions/"+roleId,
            "dataType" : "json",
            "success" : function(data) {
            	$("#role_permissionId").val(roleId);
            	var operationNodes = null;
            	var menuNodes = null;
            	if(data.operationTreeJson!=null&&data.operationTreeJson!=''&&data.operationTreeJson!=""){
            		operationNodes = JSON.parse(data.operationTreeJson);
            	}
            	if(data.menuTreeJson!=null&&data.menuTreeJson!=''&&data.menuTreeJson!=""){
            		menuNodes = JSON.parse(data.menuTreeJson);
            	}
            	initOperationTree("operationTree",operationNodes);
            	initMenuTree("menuTree",menuNodes);
            	$("#add_permission").modal('show');
            },
            "error":function(){
            	error();
            }
        });
    	
    }
    
    
    function initOperationTree(treeId,nodes){
    		var setting = {
    			view : {
    				fontCss : setFontCss
    			},
    			async:{
    				enable: true
    			},
    			callback: {
    				onCheck: zTreeOnAsyncSuccess
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
					key : {
						checked : "checked"
					},
				}
			};
    		$.fn.zTree.init($("#"+treeId), setting, nodes);
    		var tree = $.fn.zTree.getZTreeObj(treeId);
    		var root = tree.getNodeByParam("pId", null, null);
    		tree.selectNode(root);
    		tree.expandAll(true);
    }
    function zTreeOnAsyncSuccess(event, treeId, treeNode, msg) {
    	var treeObj = $.fn.zTree.getZTreeObj(treeId);
    	var parentNode = treeNode.getParentNode();
    	treeObj.checkNode(parentNode,true,true);
    	if(parentNode!=null){
    		var zuNode = parentNode.getParentNode();
    		if(zuNode!=undefined){
    			treeObj.checkNode(zuNode,true,true);
    		}
    	}
    	
    };
    
    function setFontCss(treeId, treeNode) {
    	return treeNode.status == 0 ? {color:"#b7a5a5"} : {};
    };
    function initMenuTree(treeId,nodes){
    	var setting = {
    			view : {
    				fontCss : setFontCss
    			},
    			async:{
    				enable: true
    			},
    			callback: {
    				onCheck: zTreeOnAsyncSuccess
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
    		$.fn.zTree.init($("#" + treeId), setting, nodes);
    		var tree = $.fn.zTree.getZTreeObj(treeId);
    		var root = tree.getNodeByParam("pId", null, null);
    		tree.selectNode(root);
    		tree.expandAll(true);
    }
    
    
    
    
    function updatePermissionSubmit(){
    	//获取操作勾选的
    	var operationTreeObj = $.fn.zTree.getZTreeObj("operationTree");
    	var operation_checkedNodes = operationTreeObj.getCheckedNodes();
    	var operationArr = [];
    	for(var i=0;i<operation_checkedNodes.length;i++){
    		operationArr.push(operation_checkedNodes[i].id);
    	}
    	//获取菜单勾选的
    	var menuTreeObj = $.fn.zTree.getZTreeObj("menuTree");
    	var menu_checkedNodes = menuTreeObj.getCheckedNodes();
    	var menuArr = [];
    	for(var i=0;i<menu_checkedNodes.length;i++){
    		menuArr.push(menu_checkedNodes[i].id);
    	}
    	
    	if(operationArr.length==0 && menuArr.length==0){
    		return ;
    	}else{
    		
    		var roles = [];
    		roles.push($("#role_permissionId").val());
    		var data = new Object();
    		data["operations"] = operationArr;
    		data["menus"] = menuArr;
    		data["roles"] = roles;
    		$.ajax({
    			type : 'POST',
    			contentType : 'application/json;charset=UTF-8',
    			dataType : 'json',
    			data : JSON.stringify(data),
    			cache : false,
    			url : base + '/role/updatePermisssions',
    			success : function(json) {
    				if (json.statusCode == !200) {
    					warning("操作失败");
    				} else {
    					window.location.href = base + "/role/list"
    				}
    			}
    		});
    	}
    	
    	
    }
    
});
