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
    function initComponents(){
    	initDataTable();
    }

    /**
     * 组装event监听
     */
    function addEventListener(){
    	$("#chk_all").click(checkAll);
    	$("#search").click(search);
    	$("#delete").click(deleteUsers);// 删除
    	
      	$("#add_user").click(addUser);
    	$("#add_sure").click(addSure); //添加用户的确定键
    	$("#update_sure").click(updateSure);
    	
      	$("#update_password_sure").click(updatePasswordSure);
      	
    }
    
    function classificationInput(){
	    	var setting = {
	    			view : {
	    				fontCss : setFontCss
	    			},
					check: {
						enable: true,
	    				chkboxType: {"Y":"p", "N":"ps"}
					},
					data: {
						simpleData: {
							enable: true
						},
						keep : {
							parent : true
						},
						key : {},
					},
					callback: {
						onCheck: onCheck
					}
				};
		    	$.ajax({
		    		type: "GET",
		            url: base+"/classifications",
		            success: function(data){
		            	var zNodes = JSON.parse(data.data.tree);
		            	$.fn.zTree.init($("#classifyTree"), setting, zNodes);
		            	var treeObj = $.fn.zTree.getZTreeObj("classifyTree");
		            	treeObj.expandAll(true);
		            	
		            	var nodes = treeObj.getNodes();
		            	disableCheckBox(nodes,"classifyTree")
		            	
		            	var input = $("#classification-input");           
		                var offset = input.offset();
		                $('.showDiv')
		                    .css('border', '1px solid #cccccc')
		                    .css('z-index',99)
		                	.css("background-color","#FFFFFF")
		    				.css('width','100%')	
		    				.css("border-top","0px")
		                    .slideDown();
		            },
		            error: function(){
		            	error();
		            }
		    	});
    }
    
    function setFontCss(treeId, treeNode) {
    	return treeNode.status == 0 ? {color:"#b7a5a5"} : {};
    };
    
    /**
     * 树点击事件
     */
    var nodes = new Array();
    var classificationIds = new Array();
    function onCheck(e, treeId, treeNode) {
    	var parent1 = treeNode.getParentNode();
    	var parent2 = parent1.getParentNode();
    	var name = "";
    	if(parent2 != null){
    		name = parent2.name+">";
    	}
    	if(parent1 != null){
    		name += parent1.name+">"+treeNode.name;
    	}
    	if(treeNode.checked){
    		nodes.push(name);
    		classificationIds.push(treeNode.id);
    		var value = JSON.stringify(unique(nodes));
        	$("#classification-input").val(value.replace(/\"/g,"").replace("[","").replace("]",""));
	    }else{
	    	var index = nodes.indexOf(name);
	    	var indexClassification = classificationIds.indexOf(treeNode.id);
	    	if(-1 != index){
	    		nodes.splice(index,1);
	    		classificationIds.splice(indexClassification,1);
	    	}
	    	var value = JSON.stringify(unique(nodes));
	    	$("#classification-input").val(value.replace(/\"/g,"").replace("[","").replace("]",""));
	    }
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
                { "data": "username" },     
                { "data": "nickname"},
                { "data": "roleName"},
                { "data": "inserted_at" },
                { "data": "updated_at" },
                { "data": null },
                { "data":null}
            ],
            "bFilter" : false,// 搜索栏
            "oLanguage": language,
            "rowCallback": function( row, data, index ) {
            	
            	switch(data.status)
				{
				case 1:
					$('td:eq(0)', row).html("<input name='chk_list'   id='"+data.id+"' type='checkbox' value='"+data.id+"'/>")
					$('td:eq(6)', row).html("<span class='label label-info'>启用</span>");
					
					var  disalbeHTML = "<a href='javascript:void(0);' style='display:none;' name='disable-user' data-user-id="+data.id+" data-toggle='tooltip' data-placement='left' title='禁用'><i class='fa fa-lock'></i></a>";
					
					var  editHTML = "<a style='margin-left:10px;display:none;' href='javascript:void(0);' name='edit-user'     data-user-id="+data.id+"    data-toggle='tooltip' data-placement='left' title='编辑'>"+
					"<i class='glyphicon glyphicon-edit'></i></a>";
					
					var editPasswordHTML = "<a style='margin-left:10px;display:none;' href='javascript:void(0);' name='edit-password'   data-user-id="+data.id+"            data-toggle='tooltip' data-placement='left' title='修改密码'>"+
					"<i class='fa fa-wrench'></i></a>";
					
					if(userDisableFlag && data.type!=1){
						disalbeHTML = "<a href='javascript:void(0);' style='display:inline;' name='disable-user' data-user-id="+data.id+" data-toggle='tooltip' data-placement='left' title='禁用'><i class='fa fa-lock'></i></a>";
					}
					if(userEditFlag && data.type!=1){
						editHTML = "<a style='margin-left:10px;diplay:inline;' href='javascript:void(0);' name='edit-user'     data-user-id="+data.id+"    data-toggle='tooltip' data-placement='left' title='编辑'>"+
						"<i class='glyphicon glyphicon-edit'></i></a>";
					}
					if(userEditPasswordFlag){
						editPasswordHTML = "<a style='margin-left:10px;display:inline;' href='javascript:void(0);' name='edit-password'   data-user-id="+data.id+"            data-toggle='tooltip' data-placement='left' title='修改密码'>"+
						"<i class='fa fa-wrench'></i></a>";
					}
					
					$('td:eq(7)', row).html(disalbeHTML+editHTML+editPasswordHTML);
					
				  break;
				case 0:
					$('td:eq(0)', row).html("<input name='chk_list'  id='"+data.id+"' type='checkbox' value='"+data.id+"'/>")
					$('td:eq(6)', row).html("<span class='label label-success'>禁用</span>");
					
					var  disalbeHTML = "<a href='javascript:void(0);' name='enable-user' data-user-id="+data.id+" data-toggle='tooltip' data-placement='left' title='启用'><i class='fa fa-unlock'></i></a>";
					var  editHTML = "<a style='margin-left:10px;display:none;' href='javascript:void(0);' name='edit-user'     data-user-id="+data.id+"    data-toggle='tooltip' data-placement='left' title='编辑'>"+
					"<i class='glyphicon glyphicon-edit'></i></a>";
					
					var editPasswordHTML = "<a style='margin-left:10px;display:none;' href='javascript:void(0);' name='edit-password'   data-user-id="+data.id+"            data-toggle='tooltip' data-placement='left' title='修改密码'>"+
					"<i class='fa fa-wrench'></i></a>";
					
					if(userDisableFlag && data.type!=1){
						disalbeHTML = "<a href='javascript:void(0);' name='enable-user' data-user-id="+data.id+" data-toggle='tooltip' data-placement='left' title='启用'><i class='fa fa-unlock'></i></a>";
					}
					if(userEditFlag && data.type!=1){
						editHTML = "<a style='margin-left:10px;diplay:inline;' href='javascript:void(0);' name='edit-user'     data-user-id="+data.id+"    data-toggle='tooltip' data-placement='left' title='编辑'>"+
						"<i class='glyphicon glyphicon-edit'></i></a>";
					}
					if(userEditPasswordFlag){
						editPasswordHTML = "<a style='margin-left:10px;display:inline;' href='javascript:void(0);' name='edit-password'   data-user-id="+data.id+"            data-toggle='tooltip' data-placement='left' title='修改密码'>"+
						"<i class='fa fa-wrench'></i></a>";
					}
					
					$('td:eq(7)', row).html(disalbeHTML+editHTML+editPasswordHTML);
					
					break;
				default:
				}
                
                $('[data-toggle="tooltip"]',row).tooltip();
                $('[data-toggle="tooltip"]',row).tooltip();
                $("a[name='edit-user']",row).click(updateUser);
                $("a[name='edit-password']",row).click(updatePassword);
                $("a[name='disable-user']",row).click(disableUser);
                $("a[name='enable-user']",row).click(enableUser);
             },
             "fnServerParams": function ( data ) {
                 data.push(
                         { "name": "username", "value": $("#username").val() },
                         { "name": "nickname", "value": $("#nickname").val() },
                         { "name": "status", "value": $("#status").val() }
                         
                 );
	         },
	         "sAjaxSource": base+"/ajax",
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
    
    function addUser(){
    	$("#id").val("");
    	$("#addModel").modal("show");
    	$(".requireds").text("");// 提示置空
    	$("#myModalLabel").html("添加用户");
    	
    	nodes.splice(0,nodes.length);//清空数组
    	
    	$('#add_form')[0].reset();
    	//请求所有的角色
    	 $.ajax({
     		type: "get",
             url: base+"/role/allRoles",
             success: function(data){
            	 $("#role").empty();
            	var roles=data.roles;
            	for(var i=0;i<roles.length;i++){
            		var role = roles[i];
            		$("#role").append('<option value="'+role.id+'">'+role.name+'</option>');
            	}
            	destroyMultiSelect('#role');
            	
            	classificationInput();
             },
             error: function(){
             	warning("获取角色失败");
             }
     	});
    }
    
    var classificationAllIds = new Array();
    var classificationChildrenIds = new Array();
    function updateUser(){
    	var userId= $(this).data("user-id");
    	$.ajax({
            "type" : 'get',
            "url" : base+"/info?id="+userId,
            "dataType" : "json",
            "success" : function(data) {
            	$("#updateModel").modal("show");
            	$(".requireds").text("");// 提示置空
            	$("#updateMyModalLabel").html("修改用户");
            	clearArray();
            	$('#update_form')[0].reset();
            	$("#updateId").val(data.id); 
            	$("#modal-update-username").val(data.userName);
            	$("#modal-update-nickname").val(data.nickName);
            	var roles=data.roles;
            	var selectedRoleIds = data.selectedRoleS;
            	 $("#update_role").empty();
            	for(var i=0;i<roles.length;i++){
            		var role = roles[i];
            		var roleId = role.id;
            		var contain = containRoleId(roleId,selectedRoleIds);
            		if(contain){
            			$("#update_role").append('<option  selected value="'+role.id+'">'+role.name+'</option>');
            		}else{
            			$("#update_role").append('<option value="'+role.id+'">'+role.name+'</option>');
            			
            		}
            	}
            	destroyMultiSelect("#update_role");
            	
            	classificationChildrenIds = data.classificationIds;
            	
            	classificationAllIds = data.classificationAllIds;
            	
            	classificationInputUpdate();
            	
            },
            "error":function(){
            	error();
            }
        });
    	
    }
    
    function classificationInputUpdate(){
	    	var setting = {
	    			view : {
	    				fontCss : setFontCss
	    			},
					check: {
						enable: true,
	    				chkboxType: {"Y":"p", "N":"ps"}
					},
					data: {
						simpleData: {
							enable: true
						},
						keep : {
							parent : true
						},
						key : {},
					},
					callback: {
						onCheck: onCheckUpdate
					}
				};
		    	$.ajax({
		    		type: "GET",
		            url: base+"/classifications",
		            success: function(data){
		            	var zNodes = JSON.parse(data.data.tree);
		            	$.fn.zTree.init($("#classifyTreeUpdate"), setting, zNodes);
		            	var treeObj = $.fn.zTree.getZTreeObj("classifyTreeUpdate");
		            	treeObj.expandAll(true);
		            	
		            	showUpdateTreeDiv();
		            	
		            	var nodes = treeObj.getNodes();
		            	disableCheckBox(nodes,"classifyTreeUpdate");
		                
		              //设置tree选中
		                for(i in classificationAllIds){
		                	var node = treeObj.getNodeByParam("id", classificationAllIds[i], null);
		                	treeObj.checkNode(node);
		                }
		                
		              //设置ztree中文节点
		              initTreeTextValue(classificationChildrenIds);
		                
		            },
		            error: function(){
		            	error();
		            }
		    	});
		    	
    }
    
    function disableCheckBox(nodes,tree){
    	var treeObj = $.fn.zTree.getZTreeObj(tree);
    	for (var i=0, l=nodes.length; i < l; i++) {
    		var node = nodes[i];
    		if(node.isParent){
    			treeObj.setChkDisabled(nodes[i], true);
    			if(node.children.length > 0){
    				disableCheckBox(node.children,tree);
    			}
    		}
    	}
    }
    
    function showUpdateTreeDiv(){
	        $('#showDivUpdate')
	            .css('border', '1px solid #cccccc')
	            .css('z-index',99)
	        	.css("background-color","#FFFFFF")
	        	.css('width','100%')	
	        	.css("border-top","0px")
	            .slideDown();
    }
    
    function clearArray(){
    	nodesUpdate.splice(0,nodesUpdate.length);
    	classificationIdsUpdate.splice(0,classificationIdsUpdate.length);
    }
    
    var nodesUpdate = new Array();
    var classificationIdsUpdate = new Array();
    function onCheckUpdate(e, treeId, treeNode) {
    	var parent1 = treeNode.getParentNode();
    	var parent2 = parent1.getParentNode();
    	var name = "";
    	if(parent2 != null){
    		name = parent2.name+">";
    	}
    	if(parent1 != null){
    		name += parent1.name+">"+treeNode.name;
    	}
    	if(treeNode.checked){
    		nodesUpdate.push(name);
    		classificationIdsUpdate.push(treeNode.id);
    		var value = JSON.stringify(unique(nodesUpdate));
        	$("#classification-input-update").val(value.replace(/\"/g,"").replace("[","").replace("]",""));
	    }else{
	    	var index = nodesUpdate.indexOf(name);
	    	var indexClassification = classificationIdsUpdate.indexOf(treeNode.id);
	    	if(-1 != index){
	    		nodesUpdate.splice(index,1);
	    		classificationIdsUpdate.splice(indexClassification,1);
	    	}
	    	var value = JSON.stringify(unique(nodesUpdate));
	    	$("#classification-input-update").val(value.replace(/\"/g,"").replace("[","").replace("]",""));
	    }
    }
    
    /**
     * 设置tree中文显示
     */
    function initTreeTextValue(classificationIds){
    	var treeObj = $.fn.zTree.getZTreeObj("classifyTreeUpdate");
    	for(var i in classificationIds){
    		var treeNode = treeObj.getNodeByParam("id", classificationIds[i], null);
    		var parent1 = treeNode.getParentNode();
        	var parent2 = parent1.getParentNode();
        	var name = "";
        	if(parent2 != null){
        		name = parent2.name+">";
        	}
        	if(parent1 != null){
        		name += parent1.name+">"+treeNode.name;
        	}
        	if(treeNode.checked){
        		nodesUpdate.push(name);
        		classificationIdsUpdate.push(treeNode.id);
        		var value = JSON.stringify(unique(nodesUpdate));
            	$("#classification-input-update").val(value.replace(/\"/g,"").replace("[","").replace("]",""));
    	    }else{
    	    	var index = nodesUpdate.indexOf(name);
    	    	var indexClassification = classificationIdsUpdate.indexOf(treeNode.id);
    	    	if(-1 != index){
    	    		nodesUpdate.splice(index,1);
    	    		classificationIdsUpdate.splice(indexClassification,1);
    	    	}
    	    	var value = JSON.stringify(unique(nodesUpdate));
    	    	$("#classification-input-update").val(value.replace(/\"/g,"").replace("[","").replace("]",""));
    	  }
    	}
    }
    
    /**
     * 销毁并且初始化
     */
    function destroyMultiSelect(select){
    	$(select).multiselect("destroy").multiselect({
            enableFiltering: true,
            enableClickableOptGroups: true,
            buttonWidth: '100%',
            maxHeight: 200,
            enableCaseInsensitiveFiltering: true
        });
    }
    
    function containRoleId(roleId, roles){
    	for(var i=0;i<roles.length;i++){
    		var roleId_inner=roles[i];
    		if(roleId_inner==roleId){
    			return true;
    		}
    	}
    	return false;
    }
    
    function updatePassword(){
    	var userId= $(this).data("user-id");
    	$.ajax({
            "type" : 'get',
            "url" : base+"/info?id="+userId,
            "dataType" : "json",
            "success" : function(data) {
            	$("#updatePasswordModel").modal("show");
            	$(".requireds").text("");// 提示置空
            	$("#updatePasswordModalLabel").html("修改密码");
            	$('#update_password_form')[0].reset();
            	$("#update_password_id").val(data.id);
            },
            "error":function(){
            	error();
            }
        });
    }
    
    function updatePasswordSure(){
    	
    	var password = $("#update_password").val();
    	if(password == null || password == ""){
    		$("#update_password_required").text("密码不能为空");
    		return ;
    	}
    	
    	var repassword = $("#update_repassword").val();
    	if(repassword != password){
    		$("#update_repassword_required").text("两次密码不一致");
    		return ;
    	}
    	
    	if($('#update_password_form').parsley().validate()){
    		var url = base+"/updatePassword";
        	$.ajax({ 
    			"url":url,
    			"data":$('#update_password_form').serialize(),
    			"type":"POST",
    			"success":function(resp){
    				$("#updatePasswordModel").modal("hide");
    				if(resp.statusCode == 200)
    					table.draw();
    				else{
    					warning(resp.message);
    				}
    				$("#update_password_id").val("");
    			},
    			"error":function(){
    				$("#update_password_id").val("");
    				error();
    			}
    		});
        	$("#updatePasswordModel").modal("hide");
		}
    }
    
    function addSure(){
    	var username = $("input[name='username']").val();
    	if(username == null || username == ""){
    		$("#username_required").text("用户名不能为空");
    		return ;
    	}
    	var password = $("input[name='password']").val();
    	if(password == null || password == ""){
    		$("#password_required").text("密码不能为空");
    		return ;
    	}
    	
    	var repassword = $("input[name='repassword']").val();
    	if(repassword != password){
    		$("#repassword_required").text("两次密码不一致");
    		return ;
    	}
    	
    	var phone=$("input[name='phone']").val();
    	if(phone!=undefined && phone!=""){
    		var reg = /^1[34578]\d{9}$/;
    		var phone_result = reg.test(phone);
    		if(phone_result==false){
    			$("#phone_required").text("请输入正确的电话");
    			return ;
    		}
    	}
    	
    	var email=$("input[name='email']").val();
    	if(email!=undefined && email!="" ){
    		var reg=/^([0-9A-Za-z\-_\.]+)@([0-9a-z]+\.[a-z]{2,3}(\.[a-z]{2})?)$/g;
    		var email_result=reg.test(email);
    		if(email_result==false){
    			$("#email_required").text("请输入正确的邮箱地址");
    			return ;
    		}
    	}
    	
    	if($('#add_form').parsley().validate()){
    		var url = base+"/add";
    		$("#classificationStr").val(getTreeChecked("classifyTree"));
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
    	var username = $("#modal-update-username").val();
    	if(username == null || username == ""){
    		$("#update_username_required").text("用户名不能为空");
    		return ;
    	}
    	
    	if($('#update_form').parsley().validate()){
  			url = base+"/update"
    		var ddd=$('#update_form').serialize();
  			$("#classificationStrUpdate").val(getTreeChecked("classifyTreeUpdate"));
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
     * 获取tree勾选的节点
     */
    function getTreeChecked(tree){
    	var checkedIds = new Array();
    	var treeObj = $.fn.zTree.getZTreeObj(tree);
    	var nodes = treeObj.getCheckedNodes(true);
    	for(i in nodes){
    		checkedIds.push(nodes[i].id);
    	}
    	return checkedIds;
    }
    
    function disableUser(){
    	var obj=$(this);
		var user_id=obj.data("user-id");
		  /**
	     * 重试
	     */
		$.ajax({
            "type" : 'get',
            "url" : base+"/updateUserStatus/"+user_id+"/0",
            "dataType" : "json",
            "success" : function(data) {
            	search();
            },
            "error":function(){
            	search();
            }
        });
    }
    
    function enableUser(){
    	var obj=$(this);
		var user_id=obj.data("user-id");
		$.ajax({
            "type" : 'get',
            "url" : base+"/updateUserStatus/"+user_id+"/1",
            "dataType" : "json",
            "success" : function(data) {
            	search();
            },
            "error":function(){
            	error();
            	search();
            }
        });
    }
    
    /**
     * 批量删除
     */
    function deleteUsers(){
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
    			                url: base+"/delete?ids="+userIds,
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
   
});
