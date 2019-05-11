package com.userinterface.Controllers;

import com.userinterface.Entities.User;
import com.userinterface.Services.FileService;
import com.userinterface.Services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import javax.validation.Valid;

@Controller
public class RegistrationController {
    private UserService userService;
    private FileService fileService;
    private BCryptPasswordEncoder passwordEncoder;

    @Autowired
    public void setPasswordEncoder(BCryptPasswordEncoder passwordEncoder) {
        this.passwordEncoder = passwordEncoder;
    }
    @Autowired
    public void setFileService(FileService fileService) {
        this.fileService = fileService;
    }
    @Autowired
    public void setUserService(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/register")
    public String ShowRegistrationPage(Model model){
        model.addAttribute("User",new User());
        return "registration-form";
    }

    @PostMapping("/register/saveUser")
    public String processRegistrationForm(@ModelAttribute("User") User user) {
        user.setFolder(user.getUserName());
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        userService.save(user);
        fileService.createFolder(user.getFolder());
        return "registration-confirmation";
    }
}
