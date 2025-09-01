package ru.clouddonate.cloudpaymentslegacy.messengers;

import lombok.Generated;
import ru.clouddonate.cloudpaymentslegacy.CloudPayments;
import ru.clouddonate.cloudpaymentslegacy.api.Manager;
import ru.clouddonate.cloudpaymentslegacy.config.Config;
import ru.clouddonate.cloudpaymentslegacy.messengers.api.ConnectException;
import ru.clouddonate.cloudpaymentslegacy.messengers.api.MessengerService;
import ru.clouddonate.cloudpaymentslegacy.messengers.list.TelegramMessenger;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.logging.Level;

public final class MessengersManager
        extends Manager {
    private final List<MessengerService> connectedMessengers = new ArrayList<MessengerService>();

    public MessengersManager(CloudPayments cloudPayments) {
        super(cloudPayments);
        this.reload();
    }

    private void register(MessengerService ... messengers) {
        Arrays.stream(messengers).forEach(messenger -> {
            try {
                this.getCloudPayments().getLogger().log(Level.INFO, "Registering Messenger " + messenger.getClass().getSimpleName());
                this.connectedMessengers.add((MessengerService)messenger);
                messenger.connect();
                this.getCloudPayments().getLogger().log(Level.INFO, "Messenger " + messenger.getClass().getSimpleName() + " connected");
            }
            catch (ConnectException e) {
                this.getCloudPayments().getLogger().log(Level.SEVERE, "Вызвано исключение! Не удалось подключить мессенджер: " + messenger + ", ошибка: " + e.getMessage());
            }
        });
    }

    public void reload() {
        if (!this.getConnectedMessengers().isEmpty()) {
            this.connectedMessengers.forEach(MessengerService::disconnect);
        }
        this.connectedMessengers.clear();
        if (Config.Messengers.Telegram.enabled) {
            this.register(new TelegramMessenger(Config.Messengers.Telegram.apiToken, Config.Messengers.Telegram.ids));
        }
    }

    @Generated
    public List<MessengerService> getConnectedMessengers() {
        return this.connectedMessengers;
    }
}
