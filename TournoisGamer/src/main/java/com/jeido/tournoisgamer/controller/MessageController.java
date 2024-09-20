package com.jeido.tournoisgamer.controller;

import com.jeido.tournoisgamer.entity.Message;
import com.jeido.tournoisgamer.service.AuthService;
import com.jeido.tournoisgamer.service.MessageService;
import com.jeido.tournoisgamer.utils.Role;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.UUID;

@Controller
public class MessageController {


    private final AuthService authService;
    private final MessageService messageService;

    @Autowired
    public MessageController(AuthService authService, MessageService messageService) {

        this.authService = authService;
        this.messageService = messageService;
    }

    @GetMapping("/messages")
    public String messages(Model model) {
        if (!authService.isLogged()) {
            return "redirect:/login";
        }
        model.addAttribute("messages", messageService.findAll());
        model.addAttribute("user", authService.getUser());
        model.addAttribute("isAdmin", authService.getUser().getRole() == Role.ADMIN);
        return "messages";
    }

    @PostMapping("/messages")
    public String messages(@RequestParam("message")String msg) {
        if(!authService.isLogged()) {
            return "redirect:/login";
        }

        Message message = Message.builder().content(msg).user(authService.getUser()).dateTime(LocalDateTime.now()).build();

        messageService.save(message);

        return "redirect:/messages";
    }

    @GetMapping("/messages/delete/{id}")
    public String deleteMessage(@PathVariable("id") UUID id) {
        if(!authService.isLogged()) {
            return "redirect:/login";
        }

        Message messageToDelete = messageService.findById(id);

        if(messageToDelete == null) {
            //TODO error Message not found
            return "redirect:/messages";
        }

        if(authService.getUser().getRole() != Role.ADMIN || !messageToDelete.getUser().getId().equals(authService.getUser().getId())) {
            //TODO error Unauthorized
            return "redirect:/messages";
        }

        messageService.delete(messageToDelete);
        return "redirect:/messages";
    }

}
