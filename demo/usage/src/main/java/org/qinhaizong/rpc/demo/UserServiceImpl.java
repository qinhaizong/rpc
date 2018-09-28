package org.qinhaizong.rpc.demo;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Collections;
import java.util.List;
import java.util.UUID;

/**
 * @author haizongqin
 */
public class UserServiceImpl implements UserService {

    private static final Logger LOGGER = LoggerFactory.getLogger(UserServiceImpl.class);

    @Override
    public void save(User user) {
        LOGGER.info("user: {}", user);
    }

    @Override
    public List<User> findAll() {
        return Collections.singletonList(new User());
    }

    @Override
    public User findUserById(String id) {
        User user = new User();
        user.setId(UUID.randomUUID().toString());
        return user;
    }

    @Override
    public User findUserByIdAndName(String id, String name) {
        return findUserById(id);
    }

}
