

			var flightTypeLow = FLIGHTTYPE.toLowerCase(),
			airlineLow = AIRLINE.toLowerCase(),
			mapImage,
			mapImg = {
				atg: 'atgMap',
				ku: 'kuMap',
			};

			var twoCharAirline = airlineLow.substr(0,2);

			if(flightTypeLow.indexOf('atg') != -1){
				mapImage = mapImg.atg;
				flightTypeLow = 'atg';
			}
			else if (flightTypeLow.indexOf('ku') != -1){
				mapImage = mapImg.ku;
				flightTypeLow = 'ku';
			}
			else {
				mapImage = mapImg.atg;
				flightTypeLow = 'atg';
			}

	var twoLetterAirline = {
		aal: 'aa',
		aca: 'ac',
		amx: 'am',
		asa: 'as',
		dal: 'dl',
		jal: 'jl',
		fft: 'f9',
		trs: 'fl',
		ual: 'ua',
		awe: 'us',
		vrd: 'vx',
		stc: 'noAirline'
	}

	function domcleanup(){
			$('.errorMssg').each(function(){
				if($(this).css('display') == 'none'){
					$(this).remove();
				}
			});
	}

	$(document).ready(function(){

		if(twoLetterAirline[airlineLow]){
			$('body').addClass(twoLetterAirline[airlineLow] + ' ' + flightTypeLow);

			domcleanup();
		}
		else {
			$('body').addClass('noAirline' + ' ' + flightTypeLow);

			domcleanup();
		}
		
		if(twoLetterAirline[airlineLow] == 'jl') {
			var errorJpDiv = $('#errorJap');
			$('#errorJap').remove();
			$('#errorBody').prepend(errorJpDiv);
		}
	});

	var coverageDOM = '<div id="modalBg""></div>'+
			'<div id="basicModalWrap">' +
        '<div id="basicModal">' +
        '<a id="modalClose" href="#">Close [X]</a>' +
        '<img src="/abp/images/' + mapImage  +'.jpg" alt="Coverage Map"/>' +
        '</div><!-- /basicModal -->' +
        '</div><!-- /basicModalWrap -->';

$(document).ready(function(){

		$('body').prepend(coverageDOM);

		$('.kuMapLink').click(function(){
			$('#basicModalWrap, #modalBg').show();
			return false;
		});

		$('#modalClose').click(function(){
			$('#basicModalWrap, #modalBg').hide();
			return false;
		});
});