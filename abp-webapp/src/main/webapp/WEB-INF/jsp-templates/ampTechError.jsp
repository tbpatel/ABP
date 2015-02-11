<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html lang="en" xmlns="http://www.w3.org/1999/xhtml" xml:lang="en">
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!--
<?xml version="1.0" encoding="UTF-8"?>
<WISPAccessGatewayParam xmlns:xsi=”http://www.w3.org/2001/XMLSchema-instance xsi:noNamespaceSchemaLocation="http://@abp-webapp.hostandpath@/static/xsd/WISPAccessGatewayParam.xsd">
		<AuthenticationReply>
			<MessageType>120</MessageType>
			<ResponseCode>255</ResponseCode>
			<ReplyMessage>Access Gateway Error</ReplyMessage>
			<LoginResultsURL>
https://@abp-webapp.hostandpath@/gbp/smartClientLogin.do;jsessionid=${jsessionid}
			</LoginResultsURL>
			<LogoffURL>
				http://@abp-webapp.hostandpath@/abp/page/aaaDeactivate.do?gbpsessionid=${jsessionid}
			</LogoffURL>
		</AuthenticationReply>
</WISPAccessGatewayParam>
-->
<!--
<CaptchaReply>
<MessageType>120</MessageType>
<ResponseCode>201</ResponseCode>
<ReplyMessage>Captcha Pending</ReplyMessage>
<Retry>0</Retry>
</CaptchaReply>
-->

    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
        <title>Gogo Inflight Internet</title>
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
		<script>
		var siteRoot='http://@abp-webapp.host@';
	/*on click of the retrt button this function will be called*/
	function retryActivateAbp()
		{
			var ajax2 = new ExAjax();
			var url =   siteRoot +'/abp/page/aaaActivate.do';
			sessionId = document.cookie.substring(document.cookie.indexOf("JSESSIONID"));	
			sessionId = sessionId.substring(sessionId.indexOf("=") + 1,sessionId.indexOf(";"));
			var index = new Date().getTime();
			var param='gbpsessionid=' + sessionId + '&index=' + index;
			ajax2.setPost(false);
			ajax2.setPostData(url,param,"activateSuccess");
		}


		function activateSuccess(http){	  
		var response=eval("(" + http.responseText + ")");
		 if(response.status ==200){
	  	window.location = siteRoot + '/gbp/gobrowse.do';
		}
	  else{
			if(response.gbptecherror=="false")
				{
					  window.location = siteRoot + '/abp/techError.do';
				}else
				{
					 window.location = siteRoot + '/gbp/techError.do';
				}
			 }
		}
		/*On click of gogo customer care button this function will be called
		function  goToCustomercare()
		{
			
			 window.location = siteRoot + '/gbp/customerCare.do'; 
		}
		*/

		var newWindow = null;
function showChat(winURL) {
	winURL=siteRoot+winURL;
        newWindow = window.open(winURL, "eChatWindow", "width=600,height=500,toolbar=no,menubar=no,scrollbars=no,resizable=no");
        newWindow.focus();
        return newWindow;
}
var AIRLINE = '<c:out value="${airlineCode}"/>';
		</script>
    </head>
    <body id="activation">
        <div id="wrap">
			<div id="header">
		<div id="brand">
        	<a href=""><img id="logo" src="/abp/images/Gogo_Tag_5C_RGB_148x100.png" width="148" height="100" border="0" alt="Gogo" /></a>
        </div><!-- /brand -->
	</div><!-- /header -->
        <c:if test="${'JAL' eq airlineCode }">
        <div id="errorJap" class="errorMssg">
        	<h2>&#12456;&#12521;&#12540;</h2>
            <div class="content">
            	<p>&#21482;&#20170;&#12289;&#25509;&#32154;&#12377;&#12427;&#12371;&#12392;&#12364;&#12391;&#12365;&#12414;&#12379;&#12435;&#12290;&#20877;&#24230;&#12420;&#12426;&#30452;&#12375;&#12390;&#38914;&#12367;&#12363;&#65288;&#12362;&#23458;&#27096;&#12398;&#12459;&#12540;&#12489;&#12395;&#12399;&#35531;&#27714;&#12373;&#12428;&#12414;&#12379;&#12435;&#65289;&#12289;&#12459;&#12473;&#12479;&#12510;&#12540;&#12469;&#12509;&#12540;&#12488;&#12414;&#12391;&#12362;&#21839;&#12356;&#21512;&#12431;&#12379;&#12367;&#12384;&#12373;&#12356;&#12290;</p>
            </div>
            <div class="buttonTray">
	           <a href="#" onclick="retryActivateAbp()"><img src="http://@abp-webapp.hostandpath@/abp/library/images/abp/retry.png" alt="&#12522;&#12488;&#12521;&#12452;" /></a>
	           <a href="#" onclick="showChat('/gbp/eChat.do');"><img src="http://@abp-webapp.hostandpath@/abp/library/images/abp/customer_care_techError.png" alt="&#12459;&#12473;&#12479;&#12510;&#12540;&#12469;&#12509;&#12540;&#12488;" /></a>
            </div>
        </div>
        </c:if>
        <c:if test="${'ACA' eq airlineCode}">
    	<div id="errorFr" class="errorMssg">
			<h2>erreur technique</h2>
    		<div class="content">
            	<p>Nous ne pouvons &eacute;tablir votre connexion pour le moment. Veuillez r&eacute;essayer (aucun frais ne seront port&eacute;s &agrave; votre carte) ou communiquer avec le service &agrave; la client&egrave;le. </p>
            </div>
            <div class="buttonTray">
	           <a href="#" onclick="retryActivateAbp()"><img src="http://@abp-webapp.hostandpath@/abp/library/images/abp/retry.png" alt="R&eacute;essayer"  /></a>
	           <a href="#" onclick="showChat('/gbp/eChat.do');"><img src="http://@abp-webapp.hostandpath@/abp/library/images/abp/customer_care_techError.png" alt="Service &agrave; la client&egrave;le"  /></a>
            </div>
    	</div>
    	</c:if>
        <div id="errorEng" class="errorMssg">
        	<h2>Technical Error</h2>
            <div class="content">
            	<p>We are unable to connect you at this time.
					Please retry (we will not charge your card) or contact customer care. </p>
            </div>
            <div class="buttonTray">
           <a href="#" onclick="retryActivateAbp()"><img src="http://@abp-webapp.hostandpath@/abp/library/images/abp/retry.png" alt="Retry" /></a>
           <a href="#" onclick="showChat('/gbp/eChat.do');"><img src="http://@abp-webapp.hostandpath@/abp/library/images/abp/customer_care_techError.png" alt="Customer Care" /></a>
            </div>
        </div>
          
    	</div><!-- /wrap -->
        
    </body>
</html>