package poly.service;

import java.util.ArrayList;
import java.util.Map;

import org.json.simple.JSONObject;

public interface IMongoService {

	//db에 파싱한 데이터 저장
	public boolean insertMongo(Map<String, Map<String, ArrayList<Map<String, String>>>> api_Data);
	//db에 있는 데이터 가져오기
	public JSONObject getcountry_data(String country_nm) throws Exception;

}


