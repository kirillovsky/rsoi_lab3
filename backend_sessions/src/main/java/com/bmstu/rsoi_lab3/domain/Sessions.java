package com.bmstu.rsoi_lab3.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.io.Serializable;
import java.sql.Date;

/**
 * Created by Александр on 13.02.2016.
 */

@Entity
public class Sessions implements Serializable {

    static final long SESSION_LIVE_TIME = 2*60*1000L;
    @Id
    @GeneratedValue
    private Long sessionId;

    @Column(nullable = false, unique = true)
    private Long userId;

    @Column(nullable = false)
    private Long expired_time;

    protected Sessions() {
    }

    public Sessions(Long userId) {
        super();
        this.userId = userId;
        this.expired_time = System.currentTimeMillis() + SESSION_LIVE_TIME;
    }

    public Sessions(Long sessionId, Long userId) {
        super();
        this.sessionId = sessionId;
        this.userId = userId;
        this.expired_time = System.currentTimeMillis() + SESSION_LIVE_TIME;
    }

    public Long getExpiredTime() {
        return expired_time;
    }

    public Long getSessionId() {
        return sessionId;
    }

    public Long getUserId() {
        return userId;
    }

    public void refreshExpiredTime(){
        this.expired_time = System.currentTimeMillis() + SESSION_LIVE_TIME;
    }

    @Override
    public String toString() {
        return "Sessions{" +
                "sessionId=" + sessionId +
                ", userId=" + userId +
                '}';
    }
}
