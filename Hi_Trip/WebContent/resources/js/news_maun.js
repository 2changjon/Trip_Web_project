var manual_btn; //뉴스 메뉴 구분을 위한 함수
var dangerous_length; //위험 뉴스 개수
var safety_length; //안전 뉴스 개수
var accident_length; //사건사고 뉴스 개수
var local_length; //현재 연락처 뉴스 개수
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
			dangerous_length = parseInt(country_datas["dangerous_News"].length);
		/*console.log(dangerous_length+" "+"dangerous_News"+country_datas["dangerous_News"]);*/
			if(dangerous_length > 0){
				$(".list_btn_area *").remove();//내부요소 삭제
				manual_btn=1;
				/*console.log(country_datas["dangerous_News"][0]);	*/
				$('.title').text($('.available a').text());
				$('.news_title').text(country_datas["dangerous_News"][0].title);
				$('.content').html(country_datas["dangerous_News"][0].html_origin_cn.replace(/font-size:/gi,"").replace(/line-height:/gi,"").replace(/https:\/\/www.0404.go.kr/gi,"").replace(/src="/gi,'src="https://www.0404.go.kr').replace(/href="/gi,'href="https://www.0404.go.kr'));
				
				var list_btn='<div class="list_btn" onmousedown="list_btn_click(this)" data-value="0">&#9679;</div>';
								
				for(var i = 1; i<dangerous_length; i++){
					list_btn+=
					'<div class="list_btn" onmousedown="list_btn_click(this)" data-value="'+i+'">&#9675;</div>'
				}
				
				$(".list_btn_area").append(list_btn);
				
			}else{
				$('#dangerous').removeClass("available");
				$('#contents_area').removeClass().addClass("contents");
				
				swal.fire("<b style="+"color:red"+">"+"데이터가 없습니다"+"</b>");
			}
		}
	});
	
	$('#safety').click(function go_safety(){
		if(focused != false){
			safety_length = parseInt(country_datas["safety_notice_news"].length);
		/*console.log(safety_length+" "+"safety_notice_news"+country_datas["safety_notice_news"]);*/
			if(safety_length > 0){
				$(".list_btn_area *").remove();//내부요소 삭제
				manual_btn=2;
				/*console.log(country_datas["safety_notice_news"][0]);*/
				$('.title').text($('.available a').text());
				$('.news_title').text(country_datas["safety_notice_news"][0].title);
				$('.content').html(country_datas["safety_notice_news"][0].txt_origin_cn.replace(/font-size:/gi,"").replace(/line-height:/gi,"").replace(/https:\/\/www.0404.go.kr/gi,"").replace(/src="/gi,'src="https://www.0404.go.kr').replace(/href="/gi,'href="https://www.0404.go.kr'));
				
				var list_btn='<div class="list_btn" onmousedown="list_btn_click(this)" data-value="0">&#9679;</div>';
								
				for(var i = 1; i<safety_length; i++){
					list_btn+=
					'<div class="list_btn" onmousedown="list_btn_click(this)" data-value="'+i+'">&#9675;</div>'
				}
				
				$(".list_btn_area").append(list_btn);
				
			}else{	
				$('#safety').removeClass("available");
				$('#contents_area').removeClass().addClass("contents");
				
				swal.fire("<b style="+"color:red"+">"+"데이터가 없습니다"+"</b>");
			}
		}
	});
	
	$('#accident').click(function go_accident(){
		if(focused != false){
			accident_length = parseInt(country_datas["accident_type_news"].length);
		/*console.log(accident_length+" "+"accident_type_news"+country_datas["accident_type_news"]);*/
			if(accident_length > 0){
				$(".list_btn_area *").remove();//내부요소 삭제
				manual_btn=3;
			/*	console.log(country_datas["accident_type_news"][0]);	*/	
				$('.title').text($('.available a').text());
				$('.news_title').text("");
				$('.content').html(country_datas["accident_type_news"][0].news.replace(/font-size:/gi,"").replace(/line-height:/gi,"").replace(/https:\/\/www.0404.go.kr/gi,"").replace(/src="/gi,'src="https://www.0404.go.kr').replace(/href="/gi,'href="https://www.0404.go.kr'));
				
				var list_btn='<div class="list_btn" onmousedown="list_btn_click(this)" data-value="0">&#9679;</div>';
								
				for(var i = 1; i<accident_length; i++){
					list_btn+=
					'<div class="list_btn" onmousedown="list_btn_click(this)" data-value="'+i+'">&#9675;</div>'
				}
				
				$(".list_btn_area").append(list_btn);
				
			}else{	
				$('#accident').removeClass("available");
				$('#contents_area').removeClass().addClass("contents");
				
				swal.fire("<b style="+"color:red"+">"+"데이터가 없습니다"+"</b>");
			}
		}
	});
	
	$('#contact').click(function go_contact(){
		if(focused != false){
			local_length = parseInt(country_datas["local_contact_news"].length);
		/*console.log(local_length+" "+"local_contact_news"+country_datas["local_contact_news"]);*/
			if(local_length > 0){
				$(".list_btn_area *").remove();//내부요소 삭제
				manual_btn=4;
				/*console.log(country_datas["local_contact_news"][0]);*/
				$('.title').text($('.available a').text());
				$('.news_title').text("");
				$('.content').html(country_datas["local_contact_news"][0].contact_remark.replace(/font-size:/gi,"").replace(/line-height:/gi,"").replace(/https:\/\/www.0404.go.kr/gi,"").replace(/src="/gi,'src="https://www.0404.go.kr').replace(/href="/gi,'href="https://www.0404.go.kr'));
				
				
				var list_btn='<div class="list_btn" onmousedown="list_btn_click(this)" data-value="0">&#9679;</div>';
								
				for(var i = 1; i<local_length; i++){
					list_btn+=
					'<div class="list_btn" onmousedown="list_btn_click(this)" data-value="'+i+'">&#9675;</div>'
				}
				
				$(".list_btn_area").append(list_btn);
				
			}else{
				$('#accident').removeClass("available");
				$('#contents_area').removeClass().addClass("contents");
				
				swal.fire("<b style="+"color:red"+">"+"데이터가 없습니다"+"</b>");
			}
		}
	});
	
});//window.addEventListener('load', function news_data()

/*뉴스 내용 변경 버튼*/
function list_btn_click(a){
	
	var value = parseInt(a.getAttribute("data-value"));
	
	$(a).addClass("click").siblings().removeClass("click");
	
	/*dangerous*/
	if(manual_btn === 1){
		
		for(var i = 0; i<dangerous_length; i++){
			if(i === value){
				$('.list_btn').eq(i).html("&#9679;");
			}else{
				$('.list_btn').eq(i).html("&#9675;");
			}
		}
		
		$('.news_title').text(country_datas["dangerous_News"][value].title);
		$('.content').html(country_datas["dangerous_News"][value].html_origin_cn.replace(/font-size:/gi,"").replace(/line-height:/gi,"").replace(/https:\/\/www.0404.go.kr/gi,"").replace(/src="/gi,'src="https://www.0404.go.kr').replace(/href="/gi,'href="https://www.0404.go.kr'));
	
	/*safety*/
	}else if(manual_btn === 2){
		
		for(var i = 0; i<safety_length; i++){
			if(i === value){
				$('.list_btn').eq(i).html("&#9679;");
			}else{
				$('.list_btn').eq(i).html("&#9675;");
			}
		}
		
		$('.news_title').text(country_datas["safety_notice_news"][value].title);
		$('.content').html(country_datas["safety_notice_news"][value].txt_origin_cn.replace(/font-size:/gi,"").replace(/line-height:/gi,"").replace(/https:\/\/www.0404.go.kr/gi,"").replace(/src="/gi,'src="https://www.0404.go.kr').replace(/href="/gi,'href="https://www.0404.go.kr'));
	
	/*accident*/
	}else if(manual_btn === 3){
		
		for(var i = 0; i<accident_length; i++){
			if(i === value){
				$('.list_btn').eq(i).html("&#9679;");
			}else{
				$('.list_btn').eq(i).html("&#9675;");
			}
		}
		
		$('.content').html(country_datas["accident_type_news"][value].news.replace(/font-size:/gi,"").replace(/line-height:/gi,"").replace(/https:\/\/www.0404.go.kr/gi,"").replace(/src="/gi,'src="https://www.0404.go.kr').replace(/href="/gi,'href="https://www.0404.go.kr'));
	
	/*contact*/
	}else if(manual_btn === 4){
		
		for(var i = 0; i<local_length; i++){
			if(i === value){
				$('.list_btn').eq(i).html("&#9679;");
			}else{
				$('.list_btn').eq(i).html("&#9675;");
			}
		}
		
		$('.content').html(country_datas["local_contact_news"][value].contact_remark.replace(/font-size:/gi,"").replace(/line-height:/gi,"").replace(/https:\/\/www.0404.go.kr/gi,"").replace(/src="/gi,'src="https://www.0404.go.kr').replace(/href="/gi,'href="https://www.0404.go.kr'));
	}
}