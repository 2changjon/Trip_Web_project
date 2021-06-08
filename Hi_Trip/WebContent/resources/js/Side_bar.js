var ticket_serch_ck = false; //티켓 검색하기전 

var orderDate = "";
var today = new Date();
var yyyy =  today.getFullYear();
	//0부터 1월
var mm =  today.getMonth()+1 > 9 ? today.getMonth()+1 : '0' + (today.getMonth()+1);
var dd =  today.getDate() > 9 ? today.getDate() : '0' + today.getDate();
window.onload = function(){
	
	var sidebar_btn = document.querySelector(".sidebar_btn"),
		basic = document.querySelector(".basic");

		/* 사이드바 버튼 클릭시 이벤트*/
		sidebar_btn.addEventListener('click',function(){
      		sidebar_btn.classList.toggle("click"); //버튼 클릭에 따른 버튼 스타일 변경
      		$('.sidebar').toggleClass("show");//버튼 클릭에 따른 사이드 바 스타일 변경
			$(".top").toggleClass("back"); //버튼 클릭에 따른 .top 스타일 변경  
			$(".bottom").toggleClass("back");
			$("#globeSvg").toggleClass("back");
			$(".infoLabel").toggleClass("back");
			
		/*	if( height > width ){
				$("#planeSvg")
			}else{
				
			}*/
				$('#ticket_list').removeClass("ticket-show");
				$('#news_list').removeClass("news-show");
				$('.sidebar .maun .select li').siblings().removeClass("active");
				$('.sidebar .maun .select li ul li').siblings().removeClass("available");
				$("#planeSvg").toggleClass("back");
				basic.classList.remove("no-show");
				$('.serch_List').siblings().removeClass("active");
				
				/*$('.mapData focused')*/
				
				$('#contents_area').removeClass().addClass("contents");
				$('.country_serch_area').css("display","inline-block");
    	});

		/* 뉴스 클릭시 티켓-상세 리스트 해제 및 상세 클릭 해제  */
		$('.news_maum').click(function(){
			$('#news_list').toggleClass("news-show");
			$('#ticket_list').removeClass("ticket-show");
			$('.sidebar .maun .select li ul li').siblings().removeClass("available");
			$('.basic').addClass("no-show");
			
			$('#contents_area').removeClass().addClass("contents");
			$('.contents').addClass("news");
			
			$('.title').text("News");
			$('.news_title').text("-뉴스목록에 대한 설명이 있는 곳입니다-");
			
			$('.country_serch_area').css("display","inline-block");
			
	    });
	
		/* 티켓 클릭시 뉴스-상세 리스트 해제 및 상세 클릭 해제 */
		$('.ticket_maum').click(function(){
			/*입력값이 있었으면 초기화*/
			if(ticket_serch_ck){
				$('#departure_Place').val("");
				$('#departure_Date').val(yyyy+"-"+mm+"-"+dd);
				$('#arrival_Place').val("");
				$('#arrival_Date').val(yyyy+"-"+mm+"-"+dd);
				
				$('#flight_Type').val("RT");
				$('#adult').val("1");
				$('#teenager').val("0");
				$('#child').val("0");
				$('#baby').val("0");
				$('#class_Type').val("Normal");
				$(".ticket_results").remove();//삭제
				ticket_serch_ck = false;
			}			
			$('#ticket_list').toggleClass("ticket-show");
			$('#news_list').removeClass("news-show");
			$('.sidebar .maun .select li ul li').removeClass("available");
			$('.basic').addClass("no-show");
			
			/*content 스타일 변경*/
			$('#contents_area').removeClass().addClass("contents");
			$('.contents').addClass("ticket");
			$('.country_serch_area').css("display","none");
			
			$('.title').text("Ticket");
			
	    });
	
		/* 커뮤니티 클릭시 티켓,뉴스-상세 리스트 해제 및 상세 클릭 해제  */
		$('#community').click(function(){
			$('#ticket_list').removeClass("ticket-show");
			$('#news_list').removeClass("news-show");
			/*$('.sidebar .maun .select li ul li').removeClass("available");*/ //상세 메뉴 없음
			$('.basic').addClass("no-show");
			
			$('#contents_area').removeClass().addClass("contents");
			$('.contents').addClass("community");
			$('.country_serch_area').css("display","inline-block");
	    });
	
		/* 로코 클릭시 전체 해제 */
		$('.sidebar_logo').click(function(){
			$("#serch_list_area *").remove();//내부요소 삭제
			$("#country_serch").val(""); //초기화
			$('#ticket_list').removeClass("ticket-show");
			$('#news_list').removeClass("news-show");
			$('.sidebar .maun .select li').siblings().removeClass("active");
			$('.sidebar .maun .select li ul li').siblings().removeClass("available");
			$('.basic').removeClass("no-show");
			
			$('#contents_area').removeClass().addClass("contents");
			$('.country_serch_area').css("display","inline-block");
			
			if(focused != false){
				reset()
			}
		});
		
		/* top 로코 클릭시 전체 해제 */
		$('.top_logo').click(function(){
			$("#serch_list_area *").remove();//내부요소 삭제
			$("#country_serch").val(""); //초기화
			$('#ticket_list').removeClass("ticket-show");
			$('#news_list').removeClass("news-show");
			$('.sidebar .maun .select li').siblings().removeClass("active");
			$('.sidebar .maun .select li ul li').siblings().removeClass("available");
			$('.basic').removeClass("no-show");
			
			if(focused != false){
				reset()
			}
			$('.country_serch_area').css("display","inline-block");
		});
		
	
		/* 메뉴 클릭시 스타일 고정 */
		$('.sidebar .maun .select li').click(function(){
	        $(this).addClass("active").siblings().removeClass("active");
	    });
	
		/* 상세메뉴 클릭시 스타일 고정 */
		$('.sidebar .maun .select li ul li').click(function(){
			if(focused != false){
	       		$(this).addClass("available").siblings().removeClass("available");
			
				$('#contents_area').removeClass().addClass("contents");
				$('.contents').addClass("insert");
				
			}else{
				swal.fire("<b style="+"color:red"+">국가 미선택</b>","국가를 선택해주세요","warning");
			}
	    });
		
}//window.onload