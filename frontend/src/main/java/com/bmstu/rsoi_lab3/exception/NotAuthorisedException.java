package com.bmstu.rsoi_lab3.exception;

/**
 * Created by Александр on 19.02.2016.
 */
public class NotAuthorisedException extends RuntimeException {
    public NotAuthorisedException() {
        super("Operation is denined. User not authorised");
    }
}
