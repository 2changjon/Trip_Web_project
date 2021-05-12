package poly.service.impl;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLSession;

import org.apache.log4j.Logger;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.springframework.stereotype.Service;

import poly.dto.AccStatDTO;
import poly.service.IGetAccStatService;
import poly.util.CmmUtil;

@Service("GetAccStatService")
public class GetAccStatService implements IGetAccStatService {

	private Logger log = Logger.getLogger(this.getClass());

	/**
	 * JSON 결과를 받아오기 위한 함수
	 * 
	 * URL을 피라미터로 전달하면, 자동으로 JSON 결과를 String 변수에 저장하고 결과값을 반환
	 * 
	 * @param 호출URL
	 * @return JSON 결과
	 */
	private String getUrlForJSON(String callUrl) { // private함수는 인터페이스 함수가 선언되지않아도 된다.
		// String myURL "http:/localhost:9010/accStat/getAccStatInfo.do";

		log.info(this.getClass().getName() + ".getUrlForJSON start");

		log.info("Requeted URL : " + callUrl);

		StringBuilder sb = new StringBuilder();
		URLConnection urlConn = null;
		InputStreamReader in = null;

		// json 결과값이 저장되는 변수
		String json = "";

		// SSL 적용된 사이트일 경우, 데이터 증명을 위해 사용
		HostnameVerifier allHostsValid = new HostnameVerifier() {

			@Override
			public boolean verify(String hostname, SSLSession session) {
				return true;
			}
		};

		HttpsURLConnection.setDefaultHostnameVerifier(allHostsValid);

		try {

			// 웹사이트 접속을 위한 URL 파싱
			URL url = new URL(callUrl);

			// 접속
			urlConn = url.openConnection();

			// 접속하면, 응답을 60초(60*1000ms)동안 기다림
			if (urlConn != null) {
				urlConn.setReadTimeout(60 * 1000);
			}

			if (urlConn != null && urlConn.getInputStream() != null) {
				in = new InputStreamReader(urlConn.getInputStream(), Charset.forName("UTF-8"));

				BufferedReader bufferedReader = new BufferedReader(in);

				// 주어진 문자 입력 스트림 unoutStream에 대해 기본 크기의 버퍼를 갖는 객체를 생성.
				if (bufferedReader != null) {
					int cp;
					while ((cp = bufferedReader.read()) != -1) {
						sb.append((char) cp);
					}
					bufferedReader.close();
				}
			}
			in.close();
		} catch (Exception e) {
			throw new RuntimeException("Exception URL : " + callUrl, e);
		}

		json = sb.toString(); // json 결과 저장

		log.info("JSON result : " + json);

		log.info(this.getClass().getName() + ".getUrlForJSON END!");

		return json;
	}

	/**
	 * 교통사고 정보 가져오기
	 * 
	 * @param pDTO 교통사고 정보 가져오기 위한 피라미터
	 * @return json 결과를 Map형태로 반환결과
	 * @throws Exception
	 */
	@Override
	public Map<String, Object> getAccStatForJSON(AccStatDTO pDTO) throws Exception {

		// 로그
		log.info(this.getClass().getName() + ".getAccStatForJSON Start");

		// JSON 읽은 값을 Controller에 전달하기 위한 결과 변수
		Map<String, Object> rMap = new HashMap<String, Object>();

		// JSON 결과 받아오기
		String json = getUrlForJSON(CmmUtil.nvl(pDTO.getUrl()));

		// String 변수의 문자열을 json 형태의 데이터 구조로 변경하기 위한 객체를 메모리에 올림
		JSONParser parser = new JSONParser();

		// String 변수의 문자열을 json 형태의 데이터 구조롤 변경하기 위해 자바 최상위 object 변환
		Object obj = parser.parse(json);

		// 변환된 Object 객체를 json 데이터 구조로 변경
		JSONObject jsonObject = (JSONObject) obj;

		// 요청한 피라미터 가져오기
		String reqYYYYMM = CmmUtil.nvl((String) jsonObject.get("reqYYYYMM"));
		log.info("reqYYYYMM : " + reqYYYYMM);

		rMap.put("reqYYYYMM", reqYYYYMM); // 데이터 저장

		// 요청한 피라미터 가져오기
		String reqAcode = CmmUtil.nvl((String) jsonObject.get("reqAcode"));
		log.info("reqAcode : " + reqAcode);

		rMap.put("reqAcode", reqAcode); // 데이터 저장

		// 요청한 파라미터 가져오기
		long recordCnt = (Long) jsonObject.get("recordCnt");
		log.info("recordCnt : " + recordCnt);

		rMap.put("recordCnt", recordCnt); // 데이터 저장

		// json에 저장된 배열형태 데이터
		JSONArray resArr = (JSONArray) jsonObject.get("res");

		// json 배열에 저장된 데이터를 List<AccStatDTO> 구조로 변경하기 위해 메모리에 올림
		List<AccStatDTO> rList = new ArrayList<AccStatDTO>();

		// 걱 레코드마다 DTO로 저장
		AccStatDTO rDTO = null;

		for (int i = 0; i < resArr.size(); i++) {
			JSONObject result = (JSONObject) resArr.get(i);

			rDTO = new AccStatDTO(); // 데이터 저장을 위해 메모리에 올림

			// 로그
			log.info("yyyymm : " + CmmUtil.nvl((String) result.get("yyyymm")));
			log.info("a_code : " + CmmUtil.nvl((String) result.get("a_code")));
			log.info("a_name : " + CmmUtil.nvl((String) result.get("a_name")));
			log.info("stat_a : " + CmmUtil.nvl((String) result.get("stat_a")));
			log.info("stat_b : " + CmmUtil.nvl((String) result.get("stat_b")));

			// json 데이터를 DTO에 저장
			rDTO.setYyyymm(CmmUtil.nvl((String) result.get("yyyymm")));
			rDTO.setA_code(CmmUtil.nvl((String) result.get("a_code")));
			rDTO.setA_name(CmmUtil.nvl((String) result.get("a_name")));
			rDTO.setStat_a(CmmUtil.nvl((String) result.get("stat_a")));
			rDTO.setStat_b(CmmUtil.nvl((String) result.get("stat_b")));

			// 저장된 DTO를 list에 저장
			rList.add(rDTO);
		}

		// controller에 저장할 데이터 저장
		rMap.put("res", rList);

		// 로그 찍기
		log.info(this.getClass().getName() + ".getAccStatForJSON end");

		return rMap;
	}

	/**
	 * 야간 교통사고 건수 가져오기
	 * 
	 * @param pDTO 교통사고 정보 가져오기 위한 피라미터
	 * @return json 결과를 Map형태로 반환결과
	 * @throws Exception
	 */
	@Override
	public Map<String, Object> getAccStatNightForJSON(AccStatDTO pDTO) throws Exception {
		// 로그
		log.info(this.getClass().getName() + ".getAccStatNightForJSON Start");

		// JSON 읽은 값을 Controller에 전달하기 위한 결과 변수
		Map<String, Object> rMap = new HashMap<String, Object>();

		// JSON 결과 받아오기
		String json = getUrlForJSON(CmmUtil.nvl(pDTO.getUrl()));

		// String 변수의 문자열을 json 형태의 데이터 구조로 변경하기 위한 객체를 메모리에 올림
		JSONParser parser = new JSONParser();

		// String 변수의 문자열을 json 형태의 데이터 구조롤 변경하기 위해 자바 최상위 object 변환
		Object obj = parser.parse(json);

		// 변환된 Object 객체를 json 데이터 구조로 변경
		JSONObject jsonObject = (JSONObject) obj;

		// 요청한 피라미터 가져오기
		String reqYYYYMM = CmmUtil.nvl((String) jsonObject.get("reqYYYYMM"));
		log.info("reqYYYYMM : " + reqYYYYMM);

		rMap.put("reqYYYYMM", reqYYYYMM); // 데이터 저장

		// 요청한 피라미터 가져오기
		String reqAcode = CmmUtil.nvl((String) jsonObject.get("reqAcode"));
		log.info("reqAcode : " + reqAcode);

		rMap.put("reqAcode", reqAcode); // 데이터 저장

		// 요청한 파라미터 가져오기
		long recordCnt = (Long) jsonObject.get("recordCnt");
		log.info("recordCnt : " + recordCnt);

		rMap.put("recordCnt", recordCnt); // 데이터 저장

		// json에 저장된 배열형태 데이터
		JSONArray resArr = (JSONArray) jsonObject.get("res");

		// json 배열에 저장된 데이터를 List<AccStatDTO> 구조로 변경하기 위해 메모리에 올림
		List<AccStatDTO> rList = new ArrayList<AccStatDTO>();

		// 걱 레코드마다 DTO로 저장
		AccStatDTO rDTO = null;

		for (int i = 0; i < resArr.size(); i++) {
			JSONObject result = (JSONObject) resArr.get(i);

			rDTO = new AccStatDTO(); // 데이터 저장을 위해 메모리에 올림

			// 로그
			log.info("yyyymm : " + CmmUtil.nvl((String) result.get("yyyymm")));
			log.info("a_code : " + CmmUtil.nvl((String) result.get("a_code")));
			log.info("a_name : " + CmmUtil.nvl((String) result.get("a_name")));
			log.info("stat_a : " + CmmUtil.nvl((String) result.get("stat_a")));

			// json 데이터를 DTO에 저장
			rDTO.setYyyymm(CmmUtil.nvl((String) result.get("yyyymm")));
			rDTO.setA_code(CmmUtil.nvl((String) result.get("a_code")));
			rDTO.setA_name(CmmUtil.nvl((String) result.get("a_name")));
			rDTO.setStat_a(CmmUtil.nvl((String) result.get("stat_a")));

			// 저장된 DTO를 list에 저장
			rList.add(rDTO);
		}

		// controller에 저장할 데이터 저장
		rMap.put("res", rList);

		// 로그 찍기
		log.info(this.getClass().getName() + ".getAccStatNightForJSON end");

		return rMap;
	}

}
