package com.bmstu.rsoi_lab3.models;

import com.bmstu.rsoi_lab3.markers.SessionBackend;

import java.io.Serializable;
import java.sql.Date;

/**
 * Created by Александр on 13.02.2016.
 */

public class Sessions implements Serializable, SessionBackend {

    static final long SESSION_LIVE_TIME = 2*60*1000L;
    private Long sessionId;
    private Long userId;
    private Long expired_time;

    protected Sessions() {
    }


    public Sessions(Long sessionId, Long userId, Long expired_time) {
        super();
        this.sessionId = sessionId;
        this.userId = userId;
        this.expired_time = expired_time;
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

    @Override
    public String toString() {
        return "Sessions{" +
                "sessionId=" + sessionId +
                ", userId=" + userId +
                '}';
    }
}
