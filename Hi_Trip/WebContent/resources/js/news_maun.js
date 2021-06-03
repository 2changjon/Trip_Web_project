//상세 메뉴눌렀을시 안에 들어갈 데이터
window.addEventListener('load', function news_data() {

	/*console.log("accident_type_news"+JSON.stringify(country_datas["accident_type_news"]));
	console.log(country_datas["dangerous_News"].length+" "+"dangerous_News"+country_datas["dangerous_News"]);
	console.log(country_datas["local_contact_news"].length+" "+"local_contact_news"+country_datas["local_contact_news"]);
	console.log(country_datas["safety_notice_news"].length+" "+"safety_notice_news"+country_datas["safety_notice_news"]);
	console.log(country_datas["special_travel"].length+" "+"special_travel"+country_datas["special_travel"]);
	console.log(country_datas["travel_alert"].length+" "+"travel_alert"+country_datas["travel_alert"]);
	console.log(country_datas["travel_prohibited"].length+" "+"travel_prohibited"+country_datas["travel_prohibited"]);*/
	news_title= "-뉴스 제목이 들어갈 공간-"
	$('#dangerous').click(function go_dangerous(){
		if(focused != false){
		console.log(country_datas["dangerous_News"].length+" "+"dangerous_News"+country_datas["dangerous_News"]);
			if(country_datas["dangerous_News"].length > 0){
				/*console.log(country_datas["dangerous_News"][0]);	*/
				$('.title').text($('.available a').text());
				$('.news_title').text(country_datas["dangerous_News"][0].title);
				$('.content').html(country_datas["dangerous_News"][0].html_origin_cn);
			}else{
				$('#dangerous').removeClass("available");
				$('#contents_area').removeClass().addClass("contents");
				
				swal.fire("<b style="+"color:red"+">"+"데이터가 없습니다"+"</b>");
			}
		}
	});
	
	$('#safety').click(function go_safety(){
		if(focused != false){
		console.log(country_datas["safety_notice_news"].length+" "+"safety_notice_news"+country_datas["safety_notice_news"]);
			if(country_datas["safety_notice_news"].length > 0){	
				/*console.log(country_datas["safety_notice_news"][0]);*/
				$('.title').text($('.available a').text());
				$('.news_title').text(country_datas["safety_notice_news"][0].title);
				$('.content').html(country_datas["safety_notice_news"][0].txt_origin_cn);
			}else{	
				$('#safety').removeClass("available");
				$('#contents_area').removeClass().addClass("contents");
				
				swal.fire("<b style="+"color:red"+">"+"데이터가 없습니다"+"</b>");
			}
		}
	});
	
	$('#accident').click(function go_accident(){
		if(focused != false){
		console.log(country_datas["accident_type_news"].length+" "+"accident_type_news"+country_datas["accident_type_news"]);
			if(country_datas["accident_type_news"].length > 0){	
			/*	console.log(country_datas["accident_type_news"][0]);	*/	
				$('.title').text($('.available a').text());
				$('.news_title').text("");
				$('.content').html(country_datas["accident_type_news"][0].news);
			}else{	
				$('#accident').removeClass("available");
				$('#contents_area').removeClass().addClass("contents");
				
				swal.fire("<b style="+"color:red"+">"+"데이터가 없습니다"+"</b>");
			}
		}
	});
	
	$('#contact').click(function go_contact(){
		if(focused != false){
		console.log(country_datas["local_contact_news"].length+" "+"local_contact_news"+country_datas["local_contact_news"]);
			if(country_datas["local_contact_news"].length > 0){
				/*console.log(country_datas["local_contact_news"][0]);*/
				$('.title').text($('.available a').text());
				$('.news_title').text("");
				$('.content').html(country_datas["local_contact_news"][0].contact_remark);
			}else{
				$('#accident').removeClass("available");
				$('#contents_area').removeClass().addClass("contents");
				
				swal.fire("<b style="+"color:red"+">"+"데이터가 없습니다"+"</b>");
			}
		}
	});
	
});