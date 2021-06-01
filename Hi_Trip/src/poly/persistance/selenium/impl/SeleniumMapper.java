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
import org.jsoup.select.Elements;
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
	public ArrayList<Map<String, String>> getTicket(String url) {
		log.info(this.getClass()+".getTicket start");
		// WebDriver 경로 설정
		System.setProperty(WEB_DRIVER_ID, WEB_DRIVER_PATH);
		// 2. WebDriver 옵션 설정
		DesiredCapabilities capabilities = DesiredCapabilities.chrome();
		ChromeOptions options = new ChromeOptions();
		options.addArguments("--headless");
		options.addArguments("--user-agent=Mozilla/5.0 (Windows NT 10.0; Win32; x32) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/90.0.4430.24 Safari/537.36");
		options.addArguments("--no-sandbox");
		options.addArguments("--start-maximized");
		options.addArguments("--disable-popup-blocking");
		capabilities.setCapability(ChromeOptions.CAPABILITY, options);
		WebDriverManager.chromedriver().setup();
		
		driver = new ChromeDriver(options);
		
		Map<String, String> set_Tiket_Map; //항공편에 대한 정보 
		ArrayList<Map<String, String>> tiket_List = new ArrayList<Map<String,String>>(); //항공편들 모음
		
		try {
			
			driver.get(url);
			Thread.sleep(15000); // 3. 페이지 로딩 대기 시간 15초간 정지  15000
			webTest3(url);
			WebElement main_element = driver.findElement(By.xpath("(//html)[1]"));
			
			
			if(Pop_Up1(main_element)) { 
				element = driver.findElement(By.xpath("//button[@class=\"Button-No-Standard-Style close inside darkIcon \"]"));
				element.click();
			}
			if(Pop_Up2(main_element)) {
				element = driver.findElement(By.xpath("//div[@class=\"bBPb-close\"]//span[@class=\"svg\"]"));
				element.click();
			}
			

			if(ticket_Check(main_element)) {
				List<WebElement> get_Tickets = driver.findElements(By.xpath("//div[@class=\"resultWrapper\"]//div[@class=\"resultInner\"]//div[@class=\"inner-grid keel-grid\"]"));
				
				int i = 0;
				for(WebElement ticket : get_Tickets) {
					set_Tiket_Map = new LinkedHashMap<String, String>();
					//출항 편
					WebElement go_area = ticket.findElement(By.xpath("(//div[@class=\"container\"])[1]"));	// 출항편에 관한 정보가 있는 공간
					String go_time = Time(go_area);	// 출항편 시간(출발,도착)					
					String go_flight_Type = Flight_Type(go_area);	// 직항, 경유
					String go_airport = Airport(go_area);	// 출항편 공항(출발,도착)
					String go_stopover = Stopover(go_area); //경유지
					String go_addendum = Addendum(go_area); //날짜 변동 유무
					String go_flight_time = Flight_time(go_area);

					//회항 편
					WebElement bak_area = ticket.findElement(By.xpath("(//div[@class=\"container\"])[2]"));
					String bak_time = Time(bak_area);	// 출항편 시간(출발,도착)					
					String bak_flight_Type = Flight_Type(bak_area);	// 직항, 경유
					String bak_airport = Airport(bak_area); // 출항편 공항(출발,도착)
					String bak_stopover = Stopover(bak_area); // 경유지
					String bak_addendum = Addendum(bak_area); // 날짜 변동 유무
					String bak_flight_time = Flight_time(bak_area);
					
					String price = ticket.findElement(By.xpath("//div[@class=\"multibook-dropdown\"]//span[@class=\"price-text\"]")).getText();
					String total_price = price;

					if(super.total_Check(ticket)) {
						total_price = ticket.findElement(By.xpath("//div[@class=\"multibook-dropdown\"]//div[@class=\"price-total\"]")).getText();
					}
					
					String ticket_ting = Ticket_ting(ticket);
					
					if(i<5) {
						set_Tiket_Map.put("total_price", total_price);
						set_Tiket_Map.put("price", price);
						set_Tiket_Map.put("ticket_ting", ticket_ting);
						set_Tiket_Map.put("go_time", go_time);
						set_Tiket_Map.put("go_flight_Type", go_flight_Type);
						set_Tiket_Map.put("go_airport", go_airport);
						set_Tiket_Map.put("go_stopover", go_stopover);
						set_Tiket_Map.put("go_addendum", go_addendum);
						set_Tiket_Map.put("go_flight_time", go_flight_time);
						set_Tiket_Map.put("bak_time", bak_time);        
						set_Tiket_Map.put("bak_flight_Type", bak_flight_Type); 
						set_Tiket_Map.put("bak_airport", bak_airport);     
						set_Tiket_Map.put("bak_stopover", bak_stopover);    
						set_Tiket_Map.put("bak_addendum", bak_addendum);    
						set_Tiket_Map.put("bak_flight_time", bak_flight_time); 
						
						tiket_List.add(set_Tiket_Map);
						set_Tiket_Map = null;
						
					}else {
						break;
					}
					i++;
				}//for

			}//if(ticket_Check())
			
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
	
	
	public void webTest3(String url) {
		
		try {
			Document doc = Jsoup.connect(url).get();
			Elements element = doc.select("div.resultsContainer"); //검색된 항공권 결과
			Iterator<Element> ticket_List = element.select("div.resultWrapper").select("div.inner-grid keel-grid").iterator();
			int a=0;
			String filepath ="C:\\Users\\2chan\\OneDrive\\바탕 화면\\새 폴더\\test13.txt";
			// 파일 객체 생성
	        File setfile = new File(filepath) ;
	        // true 지정시 파일의 기존 내용에 이어서 작성
	        BufferedWriter fw = new BufferedWriter(new FileWriter(setfile, true));
			while (ticket_List.hasNext()) {
				a+=1;
				Element ticket = ticket_List.next(); // 티켓 하나
				Iterator<Element> ticket_infos = ticket.getElementsByClass("container").iterator(); //티켓 정보
				while (ticket_infos.hasNext()) {
					Element ticket_info = ticket_infos.next(); //출발 관련 1개 도착 관련 1개
					
					String go_time;
					String go_flight_Type;
					String go_airport;
					String go_stopover;
					String go_addendum;
					String go_flight_time;
					
					String bak_time;
					String bak_flight_Type;
					String bak_airport;
					String bak_stopover;
					String bak_addendum;
					String bak_flight_time;
					
					
				}
				String price;
				String total_price;
				
				String ticket_ting;
			}
			fw.write("");
			fw.newLine();
			fw.flush();
			System.out.println(a);
			fw.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
}
