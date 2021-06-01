package poly.persistance.selenium.impl;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
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
	
	@Override
	public ArrayList<Map<String, String>> getTicket(String url, String flight_Type) {
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
				String filepath ="C:\\Users\\2chan\\OneDrive\\바탕 화면\\새 폴더\\test21.txt";
				// 파일 객체 생성
		        File setfile = new File(filepath) ;
		        // true 지정시 파일의 기존 내용에 이어서 작성
		        BufferedWriter fw = new BufferedWriter(new FileWriter(setfile, true));
		        fw.write("-------------------getTicket-------------------");
		        fw.newLine();                   
		        fw.flush();
				
				int i = 0;
				for(WebElement ticket : get_Tickets) {
					set_Tiket_Map = new LinkedHashMap<String, String>();
					System.out.println(get_Tickets.size()+"-"+i+"------"+ticket.findElement(By.cssSelector(".price-text")).getText());
					if(0 < i && i <= 5) {//첫번째 꺼는 추천

						//출항 편
						WebElement go_area = ticket.findElement(By.cssSelector("li.flight:nth-of-type(1)"));	// 출발
						String go_time = Time(go_area);	// 출항편 시간(출발,도착)					
						String go_flight_Type = Flight_Type(go_area);	// 직항, 경유
						String go_airport = Airport(go_area);	// 출항편 공항(출발,도착)
						String go_waypoint = wayPoint(go_area); //경유지
						String go_addendum = Addendum(go_area); //날짜 변동 유무
						String go_flight_time = Flight_time(go_area); // 비행시간
										
						set_Tiket_Map.put("go_time", go_time);
						set_Tiket_Map.put("go_flight_Type", go_flight_Type);
						set_Tiket_Map.put("go_airport", go_airport);
						set_Tiket_Map.put("go_waypoint", go_waypoint);
						set_Tiket_Map.put("go_addendum", go_addendum);
						set_Tiket_Map.put("go_flight_time", go_flight_time);
						
						if(flight_Type.equals("RT")) {
							WebElement bak_area = ticket.findElement(By.cssSelector("li.flight:nth-of-type(2)"));	// 반환 	
							//회항 편
							String bak_time = Time(bak_area);	// 출항편 시간(출발,도착)					
							String bak_flight_Type = Flight_Type(bak_area);	// 직항, 경유
							String bak_airport = Airport(bak_area); // 출항편 공항(출발,도착)
							String bak_waypoint = wayPoint(bak_area); // 경유지
							String bak_addendum = Addendum(bak_area); // 날짜 변동 유무
							String bak_flight_time = Flight_time(bak_area);
							
							set_Tiket_Map.put("bak_time", bak_time);        
							set_Tiket_Map.put("bak_flight_Type", bak_flight_Type); 
							set_Tiket_Map.put("bak_airport", bak_airport);     
							set_Tiket_Map.put("bak_waypoint", bak_waypoint);    
							set_Tiket_Map.put("bak_addendum", bak_addendum);    
							set_Tiket_Map.put("bak_flight_time", bak_flight_time); 
						}
	
						
						String price = ticket.findElement(By.className("price-text")).getText();
						String total_price = price;
				
						if(total_Check(ticket)) {
							total_price = ticket.findElement(By.cssSelector(".price-total")).getText();
						}
						
						String ticket_ting = Ticket_ting(ticket);
						
						set_Tiket_Map.put("total_price", total_price);
						set_Tiket_Map.put("price", price);
						set_Tiket_Map.put("ticket_ting", ticket_ting);
							
						tiket_List.add(set_Tiket_Map);
						fw.write(i+"-"+set_Tiket_Map.toString());
						fw.newLine();                   
						fw.flush(); 
						
					}else if(5 < i){//if(0 < i && i <= 5)
						break;
					}
					set_Tiket_Map = null;
					i++;
					
				}//for(WebElement ticket : get_Tickets)
				fw.close();
			}//if(ticket_Check())
			webTest3(url);
		}catch (Exception e) {
			e.printStackTrace();
		} finally {
//			driver.close(); // 5. 브라우저 종료
			driver.quit();
			
			//크롬 드라이버 프로세스 삭제
			try {
				Runtime.getRuntime().exec("taskkill /F /IM chromedriver.exe /T");
			} catch (IOException e) {
				e.printStackTrace();
			}			
		}
		log.info(this.getClass()+".getTicket end");
		return tiket_List;
	}
	
	
	private void webTest3(String url) {
		System.out.println("webTest3 start");
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
		
		try {
			Thread.sleep(15000);
			Document doc = Jsoup.parse(driver.getPageSource());
			Iterator<Element> ticket_List = doc.getElementsByClass("inner-grid keel-grid").iterator(); //검색된 항공권 결과

			String filepath ="C:\\Users\\2chan\\OneDrive\\바탕 화면\\새 폴더\\test21.txt";
			// 파일 객체 생성
	        File setfile = new File(filepath) ;
	        // true 지정시 파일의 기존 내용에 이어서 작성
	        BufferedWriter fw = new BufferedWriter(new FileWriter(setfile, true));
	        fw.write("-------------------webTest3-------------------");
	        fw.newLine();                   
	        fw.flush();
	        int ticket_num = 0;
			while (ticket_List.hasNext()) {
				//최초 1개는 추천 최대 5개
				if(1 <= ticket_num && ticket_num <= 5) {
					Element ticket = ticket_List.next(); // 티켓 하나
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
							String go_waypoint = wayPoint2(ticket_info);
							String go_addendum = Addendum2(ticket_info);
							String go_flight_time = Flight_time2(ticket_info);
							li_num++;
							
							set_Tiket_Map.put("go_time", go_time);              
							set_Tiket_Map.put("go_flight_Type", go_flight_Type);
							set_Tiket_Map.put("go_airport", go_airport);        
							set_Tiket_Map.put("go_waypoint", go_waypoint);      
							set_Tiket_Map.put("go_addendum", go_addendum);      
							set_Tiket_Map.put("go_flight_time", go_flight_time);
							
						//반환
						}else {
							String bak_time = Time2(ticket_info);               
							String bak_flight_Type = Flight_Type2(ticket_info); 
							String bak_airport = Airport2(ticket_info);         
							String bak_waypoint = wayPoint2(ticket_info);       
							String bak_addendum = Addendum2(ticket_info);       
							String bak_flight_time = Flight_time2(ticket_info); 
							li_num = 1;
							
							set_Tiket_Map.put("bak_time", bak_time);               
							set_Tiket_Map.put("bak_flight_Type", bak_flight_Type); 
							set_Tiket_Map.put("bak_airport", bak_airport);         
							set_Tiket_Map.put("bak_waypoint", bak_waypoint);       
							set_Tiket_Map.put("bak_addendum", bak_addendum);       
							set_Tiket_Map.put("bak_flight_time", bak_flight_time); 
							
						}
					}
					Element price_area = ticket.getElementsByClass("Flights-Results-FlightPriceSection right-alignment sleek largeNumberCurrency").first();
					
					String price = price_area.getElementsByClass("price-text").text();
					String total_price = price_area.getElementsByClass("price-total").text();
					
					if(total_price.equals("")) {
						total_price = price;
					}
					String ticket_ting = Ticket_ting2(price_area);
					
					set_Tiket_Map.put("total_price", total_price);
					set_Tiket_Map.put("price", price);
					set_Tiket_Map.put("ticket_ting", ticket_ting);
					
					tiket_List.add(set_Tiket_Map);
					fw.write(ticket_num+"-"+set_Tiket_Map.toString());
					fw.newLine();                   
					fw.flush();                     
					set_Tiket_Map = null;
						
				} else if(5 < ticket_num){ //if(0 < ticket_num && ticket_num <= 5) 
					break;
				}
				ticket_num++;
			}//while (ticket_List.hasNext())
			
			fw.close();
		} catch (Exception e) {
			e.printStackTrace();
		}finally {
			driver.quit();
			
			//크롬 드라이버 프로세스 삭제
			try {
				Runtime.getRuntime().exec("taskkill /F /IM chromedriver.exe /T");
			} catch (IOException e) {
				e.printStackTrace();
			}	
		}
		System.out.println("webTest3 end");
	}
	
}
