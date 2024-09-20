package com.jeido.tournoisgamer.controller;

import com.jeido.tournoisgamer.entity.User;
import com.jeido.tournoisgamer.service.AuthService;
import com.jeido.tournoisgamer.service.UserService;
import com.jeido.tournoisgamer.utils.Role;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.UUID;

@Controller
public class UserController {
    private final UserService userService;
    private final AuthService authService;

    @Autowired
    public UserController(UserService userService, AuthService authService) {
        this.userService = userService;
        this.authService = authService;
        //DEBUG PLZ DELETE
        if (!userService.exist("Admin")) {
            userService.save(User.builder().name("Admin").password("adminadmin").email("admin@admin.com").imgPath("th.webp").role(Role.ADMIN).build());
        }
    }

    @RequestMapping("/user")
    public String home(Model model) {
        
        model.addAttribute("users", userService.findAll());
        
        return "user/list";
    }

    @RequestMapping("/user/{id}")
    public String detail(@PathVariable("id") UUID id, Model model){

        model.addAttribute("user", userService.findById(id));
        return "user/account";
    }

    @RequestMapping("/account")
    public String account(){
        if(!authService.isLogged()) {
            System.out.println("end3");
            return "redirect:/login";
        }

        User user = authService.getUser();

        if (user == null) {
            System.out.println("end2");
            return "redirect:/login";
        }
        System.out.println("end1");
        return "redirect:/user/" + user.getId();
    }



}
