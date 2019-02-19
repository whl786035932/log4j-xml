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
    	$.backstretch(base+"/static/libs/template/login/assets/img/backgrounds/1.jpg");
    }

    /**
     * 组装event监听
     */
    function addEventListener(){
    	$("#submit").click(loginSubmit);
    	$("body").keydown(keyLogin);
   		 
    }
    function keyLogin(){
    	 if (event.keyCode==13){
    		 $("#submit").click();
    	 }
    }
    function loginSubmit(){
    	var user = {
    		username:$("#form-username").val(),
    		password:$("#form-password").val()
    	};
    	$.ajax({
    		type: "POST",
            url:base+"/login",
            data:user,
            dataType:"json",
            success: function(data){
            	if(data.statusCode == 200){
            		window.location.href = base+"/index";
            	}else{
            		$("#vertify").html("用户名或密码错误！");
            		$("#vertify").css("color","red");
            		console.log(data)
            	}
            	
            },
            error: function(data){
            	console.log(data)
            }
    	});
    }
});
