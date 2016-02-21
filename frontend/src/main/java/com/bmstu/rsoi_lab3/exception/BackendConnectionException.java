package com.bmstu.rsoi_lab3.exception;

/**
 * Created by Александр on 19.02.2016.
 */
public class BackendConnectionException extends RuntimeException {
    public BackendConnectionException(String backendName) {
        super("Backend " + backendName + " did not work normally");
    }
}
