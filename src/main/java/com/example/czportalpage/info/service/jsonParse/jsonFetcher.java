package com.example.czportalpage.info.service.jsonParse;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class jsonFetcher {
    public static String fetchJson(String urlString) throws Exception {
        // API URL을 user로 대체
        URL url = new URL(urlString);

        // URL 연결
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("GET");
        connection.setConnectTimeout(5000);  // 연결 타임아웃 (5초)
        connection.setReadTimeout(5000);     // 읽기 타임아웃 (5초)

        // 응답 코드 확인
        int responseCode = connection.getResponseCode();
        if (responseCode != 200) {
            throw new Exception("Failed to get data from the server. HTTP response code: " + responseCode);
        }

        // 응답 읽기
        BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
        StringBuilder response = new StringBuilder();
        String inputLine;
        while ((inputLine = in.readLine()) != null) {
            response.append(inputLine);
        }
        in.close();

        // 받아온 데이터를 String으로 반환
        return response.toString();
    }
}
