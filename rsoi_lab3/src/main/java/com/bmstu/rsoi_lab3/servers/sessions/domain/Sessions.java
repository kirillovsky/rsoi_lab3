package com.bmstu.rsoi_lab3.servers.sessions.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.io.Serializable;

/**
 * Created by Александр on 13.02.2016.
 */

@Entity
public class Sessions implements Serializable {

    @Id
    @GeneratedValue
    private Long sessionId;

    @Column(nullable = false, unique = true)
    private Long userId;

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
