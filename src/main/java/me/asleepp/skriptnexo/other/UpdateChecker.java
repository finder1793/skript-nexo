package me.asleepp.skriptnexo.other;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import me.asleepp.skriptnexo.SkriptNexo;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.concurrent.CompletableFuture;

public class UpdateChecker implements Listener {

    private final SkriptNexo plugin;
    private final String currentVersion;
    private String latestVersion;

    public UpdateChecker(SkriptNexo plugin) {
        this.plugin = plugin;
        this.currentVersion = plugin.getDescription().getVersion();
        Bukkit.getPluginManager().registerEvents(this, plugin);
        checkForUpdate();
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        if (player.hasPermission("skript-nexo.update.check") && latestVersion != null) {
            if (!currentVersion.equals(latestVersion)) {
                player.sendMessage(" ");
                player.sendRichMessage("<dark_blue>[<blue>skript-nexo<dark_blue>] <white>skript-nexo is <red><bold>OUTDATED</bold><white>!");
                player.sendRichMessage("<dark_blue>[<blue>skript-nexo<dark_blue>] <white>New version: <gold>" + latestVersion);
                player.sendRichMessage("<dark_blue>[<blue>skript-nexo<dark_blue>] <white>Download <gold><click:open_url:https://github.com/finder1793/skript-oraxen><hover:show_text:'<red>Click here to get the latest version!'>here<white>!</click>");
                player.sendMessage(" ");
            }
        }
    }

    private void checkForUpdate() {
        HttpClient httpClient = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("https://api.github.com/repos/finder1793/skript-oraxen/releases/latest"))
                .build();

        CompletableFuture<HttpResponse<String>> responseFuture = httpClient.sendAsync(request, HttpResponse.BodyHandlers.ofString());

        responseFuture.thenApply(HttpResponse::body)
                .thenAccept(body -> {
                    JsonObject jsonResponse = new Gson().fromJson(body, JsonObject.class);
                    if (jsonResponse != null && jsonResponse.has("tag_name")) {
                        latestVersion = jsonResponse.get("tag_name").getAsString();

                        if (!currentVersion.equals(latestVersion)) {
                            SkriptNexo.getInstance().getLogger().warning("An update for skript-nexo is available: " + latestVersion + " (current version: " + currentVersion + ")");
                        } else {
                            SkriptNexo.getInstance().getLogger().info("skript-nexo is up to date!");
                        }
                    } else {
                        SkriptNexo.getInstance().getLogger().severe("Failed to check for updates: Unexpected JSON format.");
                    }
                })
                .exceptionally(e -> {
                    SkriptNexo.getInstance().getLogger().severe("Failed to check for updates: " + e.getMessage());
                    return null;
                });
    }
}