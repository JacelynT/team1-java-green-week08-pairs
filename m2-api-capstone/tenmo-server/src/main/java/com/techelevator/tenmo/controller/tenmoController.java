package com.techelevator.tenmo.controller;

import com.techelevator.tenmo.InsufficientBalanceException.InsufficientBalanceException;
import com.techelevator.tenmo.auth.dao.UserDAO;
import com.techelevator.tenmo.auth.model.User;
import com.techelevator.tenmo.dao.tenmoDAO;
import com.techelevator.tenmo.model.Account;
import com.techelevator.tenmo.model.Transfer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
public class tenmoController {

    @Autowired
    private tenmoDAO dao;
    @Autowired
    private UserDAO userDAO;


    @RequestMapping(path = "/accounts/{userId}", method = RequestMethod.GET)
    public Account retrieveAccountDetails(@PathVariable int userId) {
        return dao.retrieveAccountDetails(userId);
    }


    @RequestMapping(path = "/transfers", method = RequestMethod.POST)
    public Transfer createTransfer(@RequestBody Transfer transfer) throws InsufficientBalanceException {
        return dao.createTransfer (transfer.getAccountTo(), transfer.getAccountFrom(), transfer.getAmount());
    }

    @RequestMapping(path = "/users", method = RequestMethod.GET)
    public List<User> retrieveAllUsers() {
        return userDAO.findAll();
    }

    @RequestMapping(path = "/transfers/{accountId}", method = RequestMethod.GET)
    public List<Transfer> retrieveTransfersForUser(@PathVariable int accountId) {
        return dao.retrieveAllTransfersForUser(accountId);
    }

    @RequestMapping(path = "/transfer-details/{transferId}", method = RequestMethod.GET)
    public Transfer retrieveTransferDetails(@PathVariable int transferId){
        return dao.retrieveTransferDetails(transferId);
    }

//    @RequestMapping(path = "/transfers/account-from/{accountId}")
//    public List<Transfer> retrieveTransfersFromUser(@PathVariable int accountId) {
//        return dao.retrieveAllTransfersFromUser(accountId);
//    }


}
