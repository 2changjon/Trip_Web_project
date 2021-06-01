package poly.persistance.selenium;

import java.util.ArrayList;
import java.util.Map;

public interface ISeleniumMapper {

	ArrayList<Map<String, String>> getTicket(String url, String Flight_Type);

}
