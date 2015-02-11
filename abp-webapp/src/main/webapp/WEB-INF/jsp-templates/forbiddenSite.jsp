<!DOCTYPE HTML PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html xml:lang="en" xmlns="http://www.w3.org/1999/xhtml" lang="en">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Gogo Inflight Internet</title>
<meta name="viewport" content="width=device-width, initial-scale = 1.0, maximum-scale = 1.0">
<meta name="MobileOptimized" content="320" />
<script>
var AIRLINE = '<c:out value="${airlineCode}"/>';
</script>
<style type="text/css">
body, div, a, img, h1, p {
	margin:0;
	padding:0;
	border:none;
}
body {
	font-family:Helvetica,Arial, sans-serif;
	font-size:12px;
	color:#58595b;
	background:#fff;
	margin:0 auto;
	text-align:center;
}
#wrap {
	width:100%;
	margin:5% auto;
}
#brand {
	text-align:center;
}
.align_center {
	text-align:center;
}
h1 {
	font-size:15px;
	margin:25px auto 5px auto;
}
#primary p {
	font-size:12px;
	line-height:20px;
	margin:4px auto 25px auto;
}
</style>
</head>

<body>
<div id="wrap">
    <div id="header">
        <div id="brand" class="align_center">
            <a href="#"><img id="logo" src="/abp/images/Gogo_Tag_5C_RGB_148x100.png" width="148" height="100" border="0" alt="Gogo" /></a>
        </div><!-- /brand -->
    </div><!-- /header -->
	
	<div id="primary" class="align_center">
	<c:if test="${'JAL' eq airlineCode}">
	<div id="errorJap" class="errorMssg">
    	<h1>&#30003;&#12375;&#35379;&#24481;&#24231;&#12356;&#12414;&#12379;&#12435;&#12364;&#12289;&#20170;&#34920;&#31034;&#12375;&#12424;&#12358;&#12392;&#12375;&#12390;&#12356;&#12427;&#12506;&#12540;&#12472;&#12399;&#12372;&#21033;&#29992;&#12356;&#12383;&#12384;&#12369;&#12414;&#12379;&#12435;&#12290;</h1><br />
        <p>&#12372;&#21033;&#29992;&#12395;&#12394;&#12425;&#12428;&#12427;&#12362;&#23458;&#12373;&#12414;&#20840;&#21729;&#12395;&#27231;&#20869;&#12452;&#12531;&#12479;&#12540;&#12493;&#12483;&#12488;&#12469;&#12540;&#12499;&#12473;&#12434;&#26368;&#22823;&#38480;&#12362;&#27005;&#12375;&#12415;&#12356;&#12383;&#12384;&#12367;&#12383;&#12417;&#12289;&#33322;&#31354;&#20250;&#31038;&#12398;&#26041;&#37341;&#12395;&#12424;&#12426;&#12467;&#12531;&#12486;&#12531;&#12484;&#12501;&#12451;&#12523;&#12479;&#12522;&#12531;&#12464;&#12434;&#23455;&#26045;&#12375;&#12390;&#12362;&#12426;&#12414;&#12377;&#12290;</p>
        <p>&#12372;&#29702;&#35299;&#12398;&#31243;&#12289;&#23452;&#12375;&#12367;&#12362;&#39000;&#12356;&#33268;&#12375;&#12414;&#12377;&#12290;</p>
	</div>
	</c:if>
	<c:if test="${'ACA' eq airlineCode}">
    <div id="errorFr" class="errorMssg">
    	<h1>Veuillez nous excuser, mais la page que vous tentez de voir n'est pas disponible.</h1><br />
        <p>Cette compagnie a&eacute;rienne a pour politique d'emp&ecirc;cher tout contenu potentiellement inappropri&eacute; d'&ecirc;tre affich&eacute; sur votre ordinateur ou appareil.</p>
        <p>Merci de votre compr&eacute;hension.</p>
    </div>
    </c:if>
	<div id="errorEng" class="errorMssg">
    	<h1>We apologize, but the page you’re attempting to view is not available.</h1><br />
        <p>It is the policy of this airline to prevent potentially inappropriate content from being displayed on your computer or device.</p>
        <p>Thank you for understanding.</p>
    </div>
    </div><!-- /primary -->

    <div id="secondary"></div><!--  /secondary  -->
    <div id="footer">
    	<div id="legal"></div>
    </div><!-- /footer -->
</div><!-- /wrap -->
</body>
</html>