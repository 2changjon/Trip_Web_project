package poly.service;

import java.util.ArrayList;
import java.util.Map;

public interface IMongoService {

	//db에 파싱한 데이터 저장
	public boolean insertMongo(Map<String, Map<String, ArrayList<Map<String, String>>>> api_Data);

}


