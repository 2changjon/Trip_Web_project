window.addEventListener('load', function ticket() {
	
	setToday();
	function setToday(){
		var today = new Date();
		var yyyy =  today.getFullYear();
		//0부터 1월
		var mm =  today.getMonth()+1 > 9 ? today.getMonth()+1 : '0' + (today.getMonth()+1);
		var dd =  today.getDate() > 9 ? today.getDate() : '0' + today.getDate();
		document.getElementById('Departure_Date').value = yyyy+"-"+mm+"-"+dd;
		document.getElementById('Departure_Date').min = yyyy+"-"+mm+"-"+dd;
		document.getElementById('Arrival_Date').value = yyyy+"-"+mm+"-"+(dd+3);
		document.getElementById('Arrival_Date').min = yyyy+"-"+mm+"-"+dd;
	}
	
	$('.ticket_option').click(function(){
		var cabinclass_kr = cabinclass_change(document.getElementById('cabinclass').value);
		var quantity = document.getElementById('quantity').value;
		var childqty = document.getElementById('childqty').value;
		var babyqty = document.getElementById('babyqty').value;
		var classty_kr = classty_change(document.getElementById('classty').value);
		
		var sum_mem = parseInt(quantity)+parseInt(childqty)+parseInt(babyqty);
		var max_mem = 9-sum_mem;
		
		swal.fire({
			title: '현재 티켓 옵션',
  			html: "여행구분 : "+cabinclass_kr
			+"<br>"+"-----탑승인원-----"
			+"<br>"+"성인 : "+quantity+"명"
			+"<br>"+"어린이 : "+childqty+"명"
			+"<br>"+"유아 : "+babyqty+"명"
			+"<br>"+"--- 총 인원 "+sum_mem+"명---"
			+"<br>"+"좌석등급 : "+classty_kr,
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
						'2':'어른 인원',
						'3':'어린이 인원',
						'4':'유아 인원',
						'5':'좌석등급'
					},
					inputValidator: function (value) {
						//여행구분
						if(value==1){
							swal.fire({
								title:"여행구분",
								input: 'radio',
								inputOptions:{
									'1':'편도',
									'2':'왕복'
								},
								inputValidator: function (value) {
									document.getElementById('cabinclass').value = value;
								}
							})
						
						//어른 인원
						}else if(value==2){
							swal.fire({
								title:"어른 인원",
								input: "range",
								inputAttributes: {
    								min: 1,
    								max: max_quanti(max_mem),
    								step: 1
  								},
  								inputValue: parseInt(quantity),
								inputValidator: function (value) {
									document.getElementById('quantity').value = value;
								}
							})
						
						//어린이 인원
						}else if(value==3){
							swal.fire({
								title:"어린이 인원",
								input: "range",
								inputAttributes: {
    								min: 0,
    								max: max_child(max_mem),
    								step: 1
  								},
  								inputValue: parseInt(childqty),
								inputValidator: function (value) {
									document.getElementById('childqty').value = value;
								}
							})
						
						//유아 인원
						}else if(value==4){
							swal.fire({
								title:"유아 인원",
								input: "range",
								inputAttributes: {
    								min: 0,
    								max: max_baby(max_mem),
    								step: 1
  								},
  								inputValue: parseInt(babyqty),
								inputValidator: function (value) {
									document.getElementById('babyqty').value = value;
								}
							})
						
						//좌석등급
						}else if(value==5){
							swal.fire({
								title:"좌석등급",
								input: 'select',
  								inputOptions: {
									"1" : "이코노미",
									"3" : "프리미엄 이코노미",
									"5" : "비지니스",
									"6" : "퍼스트"
								},
								inputValidator: function (value) {
									document.getElementById('classty').value = value;
								}
							})
						}
					}
				})
		  }
		});
		
		
	})//Ticket_options
	
	function cabinclass_change(cabinclass){
		
		if(cabinclass == 1){
			cabinclass = "편도";
		}else if(cabinclass == 2){
			cabinclass = "왕복";
		}
		
		return cabinclass;
	}
	function classty_change(classty){
		
		if(classty == 1){
			classty = "이코노미";
		}else if(classty == 3){
			classty = "프리미엄 이코노미";
		}else if(classty == 5){
			classty = "비지니스";
		}else if(classty == 6){
			classty = "퍼스트";
		}
		return classty;
	}
	function max_quanti(max_mem){
		var quantity = document.getElementById('quantity').value;

		if(max_mem > 0){
			return max_mem;
		}else {
			return parseInt(quantity);						
		}
	}
	function max_child(max_mem){
		var quantity = document.getElementById('quantity').value;
		var childqty = document.getElementById('childqty').value;

		if(max_mem > 0 && max_mem < parseInt(quantity)*2){
			return max_mem;
		}else if( parseInt(quantity)*2 < max_mem){
			return parseInt(quantity)*2;
		}else if(max_mem == 0){
			return parseInt(childqty);						
		}
	}
	function max_baby(max_mem){
		var quantity = document.getElementById('quantity').value;
		var babyqty = document.getElementById('babyqty').value;
		
		if(max_mem > 0 && max_mem < quantity){
			return max_mem;
		}else if(max_mem == 0){
			return parseInt(babyqty);						
		}else {
			return parseInt(quantity);
		}
	}
	
			/*swal.fire({
			title: '변경할 옵션을 선택해주세요',
  			input: 'radio',
			inputOptions:{
				'1':'여행구분',
				'2':'어른 인원',
				'3':'어린이 인원',
				'4':'유아 인원',
				'5':'좌석구분'
			},
			inputValidator: function (value) {
				
			}
			
		});*/
	
	
	
	
})
