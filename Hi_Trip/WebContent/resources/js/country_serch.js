var eng = /^[a-zA-Z]*$/;
var country_datas;

function country_serch_focus(){
	var	country_serch = document.getElementById('country_serch');
	var serch_list_area = document.getElementById("serch_list_area");

	if( !country_serch.value ){
/*		console.log("country_serch : null check");*/
		$('.serch_List').remove();
	}else{
/*		console.log("country_serch : "+country_serch.value);
		console.log("keyup");*/
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
				$('.serch_List').remove();
/*				console.log("결과 갯수 : "+data.length);
				console.log("첫번째 결과 : "+data[0]);*/
				/*for-of 문을 사용해 arraylist의 요소값을 하나 하나 가져온다*/
				for(var i of data){
					var li = document.createElement("li");
					li.className = 'serch_List'
					li.appendChild(document.createTextNode(i));
					serch_list_area.appendChild(li);
				}
/*				console.log("끝");*/
			}
		})
	}
}

function map_change() {
	//console.clear();
	var	country_nm = document.getElementById('country_serch').value;
	
	//console.log("----------------");
		
	$.ajax({
			url : "/getcountry_Data.do",
			type : "get",
			dataType : "json",
			error : function(err) {
				console.log("실행중 오류가 발생하였습니다."); 
			},
			data : {
				"country_nm" : country_nm
			},
			success : function(data) {
				/*for (var i in data) {
					console.log(i);	
				}*/
				//console.log("----------");
				//console.log(data);
				//console.log("----------");
				if(JSON.stringify(data) === '{}'){
					swal.fire("<b style="+"color:red"+">"+"검색한 국가"+"</b>"+"는 없습니다");
				}else{
					if(data["travel_prohibited"].length > 0){
						if(data["travel_prohibited"][0].hasOwnProperty('banPartial')){//값유무 확인
							//console.log("travel_prohibited if :"+data["travel_prohibited"][0].banPartial);
							swal.fire({
								title: "<b style="+"color:red"+">"+data["travel_prohibited"][0].banPartial+"</b>", 
								text: data["travel_prohibited"][0].banNote,
								width: 600,
								imageUrl: data["travel_prohibited"][0].imgUrl2,
								imageWidth: 600,
								imageHeight: 300
								});	
						}else{
							//console.log("travel_prohibited else "+data["travel_prohibited"][0].ban);
							swal.fire({
								title: "<b style="+"color:red"+">"+data["travel_prohibited"][0].ban+"</b>", 
								text: data["travel_prohibited"][0].banNote,
								width: 600,
								imageUrl: data["travel_prohibited"][0].imgUrl2,
								imageWidth: 600,
								imageHeight: 300
							});
						}
					}
					//console.log("data[travel_alert].length ::"+data["travel_alert"].length);
					if(data["travel_alert"].length > 0){
						if(data["travel_alert"][0].hasOwnProperty('limitaPartial')){
							/*console.log("travel_alert if "+data["travel_alert"][0].limitaPartial);*/
							swal.fire("<b style="+"color:red"+">"+data["travel_alert"][0].limitaPartial+"</b>", data["travel_alert"][0].limitaNote,"error");
						}
					}
					console.log("data[special_travel].length ::"+data["special_travel"].length);
					if(data["special_travel"].length > 0){
						if(data["special_travel"][0].hasOwnProperty('splimit')){
							//console.log("special_travel if "+data["special_travel"][0].splimit);
							swal.fire("<b style="+"color:red"+">"+data["special_travel"][0].splimit+"</b>", data["special_travel"][0].splimitNote,"error");
						}else{
							//console.log("special_travel else "+data["special_travel"][0].splimitPartial);
							swal.fire("<b style="+"color:red"+">"+data["special_travel"][0].splimitPartial+"</b>", data["special_travel"][0].splimitNote,"error");
						}
					}
						/*console.log("accident_type_news"+JSON.stringify(data["accident_type_news"]));*/
						/*console.log(data["dangerous_News"].length+" "+"dangerous_News"+data["dangerous_News"]);
						console.log(data["local_contact_news"].length+" "+"local_contact_news"+data["local_contact_news"]);
						console.log(data["safety_notice_news"].length+" "+"safety_notice_news"+data["safety_notice_news"]);
						console.log(data["special_travel"].length+" "+"special_travel"+data["special_travel"]);
						console.log(data["travel_alert"].length+" "+"travel_alert"+data["travel_alert"]);
						console.log(data["travel_prohibited"].length+" "+"travel_prohibited"+data["travel_prohibited"]);
						
						console.log(country_datas["local_contact_news"].length+" "+"dddd"+JSON.stringify(country_datas["local_contact_news"]));*/
					country_datas = data;
					//console.log("map_change 끝");
					$.ajax({
						url : "/insert_country_serch.do",
						type : "get",
						dataType : "json",
						timeout : 60000,
						error : function(err) {
							console.log(err); 
						},
						data : {
							country_nm :country_nm
						},
						success : function(data) {
							if(data){
								console.log("완료");
							}else{
								console.log("실패");
							}	
						}	
					})//url : "/insert_country_serch.do"
				}//if(JSON.stringify(data) === '{}') else
			}
		})//url : "/getcountry_Data.do"
		
		
	
	/*	Javascript의 for-in문을 사용해 key를 뽑아낼 수 있다.
    	key 변수에는 obj가 가진 key가 하나씩 들어온다. */
	if(eng.test(country_nm)){
		for (var key in countryById) {
			
			if(country_nm.toUpperCase() === countryById[key].toUpperCase()){	
				/*console.log("동일 국 있음");
				console.log("key "+key);	*/
				    g.selectAll(".focused").classed("focused", false);
				    d3.select("[id='"+key+"']").classed("focused", focused = key);
	
					infoLabel.text(koreanById[key]) //좌측 하단에 출력
		      		.style("display", "inline");
	
					contents_basic.innerHTML = koreanById[key]+'<br>&nbsp;&nbsp;&nbsp;'+countryById[key]+'&nbsp;&nbsp;&nbsp;';
		      		select_country.style.display = 'block';
					select_country_name.textContent = koreanById[key];
					country_number_time.value = utcById[key];
					$(".basic_time").addClass("show");
					
				//지구본을 평면지도로 변환 및 svg 공간크기 변경 
				if (ortho === true) {
					//평명지도용 svg 공간 직사각형으로 변경
					if( height > width ){
						$("#globeSvg").attr("width", width);
						$("#globeSvg").attr("height", height*0.8);
						console.log(" 평면지도로 변환 height :"+height);
						if(height < 600){
							projectionMap = d3.geo.equirectangular().scale(width*0.16) 
							.translate([width * 0.5, (height*0.8) * 0.5]) ;
							/*console.log("height ///if height < 600 // projectionMap scale:"+width*0.16);*/
						}else{
							projectionMap = d3.geo.equirectangular().scale(Math.min((width*0.8)/ Math.PI, (height*0.8)/ Math.PI)) 
							.translate([width * 0.5, height * 0.498046875]) ;
							/*console.log("height /// else // projectionMap scale:"+Math.min((width*0.8)/ Math.PI, (height*0.8)/ Math.PI));*/
						}
					}else{
						$("#globeSvg").attr("width", width);
						$("#globeSvg").attr("height", height);
						/*console.log(" 평면지도로 변환 width :"+width);*/
					}
	
					//평면지도 css 적용을 위해 id 수정
					$("#globeSvg").attr('id', 'planeSvg');
	
					corr = projection.rotate()[0]; // <- 마지막 회전 각도 저장     
		      		g.selectAll(".ortho").classed("ortho", ortho = false);
		      		projection = projectionMap;
		      		path.projection(projection);
		      		g.selectAll("path").transition().duration(3000).attr("d", path);
		    	}
			}//if
		}//for
	}else{
		for (var key in koreanById) {
			
			if(country_nm === koreanById[key]){	
				/*console.log("동일 국가 있음");
				console.log("key "+key);	*/
				    g.selectAll(".focused").classed("focused", false);
				    d3.select("[id='"+key+"']").classed("focused", focused = key);
	
					infoLabel.text(koreanById[key]) //좌측 하단에 출력
		      		.style("display", "inline");
	
					contents_basic.innerHTML = koreanById[key]+'<br>&nbsp;&nbsp;&nbsp;'+countryById[key]+'&nbsp;&nbsp;&nbsp;';
		      		select_country.style.display = 'block';
					select_country_name.textContent = koreanById[key];
					country_number_time.value = utcById[key];
					$(".basic_time").addClass("show");
					
				//지구본을 평면지도로 변환 및 svg 공간크기 변경 
				if (ortho === true) {
					//평명지도용 svg 공간 직사각형으로 변경
					if( height > width ){
						$("#globeSvg").attr("width", width);
						$("#globeSvg").attr("height", height*0.8);
						/*console.log(" 평면지도로 변환 height :"+height);*/
						if(height < 600){
							projectionMap = d3.geo.equirectangular().scale(width*0.16) 
							.translate([width * 0.5, (height*0.8) * 0.5]) ;
							/*console.log("height ///if height < 600 // projectionMap scale:"+width*0.16);*/
						}else{
							projectionMap = d3.geo.equirectangular().scale(Math.min((width*0.8)/ Math.PI, (height*0.8)/ Math.PI)) 
							.translate([width * 0.5, height * 0.498046875]) ;
							/*console.log("height /// else // projectionMap scale:"+Math.min((width*0.8)/ Math.PI, (height*0.8)/ Math.PI));*/
						}
					}else{
						$("#globeSvg").attr("width", width);
						$("#globeSvg").attr("height", height);
						/*console.log(" 평면지도로 변환 width :"+width);*/
					}
	
					//평면지도 css 적용을 위해 id 수정
					$("#globeSvg").attr('id', 'planeSvg');
	
					corr = projection.rotate()[0]; // <- 마지막 회전 각도 저장     
		      		g.selectAll(".ortho").classed("ortho", ortho = false);
		      		projection = projectionMap;
		      		path.projection(projection);
		      		g.selectAll("path").transition().duration(3000).attr("d", path);
					
					
		    	}
			}//if
		}//for
	}//if(영문체크)
	
			
}