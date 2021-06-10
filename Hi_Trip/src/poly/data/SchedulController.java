package poly.data;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.util.ResourceUtils;
import org.springframework.web.bind.annotation.ResponseBody;

import com.univocity.parsers.tsv.TsvParser;
import com.univocity.parsers.tsv.TsvParserSettings;

import poly.service.IMongoService;
import poly.util.DateUtil;

@Configuration
@EnableScheduling
public class SchedulController { 
	
	// 로그 파일 생성 및 로그 출력을 위한 log4j 프레임워크의 자바 객체 private
	private Logger log = Logger.getLogger(this.getClass());
	
	// api 파싱 데이터 가져오기
	ApiParser apiParser = new ApiParser();
	
	@Resource(name = "MongoService")
	private IMongoService mongoService;
	
	ArrayList<String> country_List = new ArrayList<String>();
	ArrayList<String> country_En_List = new ArrayList<String>();
	
	@ResponseBody
	@Scheduled(cron="0 0 5 * * ?")  
	public void run() throws Exception {
		Map<String, Map<String, ArrayList<Map<String, String>>>> api_Data = new HashMap<>();
		Map<String, ArrayList<Map<String, String>>> country_Datas = new HashMap<>();
		
		log.info("Mongo Insert Start");

		//country_List 빈값인지
		if(country_List.isEmpty()) {
			
			File readFile = ResourceUtils.getFile("classpath:/poly/data/world_country_data.tsv");
			TsvParserSettings settings = new TsvParserSettings();
			settings.getFormat().setLineSeparator("\n"); // tsv 읽을때 나눌 기준
			
			// TSV parser 생성
			TsvParser parser = new TsvParser(settings);
			// tsv 파일 내용 전체 가져오기
			List<String[]> country_Data = parser.parseAll(new FileInputStream(readFile));
			//첫번째 줄은 목록이기 때문에 삭제를 해준다
			country_Data.remove(0);
			Iterator<String[]> list = country_Data.iterator();
			
			while(list.hasNext()) {
				String[] serch = list.next();
				country_List.add(serch[2].replace(" ", ""));
				country_En_List.add(serch[1].replace(" ", ""));
			}
			// 사용이 완료된 객체는 메모리에서 강제로 비우기
			readFile = null;
			settings = null;
			parser = null;
			country_Data = null;
			list = null;
			
		}
		log.info("country_List: "+country_List);
		
		Iterator<String> countrys = country_List.iterator();
		Iterator<String> countryEns = country_En_List.iterator();
		
		while(countrys.hasNext()) {
			String country_nm = countrys.next();
			String country_En_nm = countryEns.next();
			country_Datas = new HashMap<>();
			country_Datas.put("dangerous_News", apiParser.Dangerous_News(country_nm)); //위험 경보 뉴스
			country_Datas.put("safety_notice_news", apiParser.Safety_Notice_News(country_nm)); //안전 공지 뉴스
			country_Datas.put("accident_type_news", apiParser.Accident_Type_News(country_nm)); //사건사고 유형 뉴스
			country_Datas.put("local_contact_news", apiParser.Local_Contact_News(country_nm)); //현지 연락처 뉴스
			country_Datas.put("travel_alert", apiParser.Travel_Alert(country_nm)); //여행위험 경보
			country_Datas.put("special_travel", apiParser.Special_Travel_Alert(country_nm)); //특별여행 위험 경보
			country_Datas.put("travel_prohibited", apiParser.Travel_Prohibited(country_nm)); //여행금지 경보
			
			api_Data.put(country_En_nm, country_Datas);
			country_Datas = null;
		}
		
		boolean success = mongoService.insertMongo(api_Data);

		// 사용이 완료된 객체는 메모리에서 강제로 비우기
		countrys = null;
		countryEns = null;
		api_Data = null;
		country_Datas = null;
		
		if(success) {
			log.info("Mongo Insert End");
		}else {
			log.info("Mongo Insert Error");
		}
	}
	
	@Scheduled(cron="0 0 0 * * ?") 
	public void reset_txt() {
		log.info("Mongo Insert reset_txt Start");
		//날짜
        SimpleDateFormat date = new SimpleDateFormat("yyyyMMdd");
        
        BufferedWriter fw;//쓰기
        
        //숫자 검사
        String num_ch = "^[0-9]*$";
        //국가검색
        try {
        	//국가검색
			Map<String, String> country_Map;
			ArrayList<Map<String, String>> country_List = new ArrayList<Map<String,String>>(); 

			//파일 객체
			File readFile_Country = ResourceUtils.getFile("classpath:/poly/data/country_Serch_Data.txt");//국가
			
			//파일 읽기 객체 생성
	        FileReader filereader_Country = new FileReader(readFile_Country);
	        
	        //읽기 버퍼 생성
	        BufferedReader bufReader_Country = new BufferedReader(filereader_Country); //첫번째 줄은 언제나 날짜
	        
	        //처음 날짜는 두 파일 다 동일 시작일부터 월요일전까지
	        String days = bufReader_Country.readLine();
	        
	        if(days.matches(num_ch)) {
		        String line = "";
		        int i =0;
		        log.info("--------------------------");
		        //국가 검색 파일 한줄씩 읽기
		        while((line = bufReader_Country.readLine()) != null){
		        	if(0<i) {
		        		country_Map = new HashMap<String, String>();
		        		country_Map.put("serch", line);
		        		country_List.add(country_Map);
		        		log.info(country_List);
		        		country_Map = null;
		        	}
		        	i++;
		        }	
		        log.info("--------------------------");
		        
		        bufReader_Country.close();
		        
		        boolean success = mongoService.insert_serch("serch_country_"+days, days+" ~ "+DateUtil.getDateTime("yyyyMMdd"), country_List);
		        
		        country_List = null;
		        
		        if(success) {
					log.info("Mongo serch_country Insert End");
				}else {
					log.info("Mongo serch_country Insert Error");
				}
	        }

        	Date eveday  = date.parse(bufReader_Country.readLine());//이전 날짜
        	Date today = date.parse(DateUtil.getDateTime("yyyyMMdd"));//오늘
	        //시,분,초,밀리초        	
        	long difference = Math.abs( (eveday.getTime()/(24*60*60*1000))-(today.getTime()/(24*60*60*1000))); //차이
        	
        	//월요일로 초기화
			if(1 == difference || !days.matches(num_ch)) {
				fw = new BufferedWriter(new FileWriter(readFile_Country));
				fw.write(DateUtil.getDateTime("yyyyMMdd").toString());
				fw.newLine();
				fw.flush();
				fw.close();
			}
		} catch (Exception e) {
			log.info("serch_country_ERR = "+e);
		}
        
        //티켓검색
        try {
        	//티켓검색
			Map<String, String> ticket_Map =new HashMap<String, String>();
			ArrayList<Map<String, String>> ticket_List = new ArrayList<Map<String,String>>();
			
			File readFile_Ticket = ResourceUtils.getFile("classpath:/poly/data/ticket_Serch_Data.txt");//티켓
			
			FileReader filereader_Ticket = new FileReader(readFile_Ticket);
			
			BufferedReader bufReader_Ticket = new BufferedReader(filereader_Ticket); //첫번째 줄은 언제나 날짜
			
			//처음 날짜는 두 파일 다 동일 시작일부터 월요일전까지
	        String days = bufReader_Ticket.readLine();
	        
	        if(days.matches(num_ch)) {
		        log.info("--------------------------");
				String line = "";
		        //티켓 검색 파일 한줄씩 읽기
				int i= 0;
		        while((line = bufReader_Ticket.readLine()) != null){
		        	if(0<i) {
			        	ticket_Map = new HashMap<String, String>();
			        	ticket_Map.put("serch", line);
			        	ticket_List.add(ticket_Map);
			        	log.info(ticket_List);
			        	ticket_Map = null;
		        	}
		        	i++;
		        }
		        log.info("--------------------------");
		        bufReader_Ticket.close();
		        boolean success2 = mongoService.insert_serch("serch_ticket_"+days, days+" ~ "+DateUtil.getDateTime("yyyyMMdd"), ticket_List);
		        
		        if(success2) {
					log.info("Mongo serch_ticket Insert End");
				}else {
					log.info("Mongo serch_ticket Insert Error");
				}
	        }
	        Date eveday  = date.parse(bufReader_Ticket.readLine());//이전 날짜
        	Date today = date.parse(DateUtil.getDateTime("yyyyMMdd"));//오늘
	        //시,분,초,밀리초        	
        	long difference = Math.abs( (eveday.getTime()/(24*60*60*1000))-(today.getTime()/(24*60*60*1000))); //차이
	        
	        //월요일로 초기화
			if(1 == difference || !days.matches(num_ch)) {
				fw = new BufferedWriter(new FileWriter(readFile_Ticket));
				fw.write(DateUtil.getDateTime("yyyyMMdd").toString());
				fw.newLine();
				fw.flush();
				fw.close();
			}
		} catch (Exception e) {
			log.info("serch_ticket_ERR = "+e);
		}
	
		log.info("Mongo Insert reset_txt End");
	}
	
	
}
