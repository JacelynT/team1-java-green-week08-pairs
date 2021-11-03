package com.techelevator.tenmo.controller;

import com.techelevator.tenmo.dao.tenmoDAO;
import com.techelevator.tenmo.model.Account;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
public class tenmoController {

    @Autowired
    private tenmoDAO dao;


    @RequestMapping(path = "/accounts/{userId}", method = RequestMethod.GET)
    public Account retrieveAccountDetails(@PathVariable int userId) {
        return dao.retrieveAccountDetails(userId);
    }
}
