package me.asleepp.skriptnexo.config;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import me.asleepp.skriptnexo.SkriptNexo;

import java.io.File;
import java.io.IOException;

public class SkriptNexoConfig {
    private final SkriptNexo plugin;
    private File configFile;
    private FileConfiguration config;

    public SkriptNexoConfig(SkriptNexo plugin) {
        this.plugin = plugin;
        setupConfig();
    }

    private void setupConfig() {
        configFile = new File(plugin.getDataFolder(), "config.yml");
        if (!configFile.exists()) {
            plugin.saveResource("config.yml", false);
        }
        config = YamlConfiguration.loadConfiguration(configFile);
        setDefaults();
    }

    private void setDefaults() {
        // Furniture events
        setEventDefaults("furniture.interact");
        setEventDefaults("furniture.break");

        // StringBlock events
        setEventDefaults("stringblock.break");
        setEventDefaults("stringblock.interact");
        setEventDefaults("stringblock.place");

        // NoteBlock events
        setEventDefaults("noteblock.break");
        setEventDefaults("noteblock.interact");
        setEventDefaults("noteblock.place");

        // Mechanic events
        setEventDefaults("mechanic.interact");

        config.addDefault("settings.debug-mode", false);
        config.addDefault("settings.cache-size", 100);

        config.options().copyDefaults(true);
        saveConfig();
    }

    private void setEventDefaults(String path) {
        config.addDefault("events." + path + ".enabled", true);
        config.addDefault("events." + path + ".cooldown", 1);
    }

    public boolean isEventEnabled(String eventType, String action) {
        return config.getBoolean("events." + eventType + "." + action + ".enabled", true);
    }

    public int getEventCooldown(String eventType, String action) {
        return config.getInt("events." + eventType + "." + action + ".cooldown", 1);
    }

    public void saveConfig() {
        try {
            config.save(configFile);
        } catch (IOException e) {
            plugin.getLogger().severe("Could not save config.yml!");
        }
    }

    public void reloadConfig() {
        config = YamlConfiguration.loadConfiguration(configFile);
    }

    public FileConfiguration getConfig() {
        return config;
    }
}
