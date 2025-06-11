package Service;
import Model.Account;
import Model.Message;

import java.util.List;

import DAO.AccountDAO;

public class AccountService {
    AccountDAO accountDAO;

    /* constructors */
    public AccountService(){
        accountDAO = new AccountDAO();
    }

    public AccountService(AccountDAO accountDAO){
        this.accountDAO = accountDAO;
    }

    /* create new account */
    public Account addUser(Account account) {
        return accountDAO.insertAccount(account);
    }

    /* process login */
    public Account login(String username, String password) {
        return accountDAO.processLogin(username, password);
    }

    /* retrieve all messages by user */
    public List<Message> getAllMessagesByUser(int id) {
        return accountDAO.getAllMessagesByUser(id);
    }
}
