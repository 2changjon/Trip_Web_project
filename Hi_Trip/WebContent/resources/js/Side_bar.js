/*news_title country_serch에서 생성*/
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
			if(focused != false){
				if(country_datas["travel_prohibited"].length > 0){
					
					if(country_datas["travel_prohibited"][0].hasOwnProperty('banPartial')){
						console.log("travel_prohibited if :"+country_datas["travel_prohibited"][0].banPartial);
						swal.fire({
							title: country_datas["travel_prohibited"][0].banPartial, 
							text: country_datas["travel_prohibited"][0].banNote,
							width: 600,
							imageUrl: country_datas["travel_prohibited"][0].imgUrl2,
							imageWidth: 600,
	  						imageHeight: 300,
							customClass: {
								 title: 'swal_title'
							}
							});	
					}else{
						console.log("travel_prohibited else "+country_datas["travel_prohibited"][0].ban);
						swal.fire({
							title: country_datas["travel_prohibited"][0].ban, 
							text: country_datas["travel_prohibited"][0].banNote,
							width: 600,
							imageUrl: country_datas["travel_prohibited"][0].imgUrl2,
							imageWidth: 600,
	  						imageHeight: 300,
							customClass: {
								 title: 'swal_title'
							}
							});
					}
				}else if(country_datas["travel_alert"].length > 0){
						
					if(country_datas["travel_alert"][0].hasOwnProperty('limitaPartial')){
						console.log("travel_alert if "+country_datas["travel_alert"][0].limitaPartial);
						swal.fire(country_datas["travel_alert"][0].limitaPartial, country_datas["travel_alert"][0].limitaNote,"error");
					}
				}else if(country_datas["special_travel"].length > 0){
					if(country_datas["special_travel"][0].hasOwnProperty('splimit')){
						console.log("special_travel if "+country_datas["special_travel"][0].splimit);
						swal.fire(country_datas["special_travel"][0].splimit, country_datas["special_travel"][0].splimitNote,"error");
					}else{
						console.log("special_travel else "+country_datas["special_travel"][0].splimitPartial);
						swal.fire(country_datas["special_travel"][0].splimitPartial, country_datas["special_travel"][0].splimitNote,"error");
					}
				}
			}
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
				
				$('.title').text($('.available a').text());
				$('.news_title').text(news_title);
			}else{
				swal.fire("국가 미선택","국가를 선택해주세요","warning")
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