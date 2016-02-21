package com.bmstu.rsoi_lab3.models;

import com.bmstu.rsoi_lab3.markers.SessionBackend;
import com.bmstu.rsoi_lab3.markers.UserBackend;
import org.hibernate.validator.constraints.NotEmpty;

import java.io.Serializable;

/**
 * Created by Александр on 19.02.2016.
 */
public class LoginEntity implements Serializable, UserBackend, SessionBackend {

    @NotEmpty(message = "Login is required.")
    private String login;

    @NotEmpty(message = "Password is required.")
    private String password;

    public LoginEntity() {
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getLogin() {
        return login;
    }

    public String getPassword() {
        return password;
    }
}

