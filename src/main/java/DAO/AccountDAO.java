package DAO;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import Model.Account;
import Util.ConnectionUtil;

public class AccountDAO {
    
    // Register an account
    public Account register(Account account) {
        // Try to connect to the database
        try (Connection conn = ConnectionUtil.getConnection()) {
            // Set the sql insert string and prevent SQL injection
            String sql = "INSERT INTO account (username, password) VALUES (?,?);";

            // Prepare the statement and execute the query
            PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            ps.setString(1, account.getUsername());
            ps.setString(2, account.getPassword());

            // Execute the query and get the generated keys
            ps.executeUpdate();

            // Get the generated keys and set the account_id
            ResultSet rs = ps.getGeneratedKeys();
            rs.next();

            // Set the account_id and return the account
            account.setAccount_id(rs.getInt(1));

            // Return the account object
            return account;

        } catch (SQLException e) {
            // Print the exception and return null if the exception is caught
            e.printStackTrace();
            return null;
        }
    }

    // Login an account
    public Account login(String username, String password) {
        // Try to connect to the database
        try (Connection conn = ConnectionUtil.getConnection()) {
            // Set the sql select string and prevent SQL injection
            String sql = "SELECT * FROM account WHERE username = ? AND password = ?;";

            // Prepare the statement and execute the query
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, username);
            ps.setString(2, password);

            // Execute the query and get the result set
            ResultSet rs = ps.executeQuery();
            
            // If a row is found, create an account object and return it, otherwise return null
            if (rs.next()) {
                Account account = new Account();
                account.setAccount_id(rs.getInt("account_id"));
                account.setUsername(rs.getString("username"));
                account.setPassword(rs.getString("password"));
                return account;
            } else {
                // Return null if no account
                return null;
            }

        } catch (SQLException e) {
            // Print the exception and return null if the exception is caught
            e.printStackTrace();
            return null;
        }
    }
}