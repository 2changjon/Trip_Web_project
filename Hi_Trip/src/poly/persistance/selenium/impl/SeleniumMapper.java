package poly.persistance.selenium.impl;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.springframework.stereotype.Component;

import io.github.bonigarcia.wdm.WebDriverManager;
import poly.persistance.comm.AbstractSeleniumComm;
import poly.persistance.selenium.ISeleniumMapper;

@Component("SeleniumMapper")
public class SeleniumMapper extends AbstractSeleniumComm implements ISeleniumMapper {
	
	private Logger log = Logger.getLogger(this.getClass());
	
	//WebDriver 설정
	private WebDriver driver;
	private WebElement element;
	
	//Properties 설정
	// 1. 드라이버 설치 경로
	public static String WEB_DRIVER_ID = "webdriver.chrome.driver";
	public static String WEB_DRIVER_PATH = "./src/poly/data/chromedriver.exe";

	//셀레니움 + jsoup
	@Override
	public ArrayList<Map<String, String>> getTicket1(String url) {
		log.info(this.getClass()+".getTicket2 start");
		// WebDriver 경로 설정
		System.setProperty(WEB_DRIVER_ID, WEB_DRIVER_PATH);
		
		DesiredCapabilities capabilities = DesiredCapabilities.chrome();
		ChromeOptions options = new ChromeOptions();
		options.addArguments("headless");
		options.addArguments("user-agent=Mozilla/5.0 (Windows NT 10.0; Win32; x32) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/90.0.4430.24 Safari/537.36");
		options.addArguments("no-sandbox");
		options.addArguments("start-maximized");
		options.addArguments("disable-popup-blocking");
		capabilities.setCapability(ChromeOptions.CAPABILITY, options);
		WebDriverManager.chromedriver().setup();
		
		driver = new ChromeDriver(options);
		driver.get(url);
		
		Map<String, String> set_Tiket_Map; //항공편에 대한 정보 
		ArrayList<Map<String, String>> tiket_List = new ArrayList<Map<String,String>>(); //항공편들 모음
		List<String> price_check = new ArrayList<String>();//티켓 중복 검사를 위해 가격 저장
		
		try {
			Thread.sleep(15000);
			Document doc = Jsoup.parse(driver.getPageSource());
			
			driver.quit();
			
			//크롬 드라이버 프로세스 삭제
			try {
				Runtime.getRuntime().exec("taskkill /F /IM chromedriver.exe /T");
			} catch (IOException e) {
				e.printStackTrace();
			}
			
			Iterator<Element> ticket_List = doc.getElementsByClass("inner-grid keel-grid").iterator(); //검색된 항공권 결과
			
			int ticket_num = 1;
			while (ticket_List.hasNext()) {
				Element ticket = ticket_List.next(); // 티켓 하나
				
				Element price_area = ticket.getElementsByClass("Flights-Results-FlightPriceSection right-alignment sleek largeNumberCurrency").first();
				String price = price_area.getElementsByClass("price-text").text(); //1인당 가격
				
				boolean ticket_check = true; // 중복되지 않은 티켓 유무 
				//티켓 중복 검사(1인당 가격으로 비교)
				for(int i = 0; i<price_check.size(); i++) {
					if(price_check.get(i).equals(price)) {
						ticket_check = false;
						break;
					}
				}
				
				price_check.add(price);
				
				//최초 1개는 추천 최대 5개
				if(ticket_num <= 5 && ticket_check) {
					Iterator<Element> ticket_infos = ticket.getElementsByClass("container").iterator(); //티켓 정보
					set_Tiket_Map = new LinkedHashMap<String, String>();
					int li_num = 1; //출발:1 반환:2
					
					while (ticket_infos.hasNext()) {
						Element ticket_info = ticket_infos.next(); //출발 관련 1개 도착 관련 1개
						//출발
						if(li_num == 1) {
							String go_time = Time2(ticket_info);
							String go_flight_Type = Flight_Type2(ticket_info);
							String go_airport = Airport2(ticket_info);
							String go_waypoint = wayPoint2(ticket_info, 0);
							String go_waypoint_title = wayPoint2(ticket_info, 1);
							String go_addendum = Addendum2(ticket_info);
							String go_flight_time = Flight_time2(ticket_info);
							li_num++;
							
							set_Tiket_Map.put("go_time", go_time);              
							set_Tiket_Map.put("go_flight_Type", go_flight_Type);
							set_Tiket_Map.put("go_airport", go_airport);        
							set_Tiket_Map.put("go_waypoint", go_waypoint);
							set_Tiket_Map.put("go_waypoint_title", go_waypoint_title);   
							set_Tiket_Map.put("go_addendum", go_addendum);      
							set_Tiket_Map.put("go_flight_time", go_flight_time);
							
							//반환
						}else {
							String bak_time = Time2(ticket_info);               
							String bak_flight_Type = Flight_Type2(ticket_info); 
							String bak_airport = Airport2(ticket_info);         
							String bak_waypoint = wayPoint2(ticket_info, 0);
							String bak_waypoint_title = wayPoint2(ticket_info, 1);
							String bak_addendum = Addendum2(ticket_info);       
							String bak_flight_time = Flight_time2(ticket_info); 
							li_num = 1;
							
							set_Tiket_Map.put("bak_time", bak_time);               
							set_Tiket_Map.put("bak_flight_Type", bak_flight_Type); 
							set_Tiket_Map.put("bak_airport", bak_airport);         
							set_Tiket_Map.put("bak_waypoint", bak_waypoint);
							set_Tiket_Map.put("bak_waypoint_title", bak_waypoint_title);  
							set_Tiket_Map.put("bak_addendum", bak_addendum);       
							set_Tiket_Map.put("bak_flight_time", bak_flight_time); 
							
						}
					}
					
					String total_price = price_area.getElementsByClass("price-total").text();
					
					if(total_price.equals("")) {
						total_price = "총 "+ price;
					}
					String ticket_ting = Ticket_ting2(price_area);
					
					set_Tiket_Map.put("total_price", total_price);
					set_Tiket_Map.put("price", price);
					set_Tiket_Map.put("ticket_ting", ticket_ting);
					
					tiket_List.add(set_Tiket_Map);     
					set_Tiket_Map = null;
					
					ticket_num++;
				} else if(5 < ticket_num){ //if(ticket_num <= 5 && ticket_check) 
					break;
				}
			}//while (ticket_List.hasNext())
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		log.info(this.getClass()+".getTicket2 end");
		return tiket_List;
	}

	//순수 셀레니움
	@Override
	public ArrayList<Map<String, String>> getTicket2(String url, String flight_Type) {
		log.info(this.getClass()+".getTicket start");
		// WebDriver 경로 설정
		System.setProperty(WEB_DRIVER_ID, WEB_DRIVER_PATH);
		// 2. WebDriver 옵션 설정
		DesiredCapabilities capabilities = DesiredCapabilities.chrome();
		ChromeOptions options = new ChromeOptions();
		options.addArguments("headless");
		options.addArguments("user-agent=Mozilla/5.0 (Windows NT 10.0; Win32; x32) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/90.0.4430.24 Safari/537.36");
		options.addArguments("no-sandbox");
		options.addArguments("start-maximized");
		options.addArguments("disable-popup-blocking");
		capabilities.setCapability(ChromeOptions.CAPABILITY, options);
		WebDriverManager.chromedriver().setup();
		
		driver = new ChromeDriver(options);
		
		Map<String, String> set_Tiket_Map; //항공편에 대한 정보 
		ArrayList<Map<String, String>> tiket_List = new ArrayList<Map<String,String>>(); //항공편들 모음
		List<String> price_check = new ArrayList<String>();//티켓 중복 검사를 위해 가격 저장

		try {
			
			driver.get(url);
			Thread.sleep(15000); // 3. 페이지 로딩 대기 시간 15초간 정지  15000
			
			WebElement main_element = driver.findElement(By.xpath("(//html)[1]"));
			
			
			if(Pop_Up1(main_element)) { 
				element = driver.findElement(By.xpath("//button[@class=\"Button-No-Standard-Style close inside darkIcon \"]"));
				element.click();
			}
			if(Pop_Up2(main_element)) {
				element = driver.findElement(By.xpath("//div[@class=\"bBPb-close\"]//span[@class=\"svg\"]"));
				element.click();
			}
			
			log.info(ticket_Check(main_element));
			if(ticket_Check(main_element)) {
				List<WebElement> get_Tickets = main_element.findElements(By.xpath("//div[@class=\"resultWrapper\"]//div[@class=\"resultInner\"]//div[@class=\"inner-grid keel-grid\"]"));
				
				int ticket_num = 1;
				for(WebElement ticket : get_Tickets) {
					set_Tiket_Map = new LinkedHashMap<String, String>();
					System.out.println(get_Tickets.size()+"-"+ticket_num+"------"+ticket.findElement(By.cssSelector(".price-text")).getText());
					
					boolean ticket_check = true; // 중복되지 않은 티켓 유무 
					String price = ticket.findElement(By.cssSelector(".price-text")).getText(); //1인당 가격
					//티켓 중복 검사(1인당 가격으로 비교)
					for(int i = 0; i<price_check.size(); i++) {
						if(price_check.get(i).equals(price)) {
							ticket_check = false;
							break;
						}
					}
					
					price_check.add(price);
					//총 5장만 저장하는데 중복하는 티켓은 없어야됨
					if(ticket_num <= 5 && ticket_check) {
						//출항 편
						WebElement go_area = ticket.findElement(By.cssSelector("li.flight:nth-of-type(1)"));	// 출발
						String go_time = Time(go_area);	// 출항편 시간(출발,도착)					
						String go_flight_Type = Flight_Type(go_area);	// 직항, 경유
						String go_airport = Airport(go_area);	// 출항편 공항(출발,도착)
						String go_waypoint = wayPoint(go_area, 0); //경유지
						String go_waypoint_title = wayPoint(go_area, 1); //경유지
						String go_addendum = Addendum(go_area); //날짜 변동 유무
						String go_flight_time = Flight_time(go_area); // 비행시간
										
						set_Tiket_Map.put("go_time", go_time);
						set_Tiket_Map.put("go_flight_Type", go_flight_Type);
						set_Tiket_Map.put("go_airport", go_airport);
						set_Tiket_Map.put("go_waypoint", go_waypoint);
						set_Tiket_Map.put("go_waypoint_title", go_waypoint_title);
						set_Tiket_Map.put("go_addendum", go_addendum);
						set_Tiket_Map.put("go_flight_time", go_flight_time);
						
						if(flight_Type.equals("RT")) {
							WebElement bak_area = ticket.findElement(By.cssSelector("li.flight:nth-of-type(2)"));	// 반환 	
							//회항 편
							String bak_time = Time(bak_area);	// 출항편 시간(출발,도착)					
							String bak_flight_Type = Flight_Type(bak_area);	// 직항, 경유
							String bak_airport = Airport(bak_area); // 출항편 공항(출발,도착)
							String bak_waypoint = wayPoint(bak_area, 0); // 경유지
							String bak_waypoint_title = wayPoint(bak_area, 1); // 경유지
							String bak_addendum = Addendum(bak_area); // 날짜 변동 유무
							String bak_flight_time = Flight_time(bak_area);
							
							set_Tiket_Map.put("bak_time", bak_time);        
							set_Tiket_Map.put("bak_flight_Type", bak_flight_Type); 
							set_Tiket_Map.put("bak_airport", bak_airport);     
							set_Tiket_Map.put("bak_waypoint", bak_waypoint);
							set_Tiket_Map.put("bak_waypoint_title", bak_waypoint_title);  
							set_Tiket_Map.put("bak_addendum", bak_addendum);    
							set_Tiket_Map.put("bak_flight_time", bak_flight_time); 
						}
	
						String total_price = "총 "+ price;
				
						if(total_Check(ticket)) {
							total_price = ticket.findElement(By.cssSelector(".price-total")).getText();
						}
						
						String ticket_ting = Ticket_ting(ticket);
						
						set_Tiket_Map.put("total_price", total_price);
						set_Tiket_Map.put("price", price);
						set_Tiket_Map.put("ticket_ting", ticket_ting);
							
						tiket_List.add(set_Tiket_Map);
						
						ticket_num++;
					}else if(5 < ticket_num){//if(ticket_num <= 5 && ticket_check)
						break;
					}
					set_Tiket_Map = null;
				}//for(WebElement ticket : get_Tickets)
			}//if(ticket_Check())
			
		}catch (Exception e) {
			e.printStackTrace();
		} finally {
//			driver.close(); // 5. 브라우저 종료
			driver.quit();
			
			//크롬 드라이버 프로세스 삭제
			try {
				Runtime.getRuntime().exec("taskkill /F /IM chromedriver.exe /T");
				Thread.sleep(5000);
			} catch (Exception e) {
				e.printStackTrace();
			}			
		}
		log.info(this.getClass()+".getTicket end");
		return tiket_List;
	}
	
	
}
