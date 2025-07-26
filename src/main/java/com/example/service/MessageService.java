package com.example.service;

import com.example.entity.Message;
import com.example.repository.MessageRepository;
import com.example.repository.AccountRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
public class MessageService {
    private final MessageRepository messageRepository;
    private final AccountRepository accountRepository;

    @Autowired
    public MessageService(MessageRepository messageRepository, AccountRepository accountRepository) {
        this.messageRepository = messageRepository;
        this.accountRepository = accountRepository;
    }

    public Message createMessage(Message message) {
        validateMessage(message);
        return messageRepository.save(message);
    }

    public List<Message> getAllMessages() {
        return messageRepository.findAll();
    }

    public Optional<Message> getMessageById(Integer id) {
        return messageRepository.findById(id);
    }

    public List<Message> getMessagesByUserId(Integer userId) {
        return messageRepository.findByPostedBy(userId);
    }

    public int deleteMessage(Integer id) {
        if (messageRepository.existsById(id)) {
            messageRepository.deleteById(id);
            return 1;
        }
        return 0;
    }

    public Optional<Message> updateMessage(Integer id, String newText) {
        if (newText == null || newText.trim().isEmpty() || newText.length() > 255) {
            throw new IllegalArgumentException("Invalid message text");
        }

        return messageRepository.findById(id).map(message -> {
            message.setMessageText(newText);
            return messageRepository.save(message);
        });
    }

    private void validateMessage(Message message) {
        if (message.getMessageText() == null || 
            message.getMessageText().trim().isEmpty() || 
            message.getMessageText().length() > 255) {
            throw new IllegalArgumentException("Message text must be between 1 and 255 characters");
        }
        if (message.getPostedBy() == null || 
            !accountRepository.existsById(message.getPostedBy())) {
            throw new IllegalArgumentException("User does not exist");
        }
    }
}