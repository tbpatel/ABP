<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
	<title>Gogo Inflight Internet - Customer Care - Status Tray Opened</title>
	<meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1" />	
	<script type="text/javascript" src="http://airborne.gogoinflight.com/abp/library/js/gogo/gbp/gbp_wtinit.js"></script>
	<script>
		dcsInit.enabled = false;
	</script>
	<script type="text/javascript" src="http://airborne.gogoinflight.com/abp/library/js/gogo/wtbase.js"></script>
  	<NOSCRIPT><IMG ALT="" BORDER="0" NAME="DCSIMG" WIDTH="1" HEIGHT="1" SRC="http://statse.webtrendslive.com/dcsu77l1g100004n0nhv107n0_1s5g/njs.gif?dcsuri=/nojavascript&amp;WT.js=No&amp;WT.tv=8.0.2"></NOSCRIPT>
	<script>
	var loggedInState = "1";
	dcsMultiTrack('DCS.dcsuri', '/abp/status_tray/opened.htm', 'WT.ti', 'ABP-Status Tray', 'WT.cg_n', 'Status_tray', 'WT.cg_s', 'Interaction','WT.rv', loggedInState,'DCSext.airline','${airlineCode}', 'DCSext.flight_no','${flightNumberInfo}', 'DCSext.flight_details','${route}');
	</script>	
	<link rel="stylesheet" href="http://airborne.gogoinflight.com/abp/library/css/gbp/statustray.css" type="text/css" />
	<script type="text/javascript" src="http://airborne.gogoinflight.com/abp/library/js/gogo/shared/tools.js"></script>
	<script type="text/javascript" src="http://airborne.gogoinflight.com/abp/library/js/gogo/gbp/gbp.js"></script>
	<script type="text/javascript" src="http://airborne.gogoinflight.com/abp/library/js/gogo/abp/abp.js"></script>
	<script type="text/javascript" src="http://airborne.gogoinflight.com/abp/library/js/gogo/gbp/pages/statustray.js"></script>
	
	<script type="text/javascript">
        var closing;
	function openpath(action){
	        
			window.open('/gbp/'+action,'gogoWindow','toolbar=yes,directories=yes,status=yes,menubar=yes,location=yes,scrollbars=yes,resizable=yes');
		}
		//Added setCookie method by Kannan for Status tray defect.
	function setCookie(){
		closing=true;
		
		if(isIE()) {
			document.cookie = "statustray=open"+"; path=/";
		} else {
			document.cookie = "statustray=open"+";expires=-1"+"; path=/";
		}		
	}

	function isIE() {
	
		var agt=navigator.userAgent.toLowerCase();
		
		if (agt.indexOf("msie") != -1)  {
			return true;
		} else {
			return false;
		}		
	}

	</script> 
	
	
</head>
<body id="statusTray" onLoad="setCookie();">
	<div id="surround">
		<div id="nav">
			<div class="navButton"><a href="javascript:openpath('splash.do')" onclick="closing=true;displayWebtrends('homePage');"><img src="http://airborne.gogoinflight.com/abp/library/images/gbp/statustray/bt_home.gif" alt="portal homepage"/></a></div>
			<div class="navButton"><a href="javascript:openpath('selfCare.do')" onclick="closing=true;displayWebtrends('myAccount');"><img src="http://airborne.gogoinflight.com/abp/library/images/gbp/statustray/bt_account.gif" alt="my account"/></a></div>
			
			<!-- added by Abhishek for defect ID 911 --> 
	    	<div class="navButton"><a href="javascript:openpath('customerCare.do')" onclick="closing=true"><img src="http://airborne.gogoinflight.com/abp/library/images/gbp/statustray/bt_care.gif" alt="customer care"/></a></div>
	    	<!-- added by Abhishek for defect ID 911 --> 
			
			
		</div>
		<div id="status">
			<div id="logo" class="png">&#160;</div>
			<div id="service">&#160;</div>
		</div>
		<div id="loading"><p>Your internet service is being initialized. Use this window to keep track of your Gogo connection.</div>
		<div id="content">
			<div id="contentTop">&#160;</div>
			<div id="contentMid">
				<div id="contentWrapper">
					<div id="serviceInfo" class="pill">
						<div class="pillTop png">&#160;</div>
						<div class="pillMid png">
							<img src="http://airborne.gogoinflight.com/abp/library/images/gbp/statustray/hd_service.gif" alt="service information"/>
							<p id="alerts"></p>
						</div>
						<div class="pillBot png">&#160;</div>
					</div>
					<div id="facts" class="pill">
						<div class="pillTop png">&#160;</div>
						<div class="pillMid png">
							<p><font size="2"><strong>Gogo<sup><font size="1">&reg;</font></sup> fly facts</strong></font></p>
							<p id="gogoFacts"></p>
						</div>
						<div class="pillBot png">&#160;</div>
					</div>
				</div>
			</div>
			<div id="contentBot">&#160;</div>
		</div>
		<div id="flightInfo">
			<center>
				<img src="http://airborne.gogoinflight.com/abp/library/images/gbp/statustray/hd_flightinfo.gif" alt="flight information"/>
				
				<p id="flightPath"></p>
			</center>
		</div>
	</div>
	
	<script type="text/javascript">
var browser=navigator.userAgent;
//alert("browser==>"+browser)
window.addEvent('unload',function(){
        dothisbeforeclose();
});



//window.onbeforeunload = function(){
//Added by Kannan for status tray defect.
function dothisbeforeclose(){

        if(closing) {
                var url = window.location.href;
                if ( browser.indexOf( "Safari" ) != -1 ){
                confirmed = window.confirm("Keeping the Status Window open provides you with important Inflight Internet connection alerts and quick access back to Gogo.Are you sure you want to close?");
                }else{
                confirmed = window.confirm("Keeping the Status Window open provides you with important Inflight"+"\nInternet connection alerts and quick access back to Gogo." + "\nAre you sure you want to close?");
                }
                if (confirmed){
			if ( browser.indexOf( "Safari" ) != -1 ){
				window.alert("Ok, thanks!  If you need to get back to Gogo just type airborne.gogoinflight.com into your browser and you will be redirect back to the inflight portal");
			}else{
				window.alert("Ok, thanks!  If you need to get back to Gogo just type airborne.gogoinflight.com"+"\n into your browser and you will be redirect back to the inflight portal");
			}               
			
			if(isIE()) {
				document.cookie = "statustray=close"+"; path=/";
			} else {
				document.cookie = "statustray=close"+";expires=-1"+"; path=/";
			}   //alert("before closing the status tray::"+document.cookie)
                
                }
                else
                {
                        if ( browser.indexOf( "Safari" ) != -1 ){
                        window.alert("Great! Thanks for keeping the Status Window open. Feel free to minimize the window if it's in your way. You are now free to enjoy Gogo Inflight Internet.");
                        }
                        else{
                        window.alert("Great! Thanks for keeping the Status Window open.  "+"\nFeel free to minimize the window if it's in your way. "+"\nYou are now free to enjoy Gogo Inflight Internet.");
                        }
                        var scr_width=240;
                        var screenwidth = screen.width - scr_width;
                        var x = screen.width - scr_width;
                        var y = 0;
                        var scr_height = 450;
                        var str = "directories=0,location=0,status=1,scrollbars=0,toolbar=0,menubar=0,width=240,height=450,left="+screenwidth+",screenX="+screenwidth+",screenY=0,top=0";
                        var win_features = "width=" + scr_width + ",height=" + scr_height ;
                        var text = "left=" + x + ",screenX=" + x + ",top=" + y + ", screenY=" + y ;
                        var text2 = "scrollbars=no,resizable=no,status=no,titlebar=no,toolbar=no,directories=no,menubar=no,location=no";
                        var finalStr = win_features+","+text+","+text2;
                        /*window.open(url,"",finalStr)*/
			if(isIE()) {
				document.cookie = "statustray=close"+"; path=/";
			} else {
				document.cookie = "statustray=close"+";expires=-1"+"; path=/";
			}   //alert("before closing the status tray::"+document.cookie)
			window.opener.launchStatusTray('true');//To use the javascript function of gbrowsesplash.js


                }
        }
}



		
		gogo.abp.userInfo.userName = '';
		/*#if ($username)
			gogo.abp.userInfo.userName = '${username}';
		#end*/
	</script>
</body>
</html>