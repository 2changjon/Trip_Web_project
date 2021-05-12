package poly.service.impl;

import java.io.File;
import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.regex.Pattern;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;
import org.springframework.util.ResourceUtils;

import com.univocity.parsers.tsv.TsvParser;
import com.univocity.parsers.tsv.TsvParserSettings;

import poly.service.IMainService;

@Service("MainService")
public class MainService implements IMainService {
	
	private Logger log =Logger.getLogger(this.getClass());

	@Override
	public ArrayList<String> getserch_list(String keyWord) throws Exception {
		log.info(this.getClass()+"getserch_list start");
		
		String eng_check = "^[a-zA-Z]*$"; //영어를 찾아내는 정규식
		boolean result = Pattern.matches(eng_check, keyWord); // 입력값에 영어가 있으면 영문 리스트에서 찾기
		
		File employeesFile = ResourceUtils.getFile("classpath:/poly/Data/world_country_data.tsv");
		
		TsvParserSettings settings = new TsvParserSettings();
		settings.getFormat().setLineSeparator("\n"); // tsv 읽을떄 나눌 기준

		// TSV parser 생성
		TsvParser parser = new TsvParser(settings);
		
		// tsv 파일 내용 전체 가져오기
		List<String[]> country_data = parser.parseAll(new FileInputStream(employeesFile));
		//첫번째 줄은 목록이기 때문에 삭제를 해준다
		country_data.remove(0);
		
		Iterator<String[]> list = country_data.iterator(); 
		
		ArrayList<String> serch_list = new ArrayList<String>();
		
		while(list.hasNext()) {
			String[] serch = list.next();
			
			if(result) {
				//영어로 검색시
				if(serch[1].toUpperCase().contains(keyWord.toUpperCase())) {
					log.info(serch.length+"개 검색됨");
					serch_list.add(serch[2]+" "+serch[1]);	
				}		
			} else {
				//한글로 검색시
				if(serch[2].contains(keyWord)) {
					log.info(serch.length+"개 검색됨");
					serch_list.add(serch[2]+" "+serch[1]);	
				}
			}
		}
		
		log.info(this.getClass()+"getserch_list end");
		
		return serch_list;
	}


}
