package com.bmstu.rsoi_lab3.exception;

import org.springframework.http.HttpStatus;

/**
 * Created by Александр on 19.02.2016.
 */
public class BackendNotException extends RuntimeException {
    public BackendNotException(String backendName, HttpStatus status) {
        super("Backend " + backendName + " returned " + status.getReasonPhrase());
    }
}
