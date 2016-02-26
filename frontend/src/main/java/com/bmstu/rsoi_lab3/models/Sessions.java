package com.bmstu.rsoi_lab3.models;

import com.bmstu.rsoi_lab3.markers.SessionBackend;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.hibernate.validator.constraints.NotEmpty;

import java.io.Serializable;

/**
 * Created by Александр on 13.02.2016.
 */

@JsonIgnoreProperties(ignoreUnknown = true)
public class Sessions implements Serializable, SessionBackend {

    private Long sessionId;

    @NotEmpty(message = "Login is required.")
    private String login;

    @NotEmpty(message = "Password is required.")
    private String password;


    public Sessions() {
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Long getSessionId() {
        return sessionId;
    }

    public void setSessionId(Long sessionId) {
        this.sessionId = sessionId;
    }

    @Override
    public String toString() {
        return "Sessions{" +
                "login='" + login + '\'' +
                ", sessionId=" + sessionId +
                ", password='" + password + '\'' +
                '}';
    }
}
