package DAO;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import Model.Message;
import Util.ConnectionUtil;

public class MessageDAO {
    // Insert a message into the database
    public Message createMessage(Message message) {
        // Try to connect to the database and insert the message into the messages table
        try (Connection conn = ConnectionUtil.getConnection()) {
            // Set the sql insert string and prevent SQL injection
            String sql = "INSERT INTO message (posted_by, message_text, time_posted_epoch) VALUES (?,?,?)";

            // Prepare the statement and execute the query
            PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            ps.setInt(1, message.getPosted_by());
            ps.setString(2, message.getMessage_text());
            ps.setLong(3, message.getTime_posted_epoch());

            // Execute the query and get the generated keys
            int rowsAffected = ps.executeUpdate();
            System.out.println("Rows affected: " + rowsAffected);

            // If a row is inserted, get the generated ID and set it in the message object
            if (rowsAffected > 0) {
                ResultSet rs = ps.getGeneratedKeys();
                if (rs.next()) {
                    // Set the generated ID and message_id
                    int generatedId = rs.getInt(1);
                    message.setMessage_id(generatedId);
                    System.out.println("Generated message ID: " + generatedId);
                    return message;
                }
            }
    
        } catch (SQLException e) {
            // Print the exception and return null if the exception is caught
            e.printStackTrace();
            return null;
        }    
        return null;
    }

    // Get all messages from the database
    public List<Message> getAllMessages() {
        // Try to connect to the database and get all messages from the messages table
        try (Connection conn = ConnectionUtil.getConnection()) {
            // Set the sql select string and prevent SQL injection
            String sql = "SELECT * FROM message";

            // Prepare the statement and execute the query
            PreparedStatement ps = conn.prepareStatement(sql);

            // Execute the query and get the result set
            ResultSet rs = ps.executeQuery();

            // Create a list of messages and return it
            List<Message> messages = new ArrayList<>();

            // Iterate through the result set and add each message to the list
            while (rs.next()) {
                messages.add(
                    new Message(
                        rs.getInt("message_id"),
                        rs.getInt("posted_by"), 
                        rs.getString("message_text"), 
                        rs.getLong("time_posted_epoch")
                        )
                    );
            }

            // Return the message
            return messages;

        } catch (SQLException e) {
            // Print the exception and return null if the exception is caught
            e.printStackTrace();
            return null;
        }
    }

    // Get a message by its id from the database
    public Message getMessageById(int id) {
        // Try to connect to the database and get the message by its id from the messages table
        try (Connection conn = ConnectionUtil.getConnection()) {
            // Set the sql select string and prevent SQL injection
            String sql = "SELECT * FROM message WHERE message_id = ?";

            // Prepare the statement and execute the query
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setInt(1, id);

            // Execute the query and get the result set
            ResultSet rs = ps.executeQuery();

            // Check if there is a message with the given id
            if (rs.next()) {
                // Create a new message and return it
                return new Message(
                    rs.getInt("message_id"), 
                    rs.getInt("posted_by"), 
                    rs.getString("message_text"), 
                    rs.getLong("time_posted_epoch")
                    );
            } else {
                // Return null if no message was found
                return null;
            }

        } catch (SQLException e) {
            // Print the exception and return null if the exception is caught
            e.printStackTrace();
            return null;
        }
    }

    // Delete a message by its id from the database
    public boolean deleteMessageById(int id) {
        // Try to connect to the database and delete the message by its id from the messages table
        try (Connection conn = ConnectionUtil.getConnection()) {
            // Set the sql delete string and prevent SQL injection
            String sql = "DELETE FROM message WHERE message_id = ?;";

            // Prepare the statement and execute the query
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setInt(1, id);

            // Execute the query and check if the row was deleted
            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            // Print the exception and return null if the exception is caught
            e.printStackTrace();
            return false;
        }
    }

    // Update a message by its id in the database
    public Message updateMessageById(int id, String newText) {
        // Try to connect to the database and update the message text by its id in the messages table
        try (Connection conn = ConnectionUtil.getConnection()) {
            // Set the sql update string and prevent SQL injection
            String sql = "UPDATE message SET message_text = ? WHERE message_id = ?";

            // Prepare the statement and execute the query
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, newText);
            ps.setInt(2, id);

            // Execute the query and check if the row was updated
            if (ps.executeUpdate() > 0) {
                // Get the updated message by its id from the database
                return getMessageById(id);
            } else {
                // Return null if no message was updated
                return null;
            }

        } catch (SQLException e) {
            // Print the exception and return null if the exception is caught
            e.printStackTrace();
            return null;
        }
    }

    // Get all messages by a user id from the database
    public List<Message> getMessagesByUserId(int userId) {
        // Try to connect to the database and get all messages by a user id from the messages table
        try (Connection conn = ConnectionUtil.getConnection()) {
            // Set the sql select string and prevent SQL injection
            String sql = "SELECT * FROM message WHERE posted_by = ?";

            // Prepare the statement and execute the query
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setInt(1, userId);

            // Execute the query and get the result set
            ResultSet rs = ps.executeQuery();

            // Create a list of messages and return it
            List<Message> messages = new ArrayList<>();

            // Iterate through the result set and add each message to the list
            while (rs.next()) {
                messages.add(
                    new Message(
                        rs.getInt("message_id"),
                        rs.getInt("posted_by"), 
                        rs.getString("message_text"), 
                        rs.getLong("time_posted_epoch")
                        )
                    );
            }

            // Return the message
            return messages;

        } catch (SQLException e) {
            // Print the exception and return null if the exception is caught
            e.printStackTrace();
            return null;
        }
    }
}