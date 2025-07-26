package com.example.service;

import com.example.entity.Account;
import com.example.repository.AccountRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.Optional;

@Service
public class AccountService {
    private final AccountRepository accountRepository;

    @Autowired
    public AccountService(AccountRepository accountRepository) {
        this.accountRepository = accountRepository;
    }

    /**
     * Registers a new account after validating the input
     * @param account The account to register
     * @return The registered account
     * @throws IllegalArgumentException if validation fails
     */
    public Account registerAccount(Account account) {
        validateAccount(account);
        
        if (accountRepository.findByUsername(account.getUsername()).isPresent()) {
            throw new IllegalArgumentException("Username already exists");
        }
        
        return accountRepository.save(account);
    }

    /**
     * Authenticates a user with username and password
     * @param username The username to authenticate
     * @param password The password to verify
     * @return Optional containing the authenticated account if successful
     */
    public Optional<Account> loginAccount(String username, String password) {
        if (username == null || username.trim().isEmpty()) {
            throw new IllegalArgumentException("Username cannot be blank");
        }
        if (password == null || password.trim().isEmpty()) {
            throw new IllegalArgumentException("Password cannot be blank");
        }

        return accountRepository.findByUsername(username)
                .filter(account -> account.getPassword().equals(password));
    }

    /**
     * Validates account credentials
     * @param account The account to validate
     * @throws IllegalArgumentException if validation fails
     */
    private void validateAccount(Account account) {
        if (account.getUsername() == null || account.getUsername().trim().isEmpty()) {
            throw new IllegalArgumentException("Username cannot be blank");
        }
        if (account.getPassword() == null || account.getPassword().length() <= 4) {
            throw new IllegalArgumentException("Password must be longer than 4 characters");
        }
    }

    /**
     * Retrieves an account by its ID
     * @param id The account ID
     * @return Optional containing the account if found
     */
    public Optional<Account> getAccountById(Integer id) {
        return accountRepository.findById(id);
    }

    /**
     * Retrieves an account by username
     * @param username The username to search for
     * @return Optional containing the account if found
     */
    public Optional<Account> getAccountByUsername(String username) {
        return accountRepository.findByUsername(username);
    }

    /**
     * Checks if an account exists
     * @param accountId The account ID to check
     * @return true if the account exists, false otherwise
     */
    public boolean accountExists(Integer accountId) {
        return accountRepository.existsById(accountId);
    }

    /**
     * Validates account credentials for login
     * @param username The username to validate
     * @param password The password to validate
     * @throws IllegalArgumentException if validation fails
     */
    public void validateLoginCredentials(String username, String password) {
        if (username == null || username.trim().isEmpty()) {
            throw new IllegalArgumentException("Username cannot be blank");
        }
        if (password == null || password.trim().isEmpty()) {
            throw new IllegalArgumentException("Password cannot be blank");
        }
    }
}