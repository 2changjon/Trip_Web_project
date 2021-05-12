package poly.controller;

import java.util.ArrayList;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import poly.service.IMainService;
import poly.util.CmmUtil;


@Controller
public class MainController {
	
	@Resource(name = "MainService")
	private IMainService mainService;
	
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
		log.info(this.getClass()+"serch_list start");
		
		String keyWord = CmmUtil.nvl(request.getParameter("keyWord"));
		
		ArrayList<String> serch_list = mainService.getserch_list(keyWord);
		
		log.info(this.getClass()+"serch_list end");
		
		return serch_list;
	}
	
}
