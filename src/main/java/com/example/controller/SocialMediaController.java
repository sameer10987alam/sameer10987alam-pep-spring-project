package com.example.controller;

import com.example.entity.Account;
import com.example.entity.Message;
import com.example.service.AccountService;
import com.example.service.MessageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
public class SocialMediaController {

    private final AccountService accountService;
    private final MessageService messageService;

    @Autowired
    public SocialMediaController(AccountService accountService, MessageService messageService) {
        this.accountService = accountService;
        this.messageService = messageService;
    }

    /* Account Endpoints */
    
    @PostMapping("/register")
    public ResponseEntity<Account> registerAccount(@RequestBody Account account) {
        try {
            Account registeredAccount = accountService.registerAccount(account);
            return ResponseEntity.ok(registeredAccount);
        } catch (IllegalArgumentException e) {
            if ("Username already exists".equals(e.getMessage())) {
                return ResponseEntity.status(409).build(); // Conflict
            }
            return ResponseEntity.badRequest().build(); // Invalid input
        }
    }

    @PostMapping("/login")
    public ResponseEntity<Account> login(@RequestBody Map<String, String> credentials) {
        String username = credentials.get("username");
        String password = credentials.get("password");
        
        if (username == null || username.isBlank() || 
            password == null || password.isBlank()) {
            return ResponseEntity.badRequest().build();
        }

        Optional<Account> account = accountService.loginAccount(username, password);
        return account.map(ResponseEntity::ok)
                     .orElseGet(() -> ResponseEntity.status(401).build());
    }

    /* Message Endpoints */
    
    @PostMapping("/messages")
    public ResponseEntity<Message> createMessage(@RequestBody Message message) {
        try {
            Message createdMessage = messageService.createMessage(message);
            return ResponseEntity.ok(createdMessage);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @GetMapping("/messages")
    public ResponseEntity<List<Message>> getAllMessages() {
        List<Message> messages = messageService.getAllMessages();
        return ResponseEntity.ok(messages);
    }

    @GetMapping("/messages/{messageId}")
    public ResponseEntity<Message> getMessageById(@PathVariable Integer messageId) {
        return messageService.getMessageById(messageId)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.ok().build());
    }

    @DeleteMapping("/messages/{messageId}")
    public ResponseEntity<Integer> deleteMessage(@PathVariable Integer messageId) {
        int result = messageService.deleteMessage(messageId);
        return result == 1 ? ResponseEntity.ok(result) : ResponseEntity.ok().build();
    }

    @PatchMapping("/messages/{messageId}")
    public ResponseEntity<Integer> updateMessage(@PathVariable Integer messageId,
                                               @RequestBody Message messageUpdate) {
        if (messageUpdate.getMessageText() == null || 
            messageUpdate.getMessageText().isBlank() ||
            messageUpdate.getMessageText().length() > 255) {
            return ResponseEntity.badRequest().build();
        }

        return messageService.updateMessage(messageId, messageUpdate.getMessageText())
                .map(msg -> ResponseEntity.ok(1))
                .orElse(ResponseEntity.badRequest().build());
    }

    @GetMapping("/accounts/{accountId}/messages")
    public ResponseEntity<List<Message>> getMessagesByUser(@PathVariable Integer accountId) {
        List<Message> messages = messageService.getMessagesByUserId(accountId);
        return ResponseEntity.ok(messages);
    }
}