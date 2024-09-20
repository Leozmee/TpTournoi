package com.jeido.tournoisgamer.controller;

import com.jeido.tournoisgamer.entity.User;
import com.jeido.tournoisgamer.service.AuthService;
import com.jeido.tournoisgamer.service.UserService;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

@Controller
@MultipartConfig(maxFileSize = 1024 * 1024 * 10) // 10 MB MAX
public class AuthController {

    private static final String location = "src/main/resources/static/image/avatar";
    private final UserService userService;
    private final AuthService authService;

    public AuthController(UserService userService, AuthService authService) {
        this.userService = userService;
        this.authService = authService;
    }

    @RequestMapping("/registration")
    public String inscription(Model model) {
        if (authService.isLogged()) {
            return "redirect:/";
        }
        model.addAttribute("user", User.builder().name("").email("").password("").build());
        model.addAttribute("mode", "registration");
        return "/login/form";
    }

    @PostMapping("/registration")
    public String inscriptionForm(@Valid @ModelAttribute("user") User user,
                                  BindingResult bindingResult,
                                  @RequestParam("image") MultipartFile image, Model model) throws IOException {



        Path destination = Paths.get(location).resolve(image.getOriginalFilename()).toAbsolutePath();
        user.setImgPath(image.getOriginalFilename());
        try {
            InputStream inputStream = image.getInputStream();
            Files.copy(inputStream, destination, StandardCopyOption.REPLACE_EXISTING);

        } catch (IOException e) {
            System.out.println("ERROR IMG REGISTER");
            model.addAttribute("error", "Image must be 10 MB and 1000px x 1000px Max!");
            model.addAttribute("mode", "registration");
            return "/login/form";
        }

        if (bindingResult.hasErrors()) {
            model.addAttribute("mode", "registration");
            System.out.println("ERROR: " + bindingResult.getAllErrors());
            return "/login/form";
        }
        authService.login(user, user.getPassword());
        userService.save(user);


        return "redirect:/";
    }

    @RequestMapping("/login")
    public String connexion(Model model) {
        if (authService.isLogged()) {
            return "redirect:/";
        }
        model.addAttribute("user", User.builder().name("").email("").password("").build());
        model.addAttribute("mode", "login");
        return "/login/form";
    }

    @PostMapping("/login")
    public String connexionForm(@ModelAttribute("name") String username, @ModelAttribute("password") String
            password, Model model) {
        boolean connected = authService.login(userService.findByUsername(username), password);
        if (connected) {
            return "redirect:/";
        }
        return "redirect:/login";
    }

    @RequestMapping("/logout")
    public String logout() {
        authService.logout();
        return "redirect:/";
    }
}
