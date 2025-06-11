package Service;
import Model.Message;
import DAO.MessageDAO;

import java.util.List;

public class MessageService {
    MessageDAO messageDAO;

    /* constructors */
    public MessageService(){
        messageDAO = new MessageDAO();
    }

    public MessageService(MessageDAO messageDAO){
        this.messageDAO = messageDAO;
    } 

    /* create new message */
    public Message addMessage(Message message) {
        return messageDAO.insertMessage(message);
    }

    /* retrieve all messages */
    public List<Message> getAllMessages() {
        return messageDAO.getAllMessages();
    }

    /* retrieve message by id */
    public Message getMessageById(int id) {
        return messageDAO.deleteMessageByID(id);
    }

    /* delete a message by id */
    public Message deleteMessageById(int id) {
        return messageDAO.deleteMessageByID(id);
    }

    /* update a message by id */
    public Message updateMessageById(String message_text, int id) {
        return messageDAO.updateMessageByID(message_text, id);
    }
}
