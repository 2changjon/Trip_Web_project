var width = 0; //너비
var height = 0; //높이
var focused = false, //포커스 유무
	ortho = true, //국가 띄울떄 지도 구분 true면 지구본 상태
	naming = true,
	speed = -2e-2,//회전 속도
	start = Date.now(), //지구본 회전 시작
	corr = 0; //마지막 회전 각도 저장 할 변수

var svgMap,
	projectionGlobe,
	projectionMap,
	projection,
	path,
	g,
	zoneTooltip,
	infoLabel,
	contents_basic,
	contents_country_name,
	select_country,
	select_country_name,
	country_number_time;

var countryById = {}, //영문 국가명
	koreanById = {}, //한글 국가명
	utcById = {}; //국가별 utc 기준 시간
	/*country_serch; // 국가 검색*/
	
$(document).ready(function() { 
	console.clear();
	//$('#map').css('')로 브라우저 화면에 맞는 %에 대한 크기를 가져온 뒤 parseInt로 정수로 변환한다
	width = parseInt(window.innerWidth);
	height = parseInt(window.innerHeight);
	
	//실수인 width,height의 값을 정수로 변환
	$("#map").width(width);
	$("#map").height(height);

	//console.log("너비 800px 이상 현재 :"+$('#map').css('width'));
	console.log("너비 450px 이상 현재 :"+$('body').css('width'));
	console.log("현재 높이 :"+$('#map').css('height'));
	console.log("width :="+ width);
	
	//정사각형 공간 생성(지도가 들어갈 위치)
	if( height > width ){
		//div를 d3를 사용하기 위해 svg로 변환
		svgMap = d3.select("div#map").append("svg")
			.attr('id', 'globeSvg')
			.attr("overflow", "hidden")
			.attr("width", width*0.8)
			.attr("height", width*0.8);	//정사각형으로 공간을 잡기 위해 너비로 맞춤
			
		//d3의 크기와 데이터를 가지고 그릴 그림(지구본)를 설정
		projectionGlobe = d3.geo.orthographic().scale((width*0.8)*0.5)
		    .translate([(width*0.8) * 0.5, (width*0.8) * 0.5]).clipAngle(90);
		console.log("지구본 scale width:="+ width*0.5);
		
	}else{
		//div를 d3를 사용하기 위해 svg로 변환
		svgMap = d3.select("div#map").append("svg")
			.attr('id', 'globeSvg')
			.attr("overflow", "scroll")
			.attr("width", height*0.8)//정사각형으로 공간을 잡기 위해 높이로 맞춤
			.attr("height", height*0.8);
			
		//d3의 크기와 데이터를 가지고 그릴 그림(지구본)를 설정
		projectionGlobe = d3.geo.orthographic().scale((height*0.8)*0.5)
		    .translate([(height*0.8) * 0.5, (height*0.8) * 0.5]).clipAngle(90);
		console.log("지구본 scale height:="+ height*0.5);
		
	}
	//d3의 크기와 데이터를 가지고 그릴 그림(평면지도)를 설정   201.6
	projectionMap = d3.geo.equirectangular().scale(Math.min((width*0.8)/ Math.PI, (height*0.8)/ Math.PI)) 
		.translate([width * 0.5, height * 0.498046875]) ;
		console.log("평면 scale :="+ width*0.16);
	
	/*var projectionMap = d3.geo.equirectangular().scale(Math.min((width * 0.8)/ Math.PI, (height * 0.8)/ Math.PI)) 
		.translate([width * 0.5, height * 0.4140625]) ;
		console.log("평면 scale :="+ width*0.16);*/
	
	//기본적인 화면을 지구본으로
	projection = projectionGlobe;
	
	//지리적 경로 생성기 설정(d3.geo (Geography))
	path = d3.geo.path().projection(projection);
	
	//마우스 이벤트에 따른 국가명 출력
		zoneTooltip = d3.select("div#map").append("div").attr("class", "zoneTooltip"),//마우스 옆 나라명
		infoLabel = d3.select("div#map").append("div").attr("class", "infoLabel");//좌측 아래 나라명
	
	//사이드바를 열떄 보여주는 국가명
		contents_basic = document.querySelector('.contents .basic'),
		contents_country_name = document.querySelector('.contents .country_name'), 
		select_country = document.querySelector('.select_country_time'),
		select_country_name = document.querySelector('.select_country_time .country_name'),
		country_number_time = document.getElementById('country_number');
	//svg에 경로를 추가 g는 경로의 그룹
	g = svgMap.append('g');
	
	//데이터 불러오기 tsv는 tab으로 데이터 구분
	queue()
	.defer(d3.json, "/resources/geo/world-110m.json")
	.defer(d3.tsv, "/resources/geo/world_country_data.tsv")//영문 국가명 한글 국가명 utc 시간
	.await(ready);
	
	//데이터를 가져옴 에러, json, 영문 국가명, 한글 국가명
	function ready(error, world, countryData) {
		
		var countries = topojson.feature(world, world.objects.countries).features;
	
		//전 세계에 국가 그리기
		var world = g.selectAll("path").data(countries);
		world.enter().append("path")
	  	.attr("class", "mapData")
		.attr("id", function(d) {return d.id})
		.attr("d", path)
		.classed("ortho", ortho = true);
	 	
		countryData.forEach(function(d) {
	    	countryById[d.id] = d.english_name;//id로 영문 국가 추가
			koreanById[d.id] = d.korean_name;//id로 한글 국가 추가
			utcById[d.id] = d.utc_time;//id로 utc 시간 추가
		});
		
		//이벤트 처리
		world.on("mouseover", function(d) {
			    zoneTooltip.text(koreanById[d.id]) //마우스 옆에 출력
	      		.style("left", (d3.event.pageX + 7) + "px")
	      		.style("top", (d3.event.pageY - 15) + "px")
	      		.style("display", "inline");	
	  	})
	
		.on("mouseout", function(d) {
		    if (ortho === true) {
			zoneTooltip.style("display", "none");
			infoLabel.style("display", "none");
		    } else {
			zoneTooltip.style("display", "none");
		    }
	  	})
		
		.on("mousemove", function() {
		     	zoneTooltip.style("left", (d3.event.pageX + 7) + "px")
		      	.style("top", (d3.event.pageY - 15) + "px");
	  	})
	  	.on("click", function(d) {
		    if (focused === d)return reset();
			    g.selectAll(".focused").classed("focused", false);
			    d3.select(this).classed("focused", focused = d);
	      	    
				infoLabel.text(koreanById[d.id]) //좌측 하단에 출력
	      		.style("display", "inline");
				
				/*contents_basic.style.display = 'none';*/
				contents_basic.innerHTML = koreanById[d.id]+'<br>&nbsp;&nbsp;&nbsp;'+countryById[d.id]+'&nbsp;&nbsp;&nbsp;';
	/*			contents_country_name.textContent = koreanById[d.id];
				contents_country_name.style.display = 'block';*/
	      		select_country.style.display = 'block';
				select_country_name.textContent = koreanById[d.id];
				country_number_time.value = utcById[d.id];
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
	 	});
		//타이머를 통해 회전하는 지구본
		d3.timer(function() {
	    	var λ = speed * (Date.now() - start);
	    	projection.rotate([λ + corr, -2]);
	    	g.selectAll(".ortho").attr("d", path);
	
	  	});
		//해당 데이터에 초점에 맞춰질 때 여분 데이터 추가
		function focus(d) {
	    	if (focused === d) return reset(); 
	    	g.selectAll(".focused").classed("focused", false);
	    	d3.select(this).classed("focused", focused = d);
	  	}
	}
})//$(document).ready(function

/** 모든 전환 후 기능용 스타터 */
function endall(transition, callback) { 
  var n = 0; 
  transition 
  .each(function() { ++n; }) 
  .each("end", function() { if (!--n) callback.apply(this, arguments); }); 
}//endall
	
//지도 재설정
function reset() {
	$(".basic_time").removeClass("show");
	var	country_serch = document.getElementById('country_serch');
	country_serch.value = "";
	$('.serch_List').remove();
	contents_basic.innerHTML = "Welcome"+'<br>&nbsp;&nbsp;&nbsp;'+"Earth"+'&nbsp;&nbsp;&nbsp;';
	//지구본용 svg 공간 원으로 변경
	if( height > width ){
		$("#planeSvg").attr("width", width*0.8);
		$("#planeSvg").attr("height", width*0.8);
		console.log("지구본용 변환 height :"+width*0.8);
	}else{
		$("#planeSvg").attr("width", height*0.8);
		$("#planeSvg").attr("height", height*0.8);
		console.log("지구본용 변환 width :"+height*0.8);
	}
	//지구본 css 적용을 위해 id 수정
	$("#planeSvg").attr('id', 'globeSvg');
	
   	g.selectAll(".focused").classed("focused", focused = false);
   	infoLabel.style("display", "none");
   	zoneTooltip.style("display", "none");
	contents_country_name.style.display = 'none';
   	select_country.style.display = 'none';
		
	//평면지도를 지구본으로 변환
	projection = projectionGlobe;
   	path.projection(projection);
   	g.selectAll("path").transition()
   	.duration(3000).attr("d", path)
   	
	.call(endall, function() {
  	  g.selectAll("path").classed("ortho", ortho = true);
   	  start = Date.now(); // <- 회전 시작 재설정
	});
}//reset