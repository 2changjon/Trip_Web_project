window.addEventListener('load', function ticket_serch() {
	
	
	$('#serch_bt').click(function(){
		var departure_Place = document.getElementById('cabinclass').value;	//	출발지
		var arrival_Place = document.getElementById('arrival_Place').value;	//	도착지
		var departure_Date = document.getElementById('departure_Date').value;	//	출발일
		var arrival_Date = document.getElementById('arrival_Date').value;	//	반환일
		
		var flight_Type = document.getElementById('flight_Type').value;	//	여행구분
		var adult = document.getElementById('adult').value;	//	성인
		var teenager = document.getElementById('teenager').value;	//	청소년
		var child = document.getElementById('child').value;	//	어린이
		var baby = document.getElementById('baby').value;	//	유아
		var class_Type = document.getElementById('class_Type').value;	//	좌석
		
		$.ajax({
			url : "/getTicket.do",
			type : "get",
			dataType : "json",
			error : function(err) { 
				console.log("실행중 오류가 발생하였습니다."); 
			},
			data : {
				"departure_Place" : departure_Place,
				"arrival_Place" : arrival_Place,
				"departure_Date" : departure_Date,
				"arrival_Date" : arrival_Date,
				
				"flight_Type" : flight_Type,
				"adult" : adult,
				"teenager" : teenager,
				"child" : child,
				"baby" : baby,
				"class_Type" : class_Type,
			},
			success : function(data) {
				console.log(data);
				console.log("끝");
			}
		})
		
	})//$('.ticket_option').click(function(){
	
})//window.addEventListener('load', function ticket_serch() {