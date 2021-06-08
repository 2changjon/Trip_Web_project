package poly.data;

import java.io.File;
import java.io.FileInputStream;
import java.util.ArrayList;
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
	@Scheduled(cron="0 1 * * * ?")  
	public void run() throws Exception {
		Map<String, Map<String, ArrayList<Map<String, String>>>> api_Data = new HashMap<>();
		Map<String, ArrayList<Map<String, String>>> country_Datas = new HashMap<>();
		
		log.info("Mongo Insert Start");
		
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
	
}
