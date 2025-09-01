package ru.clouddonate.cloudpaymentslegacy.shop;

import lombok.Generated;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandException;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.scheduler.BukkitRunnable;
import ru.clouddonate.cloudpaymentslegacy.CloudPayments;
import ru.clouddonate.cloudpaymentslegacy.config.Config;
import ru.clouddonate.cloudpaymentslegacy.http.GetResult;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;

public final class Shop {
    private static final String GET_URL = "https://api.cdonate.ru/api/v1/shops/{shop_id}/purchases/pending?server_id={server_id}";
    private static final String POST_URL = "https://api.cdonate.ru/api/v1/shops/{shop_id}/purchases/{purchase_id}/approve";
    private final String shopId;
    private final String shopKey;
    private final String serverId;
    private final long requestDelay;
    private final CloudPayments plugin;
    private BukkitRunnable runnable;

    public Shop(final String shopId, final String shopKey, final String serverId, long requestDelay, final CloudPayments plugin) {
        this.shopId = shopId;
        this.plugin = plugin;
        this.shopKey = shopKey;
        this.serverId = serverId;
        this.requestDelay = requestDelay;
        this.runnable = new BukkitRunnable(){

            public void run() {
                try {
                    String getUrl = Shop.GET_URL.replace("{shop_id}", shopId).replace("{server_id}", serverId);
                    URL url = new URL(getUrl);
                    HttpURLConnection getConnection = (HttpURLConnection)url.openConnection();
                    getConnection.setRequestMethod("GET");
                    getConnection.setRequestProperty("X-Shop-Key", shopKey);
                    getConnection.setRequestProperty("Content-Type", "application/json");
                    getConnection.setConnectTimeout(5000);
                    getConnection.setReadTimeout(5000);
                    int getResponseCode = getConnection.getResponseCode();
                    if (getResponseCode == 200) {
                        StringBuilder response = new StringBuilder();
                        try (BufferedReader br = new BufferedReader(new InputStreamReader(getConnection.getInputStream(), StandardCharsets.UTF_8));){
                            String line;
                            while ((line = br.readLine()) != null) {
                                response.append(line);
                            }
                        }
                        GetResult[] getResults = (GetResult[])plugin.getConverterService().gson.fromJson(response.toString(), GetResult[].class);
                        if (getResults == null) {
                            if (Config.Settings.debug) {
                                plugin.getLogger().info("Not correct GET result format (null)");
                            }
                            return;
                        }
                        if (Config.Settings.debug) {
                            plugin.getLogger().info("GET return " + getResults.length + " results");
                        }
                        ArrayList<String> commands = new ArrayList<String>();
                        for (GetResult data : getResults) {
                            for (String command : data.getCommands()) {
                                commands.add(command.replaceAll("\\{user}", data.getNickname()).replaceAll("\\{amount}", String.valueOf(data.getAmount())));
                            }
                            HttpURLConnection postConnection = this.getHttpURLConnection(data);
                            int postResponseCode = postConnection.getResponseCode();
                            if (postResponseCode != 204 && Config.Settings.debug) {
                                plugin.getLogger().warning("Failed to approve purchase ID " + data.getId() + ". Response code: " + postResponseCode);
                            } else {
                                plugin.getMessengersManager().getConnectedMessengers().forEach(messenger -> messenger.sendMessage("✅ Пришёл платёж: ID " + data.getId() + "\n\n❓ Информация:\n👤 Никнейм: " + data.getNickname() + "\n🪪 Товар: " + data.getName() + " (кол-во: x" + data.getAmount() + ")\n🔥 Пришло с учётом комиссии сервиса: " + data.getPrice() + " рублей\n\n❤️ Благодарим за использование CloudDonate!"));
                                plugin.getAnnouncementsManager().process(data);
                                plugin.getLocalStorage().addPayment(data);
                            }
                            postConnection.disconnect();
                        }
                        if (!commands.isEmpty()) {
                            plugin.getServer().getScheduler().runTask(plugin, () -> {
                                ConsoleCommandSender sender = Bukkit.getConsoleSender();
                                for (String command : commands) {
                                    try {
                                        if (Config.Settings.debug) {
                                            plugin.getLogger().info("Executing command: " + command);
                                        }
                                        plugin.getServer().dispatchCommand((CommandSender)sender, command);
                                    }
                                    catch (CommandException e) {
                                        plugin.getLogger().warning("Failed to execute command: " + command);
                                        e.printStackTrace();
                                    }
                                }
                            });
                        }
                    } else {
                        plugin.getLogger().warning("Failed to fetch data. Response code: " + getResponseCode);
                    }
                    getConnection.disconnect();
                }
                catch (IOException e) {
                    plugin.getLogger().severe("[CloudPayments] Error fetching shop data: " + e.getMessage());
                }
            }

            private HttpURLConnection getHttpURLConnection(GetResult data) throws IOException {
                String postUrl = Shop.POST_URL.replace("{shop_id}", shopId).replace("{purchase_id}", String.valueOf(data.getId()));
                HttpURLConnection postConnection = (HttpURLConnection)new URL(postUrl).openConnection();
                postConnection.setRequestMethod("POST");
                postConnection.setRequestProperty("X-Shop-Key", shopKey);
                postConnection.setRequestProperty("Content-Type", "application/json");
                postConnection.setDoOutput(true);
                postConnection.setConnectTimeout(5000);
                postConnection.setReadTimeout(5000);
                try (OutputStream os = postConnection.getOutputStream();){
                    byte[] input = "{}".getBytes(StandardCharsets.UTF_8);
                    os.write(input, 0, input.length);
                }
                return postConnection;
            }
        };
        this.runnable.runTaskTimerAsynchronously(this.getPlugin(), this.getRequestDelay() * 20L, this.getRequestDelay() * 20L);
    }

    @Generated
    public String getShopId() {
        return this.shopId;
    }

    @Generated
    public String getShopKey() {
        return this.shopKey;
    }

    @Generated
    public String getServerId() {
        return this.serverId;
    }

    @Generated
    public long getRequestDelay() {
        return this.requestDelay;
    }

    @Generated
    public CloudPayments getPlugin() {
        return this.plugin;
    }

    @Generated
    public BukkitRunnable getRunnable() {
        return this.runnable;
    }

    @Generated
    public void setRunnable(BukkitRunnable runnable) {
        this.runnable = runnable;
    }
}
