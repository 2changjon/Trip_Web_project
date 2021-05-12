var news_title="해당 국가 해당 항목 뉴스기사 제목"
window.onload = function(){
	var sidebar_btn = document.querySelector(".sidebar_btn"),
		basic = document.querySelector(".basic");
/*		sidebar = document.querySelector(".sidebar"),
		top = document.querySelector(".top"),
		bottom = document.querySelector(".bottom"),
		top_logo = document.querySelector(".top_logo"),
		infoLabel = document.querySelector(".infoLabel"),
		news_maum = document.querySelector(".news_maum"),
		ticket_maum = document.querySelector(".ticket_maum"),
		go_Dangerous = document.querySelector(".go_Dangerous"),
		go_Safety = document.querySelector(".go_Safety"),
		go_Accident = document.querySelector(".go_Accident"),
		go_Contact = document.querySelector(".go_Contact");
		
	var	globeSvg = document.getElementById("globeSvg"),
		planeSvg = document.getElementById("planeSvg"),
		news_list = document.getElementById("news_list"),
		ticket_list = document.getElementById("ticket_list"),
		content = document.getElementById("content"),
		dangerous = document.getElementById("dangerous"),
		safety = document.getElementById("safety"),
		accident = document.getElementById("accident"),
		contact = document.getElementById("contact");
		*/
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
			$('.title').html("<b>News</b><br><br><b>-뉴스목록에 대한 설명이 있는 곳입니다-</b>");
			$('.country_serch_area').css("display","inline-block");
			
	    });
	
		/* 티켓 클릭시 뉴스-상세 리스트 해제 및 상세 클릭 해제 */
		$('.ticket_maum').click(function(){
			$('#ticket_list').toggleClass("ticket-show");
			$('#news_list').removeClass("news-show");
			$('.sidebar .maun .select li ul li').removeClass("available");
			$('.basic').addClass("no-show");
			
			/*content 스타일 변경*/
			$('#contents_area').removeClass().addClass("contents");
			$('.contents').addClass("ticket");
			$('.country_serch_area').css("display","none");
			
			$('.title').html("<b>Ticket</b>");
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
			
			$('.title').html("<b>"+$('.available a').text()+"</b><br><br>"+"<b>-"+news_title+"-</b>")
			}else{
				swal("국가 미선택","국가를 선택해주세요","warning")
			}
	    });
		/*바로가기 클릭시*/
		$('.go_Dangerous').click(function(){
			$('.sidebar .maun .select li ul li').siblings().removeClass("available");
			$('#news_list').addClass("news-show");
			$('#dangerous').addClass("available");
			
			$('#contents_area').removeClass().addClass("contents");
			$('.contents').addClass("insert");
		});
		$('.go_Safety').click(function(){
			$('.sidebar .maun .select li ul li').siblings().removeClass("available");
			$('#news_list').addClass("news-show");
			$('#safety').addClass("available");
			
			$('#contents_area').removeClass().addClass("contents");
			$('.contents').addClass("insert");
		});
		$('.go_Accident').click(function(){
			$('.sidebar .maun .select li ul li').siblings().removeClass("available");
			$('#news_list').addClass("news-show");
			$('#accident').addClass("available");
			
			$('#contents_area').removeClass().addClass("contents");
			$('.contents').addClass("insert");
		});
		$('.go_Contact').click(function(){
			$('.sidebar .maun .select li ul li').siblings().removeClass("available");
			$('#news_list').addClass("news-show");
			$('#contact').addClass("available");
			
			$('#contents_area').removeClass().addClass("contents");
			$('.contents').addClass("insert");
		});
}//window.onload