package poly.service.impl;

import java.util.ArrayList;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;

import poly.persistance.mongo.IMongoMapper;
import poly.service.IMongoService;
import poly.util.DateUtil;

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

}
