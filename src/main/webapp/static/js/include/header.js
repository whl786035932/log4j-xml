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
    }

    /**
     * 组装event监听
     */
    function addEventListener(){
      	$("#updateCrrentPassword").click(updateCrrentPassword);
      	$("#update_current_password_sure").click(updateCrrentPasswordSure);
    }

    function updateCrrentPassword(){
          $("#updateCrrentPasswordModel").modal("show");
    }
    
    function updateCrrentPasswordSure(){
    	
    	var password = $("#update_current_password").val();
    	if(password == null || password == ""){
    		$("#update_current_password_required").text("密码不能为空");
    		return ;
    	}
    	
    	var repassword = $("#update_current_repassword").val();
    	if(repassword != password){
    		$("#update_current_repassword_required").text("两次密码不一致");
    		return ;
    	}
    	
    	if($('#update_current_password_form').parsley().validate()){
    		var url = base+"/updateCurrentPassword";
        	$.ajax({ 
    			"url":url,
    			"data":$('#update_current_password_form').serialize(),
    			"type":"POST",
    			"success":function(resp){
    				$("#updateCurrentPasswordModel").modal("hide");
    				if(resp.statusCode == 200)
    					location.href = base+"/logout";
    				else{
    					warning(resp.message);
    				}
    			},
    			"error":function(){
    				$("#updateCurrentPasswordModel").modal("hide");
    				error();
    			}
    		});
		}
    }
    
});
