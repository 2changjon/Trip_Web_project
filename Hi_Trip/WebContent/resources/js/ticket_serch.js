window.addEventListener('load', function ticket_serch() {
	
	
	$('#serch_bt').click(function(){
		var departure_Place = document.getElementById('cabinclass').value;	//	출발지
		var arrival_Place = document.getElementById('arrival_Place').value;	//	도착지
		var departure_Date = document.getElementById('departure_Date').value;	//	출발일
		var arrival_Date = document.getElementById('arrival_Date').value;	//	반환일
		var flightType = document.getElementById('cabinclass').value;	//	여행구분
		var quantity = document.getElementById('quantity').value;	//	성인
		var childqty = document.getElementById('childqty').value;	//	어린이
		var babyqty = document.getElementById('babyqty').value;	//	유아
		var classty = document.getElementById('classty').value;	//	좌석
		
		$.ajax({
			url : "/serch_List.do",
			type : "get",
			dataType : "json",
			error : function(err) { 
				console.log("실행중 오류가 발생하였습니다."); 
			},
			data : {
				"keyWord" : country_serch.value
			},
			success : function(data) {
				console.log("끝");
			}
		})
		
	})//$('.ticket_option').click(function(){
	
})//window.addEventListener('load', function ticket_serch() {