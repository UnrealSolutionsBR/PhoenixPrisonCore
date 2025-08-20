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

        if (!PickaxeManager.isCustomPickaxe(item)) return;

        // Bloques +1
        int blocks = PickaxeManager.getBlocksBroken(item) + 1;

        // Skin y bonus
        String skinId = PickaxeManager.getSkin(item);
        SkinManager.SkinData skinData = SkinManager.getSkin(skinId);

        // XP anterior y nivel actual
        double xp = PickaxeManager.getXP(item);
        int level = PickaxeManager.getLevel(item);

        // XP ganado
        double baseValue = BlockValueManager.getValue(event.getBlock().getType());
        double gain = baseValue * skinData.bonus();
        xp += gain;

        // Revisar si sube de nivel
        int needed = PhoenixPickaxes.getInstance().getConfig().getInt("blocks-per-level", 100) * level;
        if (xp >= needed) {
            xp -= needed;
            level++;
            player.sendMessage("§6¡Tu pico ha subido al nivel " + level + "!");
        }

        // Regenerar el item con datos actualizados
        ItemStack updated = PickaxeManager.createPickaxe(player, blocks, level, xp, 0, 0, skinId);
        player.getInventory().setItemInMainHand(updated);
    }
}
