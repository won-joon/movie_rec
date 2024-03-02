package com.movierec.demo.service;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.type.TypeFactory;
import com.movierec.demo.dto.MovieDto;
import com.movierec.demo.dto.TrendDto;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class NaverApiService {

    private final JSONParser parser = new JSONParser();
    private final ObjectMapper mapper = new ObjectMapper().configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES,false);
    @Value("${search.naver.client.id}")
    private String naverClientId;
    @Value("${search.naver.client.secret}")
    private String naverClientSecret;

    public TrendDto getTrendData(String apiUrl, String movieNm) {

        Map<String, String> requestHeaders = new HashMap<>();
        requestHeaders.put("X-Naver-Client-Id", naverClientId);
        requestHeaders.put("X-Naver-Client-Secret", naverClientSecret);
        requestHeaders.put("Content-Type", "application/json");

        LocalDate now = LocalDate.now(); //현재 날짜
        LocalDate lastWeek = now.minusWeeks(1); //현재 날짜 기준 일주일 전

        String requestBody = "{\"startDate\":\"" + lastWeek + "\", " +
                "\"endDate\":\"" + now + "\"," +
                "\"timeUnit\":\"date\"," +
                "\"keywordGroups\":[{\"groupName\":\"영화\"," + "\"keywords\":[\"" + movieNm + "\"]}]}";

        String responseBody = post(apiUrl, requestHeaders, requestBody);
        JSONArray trendResults = getJsonArray(responseBody);

        TrendDto trendDto = new TrendDto();
        List<String> periodList = new ArrayList<>();
        List<Integer> ratioList = new ArrayList<>();

        for (Object item : trendResults) {
            JSONObject object = (JSONObject) item;
            periodList.add(object.get("period").toString());
            ratioList.add((int) Double.parseDouble(object.get("ratio").toString()));
        }

        trendDto.setPeriod(periodList);
        trendDto.setRatio(ratioList);
        return trendDto;
    }

    private JSONArray getJsonArray(String responseBody) {
        try {
            JSONObject jsonObject = (JSONObject) parser.parse(responseBody);
            JSONArray trendObjectList = (JSONArray) jsonObject.get("results");
            JSONObject trendObject = (JSONObject) trendObjectList.get(0);
            return (JSONArray) trendObject.get("data");
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
    }

    private static String post(String apiUrl, Map<String, String> requestHeaders, String requestBody) {
        HttpURLConnection con = connect(apiUrl);

        try {
            con.setRequestMethod("POST");
            for(Map.Entry<String, String> header :requestHeaders.entrySet()) {
                con.setRequestProperty(header.getKey(), header.getValue());
            }

            con.setDoOutput(true);
            try (DataOutputStream wr = new DataOutputStream(con.getOutputStream())) {
                wr.write(requestBody.getBytes());
                wr.flush();
            }

            int responseCode = con.getResponseCode();
            if (responseCode == HttpURLConnection.HTTP_OK) { // 정상 응답
                return readBody(con.getInputStream());
            } else {  // 에러 응답
                return readBody(con.getErrorStream());
            }
        } catch (IOException e) {
            throw new RuntimeException("API 요청과 응답 실패", e);
        } finally {
            con.disconnect(); // Connection을 재활용할 필요가 없는 프로세스일 경우
        }
    }

    private static HttpURLConnection connect(String apiUrl) {
        try {
            URL url = new URL(apiUrl);
            return (HttpURLConnection) url.openConnection();
        } catch (MalformedURLException e) {
            throw new RuntimeException("API URL이 잘못되었습니다. : " + apiUrl, e);
        } catch (IOException e) {
            throw new RuntimeException("연결이 실패했습니다. : " + apiUrl, e);
        }
    }

    private static String readBody(InputStream body) {
        InputStreamReader streamReader = new InputStreamReader(body, StandardCharsets.UTF_8);

        try (BufferedReader lineReader = new BufferedReader(streamReader)) {
            StringBuilder responseBody = new StringBuilder();

            String line;
            while ((line = lineReader.readLine()) != null) {
                responseBody.append(line);
            }

            return responseBody.toString();
        } catch (IOException e) {
            throw new RuntimeException("API 응답을 읽는데 실패했습니다.", e);
        }
    }
}
