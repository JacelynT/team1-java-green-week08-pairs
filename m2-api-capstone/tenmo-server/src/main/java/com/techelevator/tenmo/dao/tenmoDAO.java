package com.techelevator.tenmo.dao;

import com.techelevator.tenmo.Exceptions.InsufficientBalanceException;
import com.techelevator.tenmo.Exceptions.NegativeNumberException;
import com.techelevator.tenmo.model.Account;
import com.techelevator.tenmo.model.Transfer;

import java.util.List;

public interface tenmoDAO {


    //TODO methods
    public Account retrieveAccountDetails(int userId);

//    public Account addMoney(int toUserId, double moneyToAdd);
//
//    public void subtractMoney(int fromUserId, double moneyToSubtract);


    public Transfer createTransfer(int toUserId, int fromUserId, double moneyToTransfer) throws InsufficientBalanceException, NegativeNumberException;

    public List<Transfer> retrieveAllTransfersForUser(int accountId);

    //public List<Transfer> retrieveAllTransfersToUser(int accountId);

    public Transfer retrieveTransferDetails(int transferId);


}
