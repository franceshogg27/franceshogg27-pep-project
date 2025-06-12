package DAO;

import Util.ConnectionUtil;
import Model.Account;
import Model.Message;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;
import java.util.ArrayList;

/*import org.junit.runners.model.Statement;

/* NOTE: THIS FILE WAS CREATED BY FRANCES 
 * METHODS:
 * public void addAccount();
 * public Account getAccountByUsername()
 * public Account getAccountById()
 * public void processLogin();
 * public List<Message> getAllMessagesByUser()
*/


public class AccountDAO {

    public Account getAccountById(int id) {
        try {
            Connection connection = ConnectionUtil.getConnection();
            String sql = "SELECT * FROM account WHERE account_id = ?";
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setInt(1, id);

            ResultSet rs = preparedStatement.executeQuery();
            if (rs.next()) {
                return new Account(id, rs.getString("username"), rs.getString("password"));
            }

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return null;
    }

    public Account getAccountByUsername(String username) {
        try {
            Connection connection = ConnectionUtil.getConnection();
            String sql = "SELECT * FROM account WHERE username = ?";
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1, username);

            ResultSet rs = preparedStatement.executeQuery();
            if (rs.next()) {
                return new Account(rs.getInt("account_id"), username, rs.getString("password"));
            }

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return null;
    }

    /*The registration will be successful if and only if the username is not blank, 
    the password is at least 4 characters long, and an Account with that username does not already exist. */
    public Account insertAccount(Account account) {
        if (getAccountByUsername(account.getUsername()) != null) {
            return null;
        }
        if (account.getUsername() == "" || account.getUsername() == null) {
            return null;
        }
        if (account.getPassword().length() < 4) {
            return null;
        }
        try {
            Connection connection = ConnectionUtil.getConnection();
            String sql = "INSERT INTO account (username, password) VALUES (?, ?)";
            PreparedStatement preparedStatement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);

            preparedStatement.setString(1, account.getUsername());
            preparedStatement.setString(2, account.getPassword());

            preparedStatement.executeUpdate();
            ResultSet rs = preparedStatement.getGeneratedKeys();
            if (rs.next()) {
                int generated_id = rs.getInt(1);
                return new Account(generated_id, account.getUsername(), account.getPassword());
            }

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return null;
    }

    public Account processLogin(Account account) {
        AccountDAO accountDAO = new AccountDAO();
        if (account == null) {
            return null;
        }
        Account matchingAccount = accountDAO.getAccountByUsername(account.username);
        if (matchingAccount == null) {
            return null;
        }
        if (matchingAccount.getPassword().equals(account.getPassword())) {
            return account;
        }
        return null;
    }

    public List<Message> getAllMessagesByUser(int account_id) {
        List<Message> messages = new ArrayList<>();
        try {
            Connection connection = ConnectionUtil.getConnection();
            String sql = "SELECT * FROM account AS a JOIN message AS m ON a.account_id = m.posted_by WHERE a.account_id = ?";

            PreparedStatement preparedStatement = connection.prepareStatement(sql);

            preparedStatement.setInt(1, account_id);

            ResultSet rs = preparedStatement.executeQuery();

            while (rs.next()) {
                Message message = new Message(rs.getInt("message_id"), 
                                    rs.getInt("posted_by"), 
                                    rs.getString("message_text"),
                                    rs.getLong("time_posted_epoch"));
                messages.add(message);
            }

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return messages;
    }
}
