init();
/*UTC*/
	//한국시간
	function getKoranTime() {
		const days = document.getElementById("korea_day");
		const times = document.getElementById("korea_time");
		var now = new Date();
		var tz = now.getTime() + (now.getTimezoneOffset() * 60000) + (9 * 3600000);
		now.setTime(tz);
		
		var day =
			leadingZeros(now.getFullYear(), 4) + '-' +
			leadingZeros(now.getMonth() + 1, 2) + '-' +
			leadingZeros(now.getDate(), 2);
		
		var time =	
			leadingZeros(now.getHours(), 2) + ':' +
			leadingZeros(now.getMinutes(), 2) + ':' +
			leadingZeros(now.getSeconds(), 2);
		
		days.innerHTML = day;
		times.innerHTML = time;
	}
	//국가 시간
	function getCountryTime() {
		var country_utc_time = parseFloat(document.getElementById("country_number").value);
	
		//console.log(country_utc_time);
		
		const days = document.getElementById("country_day");
		const times = document.getElementById("country_time");
		var now = new Date();
		var tz = now.getTime() + (now.getTimezoneOffset() * 60000) + (country_utc_time * 3600000);
		now.setTime(tz);
		
		var day =
			leadingZeros(now.getFullYear(), 4) + '-' +
			leadingZeros(now.getMonth() + 1, 2) + '-' +
			leadingZeros(now.getDate(), 2);
		
		var time =		
			leadingZeros(now.getHours(), 2) + ':' +
			leadingZeros(now.getMinutes(), 2) + ':' +
			leadingZeros(now.getSeconds(), 2);
		
		days.innerHTML = day;
		times.innerHTML = time;
	}
	 
	function leadingZeros(n, digits) {
		var zero = '';
		n = n.toString();
	
		if (n.length < digits) {
	  		for (i = 0; i < digits - n.length; i++)
		    zero += '0';
		}
		
		return zero + n;
	}
	
	function init(){
		setInterval(getKoranTime, 1000);
		setInterval(getCountryTime, 1000);
	}