<#assign tiles=JspTaglibs["http://tiles.apache.org/tags-tiles"]>
<!DOCTYPE html>
<html lang="en">
  <head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <!-- Meta, title, CSS, favicons, etc. -->
    <meta charset="utf-8">
    <base id="base" href="${base}">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="width=device-width, initial-scale=1">

    <title>${ (project.title)! }</title>
	<link rel="shortcut icon" href="${ (project.staticDomain)! }/images/favicon.ico">
    <!-- Bootstrap -->
    <link href="${ (project.staticDomain)! }/libs/template/vendors/bootstrap/dist/css/bootstrap.min.css" rel="stylesheet">
    <!-- Font Awesome -->
    <link href="${ (project.staticDomain)! }/libs/template/vendors/font-awesome/css/font-awesome.min.css" rel="stylesheet">
    <!-- NProgress -->
    <link href="${ (project.staticDomain)! }/libs/template/vendors/nprogress/nprogress.css" rel="stylesheet">
    <!-- Custom Theme Style -->
    <link href="${ (project.staticDomain)! }/libs/template/build/css/custom.min.css" rel="stylesheet">
    <!-- common Style -->
    <link href="${ (project.staticDomain)! }/css/common/style.default.css" rel="stylesheet">
    <link rel="stylesheet" href="${(project.staticDomain)!}/libs/Zebra_Dialog-1.4.1/dist/css/flat/zebra_dialog.min.css">
    <link href="${ (project.staticDomain)! }/libs/dataTables-1.10.15/css/dataTables.bootstrap.min.css" rel="stylesheet">
    <!-- <link href="${ (project.staticDomain)! }/libs/dataTables-1.10.15/css/jquery.dataTables.min.css" rel="stylesheet">-->
   
    <!-- jQuery -->
    <script src="${ (project.staticDomain)! }/libs/template/vendors/jquery/dist/jquery.min.js"></script>
    <!-- Bootstrap -->
    <script src="${ (project.staticDomain)! }/libs/template/vendors/bootstrap/dist/js/bootstrap.js"></script>
    <script src="${ (project.staticDomain)! }/libs/template/vendors/parsleyjs/dist/parsley.min.js"></script>
    <script src="${ (project.staticDomain)! }/libs/dataTables-1.10.15/js/jquery.dataTables.min.js"></script>
	<script src="${ (project.staticDomain)! }/libs/dataTables-1.10.15/js/dataTables.bootstrap.min.js"></script>
	<script src="${ (project.staticDomain)! }/libs/Zebra_Dialog-1.4.1/dist/zebra_dialog.min.js"></script>
	<script src="${ (project.staticDomain)! }/libs/My97DatePicker/WdatePicker.js"></script>
  </head>

  <body class="nav-md">
    <div class="container body">
      <div class="main_container">
		  <@tiles.insertAttribute name="header"/>
		  <@tiles.insertAttribute name="body"/>
		  <@tiles.insertAttribute name="footer"/>
      </div>
    </div>
    <!-- NProgress -->
    <script src="${ (project.staticDomain)! }/libs/template/vendors/nprogress/nprogress.js"></script>

    <!-- Custom Theme Scripts -->
    <script src="${ (project.staticDomain)! }/libs/template/build/js/custom.js"></script>
    
     <script src="${ (project.staticDomain)! }/js/common.js"></script>
 </body>
</html>