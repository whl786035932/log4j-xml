<#assign tiles=JspTaglibs["http://tiles.apache.org/tags-tiles"]>
<!DOCTYPE html>
<html lang="en">

    <head>

        <meta charset="utf-8">
        
        <meta http-equiv="X-UA-Compatible" content="IE=edge">
        <meta name="viewport" content="width=device-width, initial-scale=1">
        <title>${ (project.title)! }</title>
		<link rel="shortcut icon" href="${ (project.staticDomain)! }/images/favicon.ico">
        <!-- CSS -->
        <link rel="stylesheet" href="${ (project.staticDomain)! }/libs/template/login/assets/css/fonts.googleapis.css">
        <link rel="stylesheet" href="${ (project.staticDomain)! }/libs/template/login/assets/bootstrap/css/bootstrap.min.css">
        <link rel="stylesheet" href="${ (project.staticDomain)! }/libs/template/login/assets/font-awesome/css/font-awesome.min.css">
		<link rel="stylesheet" href="${ (project.staticDomain)! }/libs/template/login/assets/css/form-elements.css">
        <link rel="stylesheet" href="${ (project.staticDomain)! }/libs/template/login/assets/css/style.css">

        <!-- HTML5 Shim and Respond.js IE8 support of HTML5 elements and media queries -->
        <!-- WARNING: Respond.js doesn't work if you view the page via file:// -->
        <!--[if lt IE 9]>
            <script src="https://oss.maxcdn.com/libs/html5shiv/3.7.0/html5shiv.js"></script>
            <script src="https://oss.maxcdn.com/libs/respond.js/1.4.2/respond.min.js"></script>
        <![endif]-->

        <link rel="apple-touch-icon-precomposed" sizes="144x144" href="${ (project.staticDomain)! }/libs/template/login/assets/ico/apple-touch-icon-144-precomposed.png">
        <link rel="apple-touch-icon-precomposed" sizes="114x114" href="${ (project.staticDomain)! }/libs/template/login/assets/ico/apple-touch-icon-114-precomposed.png">
        <link rel="apple-touch-icon-precomposed" sizes="72x72" href="${ (project.staticDomain)! }/libs/template/login/assets/ico/apple-touch-icon-72-precomposed.png">
        <link rel="apple-touch-icon-precomposed" href="${ (project.staticDomain)! }/libs/template/login/assets/ico/apple-touch-icon-57-precomposed.png">
		<base id="base" href="">
    </head>

    <body>

        <!-- Top content -->
        <div class="top-content">
        	
            <div class="inner-bg">
                <div class="container">
                    <div class="row">
                        <div class="col-sm-8 col-sm-offset-2 text">
                            <h1><strong>Content Management System</strong></h1>
                        </div>
                    </div>
                    <div class="row">
                        <div class="col-sm-6 col-sm-offset-3 form-box">
                        	<div class="form-top">
                        		<div class="form-top-left">
                        			<h3>内容管理系统 CMS2018</h3>
                            		<p id="vertify">请输入用户名和密码</p>
                        		</div>
                        		<div class="form-top-right">
                        			<i class="fa fa-key"></i>
                        		</div>
                            </div>
                            <div class="form-bottom">
		                    	<div class="form-group">
		                    		<label class="sr-only" for="username">Username</label>
		                        	<input type="text" name="username" placeholder="Username..." class="form-username form-control" id="form-username">
		                        </div>
		                        <div class="form-group">
		                        	<label class="sr-only" for="password">Password</label>
		                        	<input type="password" name="password" placeholder="Password..." class="form-password form-control" id="form-password">
		                        </div>
		                        <button id="submit" class="btn" style="width: 100%;">登录</button>
		                    </div>
                        </div>
                    </div>
                </div>
            </div>
            
        </div>
        <div class="copyrights">Collect from <a href="http://www.cssmoban.com/"  title="网站模板">网站模板</a></div>

        <!-- Javascript -->
        <script src="${ (project.staticDomain)! }/libs/template/login/assets/js/jquery-1.11.1.min.js"></script>
        <script src="${ (project.staticDomain)! }/libs/template/login/assets/bootstrap/js/bootstrap.min.js"></script>
        <script src="${ (project.staticDomain)! }/libs/template/login/assets/js/jquery.backstretch.min.js"></script>
        <script src="${ (project.staticDomain)! }/js/user/login.js"></script>
        
        <!--[if lt IE 10]>
            <script src="assets/js/placeholder.js"></script>
        <![endif]-->

    </body>

</html>
