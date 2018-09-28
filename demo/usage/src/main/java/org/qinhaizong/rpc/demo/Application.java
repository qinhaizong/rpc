package org.qinhaizong.rpc.demo;

import org.qinhaizong.rpc.core.annotation.RpcProvider;
import org.qinhaizong.rpc.core.servlet.RpcServlet;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.context.annotation.Bean;

/**
 * @author haizongqin
 */
@SpringBootApplication
public class Application {

    @RpcProvider
    @Bean
    public UserService userService() {
        return new UserServiceImpl();
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
