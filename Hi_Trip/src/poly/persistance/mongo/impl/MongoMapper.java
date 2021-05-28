package poly.persistance.mongo.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.apache.log4j.Logger;
import org.bson.Document;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Component;

import com.mongodb.MongoClient;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;

import poly.persistance.mongo.IMongoMapper;
import poly.persistance.comm.AbstractMongoDBComon;

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

	@Override
	public JSONObject getcountry_data(String country_nm) {
		log.info(this.getClass().getName() + ".getcountr_data Start!");
		
		JSONObject country_data= new JSONObject();
		
		try (MongoClient client = new MongoClient("localhost", 27017)) {
            
			MongoCollection<Document> collection = mongodb.getCollection("country_datas");
            
            Document projection = new Document();

            projection.append(country_nm, "$"+country_nm);
            projection.append("_id", 0);
            
            FindIterable<Document> rs = collection.find(new Document()).projection(projection);
            
            Iterator<Document> cursor = rs.iterator();
            
            JSONParser jsonParse = new JSONParser();
            
            while (cursor.hasNext()) {
    			Document doc = cursor.next();
    			
    			if (doc == null) {
    				doc = new Document();
    			}
    			//json으로 만듬
    			country_data = (JSONObject) jsonParse.parse(doc.toJson());
    			log.info(country_data);
    			
    			doc = null;
            }
            
            // 사용이 완료된 객체는 메모리에서 강제로 비우기
    		cursor = null;
    		rs = null;
    		collection = null;
    		projection = null;
    		jsonParse = null;
    		
        } catch (Exception e) {
        	e.printStackTrace();
        }
		
		log.info(this.getClass().getName() + ".getcountr_data End!");
		return country_data;
	}


}
