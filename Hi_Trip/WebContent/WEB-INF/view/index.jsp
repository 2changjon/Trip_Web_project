<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<title>Insert title here</title>

<!-- 기본 -->
<script type="text/javascript" src="/resources/js/jquery-3.6.0.min.js"></script>
<!-- d3 사용을 위해 -->
<script type="text/javascript" src="/resources/d3/d3.v3.min.js"></script>
<!-- d3 퀵 사용을 위해 -->
<script type="text/javascript" src="/resources/d3/queue.v1.min.js"></script>
<!-- topojson 사용하기 위해 -->
<script type="text/javascript" src="/resources/geo/topojson.v1.min.js"></script>
<!-- d3 이미지 스타일적용 -->
<link rel="stylesheet" href="/resources/css/mapStyle.css">
<!-- d3 이미지 생성 및 작동 스크립트 적용 -->
<script type="text/javascript" src="/resources/js/mapStart.js"></script>
<!-- 화면 크기 변경에 따른 자바스크립트 -->
<script type="text/javascript" src="/resources/js/reSize.js"></script>

</head>
<body>
<div id="back"></div>
<div id="map"></div>
</body>
</html>