package Controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import Service.AccountService;
import Service.MessageService;
import io.javalin.Javalin;
import io.javalin.http.Context;
import Model.Account;
import Model.Message;

/**
 * TODO: You will need to write your own endpoints and handlers for your controller. The endpoints you will need can be
 * found in readme.md as well as the test cases. You should
 * refer to prior mini-project labs and lecture materials for guidance on how a controller may be built.
 */
public class SocialMediaController {

    AccountService accountService;
    MessageService messageService;

    public SocialMediaController() {
        this.accountService = new AccountService();
        this.messageService = new MessageService();
    }

    /**
     * In order for the test cases to work, you will need to write the endpoints in the startAPI() method, as the test
     * suite must receive a Javalin object from this method.
     * @return a Javalin app object which defines the behavior of the Javalin controller.
     */
    /* public Account addUser(Account account)                          --> POST, /register. If null return 400.
     * public Account login(String username, String password)           --> POST, /login. If null return 401
     * public Message addMessage(Message message)                       --> POST, /messages. If null return 400.
     * public List<Message> getAllMessages()                            --> GET, /messages
     * public Message getMessageById(int id)                            --> GET, messages/{message_id}. Return empty if no message.
     * public Message deleteMessageById(int id)                         --> DELETE, messages/{message_id}. Return empty if no message.
     * public Message updateMessageById(String message_text, int id)    --> PATCH, messages/{message_id}. If null return 400.
     * public List<Message> getAllMessagesByUser(int id)                --> GET, accounts/{account_id}/messages
     */
    public Javalin startAPI() {
        Javalin app = Javalin.create();
        app.post("/register", this::postAccountRegister);
        app.post("/login", this::postAccountLogin);
        app.post("/messages", this::postMessage);
        app.get("/messages", this::getMessages);
        app.get("/messages/{message_id}", this::getMessageById);
        app.delete("/messages/{message_id}", this::deleteMessage);
        app.patch("/messages/{message_id}", this::updateMessage);
        app.get("accounts/{account_id}/messages", this::getMessagesByUser);
        return app;
    }

    /**
     * This is an example handler for an example endpoint.
     * @param context The Javalin Context object manages information about both the HTTP request and response.
     */
    private void postAccountRegister(Context ctx) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        Account account = mapper.readValue(ctx.body(), Account.class);
        Account addedAccount = accountService.addUser(account);
        if(addedAccount==null){
            ctx.status(400);
        }else{
            ctx.json(mapper.writeValueAsString(addedAccount));
        }
    }

    private void postAccountLogin(Context ctx) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        Account account = mapper.readValue(ctx.body(), Account.class);
        Account loggedAccount = accountService.login(account);
        if(loggedAccount==null){
            ctx.status(401);
        }else{
            Account accountWithId = new Account(accountService.getAccountByUsername(account.getUsername()).getAccount_id(), account.getUsername(), account.getPassword());
            ctx.json(mapper.writeValueAsString(accountWithId));
        }
    }

    private void postMessage(Context ctx) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        Message message = mapper.readValue(ctx.body(), Message.class);
        Message addedMessage = messageService.addMessage(message);
        if(addedMessage==null){
            ctx.status(400);
        }else{
            ctx.json(mapper.writeValueAsString(addedMessage));
        }
    }

    private void getMessages(Context ctx) throws JsonProcessingException {
        ctx.json(messageService.getAllMessages());
    }

    private void getMessageById(Context ctx) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        Message gotMessage = messageService.getMessageById(Integer.parseInt(ctx.pathParam("message_id")));
        if (gotMessage == null) {
            ctx.json("");
        }
        else {
            ctx.json(mapper.writeValueAsString(gotMessage));
        }
    }

    private void deleteMessage(Context ctx) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        int message_id = Integer.parseInt(ctx.pathParam("message_id"));
        Message deletedMessage = messageService.deleteMessageById(message_id);
        if (deletedMessage == null) {
            ctx.json("");
        }
        else {
            ctx.json(mapper.writeValueAsString(deletedMessage));
        }
    }

    private void updateMessage(Context ctx) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        int message_id = Integer.parseInt(ctx.pathParam("message_id"));
        Message message = mapper.readValue(ctx.body(), Message.class);
        String message_text = message.message_text;
        Message updatedMessage = messageService.updateMessage(message_text, message_id);
        if (updatedMessage == null) {
            ctx.status(400);
        }
        else {
            ctx.json(mapper.writeValueAsString(updatedMessage));
        }
    }

    private void getMessagesByUser(Context ctx) throws JsonProcessingException {
        ctx.json(accountService.getAllMessagesByUser(Integer.parseInt(ctx.pathParam("account_id"))));
    }
}