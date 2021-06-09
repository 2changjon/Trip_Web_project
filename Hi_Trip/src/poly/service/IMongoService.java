package poly.service;

import java.util.ArrayList;
import java.util.Map;

import org.json.simple.JSONObject;

public interface IMongoService {

	//db에 파싱한 데이터 저장
	public boolean insertMongo(Map<String, Map<String, ArrayList<Map<String, String>>>> api_Data);
	//db에 있는 데이터 가져오기(국가 정보)
	public JSONObject getcountry_data(String country_nm) throws Exception;
	//db에 있는 데이터 가져오기(공항 리스트)
	public ArrayList<Map<String, String>> getair_port(String keyWord);
	//주에 한번 db에 컬렉션 생성 국가검색과 티켓검색 
	public boolean insert_serch(String colNm, String collectTime, ArrayList<Map<String, String>> country_List);


}


