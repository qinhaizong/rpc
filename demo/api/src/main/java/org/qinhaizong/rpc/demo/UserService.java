package org.qinhaizong.rpc.demo;

import java.util.List;

public interface UserService {

    void save(User user);

    List<User> findAll();

    User findUserById(String id);

    User findUserByIdAndName(String id, String name);

}
