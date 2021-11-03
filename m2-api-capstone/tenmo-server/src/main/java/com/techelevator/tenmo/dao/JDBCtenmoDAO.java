package com.techelevator.tenmo.dao;

import com.techelevator.tenmo.InsufficientBalanceException.InsufficientBalanceException;
import com.techelevator.tenmo.auth.model.User;
import com.techelevator.tenmo.model.Account;
import com.techelevator.tenmo.model.Transfer;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;

@Component
public class JDBCtenmoDAO implements tenmoDAO{

    private JdbcTemplate jdbcTemplate;

    public JDBCtenmoDAO(DataSource dataSource) {
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }

    @Override
    public Account retrieveAccountDetails(int userId){

        Account account = new Account();

        String sql = "SELECT * FROM accounts WHERE user_id = ?";

        SqlRowSet results = jdbcTemplate.queryForRowSet(sql, userId);

        while (results.next()) {
            account = mapRowToAccount(results);
        }

        return account;
    }

//    @Override
//    public double subtractMoney(int fromUserId, double moneyToSubtract) {
//
//        Account account = new Account();
//
//
//        String sql = "SELECT * FROM accounts WHERE user_id = ?";
//
//        SqlRowSet results = jdbcTemplate.queryForRowSet(sql, fromUserId);
//
//        while (results.next()) {
//            account = mapRowToAccount(results);
//        }
//
//        double accountBalance = account.getAccountBalance();
//        double newBalance = accountBalance - moneyToSubtract;
//
//        account.setAccountBalance(newBalance);
//
//        String sql2 = "UPDATE accounts SET balance = ? WHERE user_id = ?";
//        jdbcTemplate.update(sql2, newBalance, fromUserId);
//    }
//
//
//    @Override
//    public double addMoney(int toUserId, double moneyToAdd) {
//
//        Account account = new Account();
//
//
//        String sql = "SELECT * FROM accounts WHERE user_id = ?";
//
//        SqlRowSet results = jdbcTemplate.queryForRowSet(sql, toUserId);
//
//        while (results.next()) {
//            account = mapRowToAccount(results);
//        }
//
//        double accountBalance = account.getAccountBalance();
//        double newBalance = accountBalance + moneyToAdd;
//
//        account.setAccountBalance(newBalance);
//
//        String sql2 = "UPDATE accounts SET balance = ? WHERE user_id = ?";
//        jdbcTemplate.update(sql2, newBalance, toUserId);
//    }


    @Override
    public Transfer createTransfer(int toUserId, int fromUserId, double moneyToTransfer) throws InsufficientBalanceException {
        Transfer transfer = new Transfer();

        Account fromAccount = new Account();


        String sql1 = "SELECT * FROM accounts WHERE user_id = ?";

        SqlRowSet results = jdbcTemplate.queryForRowSet(sql1, fromUserId);

        if (results.next()) {
            fromAccount = mapRowToAccount(results);
        }

        double accountBalance1 = fromAccount.getAccountBalance();

        if (accountBalance1 >= moneyToTransfer) {
            double newBalance1 = accountBalance1 - moneyToTransfer;

            fromAccount.setAccountBalance(newBalance1);

            String sql2 = "UPDATE accounts SET balance = ? WHERE user_id = ?";
            jdbcTemplate.update(sql2, newBalance1, fromUserId);

        }
        else {
            throw new InsufficientBalanceException();
        }

        Account toAccount = new Account();


        String sql3 = "SELECT * FROM accounts WHERE user_id = ?";

        SqlRowSet results2 = jdbcTemplate.queryForRowSet(sql3, toUserId);

        if (results2.next()) {
            toAccount = mapRowToAccount(results2);
        }

        double accountBalance2 = toAccount.getAccountBalance();
        double newBalance2 = accountBalance2 + moneyToTransfer;

        toAccount.setAccountBalance(newBalance2);

        String sql4 = "UPDATE accounts SET balance = ? WHERE user_id = ?";
        jdbcTemplate.update(sql4, newBalance2, toUserId);

        String sql5 = "INSERT INTO transfers (transfer_type_id, transfer_status_id, account_from, account_to, amount) VALUES (2, 2, ?, ?, ?)";

        jdbcTemplate.update(sql5, fromAccount.getAccountId(), toAccount.getAccountId(), moneyToTransfer);

        //transfer.setTransferId(getNextTransferId());
        transfer.setAccountFrom(fromAccount.getAccountId());
        transfer.setAccountTo(toAccount.getAccountId());
        transfer.setAmount(moneyToTransfer);
        transfer.setTransferStatusId(2);
        transfer.setTransferTypeId(2);

        return transfer;
    }

    private Account mapRowToAccount(SqlRowSet results) {
        Account account = new Account();

        account.setAccountBalance(results.getDouble("balance"));
        account.setAccountId(results.getInt("account_id"));
        account.setUserId(results.getInt("user_id"));

        return account;

    }

//    private int getNextTransferId() {
//        SqlRowSet nextIdResult = jdbcTemplate.queryForRowSet("SELECT nextval('seq_transfer_id')");
//        return nextIdResult.getInt(1);
//    }



}
