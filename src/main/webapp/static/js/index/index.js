$(function(){
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
    	$("#search").click(search);
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
    		"bSort": true,
    		"bDeferRender": true,//是否启用延迟加载：当你使用AJAX数据源时，可以提升速度。
    		"aLengthMenu": [20, 25,30,100],
            "columns": [
            	{ "data": "statisticsAt"},
                { "data": "totalNumber"},     
                { "data": "successNumber"},
                { "data": "waitingNumber" },
                { "data": "failureNumber" },
                { "data": "repeatNumber" },
                { "data": "insertedAt" },
                { "data": "updatedAt"}
            ],
            "bFilter" : false,// 搜索栏
            "oLanguage": language,
            "rowCallback": function( row, data, index ) {
            	$('td:eq(5)', row).html("<a style='text-decoration: underline;cursor: pointer;' data-toggle='modal' data-target='#taskStatisticsModel' data-statistics-id='"+data.id+"' name='repeatDataHShow'><span class='badge'>"+data.repeatNumber+"</span></a>")
            	
            	 $('[data-toggle="tooltip"]',row).tooltip();
                $("a[name='repeatDataHShow']",row).click(repeatDataHShow);
             },
             "aaSorting": [[ 6, "desc" ]],
             "columnDefs":[
//	        	 {"orderable":false,"targets":[0]}
	         ],
             "fnServerParams": function ( data ) {
                 data.push(
                         { "name": "insertedBegin", "value": $("#insertedBegin").val() },
                         { "name": "insertedEnd", "value": $("#insertedEnd").val() }
                 );
	         },
	         "sAjaxSource": base+"/index/ajax",
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
    
    function repeatDataHShow(){
    	var obj=$(this);
		var id=obj.data("statistics-id");
		$.ajax({
    		type : 'POST',
    		contentType : 'application/json;charset=UTF-8',
    		dataType : 'json',
    		cache : false,
    		url : base + "/index/get/"+id,
    		success : function(json) {
    			$("#repeatDataOl").empty();
    			if (json.statusCode == 200) {
    				if(null !=  json.data.taskStatistics.repeatData){
	    				for(var li in json.data.taskStatistics.repeatData){
	    					var li = "<li>"+json.data.taskStatistics.repeatData[li]+"</li>";
	    					$("#repeatDataOl").append(li);
	    				}
	    			}else{
	    				$("#repeatDataOl").append("<li>无</li>");
	    			}
    			} else {
    				warning(json.message);
    			}
    		}
    	});
    	
    }
 
});
