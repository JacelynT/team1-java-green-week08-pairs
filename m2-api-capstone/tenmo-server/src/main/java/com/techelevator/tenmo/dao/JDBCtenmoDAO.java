package com.techelevator.tenmo.dao;

import com.techelevator.tenmo.model.Account;
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

    //TODO implement
    public Account retrieveAccountDetails(int userId){

        Account account = new Account();

        String sql = "SELECT * FROM accounts WHERE user_id = ?";

        SqlRowSet results = jdbcTemplate.queryForRowSet(sql, userId);

        while (results.next()) {
            account = mapRowToAccount(results);
        }

        return account;
    }


    private Account mapRowToAccount(SqlRowSet results) {
        Account account = new Account();

        account.setAccountBalance(results.getDouble("balance"));
        account.setAccountId(results.getInt("account_id"));
        account.setUserId(results.getInt("user_id"));

        return account;

    }

}
