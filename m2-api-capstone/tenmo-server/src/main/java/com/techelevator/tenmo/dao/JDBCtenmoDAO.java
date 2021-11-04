package com.techelevator.tenmo.dao;

import com.techelevator.tenmo.InsufficientBalanceException.InsufficientBalanceException;
import com.techelevator.tenmo.auth.model.User;
import com.techelevator.tenmo.model.Account;
import com.techelevator.tenmo.model.Transfer;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;
import java.util.ArrayList;
import java.util.List;

@Component
public class JDBCtenmoDAO implements tenmoDAO {

    private JdbcTemplate jdbcTemplate;

    public JDBCtenmoDAO(DataSource dataSource) {
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }

    @Override
    public Account retrieveAccountDetails(int userId) {

        Account account = new Account();

        String sql = "SELECT * FROM accounts WHERE user_id = ?";

        SqlRowSet results = jdbcTemplate.queryForRowSet(sql, userId);

        while (results.next()) {
            account = mapRowToAccount(results);
        }

        return account;
    }


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
        } else {
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

    @Override
    public List<Transfer> retrieveAllTransfersForUser(int accountId) {
        List<Transfer> transferList = new ArrayList<>();

        String sql1 = "SELECT * FROM transfers JOIN accounts ON transfers.account_from = accounts.account_id JOIN users ON accounts.user_id = users.user_id WHERE transfers.account_to = ?";
        SqlRowSet results1 = jdbcTemplate.queryForRowSet(sql1, accountId);

        while (results1.next()) {
            Transfer transfer = mapRowToTransfer(results1);
            transfer.setAccountFromName(results1.getString("username"));
            transferList.add(transfer);
        }

        String sql2 = "SELECT * FROM transfers JOIN accounts ON transfers.account_to = accounts.account_id JOIN users ON accounts.user_id = users.user_id WHERE transfers.account_from = ?";
        SqlRowSet results2 = jdbcTemplate.queryForRowSet(sql2, accountId);

        while (results2.next()) {
            Transfer transfer = mapRowToTransfer(results2);
            transfer.setAccountToName(results2.getString("username"));
            transferList.add(transfer);
        }

        return transferList;
    }


    @Override
    public Transfer retrieveTransferDetails(int transferId) {
        Transfer transfer = new Transfer();

        String sql = "SELECT * FROM transfers WHERE transfer_id = ?";

        SqlRowSet results = jdbcTemplate.queryForRowSet(sql, transferId);

        if (results.next()) {
            transfer = mapRowToTransfer(results);
        }

        return transfer;
    }

    private Account mapRowToAccount(SqlRowSet results) {
        Account account = new Account();

        account.setAccountBalance(results.getDouble("balance"));
        account.setAccountId(results.getInt("account_id"));
        account.setUserId(results.getInt("user_id"));

        return account;

    }

    private Transfer mapRowToTransfer(SqlRowSet results) {
        Transfer transfer = new Transfer();

        transfer.setTransferStatusId(results.getInt("transfer_status_id"));
        transfer.setTransferTypeId(results.getInt("transfer_type_id"));
        transfer.setTransferId(results.getInt("transfer_id"));
        transfer.setAccountFrom(results.getInt("account_from"));
        transfer.setAccountTo(results.getInt("account_to"));
        transfer.setAmount(results.getDouble("amount"));

        return transfer;

    }


//    private int getNextTransferId() {
//        SqlRowSet nextIdResult = jdbcTemplate.queryForRowSet("SELECT nextval('seq_transfer_id')");
//        return nextIdResult.getInt(1);
//    }



}
