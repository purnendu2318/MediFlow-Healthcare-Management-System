package com.mediflow.service;

import com.mediflow.model.User;
import com.mediflow.repository.UserRepository;
import com.mediflow.security.PasswordUtil;

public class AuthService {
    private final UserRepository repo=new UserRepository();
    public User login(String email,String password) throws Exception {
        if(email==null || email.isBlank() || password==null || password.isBlank()) return null;
        User u=repo.findActiveByEmail(email.trim().toLowerCase());
        if(u==null) return null;
        String[] parts=u.role().split("\\|",2);
        if(parts.length!=2 || !PasswordUtil.matches(password,parts[1])) return null;
        return new User(u.id(),u.fullName(),u.email(),parts[0]);
    }
}
