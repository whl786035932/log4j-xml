$(function() {
	var table;
	var base = $("#base").attr("href");
	/** 初始化所有组件 */
	initComponents();

	/** 监听所有event事件 */
	addEventListener();

	/**
	 * 组装时间控件
	 */
	function initComponents() {
		initDataTable();
	}

	/**
	 * 组装event监听
	 */
	function addEventListener() {
		$("#chk_all").click(checkAll);
		$("#search").click(search);

		$("#add_user").click(addUser);
		$("#add_sure").click(addSure); // 添加用户的确定键
	}

	/**
	 * 树点击事件
	 */
	var nodes = new Array();
	var classificationIds = new Array();
	function onCheck(e, treeId, treeNode) {
		var parent1 = treeNode.getParentNode();
		var parent2 = parent1.getParentNode();
		var name = "";
		if (parent2 != null) {
			name = parent2.name + ">";
		}
		if (parent1 != null) {
			name += parent1.name + ">" + treeNode.name;
		}
		if (treeNode.checked) {
			nodes.push(name);
			classificationIds.push(treeNode.id);
			var value = JSON.stringify(unique(nodes));
			$("#classification-input").val(
					value.replace(/\"/g, "").replace("[", "").replace("]", ""));
		} else {
			var index = nodes.indexOf(name);
			var indexClassification = classificationIds.indexOf(treeNode.id);
			if (-1 != index) {
				nodes.splice(index, 1);
				classificationIds.splice(indexClassification, 1);
			}
			var value = JSON.stringify(unique(nodes));
			$("#classification-input").val(
					value.replace(/\"/g, "").replace("[", "").replace("]", ""));
		}
	}

	/**
	 * 搜索
	 */
	function search() {
		table.draw();
	}
	
	function initDataTable() {
		table = $('#table').DataTable(
						{
							"bProcessing" : true,// 加载中开启
							"bServerSide" : true,
							"bSort" : false,
							"bDeferRender" : true,// 是否启用延迟加载：当你使用AJAX数据源时，可以提升速度。
							"aLengthMenu" : [ 20, 25, 30, 100, 200, 500 ],
							"columns" : [
								{"data" : "mainVersion"}, 
								{"data" : "childVersion"}, 
								{"data" : "entry"}, 
								{"data" : "md5"}, 
								{"data" : "size"}, 
								{"data" : "archive"}, 
								{"data" : "isForce"},
								{"data" : "description"},
								{"data" : "status"}, 
								{"data" : null}
							],
							"bFilter" : false,// 搜索栏
							"oLanguage" : language,
							"rowCallback" : function(row, data, index) {
								switch (data.isForce) {
									case true:
										$('td:eq(6)', row).html("<span class='label label-success'>是</span>");
										break;
									case false:
										$('td:eq(6)', row).html("<span class='label label-info'>否</span>");
										break;
									default:
										$('td:eq(6)', row).html("<span class='label label-danger'>未知</span>");
										break;
								}
								
								switch (data.status) {
									case 1:
										$('td:eq(8)', row).html("<span class='label label-success'>启用</span>");
										$('td:eq(9)', row).html("");
										break;
									case 0:
										$('td:eq(8)', row).html("<span class='label label-info'>禁用</span>");
										var disalbeHTML = "<a href='javascript:void(0);' name='enable-apk' data-apk-id="
												+ data.id
												+ " data-toggle='tooltip' data-placement='left' title='启用'><i class='fa fa-unlock'></i></a>";
										
										var deleteHTML = "<a href='javascript:void(0);' name='delete-apk' data-apk-id="
											+ data.id
											+ " data-toggle='tooltip' data-placement='left' title='删除'><i class='fa fa-close'></i></a>";
										$('td:eq(9)', row).html(disalbeHTML+deleteHTML);
										break;
									default:
										$('td:eq(8)', row).html("<span class='label label-danger'>未知</span>");
										$('td:eq(9)', row).html("");
										break;
								}
								$('[data-toggle="tooltip"]',row).tooltip();
								$("a[name='disable-apk']", row).click(disableApk);
								$("a[name='enable-apk']", row).click(enableApk);
								$("a[name='delete-apk']", row).click(deleteApk);
							},
							"fnServerParams" : function(data) {
								data.push({
									"name" : "type",
									"value" : $("#modal-type").val()
								});
							},
							"sAjaxSource" : base + "/apkmanager/ajax",
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
						});
	}

	/**
	 * 取消全选
	 * 
	 */
	function unACheck() {
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
	 * 
	 * @returns {Array}
	 */
	function getChecks() {
		var contentIds = new Array();
		$("input[name='chk_list']:checked").each(function() {
			contentIds.push($(this).val());
		})
		return contentIds;
	}

	function addUser() {
		$("#add_form")[0].reset();
		$("#add_form").parsley().reset()
		$("#id").val("");
		$("#addModel").modal("show");
		$(".requireds").text("");// 提示置空
		$("#myModalLabel").html("添加版本");
		nodes.splice(0, nodes.length);// 清空数组
		$('#add_form')[0].reset();
	}

	function disableCheckBox(nodes, tree) {
		var treeObj = $.fn.zTree.getZTreeObj(tree);
		for (var i = 0, l = nodes.length; i < l; i++) {
			var node = nodes[i];
			if (node.isParent) {
				treeObj.setChkDisabled(nodes[i], true);
				if (node.children.length > 0) {
					disableCheckBox(node.children, tree);
				}
			}
		}
	}

function addSure() {
	if($('#add_form').parsley().validate()){
		var typeFtl = "";
		if (type == 0) {
			typeFtl = "test";
		} else if (type == 1) {
			typeFtl = "release";
		}
		$("#add_form").ajaxSubmit({
				url : base + '/apkmanager/apkpackage',
				type : 'POST',
				cache : false,
				dataType : 'json',
				data:{"force":$("#force").prop("checked")},
				processData : false,
				success : function(result) {
						hideAdd();// 隐藏
						if (result.statusCode == 200) {
							information("上传成功");
							search() ;
						} else if (result.statusCode == 409) {
							warning(result.message);
						} else {
							error();
						}
				},
				error:function(){
					error();
				}
		});
	}
}

	/**
	 * 隐藏上传
	 */
	function hideAdd() {
		$("input[name='apk_file']").val("");
		$("#poster_required").text("");
		$('#addModel').modal('hide');
	}

	function disableApk() {
		var obj = $(this);
		var apk_id = obj.data("apk-id");

		var data = new Object();

		data["apkId"] = apk_id;
		data["status"] = 0;
		data["type"] = type;
		$.ajax({
			"type" : 'post',
			"url" : base + "/apkmanager/updateApkStatus",
			"dataType" : "json",
			"data" : {
				data : JSON.stringify(data)
			},
			"success" : function(resp) {
				fnCallback(resp);
			}
		});
	}

	function enableApk() {
		var obj = $(this);
		var apk_id = obj.data("apk-id");
		var data = new Object();
		
		data["apkId"] = apk_id;
		data["status"] = 1;
		data["type"] = type;
		$.Zebra_Dialog("启用该版本，将会禁用其他版本!", {
			type : 'information',
			title : '提示',
			buttons : ["取消", "确定"],
			onClose : function(caption) {
				var option = (caption != '' ? '"' + caption + '"'
						: 'nothing');
				if (option == "\"确定\"") {
					$.ajax({
						"type" : 'post',
						"url" : base + "/apkmanager/updateApkStatus",
						"dataType" : "json",
						"contentType" : "application/json; charset=utf-8",
						"data" : JSON.stringify(data),
						"success" : function(resp) {
							search();
						}
					});
				}
			}
		});
	}
	
	function deleteApk() {
		var obj = $(this);
		var apk_id = obj.data("apk-id");
		$.Zebra_Dialog("确定删除该版本吗？", {
			type : 'information',
			title : '提示',
			buttons : ["取消", "确定"],
			onClose : function(caption) {
				var option = (caption != '' ? '"' + caption + '"' : 'nothing');
				if (option == "\"确定\"") {
					$.ajax({
						"type" : 'get',
						"url" : base + "/apkmanager/delete/"+apk_id,
						"dataType" : "json",
						"contentType" : "application/json; charset=utf-8",
						"success" : function(resp) {
							if(resp.statusCode == 200){
								 information(resp.message);
								search();
							}else{
								warning(resp.message);
							}
						},
						error:function(){
							error();
						}
					});
				}
			}
		});
	}

});
