<!DOCTYPE HTML PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Gogo Inflight Internet</title>
<meta name="viewport" content="width=device-width, initial-scale = 1.0, maximum-scale = 1.0">
<script type="text/javascript" src="/abp/js/IE6PNG.js"></script>
<script type="text/javascript" src="/abp/js/gbp_wtinit.js"></script>
<script>
var AIRLINE = '<c:out value="${airlineCode}"/>';
function loadfirst()
{
	if(_IE6_processPNG)
		_IE6_processPNG();
}
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
	max-width: 600px;
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
	<c:if test="${'JAL' eq airlinecode}">
	<div id="errorJap" class="errorMssg">
          <h1>&#12463;&#12483;&#12461;&#12540;&#12364;&#24517;&#35201;&#12391;&#12377;</h1>
		  <p>&#24656;&#12428;&#20837;&#12426;&#12414;&#12377;&#12364;&#12289;Gogo&#27231;&#20869;&#12452;&#12531;&#12479;&#12540;&#12493;&#12483;&#12488;&#12434;&#12372;&#21033;&#29992;&#12398;&#22580;&#21512;&#12289;&#12502;&#12521;&#12454;&#12470;&#12364;Cookie&#12434;&#21463;&#12369;&#20837;&#12428;&#12427;&#12424;&#12358;&#12395;&#35373;&#23450;&#12375;&#12390;&#19979;&#12373;&#12356;&#12290;<br />			&#12372;&#20351;&#29992;&#12398;&#12502;&#12521;&#12454;&#12470;&#12398;&#35373;&#23450;&#12424;&#12426;&#12289;&#12300;&#12469;&#12452;&#12488;&#12363;&#12425;&#12398;Cookie&#12434;&#21463;&#12369;&#20837;&#12428;&#12427;(&#26377;&#21177;&#12395;&#12377;&#12427;)&#12301; &#12434;&#36984;&#25246;&#12375;&#12289; &#20877;&#24230;&#12362;&#35430;&#12375;&#12367;&#12384;&#12373;&#12356;&#12290;</p>
		  <p>&nbsp;</p><br/>
    </div>
	</c:if>
	<c:if test="${'ACA' eq airlineCode}">
    <div id="errorFr" class="errorMssg">
    	<h1>T&eacute;moins requis</h1>
	    <p>Une session Internet en vol Gogo exige que votre navigateur accepte les t&eacute;moins.<br />
		Dans les pr&eacute;f&eacute;rences de votre navigateur, s&eacute;lectionnez &laquo; accepter les t&eacute;moins des sites &raquo;, <br />
		puis essayez de nouveau. </p>
	  	<p>&nbsp;</p><br/>
    </div>
    </c:if>
	<div id="errorEng" class="errorMssg">
          <h1>Cookies Required</h1>
		  <p>Sorry.A Gogo Inflight Internet session requires your browser to accept Cookies.<br />
			In your browser preferences, select &quot;accept cookies from sites,&quot; then try again. </p>
		  <p>&nbsp;</p>
    </div>
	</div><!-- /primary -->

    <div id="secondary"></div><!-- /secondary -->

	<div id="footer"></div><!-- /footer -->
</div><!-- /wrap -->
</body>
</html>