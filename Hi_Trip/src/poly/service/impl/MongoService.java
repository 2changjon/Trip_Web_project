package poly.service.impl;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.json.simple.JSONObject;
import org.springframework.stereotype.Service;

import poly.persistance.mongo.IMongoMapper;
import poly.service.IMongoService;
import poly.util.DateUtil;

import java.io.File;
import java.io.FileInputStream;
import java.util.regex.Pattern;
import org.springframework.util.ResourceUtils;
import com.univocity.parsers.tsv.TsvParser;
import com.univocity.parsers.tsv.TsvParserSettings;

@Service("MongoService")
public class MongoService implements IMongoService {

	@Resource(name = "MongoMapper")
	private IMongoMapper mongoMapper; // MongoDB에 저장할 Mapper

	// 로그 파일 생성 및 로그 출력을 위한 log4j 프레임워크의 자바 객체
	private Logger log = Logger.getLogger(this.getClass());

	@Override
	public boolean insertMongo(Map<String, Map<String, ArrayList<Map<String, String>>>> api_Data) {
		log.info(this.getClass().getName() + ".insertMongo start!");
		// 생성할 컬렉션명
		String colNm = "country_datas";
		String collectTime = DateUtil.getDateTime("yyyyMMddhhmmss");
		// MongoDB에 데이터저장하기
		boolean success;
		
		try {
			success = mongoMapper.insertMongo(api_Data, colNm, collectTime);
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
		
		log.info(this.getClass().getName() + ".insertMongo End!");
		return success;
	}
	
	@Override
	public JSONObject getcountry_data(String country_nm) throws Exception {
		log.info(this.getClass()+".getcountr_data start");
		
		String eng = "^[a-zA-Z]*$"; //영어를 찾아내는 정규식
		boolean check = Pattern.matches(eng, country_nm); // 입력값에 영어가 있으면 영문 리스트에서 찾기
		
		File employeesFile = ResourceUtils.getFile("classpath:/poly/data/world_country_data.tsv");
		
		TsvParserSettings settings = new TsvParserSettings();
		settings.getFormat().setLineSeparator("\n"); // tsv 읽을때 나눌 기준

		// TSV parser 생성
		TsvParser parser = new TsvParser(settings);
		
		// tsv 파일 내용 전체 가져오기
		List<String[]> file_data = parser.parseAll(new FileInputStream(employeesFile));
		//첫번째 줄은 목록이기 때문에 삭제를 해준다
		file_data.remove(0);

		Iterator<String[]> list = file_data.iterator(); 
		
		JSONObject country_datas = new JSONObject();
		JSONObject country_data = new JSONObject();
		while(list.hasNext()) {
			String[] serch = list.next();
			//serch[0] = number
			if(check) {
				//영어로 검색시 country_nm가 serch[1]에  contains(포함) 되어있으면
				if(serch[1].toUpperCase().equals(country_nm.toUpperCase())) {
					country_datas = mongoMapper.getcountry_data(serch[1].replaceAll(" ", ""));
					country_data = (JSONObject) country_datas.get(serch[1].replaceAll(" ", ""));
				}		
			} else {
				//한글로 검색시
				if(serch[2].equals(country_nm)) {
					country_datas = mongoMapper.getcountry_data(serch[1].replaceAll(" ", ""));
					country_data = (JSONObject) country_datas.get(serch[1].replaceAll(" ", ""));
				}
			}
		}
		
		// 사용이 완료된 객체는 메모리에서 강제로 비우기
		employeesFile = null;
		settings = null;
		parser = null;
		file_data = null;
		list = null;
		 
		log.info(this.getClass()+".getcountr_data end");
		
		return country_data;
	}
	
}
