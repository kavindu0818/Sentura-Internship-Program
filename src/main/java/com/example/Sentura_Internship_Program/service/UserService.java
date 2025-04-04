package com.example.Sentura_Internship_Program.service;

import okhttp3.*;

import org.apache.tomcat.util.http.parser.MediaType;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;

import jakarta.security.auth.message.callback.PrivateKeyCallback.Request;

import java.io.IOException;

@Service
public class UserService {
    private final OkHttpClient client;
    private final String apiUrl;

    public UserService(@Value("${weavy.api.url}") String apiUrl) {
        this.client = new OkHttpClient();
        this.apiUrl = apiUrl;
    }

    // Create a new user
    public String createUser(String username, String email, String firstName, String lastName) throws IOException {
        JSONObject userJson = new JSONObject();
        userJson.put("username", username);
        userJson.put("email", email);
        userJson.put("first_name", firstName);
        userJson.put("last_name", lastName);
        
        RequestBody body = RequestBody.create(
            MediaType.parse("application/json"),
            userJson.toString()
        );
        
        Request request = new Request.Builder()
            .url(apiUrl + "/api/users")
            .addHeader("Content-Type", "application/json")
            .post(body)
            .build();
            
        try (Response response = client.newCall(request).execute()) {
            if (!response.isSuccessful()) {
                throw new IOException("Unexpected response code: " + response.code());
            }
            return response.body().string();
        }
    }
    
    // Get list of users
    public String listUsers(int page, int pageSize) throws IOException {
        HttpUrl.Builder urlBuilder = HttpUrl.parse(apiUrl + "/api/users").newBuilder();
        urlBuilder.addQueryParameter("page", String.valueOf(page));
        urlBuilder.addQueryParameter("page_size", String.valueOf(pageSize));
        
        Request request = new Request.Builder()
            .url(urlBuilder.build())
            .get()
            .build();
            
        try (Response response = client.newCall(request).execute()) {
            if (!response.isSuccessful()) {
                throw new IOException("Unexpected response code: " + response.code());
            }
            return response.body().string();
        }
    }
    
    // Get user details
    public String getUserDetails(String userId) throws IOException {
        Request request = new Request.Builder()
            .url(apiUrl + "/api/users/" + userId)
            .get()
            .build();
            
        try (Response response = client.newCall(request).execute()) {
            if (!response.isSuccessful()) {
                throw new IOException("Unexpected response code: " + response.code());
            }
            return response.body().string();
        }
    }
    
    // Update user
    public String updateUser(String userId, String jsonData) throws IOException {
        RequestBody body = RequestBody.create(
            MediaType.parse("application/json"),
            jsonData
        );
        
        Request request = new Request.Builder()
            .url(apiUrl + "/api/users/" + userId)
            .addHeader("Content-Type", "application/json")
            .patch(body)
            .build();
            
        try (Response response = client.newCall(request).execute()) {
            if (!response.isSuccessful()) {
                throw new IOException("Unexpected response code: " + response.code());
            }
            return response.body().string();
        }
    }
    
    // Delete user
    public String deleteUser(String userId) throws IOException {
        Request request = new Request.Builder()
            .url(apiUrl + "/api/users/" + userId)
            .delete()
            .build();
            
        try (Response response = client.newCall(request).execute()) {
            if (!response.isSuccessful() && response.code() != 204) {
                throw new IOException("Unexpected response code: " + response.code());
            }
            String responseBody = response.body() != null ? response.body().string() : "";
            return responseBody.isEmpty() ? "{\"success\": true, \"message\": \"User deleted successfully\"}" : responseBody;
        }
    }
}