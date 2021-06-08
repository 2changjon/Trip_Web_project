<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta name="viewport"
	content="width=device-width, initial-scale=1, minimum-scale=1" />
<title>Travel'News</title>

<!-- 기본 -->
<script type="text/javascript" src="/resources/js/jquery-3.6.0.min.js"></script>
<!-- d3 사용을 위해 -->
<script type="text/javascript" src="/resources/d3/d3.v3.min.js"></script>
<!-- d3 큐 사용을 위해 -->
<script type="text/javascript" src="/resources/d3/queue.v1.min.js"></script>
<!-- topojson 사용하기 위해 -->
<script type="text/javascript" src="/resources/geo/topojson.v1.min.js"></script>
<!-- d3 이미지 스타일적용 -->
<link rel="stylesheet" href="/resources/css/map_style.css">
<!-- 이미지 스타일적용 -->
<link rel="stylesheet" href="/resources/css/main.css">
<!-- 국가 검색에 대한 스크립트 -->
<script type="text/javascript" src="/resources/js/country_serch.js"></script>
<!-- Clock 작동 -->
<script type="text/javascript" src="/resources/js/clock.js"></script>
<!-- Side bar 작동 -->
<script type="text/javascript" src="/resources/js/side_bar.js"></script>
<!-- Side bar 스타일적용 -->
<link rel="stylesheet" type="" href="/resources/css/side_bar.css">
<!-- 상세메뉴 클릭시 데이터 삽입 -->
<script type="text/javascript" src="/resources/js/news_maun.js"></script>
<!-- 티켓 옵션 변경 -->
<script type="text/javascript" src="/resources/js/ticket_option.js"></script>
<!-- 티켓 검색 -->
<script type="text/javascript" src="/resources/js/ticket_serch.js"></script>
<!-- Date picker 사용을 위해 -->
<script type="text/javascript" src="/resources/js/jquery-ui.min.js"></script>
<!-- Date picker 스타일적용 -->
<link rel="stylesheet" href="/resources/css/jquery-ui.css">
<!-- d3 이미지 생성 및 작동 스크립트 적용 -->
<script type="text/javascript" src="/resources/js/map_start.js"></script>
<!-- 화면 크기 변경에 따른 자바스크립트 -->
<script type="text/javascript" src="/resources/js/reSize.js"></script>
<!-- 이쁜 경고창 가져오기 -->
<script src="//cdn.jsdelivr.net/npm/sweetalert2@11"></script>
<!-- 폰트 가져오기 -->
<link rel = "preconnect"href = "https://fonts.gstatic.com">
<link href="https://fonts.googleapis.com/css2?family=Cinzel&family=Kaushan+Script&family=Dancing+Script&family=Mate+SC&display=swap" rel="stylesheet">
</head>
<body>
	<input type="hidden" id="country_number" value="+1">
	<div class="loading_area"><div class="loading"></div></div>
	<nav class="sidebar" id="side">
		<div class="maun">
			<div class="sidebar_logo"><b>Travel'News</b></div>
			<ul class="select">
				<li>
					<a class="news_maum">News</a>
					<ul id="news_list">
						<li id="dangerous"><a>Dangerous</a></li>
						<li id="safety"><a>Safety</a></li>
						<li id="accident"><a>Accident</a></li>
						<li id="contact"><a>Contact</a></li>
					</ul>
				</li>
				<li>
					<a class="ticket_maum">Ticket</a>
					<!-- <ul id="ticket-list">
						<li><a>Ticketing</a></li>
					</ul> -->
				</li>
<!-- 				<li>
					<a id="community">Community</a>
				</li> -->
			</ul>
			<ul class="time">
				<li>
					<b>Time</b>
					<ul class="basic_time">
						<li>대한민국</li>
						<li id="korea_day"></li>
						<li id="korea_time"></li>
					</ul>
					<ul class="select_country_time">
						<li class="country_name">클릭됨</li>
						<li id="country_day"></li>
						<li id="country_time"></li>
					</ul>
				</li>
			</ul>
		</div>
		<div class="contents" id="contents_area">
			<div class="country_name">국가명</div>
			<div class="basic">Welcome <br>&nbsp;&nbsp;&nbsp;Earth&nbsp;&nbsp;&nbsp;</div>
			<div class="manual">
				<div class="manual_dangerous">
					<b>Dangerous</b>
					<br>
					<br>
					해당 국가에 대하여 외교부에서 지정하는 여행 유의, 자제, 제한 등 해외 여행경보에 관련된 상세정보가 담긴 기사를 제공하는 게시판 입니다. 
				</div>
				<div class="manual_safety">
					<b>Safety</b>
					<br>
					<br>
					해당 국가에 대하여 최신 안전공지에 대한 정보를 제공하는 게시판 입니다.
				</div>
				<div class="manual_accident">
					<b>Accident</b>
					<br>
					<br>
					해당 국가에 대하여 사건 사고가 발생한  발생년도, 사건사고 유형 및 사고 내용에 대한 상세 정보를 제공하는 게시판 입니다.
				</div>
				<div class="manual_contact">
					<b>Contact</b>
					<br>
					<br>
					해당 국가에 대하여 여권 분실 등 해외 여행 중 도움이 필요한 경우 해외 국가의 현지 대사관 연락처 등 연락처 목록 및 상세정보를 제공하는 게시판 입니다.
				</div>
			</div>
			<div class="title_area">
				<div class="title"></div>
				<div class="news_title"></div>
			</div>
			<div class="content_area">
				<div class="content"></div>
				<div class="list_btn_area"></div>
			</div>
			<div class="ticket_area">
				<div class="start_area">
					Departure Place
					<input type="text" id="departure_Place" name="A" onkeyup="getair_port(this)" required autocomplete='off'></input>
					<ul class="serch_air_port_area" id="st_serch_air_port"></ul>
				</div>
				<div class="end_area">
					Arrival Place
					<input type="text" id="arrival_Place" name="B" onkeyup="getair_port(this)" required autocomplete='off'></input>
					<ul class="serch_air_port_area" id="ed_serch_air_port"></ul>
				</div>
				<div class="start_time">
					Departure Date
					<input type="text" id="departure_Date" readonly></input>
				</div>
				<div class="end_time">
					Arrival Date
					<input type="text" id="arrival_Date" readonly></input>
				</div>
				<button class="ticket_option">Ticket Option</button>
				<input type="hidden" id="flight_Type" value="RT"></input>
				<input type="hidden" id="adult" value="1"></input>
				<input type="hidden" id="teenager" value="0"></input>
				<input type="hidden" id="child" value="0"></input>
				<input type="hidden" id="baby" value="0"></input>
				<input type="hidden" id="class_Type" value="Normal"></input>
				<button class="serch_btn" id="serch_bt">검색</button>
			</div>			
		</div>
	</nav>
	<div class="top">
		<div class="sidebar_btn">
			<span class="btn">&#9776;</span>
		</div>
		<b class="top_logo">Travel'News</b>
		<div class="country_serch_area">
			<input type="text" id="country_serch" onkeyup="country_serch_focus()" onKeyPress="javascript:if(event.keyCode==13) {map_change()}" autocomplete="off" placeholder="국가를 입력해주세요">
			<ul id="serch_list_area"></ul> <!-- 검색리스트가 나타나는 영역 -->
		</div>
	</div>
	<div class="bottom">
		<span><b>[공지]</b>&nbsp;&nbsp;임시 서비스중</span>
	</div>
	<div id="map"></div>
</body>
</html>