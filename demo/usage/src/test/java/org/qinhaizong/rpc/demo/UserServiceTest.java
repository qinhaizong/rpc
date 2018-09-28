package org.qinhaizong.rpc.demo;

import org.junit.Assert;
import org.junit.Test;
import org.qinhaizong.rpc.core.client.RpcClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.UUID;

public class UserServiceTest {

    private static final Logger LOGGER = LoggerFactory.getLogger(UserServiceTest.class);

    private UserService userService = RpcClient.from(UserService.class, "http://localhost:8080");

    @Test
    public void save() {
        User user = new User();
        user.setId(UUID.randomUUID().toString());
        user.setAge(20);
        user.setName("Spring");
        userService.save(user);
    }

    @Test
    public void findAll() {
        for (int i = 0; i < 10; i++) {
            List<User> all = userService.findAll();
            LOGGER.info("{}", all);
            Assert.assertNotNull(all);
        }
    }

    @Test
    public void findUserById() {
        User user = userService.findUserById(UUID.randomUUID().toString());
        LOGGER.info("{}", user);
        Assert.assertNotNull(user);
    }

    @Test
    public void findUserByIdAndName() {
        User user = userService.findUserByIdAndName(UUID.randomUUID().toString(), null);
        LOGGER.info("{}", user);
        Assert.assertNotNull(user);
    }

}