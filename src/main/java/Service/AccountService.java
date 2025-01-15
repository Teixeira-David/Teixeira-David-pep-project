package Service;
import DAO.AccountDAO;
import Model.Account;

public class AccountService {
    private AccountDAO accountDao;

    // Constructor for the account service 
    public AccountService(AccountDAO accountDao) {
        this.accountDao = accountDao;
    }

    // Register an account
    public Account register(Account account) {
    
    }

    // Login an account
    public Account login(Account account) {
    
    }
}