package com.techelevator.tenmo.services;

import com.techelevator.tenmo.auth.models.User;
import com.techelevator.tenmo.models.Account;
import com.techelevator.tenmo.models.Transfer;
import io.cucumber.java.en_old.Ac;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;

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

    public void createTransfer(Transfer transfer) {
        HttpEntity entity = makeTransferEntity(transfer);

        restTemplate.exchange(BASE_SERVICE_URL + "transfers", HttpMethod.POST, entity, Transfer.class);

    }

    public User[] retrieveAllUsers() {
        User[] users = null;

        HttpEntity entity = makeAuthEntity();

        users = restTemplate.exchange(BASE_SERVICE_URL + "users", HttpMethod.GET, entity, User[].class).getBody();

        return users;
    }

    public Transfer[] retrieveTransfersForUser(int accountId) {
        Transfer[] transfers = null;

        HttpEntity entity = makeAuthEntity();

        transfers = restTemplate.exchange(BASE_SERVICE_URL + "transfers/" + accountId, HttpMethod.GET, entity, Transfer[].class).getBody();

        return transfers;
    }

    public Transfer retrieveTransferDetails(int transferId){
        Transfer transfer = null;

        HttpEntity entity = makeAuthEntity();

        transfer = restTemplate.exchange(BASE_SERVICE_URL + "transfer-details/" + transferId, HttpMethod.GET, entity, Transfer.class).getBody();

        return transfer;
    }


    private HttpEntity<Transfer> makeTransferEntity(Transfer transfer) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth(AUTH_TOKEN);
        HttpEntity<Transfer> entity = new HttpEntity<>(transfer, headers);
        return entity;
    }


    private HttpEntity makeAuthEntity() {
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(AUTH_TOKEN);
        HttpEntity entity = new HttpEntity<>(headers);
        return entity;
    }


    public void setAUTH_TOKEN(String token) {
        AUTH_TOKEN = token;
    }
}
