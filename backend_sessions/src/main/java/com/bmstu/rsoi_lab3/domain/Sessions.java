package com.bmstu.rsoi_lab3.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.io.Serializable;

/**
 * Created by Александр on 13.02.2016.
 */

@Entity
@JsonIgnoreProperties(ignoreUnknown = true)
public class Sessions implements Serializable {

    static final long SESSION_LIVE_TIME = 2*60*1000L;
    @Id
    @GeneratedValue
    private Long sessionId;

    @Column(nullable = false, unique = true)
    private String login;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false)
    private Long expiredTime;

    public Sessions(String password, String login) {
        this.expiredTime = System.currentTimeMillis() + SESSION_LIVE_TIME;
        this.password = password;
        this.login = login;
    }

    public Sessions() {
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public Long getSessionId() {
        return sessionId;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Long getExpiredTime() {
        return expiredTime;
    }

    public void setExpiredTime(Long expiredTime) {
        this.expiredTime = expiredTime;
    }

    public void refreshExpiredTime(){
        this.expiredTime = SESSION_LIVE_TIME + System.currentTimeMillis();
    }
}
