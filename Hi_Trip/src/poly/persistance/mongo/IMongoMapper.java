package poly.persistance.mongo;

import java.util.ArrayList;
import java.util.Map;

import org.json.simple.JSONObject;

public interface IMongoMapper {

	/**
	 * 매주 월요일 데이터 업데이트
	 * 
	 * @param colNm 조회할 컬렉션 이름
	 * @param collectTime 
	 * @return 노래 리스트
	 * @throws Exception 
	 */
	public boolean insertMongo(Map<String, Map<String, ArrayList<Map<String, String>>>> api_Data, String colNm, String collectTime) throws Exception;

	public JSONObject getcountry_data(String country_nm);

}


