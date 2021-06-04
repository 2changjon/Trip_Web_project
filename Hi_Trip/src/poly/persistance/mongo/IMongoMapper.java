package poly.persistance.mongo;

import java.util.ArrayList;
import java.util.Map;

import org.json.simple.JSONObject;

public interface IMongoMapper {
	//국가 정보 입력
	public boolean insertMongo(Map<String, Map<String, ArrayList<Map<String, String>>>> api_Data, String colNm, String collectTime) throws Exception;
	//국가 정보 조회
	public JSONObject getcountry_data(String country_nm);
	//공항 리스트 조회
	public ArrayList<Map<String, String>> getair_port(String keyWord);

}


