package poly.service;

import java.util.ArrayList;

public interface IMainService {

	ArrayList<String> getserch_list(String keyWord) throws Exception;

	ArrayList<String> getplace_List(String country_nm);

}
