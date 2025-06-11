package Service;
import Model.Account;
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
}
