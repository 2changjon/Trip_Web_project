window.addEventListener('load', function ticket_option() {
	
	var orderDate = "";
	var today = new Date();
	var yyyy =  today.getFullYear();
	//0부터 1월
	var mm =  today.getMonth()+1 > 9 ? today.getMonth()+1 : '0' + (today.getMonth()+1);
	var dd =  today.getDate() > 9 ? today.getDate() : '0' + today.getDate();
	document.getElementById('departure_Date').value = yyyy+"-"+mm+"-"+dd;
	document.getElementById('arrival_Date').value = yyyy+"-"+mm+"-"+dd;
	
	/*출발 달력*/
	$('#departure_Date').datepicker({
		numberOfMonths: 1, //출력 월 개수
		dateFormat:"yy-mm-dd",
		dayNamesMin: ['일', '월', '화', '수', '목', '금', '토'],
		monthNames:['1월','2월','3월','4월','5월','6월','7월','8월','9월','10월','11월','12월'],
		firstDay:1, //주시작일 - 일요일:0 월요일:1
		prevText: '이전 달',
		nextText: '다음 달',
		minDate: 0, // 오늘부터:0
		showMonthAfterYear :true, //년도 - 월 순서
		yearRange: yyyy+':'+(yyyy+1), //년도 제한
		yearSuffix: '년', //년 글자 입력
		onSelect : function(date){
			$("#departure_Date").val(date);
			$('#arrival_Date').datepicker("option", "minDate", date);
		}
	})
	/*도착 달력*/
	$('#arrival_Date').datepicker({
		numberOfMonths: 1,
		dateFormat:"yy-mm-dd",
		dayNamesMin: ['일', '월', '화', '수', '목', '금', '토'],
		monthNames:['1월','2월','3월','4월','5월','6월','7월','8월','9월','10월','11월','12월'],
		firstDay:1,//주시작일 - 일요일:0 월요일:1
		prevText: '이전 달',
		nextText: '다음 달',
		minDate: 0, // 오늘부터:0
		showMonthAfterYear :true, //년도 - 월 순서
		yearRange: yyyy+':'+(yyyy+1), //년도 제한
		yearSuffix: '년', //년 글자 입력
		onSelect : function(date){ //날짜 클릭시 
			$("#arrival_Date").val(date);
			$("#departure_Date").datepicker( "option", "maxDate", date );
		}
	})
	
	$('.ticket_option').click(function(){
		var flight_Type_kr = flight_Type_change(document.getElementById('flight_Type').value);
		var adult = document.getElementById('adult').value;
		var teenager = document.getElementById('teenager').value;
		var child = document.getElementById('child').value;
		var baby = document.getElementById('baby').value;
		var class_Type_kr = class_Type_change(document.getElementById('class_Type').value);
		
		var sum_minority = parseInt(teenager)+parseInt(child)+parseInt(baby); //미성년
		var max_minority = 7-sum_minority //남은 탑승 가능 미성년 수 
		var sum_mem = parseInt(adult)+parseInt(teenager)+parseInt(child)+parseInt(baby); //현재 탑승인원
		var max_mem = 16-sum_mem; 
		swal.fire({
			title: '현재 티켓 옵션',
  			html: "여행구분 : "+flight_Type_kr
			+"<br>"+"-----탑승인원-----"
			+"<br>"+"성인 : "+adult+"명"
			+"<br>"+"청소년 : "+teenager+"명"
			+"<br>"+"어린이 : "+child+"명"
			+"<br>"+"유아 : "+baby+"명"
			+"<br>"+"--- 총 인원 "+sum_mem+"명---"
			+"<br>"+"좌석등급 : "+class_Type_kr,
			confirmButtonColor: '#3085d6',
			confirmButtonText: "변경",
			showCancelButton : true,
			cancelButtonColor: "#d33",
			cancelButtonText : "확인",
			focusCancel:true
		}).then((result) => {
			if (result.value) {
				swal.fire({
					title:"변경할 옵션을 선택해주세요",
					input: 'radio',
					inputOptions:{
						'1':'여행구분',
						'2':'성인 탑승인원',
						'3':'청소년 탑승인원',
						'4':'어린이 탑승인원',
						'5':'유아 탑승인원',
						'6':'좌석등급'
					},
					inputValidator: function (value) {
						//여행구분
						if(value==1){
							swal.fire({
								title:"여행구분",
								input: 'radio',
								inputOptions:{
									'OW':'편도',
									'RT':'왕복'
								},
								inputValue: document.getElementById('flight_Type').value,
								inputValidator: function (value) {
									document.getElementById('flight_Type').value = value;
									if(value === "OW"){
										$(".end_time").addClass("none");
									}else{
										$(".end_time").removeClass().addClass("end_time");
									}
								}
							})
						
						//어른 인원
						}else if(value==2){
							swal.fire({
								title:"성인 탑승인원",
								input: "range",
								inputAttributes: {
    								min: 1,
    								max: max_mem,
    								step: 1
  								},
  								inputValue: parseInt(adult),
								inputValidator: function (value) {
									document.getElementById('adult').value = value;
								}
							})
						
						//청소년 인원
						}else if(value==2){
							swal.fire({
								title:"청소년(만 12~17세) 탑승인원",
								input: "range",
								inputAttributes: {
    								min: 1,
    								max: max_minority,
    								step: 1
  								},
  								inputValue: parseInt(teenager),
								inputValidator: function (value) {
									document.getElementById('teenager').value = value;
								}
							})
						
						//어린이 인원
						}else if(value==4){
							swal.fire({
								title:"어린이(2~11세) 탑승인원",
								input: "range",
								inputAttributes: {
    								min: 0,
    								max: max_minority,
    								step: 1
  								},
  								inputValue: parseInt(child),
								inputValidator: function (value) {
									document.getElementById('child').value = value;
								}
							})
						
						//유아 인원
						}else if(value==5){
							swal.fire({
								title:"유아(2세 미만) 탑승인원",
								input: "range",
								inputAttributes: {
    								min: 0,
    								max: max_minority,
    								step: 1
  								},
  								inputValue: parseInt(baby),
								inputValidator: function (value) {
									document.getElementById('baby').value = value;
								}
							})
						
						//좌석등급
						}else if(value==6){
							swal.fire({
								title:"좌석등급",
								input: 'radio',
  								inputOptions: {
									"Normal" : "이코노미",
									"premium" : "프리미엄 이코노미",
									"business" : "비지니스",
									"first" : "퍼스트"
								},
								inputValue: document.getElementById('class_Type').value,
								inputValidator: function (value) {
									document.getElementById('class_Type').value = value;
								}
							})
						}
					}
				})
		  }
		});
		
		
	})//Ticket_options
	
	function flight_Type_change(flight_Type){
		
		if(flight_Type === "OW"){
			flight_Type = "편도";
		}else if(flight_Type === "RT"){
			flight_Type = "왕복";
		}
		
		return flight_Type;
	}
	function class_Type_change(class_Type){
		
		if(class_Type === "Normal"){
			class_Type = "이코노미";
		}else if(class_Type === "premium"){
			class_Type = "프리미엄 이코노미";
		}else if(class_Type === "business"){
			class_Type = "비지니스";
		}else if(class_Type === "first"){
			class_Type = "퍼스트";
		}
		return class_Type;
	}

})//window.addEventListener('load', function ticket_option() {
