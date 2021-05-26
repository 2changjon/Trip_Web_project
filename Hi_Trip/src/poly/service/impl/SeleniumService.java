package poly.service.impl;

import org.apache.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.springframework.stereotype.Service;

import poly.service.ISeleniumService;

@Service("SeleniumService")
public class SeleniumService  implements ISeleniumService  {
	
	private Logger log =Logger.getLogger(this.getClass());
	
	//WebDriver 설정
	private WebDriver driver;
	private WebElement element;
	
	//Properties 설정
	// 1. 드라이버 설치 경로
	public static String WEB_DRIVER_ID = "webdriver.chrome.driver";
	public static String WEB_DRIVER_PATH = "./src/poly/data/chromedriver.exe";

	
	
	//출발지가 미리 입력된 값이 있는지 유무 
	private boolean check1() {
		try {
			driver.findElement(By.xpath("//*[@id=\"searchBoxCon\"]/div/div/form/div/div[2]/ul/li[1]/div[2]/div[1]/div[2]/div[1]/div/span/i"));
			return true;
		}catch (Exception e) {
			return false;
		}
	}
	//팝업창이 있는지 유무
	private boolean check2() {
		try {
			driver.findElement(By.xpath("//div[@class=\"online-login-popup show \"]"));
			return true;
		}catch (Exception e) {
			return false;
		}
	}
}
