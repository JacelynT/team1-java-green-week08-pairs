package com.techelevator.tenmo.controller;

import com.techelevator.tenmo.Exceptions.InsufficientBalanceException;
import com.techelevator.tenmo.Exceptions.NegativeNumberException;
import com.techelevator.tenmo.auth.dao.UserDAO;
import com.techelevator.tenmo.auth.model.User;
import com.techelevator.tenmo.dao.tenmoDAO;
import com.techelevator.tenmo.model.Account;
import com.techelevator.tenmo.model.Transfer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@PreAuthorize("isAuthenticated()")
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
    public Transfer createTransfer(@RequestBody Transfer transfer) throws InsufficientBalanceException, NegativeNumberException {
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


}
