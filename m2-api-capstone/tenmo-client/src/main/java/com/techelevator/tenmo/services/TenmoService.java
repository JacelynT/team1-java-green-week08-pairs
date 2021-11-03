package com.techelevator.tenmo.services;

import com.techelevator.tenmo.model.Account;
import io.cucumber.java.en_old.Ac;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.web.client.RestTemplate;

public class TenmoService {

    private final String BASE_SERVICE_URL;
    public String AUTH_TOKEN = "";
    private RestTemplate restTemplate = new RestTemplate();

    public TenmoService(String baseUrl) {
        this.BASE_SERVICE_URL = baseUrl;
    }

    public Account retrieveAccountDetails(int userId) {
        Account account = null;

        HttpEntity entity = makeAuthEntity();

        account = restTemplate.exchange(BASE_SERVICE_URL + "accounts/" + userId, HttpMethod.GET, entity, Account.class).getBody();

        return account;
    }


    private HttpEntity makeAuthEntity() {
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(AUTH_TOKEN);
        HttpEntity entity = new HttpEntity<>(headers);
        return entity;
    }

}
