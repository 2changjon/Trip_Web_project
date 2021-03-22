var width = 0; //너비
var height = 0; //높이
var focused = false, //포커스 유무
ortho = true, //국가
speed = -7e-3,//회전 속도
start = Date.now(), //지구본 회전 시작
corr = 0; //마지막 회전 각도 저장 할 변수

$(document).ready(function() { 
	if(window.matchMedia("screen and (min-width: 1000px)").matches){
	console.clear();
	
	//$('#map').css('')로 브라우저 화면에 맞는 %에 대한 크기를 가져온 뒤 parseInt로 정수로 변환한다
	width = parseInt($('#map').css('width'));
	height = parseInt($('#map').css('height'));
	
	//실수인 width,height의 값을 정수로 변환
	$("#map").width(width)
	$("#map").height(height)
	
	console.log("너비 1000px 이상 현재 :"+width);
	console.log("width :="+ width);
	
	//d3의 크기와 데이터를 가지고 그릴 그림(지구본)를 설정
	var projectionGlobe = d3.geo.orthographic().scale(280)
	    .translate([width * 0.5, height * 0.5]).clipAngle(90);
	//d3의 크기와 데이터를 가지고 그릴 그림(평면지도)를 설정
	var projectionMap = d3.geo.equirectangular().scale(150)
		.translate([width * 0.5, height * 0.5]) ;
		
	//기본적인 화면을 지구본으로
	var projection = projectionGlobe;
	
	//지리적 경로 생성기 설정(d3.geo (Geography))
	var path = d3.geo.path().projection(projection);
	
	//div를 d3를 사용하기 위해 svg로 변환
	var svgMap = d3.select("div#map").append("svg")
		.attr('id', 'mapsvg')
		.attr("overflow", "hidden")
		.attr("width", width)
		.attr("height", height);
	
	var zoneTooltip = d3.select("div#map").append("div").attr("class", "zoneTooltip"),//마우스 옆 나라명
	infoLabel = d3.select("div#map").append("div").attr("class", "infoLabel");//좌측 아래 나라명
	//svg에 경로를 추가 g는 경로의 그룹
	var g = svgMap.append('g');
	/** 모든 전환 후 기능용 스타터 */
	function endall(transition, callback) { 
	  var n = 0; 
	  transition 
	  .each(function() { ++n; }) 
	  .each("end", function() { if (!--n) callback.apply(this, arguments); }); 
	}
	//데이터 불러오기 tsv는 tab으로 데이터 구분
	queue()
	.defer(d3.json, "/resources/geo/countries-110m.json")
	.defer(d3.tsv, "/resources/geo/world-110m-country-names.tsv")//영문 국가명
	.defer(d3.tsv, "/resources/geo/world-korean-country-names.tsv")//한글 국가명
	.await(ready);
	
	//데이터를 가져옴 에러, json, 영문 국가명, 한글 국가명
	function ready(error, world, countryData, koreanData) {
		
		var countryById = {},
		koreanById = {},
		countries = topojson.feature(world, world.objects.countries).features;
	
	 	//id로 영문 국가 추가
		countryData.forEach(function(d) {
	    	countryById[d.id] = d.name;
		});
		
		//id로 한글 국가 추가
		koreanData.forEach(function(d) {
	    	koreanById[d.id] = d.korean_name;
		});
		
	
		//전 세계에 국가 그리기
		var world = g.selectAll("path").data(countries);
		world.enter().append("path")
	  	.attr("class", "mapData")
		.attr("d", path)
		.classed("ortho", ortho = true);
		
		//이벤트 처리
		world.on("mouseover", function(d) {
	    	if (ortho === true) {
	      		infoLabel.text(koreanById[d.id]) //좌측 하단에 출력
	      		.style("display", "inline");
	    	} else {
	      		zoneTooltip.text(koreanById[d.id]) //마우스 옆에 출력
	      		.style("left", (d3.event.pageX + 7) + "px")
	      		.style("top", (d3.event.pageY - 15) + "px")
	      		.style("display", "block");
	    	}
	  	})
	
		.on("mouseout", function(d) {
		    if (ortho === true) {
		      infoLabel.style("display", "none");
		    } else {
		      zoneTooltip.style("display", "none");
		    }
	  	})
		
		.on("mousemove", function() {
		    if (ortho === false) {
		     	zoneTooltip.style("left", (d3.event.pageX + 7) + "px")
		      	.style("top", (d3.event.pageY - 15) + "px");
		    }
	  	})
	  	.on("click", function(d) {
		    if (focused === d) return reset();
			    g.selectAll(".focused").classed("focused", false);
			    d3.select(this).classed("focused", focused = d);
			    infoLabel.text(koreanById[d.id])
	    		.style("display", "inline");
			
			//지구본을 평면지도로 변환
			if (ortho === true) {
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
	    	projection.rotate([λ + corr, -5]);
	    	g.selectAll(".ortho").attr("d", path);
	
	  	});
		//해당 데이터에 초점에 맞춰질 때 여분 데이터 추가
		function focus(d) {
	    	if (focused === d) return reset();
	    	g.selectAll(".focused").classed("focused", false);
	    	d3.select(this).classed("focused", focused = d);
	  	}
		//지도 재설정
		function reset() {
	    	g.selectAll(".focused").classed("focused", focused = false);
	    	infoLabel.style("display", "none");
	    	zoneTooltip.style("display", "none");
			//평면지도를 지구본으로 변환
			projection = projectionGlobe;
	    	path.projection(projection);
	    	g.selectAll("path").transition()
	    	.duration(3000).attr("d", path)
	    	
			.call(endall, function() {
	    	  g.selectAll("path").classed("ortho", ortho = true);
	    	  start = Date.now(); // <- 회전 시작 재설정
	   		 });
		}
	}
	console.log("너비 1000px이상");
	}else{
		console.clear();
		console.log("너비 1000px 이하 현재 :"+$('#map').css('width'));
		
		//$('#map').css('')로 브라우저 화면에 맞는 %에 대한 크기를 가져온 뒤 parseInt로 정수로 변환한다
		width = parseInt($('#map').css('width'));
		height = parseInt($('#map').css('height'));
		
		//실수인 width,height의 값을 정수로 변환
		$("#map").width(width)
		$("#map").height(height)
		
		//d3의 크기와 데이터를 가지고 그릴 그림(지구본)를 설정
		var projectionGlobe = d3.geo.orthographic().scale(70)
		    .translate([width * 0.5, height * 0.5]).clipAngle(90);
		//d3의 크기와 데이터를 가지고 그릴 그림(평면지도)를 설정
		var projectionMap = d3.geo.equirectangular().scale(70)
			.translate([width * 0.5, height * 0.5]) ;
			
		//기본적인 화면을 지구본으로
		var projection = projectionGlobe;
		
		//지리적 경로 생성기 설정(d3.geo (Geography))
		var path = d3.geo.path().projection(projection);
		
		//div를 d3를 사용하기 위해 svg로 변환
		var svgMap = d3.select("div#map").append("svg")
			.attr('id', 'mapsvg')
			.attr("overflow", "hidden")
			.attr("width", width)
			.attr("height", height);
		
		var zoneTooltip = d3.select("div#map").append("div").attr("class", "zoneTooltip"),//마우스 옆 나라명
		infoLabel = d3.select("div#map").append("div").attr("class", "infoLabel");//좌측 아래 나라명
		//svg에 경로를 추가 g는 경로의 그룹
		var g = svgMap.append('g');
		/** 모든 전환 후 기능용 스타터 */
		function endall(transition, callback) { 
		  var n = 0; 
		  transition 
		  .each(function() { ++n; }) 
		  .each("end", function() { if (!--n) callback.apply(this, arguments); }); 
		}
		//데이터 불러오기 tsv는 tab으로 데이터 구분
		queue()
		.defer(d3.json, "/resources/geo/countries-110m.json")
		.defer(d3.tsv, "/resources/geo/world-110m-country-names.tsv")//영문 국가명
		.defer(d3.tsv, "/resources/geo/world-korean-country-names.tsv")//한글 국가명
		.await(ready);
		
		//데이터를 가져옴 에러, json, 영문 국가명, 한글 국가명
		function ready(error, world, countryData, koreanData) {
			
			var countryById = {},
			koreanById = {},
			countries = topojson.feature(world, world.objects.countries).features;
		
		 	//id로 영문 국가 추가
			countryData.forEach(function(d) {
		    	countryById[d.id] = d.name;
			});
			
			//id로 한글 국가 추가
			koreanData.forEach(function(d) {
		    	koreanById[d.id] = d.korean_name;
			});
			
		
			//전 세계에 국가 그리기
			var world = g.selectAll("path").data(countries);
			world.enter().append("path")
		  	.attr("class", "mapData")
			.attr("d", path)
			.classed("ortho", ortho = true);
			
			//이벤트 처리
			world.on("mouseover", function(d) {
		    	if (ortho === true) {
		      		infoLabel.text(koreanById[d.id]) //좌측 하단에 출력
		      		.style("display", "inline");
		    	} else {
		      		zoneTooltip.text(koreanById[d.id]) //마우스 옆에 출력
		      		.style("left", (d3.event.pageX + 7) + "px")
		      		.style("top", (d3.event.pageY - 15) + "px")
		      		.style("display", "block");
		    	}
		  	})
		
			.on("mouseout", function(d) {
			    if (ortho === true) {
			      infoLabel.style("display", "none");
			    } else {
			      zoneTooltip.style("display", "none");
			    }
		  	})
			
			.on("mousemove", function() {
			    if (ortho === false) {
			     	zoneTooltip.style("left", (d3.event.pageX + 7) + "px")
			      	.style("top", (d3.event.pageY - 15) + "px");
			    }
		  	})
		  	.on("click", function(d) {
			    if (focused === d) return reset();
				    g.selectAll(".focused").classed("focused", false);
				    d3.select(this).classed("focused", focused = d);
				    infoLabel.text(koreanById[d.id])
		    		.style("display", "inline");
				
				//지구본을 평면지도로 변환
				if (ortho === true) {
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
		    	projection.rotate([λ + corr, -5]);
		    	g.selectAll(".ortho").attr("d", path);
		
		  	});
			//해당 데이터에 초점에 맞춰질 때 여분 데이터 추가
			function focus(d) {
		    	if (focused === d) return reset();
		    	g.selectAll(".focused").classed("focused", false);
		    	d3.select(this).classed("focused", focused = d);
		  	}
			//지도 재설정
			function reset() {
		    	g.selectAll(".focused").classed("focused", focused = false);
		    	infoLabel.style("display", "none");
		    	zoneTooltip.style("display", "none");
				//평면지도를 지구본으로 변환
				projection = projectionGlobe;
		    	path.projection(projection);
		    	g.selectAll("path").transition()
		    	.duration(3000).attr("d", path)
		    	
				.call(endall, function() {
		    	  g.selectAll("path").classed("ortho", ortho = true);
		    	  start = Date.now(); // <- 회전 시작 재설정
		   		 });
			}
		}//function ready
		console.log("너비 1000px이하");
	}//else
})//$(document).ready(function