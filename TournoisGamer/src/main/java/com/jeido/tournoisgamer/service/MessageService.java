package com.jeido.tournoisgamer.service;

import com.jeido.tournoisgamer.entity.Message;
import com.jeido.tournoisgamer.entity.User;
import com.jeido.tournoisgamer.repository.MessageRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class MessageService {


    private final MessageRepository messageRepository;

    public MessageService(MessageRepository messageRepository) {
        this.messageRepository = messageRepository;
    }

    public Message save(Message message) {
        return messageRepository.save(message);
    }

    public List<Message> findAll() {
        return messageRepository.findAll();
    }

    public Message findById(UUID id) {
        return messageRepository.findById(id).orElse(null);
    }

    public List<Message> findBySender(User sender) {
        return messageRepository.findByUser(sender);
    }

    public void delete(Message message) {
        messageRepository.delete(message);
    }
}
