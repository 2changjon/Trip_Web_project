package poly.service.impl;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

import org.apache.log4j.Logger;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.springframework.stereotype.Service;
import org.springframework.util.ResourceUtils;

import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.Unirest;
import com.univocity.parsers.tsv.TsvParser;
import com.univocity.parsers.tsv.TsvParserSettings;

import poly.data.ApiKeys;
import poly.service.IMainService;

@Service("MainService")
public class MainService implements IMainService {
	
	private Logger log =Logger.getLogger(this.getClass());
	
	//api 키 가져오기
	ApiKeys apiKeys = new ApiKeys();
	//국가 검색
	@Override
	public ArrayList<String> getserch_list(String keyWord) throws Exception {
		log.info(this.getClass()+"getserch_list start");
		
		String eng = "^[a-zA-Z]*$"; //영어를 찾아내는 정규식
		boolean check = Pattern.matches(eng, keyWord); // 입력값에 영어가 있으면 영문 리스트에서 찾기
		
		File employeesFile = ResourceUtils.getFile("classpath:/poly/data/world_country_data.tsv");
		
		TsvParserSettings settings = new TsvParserSettings();
		settings.getFormat().setLineSeparator("\n"); // tsv 읽을때 나눌 기준

		// TSV parser 생성
		TsvParser parser = new TsvParser(settings);
		
		// tsv 파일 내용 전체 가져오기
		List<String[]> file_data = parser.parseAll(new FileInputStream(employeesFile));
		//첫번째 줄은 목록이기 때문에 삭제를 해준다
		file_data.remove(0);

		Iterator<String[]> list = file_data.iterator(); 
		
		ArrayList<String> serch_list = new ArrayList<String>();
		
		while(list.hasNext()) {
			String[] serch = list.next();
			//serch[0] = number
			if(check) {
				//영어로 검색시 keyWord가 serch[1]에  contains(포함) 되어있으면
				if(serch[1].toUpperCase().contains(keyWord.toUpperCase())) {
//					log.info(serch.length+"개 검색됨");
					serch_list.add(serch[2]+" "+serch[1]);	
				}		
			} else {
				//한글로 검색시
				if(serch[2].contains(keyWord)) {
//					log.info(serch.length+"개 검색됨");
					serch_list.add(serch[2]+" "+serch[1]);	
				}
			}
		}
		
		log.info(this.getClass()+"getserch_list end");
		
		return serch_list;
	}
	//Ticket_Serch_Data.txt 에 저장
	@Override
	public boolean insert_ticket_serch(String air_cn) {
		log.info(this.getClass()+"insert_ticket_serch start");
		boolean success = false;
		try {
			File ticket_Serch_Data = ResourceUtils.getFile("classpath:/poly/data/ticket_Serch_Data.txt");
			BufferedWriter ticket_Serch_Bfw = new BufferedWriter(new FileWriter(ticket_Serch_Data, true));
			ticket_Serch_Bfw.write(air_cn);
			ticket_Serch_Bfw.newLine();
			ticket_Serch_Bfw.flush();
			ticket_Serch_Bfw.close();
			
			success = true;
		} catch (Exception e) {
			success = false;
		}
		log.info(this.getClass()+"insert_ticket_serch end");
		return success;
	}
	//Country_Serch_Data.txt 에 저장
	@Override
	public boolean insert_country_serch(String country_nm) {
		log.info(this.getClass()+"insert_country_serch start");
		boolean success = false;
		try {
			File country_Serch_Data = ResourceUtils.getFile("classpath:/poly/data/country_Serch_Data.txt");
			BufferedWriter country_Serch_Bfw = new BufferedWriter(new FileWriter(country_Serch_Data, true));
			country_Serch_Bfw.write(country_nm);
			country_Serch_Bfw.newLine();
			country_Serch_Bfw.flush();
			
			country_Serch_Bfw.close();
			success = true;
			
		} catch (Exception e) {
			success = false;
		}
		log.info(this.getClass()+"insert_country_serch end");
		return success;
	}
	//현재 날씨
	@Override
	public Map<String, String> findWeather(String area_id) {
	
		Map<String, String> weather_Map = new HashMap<String, String>();
		
		try {
		
			Unirest.setTimeouts(0, 0);
			HttpResponse<String> response = Unirest.get("http://api.openweathermap.org/data/2.5/weather?id="+area_id+"&appid="+apiKeys.weather_key+"&lang=kr")
			  .asString();
			
			//null 체크
			if(response == null) {
				response = new HttpResponse<String>(null, null);
			}
			
			JSONParser jsonParse = new JSONParser();
			JSONObject jsonObj = (JSONObject) jsonParse.parse(response.getBody().toString());
			JSONArray personArray = (JSONArray) jsonObj.get("weather");
			
			//null 체크
			if(personArray == null) {
				personArray = new JSONArray();
				log.info("personArray null");
			}
			
			for(int i=0; i < personArray.size(); i++) { 
				JSONObject personObject = (JSONObject) personArray.get(i);
				weather_Map.put("kr_weather", personObject.get("description").toString());
				weather_Map.put("en_weather", personObject.get("main").toString());
			}
			
			System.out.println(weather_Map);
		} catch (Exception e) {
			e.printStackTrace();
		}

		return weather_Map;
	}
	
}
