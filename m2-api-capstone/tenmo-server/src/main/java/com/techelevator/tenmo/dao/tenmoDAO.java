package com.techelevator.tenmo.dao;

import com.techelevator.tenmo.InsufficientBalanceException.InsufficientBalanceException;
import com.techelevator.tenmo.model.Account;
import com.techelevator.tenmo.model.Transfer;

public interface tenmoDAO {

    //TODO methods
    public Account retrieveAccountDetails(int userId);

//    public Account addMoney(int toUserId, double moneyToAdd);
//
//    public void subtractMoney(int fromUserId, double moneyToSubtract);


    public Transfer createTransfer(int toUserId, int fromUserId, double moneyToTransfer) throws InsufficientBalanceException;



}
