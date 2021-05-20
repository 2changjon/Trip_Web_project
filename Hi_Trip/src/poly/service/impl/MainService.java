package poly.service.impl;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.log4j.Logger;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.springframework.stereotype.Service;
import org.springframework.util.ResourceUtils;

import java.io.File;
import java.io.FileInputStream;
import java.util.regex.Pattern;

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
	
	@Override
	public ArrayList<String> getserch_list(String keyWord) throws Exception {
//		log.info(this.getClass()+"getserch_list start");
		
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
					log.info(serch.length+"개 검색됨");
					serch_list.add(serch[2]+" "+serch[1]);	
				}		
			} else {
				//한글로 검색시
				if(serch[2].contains(keyWord)) {
					log.info(serch.length+"개 검색됨");
					serch_list.add(serch[2]+" "+serch[1]);	
				}
			}
		}
		
//		log.info(this.getClass()+"getserch_list end");
		
		return serch_list;
	}
	
	@Override
	public ArrayList<String> getplace_List(String country_nm) {
		
		ArrayList<String> place_List = new ArrayList<String>();
		
		try {
			Unirest.setTimeouts(0, 0);
			HttpResponse<String> response = Unirest.get("https://skyscanner-skyscanner-flight-search-v1.p.rapidapi.com/apiservices/autosuggest/v1.0/KR/KRW/ko-KR/?query="+country_nm)
					.header("x-rapidapi-key", apiKeys.rapidapi_key)
					.header("x-rapidapi-host", "skyscanner-skyscanner-flight-search-v1.p.rapidapi.com")
					.asString();
			//null 체크
			if(response == null) {
				response = new HttpResponse<String>(null, null);
			}
			
			JSONParser jsonParse = new JSONParser();
			
			JSONObject jsonObj = (JSONObject) jsonParse.parse(response.getBody().toString());
			JSONArray personArray = (JSONArray) jsonObj.get("Places");

			//null 체크
			if(personArray == null) {
				personArray = new JSONArray();
				log.info("personArray null");
			}
			
			for(int i=0; i < personArray.size(); i++) { 
				JSONObject personObject = (JSONObject) personArray.get(i);
				//국가 검색 제한
				if(personObject.get("PlaceId") != personObject.get("CountryId")) {
					place_List.add(personObject.get("PlaceName").toString()+"  ("+personObject.get("PlaceId").toString().replace("-sky", "")+")");
				}
				personObject = null;
			}
			
			// 사용이 완료된 객체는 메모리에서 강제로 비우기
			response = null;
			jsonParse = null;
			jsonObj = null;
			personArray = null;
			
			return place_List;
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return place_List;
	}
	
}
