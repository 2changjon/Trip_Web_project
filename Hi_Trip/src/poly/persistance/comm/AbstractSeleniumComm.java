package poly.persistance.comm;

import java.util.Iterator;
import java.util.List;

import org.apache.log4j.Logger;
import org.jsoup.nodes.Element;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

public abstract class AbstractSeleniumComm {
	// 로그 파일 생성 및 로그 출력을 위한 log4j 프레임워크의 자바 객체
	private Logger log = Logger.getLogger(this.getClass());
	
	//팝업1
	protected boolean Pop_Up1(WebElement area) {
		try {
			area.findElement(By.xpath("//button[@class=\"Button-No-Standard-Style close inside darkIcon"));
			return true;
		}catch (Exception e) {
			log.info("에러 ="+e);
			return false;
		}
	}
	
	// 팝업2
	protected boolean Pop_Up2(WebElement area) {
		try {
			area.findElement(By.xpath("//div[@class=\"bBPb-closesvg"));
			return true;
		}catch (Exception e) {
			log.info("에러 ="+e);
			return false;
		}
	}
	
	// 검색된 항공권 유무
	protected boolean ticket_Check(WebElement area) {
		try {
			area.findElement(By.xpath("//div[@class=\"resultWrapper\"]//div[@class=\"resultInner\"]//div[@class=\"inner-grid keel-grid\"]"));
			return true;
		}catch (Exception e) {
			log.info("에러 ="+e);
			return false;
		}
	}
	
	// 경유지 유무
	protected boolean wayPoint_Check(WebElement area) {
		try {
			area.findElement(By.cssSelector(".js-layover"));
			return true;
		}catch (Exception e) {
			log.info("에러 ="+e);
			return false;
		}
	}
	
	// 경유지 가져오기(경유지 개수에 따라 입력값이 바뀜)  title:0 - text, 1- title 
	protected String wayPoint(WebElement area, int title) {
		if(wayPoint_Check(area)) {
			List<WebElement> wayPoints = area.findElements(By.cssSelector(".js-layover"));
			String wayPoint = "";
			
			int wayPoint_num = 1;
			for(WebElement getwayPoint : wayPoints) {
				if(title == 0) {
					if(wayPoint_num == 1) {
						wayPoint = wayPoint + getwayPoint.getText();
						wayPoint_num++;
					}else {
						wayPoint = wayPoint + "," + getwayPoint.getText();
						wayPoint_num++;
					}
				}else {
					if(wayPoint_num == 1) {
						wayPoint = wayPoint + getwayPoint.getAttribute("title");
						wayPoint_num++;
					}else {
						wayPoint = wayPoint + "," + getwayPoint.getAttribute("title");
						wayPoint_num++;
					}
				}
			}
			return wayPoint;
		}else {
			return "";
		}
	}
	
	// 출발, 도착 시간 가져오기
	protected String Time(WebElement area) {
		String start_time = area.findElement(By.cssSelector(".depart-time.base-time")).getText();
		String end_time = area.findElement(By.cssSelector(".arrival-time.base-time")).getText();
		String time = start_time + " - " + end_time;
		return time;
	}

	// 날짜 변동
	protected String Addendum(WebElement area) {
		if(addendum_Check(area)) {
			String addendum = area.findElement(By.cssSelector(".adendum")).getText();
			return addendum;
		}else {
			
			return "";
		}
	}
	
	// 날짜 변동 유무
	protected boolean addendum_Check(WebElement area) {
		try {
			area.findElement(By.cssSelector(".adendum"));
			return true;
		}catch (Exception e) {
			log.info("에러 ="+e);
			return false;
		}
	}
	
	// 여행 구분 가져오기(직항 등)
	protected String Flight_Type(WebElement area) {
		String flight_Type= area.findElement(By.cssSelector(".section.stops .stops-text")).getText();	// 직항, 경유
		return flight_Type;
	}
	
	// 출발, 도착 공항 가져오기
	protected String Airport(WebElement area) {
		String start_airport = area.findElement(By.cssSelector(".bottom-airport:nth-of-type(1) .airport-name")).getText();	// 출발공항
		String end_airport = area.findElement(By.cssSelector(".bottom-airport:nth-of-type(2) .airport-name")).getText();	// 도착 공항
		String airport = start_airport + " - " + end_airport;
		return airport;
	}
	
	// 비행시간
	protected String Flight_time(WebElement area) {
		String flight_time = area.findElement(By.cssSelector(".section.duration.allow-multi-modal-icons .top")).getText();
		return flight_time;
	}
	
	// 토탈가격 유무
	protected boolean total_Check(WebElement area) {
		try {
			area.findElement(By.cssSelector(".price-total"));
			return true;
		}catch (Exception e) {
			log.info("에러 ="+e);
			return false;
		}
	}
	
	// 티켓 url 확인후 가져오기:nth-child(1),:nth-of-type(1)
	protected String Ticket_ting(WebElement area) { 
		String ticket_ting;
		String getUrl_Start = area.findElement(By.cssSelector(".booking-link:last-child")).getAttribute("href");
		log.info(getUrl_Start.substring(0,4).equals("http"));
		if(getUrl_Start.substring(0,4).equals("http")) {
			ticket_ting = area.findElement(By.cssSelector(".booking-link:last-child")).getAttribute("href");
		}else {
			ticket_ting = "https://www.kayak.co.kr"+area.findElement(By.cssSelector(".booking-link:last-child")).getAttribute("href");
		}
		return ticket_ting;
	}
	
	/* jsoup */

	// 검색된 항공권 유무
	protected boolean ticket_Check2(Element area) {
		return false;
	}
		
	// 출발, 도착 시간 가져오기
	protected String Time2(Element element) {
		String st_time = element.getElementsByClass("depart-time base-time").text();
		String ed_time = element.getElementsByClass("arrival-time base-time").text();
		
		String time = st_time+" - "+ed_time;
		
		return time;
	}

	// 날짜 변동
	protected String Addendum2(Element element) {
		String addendum = element.getElementsByClass("adendum").text();
		return addendum;
	}
	
	// 출발, 도착 공항 가져오기
	protected String Airport2(Element element) {
		String st_airport = "";
		String ed_airport = "";
		
		Iterator<Element> aipports = element.getElementsByClass("airport-name").iterator();
		
		int i =1;
		while (aipports.hasNext()) {
			Element aipport = aipports.next();
			if(i == 1) {
				st_airport = aipport.getElementsByClass("airport-name").text();
				i++;
			}else {				
				ed_airport = aipport.getElementsByClass("airport-name").text();
			}
		}
	
		String airport = st_airport + " - " + ed_airport;
		return airport;
	}
	
	// 여행 구분 가져오기(직항, 경유)
	protected String Flight_Type2(Element element) {
		String flight_type = element.getElementsByClass("stops-text").text();
		return flight_type;
	}
	
	// 경유지 가져오기(경유지 개수에 따라 입력값이 바뀜) title:0 - text, 1- title 
	protected String wayPoint2(Element element, int title) {
		String wayPoint_nm = "";
		
		Iterator<Element> wayPoints = element.getElementsByClass("js-layover").iterator();
		
		int i =1;
		while (wayPoints.hasNext()) {
			Element wayPoint = wayPoints.next();
			if(title == 0) {
				if(i == 1) {
					wayPoint_nm = wayPoint_nm + wayPoint.text();
					i++;
				}else {
					wayPoint_nm = wayPoint_nm + "," + wayPoint.text();
					i++;
				}
			}else {
				if(i == 1) {
					wayPoint_nm = wayPoint_nm + wayPoint.attr("title");
					i++;
				}else {
					wayPoint_nm = wayPoint_nm + "," + wayPoint.attr("title");
					i++;
				}
			}
		}
		
		return wayPoint_nm;
	}
	
	// 비행시간
	protected String Flight_time2(Element element) {
		Element Flight  = element.getElementsByClass("section duration allow-multi-modal-icons").first();
		
		String flight_time = Flight.getElementsByClass("top").text();
		
		return flight_time;
	}

	protected String Ticket_ting2(Element element) { 
		String ticket_ting;
		String getUrl_Start = element.getElementsByClass("booking-link ").last().attr("href");
		
		if(getUrl_Start.substring(0,4).equals("http")) {
			ticket_ting = element.getElementsByClass("booking-link ").last().attr("href");
		}else {
			ticket_ting = "https://www.kayak.co.kr"+element.getElementsByClass("booking-link ").last().attr("href");
		}
		return ticket_ting;
	}
	
}