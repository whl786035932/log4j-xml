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
    	$("#delete").click(deletePermissions);// 删除
    	
      	$("#add_permission").click(addPermission);
    	$("#add_sure").click(addSure); //添加用户的确定键
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
                { "data": "typeName"},
                { "data": "realHandleName"}
                
            ],
            "bFilter" : false,// 搜索栏
            "oLanguage": language,
            "rowCallback": function( row, data, index ) {
            	$('td:eq(0)', row).html("<input name='chk_list'   id='"+data.id+"' type='checkbox' value='"+data.id+"'/>")
             },
          
 			
             "fnServerParams": function ( data ) {
                 data.push(
                         
                 );
	         },
	         "sAjaxSource": base+"/permission/ajax",
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
    
    
    
    function addPermission(){
    	$("#id").val("");
    	$("#addModel").modal("show");
    	$(".requireds").text("");// 提示置空
    	$("#myModalLabel").html("添加权限");
    	$('#add_form')[0].reset();
    	
    	//请求所有的角色
    }
    
    
    
    function addSure(){
    	if($('#add_form').parsley().validate()){
    		var url = base+"/permission/add";
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
    
    
    
    /**
     * 批量删除
     */
    function deletePermissions(){
    	
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
    			                url: base+"/permission/delete?ids="+userIds,
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
