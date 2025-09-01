package ru.clouddonate.cloudpaymentslegacy.command;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import ru.clouddonate.cloudpaymentslegacy.CloudPayments;
import ru.clouddonate.cloudpaymentslegacy.config.Config;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public final class CommandHandler
        implements CommandExecutor,
        TabCompleter {
    private final CloudPayments cloudPayments;

    public CommandHandler(CloudPayments cloudPayments) {
        this.cloudPayments = cloudPayments;
    }

    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings) {
        if (commandSender.hasPermission("cloudpayments.admin")) {
            if (strings.length == 0) {
                commandSender.sendMessage(Config.format("&9&lCloud&b&lPayments &8— &7Помощь"));
                commandSender.sendMessage(Config.format("&9/" + s + " reload &8- &7Перезагрузить плагин"));
                commandSender.sendMessage(Config.format("&9/" + s + " debug <on/off> &8- &7Вкл/выкл откладку"));
            } else if (strings.length == 1) {
                if (strings[0].equalsIgnoreCase("reload")) {
                    long start = System.currentTimeMillis();
                    this.cloudPayments.reloadConfig();
                    Config.load(this.cloudPayments);
                    this.cloudPayments.getMessengersManager().reload();
                    this.cloudPayments.getAnnouncementsManager().reload();
                    commandSender.sendMessage(Config.format(Config.Messages.reload.replaceAll("\\{took}", String.valueOf(System.currentTimeMillis() - start))));
                }
            } else if (strings.length == 2 && strings[0].equalsIgnoreCase("debug")) {
                if (strings[1].equalsIgnoreCase("on") || strings[1].equalsIgnoreCase("enable")) {
                    Config.Settings.debug = true;
                    this.cloudPayments.getConfig().set("settings.debug-mode", true);
                    this.cloudPayments.saveConfig();
                    commandSender.sendMessage(Config.format(Config.Messages.debugEnabled));
                } else {
                    Config.Settings.debug = false;
                    this.cloudPayments.getConfig().set("settings.debug-mode", false);
                    this.cloudPayments.saveConfig();
                    commandSender.sendMessage(Config.format(Config.Messages.debugDisabled));
                }
            }
        }
        return true;
    }

    public List<String> onTabComplete(CommandSender commandSender, Command command, String s, String[] strings) {
        switch (strings.length) {
            case 0: {
                return Arrays.asList("debug", "reload");
            }
        }
        return Collections.emptyList();
    }
}
