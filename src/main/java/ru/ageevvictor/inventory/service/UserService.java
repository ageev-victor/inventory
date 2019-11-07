package ru.ageevvictor.inventory.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.ageevvictor.inventory.model.User;
import ru.ageevvictor.inventory.repository.UserRepository;

@Service
public class UserService {

    private UserRepository userRepository;

    @Autowired
    public void setUserRepository(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    boolean addUser(User user) {
        userRepository.save(user);
        boolean result = false;
        if (userRepository.existsByUsername(user.getUsername())) {
            result = true;
        }
        return result;
    }
}
