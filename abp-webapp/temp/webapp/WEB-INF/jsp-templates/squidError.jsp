<!DOCTYPE HTML PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html xml:lang="en" xmlns="http://www.w3.org/1999/xhtml" lang="en"><head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<meta name="viewport" content="width=device-width, initial-scale = 1.0, maximum-scale = 1.0"> 
<meta name="MobileOptimized" content="320" /> 
<title>Gogo Inflight Internet</title>
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
 
#wrapSmall {
	width:100%;
	margin:3% auto;
}
#brand {
	text-align:center;
}
.align_center {
	text-align:center;
}
h1 {
	font-size:15px;
	margin:25px auto;
}
#primary p {
	font-size:12px;
	line-height:20px;
}
</style>
</head>

<body>
<div id="wrap">
	<div id="header">
		<div id="brand">
        	<a href=""><img id="logo" src="/abp/images/Gogo_Tag_5C_RGB_148x100.png" width="148" height="100" border="0" alt="Gogo" /></a>
        </div><!-- /brand -->
	</div><!-- /header -->

	<div id="primary" class="align_center">
	<c:if test="${'JAL' eq airlineCode}">
	<div id="errorJap" class="errorMssg">
		<h1>&#12371;&#12398;&#12506;&#12540;&#12472;&#12399;&#29694;&#22312;&#12372;&#21033;&#29992;&#12391;&#12365;&#12414;&#12379;&#12435;&#12290;</h1>
        <p>&#24656;&#12428;&#20837;&#12426;&#12414;&#12377;&#12364;&#12289;&#34920;&#31034;&#12375;&#12424;&#12358;&#12392;&#12375;&#12390;&#12356;&#12427;&#12506;&#12540;&#12472;&#12399;&#29694;&#22312;&#12450;&#12463;&#12475;&#12473;&#12391;&#12365;&#12394;&#12367;&#12394;&#12387;&#12390;&#12356;&#12414;&#12377;&#12290;<br /> &#21407;&#22240;&#12399;&#12289;&#34920;&#31034;&#12375;&#12424;&#12358;&#12392;&#12375;&#12390;&#12356;&#12427;&#12506;&#12540;&#12472;&#12395;&#21839;&#38988;&#12364;&#12354;&#12427;&#12363;&#12289;&#12414;&#12383;&#12399;&#39131;&#34892;&#27231;&#12364;&#12469;&#12540;&#12499;&#12473;&#25552;&#20379;&#12398;&#12383;&#12417;&#12395;&#24517;&#35201;&#12394;&#39640;&#24230;&#20197;&#19979;&#12434;&#39131;&#34892;&#20013;&#12391;&#12354;&#12427;&#12363;&#12398;&#12356;&#12378;&#12428;&#12363;&#12364;&#32771;&#12360;&#12425;&#12428;&#12414;&#12377;&#12290;<br /> <br /> Gogo&#12398;&#12469;&#12540;&#12499;&#12473;&#12399;&#12289;&#39131;&#34892;&#27231;&#12364;&#24033;&#33322;&#39640;&#24230;&#12434;&#39131;&#34892;&#20013;&#12372;&#21033;&#29992;&#12356;&#12383;&#12384;&#12369;&#12414;&#12377;&#12290;<br /> &#24656;&#12428;&#20837;&#12426;&#12414;&#12377;&#12364;&#12289;&#12418;&#12358;&#23569;&#12293;&#12362;&#24453;&#12385;&#38914;&#12365;&#12289;&#20877;&#24230;&#12362;&#35430;&#12375;&#12367;&#12384;&#12373;&#12356;&#12290;</p>
    </div>
	</c:if>
	<c:if test="${'ACA' eq airlineCode}">
    <div id="errorFr" class="errorMssg">
    	<h1>Cette page n'est pas disponible pour l'instant.</h1>
        <p>Nous sommes vraiment d&eacute;sol&eacute;s, mais la page Web &agrave; laquelle vous tentez d'acc&eacute;der n'est pas disponible pour l'instant.<br /> Cela peut &ecirc;tre li&eacute; &agrave; des probl&egrave;mes touchant le site Web que vous tentez d'ouvrir ou d&eacute;couler du fait que l'avion se trouve en-dehors de la zone de couverture.<br /> <br /> La couverture est disponible dans la zone continentale des &Eacute;tats-Unis, lorsque l'avion atteint une altitude sup&eacute;rieure &agrave; 3 048 m (10 000 pi).<br /> Veuillez essayer de nouveau dans environ une minute. Nous esp&eacute;rons que tout ira bien.</p>
    </div>
    </c:if>
	<div id="errorEng" class="errorMssg">
        <h1>This page is currently unavailable.</h1>
        <p>The web page you are attempting to access is currently unavailable.<br /> This may be due to issues with the website you are trying to visit, or the airplane is outside the coverage area.<br /> <br /> Coverage is available while the airplane is above 10,000 feet.<br /> Please try again in a minute or so, and keep your fingers crossed.</p> 
	</div>
	</div><!-- /primary -->

    <div id="secondary"></div><!-- /secondary -->

	<div id="footer"></div><!-- /footer -->
</div><!-- /wrap -->
</body>
</html>