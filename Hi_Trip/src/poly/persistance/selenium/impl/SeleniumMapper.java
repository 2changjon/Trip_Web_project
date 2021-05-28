package poly.persistance.selenium.impl;

import java.util.ArrayList;
import java.util.Map;

import org.springframework.stereotype.Component;

import poly.persistance.comm.AbstractSeleniumComm;
import poly.persistance.selenium.ISeleniumMapper;

@Component("SeleniumMapper")
public class SeleniumMapper extends AbstractSeleniumComm implements ISeleniumMapper {

	@Override
	public ArrayList<Map<String, String>> getTicket(String url) {
		// TODO Auto-generated method stub
		return null;
	}

}
