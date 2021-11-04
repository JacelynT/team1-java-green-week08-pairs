package com.techelevator.tenmo.dao;

import com.techelevator.tenmo.InsufficientBalanceException.InsufficientBalanceException;
import com.techelevator.tenmo.auth.model.User;
import com.techelevator.tenmo.model.Account;
import com.techelevator.tenmo.model.Transfer;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

public interface tenmoDAO {


    //TODO methods
    public Account retrieveAccountDetails(int userId);

//    public Account addMoney(int toUserId, double moneyToAdd);
//
//    public void subtractMoney(int fromUserId, double moneyToSubtract);


    public Transfer createTransfer(int toUserId, int fromUserId, double moneyToTransfer) throws InsufficientBalanceException;

    public List<Transfer> retrieveAllTransfersForUser(int accountId);

    //public List<Transfer> retrieveAllTransfersToUser(int accountId);

    public Transfer retrieveTransferDetails(int transferId);


}
