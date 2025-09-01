package ru.clouddonate.cloudpaymentslegacy.config;

import lombok.Generated;
import org.bukkit.ChatColor;
import org.bukkit.configuration.file.FileConfiguration;
import ru.clouddonate.cloudpaymentslegacy.CloudPayments;

import java.util.List;

public final class Config {
    public static String format(String string) {
        return ChatColor.translateAlternateColorCodes('&', string);
    }

    public static void load(CloudPayments cloudPayments) {
        FileConfiguration config = cloudPayments.getConfig();
        Messages.noPermission = Config.format(cloudPayments.getConfig().getString("messages.noPermission"));
        Messages.reload = Config.format(cloudPayments.getConfig().getString("messages.reload"));
        Messages.debugDisabled = Config.format(cloudPayments.getConfig().getString("messages.debug-disabled"));
        Messages.debugEnabled = Config.format(cloudPayments.getConfig().getString("messages.debug-enabled"));
        Settings.debug = config.getBoolean("settings.debug-mode");
        Settings.checkUpdates = config.getBoolean("settings.check-updates");
        Settings.requestDelay = config.getLong("settings.request-delay");
        LocalStorage.Payments.enabled = config.getBoolean("local-storage.payments.enabled");
        LocalStorage.Payments.format = config.getString("local-storage.payments.format");
        LocalStorage.Statistic.enabled = config.getBoolean("local-storage.statistic.enabled");
        Settings.Shop.shopId = config.getString("settings.shop.shop-id");
        Settings.Shop.shopKey = config.getString("settings.shop.shop-key");
        Settings.Shop.serverId = config.getString("settings.shop.server-id");
        Messengers.Telegram.enabled = config.getBoolean("messengers.telegram.enabled");
        Messengers.Telegram.apiToken = config.getString("messengers.telegram.api-token");
        Messengers.Telegram.ids = config.getStringList("messengers.telegram.ids");
    }

    @Generated
    private Config() {
        throw new UnsupportedOperationException("This is a utility class and cannot be instantiated");
    }

    public static class Messengers {

        public static class Telegram {
            public static boolean enabled;
            public static List<String> ids;
            public static String apiToken;
        }
    }

    public static class Settings {
        public static boolean debug, checkUpdates;
        public static long requestDelay;

        public static class Shop {
            public static String shopId;
            public static String shopKey;
            public static String serverId;
        }
    }

    public static class LocalStorage {

        public static class Statistic {
            public static boolean enabled;
        }

        public static class Payments {
            public static boolean enabled;
            public static String format;
        }
    }

    public static class Messages {
        public static String noPermission;
        public static String reload;
        public static String debugDisabled;
        public static String debugEnabled;
    }
}
