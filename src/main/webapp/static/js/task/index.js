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
    	$("#delete").click(deleteContent);// 删除
    	$("#batch_retrycdn").click(batchRetryCdn);
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
                { "data": "title" },     
                { "data": "message"},
                { "data": "publish_time" },
                { "data": "ruzhan_time" },
                { "data": "updated_at" },
                { "data": null },
                { "data":null}
            ],
            "bFilter" : false,// 搜索栏
            "oLanguage": language,
            "rowCallback": function( row, data, index ) {
            	
            	$('td:eq(2)', row).html("<span title = '"+data.message+"'>"+sub(data.message,60)+"</span>");
            	
            	switch(data.status)
				{
					case "进行中":
						$('td:eq(0)', row).html("<input name='chk_list'   id='"+data.id+"' type='checkbox' value='"+data.id+"'/>")
						$('td:eq(6)', row).html("<span class='label label-info'>进行中</span>");
						if(!retryFlag){
							$('td:eq(7)', row).html("<a href='javascript:void(0);' style='display:none;' name='retry-content' data-content-id="+data.id+" data-toggle='tooltip' data-placement='left' title='重试'>"+
							"<i class='glyphicon glyphicon-refresh'></i></a>");
						}else{
							$('td:eq(7)', row).html("<a href='javascript:void(0);' style='display:inline;' name='retry-content' data-content-id="+data.id+" data-toggle='tooltip' data-placement='left' title='重试'>"+
							"<i class='glyphicon glyphicon-refresh'></i></a>");
						}
					  break;
					case "成功":
						$('td:eq(0)', row).html("<input name='nochk_list' disabled id='"+data.id+"' type='checkbox' value='"+data.id+"'/>")
						$('td:eq(6)', row).html("<span class='label label-success'>成功</span>");
						$('td:eq(7)', row).html("");
					  break;
					default:
						$('td:eq(0)', row).html("<input name='chk_list' id='"+data.id+"' type='checkbox' value='"+data.id+"'/>")
						$('td:eq(6)', row).html("<span class='label label-danger'>失败</span>");
						if(!retryFlag){
							$('td:eq(7)', row).html("<a href='javascript:void(0);' style='display:none;' name='retry-content' data-content-id="+data.id+" data-toggle='tooltip' data-placement='left' title='重试'>"+
							"<i class='glyphicon glyphicon-refresh'></i></a>");
						}else{
							$('td:eq(7)', row).html("<a href='javascript:void(0);' style='display:inline;' name='retry-content' data-content-id="+data.id+" data-toggle='tooltip' data-placement='left' title='重试'>"+
							"<i class='glyphicon glyphicon-refresh'></i></a>");
						}
				}
                
                $('[data-toggle="tooltip"]',row).tooltip();
                $("a[name='retry-content']",row).click(retryContent);
             },
             "fnServerParams": function ( data ) {
                 data.push(
                         { "name": "title", "value": $("#title").val() },
                         { "name": "publishTimeBegin", "value": $("#publishTimeBegin").val() },
                         { "name": "publishTimeEnd", "value": $("#publishTimeEnd").val() },
                         { "name": "insertedBegin", "value": $("#insertedBegin").val() },
                         { "name": "insertedEnd", "value": $("#insertedEnd").val() },
                         { "name": "status", "value": $("#status").val() }
                         
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
	         "sAjaxSource": base+"/tasks/ajax",
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
     * 重试
     */
    function retryContent(){
    	var obj=$(this);
		var content_id=obj.data("content-id");
		loading();
		$.ajax({
            "type" : 'get',
            "url" : base+"/tasks/reCdnStore/"+content_id,
            "dataType" : "json",
            "success" : function(data) {
            	removeLoading('loading');
            	information(data.message);
            	search();
            },
            "error":function(){
            	removeLoading('loading');
            	error();
            	search();
            }
        });
    }
    /**
     * 同步事件
     */
    function loading(){
    	$('body').loading({
			loadingWidth:240,
			title:'请稍等!',
			name:'loading',
			discription:'正在重试CDN...',
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
    			                url: base+"/tasks/delete?ids="+contentIds,
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
    

    
    
    
    /**
     * 批量重试CDN
     */
    function batchRetryCdn(){
    	
    	var contentIds = getChecks();
    	if (contentIds<=0) { 
    		warning("至少选择一条内容！");
    		 return false;
    	 }else{
    		 $.Zebra_Dialog("确定要重试CDN吗？", {
    			    'type':     'warning',
    			    'title':    '提示',
    			    'buttons':  ['取消', '确定'],
    			    'onClose':  function(caption) {
    			    	var option=(caption != '' ? '"' + caption + '"' : 'nothing');
    			        if("\"确定\""==option){
    			        	loading();
    			            console.log(contentIds)
    			            $.ajax({
    			        		type: "get",
    			                url: base+"/tasks/batchRetryCdn?ids="+contentIds,
    			                success: function(data){
    			                	removeLoading('loading');
    			                	unACheck();
    			                	search();
    			                },
    			                error: function(){
    			                	removeLoading('loading');
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
    
    
   
});
