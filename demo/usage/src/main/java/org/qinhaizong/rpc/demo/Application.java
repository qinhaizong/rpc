package org.qinhaizong.rpc.demo;

import org.qinhaizong.rpc.core.annotation.RpcProvider;
import org.qinhaizong.rpc.core.servlet.RpcServlet;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.context.annotation.Bean;

import java.util.Collections;
import java.util.List;
import java.util.UUID;

@SpringBootApplication
public class Application {

    @RpcProvider
    @Bean
    public UserService userService() {
        return new UserService() {

            @Override
            public void save(User user) {
                System.out.println("**************");
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
        };
    }

    @Bean
    public ServletRegistrationBean servletRegistrationBean() {
        ServletRegistrationBean bean = new ServletRegistrationBean(new RpcServlet(), "/org.qinhaizong.rpc.demo.UserService/*");
        bean.addInitParameter("beanNames", "userService");
        bean.addInitParameter("interfaces", "org.qinhaizong.rpc.demo.UserService");
        return bean;
    }

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }

}
