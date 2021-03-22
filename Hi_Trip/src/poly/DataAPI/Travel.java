
package poly.DataAPI;

import org.apache.log4j.Logger;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;

public class Travel { 
	// 로그 파일 생성 및 로그 출력을 위한 log4j 프레임워크의 자바 객체 private
	Logger log = Logger.getLogger(this.getClass());

	public static void main(String[] args) throws UnirestException {
		
		/* 
		 * Gson은 구글에서 만든 라이브러리로 자바객체를 json으로 json을 자바 객체로 만들 수 있음
		 * HMashape의 경량 HTTP 클라이언트 라이브러리 
		 * Unirest는 post, get, put, delete 등 기본적인 4가지 요청 외에 patch, options까지 지원
		 * asJsonAsync, asBinaryAsync등의 메소드를 이용해서 비동기식 요청 가능
		 */
		HttpResponse<String> response = Unirest.get(
				"https://skyscanner-skyscanner-flight-search-v1.p.rapidapi.com/apiservices/autosuggest/v1.0/KR/KRW/ko-KR/?query="
						+ "부산")
				.header("x-rapidapi-key", "d424301350mshbea8b761854fc09p106d9cjsnd3dd91f8a1bb")
				.header("x-rapidapi-host", "skyscanner-skyscanner-flight-search-v1.p.rapidapi.com").asString();
		/*
		 * Unirest.post(url).headers(header).queryString(params).asString(); // 문자열로 파라미터를 전달 후 응답을 String형으로 받음
		 * Unirest.post(url).headers(header).queryString(params).asJson(); // 문자열로 파라미터를 전달 후 응답을 Json형태로 받음
		 * Unirest.post(url).headers(header).body(params); // Json형태로 파라미터를 보낼 때 사용
		 * Unirest.post(url).headers(header).field("file", new File("/file/file1234.jpg")); // front-end 기준 formData / Back-End기준 multipartRequest를 이용하는 것 처럼(파일전송가능)
		 * 
		 * http://kong.github.io/unirest-java/#requests
		 */
		
		/*
		 * 응답결과를 볼수 있는 방법
		 * .getBody() 응답결과의 body의 내용을 볼 수 있음
		 * .getHeaders() 응답결과의 Header의 내용을 볼 수 있음
		 * .getStatus() 응답결과 상태를 볼 수 있음.
		 */
		
		System.out.println("response:"+response.getBody().toString());
		try {
			
		JSONParser jsonParse = new JSONParser();
		
		JSONObject jsonObj = (JSONObject) jsonParse.parse(response.getBody().toString());
		
		JSONArray personArray = (JSONArray) jsonObj.get("Places");
		for(int i=0; i < personArray.size(); i++) { 
			System.out.println("======== person : " + i + " ========");
			JSONObject personObject = (JSONObject) personArray.get(i);
			System.out.println(personObject.get("PlaceId"));
			System.out.println(personObject.get("PlaceName"));
			System.out.println(personObject.get("CountryId"));
			System.out.println(personObject.get("RegionId"));
			System.out.println(personObject.get("CityId"));
			System.out.println(personObject.get("CountryName"));

			}
		
			} catch (ParseException e) {
				e.printStackTrace(); 
				}
}}
