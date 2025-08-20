package com.phoenixpickaxes.managers;

import com.phoenixpickaxes.PhoenixPickaxes;
import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.util.*;

public class SkinManager {

    private static final Map<String, SkinData> skins = new HashMap<>();
    private static File file;
    private static FileConfiguration config;

    /**
     * Cargar skins desde skins.yml
     */
    public static void loadSkins() {
        file = new File(PhoenixPickaxes.getInstance().getDataFolder(), "skins.yml");

        if (!file.exists()) {
            PhoenixPickaxes.getInstance().saveResource("skins.yml", false);
        }

        config = YamlConfiguration.loadConfiguration(file);
        skins.clear();

        if (config.isConfigurationSection("skins")) {
            for (String key : config.getConfigurationSection("skins").getKeys(false)) {
                String display = config.getString("skins." + key + ".display", key);
                int bonus = config.getInt("skins." + key + ".bonus", 0);
                String materialStr = config.getString("skins." + key + ".material", "WOODEN_PICKAXE");

                Material material;
                try {
                    material = Material.matchMaterial(materialStr.toUpperCase());
                } catch (Exception e) {
                    material = Material.WOODEN_PICKAXE;
                }

                skins.put(key, new SkinData(key, display, bonus, material));
            }
        }

        PhoenixPickaxes.getInstance().getLogger().info("Se cargaron " + skins.size() + " skins desde skins.yml");
    }

    /**
     * Obtener skin por id
     */
    public static SkinData getSkin(String id) {
        return skins.getOrDefault(id, new SkinData("wooden", "madera", 0, Material.WOODEN_PICKAXE));
    }

    /**
     * Obtener todas las IDs de skins (para TabCompleter)
     */
    public static List<String> getAllSkinIds() {
        return new ArrayList<>(skins.keySet());
    }

    /**
     * Clase para datos de skin
     */
    public record SkinData(String id, String display, int bonus, Material material) {}
}
