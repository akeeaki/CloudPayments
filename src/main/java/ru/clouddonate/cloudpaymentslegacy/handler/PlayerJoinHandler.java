package ru.clouddonate.cloudpaymentslegacy.handler;

import lombok.Getter;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import ru.clouddonate.cloudpaymentslegacy.CloudPayments;
import ru.clouddonate.cloudpaymentslegacy.config.Config;

@Getter
public final class PlayerJoinHandler implements Listener {
    private final CloudPayments plugin;

    public PlayerJoinHandler(final CloudPayments plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onJoin(final PlayerJoinEvent event) {
        if (!Config.Settings.checkUpdates) return;

        final boolean isAdmin = event.getPlayer().isOp() || event.getPlayer().hasPermission("*");

        if (isAdmin) {
            this.getPlugin().getServer().getScheduler().runTaskLater(this.getPlugin(), () -> {
                if (this.getPlugin().isUpdateFound()) {
                    final Player player = event.getPlayer();
                    player.sendMessage(format("&9Cloud&bPayments &8- &fВаша версия плагина отличается от новой!"));
                    player.sendMessage(format("&9Cloud&bPayments &8- &fВаша версия: &9" + this.getPlugin().getVersion() + "&f, новая: &b" + this.getPlugin().getLatestVersion()));
                    player.sendMessage(format("&9Cloud&bPayments &8- &fОбновитесь на сайте: &bhttps://dashboard.cdonate.ru/shops/" + Config.Settings.Shop.shopId + "/settings"));
                }
            }, 100L);
        }
    }

    private String format(String input) {
        return ChatColor.translateAlternateColorCodes('&', input);
    }
}
