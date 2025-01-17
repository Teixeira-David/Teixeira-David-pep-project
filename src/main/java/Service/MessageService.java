package Service;
import java.util.List;

import DAO.AccountDAO;
import DAO.MessageDAO;
import Model.Message;

public class MessageService {
    private MessageDAO messageDAO;
    private AccountDAO accountDAO;

    // Constructor for the message service
    public MessageService(MessageDAO messageDAO, AccountDAO accountDAO) {
        this.messageDAO = messageDAO;
        this.accountDAO = accountDAO;
    }

    // Create a message in the database
    public Message createMessage(Message message) {
        // Validate the message
        validateMessageForCreation(message);
        // If valid, return the new message
        return messageDAO.createMessage(message);
    }

    // Get all messages from the database
    public List<Message> getAllMessages() {
        return messageDAO.getAllMessages();
    }

    // Get a message by id
    public Message getMessageById(int id) {
        return messageDAO.getMessageById(id);
    }

    // Delete a message by id
    public Message deleteMessageById(int id) {
        Message existingMessage = messageDAO.getMessageById(id);
        if (existingMessage == null) {
            return null; // No message found with the given ID
        }
        
        // Delete the message by id and return the deleted message if successful
        boolean success = messageDAO.deleteMessageById(id);
        // Return the deleted message if successful
        return success ? existingMessage : null; 
    }
    

    // Update a message by id
    public Message updateMessageById(int id, Message updatedMessage) {
        // Validate the updated message
        validateMessageForUpdate(updatedMessage);
        // Get the existing message by id
        Message existingMessage = messageDAO.getMessageById(id);
        // If the message not found then return null
        if (existingMessage == null) {
            return null;
        }

        // Update the message by id and return the updated message
        return messageDAO.updateMessageById(id, updatedMessage.message_text);
    }


    // Get messages by user id
    public List<Message> getMessagesByUserId(int userId) {
        return messageDAO.getMessagesByUserId(userId);
    }

    // Validate the message for creation
    private void validateMessageForCreation(Message message) {
        if (message == null) {
            throw new IllegalArgumentException("Message cannot be null.");
        }
        if (isNullOrEmpty(message.getMessage_text()) || message.getMessage_text().length() > 255) {
            throw new IllegalArgumentException("Message text must be between 1 and 255 characters.");
        }
        if (!accountDAO.existsById(message.getPosted_by())) {
            throw new IllegalArgumentException("The user posting the message does not exist.");
        }
        // Log successful validation
        System.out.println("Validation successful for message: " + message);        
    }

    // Validate the message for update
    private void validateMessageForUpdate(Message message) {
        if (message == null) {
            throw new IllegalArgumentException("Message cannot be null.");
        }
        if (isNullOrEmpty(message.getMessage_text()) || message.getMessage_text().length() > 255) {
            throw new IllegalArgumentException("Message text must be between 1 and 255 characters.");
        }
    }

    // Helper method to check if a string is null or empty
    private boolean isNullOrEmpty(String input) {
        return input == null || input.trim().isEmpty();
    }    
}