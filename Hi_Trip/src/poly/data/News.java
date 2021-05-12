package poly.data;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

//파싱
import org.json.XML;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.Unirest;

import org.apache.log4j.Logger;

//import poly.persistance.mongo.IMongoMapper;

@EnableScheduling
public class News {
	// 로그 파일 생성 및 로그 출력을 위한 log4j 프레임워크의 자바 객체 private
	private Logger log = Logger.getLogger(this.getClass());
	
	private static int count = 0;
	
//	@Resource(name = "MongoMapper")
//	private IMongoMapper mongoMapper;
	
//	Côte D'Ivoire	코트디부아르 조심
	@Scheduled(cron="30 * * * * *")  
	public void run() throws Exception {
		News news = new News();	
		news.Local_Contact_News();
	}

	//현지 연락처 뉴스
	public void Local_Contact_News() {
		System.out.println("===============Local_Contact_News=============");
		
		Map<String, Map<String, ArrayList<Map<String, String>>>> contry_Map = new LinkedHashMap<String, Map<String,ArrayList<Map<String,String>>>>();
		Map<String, ArrayList<Map<String, String>>> news_Map = new LinkedHashMap<String, ArrayList<Map<String, String>>>();
		
		try {
			Unirest.setTimeouts(0, 0);
			HttpResponse<String> response = Unirest.get("http://apis.data.go.kr/1262000/LocalContactService2/getLocalContactList2?serviceKey=beBygLcpPjkumsekp0oNS%2BPqEAP%2BV8bAP2Vyp4Wlg7qhHE63JCOJOcDHolv7ZZnIXm08QPMWz9l9nhsGa0znmw%3D%3D&numOfRows=1268")
			  .asString();
			
			//null 체크
			if(response == null) {
				response = new HttpResponse<String>(null, null);
			}
			
			JSONParser jsonParse = new JSONParser();
			
			JSONObject jsonObj = (JSONObject) jsonParse.parse(response.getBody().toString());
			JSONArray personArray = (JSONArray) jsonObj.get("data");

			//null 체크
			if(personArray == null) {
				personArray = new JSONArray();
				log.info("null");
	
			}
			
			String country_nm = "한";
			for(int i=0; i < personArray.size(); i++) { 
				JSONObject personObject = (JSONObject) personArray.get(i);
				if(country_nm.equals((String) personObject.get("country_nm"))) {
				}else {
					news_Map = new HashMap<String, ArrayList<Map<String, String>>>();
					country_nm = (String) personObject.get("country_nm");
//					System.out.println(country_nm+"="+Alert_News(country_nm));
					news_Map.put("alert_news", Alert_News(country_nm));
//					System.out.println(news_Map);
					contry_Map.put(country_nm, news_Map);
					news_Map = null;
					System.out.println(contry_Map.get(country_nm));
				}
//				System.out.println(country_nm+" "+i+" "+contry_Map);
			}
		}catch (Exception e) {
			count++;
			log.info("Local_Contact_News 에러 횟수 :"+count+" 에러 종류 :"+e);
		}finally {
			log.info("Local_Contact_News 에러 횟수 :"+count);
			if(count == 6) {
				count = 0;
			}
			
			System.out.println("===============end=============");
		}
	
	}	
	
	//위험 경보 뉴스	
	public ArrayList<Map<String, String>> Alert_News(String country_nm) {
		
		ArrayList<Map<String, String>> alert_News_list = new ArrayList<Map<String,String>>();
		Map<String, String> alert_News_map = new HashMap<String, String>();
		
		try {
			/* 
			 * Gson은 구글에서 만든 라이브러리로 자바객체를 json으로 json을 자바 객체로 만들 수 있음
			 * HMashape의 경량 HTTP 클라이언트 라이브러리 
			 * Unirest는 post, get, put, delete 등 기본적인 4가지 요청 외에 patch, options까지 지원
			 * asJsonAsync, asBinaryAsync등의 메소드를 이용해서 비동기식 요청 가능
			 */
			Unirest.setTimeouts(0, 0);
			HttpResponse<String> response = Unirest.get("http://apis.data.go.kr/1262000/CountryHistoryService2/getCountryHistoryList2?serviceKey=beBygLcpPjkumsekp0oNS%2BPqEAP%2BV8bAP2Vyp4Wlg7qhHE63JCOJOcDHolv7ZZnIXm08QPMWz9l9nhsGa0znmw%3D%3D&returnType=JSON&pageNo=1&numOfRows=5&cond[country_nm::EQ]="+country_nm)
			  .asString();
			/*
			 * Unirest.post(url).headers(header).queryString(params).asString(); // 문자열로 파라미터를 전달 후 응답을 String형으로 받음
			 * Unirest.post(url).headers(header).queryString(params).asJson(); // 문자열로 파라미터를 전달 후 응답을 Json형태로 받음
			 * Unirest.post(url).headers(header).body(params); // Json형태로 파라미터를 보낼 때 사용
			 * Unirest.post(url).headers(header).field("file", new File("/file/file1234.jpg")); // front-end 기준 formData / Back-End기준 multipartRequest를 이용하는 것 처럼(파일전송가능)
			 * 
			 * http://kong.github.io/unirest-java/#requests
			 */
			
			//null 체크
			if(response == null) {
				response = new HttpResponse<String>(null, null);
			}
			
			JSONParser jsonParse = new JSONParser();
			/*
			 * 응답결과를 볼수 있는 방법
			 * .getBody() 응답결과의 body의 내용을 볼 수 있음
			 * .getHeaders() 응답결과의 Header의 내용을 볼 수 있음
			 * .getStatus() 응답결과 상태를 볼 수 있음.
			 */
			JSONObject jsonObj = (JSONObject) jsonParse.parse(response.getBody().toString());
			JSONArray personArray = (JSONArray) jsonObj.get("data");
			
			//null 체크
			if(personArray == null) {
				personArray = new JSONArray();
				log.info("null");
			}
			
			for(int i=0; i < personArray.size(); i++) { 
				alert_News_map = new HashMap<String, String>();
				JSONObject personObject = (JSONObject) personArray.get(i);
				
				if(country_nm.equals((String) personObject.get("country_nm"))) {
//					System.out.printf("%s\t%s\t%s\n",personObject.get("country_eng_nm"),personObject.get("country_nm"),personObject.get("wrt_dt"));
					alert_News_map.put("title", (String) personObject.get("title"));
//					alert_News_map.put("txt_origin_cn", (String) personObject.get("txt_origin_cn"));
					alert_News_map.put("wrt_dt", (String) personObject.get("wrt_dt"));
					alert_News_list.add(alert_News_map);
					alert_News_map = null;
				}
			}
			
		}catch (Exception e) {
			count++;
			log.info("Alert_News 에러 횟수 :"+count+" 에러 종류 :"+e);		
			return null;
		}finally {
			log.info("Alert_News 에러 횟수 :"+count);
		}
		
		return alert_News_list;

	}
	
	//안전 공지 뉴스
	public String Safety_Notice_News(String country_nm) {
		System.out.println("===============Safety_Notice_News=============");
		
		try {
			Unirest.setTimeouts(0, 0);
			HttpResponse<String> response = Unirest.get("http://apis.data.go.kr/1262000/CountrySafetyService2/getCountrySafetyList2?serviceKey=beBygLcpPjkumsekp0oNS%2BPqEAP%2BV8bAP2Vyp4Wlg7qhHE63JCOJOcDHolv7ZZnIXm08QPMWz9l9nhsGa0znmw%3D%3D&returnType=JSON&pageNo=1&numOfRows=5&cond[country_nm::EQ]="+country_nm)
			  .asString();
			
			//null 체크
			if(response == null) {
				response = new HttpResponse<String>(null, null);
			}
			
			JSONParser jsonParse = new JSONParser();
			
			JSONObject jsonObj = (JSONObject) jsonParse.parse(response.getBody().toString());
			JSONArray personArray = (JSONArray) jsonObj.get("data");
			
			//null 체크
			if(personArray == null) {
				personArray = new JSONArray();
				log.info("null");

			}
			
			for(int i=0; i < personArray.size(); i++) { 
				JSONObject personObject = (JSONObject) personArray.get(i);
				if(country_nm.equals((String) personObject.get("country_nm"))) {
				}else {
					country_nm = (String) personObject.get("country_nm");
					System.out.printf("%s\t%s\t%s\n",personObject.get("country_eng_nm"),personObject.get("country_nm"),personObject.get("wrt_dt"));
				}
					
			}
		}catch (Exception e) {
			count++;
			log.info("Safety_Notice_News 에러 횟수 :"+count+" 에러 종류 :"+e);
			
		}finally {
			log.info("Safety_Notice_News 에러 횟수 :"+count);
			if(count == 6) {
				count = 0;
			}
			System.out.println("===============end=============");
		}
		return null;

	}
	
	//사건사고 유형 뉴스
	public String Accident_Type_News(String country_nm) {
		System.out.println("===============Accident_Type_News=============");

		try {
			Unirest.setTimeouts(0, 0);
			HttpResponse<String> response = Unirest.get("http://apis.data.go.kr/1262000/CountryAccidentService2/CountryAccidentService2?serviceKey=beBygLcpPjkumsekp0oNS%2BPqEAP%2BV8bAP2Vyp4Wlg7qhHE63JCOJOcDHolv7ZZnIXm08QPMWz9l9nhsGa0znmw%3D%3D&returnType=JSON&pageN=1&numOfRows=5&cond[country_nm::EQ]="+country_nm)
			  .asString();
			
			//null 체크
			if(response == null) {
				response = new HttpResponse<String>(null, null);
			}
			
			JSONParser jsonParse = new JSONParser();
			
			JSONObject jsonObj = (JSONObject) jsonParse.parse(response.getBody().toString());
			JSONArray personArray = (JSONArray) jsonObj.get("data");
			
			//null 체크
			if(personArray == null) {
				personArray = new JSONArray();
				log.info("null");

			}
			
			for(int i=0; i < personArray.size(); i++) { 
				JSONObject personObject = (JSONObject) personArray.get(i);
				if(country_nm.equals((String) personObject.get("country_nm"))) {
				}else {
					country_nm = (String) personObject.get("country_nm");
					System.out.printf("%s\t%s\t%s\n",personObject.get("country_eng_nm"),personObject.get("country_nm"),personObject.get("wrt_dt"));
				}
				
			}
		}catch (Exception e) {
			count++;
			log.info("Accident_Type_News 에러 횟수 :"+count+" 에러 종류 :"+e);
		}finally {
			log.info("Accident_Type_News 에러 횟수 :"+count);
			if(count == 6) {
				count = 0;
			}
			System.out.println("===============end=============");
		}
		return null;

	}
	
	//위험 경보
	public String Travel_Alert(String country_nm) {
		System.out.println("===============Travel_Alert=============");
		try {
			Unirest.setTimeouts(0, 0);
			HttpResponse<String> response = Unirest.get("http://apis.data.go.kr/1262000/TravelWarningService/getTravelWarningList?serviceKey=beBygLcpPjkumsekp0oNS%2BPqEAP%2BV8bAP2Vyp4Wlg7qhHE63JCOJOcDHolv7ZZnIXm08QPMWz9l9nhsGa0znmw%3D%3D&numOfRows=10&pageNo=1")
			  .asString();
			
			//null 체크
			if(response == null) {
				response = new HttpResponse<String>(null, null);
			}

		    org.json.JSONObject joObject = XML.toJSONObject(response.getBody().toString());
		    org.json.JSONObject xmlresponse = (org.json.JSONObject) joObject.get("response");
		    
		    org.json.JSONObject body = (org.json.JSONObject) xmlresponse.get("body");
		    org.json.JSONObject items = (org.json.JSONObject) body.get("items");
		    org.json.JSONArray personArray =  (org.json.JSONArray) items.get("item");
		    
		  //null 체크
			if(personArray == null) {
				personArray = new org.json.JSONArray();
				log.info("null");

			}
			
			for(int i=0; i < personArray.length(); i++) { 
				org.json.JSONObject personObject = (org.json.JSONObject) personArray.get(i);
				if(country_nm.equals((String) personObject.get("countryName"))) {
				}else {
					country_nm = (String) personObject.get("countryName");
					//has == "limitaPartial"가 있는 경우 체크
					if(personObject.has("limitaPartial")) {
						System.out.printf("%s\t%s\t%s\t%s\n",personObject.get("countryEnName"),personObject.get("countryName"),personObject.get("limitaPartial"),personObject.get("wrtDt"));
					}else {
						System.out.printf("%s\t%s\t%s\n",personObject.get("countryEnName"),personObject.get("countryName"),personObject.get("wrtDt"));
					}
				}
				
			}

		}catch (Exception e) {
			count++;
			log.info("Travel_Alert 에러 횟수 :"+count+" 에러 종류 :"+e);  
		}finally {
			log.info("Travel_Alert 에러 횟수 :"+count);
			if(count == 6) {
				count = 0;
			}
			System.out.println("===============end=============");
		}
		return null;
	}
	
	//특수 위험 경보	
	public String Special_Travel_Alert(String country_nm) {
		System.out.println("===============Special_Travel_Alert=============");
		try {
			Unirest.setTimeouts(0, 0);
			HttpResponse<String> response = Unirest.get("http://apis.data.go.kr/1262000/TravelSpecialWarningService/getTravelSpecialWarningList?serviceKey=beBygLcpPjkumsekp0oNS%2BPqEAP%2BV8bAP2Vyp4Wlg7qhHE63JCOJOcDHolv7ZZnIXm08QPMWz9l9nhsGa0znmw%3D%3D&pageNo=1&numOfRows=10")
			  .asString();
			
			//null 체크
			if(response == null) {
				response = new HttpResponse<String>(null, null);
			}

		    org.json.JSONObject joObject = XML.toJSONObject(response.getBody().toString());
		    org.json.JSONObject xmlresponse = (org.json.JSONObject) joObject.get("response");
		    
		    org.json.JSONObject body = (org.json.JSONObject) xmlresponse.get("body");
		    org.json.JSONObject items = (org.json.JSONObject) body.get("items");
		    org.json.JSONArray personArray =  (org.json.JSONArray) items.get("item");
		    
		  //null 체크
			if(personArray == null) {
				personArray = new org.json.JSONArray();
				log.info("null");

			}
			
			for(int i=0; i < personArray.length(); i++) { 
				org.json.JSONObject personObject = (org.json.JSONObject) personArray.get(i);
				if(country_nm.equals((String) personObject.get("countryName"))) {
				}else {
					country_nm = (String) personObject.get("countryName");
					//has == "limitaPartial"가 있는 경우 체크
					if(personObject.has("splimit")) {					
						System.out.printf("%s\t%s\t%s\t%s\t%s\n",personObject.get("countryEnName"),personObject.get("countryName"),personObject.get("splimit"),personObject.get("splimitNote"),personObject.get("wrtDt"));
					}else if(personObject.has("splimitPartial")) {
						System.out.printf("%s\t%s\t%s\t%s\t%s\n",personObject.get("countryEnName"),personObject.get("countryName"),personObject.get("splimitPartial"),personObject.get("splimitNote"),personObject.get("wrtDt"));
					}else {
						System.out.printf("%s\t%s\t%s\n",personObject.get("countryEnName"),personObject.get("countryName"),personObject.get("wrtDt"));
					}
				}
				
			}

		}catch (Exception e) {
			count++;
			log.info("Special_Travel_Alert 에러 횟수 :"+count+" 에러 종류 :"+e);  
		}finally {
			log.info("Special_Travel_Alert 에러 횟수 :"+count);
			if(count == 6) {
				count = 0;
			}
			System.out.println("===============end=============");
		}
		return null;
	}
	
	//여행금지 경보	
	public String Travel_Prohibited(String country_nm) {
		System.out.println("===============Travel_Prohibited=============");
		try {
			Unirest.setTimeouts(0, 0);
			HttpResponse<String> response = Unirest.get("http://apis.data.go.kr/1262000/TravelBanService/getTravelBanList?serviceKey=beBygLcpPjkumsekp0oNS%2BPqEAP%2BV8bAP2Vyp4Wlg7qhHE63JCOJOcDHolv7ZZnIXm08QPMWz9l9nhsGa0znmw%3D%3D&numOfRows=10&pageNo=1")
			  .asString();
			
			//null 체크
			if(response == null) {
				response = new HttpResponse<String>(null, null);
			}
	
		    org.json.JSONObject joObject = XML.toJSONObject(response.getBody().toString());
		    org.json.JSONObject xmlresponse = (org.json.JSONObject) joObject.get("response");
		    
		    org.json.JSONObject body = (org.json.JSONObject) xmlresponse.get("body");
		    org.json.JSONObject items = (org.json.JSONObject) body.get("items");
		    org.json.JSONArray personArray =  (org.json.JSONArray) items.get("item");
		    
		  //null 체크
			if(personArray == null) {
				personArray = new org.json.JSONArray();
				log.info("null");
	
			}
			
			for(int i=0; i < personArray.length(); i++) { 
				org.json.JSONObject personObject = (org.json.JSONObject) personArray.get(i);
				if(country_nm.equals((String) personObject.get("countryName"))) {
				}else {
					country_nm = (String) personObject.get("countryName");					
					if(personObject.has("banPartial")) {
						System.out.printf("%s\t%s\t%s\t%s\t%s\n",personObject.get("countryEnName"),personObject.get("countryName"),personObject.get("banPartial"),personObject.get("banNote"),personObject.get("wrtDt"));
					}
					else {
						System.out.printf("%s\t%s\t%s\t%s\t%s\n",personObject.get("countryEnName"),personObject.get("countryName"),personObject.get("ban"),personObject.get("banNote"),personObject.get("wrtDt"));
					}
				}
				
			}
	
		}catch (Exception e) {
			count++;
			log.info("Travel_Prohibited 에러 횟수 :"+count+" 에러 종류 :"+e);  
		}finally {
			log.info("Travel_Prohibited 에러 횟수 :"+count);
			if(count == 6) {
				count = 0;
			}
			System.out.println("===============end=============");
		}
		return null;		
	}


}
