package com.phoenixpickaxes;

import com.phoenixpickaxes.commands.PickaxeCommand;
import com.phoenixpickaxes.commands.PhoenixCommand;
import com.phoenixpickaxes.commands.PhoenixTabCompleter;
import com.phoenixpickaxes.listeners.BlockBreakListener;
import com.phoenixpickaxes.managers.BlockValueManager;
import com.phoenixpickaxes.listeners.DropListener;
import com.phoenixpickaxes.managers.SkinManager;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;

public class PhoenixPickaxes extends JavaPlugin {

    private static PhoenixPickaxes instance;

    private FileConfiguration skinsConfig;
    private File skinsFile;

    @Override
    public void onEnable() {
        instance = this;

        // Guardar config.yml por defecto
        saveDefaultConfig();

        // Registrar comando y tab completer
        getCommand("phoenixpickaxes").setExecutor(new PhoenixCommand());
        getCommand("phoenixpickaxes").setTabCompleter(new PhoenixTabCompleter());

        // Registrar listeners
        getServer().getPluginManager().registerEvents(new BlockBreakListener(), this);
        getServer().getPluginManager().registerEvents(new DropListener(), this);

        // Cargar skins.yml
        loadSkinsConfig();
        SkinManager.loadSkins();
        BlockValueManager.loadValues();

        // Registrar comandos
        getCommand("pickaxe").setExecutor(new PickaxeCommand());
        getCommand("phoenixpickaxes").setExecutor(new PhoenixCommand());

        getLogger().info("§aPhoenixPickaxes habilitado correctamente.");
    }

    @Override
    public void onDisable() {
        getLogger().info("§cPhoenixPickaxes deshabilitado.");
    }

    public static PhoenixPickaxes getInstance() {
        return instance;
    }

    // -------------------------
    //  Configuración de skins.yml
    // -------------------------
    public void loadSkinsConfig() {
        skinsFile = new File(getDataFolder(), "skins.yml");
        if (!skinsFile.exists()) {
            saveResource("skins.yml", false);
        }
        skinsConfig = YamlConfiguration.loadConfiguration(skinsFile);
    }

    public FileConfiguration getSkinsConfig() {
        if (skinsConfig == null) {
            loadSkinsConfig();
        }
        return skinsConfig;
    }

    public void reloadSkinsConfig() {
        skinsConfig = null;
        loadSkinsConfig();
        SkinManager.loadSkins();
    }
}
