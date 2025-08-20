package com.phoenixpickaxes.listeners;

import com.phoenixpickaxes.managers.BlockValueManager;
import com.phoenixpickaxes.managers.PickaxeManager;
import com.phoenixpickaxes.managers.SkinManager;
import com.phoenixpickaxes.PhoenixPickaxes;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.inventory.ItemStack;

public class BlockBreakListener implements Listener {

    @EventHandler
    public void onBlockBreak(BlockBreakEvent event) {
        Player player = event.getPlayer();
        ItemStack item = player.getInventory().getItemInMainHand();

        // Validar que es un pico custom
        if (!PickaxeManager.isCustomPickaxe(item)) return;

        // Bloques +1 (siempre aumenta de a 1)
        int blocks = PickaxeManager.getBlocksBroken(item) + 1;

        // Obtener skin y bonus
        String skinId = PickaxeManager.getSkin(item);
        SkinManager.SkinData skinData = SkinManager.getSkin(skinId);

        // XP y nivel actuales
        double xp = PickaxeManager.getXP(item);
        int level = PickaxeManager.getLevel(item);

        // XP base desde blocks.yml
        double baseValue = BlockValueManager.getValue(event.getBlock().getType());

        // Ganancia de XP con bonus de skin
        double gain = baseValue * skinData.bonus();
        xp += gain;

        // Revisar si sube de nivel
        int needed = PhoenixPickaxes.getInstance().getConfig().getInt("blocks-per-level", 100) * level;
        while (xp >= needed) {
            xp -= needed;
            level++;
            player.sendMessage("§6¡Tu pico ha subido al nivel " + level + "!");
            needed = PhoenixPickaxes.getInstance().getConfig().getInt("blocks-per-level", 100) * level;
        }

        // Actualizar el pico con datos nuevos
        ItemStack updated = PickaxeManager.createPickaxe(player, blocks, level, xp, 0, 0, skinId);
        player.getInventory().setItemInMainHand(updated);
    }
}
