package poly.data;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

//파싱
import org.json.XML;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.Unirest;

import org.apache.log4j.Logger;

//import poly.persistance.mongo.IMongoMapper;

public class ApiParser {
	// 로그 파일 생성 및 로그 출력을 위한 log4j 프레임워크의 자바 객체 private
	private Logger log = Logger.getLogger(this.getClass());
	
	//api 키 가져오기
	ApiKeys apiKeys = new ApiKeys();
	
	//위험 경보 뉴스	
	public ArrayList<Map<String, String>> Dangerous_News(String country_nm) {
		
		ArrayList<Map<String, String>> dangerous_News_List = new ArrayList<Map<String,String>>();
		Map<String, String> dangerous_News_Map = new HashMap<String, String>();
		
		try {
			/* 
			 * Gson은 구글에서 만든 라이브러리로 자바객체를 json으로 json을 자바 객체로 만들 수 있음
			 * HMashape의 경량 HTTP 클라이언트 라이브러리 
			 * Unirest는 post, get, put, delete 등 기본적인 4가지 요청 외에 patch, options까지 지원
			 * asJsonAsync, asBinaryAsync등의 메소드를 이용해서 비동기식 요청 가능
			 */
			Unirest.setTimeouts(0, 0);
			HttpResponse<String> response = Unirest.get("http://apis.data.go.kr/1262000/CountryHistoryService2/getCountryHistoryList2?serviceKey="+apiKeys.alert_Key+"&returnType=JSON&pageNo=1&numOfRows=5&cond[country_nm::EQ]="+country_nm)
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
				log.info("personArray null");
			}
			
			for(int i=0; i < personArray.size(); i++) { 
				dangerous_News_Map = new HashMap<String, String>();
				JSONObject personObject = (JSONObject) personArray.get(i);
				
				if(country_nm.equals((String) personObject.get("country_nm"))) {
					dangerous_News_Map.put("title", (String) personObject.get("title"));
					dangerous_News_Map.put("html_origin_cn", (String) personObject.get("html_origin_cn"));
					dangerous_News_Map.put("wrt_dt", (String) personObject.get("wrt_dt"));
					dangerous_News_List.add(dangerous_News_Map);
					dangerous_News_Map = null;
				}
				personObject = null;
			}
			// 사용이 완료된 객체는 메모리에서 강제로 비우기
			response = null;
			jsonParse = null;
			jsonObj = null;
			personArray = null;
			dangerous_News_Map = null;
		}catch (Exception e) {
			log.info("Alert_News 에러 국가 ="+country_nm+" 에러 종류 : "+e);		
			return null;
		}finally {
			log.info("Alert_News 국가 : "+country_nm+" 데이터 수 :"+dangerous_News_List.size()+" 종료");
		}
		
		return dangerous_News_List;

	}
	
	//안전 공지 뉴스
	public ArrayList<Map<String, String>> Safety_Notice_News(String country_nm) {
		
		ArrayList<Map<String, String>> safety_Notice_List = new ArrayList<Map<String,String>>();
		Map<String, String> safety_Notice_Map = new HashMap<String, String>();
		
		try {
			Unirest.setTimeouts(0, 0);
			HttpResponse<String> response = Unirest.get("http://apis.data.go.kr/1262000/CountrySafetyService2/getCountrySafetyList2?serviceKey="+apiKeys.safety_Notice_Key+"&returnType=JSON&pageNo=1&numOfRows=5&cond[country_nm::EQ]="+country_nm)
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
				log.info("personArray null");

			}
			
			for(int i=0; i < personArray.size(); i++) { 
				safety_Notice_Map = new HashMap<String, String>();
				JSONObject personObject = (JSONObject) personArray.get(i);
				
				if(country_nm.equals((String) personObject.get("country_nm"))) {
					safety_Notice_Map.put("title", (String) personObject.get("title"));
					safety_Notice_Map.put("txt_origin_cn", (String) personObject.get("txt_origin_cn"));
					safety_Notice_Map.put("wrt_dt", (String) personObject.get("wrt_dt"));
					safety_Notice_List.add(safety_Notice_Map);
					safety_Notice_Map = null;
				}
				personObject = null;
			}
			// 사용이 완료된 객체는 메모리에서 강제로 비우기
			response = null;
			jsonParse = null;
			jsonObj = null;
			personArray = null;
			safety_Notice_Map = null;
			
		}catch (Exception e) {
			log.info("Safety_Notice_News 에러 국가 ="+country_nm+" 에러 종류 : "+e);
			return null;
		}finally {
			log.info("Safety_Notice_News 국가 : "+country_nm+" 데이터 수 :"+safety_Notice_List.size()+" 종료");
		}
		return safety_Notice_List;

	}
	
	//사건사고 유형 뉴스
	public ArrayList<Map<String, String>> Accident_Type_News(String country_nm) {
		
		ArrayList<Map<String, String>> accident_Type_List = new ArrayList<Map<String,String>>();
		Map<String, String> accident_Type_Map = new HashMap<String, String>();
		
		try {
			Unirest.setTimeouts(0, 0);
			HttpResponse<String> response = Unirest.get("http://apis.data.go.kr/1262000/CountryAccidentService2/CountryAccidentService2?serviceKey="+apiKeys.accident_Type_Key+"&returnType=JSON&pageN=1&numOfRows=5&cond[country_nm::EQ]="+country_nm)
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
				log.info("personArray null");

			}
			
			for(int i=0; i < personArray.size(); i++) { 
				accident_Type_Map = new HashMap<String, String>();
				JSONObject personObject = (JSONObject) personArray.get(i);
				
				if(country_nm.equals((String) personObject.get("country_nm"))) {
					accident_Type_Map.put("news", (String) personObject.get("news"));
					accident_Type_Map.put("wrt_dt", (String) personObject.get("wrt_dt"));
					accident_Type_List.add(accident_Type_Map);
					accident_Type_Map = null;
				}	
				personObject = null;
			}
			// 사용이 완료된 객체는 메모리에서 강제로 비우기
			response = null;
			jsonParse = null;
			jsonObj = null;
			personArray = null;
			accident_Type_Map = null;
		}catch (Exception e) {
			log.info("Accident_Type_News 에러 국가 ="+country_nm+" 에러 종류 : "+e);
			return null;
		}finally {
			log.info("Accident_Type_News 국가 : "+country_nm+" 데이터 수 :"+accident_Type_List.size()+" 종료");
		}
		return accident_Type_List;

	}

	//현지 연락처 뉴스
	public ArrayList<Map<String, String>> Local_Contact_News(String country_nm) {
		
		ArrayList<Map<String, String>> contact_News_List = new ArrayList<Map<String,String>>();
		Map<String, String> contact_News_Map = new HashMap<String, String>();
		
		try {
			Unirest.setTimeouts(0, 0);
			HttpResponse<String> response = Unirest.get("http://apis.data.go.kr/1262000/LocalContactService2/getLocalContactList2?serviceKey="+apiKeys.local_Contac_Key+"&numOfRows=5&cond[country_nm::EQ]="+country_nm)
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
				log.info("personArray null");
	
			}
			
			for(int i=0; i < personArray.size(); i++) { 
				JSONObject personObject = (JSONObject) personArray.get(i);
				contact_News_Map = new HashMap<String, String>();
				
				if(country_nm.equals((String) personObject.get("country_nm"))) {
					contact_News_Map.put("contact_remark", (String) personObject.get("contact_remark"));
					contact_News_Map.put("wrt_dt", (String) personObject.get("wrt_dt"));
					contact_News_List.add(contact_News_Map);
					contact_News_Map = null;
				}
				personObject = null;
			}
			// 사용이 완료된 객체는 메모리에서 강제로 비우기
			response = null;
			jsonParse = null;
			jsonObj = null;
			personArray = null;
			contact_News_Map = null;
		}catch (Exception e) {
			log.info("Local_Contact_News 에러 국가 ="+country_nm+" 에러 종류 : "+e);
			return null;
		}finally {
			log.info("Local_Contact_News 국가 : "+country_nm+" 데이터 수 :"+contact_News_List.size()+" 종료");
		}
		return contact_News_List;
	}
	
	//여행위험 경보
	public ArrayList<Map<String, String>> Travel_Alert(String country_nm) {
		
		ArrayList<Map<String, String>> travel_Alert_List = new ArrayList<Map<String,String>>();
		Map<String, String> travel_Alert_Map = new HashMap<String, String>();
		
		try {
			Unirest.setTimeouts(0, 0);
			HttpResponse<String> response = Unirest.get("http://apis.data.go.kr/1262000/TravelWarningService/getTravelWarningList?serviceKey="+apiKeys.travel_Alert_Key+"&numOfRows=10&pageNo=1&countryName="+country_nm)
			  .asString();
			
			//null 체크
			if(response == null) {
				response = new HttpResponse<String>(null, null);
			}

		    org.json.JSONObject joObject = XML.toJSONObject(response.getBody().toString());
		    org.json.JSONObject xmlresponse = (org.json.JSONObject) joObject.get("response");
		    
		    org.json.JSONObject body = (org.json.JSONObject) xmlresponse.get("body");

		    //items내의 데이터 수 totalCount인데 없으면 끝내기
		    if(body.get("totalCount").equals(0)) {
		    	log.info("totalCount 0");
		    	return travel_Alert_List;
		    }
		    
		    org.json.JSONObject items = (org.json.JSONObject) body.get("items");
		    org.json.JSONArray personArray =  (org.json.JSONArray) items.get("item");
		    
		    //null 체크
			if(personArray.isNull(0)) {
				personArray = new org.json.JSONArray();
				log.info("personArray null");

			}
			
			for(int i=0; i < personArray.length(); i++) { 
				travel_Alert_Map = new HashMap<String, String>();
				org.json.JSONObject personObject = (org.json.JSONObject) personArray.get(i);
				
				if(country_nm.equals((String) personObject.get("countryName"))) {
					if(personObject.has("limitaPartial")) {
						travel_Alert_Map.put("limitaPartial", (String) personObject.get("limitaPartial"));
						travel_Alert_Map.put("limitaNote", (String) personObject.get("limitaNote"));
						travel_Alert_Map.put("wrtDt", (String) personObject.get("wrtDt"));
						travel_Alert_List.add(travel_Alert_Map);
						travel_Alert_Map = null;
					}else {
						travel_Alert_Map.put("wrtDt", (String) personObject.get("wrtDt"));
						travel_Alert_List.add(travel_Alert_Map);
						travel_Alert_Map = null;
					}
				}
				personObject = null;
			}
			// 사용이 완료된 객체는 메모리에서 강제로 비우기
			response = null;
			joObject = null;
			xmlresponse = null;
			body = null;
			items = null;
			personArray = null;
			travel_Alert_Map = null;
			
		}catch (Exception e) {
			log.info("Travel_Alert 에러 국가 ="+country_nm+" 에러 종류 : "+e);  
			return null;
		}finally {
//			log.info("Travel_Alert 국가 : "+country_nm+" 데이터 수 :"+travel_Alert_List.size()+" 종료");
		}
		return travel_Alert_List;
	}
	//특수 위험 경보	
	public ArrayList<Map<String, String>> Special_Travel_Alert(String country_nm) {
		ArrayList<Map<String, String>> special_Travel_List = new ArrayList<Map<String,String>>();
		Map<String, String> special_Travel_Map = new HashMap<String, String>();
		try {
			Unirest.setTimeouts(0, 0);
			HttpResponse<String> response = Unirest.get("http://apis.data.go.kr/1262000/TravelSpecialWarningService/getTravelSpecialWarningList?serviceKey="+apiKeys.special_Travel_Alert_Key+"&pageNo=1&numOfRows=4&countryName="+country_nm)
			  .asString();
			
			//null 체크
			if(response == null) {
				response = new HttpResponse<String>(null, null);
			}
	
			org.json.JSONObject joObject = XML.toJSONObject(response.getBody().toString());
		    org.json.JSONObject xmlresponse = (org.json.JSONObject) joObject.get("response");
		    org.json.JSONObject body = (org.json.JSONObject) xmlresponse.get("body");			  
		    org.json.JSONObject items = new org.json.JSONObject();
		    org.json.JSONArray personArray = new org.json.JSONArray();
		    
		    //items내의 데이터 수 totalCount인데 없으면 끝내기
		    if(body.get("totalCount").equals(0)) {
		    	log.info("totalCount 0");
		    	return special_Travel_List;
		    }else if(body.get("totalCount").equals(1)){
		    	log.info("totalCount 1");
		    	items  = (org.json.JSONObject) body.get("items");
		    	personArray.put(items.get("item"));
		    }else {
		    	items  = (org.json.JSONObject) body.get("items");
		    	personArray = (org.json.JSONArray) items.get("item");
		    }
		    
		    //null 체크
			if(personArray.isNull(0)) {
				personArray = new org.json.JSONArray();
			}
			
			for(int i=0; i < personArray.length(); i++) { 
				
				org.json.JSONObject personObject = (org.json.JSONObject) personArray.get(i);
				
				if(country_nm.equals((String) personObject.get("countryName"))) {		
					//has == "limitaPartial"가 있는 경우 체크
					if(personObject.has("splimit")) {					
						special_Travel_Map.put("splimit", (String) personObject.get("splimit"));
					}else if(personObject.has("splimitPartial")) {
						special_Travel_Map.put("splimitPartial", (String) personObject.get("splimitPartial"));
					}
					special_Travel_Map.put("splimitNote", (String) personObject.get("splimitNote"));
					special_Travel_Map.put("wrtDt", (String) personObject.get("wrtDt"));
					special_Travel_List.add(special_Travel_Map);
					special_Travel_Map = null;
				}
				personObject = null;
			}
			// 사용이 완료된 객체는 메모리에서 강제로 비우기
			response = null;
			joObject = null;
			xmlresponse = null;
			body = null;
			items = null;
			personArray = null;
			special_Travel_Map = null;

		}catch (Exception e) {
			log.info("Special_Travel_Alert 에러 국가 ="+country_nm+" 에러 종류 : "+e); 
			System.out.println(e);
			return null;
		}finally {
			log.info("Special_Travel_Alert 국가 : "+country_nm+" 데이터 수 :"+special_Travel_List.size()+" 종료");
		}
		return special_Travel_List;
	}
		
	//여행금지 경보	
	public ArrayList<Map<String, String>> Travel_Prohibited(String country_nm) {
		
		ArrayList<Map<String, String>> travel_Prohibited_List = new ArrayList<Map<String,String>>();
		Map<String, String> travel_Prohibited_Map = new HashMap<String, String>();
		
		try {
			Unirest.setTimeouts(0, 0);
			HttpResponse<String> response = Unirest.get("http://apis.data.go.kr/1262000/TravelBanService/getTravelBanList?serviceKey="+apiKeys.travel_Prohibited_Key+"&numOfRows=10&pageNo=1&countryName="+country_nm)
			  .asString();
			
			//null 체크
			if(response == null) {
				response = new HttpResponse<String>(null, null);
			}
	
		    org.json.JSONObject joObject = XML.toJSONObject(response.getBody().toString());
		    org.json.JSONObject xmlresponse = (org.json.JSONObject) joObject.get("response");
		    
		    org.json.JSONObject body = (org.json.JSONObject) xmlresponse.get("body");

		    //items내의 데이터 수 totalCount인데 없으면 끝내기
		    if(body.get("totalCount").equals(0)) {
		    	log.info("totalCount 0");
		    	return travel_Prohibited_List;
		    }
		    
		    org.json.JSONObject items = (org.json.JSONObject) body.get("items");
		    org.json.JSONArray personArray =  (org.json.JSONArray) items.get("item");
		    
		  //null 체크
			if(personArray.isNull(0)) {
				personArray = new org.json.JSONArray();
				log.info("personArray null");
	
			}
			
			for(int i=0; i < personArray.length(); i++) { 
				travel_Prohibited_Map = new HashMap<String, String>();
				org.json.JSONObject personObject = (org.json.JSONObject) personArray.get(i);
				
				if(country_nm.equals((String) personObject.get("countryName"))) {
					if(personObject.has("banPartial")) {
						travel_Prohibited_Map.put("banPartial", (String) personObject.get("banPartial"));
						travel_Prohibited_Map.put("banNote", (String) personObject.get("banNote"));
						travel_Prohibited_Map.put("imgUrl2", (String) personObject.get("imgUrl2"));
						travel_Prohibited_Map.put("wrtDt", (String) personObject.get("wrtDt"));
						travel_Prohibited_List.add(travel_Prohibited_Map);
						travel_Prohibited_Map = null;
					}else {
						travel_Prohibited_Map.put("ban", (String) personObject.get("ban"));
						travel_Prohibited_Map.put("banNote", (String) personObject.get("banNote"));
						travel_Prohibited_Map.put("imgUrl2", (String) personObject.get("imgUrl2"));
						travel_Prohibited_Map.put("wrtDt", (String) personObject.get("wrtDt"));
						travel_Prohibited_List.add(travel_Prohibited_Map);
						travel_Prohibited_Map = null;
					}
				}
				personObject = null;
			}
			
			// 사용이 완료된 객체는 메모리에서 강제로 비우기
			response = null;
			joObject = null;
			xmlresponse = null;
			body = null;
			items = null;
			personArray = null;
			travel_Prohibited_Map = null;
			
		}catch (Exception e) {
			log.info("Travel_Prohibited 에러 국가 ="+country_nm+" 에러 종류 : "+e);  
			return null;
		}finally {
			log.info("Travel_Prohibited 국가 : "+country_nm+" 데이터 수 :"+travel_Prohibited_List.size()+" 종료");
		}
		return travel_Prohibited_List;		
	}


}
