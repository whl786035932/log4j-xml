//$(function(){
	var  table;
	var base = $("#base").attr("href");
    /** 初始化所有组件 */
    initComponents();

    /** 监听所有event事件 */
    addEventListeners();
    
    /**
     * 组装时间控件
     */
    function initComponents()
    {
    	getToday();
    }

    /**
     * 组装event监听
     */
    function addEventListeners()
    {
    	$("#exportExcel").click(exportExcel);
    }
    

    function exportExcel() {
	var startDate = $("#startDay").val();
	var endDate = $("#endDay").val();

	if (startDate && endDate) {
		console.log(startDate + "---------" + endDate);
		$.Zebra_Dialog("确认导出Excel数据?", {
			'type' : 'question',
			'title' : '请确认',
			'buttons' : [ '取消', '确定' ],
			'onClose' : function(caption) {
				var option = (caption != '' ? '"' + caption + '"' : 'nothing');
				if ("\"确定\"" == option) {
					var data = {
						"startDate" : startDate,
						"endDate" : endDate
					};
					console.log("查询参数为:" + JSON.stringify(data));
					window.location.href = base + "/instorageStatistics/exportExcel?data=" + JSON.stringify(data);
				} else {
					return;
				}
			}
		});
	}

}
    function getToday(){
    	var date = new Date();
        var seperator1 = "-";
        var year = date.getFullYear();
        var month = date.getMonth() + 1;
        var strDate = date.getDate();
        if (month >= 1 && month <= 9) {
            month = "0" + month;
        }
        if (strDate >= 0 && strDate <= 9) {
            strDate = "0" + strDate;
        }
        var currentdate = year + seperator1 + month + seperator1 + strDate;
        $("#chooseDay").val(currentdate);
        getData(currentdate)
    }
    function initStatisticTree(){
    	getData();
    }
    function change(obj,dp){
        var date = $(obj).val();
        getData(date);
      }
    function getData(date){
    	var data = {
    			"date":date
    	};
    	$.ajax({
            "type" : 'post',
            "url" : base+"/instorageStatistics/ajax",
            "contentType" : 'application/json;charset=UTF-8',
            "data":JSON.stringify(data),
            "dataType" : "json",
            "success" : function(data) {
            	console.log("成功");
            	console.log(data);
            	if(data.data.children){
            		var children = data.data.children;
            		for(var i=0;i<children.length;i++){
            			children[i].name = children[i].name+" ( "+children[i].value+" ) ";
            			if(children[i].children){
            				for(var j=0;j<children[i].children.length;j++){
            					children[i].children[j].name = children[i].children[j].name+" ( "+children[i].children[j].value+" ) ";
            				}
            				
            			}
            			
            		}
            	}
            	showCharts(data.data)
            	
            },
            "error":function(){
            	console.log("失败");
            }
        });
    }
    function showCharts(data) {
    	var myChart = echarts.init(document.getElementById('statisticTree'));
    	myChart.showLoading();
	    myChart.hideLoading();

	    echarts.util.each(data.children, function (datum, index) {
	        //index % 2 === 0 && (datum.collapsed = true);
	    	//datum.collapsed = false
	    	return false;
	    });

	    myChart.setOption(option = {
	        tooltip: {
	            trigger: 'item',
	            triggerOn: 'mousemove'
	        },
	        series: [
	            {
	                type: 'tree',

	                data: [data],

	                top: '1%',
	                left: '10%',
	                bottom: '1%',
	                right: '20%',

	                symbolSize: 10, //标记的大小，就是那个小圆圈

	                label: { //每个节点所对应的标签的样式
	                    normal: {
	                        position: 'left',
	                        verticalAlign: 'middle',
	                        align: 'right',
	                        fontSize: 13
	                    }
	                },

	                leaves: {
	                    label: {
	                        normal: {
	                            position: 'right',
	                            verticalAlign: 'middle',
	                            align: 'left'
	                        }
	                    }
	                },

	                expandAndCollapse: true, //子树折叠和展开的交互，默认打开
	                animationDuration: 550, //初始动画的时长，支持回调函数,默认1000
	                animationDurationUpdate: 750 //数据更新动画的时长，默认300
	            }
	        ]
	    });
	}
   
//});
