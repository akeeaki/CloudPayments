package ru.clouddonate.cloudpaymentslegacy.messengers.api;

public class ConnectException extends RuntimeException {
    public ConnectException(String message) {
        super(message);
    }
}
