package com.jeido.tournoisgamer.service;

import com.jeido.tournoisgamer.entity.User;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class AuthService {
    private final UserService userService;

    private final HttpSession httpSession;

    public AuthService(UserService userService, HttpSession httpSession) {
        this.userService = userService;
        this.httpSession = httpSession;
    }

    public boolean login(User user, String password){

        if (user == null) return false;

        if(user.getPassword().equals(password)){
            httpSession.setAttribute("id", user.getId());
            httpSession.setAttribute("isLoggedIn", true);
            return true;
        }
        return false;
    }

    public boolean isLogged(){
        try{
            return (boolean) httpSession.getAttribute("isLoggedIn");
        } catch (Exception ex){
            return false;
        }
    }

    public User getUser(){
        if (!isLogged()) return null;
        return userService.findById(UUID.fromString(httpSession.getAttribute("id").toString()));
    }

    public void logout(){
        httpSession.invalidate();
    }


}
