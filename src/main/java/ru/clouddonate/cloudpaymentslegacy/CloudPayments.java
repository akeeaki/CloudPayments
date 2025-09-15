/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  org.bukkit.command.ConsoleCommandSender
 *  org.bukkit.plugin.Plugin
 *  org.bukkit.plugin.java.JavaPlugin
 */
package ru.clouddonate.cloudpaymentslegacy;

import java.io.File;

import lombok.Generated;
import lombok.Getter;
import ru.clouddonate.cloudpaymentslegacy.metrics.Metrics;
import org.bukkit.plugin.java.JavaPlugin;
import ru.clouddonate.cloudpaymentslegacy.announcements.AnnouncementsManager;
import ru.clouddonate.cloudpaymentslegacy.command.CommandHandler;
import ru.clouddonate.cloudpaymentslegacy.config.Config;
import ru.clouddonate.cloudpaymentslegacy.json.JSONConverterService;
import ru.clouddonate.cloudpaymentslegacy.localstorage.LocalStorage;
import ru.clouddonate.cloudpaymentslegacy.messengers.MessengersManager;
import ru.clouddonate.cloudpaymentslegacy.shop.Shop;

public final class CloudPayments extends JavaPlugin {
    private MessengersManager messengersManager;
    private AnnouncementsManager announcementsManager;
    private Shop shop;
    private JSONConverterService converterService;
    private LocalStorage localStorage;
    private Metrics metrics;

    @Getter
    private final String version = "1.0.2.1";

    public void onEnable() {
        this.saveDefaultConfig();
        this.converterService = new JSONConverterService();
        Config.load(this);

        this.messengersManager = new MessengersManager(this);
        this.announcementsManager = new AnnouncementsManager(this);
        this.localStorage = new LocalStorage(this);
        File dir = new File(this.getDataFolder() + "/local/");
        if (!dir.exists()) {
            dir.mkdir();
        }

        this.shop = new Shop(ru.clouddonate.cloudpaymentslegacy.config.Config.Settings.Shop.shopId, ru.clouddonate.cloudpaymentslegacy.config.Config.Settings.Shop.shopKey, ru.clouddonate.cloudpaymentslegacy.config.Config.Settings.Shop.serverId, Config.Settings.requestDelay, this);
        this.getCommand("cloudpayments").setExecutor(new CommandHandler(this));

        this.metrics = new Metrics(this, 27282);
    }

    public void onDisable() {
    }

    @Generated
    public MessengersManager getMessengersManager() {
        return this.messengersManager;
    }

    @Generated
    public AnnouncementsManager getAnnouncementsManager() {
        return this.announcementsManager;
    }

    @Generated
    public Shop getShop() {
        return this.shop;
    }

    @Generated
    public JSONConverterService getConverterService() {
        return this.converterService;
    }

    @Generated
    public LocalStorage getLocalStorage() {
        return this.localStorage;
    }

    @Generated
    public void setMessengersManager(MessengersManager messengersManager) {
        this.messengersManager = messengersManager;
    }

    @Generated
    public void setAnnouncementsManager(AnnouncementsManager announcementsManager) {
        this.announcementsManager = announcementsManager;
    }

    @Generated
    public void setShop(Shop shop) {
        this.shop = shop;
    }

    @Generated
    public void setConverterService(JSONConverterService converterService) {
        this.converterService = converterService;
    }

    @Generated
    public void setLocalStorage(LocalStorage localStorage) {
        this.localStorage = localStorage;
    }
}
