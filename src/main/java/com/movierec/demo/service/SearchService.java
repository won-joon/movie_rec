package com.movierec.demo.service;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.type.TypeFactory;
import com.movierec.demo.dto.MovieDto;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Service
public class SearchService {

    private final JSONParser parser = new JSONParser();
    private final ObjectMapper mapper = new ObjectMapper().configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES,false);

    public MovieDto getMovieData(String apiUrl) {

        String responseString = get(apiUrl);

        JSONArray movieListObject = getJsonArray(responseString);
        JSONObject movieObject = (JSONObject) movieListObject.get(0);

        List<String> movieList = new ArrayList<>();
        JSONArray directorList = (JSONArray) movieObject.get("directors");
        for (Object director : directorList) {
            JSONObject directorObj = (JSONObject) director;
            movieList.add(directorObj.get("peopleNm").toString());
        }

        MovieDto movieDto = mapper.convertValue(movieObject, MovieDto.class);
        movieDto.setDirectorList(movieList);
        return movieDto;
    }

    public List<MovieDto> getMovieListData(String apiUrl) {

        String responseString = get(apiUrl);
        JSONArray movieListObject = getJsonArray(responseString);

        try {
            List<MovieDto> movieDtoList = mapper.convertValue(movieListObject, TypeFactory.defaultInstance().constructCollectionType(List.class, MovieDto.class));
            return movieDtoList;
        } catch (Exception e) {
            throw new RuntimeException("검색 영화가 없음", e);
        }
    }

    private JSONArray getJsonArray(String responseString) {
        try {
            JSONObject jsonObject = (JSONObject) parser.parse(responseString);
            JSONObject movieObject = (JSONObject)  jsonObject.get("movieListResult");
            return (JSONArray) movieObject.get("movieList");
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
    }

    public String get(String apiUrl) {
        HttpURLConnection con = connect(apiUrl);

        try {
            con.setRequestMethod("GET");
//            for(Map.Entry<String, String> header :requestHeaders.entrySet()) {
//                con.setRequestProperty(header.getKey(), header.getValue());
//            }

            int responseCode = con.getResponseCode();
            if (responseCode == HttpURLConnection.HTTP_OK) { // 정상 호출
                return readBody(con.getInputStream());
            } else { // 오류 발생
                return readBody(con.getErrorStream());
            }
        } catch (IOException e) {
            throw new RuntimeException("API 요청과 응답 실패", e);
        } finally {
            con.disconnect();
        }
    }

    private static HttpURLConnection connect(String apiUrl){
        try {
            URL url = new URL(apiUrl);
            return (HttpURLConnection)url.openConnection();
        } catch (MalformedURLException e) {
            throw new RuntimeException("API URL이 잘못되었습니다. : " + apiUrl, e);
        } catch (IOException e) {
            throw new RuntimeException("연결이 실패했습니다. : " + apiUrl, e);
        }
    }

    private static String readBody(InputStream body){
        InputStreamReader streamReader = new InputStreamReader(body);


        try (BufferedReader lineReader = new BufferedReader(streamReader)) {
            StringBuilder responseBody = new StringBuilder();


            String line;
            while ((line = lineReader.readLine()) != null) {
                responseBody.append(line);
            }

            return responseBody.toString();
        } catch (IOException e) {
            throw new RuntimeException("API 응답을 읽는 데 실패했습니다.", e);
        }
    }


}
