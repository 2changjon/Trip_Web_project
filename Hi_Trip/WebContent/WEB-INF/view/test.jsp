<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Insert title here</title>
</head>
<body>
<div style="height:500px;width: 500px;">
<div id="kayakSearchWidgetContainer"></div>
</div>
<script type="text/javaScript" src="https://www.kayak.com/affiliate/widget-v2.js"></script>
<script type="text/javaScript">
KAYAK.embed({
container: document.getElementById("kayakSearchWidgetContainer"),
hostname: "www.kayak.com",
autoPosition: true,
defaultProduct: "hotels",
enabledProducts: ["hotels", "flights"],
startDate: "2021-05-31",
endDate: "2021-06-01",
origin: "New York, NY",
destination: "Boston, MA",
ssl: true,
affiliateId: "acme_corp",
isInternalLoad: false,
lc: "ko",
cc: "kr",
mc: "KRW"
});
</script>
</body>
</html>