<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.0 Transitional//EN">
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>
<head>
<meta http-equiv="content-type" content="text/html; charset=ISO-8859-1">
  <!--
    <?xml version="1.0" encoding="UTF-8"?>
	<WISPAccessGatewayParam xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance" xsi:noNamespaceSchemaLocation="http://airborne.gogoinflight.com/static/xsd/WISPAccessGatewayParam.xsd">
		<Proxy>
			<MessageType>110</MessageType>
			<NextURL>http://airborne.gogoinflight.com/abp/page/abpRoaming.do</NextURL>
			<ResponseCode>200</ResponseCode>
			<Delay>0</Delay>
		</Proxy>
	</WISPAccessGatewayParam>
  -->

<meta http-equiv="REFRESH" content="0;URL=/abp/page/connecting.do?abpflg=<%=request.getParameter("abpflg")%>">
<title>Connecting to Ground..</title>

<script type="text/javascript">
var secs
var timerID = null
var timerRunning = false
var delay = 1000
var AIRLINE = '<c:out value="${airlineCode}"/>';

function InitializeTimer(){
	secs = 120
	StopTheClock()
	StartTheTimer()
}

function StopTheClock(){
	if(timerRunning)
		clearTimeout(timerID)
	timerRunning = false
}

function StartTheTimer(){
	if (secs == 0){
		StopTheClock()
		window.location = "/abp/page/connecting.do?abpflg="+<%=request.getParameter("abpflg")%>;
	} else {
		self.status = secs
		secs = secs - 1
		timerRunning = true
		timerID = self.setTimeout("StartTheTimer()", delay)
	}
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
	text-align:center;
}
#wrap {
	width:600px;
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
	line-height:22px;
	margin:25px auto;
}
#primary p {
	font-size:12px;
	line-height:20px;
}
#primary a {
	color:#58595b;
}
#ani_gif {
	text-align:center;
	margin:8px auto 30px auto;
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
    <c:if test="${'JAL' eq param.airlineCode}">
    <div id="errorJap" class="errorMssg">
        <h1>&#27231;&#20869;&#12452;&#12531;&#12479;&#12540;&#12493;&#12483;&#12488;&#21450;&#12403;&#12456;&#12531;&#12479;&#12540;&#12486;&#12452;&#12531;&#12513;&#12531;&#12488;&#12506;&#12540;&#12472;&#12408;&#36578;&#36865;&#20013;&#12391;&#12377;&#12290;</h1>
        <div id="ani_gif">
        	<img src="/abp/images/animated_interstitial.gif" width="124" height="18" border="0" alt="&#23569;&#12293;&#12362;&#24453;&#12385;&#19979;&#12373;&#12356;" />
        </div>
        <p>&#36578;&#36865;&#12373;&#12428;&#12394;&#12356;&#22580;&#21512;&#12399;&#12289;<a href="/abp/page/connecting.do?abpflg=<%=request.getParameter("abpflg")%>">&#12371;&#12385;&#12425;&#12434;&#12463;&#12522;&#12483;&#12463;&#12375;&#12390;&#19979;&#12373;&#12356;</a></p>
    </div>
    </c:if>
    <c:if test="${'ACA' eq param.airlineCode}">
   	<div id="errorFr" class="errorMssg">
        <h1>Redirection vers une exp&eacute;rience<br>en vol exclusive.</h1>
        <div id="ani_gif">
        	<img src="/abp/images/animated_interstitial.gif" width="124" height="18" border="0" alt="Veuillez patienter"  />
        </div>
        <p>Si la redirection ne fonctionne pas, <a href="/abp/page/connecting.do?abpflg=<%=request.getParameter("abpflg")%>">cliquez ici</a></p>
   	</div>
    </c:if>
    <div id="errorEng" class="errorMssg">
        <h1>Redirecting you to an exclusive<br>in-air experience.</h1>
        <div id="ani_gif">
        	<img src="/abp/images/animated_interstitial.gif" width="124" height="18" border="0" alt="Please wait" />
        </div>
        <p>If you are not redirected, <a href="/abp/page/connecting.do?abpflg=<%=request.getParameter("abpflg")%>">click here </a></p>
	</div>
	</div><!-- /primary -->

    <div id="secondary"></div><!-- /secondary -->

	<div id="footer"></div><!-- /footer -->
</div><!-- /wrap -->
<input id="abpflg" value="<%=request.getParameter("abpflg")%>" type="hidden">
<noscript>
	<meta http-equiv=REFRESH content="0;URL=/abp/page/connecting.do?abpflg=<%=request.getParameter("abpflg")%>">
</noscript>
<script>
window.onload = function() {
//	InitializeTimer();
}
</script>
</body>
</html>
