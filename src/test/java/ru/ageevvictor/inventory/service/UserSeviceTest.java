package ru.ageevvictor.inventory.service;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.test.context.junit4.SpringRunner;
import ru.ageevvictor.inventory.model.User;

@RunWith(SpringRunner.class)
@SpringBootTest
public class UserSeviceTest {
    @Autowired
    private UserService userSevice;

    @Test
    public void addUser() {
        User user = new User();
        user.setUsername("confidential");
        user.setPassword(new BCryptPasswordEncoder().encode("confidential"));
        boolean isUserCreated = userSevice.addUser(user);
        Assert.assertTrue(isUserCreated);
    }
}