package me.asleepp.skriptnexo;

import java.io.IOException;


import ch.njol.skript.bstats.bukkit.Metrics;
import ch.njol.skript.util.Version;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import ch.njol.skript.Skript;
import ch.njol.skript.SkriptAddon;

import javax.annotation.Nullable;

public class SkriptNexo extends JavaPlugin {

    private static SkriptAddon addon;

    private static SkriptNexo instance;

    @Nullable
    public static SkriptNexo getInstance() {
        return instance;
    }
    @Nullable
    public static SkriptAddon getAddonInstance() {
        return addon;
    }

    @Override
    public void onEnable() {
        long start = System.currentTimeMillis();
        final PluginManager manager = this.getServer().getPluginManager();
        final Plugin skript = manager.getPlugin("Skript");
        if (skript == null || !skript.isEnabled()) {
            getLogger().severe("Could not find Skript! Disabling...");
            manager.disablePlugin(this);
            return;
        } else if (Skript.getVersion().compareTo(new Version(2, 7, 0)) < 0) {
            getLogger().warning("You are running an unsupported version of Skript. Disabling...");
            manager.disablePlugin(this);
            return;
        }

        if (!Skript.isAcceptRegistrations()) {
            getLogger().severe("The plugin can't load when it's already loaded! Disabling...");
            manager.disablePlugin(this);
            return;
        }

        final Plugin nexo = manager.getPlugin("Nexo");
        if (nexo == null || !nexo.isEnabled()) {
            getLogger().severe("Could not find Nexo! Disabling...");
            manager.disablePlugin(this);
            return;
        }
        int pluginId = 21274; // todo replace this with new bstats id
        Metrics metrics = new Metrics(this, pluginId);
        instance = this;
        addon = Skript.registerAddon(this);
        addon.setLanguageFileDirectory("lang");
        try {
            addon.loadClasses("me.asleepp.skriptnexo", "elements");
        } catch (IOException error) {
            error.printStackTrace();
            manager.disablePlugin(this);
            return;
        }
        long finish = System.currentTimeMillis() - start;
        getLogger().info("Succesfully loaded skript-nexo in " + finish + "ms!");

    }


}
