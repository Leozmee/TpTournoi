package com.jeido.tournoisgamer.repository;

import com.jeido.tournoisgamer.entity.Message;
import com.jeido.tournoisgamer.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface MessageRepository extends JpaRepository<Message, UUID> {
    List<Message> findByUser(User user);
}
