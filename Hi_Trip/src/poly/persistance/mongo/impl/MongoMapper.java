package poly.persistance.mongo.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.apache.log4j.Logger;
import org.bson.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Component;

import com.mongodb.client.MongoCollection;

import poly.persistance.mongo.IMongoMapper;
import poly.persistance.mongo.comm.AbstractMongoDBComon;

@Component("MongoMapper")
public class MongoMapper extends AbstractMongoDBComon implements IMongoMapper {

	@Autowired
	private MongoTemplate mongodb;

	private Logger log = Logger.getLogger(this.getClass());
	
	@Override
	public boolean insertMongo(Map<String, Map<String, ArrayList<Map<String, String>>>> api_Data, String colNm, String collectTime) {
		log.info(this.getClass().getName() + ".insertMongo Start!");
		boolean success = false;
		
		if(api_Data == null){
			api_Data = new HashMap<String, Map<String,ArrayList<Map<String,String>>>>();
		}
		// 데이터를 저장할 컬렉션 생성
		try {
			super.createCollection(colNm, "collectTime");
			
			// 저장할 컬렉션 객체 생성
			MongoCollection<Document> col = mongodb.getCollection(colNm);
			// 2.xx 버전의 MongoDB 저장은 Document 단위로 구성됨
			Document doc = new Document();
			doc.append("collectTime", collectTime);
			
			for(String country : api_Data.keySet()) {			
				doc.append(country, api_Data.get(country));	
			}			
			// 레코드 한개씩 저장하기
			col.insertOne(doc);
			doc = null;
			col = null;
			success = true;
			
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
		
		log.info(this.getClass().getName() + ".insertSong End!");
		return success;
	}


}
