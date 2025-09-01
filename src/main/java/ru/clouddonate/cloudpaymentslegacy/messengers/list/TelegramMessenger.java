package ru.clouddonate.cloudpaymentslegacy.messengers.list;

import lombok.Generated;
import ru.clouddonate.cloudpaymentslegacy.messengers.api.ConnectException;
import ru.clouddonate.cloudpaymentslegacy.messengers.api.MessengerService;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

public class TelegramMessenger implements MessengerService {
    private final String apiToken;
    private final List<String> ids = new ArrayList<String>();

    public TelegramMessenger(String apiToken, List<String> ids) {
        this.apiToken = apiToken;
        this.ids.addAll(ids);
    }

    @Override
    public void connect() throws ConnectException {
        try {
            URL url = new URL("https://api.telegram.org/bot" + this.apiToken + "/getMe");
            HttpURLConnection connection = (HttpURLConnection)url.openConnection();
            connection.setRequestMethod("GET");
            connection.setConnectTimeout(5000);
            connection.setReadTimeout(5000);
            int responseCode = connection.getResponseCode();
            if (responseCode != 200) {
                throw new ConnectException("Invalid bot token or unable to connect to Telegram API. Response code: " + responseCode);
            }
            StringBuilder response = new StringBuilder();
            try (BufferedReader br = new BufferedReader(new InputStreamReader(connection.getInputStream(), StandardCharsets.UTF_8));){
                String line;
                while ((line = br.readLine()) != null) {
                    response.append(line);
                }
            }
            if (!response.toString().contains("\"ok\":true")) {
                throw new ConnectException("Telegram API response indicates failure: " + response);
            }
            connection.disconnect();
        }
        catch (IOException e) {
            throw new ConnectException("Failed to connect to Telegram API: " + e.getMessage());
        }
    }

    @Override
    public void disconnect() {
    }

    @Override
    public void sendMessage(String message) {
        this.ids.forEach(id -> {
            try {
                URL url = new URL("https://api.telegram.org/bot" + this.getApiToken() + "/sendMessage");
                HttpURLConnection connection = TelegramMessenger.getHttpURLConnection(message, id, url);
                int responseCode = connection.getResponseCode();
                if (responseCode != 200) {
                    System.out.println("Failed to send message to Telegram. Response Code: " + responseCode);
                }
                connection.disconnect();
            }
            catch (Exception e) {
                e.printStackTrace();
            }
        });
    }

    private static HttpURLConnection getHttpURLConnection(String message, String id, URL url) throws IOException {
        HttpURLConnection connection = (HttpURLConnection)url.openConnection();
        connection.setRequestMethod("POST");
        connection.setRequestProperty("Content-Type", "application/json");
        connection.setDoOutput(true);
        String payload = "{\"chat_id\":\"" + id + "\",\"text\":\"" + message + "\"}";
        try (OutputStream os = connection.getOutputStream();){
            byte[] input = payload.getBytes(StandardCharsets.UTF_8);
            os.write(input, 0, input.length);
        }
        return connection;
    }

    @Generated
    public String getApiToken() {
        return this.apiToken;
    }

    @Generated
    public List<String> getIds() {
        return this.ids;
    }
}
