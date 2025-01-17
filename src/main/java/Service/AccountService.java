package Service;
import DAO.AccountDAO;
import Model.Account;

public class AccountService {
    // Declare private variables
    private AccountDAO accountDao;

    // Constructor for the account service 
    public AccountService(AccountDAO accountDao) {
        this.accountDao = accountDao;
    }

    // Register an account
    public Account register(Account account) {
        // Validate the account
        validateAccountForRegistration(account); 
        if (accountDao.existsByUsername(account.getUsername())) {
            throw new IllegalArgumentException("Username already exists.");
        }
        // Register the account and return the new account
        return accountDao.register(account); 
    }

    // Login an account
    public Account login(Account account) {
        // Validate login input
        validateAccountForLogin(account); 
        Account existingAccount = accountDao.login(account.getUsername(), account.getPassword());
        if (existingAccount == null) {
            throw new IllegalArgumentException("Invalid username or password."); // Optional for stricter checks
        }
        return existingAccount;
    }

    // Validate the account for registration
    private void validateAccountForRegistration(Account account) {
        if (account == null || isNullOrEmpty(account.getUsername())) {
            throw new IllegalArgumentException("Username cannot be empty.");
        }
        if (isNullOrEmpty(account.getPassword()) || account.getPassword().length() < 4) {
            throw new IllegalArgumentException("Password must be at least 4 characters long.");
        }
    }

    // Validate the account for login
    private void validateAccountForLogin(Account account) {
        if (account == null || isNullOrEmpty(account.getUsername())) {
            throw new IllegalArgumentException("Username cannot be blank.");
        }
        if (isNullOrEmpty(account.getPassword())) {
            throw new IllegalArgumentException("Password cannot be blank.");
        }
    }

    // Helper method to check if a string is null or empty
    private boolean isNullOrEmpty(String input) {
        return input == null || input.trim().isEmpty();
    }
}