var _IE6_processPNG = function(){
	if((BrowserDetect.browser == 'Explorer') && (BrowserDetect.version == 6)){
		var imgs = document.getElementsByTagName('img');
		var inputs = document.getElementsByTagName('input');
		for(var i=0; i<imgs.length; i++){
			var img = imgs[i];
			if(img.src) if(img.src.indexOf('.png') > -1) _global_fixSrcPng(img);
		}
		for(var j=0; j<inputs.length; j++){
			var input = inputs[j];
			if(input.src) if(input.src.indexOf('.png') > -1) _global_fixSrcPng1(input);
		}
		var el = ['div', 'span'];
		for(var index=0; index<el.length; index++){
			var elm = el[index];
			var elmts = document.getElementsByTagName(elm);
			for(var i=0; i<elmts.length; i++){
				var elmt = elmts[i];
				if(elmt.style.backgroundImage.indexOf('.png') > -1) _global_fixBgPng(elmt);
			}
		};
	}
}

var _global_blankGif = '/static/images/shared/blank.gif';

var _global_fixSrcPng = function(img){


	if(img.id != 'clickable'){
	
	img.style.height = img.offsetHeight;
	img.style.width = img.offsetWidth;
	img.style.filter = 'progid:DXImageTransform.Microsoft.AlphaImageLoader(src="' + img.src + '", sizingMethod="scale")';
	img.setAttribute('src', _global_blankGif);
	img.style.backgroundImage = 'none';
	}
}

var _global_fixBgPng = function(div){
	var bgSrc = div.style.backgroundImage;
	bgSrc = bgSrc.indexOf('url(') > -1 ? bgSrc.split('url(')[1].split(')')[0] : bgSrc;
	div.style.filter = 'progid:DXImageTransform.Microsoft.AlphaImageLoader(src="' + bgSrc + '", sizingMethod="scale")';
	div.style.backgroundImage = 'none';
}
var _global_fixSrcPng1 = function(input){
	input.style.height = input.offsetHeight;
	input.style.width = input.offsetWidth;
	input.style.filter = 'progid:DXImageTransform.Microsoft.AlphaImageLoader(src="' + input.src + '", sizingMethod="scale")';
	input.setAttribute('src', _global_blankGif);
	input.style.backgroundImage = 'none';
}

var BrowserDetect = {
	init: function () {
		this.browser = this.searchString(this.dataBrowser) || "An unknown browser";
		this.version = this.searchVersion(navigator.userAgent)
			|| this.searchVersion(navigator.appVersion)
			|| "an unknown version";
		this.OS = this.searchString(this.dataOS) || "an unknown OS";
	},
	searchString: function (data) {
		for (var i=0;i<data.length;i++)	{
			var dataString = data[i].string;
			var dataProp = data[i].prop;
			this.versionSearchString = data[i].versionSearch || data[i].identity;
			if (dataString) {
				if (dataString.indexOf(data[i].subString) != -1)
					return data[i].identity;
			}
			else if (dataProp)
				return data[i].identity;
		}
	},
	searchVersion: function (dataString) {
		var index = dataString.indexOf(this.versionSearchString);
		if (index == -1) return;
		return parseFloat(dataString.substring(index+this.versionSearchString.length+1));
	},
	dataBrowser: [
		{ 	string: navigator.userAgent,
			subString: "OmniWeb",
			versionSearch: "OmniWeb/",
			identity: "OmniWeb"
		},
		{
			string: navigator.vendor,
			subString: "Apple",
			identity: "Safari"
		},
		{
			prop: window.opera,
			identity: "Opera"
		},
		{
			string: navigator.vendor,
			subString: "iCab",
			identity: "iCab"
		},
		{
			string: navigator.vendor,
			subString: "KDE",
			identity: "Konqueror"
		},
		{
			string: navigator.userAgent,
			subString: "Firefox",
			identity: "Firefox"
		},
		{
			string: navigator.vendor,
			subString: "Camino",
			identity: "Camino"
		},
		{		// for newer Netscapes (6+)
			string: navigator.userAgent,
			subString: "Netscape",
			identity: "Netscape"
		},
		{
			string: navigator.userAgent,
			subString: "MSIE",
			identity: "Explorer",
			versionSearch: "MSIE"
		},
		{
			string: navigator.userAgent,
			subString: "Gecko",
			identity: "Mozilla",
			versionSearch: "rv"
		},
		{ 		// for older Netscapes (4-)
			string: navigator.userAgent,
			subString: "Mozilla",
			identity: "Netscape",
			versionSearch: "Mozilla"
		}
	],
	dataOS : [
		{
			string: navigator.platform,
			subString: "Win",
			identity: "Windows"
		},
		{
			string: navigator.platform,
			subString: "Mac",
			identity: "Mac"
		},
		{
			string: navigator.platform,
			subString: "Linux",
			identity: "Linux"
		}
	]

};
BrowserDetect.init();