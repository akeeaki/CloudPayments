package ru.clouddonate.cloudpaymentslegacy.announcements;

import lombok.Generated;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.ComponentBuilder;
import org.bukkit.Bukkit;
import org.bukkit.Sound;
import ru.clouddonate.cloudpaymentslegacy.CloudPayments;
import ru.clouddonate.cloudpaymentslegacy.config.Config;
import ru.clouddonate.cloudpaymentslegacy.http.GetResult;

import java.util.HashMap;

public final class Announcement {
    private final String productName;
    private final HashMap<String, AnnounceEnum> actions = new HashMap();

    public Announcement(String productName) {
        this.productName = productName;
    }

    public void process(GetResult result) {
        this.getActions().forEach((action, announceEnum) -> {
            switch (announceEnum) {
                case CHAT_MESSAGE: {
                    Bukkit.broadcastMessage(this.format((String)action, result));
                    break;
                }
                case TITLE_MESSAGE: {
                    String title = this.format(action.split("::")[0], result);
                    String subtitle = this.format(action.split("::")[1], result);
                    Bukkit.getOnlinePlayers().forEach(player -> player.sendTitle(title, subtitle, 20, 20, 20));
                    break;
                }
                case ACTIONBAR_MESSAGE: {
                    Bukkit.getOnlinePlayers().forEach(player -> player.spigot().sendMessage(ChatMessageType.ACTION_BAR, new ComponentBuilder(this.format((String)action, result)).create()));
                    break;
                }
                case SOUND: {
                    Sound sound = Sound.valueOf(this.format((String)action, result));
                    Bukkit.getOnlinePlayers().forEach(player -> {
                        if (player.getLocation().getWorld() != null) {
                            player.getLocation().getWorld().playSound(player.getLocation(), sound, 1.0f, 1.0f);
                        }
                    });
                    break;
                }
                case COMMAND: {
                    Bukkit.getScheduler().runTask(CloudPayments.getPlugin(CloudPayments.class), () -> Bukkit.dispatchCommand(Bukkit.getConsoleSender(), this.format((String)action, result)));
                }
            }
        });
    }

    private String format(String msg, GetResult result) {
        return Config.format(msg).replaceAll("<nickname>", result.getNickname()).replaceAll("<price>", String.valueOf(result.getPrice())).replaceAll("<product>", result.getName()).replaceAll("<count>", String.valueOf(result.getAmount()));
    }

    @Generated
    public String getProductName() {
        return this.productName;
    }

    @Generated
    public HashMap<String, AnnounceEnum> getActions() {
        return this.actions;
    }
}
