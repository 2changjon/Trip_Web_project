window.addEventListener('load', function ticket_serch() {
	$('#serch_bt').click(function(){
		var departure_Place = document.getElementById('departure_Place').value;	//	출발지
		var arrival_Place = document.getElementById('arrival_Place').value;	//	도착지
		
		if(departure_Place === "" && arrival_Place === ""){
			swal.fire("도착지, 출발지 미입력");
		}else{
			console.clear();
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
				timeout : 180000,
				error : function(err) {
					console.log(err); 
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
					if(data.length === 0){
						console.log("항공편 없음"+data.length);	
						swal.fire("검색된 항공편 없음","다른 조건으로 검색을 시도해 주세요");
					}else{
						console.log(data);
						$(".contents.ticket").addClass("result");
						console.log("끝");
					}
				},
				beforeSend:function(){ //통신중
					$(".loading").addClass("show");
					$(".contents.ticket").addClass("loading");
					$(".ticket_area").addClass("drop");
				},
				complete:function(){ //통신 완료시
					$(".loading").removeClass("show");
					$(".contents.ticket").removeClass("loading");
					$(".ticket_area").removeClass("drop");
				}
			})//ajax
		}//else
	})//$('#serch_bt').click(function(){
})//window.addEventListener('load', function ticket_serch() {