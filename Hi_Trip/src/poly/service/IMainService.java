package poly.service;

import java.util.ArrayList;
import java.util.Map;

public interface IMainService {
	//국가 검색
	ArrayList<String> getserch_list(String keyWord) throws Exception;
	//txt에 값 저장
	boolean insert_ticket_serch(String air_cn);
	//txt에 값 저장
	boolean insert_country_serch(String country_nm);
	//현재 날씨
	Map<String, String> findWeather(String area_id);

}
