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
		log.info(this.getClass()+".getTicket start");
		//출발지 도착지 출발일은 default
		String url = "https://www.kayak.co.kr/flights/"+pDTO.getDeparture_Place()+"-"+pDTO.getArrival_Place()+"/"+pDTO.getDeparture_Date();
		String arrival_Date = "/"+pDTO.getArrival_Date(); //반환일
		String class_Type = "/"+pDTO.getClass_Type();//좌석
		String children = Children(pDTO.getTeenager(), pDTO.getChild(), pDTO.getBaby()); // 전체 어린이
		String adult = Adult(pDTO.getAdult(), children); //성인수
		
		
		//편도일 경우
		if(pDTO.getFlight_Type().equals("OW")) {
			arrival_Date = ""; //반환일 삭제
		}
		//일반 좌석일 경우 
		if(class_Type.equals("/Normal")) {
			class_Type = ""; //좌석 삭제
		}
		
		url = url + arrival_Date + class_Type + adult + children +"?sort=bestflight_a";
		System.out.println("-----------------------------------------------");
		System.out.println(url);
		System.out.println("-----------------------------------------------");
		ArrayList<Map<String, String>> tiket_List = seleniumMapper.getTicket(url,pDTO.getFlight_Type());
		
		if(tiket_List == null) {
			tiket_List = new ArrayList<Map<String,String>>();
		}
		
		log.info(this.getClass()+".getTicket end");
		return tiket_List;
	}
	
	//성인수에 맞춘 url 삽입
	private String Adult(int adult, String children) {
		
		if(adult == 1 && children.equals("")) {
			return "";			
		}else {
			return "/"+adult+"adults";
		}
	}
	//전체 미성년 수에 맞춘 url 삽입
	private String Children(int teenager, int child, int baby) {
		
		String children = "";
		//전체 어린이 수
		int childs = teenager + child + baby;
		
		if(childs == 0) {
			return children;
		}else {
			children = "/children";
			for(int i = 1; i<=childs; i ++) {
				//유아 어린이 청소년 순
				if(baby > 0) {               
					children = children+"-1S";
					baby--;                  
				}else if(child > 0) {          
					children = children+"-11";
					child--;                 
				}else {                      
					children = children+"-17";
					teenager--;              
				}                            
			}
			return children;
		}//else
	}
	
	
}
