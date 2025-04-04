package com.example.Sentura_Internship_Program.service;

import okhttp3.*;

import org.apache.coyote.Request;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import java.io.IOException;

@Service
public class UserService {
    private final OkHttpClient client;
    private final String apiUrl;

    public UserService(@Value("${weavy.api.url}") String apiUrl) {
        this.client = new OkHttpClient();
        this.apiUrl = apiUrl;
    }


    public String createUser(String username, String email, String firstName, String lastName) throws IOException {
        JSONObject userJson = new JSONObject();
        userJson.put("username", username);
        userJson.put("email", email);
        userJson.put("first_name", firstName);
        userJson.put("last_name", lastName);

        RequestBody body = RequestBody.create(userJson.toString(), MediaType.get("application/json"));

        Request request = new Request.Builder()
            .url(apiUrl + "/users")
            .addHeader("Content-Type", "application/json")
            .post(body)
            .build();

        return executeRequest(request);
    }

  
    public String listUsers(int page, int pageSize) throws IOException {
        HttpUrl url = HttpUrl.parse(apiUrl + "/users")
                .newBuilder()
                .addQueryParameter("page", String.valueOf(page))
                .addQueryParameter("page_size", String.valueOf(pageSize))
                .build();

        Request request = new Request.Builder()
            .url(url)
            .addHeader("Content-Type", "application/json")
            .get()
            .build();

        return executeRequest(request);
    }


    public String getUserDetails(String userId) throws IOException {
        Request request = new Request.Builder()
            .url(apiUrl + "/users/" + userId)
            .addHeader("Content-Type", "application/json")
            .get()
            .build();

        return executeRequest(request);
    }


    public String updateUser(String userId, String jsonData) throws IOException {
        RequestBody body = RequestBody.create(jsonData, MediaType.get("application/json"));

        Request request = new Request.Builder()
            .url(apiUrl + "/users/" + userId)
            .addHeader("Content-Type", "application/json")
            .patch(body)
            .build();

        return executeRequest(request);
    }

  
    public String deleteUser(String userId) throws IOException {
        Request request = new Request.Builder()
            .url(apiUrl + "/users/" + userId)
            .delete()
            .build();

        return executeRequest(request);
    }

 
    private String executeRequest(Request request) throws IOException {
        try (Response response = client.newCall(request).execute()) {
            if (!response.isSuccessful()) {
                throw new IOException("Error: " + response.code() + " - " + response.message());
            }
            return response.body().string();
        }
    }
}
