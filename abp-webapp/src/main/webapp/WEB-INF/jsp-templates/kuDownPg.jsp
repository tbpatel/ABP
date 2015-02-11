<!DOCTYPE html>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<html><head><meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<meta charset="UTF-8">
<meta name="viewport" content="width=device-width, initial-scale=1.0, user-scalable=yes, maximum-scale=1.0" />

<title>Gogo is currently not available</title>
<link rel="stylesheet" type="text/css" media="screen" href="/abp/css/errors-styles.css">
<script>
var AIRLINE = '<c:out value="${airlineCode}"/>';
FLIGHTTYPE = '<c:out value="${flightType}"/>';
</script>

<script src="/abp/js/jQuery-v1.10.2.js"></script>
<script src="/abp/js/errorLogic.js"></script>
</head>

<body>
<div id="errorWrap" class="clearfix">
    <div id="errorHead"></div><!-- /errorHead -->
    <div id="errorBody">
        <div id="errorEng" class="errorMssg">
            <h1>Gogo is temporarily unavailable.</h1>
            <p>We're sorry, but Gogo is currently unavailable for one or more of the following reasons:</p>
            <ul>
                <li>The aircraft is switching from one satellite beam to another. If this is the case, you can expect service to return within a few minutes.</li>
                <li>The aircraft is currently flying where satellite coverage is not available. You can check our <a href="#" class="kuMapLink">coverage map</a> for the approximate duration of the interruption.</li>
                <li>The aircraft is below 10,000 feet in altitude or is maneuvering in a way that disrupts the satellite signal. If this is the case, you can expect service when the aircraft is closer to cruising altitude.</li>
            </ul>
            <p>We apologize for any inconvenience.</p>
        </div><!-- /errorEng -->
        
        <div id="errorFr" class="errorMssg">
            <h1>Gogo est momentan&eacute;ment indisponible.</h1>
            <p>Gogo est indisponible pour l'une des raisons suivantes. Veuillez nous en excuser.</p>
            <ul>
                <li>L'avion est en train de changer de faisceau de satellite. Si tel est le cas, le service sera de nouveau op&eacute;rationnel dans quelques minutes.</li>
                <li>L'avion vole dans une zone o&ugrave; la couverture satellite est indisponible. Consultez notre <a href="#" class="kuMapLink">carte de couverture</a> pour conna&#238;tre la dur&#233;e approximative de l'interruption.</li>
                <li>L'avion vole en dessous de 10 000 pieds ou ex&eacute;cute des man&oelig;uvres qui perturbent le signal satellite. Dans ce cas, le service sera de nouveau disponible une fois que l'avion se rapprochera de son altitude de croisi&egrave;re.</li>
            </ul>
            <p>Veuillez nous excuser du d&eacute;sagr&eacute;ment.</p>
        </div><!-- /errorFr -->
        
       
        <div id="errorCaFr" class="errorMssg">
            <h1>Gogo est momentan&eacute;ment indisponible.</h1>
            <p>Gogo est indisponible pour l'une des raisons suivantes. Veuillez nous en excuser.</p>
            <ul>
                <li>L'avion est en train de changer de faisceau de satellite. Si tel est le cas, le service sera de nouveau op&eacute;rationnel dans quelques minutes.</li>
                <li>L'avion vole dans une zone o&ugrave; la couverture satellite est indisponible. Consultez notre <a href="#" class="kuMapLink">carte de couverture</a> pour conna&#238;tre la dur&#233;e approximative de l'interruption.</li>
                <li>L’avion vole en dessous de 10 000 pieds ou ex&eacute;cute des man&oelig;uvres qui perturbent le signal satellite. Dans ce cas, le service sera de nouveau disponible une fois que l'avion se rapprochera de son altitude de croisi&egrave;re.</li>
            </ul>
            <p>Veuillez nous excuser pour ce d&eacute;sagr&eacute;ment.</p>
        </div><!-- /errorCaFr -->
        
        
		<div id=errorEsp class=errorMssg>
        	<h1>Gogo no est&#225; disponible por el momento.</h1>
            <p>Lo sentimos, en este momento Gogo no est&#225; disponible por una o m&#225;s de las siguientes razones.</p>
            <ul>
                <li>La aeronave est&#225; conmutando de un haz satelital a otro. Si este fuera el caso, el servicio se restablecer&#225; una vez que la aeronave se encuentre m&#225;s cercana a la altitud de crucero.</li>
                <li>La aeronave est&#225; volando por una zona donde la cobertura satelital no est&#225; disponible. Para ver la duraci&#243;n aproximada de esta interrupci&#243;n, revise nuestro <a href="#" class=kuMapLink>mapa de cobertura</a>.</li>
				<li>La aeronave se encuentra por debajo de los 10,000 pies de altitud o est&#225; maniobrando de manera que la se&#241;al satelital queda interrumpida. Si este fuera el caso, el servicio se restablecer&#225; una vez que la aeronave se encuentre m&#225;s cercana a la altitud de crucero.</li>
            </ul>
            <p>Lamentamos las molestias que esto le ocasiona.</p>
        </div><!-- /errorEsp -->

        <div id="errorJap" class="errorMssg">
            <h1>Gogo&#12399;&#29694;&#22312;&#12289;&#19968;&#26178;&#30340;&#12395;&#12469;&#12540;&#12499;&#12473;&#12434;&#20572;&#27490;&#12375;&#12390;&#12362;&#12426;&#12414;&#12377;&#12290;</h1>
            <p>&#12372;&#36855;&#24785;&#12434;&#12362;&#25499;&#12369;&#12375;&#12390;&#22823;&#22793;&#30003;&#12375;&#35379;&#12372;&#12374;&#12356;&#12414;&#12379;&#12435;&#12290;&#20197;&#19979;&#12398;&#12356;&#12378;&#12428;&#12363;&#12398;&#29702;&#30001;&#12395;&#12424;&#12426;&#12289;&#29694;&#22312;Gogo&#12398;&#12469;&#12540;&#12499;&#12473;&#12434;&#12372;&#21033;&#29992;&#12356;&#12383;&#12384;&#12367;&#12371;&#12392;&#12364;&#12391;&#12365;&#12414;&#12379;&#12435;&#12290;</p>
            <ul>
                <li>&#33322;&#31354;&#27231;&#12364;&#34907;&#26143;&#12398;&#20999;&#12426;&#26367;&#12360;&#12434;&#34892;&#12387;&#12390;&#12356;&#12427;&#12290;&#12371;&#12398;&#22580;&#21512;&#12289;&#25968;&#20998;&#24460;&#12395;&#12399;&#12372;&#21033;&#29992;&#12434;&#20877;&#38283;&#12375;&#12390;&#12356;&#12383;&#12384;&#12369;&#12414;&#12377;&#12290;</li>
                <li>&#34907;&#26143;&#12398;&#12469;&#12540;&#12499;&#12473;&#12456;&#12522;&#12450;&#22806;&#12434;&#39131;&#34892;&#12375;&#12390;&#12356;&#12427;&#12290;<a href="#" class="kuMapLink">&#12469;&#12540;&#12499;&#12473;&#12456;&#12522;&#12450;&#12510;&#12483;&#12503;</a>&#12363;&#12425;&#12289;&#12469;&#12540;&#12499;&#12473;&#20877;&#38283;&#12414;&#12391;&#12398;&#12362;&#12362;&#12424;&#12381;&#12398;&#26178;&#38291;&#12434;&#12372;&#30906;&#35469;&#12356;&#12383;&#12384;&#12369;&#12414;&#12377;&#12290;</li>
                <li>&#33322;&#31354;&#27231;&#12364;&#39640;&#24230;3,000m&#20197;&#19979;&#12434;&#39131;&#34892;&#12375;&#12390;&#12356;&#12427;&#12363;&#12289;&#25805;&#32294;&#19978;&#12398;&#37117;&#21512;&#12395;&#12424;&#12426;&#34907;&#26143;&#12363;&#12425;&#12398;&#20449;&#21495;&#21463;&#20449;&#12364;&#20013;&#26029;&#12373;&#12428;&#12390;&#12356;&#12427;&#12290;&#12371;&#12398;&#22580;&#21512;&#12289;&#24033;&#33322;&#39640;&#24230;&#12395;&#36817;&#12389;&#12365;&#12414;&#12375;&#12383;&#12425;&#12372;&#21033;&#29992;&#12434;&#20877;&#38283;&#12375;&#12390;&#12356;&#12383;&#12384;&#12369;&#12414;&#12377;&#12290;</li>
            </ul>
            <p>&#12372;&#36855;&#24785;&#12434;&#12362;&#12363;&#12369;&#12375;&#12390;&#12356;&#12414;&#12377;&#12371;&#12392;&#12434;&#12289;&#12362;&#35435;&#12403;&#30003;&#12375;&#19978;&#12370;&#12414;&#12377;&#12290;</p>
        </div><!-- /errorJap -->
		
    </div><!-- /errorBody -->
</div><!-- /errorWrap -->

</body></html>