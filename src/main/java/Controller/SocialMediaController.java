package Controller;

import DAO.AccountDAO;
import DAO.MessageDAO;
import Model.Account;
import Model.Message;
import Service.AccountService;
import Service.MessageService;
import io.javalin.Javalin;
import io.javalin.http.Context;

/**
 * TODO: You will need to write your own endpoints and handlers for your controller. The endpoints you will need can be
 * found in readme.md as well as the test cases. You should
 * refer to prior mini-project labs and lecture materials for guidance on how a controller may be built.
 */
public class SocialMediaController {
    // Declare the private variables
    private AccountService accountService;
    private MessageService messageService;

    // Init the account service object
    public SocialMediaController() {
        this.accountService = new AccountService(new AccountDAO());
        this.messageService = new MessageService(new MessageDAO(), new AccountDAO());
    }

    /**
     * In order for the test cases to work, you will need to write the endpoints in the startAPI() method, as the test
     * suite must receive a Javalin object from this method.
     * @return a Javalin app object which defines the behavior of the Javalin controller.
     */
    public Javalin startAPI() {
        Javalin app = Javalin.create();

        // User register and login endpoints here
        app.post("/register", this::handleRegistration);
        app.post("/login", this::handleLogin);

        // Message endpoints
        app.post("/messages", this::createMessage);
        app.get("/messages", this::getAllMessages);
        app.get("/messages/{message_id}", this::getMessageById);
        app.delete("/messages/{message_id}", this::deleteMessageById);
        app.patch("/messages/{message_id}", this::updateMessageById);
        app.get("/accounts/{account_id}/messages", this::getMessagesByUserId);

        return app;
    }

    // Handle the registration request
    private void handleRegistration(Context context) {
        try {
            // Parse the incoming JSON body into an Account object
            Account account = context.bodyAsClass(Account.class);

            // Call the service layer to handle the registration logic
            Account newAccount = accountService.register(account);

            // Respond with the created account (including account_id) and a 200 OK status
            context.json(newAccount).status(200);
        } catch (IllegalArgumentException e) {
            // Respond with a 400 Bad Request status for validation errors
            context.status(400).result("");
        }
    }

    // Handle the login request
    private void handleLogin(Context context) {
        try {
            // Parse the incoming JSON body into an Account object
            Account account = context.bodyAsClass(Account.class);

            // Call the service layer to handle the login logic
            Account existingAccount = accountService.login(account);

            // Respond with the existing account (including account_id) and a 200 OK status
            context.json(existingAccount).status(200);
        } catch (IllegalArgumentException e) {
            // Check if the exception indicates invalid credentials
            if ("Invalid username or password.".equals(e.getMessage())) {
                // Respond with 401 Unauthorized for authentication errors
                context.status(401).result("");
            } else {
                // Respond with 400 Bad Request for validation errors
                context.status(400).result("");
            }
        }
    }

    // Handle the create message request 
    private void createMessage(Context context) {
        try {
            // Parse the incoming JSON body into a Message object
            Message message = context.bodyAsClass(Message.class);

            // Call the service layer to handle the create message logic
            Message newMessage = messageService.createMessage(message);
            System.out.println("Created message: " + newMessage);

            // Respond with the created message (including message_id) and a 200 OK status
            context.json(newMessage).status(200);
        } catch (IllegalArgumentException e) {
            System.err.println("Validation error: " + e.getMessage());
            context.status(400).result("");
        } catch (Exception e) {
            System.err.println("Unexpected error:");
            e.printStackTrace();
            context.status(500).result("Internal server error.");
        }
    }        

    // Handle the get all messages request
    private void getAllMessages(Context context) {
        // Call the service layer to handle the get all messages logic
        context.json(messageService.getAllMessages()).status(200);
    }

    // Handle the get message by id request
    private void getMessageById(Context context) {
        try {
            // Get the message_id from the path parameter
            int message_id = Integer.parseInt(context.pathParam("message_id"));

            // Call the service layer to handle the get message by id logic
            Message message = messageService.getMessageById(message_id);

            if (message != null) {
                // Return the message as JSON
                context.json(message).status(200); 
            } else {
                // Return 200 with an empty body if not found
                context.status(200).result(""); 
            }
        } catch (NumberFormatException e) {
            // Handle invalid IDs
            context.status(400).result("Invalid message ID."); 
        }
    }

    // Handle the delete message by id request
    private void deleteMessageById(Context context) {
        try {
            // Get the message_id from the path parameter
            int message_id = Integer.parseInt(context.pathParam("message_id"));

            // Call the service layer to handle the delete message by id logic
            Message deletedMessage = messageService.deleteMessageById(message_id);

            if (deletedMessage != null) {
                // Return the deleted message as JSON
                context.json(deletedMessage).status(200); 
            } else {
                // Return 200 with an empty body if not found
                context.status(200).result(""); 
            }
        } catch (NumberFormatException e) {
            // Handle invalid ID formats
            context.status(400).result("Invalid message ID."); 
        }
    }

    // Handle the update message by id request
    private void updateMessageById(Context context) {
        try {
            // Get the message_id from the path parameter
            int message_id = Integer.parseInt(context.pathParam("message_id"));

            // Parse the incoming JSON body into a Message object
            Message message = context.bodyAsClass(Message.class);

            // Call the service layer to handle the update message by id logic
            Message updatedMessage = messageService.updateMessageById(message_id, message);

            if (updatedMessage != null) {
                // Return the updated message as JSON
                context.json(updatedMessage).status(200);
            } else {
                // Return 400
                context.status(400).result("");
            }
        } catch (NumberFormatException e) {
            // Handle invalid ID formats
            context.status(400).result("");
        } catch (IllegalArgumentException e) {
            // Handle validation errors
            context.status(400).result("");
        }
    }

    // Handle the get messages by user id request
    private void getMessagesByUserId(Context context) {
        try {
            // Get the account_id from the path parameter
            int account_id = Integer.parseInt(context.pathParam("account_id"));

            // Call the service layer to handle the get messages by user id logic
            context.json(messageService.getMessagesByUserId(account_id)).status(200);
        } catch (IllegalArgumentException e) {
            // Respond with a 400 Bad Request status for validation errors
            context.status(400).result(e.getMessage());
        }
    }
}