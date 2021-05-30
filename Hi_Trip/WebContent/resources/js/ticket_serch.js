window.addEventListener('load', function ticket_serch() {
	$('#serch_bt').click(function(){
		var departure_Place = document.getElementById('departure_Place').value;	//	출발지
		var arrival_Place = document.getElementById('arrival_Place').value;	//	도착지
		var departure_Date = document.getElementById('departure_Date').value;	//	출발일
		var arrival_Date = document.getElementById('arrival_Date').value;	//	반환일

		console.log(departure_Date);
		console.log(arrival_Date);
		
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
			timeout : 15000000,
			error : function(err) { 
				swal.fire("검색중 오류가 발생했습니다");
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
				if(data == null){
					swal.fire("검색된 항공편 없음","다른 조건으로 검색을 시도해 주세요");
				}
				console.log(data);
				console.log("끝");
			},
			beforeSend:function(){
				$(".loading").addClass("show");
				$(".ticket_area").addClass("drop");
			},
			complete:function(){
				$(".loading").removeClass("show");
				$(".ticket_area").removeClass("drop");
			}
		})
		
	})//$('#serch_bt').click(function(){
	
})//window.addEventListener('load', function ticket_serch() {