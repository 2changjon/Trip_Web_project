package poly.service;

import java.util.ArrayList;

public interface IMainService {
	//국가 검색
	ArrayList<String> getserch_list(String keyWord) throws Exception;

}
