package poly.persistance.selenium;

import java.util.ArrayList;
import java.util.Map;

public interface ISeleniumMapper {

	ArrayList<Map<String, String>> getTicket1(String url);
	
	ArrayList<Map<String, String>> getTicket2(String url, String Flight_Type);


}
