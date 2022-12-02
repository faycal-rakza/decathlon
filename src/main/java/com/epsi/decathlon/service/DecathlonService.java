package com.epsi.decathlon.service;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import okhttp3.*;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

@Service
public class DecathlonService {

    private final String AUTH_URL = "https://idpdecathlon.oxylane.com/as/token.oauth2";

    private final String BASIC;
    private String ACCESS_TOKEN;
    private final String API_KEY;

    public DecathlonService(@Value("${decathlon.basic}") String basic, @Value("${decathlon.api-key}") String apiKey) {
        this.BASIC = basic;
        this.API_KEY = apiKey;
    }

    public String getSports(String url, String fileUrl) throws IOException {
        auth();
        OkHttpClient client = new OkHttpClient();

        RequestBody formBody = new MultipartBody.Builder()
                .setType(MultipartBody.FORM)
                .addFormDataPart("file", fileUrl)
                .addFormDataPart("language", "fr")
                .build();


        Request request = new Request.Builder()
                .url(url)
                .post(formBody)
                .header("Authorization", "Bearer " + ACCESS_TOKEN)
                .header("X-API-KEY", API_KEY)
                .build();

        Call call = client.newCall(request);
        Response response = call.execute();

        JSONObject jsonObject = new JSONObject(response.body().string());
        String data = jsonObject.get("data").toString();
        String sport = new JSONObject(data).get("sport").toString();

        JSONArray sports = new JSONArray(sport);

        BigDecimal maxProbability = new BigDecimal("0.0");
        String maxSport = "";
        for (Object s : sports) {
            JSONObject json = (JSONObject) s;
            BigDecimal probability = new BigDecimal(json.get("probability").toString());
            if (probability.compareTo(maxProbability) > 0) {
                maxProbability = probability;
                maxSport = (String) json.get("name");
            }
        }
        System.out.println("max: " + maxSport + " probability: " + maxProbability);

        return maxSport;
    }

    public void auth() throws IOException {
        OkHttpClient client = new OkHttpClient();

        RequestBody formBody = new FormBody.Builder()
                .add("grant_type", "client_credentials")
                .build();

        Request request = new Request.Builder()
                .url(AUTH_URL)
                .header("Authorization", "Basic " + BASIC)
                .post(formBody)
                .build();

        // basic auth
        Call call = client.newCall(request);
        Response response = call.execute();

        JsonElement element = JsonParser.parseString(Objects.requireNonNull(response.body()).string());
        JsonObject obj = element.getAsJsonObject();

        Set<Map.Entry<String, JsonElement>> entries = obj.entrySet();

        // getting access_token
        String accessToken = Objects.requireNonNull(entries.stream()
                .filter(e -> e.getKey().equals("access_token"))
                .map(Map.Entry::getValue)
                .findFirst().orElse(null)).toString().replace("\"", "");

        ACCESS_TOKEN = accessToken;
        System.out.println("Access token: " + accessToken);
    }


    public void ping(String url) throws IOException {
        auth();

        OkHttpClient client = new OkHttpClient();

        Request request = new Request.Builder()
                .url(url)
                .get()
                .build();

        Call call = client.newCall(request);
        Response response = call.execute();
        System.out.println(response);
    }
}
