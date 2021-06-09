package poly.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.json.simple.JSONObject;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import poly.dto.TicketDTO;
import poly.service.IMainService;
import poly.service.IMongoService;
import poly.service.ISeleniumService;
import poly.util.CmmUtil;

@Controller
public class MainController {
	
	@Resource(name = "MainService")
	private IMainService mainService;
	
	@Resource(name = "MongoService")
	private IMongoService mongoService;
	
	@Resource(name = "SeleniumService")
	private ISeleniumService SeleniumService;
	
	private Logger log = Logger.getLogger(this.getClass());
	
	@RequestMapping(value="index")
	public String Index() {
		log.info(this.getClass());
		
		return "/index";
	}
	
	//연관 검색
	@ResponseBody
	@RequestMapping(value = "/serch_List", method = RequestMethod.GET)
	public Object serch_list(HttpServletRequest request) throws Exception {
//		log.info(this.getClass()+".serch_list start");
		
		String keyWord = CmmUtil.nvl(request.getParameter("keyWord"));
		
		ArrayList<String> serch_list = mainService.getserch_list(keyWord);
		
		if(serch_list == null) {
			serch_list = new ArrayList<String>();
		}
//		log.info(this.getClass()+".serch_list end");
		
		return serch_list;
	}
	
	//국가 검색됬을시 관련 데이터 가져오기
	@ResponseBody
	@RequestMapping(value = "/getcountry_Data", method = RequestMethod.GET)
	public Object getcountry_Data(HttpServletRequest request) throws Exception {
		log.info(this.getClass()+".getcountry_Data start");
		
		String country_nm = CmmUtil.nvl(request.getParameter("country_nm"));
		
		JSONObject country_data = mongoService.getcountry_data(country_nm);
		
		log.info(this.getClass()+".getcountry_Data end");
		return country_data;
	}
	//공항찾기
	@ResponseBody
	@RequestMapping(value = "/getair_port", method = RequestMethod.GET)
	public ArrayList<Map<String, String>> getair_port(HttpServletRequest request) throws Exception {
		log.info(this.getClass()+".getair_port start");
		
		String keyWord = CmmUtil.nvl(request.getParameter("keyWord"));
		
		ArrayList<Map<String, String>> getair_port = mongoService.getair_port(keyWord);
		
		if(getair_port == null) {
			getair_port = new ArrayList<Map<String,String>>();
		}
		
		log.info(getair_port);
		log.info(this.getClass()+".getair_port end");
		return getair_port;
	}
	//티켓 검색
	@ResponseBody
	@RequestMapping(value = "/getTicket", method = RequestMethod.GET)
	public ArrayList<Map<String, String>> getTicket(HttpServletRequest request){
		log.info(this.getClass()+".getTicket Start");
		
		String departure_Place = CmmUtil.nvl(request.getParameter("departure_Place"));	//출발지
		String arrival_Place = CmmUtil.nvl(request.getParameter("arrival_Place"));	//도착지
		String departure_Date = CmmUtil.nvl(request.getParameter("departure_Date"));	//출발일
		String arrival_Date = CmmUtil.nvl(request.getParameter("arrival_Date"));	//반환일

		String flight_Type = CmmUtil.nvl(request.getParameter("flight_Type"));	//여행구분
		int adult = Integer.parseInt(CmmUtil.nvl(request.getParameter("adult")));	//성인
		int teenager = Integer.parseInt(CmmUtil.nvl(request.getParameter("teenager")));	//청소년
		int child = Integer.parseInt(CmmUtil.nvl(request.getParameter("child")));	//어린이
		int baby = Integer.parseInt(CmmUtil.nvl(request.getParameter("baby")));	//유아
		String class_Type = CmmUtil.nvl(request.getParameter("class_Type"));	//좌석
		
		TicketDTO pDTO = new TicketDTO(); //dto에 넣고 사용
		pDTO.setDeparture_Place(departure_Place);
		pDTO.setArrival_Place(arrival_Place);
		pDTO.setDeparture_Date(departure_Date);
		pDTO.setArrival_Date(arrival_Date);
		     
		pDTO.setFlight_Type(flight_Type);
		pDTO.setAdult(adult);
		pDTO.setTeenager(teenager);
		pDTO.setChild(child);
		pDTO.setBaby(baby);
		pDTO.setClass_Type(class_Type);
		
		ArrayList<Map<String, String>> tiket_List = SeleniumService.getTicket(pDTO);
		
		log.info(tiket_List);
		
		if(tiket_List == null) {
			tiket_List = new ArrayList<Map<String,String>>();
		}
		
		log.info(this.getClass()+".getTicket end");
		return tiket_List;
	}
	//티켓 검색 저장
	@ResponseBody
	@RequestMapping(value = "/insert_ticket_serch", method = RequestMethod.GET)
	public boolean insert_ticket_serch(HttpServletRequest request){
		log.info(this.getClass()+".insert_ticket_serch start");
		String air_cn = CmmUtil.nvl(request.getParameter("air_cn"));	//공항 국가
		
		boolean success = mainService.insert_ticket_serch(air_cn);
		
//		try {
//			
//			if(success) {
//				File ticket_Serch_Data = ResourceUtils.getFile("classpath:/poly/data/ticket_Serch_Data.txt");
//				//파일 읽기 객체 생성
//		        FileReader filereader = new FileReader(ticket_Serch_Data);
//		        //입력 버퍼 생성
//		        BufferedReader bufReader = new BufferedReader(filereader); //첫번째 줄은 언제나 날짜
//		        String line = "";
//		        log.info("------------------------------------------------");
//		        //국가 검색 파일 한줄씩 읽기
//		        while((line = bufReader.readLine()) != null){
//		        	log.info(line);
//		        }
//		        bufReader.close();
//		        log.info("------------------------------------------------");
//			}
//		} catch (Exception e) {
//			// TODO: handle exception
//		}
		log.info(this.getClass()+".insert_ticket_serch end");
		return success;
	}
	
	//공항 검색 저장
	@ResponseBody
	@RequestMapping(value = "/insert_country_serch", method = RequestMethod.GET)
	public boolean insert_country_serch(HttpServletRequest request){
		log.info(this.getClass()+".insert_country_serch start");
		String country_nm = CmmUtil.nvl(request.getParameter("country_nm"));	//공항 국가
		
		boolean success = mainService.insert_country_serch(country_nm);
		
//		try {
//			
//			if(success) {
//				File country_Serch_Data = ResourceUtils.getFile("classpath:/poly/data/country_Serch_Data.txt");
//				//파일 읽기 객체 생성
//		        FileReader filereader = new FileReader(country_Serch_Data);
//		        //입력 버퍼 생성
//		        BufferedReader bufReader = new BufferedReader(filereader); //첫번째 줄은 언제나 날짜
//		        String line = "";
//		        log.info("------------------------------------------------");
//		        //국가 검색 파일 한줄씩 읽기
//		        while((line = bufReader.readLine()) != null){
//		        	log.info(line);
//		        }
//		        bufReader.close();
//		        log.info("------------------------------------------------");
//			}
//		} catch (Exception e) {
//			// TODO: handle exception
//		}
		log.info(this.getClass()+".insert_country_serch end");
		return success;
	}
	
	//도착지 현재 날씨
	@ResponseBody
	@RequestMapping(value = "/findWeather", method = RequestMethod.GET)
	public Map<String, String> findWeather(HttpServletRequest request){
		String area_id = CmmUtil.nvl(request.getParameter("area_id")); // 국가 수도 id
		
		Map<String, String> weather_Map = mainService.findWeather(area_id);
		
		if(weather_Map == null) {
			weather_Map = new HashMap<String, String>();
		}
		
		return weather_Map;
	}
		
}
