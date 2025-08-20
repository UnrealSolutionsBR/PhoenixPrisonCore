package com.phoenixpickaxes.managers;

import com.phoenixpickaxes.PhoenixPickaxes;
import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

public class BlockValueManager {

    private static final Map<Material, Double> values = new HashMap<>();
    private static File file;
    private static FileConfiguration config;

    /**
     * Cargar valores desde blocks.yml
     */
    public static void loadValues() {
        file = new File(PhoenixPickaxes.getInstance().getDataFolder(), "blocks.yml");

        if (!file.exists()) {
            PhoenixPickaxes.getInstance().saveResource("blocks.yml", false);
        }

        config = YamlConfiguration.loadConfiguration(file);
        values.clear();

        if (config.isConfigurationSection("blocks")) {
            for (String key : config.getConfigurationSection("blocks").getKeys(false)) {
                Material mat = Material.matchMaterial(key.toUpperCase());
                double value = config.getDouble("blocks." + key, 1.0);

                if (mat != null) {
                    values.put(mat, value);
                } else {
                    PhoenixPickaxes.getInstance().getLogger().warning("Bloque inválido en blocks.yml: " + key);
                }
            }
        }

        PhoenixPickaxes.getInstance().getLogger().info("Se cargaron " + values.size() + " bloques desde blocks.yml");
    }

    /**
     * Obtener el valor de un bloque
     */
    public static double getValue(Material material) {
        return values.getOrDefault(material, 1.0); // Si no está configurado → vale 1
    }
}
