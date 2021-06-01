package poly.persistance.comm;

import java.util.List;

import org.apache.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

public abstract class AbstractSeleniumComm {
	// 로그 파일 생성 및 로그 출력을 위한 log4j 프레임워크의 자바 객체
	private Logger log = Logger.getLogger(this.getClass());
	
	//팝업1
	protected boolean Pop_Up1(WebElement area) {
		try {
			area.findElement(By.xpath("//button[@class=\"Button-No-Standard-Style close inside darkIcon \"]"));
			return true;
		}catch (Exception e) {
			log.info("에러 ="+e);
			return false;
		}
	}
	
	// 팝업2
	protected boolean Pop_Up2(WebElement area) {
		try {
			area.findElement(By.xpath("//div[@class=\"bBPb-close\"]//span[@class=\"svg\"]"));
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
			area.findElement(By.xpath("//span[@class=\"js-layover\"]"));
			return true;
		}catch (Exception e) {
			log.info("에러 ="+e);
			return false;
		}
	}
	
	// 경유지 가져오기(경유지 개수에 따라 입력값이 바뀜)
	protected String Stopover(WebElement area) {
		if(wayPoint_Check(area)) {
			List<WebElement> stopovers = area.findElements(By.xpath("//span[@class=\"js-layover\"]"));
			String stopover="";
			int stopover_num = 1;
			for(WebElement getstopover : stopovers) {
				if(stopover_num == 1) {
					stopover=stopover+getstopover.getAttribute("title");
					stopover_num++;
				}else {
					stopover=stopover+","+getstopover.getAttribute("title");
					stopover_num++;
				}
			}
			return stopover;
		}else {
			return "";
		}
	}
	
	// 출발, 도착 시간 가져오기
	protected String Time(WebElement area) {
		String start_time = area.findElement(By.xpath("//span[@class=\"depart-time base-time\"]")).getText();
		String end_time = area.findElement(By.xpath("//span[@class=\"arrival-time base-time\"]")).getText();
		String time = start_time + " - " + end_time;
		return time;
	}

	// 경유지
	protected String Addendum(WebElement area) {
		if(addendum_Check(area)) {
			String addendum = area.findElement(By.xpath("//sup[@class=\"adendum\"]")).getText();
			return addendum;
		}else {
			
			return "";
		}
	}
	
	// 날짜 변동 유무
	protected boolean addendum_Check(WebElement area) {
		try {
			area.findElement(By.xpath("//sup[@class=\"adendum\"]"));
			return true;
		}catch (Exception e) {
			System.out.println("에러 ="+e);
			return false;
		}
	}
	
	// 여행 구분 가져오기
	protected String Flight_Type(WebElement area) {
		String flight_Type= area.findElement(By.xpath("//span[@class=\"stops-text\"]")).getText();	// 직항, 경유
		return flight_Type;
	}
	
	// 출발, 도착 공항 가져오기
	protected String Airport(WebElement area) {
		String start_airport = area.findElement(By.xpath("(//span[@class=\"airport-name\"])[1]")).getText();	// 출발공항
		String end_airport = area.findElement(By.xpath("(//span[@class=\"airport-name\"])[2]")).getText();	// 도착 공항
		String airport = start_airport + " - " + end_airport;
		return airport;
	}
	
	// 비행시간
	protected String Flight_time(WebElement area) {
		String flight_time = area.findElement(By.xpath("//div[@class=\"section duration allow-multi-modal-icons\"]//div[@class=\"top\"]")).getText();
		return flight_time;
	}
	
	// 토탈가격 유무
	protected boolean total_Check(WebElement area) {
		try {
			area.findElement(By.xpath("//div[@class=\"multibook-dropdown\"]//div[@class=\"price-total\"]"));
			return true;
		}catch (Exception e) {
			log.info("에러 ="+e);
			return false;
		}
	}
	
	// 티켓 url 확인후 가져오기
	protected String Ticket_ting(WebElement area) { 
		String ticket_ting;
		String getUrl_Start = area.findElement(By.xpath("//div[@class=\"Common-Widgets-Button-ButtonDeprecated Common-Widgets-Button-Button Button-Gradient ui-button size-m bookingButton \"]//a[@class=\"booking-link \"]")).getAttribute("href");
		
		if(getUrl_Start.substring(0,4).equals("http")) {
			ticket_ting = area.findElement(By.xpath("//div[@class=\"Common-Widgets-Button-ButtonDeprecated Common-Widgets-Button-Button Button-Gradient ui-button size-m bookingButton \"]//a[@class=\"booking-link \"]")).getAttribute("href");
		}else {
			ticket_ting = "https://www.kayak.co.kr"+area.findElement(By.xpath("//div[@class=\"Common-Widgets-Button-ButtonDeprecated Common-Widgets-Button-Button Button-Gradient ui-button size-m bookingButton \"]//a[@class=\"booking-link \"]")).getAttribute("href");
		}
		return ticket_ting;
	}
	
	
	protected String Time2(Element element) {
		
	}
	
}