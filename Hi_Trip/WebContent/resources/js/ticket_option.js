window.addEventListener('load', function ticket_option() {
	
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

	//상세 옵션
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
		if(9 < max_mem){
			max_mem = 9;
		}
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

	$("#departure_Place").focusout(function(){
		$("#st_serch_air_port *").remove();//내부요소 삭제
		$("#st_serch_air_port").css("display", "none");
	})
	
	$("#arrival_Place").focusout(function(){
		$("#ed_serch_air_port *").remove();//내부요소 삭제
		$("#ed_serch_air_port").css("display", "none");
	})

})//window.addEventListener('load', function ticket_option() {
	
//공항 검색
function getair_port(a){
	console.clear();
	var value = a.value;
	var name = a.name;
			
	$.ajax({
		url : "/getair_port.do",
		type : "get",
		dataType : "json",
		error : function(err) { 
			console.log("실행중 오류가 발생하였습니다."); 
		},
		data : {
			"keyWord" : value
		},
		success : function(data) {
			var contry_nm;
			var area;
			var li_insert = "";
			var k = 0;
			
			if(data.length === 0){
				if(name === "A"){
					$("#st_serch_air_port").css("display", "none");
					$("#st_serch_air_port *").remove();//내부요소 삭제
				}else{
					$("#ed_serch_air_port").css("display", "none");
					$("#ed_serch_air_port *").remove();//내부요소 삭제
				}
			}else{
			
				/*값을 하나씩 가져움*/
				for(i in data){					
					/*맨 처음은 동일*/
					if(k == 0){
					li_insert+= 
					'<li class="air_port_list_contry">'+'<b>['+data[i].contry_nm+']</b>'+'</li>'+
					'<li class="air_port_list_area">'+'<b>('+data[i].area+')</b>'+'</li>'+
					'<li class="air_port_list" value="'+data[i].iata+'" name="'+name+"-"+k+'" onmousedown="select_air_port(this)">'+data[i].airport+'</li>';
					
					contry_nm = data[i].contry_nm; 
					area = data[i].area;           
					}else{
						/*국가명이 같은 경우*/
						if(contry_nm === data[i].contry_nm){
							/*지역명이 같은 경우*/
							if(area === data[i].area ){
								li_insert+=
								'<li class="air_port_list" value="'+data[i].iata+'" name="'+name+"-"+k+'" onmousedown="select_air_port(this)">'+data[i].airport+'</li>';
							/*지역명이 다른 경우*/
							}else{
								li_insert+=
								'<li class="air_port_list_area">'+'<b>('+data[i].area+')</b>'+'</li>'+
								'<li class="air_port_list" value="'+data[i].iata+'" name="'+name+"-"+k+'" onmousedown="select_air_port(this)">'+data[i].airport+'</li>';
								area = data[i].area;
							}
						/*국가명이 다른경우*/
						}else{
							li_insert+=
							'<li class="air_port_list_contry">'+'<b>['+data[i].contry_nm+']</b>'+'</li>'+
							'<li class="air_port_list_area">'+'<b>('+data[i].area+')</b>'+'</li>'+
							'<li class="air_port_list" value="'+data[i].iata+'" name="'+name+"-"+k+'" onmousedown="select_air_port(this)">'+data[i].airport+'</li>';
							contry_nm = data[i].contry_nm;
							area = data[i].area;		
						}
					}//if(k == 0)
					k++;
				}//for(i in data)
				
				/*1: 출발 2:도착*/
				if(name === "A"){
					$("#st_serch_air_port *").remove();
					$("#st_serch_air_port").append(li_insert);
					if(0 < data.length){
						$("#st_serch_air_port").css("display", "block");
					}
				}else{
					$("#ed_serch_air_port *").remove();//내부요소 삭제
					$("#ed_serch_air_port").append(li_insert);
					if(0 < data.length){
						$("#ed_serch_air_port").css("display", "block");
					}
				}//if(name === "1")else
			}//if(data.length === 0)else
		}//success : function(data)
	})
}//function getair_port(a)


function select_air_port(a){
	var value = a.getAttribute("value");
	var name = a.getAttribute("name").substring(0,1);
	var text = a.innerText;
	/*1: 출발 2:도착*/
	if(name === "A"){
		console.log(name);
		$("#departure_Place").val(value);
		$("#st_serch_air_port *").remove();//내부요소 삭제
		$("#st_serch_air_port").css("display", "none");
	}else{
		$("#arrival_Place").val(value);
		$("#ed_serch_air_port *").remove();//내부요소 삭제
		$("#ed_serch_air_port").css("display", "none");
	}
	
}//function select_air_port(a)

