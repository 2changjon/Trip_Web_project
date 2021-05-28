package poly.service.impl;

import java.util.ArrayList;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;

import poly.dto.TicketDTO;
import poly.persistance.selenium.ISeleniumMapper;
import poly.service.ISeleniumService;

@Service("SeleniumService")
public class SeleniumService  implements ISeleniumService  {
	
	private Logger log = Logger.getLogger(this.getClass());
	
	@Resource(name = "SeleniumMapper")
	private ISeleniumMapper seleniumMapper;
	
	@Override
	public ArrayList<Map<String, String>> getTicket(TicketDTO pDTO) {
		//출발지 도착지 출발일은 default
		String url = "https://www.kayak.co.kr/flights/"+pDTO.getDeparture_Place()+"-"+pDTO.getArrival_Place()+"/"+pDTO.getDeparture_Date();
		String arrival_Date = "/"+pDTO.getArrival_Date(); //반환일
		int adult = pDTO.getAdult();
		int teenager = pDTO.getTeenager();
		int child = pDTO.getChild();
		int baby = pDTO.getBaby();
		String class_Type = pDTO.getClass_Type();
		
		int children = teenager+child+baby;
		
		//왕복일 경우
		if(pDTO.getFlight_Type().equals("RT")) {
			arrival_Date = "";
		}
		
		url = url + arrival_Date;
		
		ArrayList<Map<String, String>> tiket_List = seleniumMapper.getTicket(url);
		
		if(tiket_List == null) {
			tiket_List = new ArrayList<Map<String,String>>();
		}
		return tiket_List;
	}
	
	//성인수에 맞춘 url 삽입
	private String Adult(int adult, int children) {
		
		return null;
	}
	
	private String Teenager(int teenager, int children) {
		return null;
	}
	
	private String Child(int child, int children) {
		return null;
	}
	
	private String Baby(int baby, int children) {
		return null;
	}
	
	
}
