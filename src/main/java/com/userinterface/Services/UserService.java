package com.userinterface.Services;


import com.userinterface.Entities.User;
import org.springframework.security.core.userdetails.UserDetailsService;

public interface UserService extends UserDetailsService{
    User findByUserName(String username);
    boolean save(User user);
    User getById(long id);
}
