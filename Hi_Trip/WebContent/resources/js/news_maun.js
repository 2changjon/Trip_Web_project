//상세 메뉴눌렀을시 안에 들어갈 데이터
window.addEventListener('load', function() {
	
	/*console.log("accident_type_news"+JSON.stringify(country_datas["accident_type_news"]));
	console.log(country_datas["dangerous_News"].length+" "+"dangerous_News"+country_datas["dangerous_News"]);
	console.log(country_datas["local_contact_news"].length+" "+"local_contact_news"+country_datas["local_contact_news"]);
	console.log(country_datas["safety_notice_news"].length+" "+"safety_notice_news"+country_datas["safety_notice_news"]);
	console.log(country_datas["special_travel"].length+" "+"special_travel"+country_datas["special_travel"]);
	console.log(country_datas["travel_alert"].length+" "+"travel_alert"+country_datas["travel_alert"]);
	console.log(country_datas["travel_prohibited"].length+" "+"travel_prohibited"+country_datas["travel_prohibited"]);*/
news_title= "-뉴스 제목이 들어갈 공간-"
	$('#dangerous').click(function(){
		console.log(country_datas["dangerous_News"].length+" "+"dangerous_News"+country_datas["dangerous_News"]);
		if(country_datas["dangerous_News"].length > 0){
			console.log(country_datas["dangerous_News"][0]);	
			$('.title').text($('.available a').text());
			$('.news_title').text(country_datas["dangerous_News"][0].title);
			$('.content').html(country_datas["dangerous_News"][0].html_origin_cn);
		}else{
			swal.fire("데이터가 없습니다","question");
		}
	});
	
	$('#safety').click(function(){
		console.log(country_datas["safety_notice_news"].length+" "+"safety_notice_news"+country_datas["safety_notice_news"]);
		if(country_datas["safety_notice_news"].length > 0){	
			console.log(country_datas["safety_notice_news"][0]);
			$('.title').text($('.available a').text());
			$('.news_title').text(news_title);
		}else{	
			swal.fire("데이터가 없습니다","question");
		}
	});
	
	$('#accident').click(function(){
		console.log(country_datas["accident_type_news"].length+" "+"accident_type_news"+country_datas["accident_type_news"]);
		if(country_datas["accident_type_news"].length > 0){	
			console.log(country_datas["accident_type_news"][0]);		
			$('.title').text($('.available a').text());
			$('.news_title').text(news_title);
		}else{	
			swal.fire("데이터가 없습니다","question");
		}
	});
	
	$('#contact').click(function(){
		console.log(country_datas["local_contact_news"].length+" "+"local_contact_news"+country_datas["local_contact_news"]);
		if(country_datas["local_contact_news"].length > 0){
			console.log(country_datas["local_contact_news"][0]);
			$('.title').text($('.available a').text());
			$('.news_title').text(news_title);
		}else{
			swal.fire("데이터가 없습니다","question");
		}
	});
	
});