<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta name="viewport"
	content="width=device-width, initial-scale=1, minimum-scale=1" />
<title>Insert title here</title>

<!-- 기본 -->
<script type="text/javascript" src="/resources/js/jquery-3.6.0.min.js"></script>
<!-- d3 사용을 위해 -->
<script type="text/javascript" src="/resources/d3/d3.v3.min.js"></script>
<!-- d3 큐 사용을 위해 -->
<script type="text/javascript" src="/resources/d3/queue.v1.min.js"></script>
<!-- topojson 사용하기 위해 -->
<script type="text/javascript" src="/resources/geo/topojson.v1.min.js"></script>
<!-- d3 이미지 스타일적용 -->
<link rel="stylesheet" href="/resources/css/mapStyle.css">
<!-- 이미지 스타일적용 -->
<link rel="stylesheet" href="/resources/css/Main.css">
<!-- 국가 검색에 대한 스크립트 -->
<script type="text/javascript" src="/resources/js/country_serch.js"></script>
<!-- Clock 작동 -->
<script type="text/javascript" src="/resources/js/Clock.js"></script>
<!-- Side bar 작동 -->
<script type="text/javascript" src="/resources/js/Side_bar.js"></script>
<!-- Side bar 스타일적용 -->
<link rel="stylesheet" href="/resources/css/Side_bar.css">
<!-- 상세메뉴 클릭시 데이터 삽입 -->
<script type="text/javascript" src="/resources/js/news_maun.js"></script>
<!-- d3 이미지 생성 및 작동 스크립트 적용 -->
<script type="text/javascript" src="/resources/js/mapStart.js"></script>
<!-- 화면 크기 변경에 따른 자바스크립트 -->
<script type="text/javascript" src="/resources/js/reSize.js"></script>
<!-- 이쁜 경고창 가져오기 -->
<script src="//cdn.jsdelivr.net/npm/sweetalert2@11"></script>
</head>
<body>
	<input type="text" id="country_number" value="+1">
	<nav class="sidebar">
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
				<li>
					<a id="community">Community</a>
				</li>
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
					<b class="go_Dangerous" title="클릭시 Dangerous로 이동">Dangerous</b>
					<br>
					<br>
					해당 국가에 대하여 외교부에서 지정하는 여행 유의, 자제, 제한 등 해외 여행경보에 관련된 상세정보가 담긴 기사를 제공하는 게시판 입니다. 
				</div>
				<div class="manual_safety">
					<b class="go_Safety" title="클릭시 Safety로 이동">Safety</b>
					<br>
					<br>
					해당 국가에 대하여 최신 안전공지에 대한 정보를 제공하는 게시판 입니다.
				</div>
				<div class="manual_accident">
					<b class="go_Accident" title="클릭시 Accident로 이동">Accident</b>
					<br>
					<br>
					해당 국가에 대하여 사건 사고가 발생한  발생년도, 사건사고 유형 및 사고 내용에 대한 상세 정보를 제공하는 게시판 입니다.
				</div>
				<div class="manual_contact">
					<b class="go_Contact" title="클릭시 Contact로 이동">Contact</b>
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
				<div class="content">
				content공간
				content공간content공간
				content공간content공간content공간
				content공간content공간content공간content공간
				content공간content공간content공간content공간content공간
				content공간content공간content공간content공간content공간content공간
				content공간content공간content공간content공간content공간content공간content공간
				content공간content공간content공간content공간content공간content공간content공간content공간
				content공간content공간content공간content공간content공간content공간content공간content공간content공간
				</div>
				<div class="next_btn">버튼 공간</div>
			</div>
			<div class="ticket_area">
				<div class="start_area"></div>
				<div class="end_area"></div>
				<div class="start_time"></div>
				<div class="end_time"></div>
				<div class="serch_btn"></div>
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
		<span><b>[공지]</b>&nbsp;&nbsp;로컬에서 서비스중</span>
	</div>
	<div id="map"></div>
</body>
</html>