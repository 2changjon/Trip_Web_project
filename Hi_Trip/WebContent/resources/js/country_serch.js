var eng = /^[a-zA-Z]*$/;
 
function country_serch_focus(){
	var	country_serch = document.getElementById('country_serch');
	var serch_list_area = document.getElementById("serch_list_area");
	
	if( !country_serch.value ){
		console.log("country_serch : null check");
		$('.serch_List').remove();
	}else{
		console.log("country_serch : "+country_serch.value);
		console.log("keyup");
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
				console.log("결과 갯수 : "+data.length);
				console.log("첫번째 결과 : "+data[0]);
				/*for-of 문을 사용해 arraylist의 요소값을 하나 하나 가져온다*/
				for(var i of data){
					/*$('#serch_list_area').append( "<li class='serch_List'">+i+"</li>");*/
					
					var li = document.createElement("li");
					li.className = 'serch_List'
					li.appendChild(document.createTextNode(i));
					serch_list_area.appendChild(li);
				}
				console.log("끝");
			}
		})
		map_change(country_serch.value); //지도변경
	}
}
function map_change(country_serch) {
	/*	Javascript의 for-in문을 사용해 key를 뽑아낼 수 있다.
    	key 변수에는 obj가 가진 key가 하나씩 들어온다. */


	if(eng.test(country_serch)){
		for (var key in countryById) {
			
			if(country_serch.toUpperCase() === countryById[key].toUpperCase()){	
				console.log("동일 국가 있음");
				console.log("key "+key);	
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
							console.log("height ///if height < 600 // projectionMap scale:"+width*0.16);
						}else{
							projectionMap = d3.geo.equirectangular().scale(Math.min((width*0.8)/ Math.PI, (height*0.8)/ Math.PI)) 
							.translate([width * 0.5, height * 0.498046875]) ;
							console.log("height /// else // projectionMap scale:"+Math.min((width*0.8)/ Math.PI, (height*0.8)/ Math.PI));
						}
					}else{
						$("#globeSvg").attr("width", width);
						$("#globeSvg").attr("height", height);
						console.log(" 평면지도로 변환 width :"+width);
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
			
			if(country_serch === koreanById[key]){	
				console.log("동일 국가 있음");
				console.log("key "+key);	
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
							console.log("height ///if height < 600 // projectionMap scale:"+width*0.16);
						}else{
							projectionMap = d3.geo.equirectangular().scale(Math.min((width*0.8)/ Math.PI, (height*0.8)/ Math.PI)) 
							.translate([width * 0.5, height * 0.498046875]) ;
							console.log("height /// else // projectionMap scale:"+Math.min((width*0.8)/ Math.PI, (height*0.8)/ Math.PI));
						}
					}else{
						$("#globeSvg").attr("width", width);
						$("#globeSvg").attr("height", height);
						console.log(" 평면지도로 변환 width :"+width);
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