package com.phoenixpickaxes.managers;

import com.phoenixpickaxes.PhoenixPickaxes;
import com.phoenixpickaxes.utils.FormatUtils;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;

import java.util.ArrayList;
import java.util.List;

public class PickaxeManager {

    private static final NamespacedKey CUSTOM_PICKAXE_KEY =
            new NamespacedKey(PhoenixPickaxes.getInstance(), "custom_pickaxe");

    private static final NamespacedKey BLOCKS_BROKEN_KEY =
            new NamespacedKey(PhoenixPickaxes.getInstance(), "blocks_broken");

    private static final NamespacedKey SKIN_KEY =
            new NamespacedKey(PhoenixPickaxes.getInstance(), "skin");

    private static final NamespacedKey XP_KEY =
            new NamespacedKey(PhoenixPickaxes.getInstance(), "xp_total");

    private static final NamespacedKey LEVEL_KEY =
            new NamespacedKey(PhoenixPickaxes.getInstance(), "pickaxe_level");


    /**
     * Crea o actualiza un pico personalizado
     */
    public static ItemStack createPickaxe(Player player, int blocksBroken, int level, double xp, int fortune, int explosive, String skinId) {
        SkinManager.SkinData skinData = SkinManager.getSkin(skinId);

        // Crear el item según la skin
        ItemStack pickaxe = new ItemStack(skinData.material());
        ItemMeta meta = pickaxe.getItemMeta();
        if (meta == null) return pickaxe;

        // Nombre con bloques rotos (siempre +1 en BlockBreakListener)
        String formattedBlocks = FormatUtils.formatNumber(blocksBroken);
        meta.setDisplayName("§bPico §7[" + formattedBlocks + "]");

        // Lore
        List<String> lore = new ArrayList<>();
        lore.add("§7Eficiencia ∞");
        lore.add("§7Irrompible ∞");
        lore.add("§7Fortuna " + fortune);
        lore.add("§7Explosivo " + explosive);
        lore.add("§7");
        lore.add("§fNivel: " + level);

        // Progress bar → XP
        int needed = PhoenixPickaxes.getInstance().getConfig().getInt("blocks-per-level", 100) * level;
        String progressBar = FormatUtils.getProgressBar((int) xp, needed, 10, "§a▮", "§8▮");
        int percent = (int) ((xp / (double) needed) * 100);

        lore.add("§8" + progressBar + " (" + percent + "%)");
        lore.add("§7");

        // Skin
        lore.add("§fSkin de " + skinData.display());
        lore.add("§7Bonus de x" + skinData.bonus());

        meta.setLore(lore);

        // Datos persistentes
        meta.getPersistentDataContainer().set(CUSTOM_PICKAXE_KEY, PersistentDataType.BYTE, (byte) 1);
        meta.getPersistentDataContainer().set(BLOCKS_BROKEN_KEY, PersistentDataType.INTEGER, blocksBroken);
        meta.getPersistentDataContainer().set(SKIN_KEY, PersistentDataType.STRING, skinId);
        meta.getPersistentDataContainer().set(XP_KEY, PersistentDataType.DOUBLE, xp);
        meta.getPersistentDataContainer().set(LEVEL_KEY, PersistentDataType.INTEGER, level);

        // Hacerlo irrompible sin mostrar
        meta.setUnbreakable(true);
        meta.addItemFlags(ItemFlag.HIDE_UNBREAKABLE, ItemFlag.HIDE_ENCHANTS);

        pickaxe.setItemMeta(meta);
        return pickaxe;
    }

    // ───── Métodos auxiliares ─────

    public static boolean isCustomPickaxe(ItemStack item) {
        if (item == null || !item.hasItemMeta()) return false;
        ItemMeta meta = item.getItemMeta();
        return meta.getPersistentDataContainer().has(CUSTOM_PICKAXE_KEY, PersistentDataType.BYTE);
    }

    public static int getBlocksBroken(ItemStack item) {
        if (item == null || !item.hasItemMeta()) return 0;
        return item.getItemMeta().getPersistentDataContainer().getOrDefault(BLOCKS_BROKEN_KEY, PersistentDataType.INTEGER, 0);
    }

    public static void setBlocksBroken(ItemStack item, int blocks) {
        if (item == null || !item.hasItemMeta()) return;
        ItemMeta meta = item.getItemMeta();
        meta.getPersistentDataContainer().set(BLOCKS_BROKEN_KEY, PersistentDataType.INTEGER, blocks);
        item.setItemMeta(meta);
    }

    public static String getSkin(ItemStack item) {
        if (item == null || !item.hasItemMeta()) return "wooden";
        return item.getItemMeta().getPersistentDataContainer().getOrDefault(SKIN_KEY, PersistentDataType.STRING, "wooden");
    }

    public static void setSkin(ItemStack item, String skinId) {
        if (item == null || !item.hasItemMeta()) return;
        ItemMeta meta = item.getItemMeta();
        meta.getPersistentDataContainer().set(SKIN_KEY, PersistentDataType.STRING, skinId);
        item.setItemMeta(meta);
    }

    public static double getXP(ItemStack item) {
        if (item == null || !item.hasItemMeta()) return 0;
        return item.getItemMeta().getPersistentDataContainer().getOrDefault(XP_KEY, PersistentDataType.DOUBLE, 0.0);
    }

    public static void setXP(ItemStack item, double xp) {
        if (item == null || !item.hasItemMeta()) return;
        ItemMeta meta = item.getItemMeta();
        meta.getPersistentDataContainer().set(XP_KEY, PersistentDataType.DOUBLE, xp);
        item.setItemMeta(meta);
    }

    public static int getLevel(ItemStack item) {
        if (item == null || !item.hasItemMeta()) return 1;
        return item.getItemMeta().getPersistentDataContainer().getOrDefault(LEVEL_KEY, PersistentDataType.INTEGER, 1);
    }

    public static void setLevel(ItemStack item, int level) {
        if (item == null || !item.hasItemMeta()) return;
        ItemMeta meta = item.getItemMeta();
        meta.getPersistentDataContainer().set(LEVEL_KEY, PersistentDataType.INTEGER, level);
        item.setItemMeta(meta);
    }
}
