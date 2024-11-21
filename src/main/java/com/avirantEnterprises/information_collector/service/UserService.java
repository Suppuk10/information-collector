package com.avirantEnterprises.information_collector.service;


import com.avirantEnterprises.information_collector.model.Userr;
import com.avirantEnterprises.information_collector.repository.UserrRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {
    private final UserrRepository userRepository;

    public UserService(UserrRepository userRepository) {
        this.userRepository = userRepository;
    }

    public List<Userr> getAllUsers() {
        return userRepository.findAll();
    }

    public void saveUser(Userr user) {
        userRepository.save(user);
    }
}

