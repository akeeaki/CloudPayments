package ru.clouddonate.cloudpaymentslegacy.messengers.api;

public interface MessengerService {
    public void connect() throws ConnectException;

    public void disconnect();

    public void sendMessage(String var1);
}
