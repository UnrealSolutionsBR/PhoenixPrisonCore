package com.phoenixpickaxes.commands;

import com.phoenixpickaxes.managers.PickaxeManager;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class PickaxeCommand implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player player)) {
            sender.sendMessage("§cEste comando solo puede usarse en el juego.");
            return true;
        }

        // Crear y dar el pico con skin por defecto (ejemplo: "default")
        player.getInventory().addItem(PickaxeManager.createPickaxe(
                player,
                0,     // blocksBroken
                1,     // level
                0,     // fortune
                0,     // explosive
                "default" // skinId como String
        ));

        player.sendMessage("§a¡Se te ha entregado tu pico inicial!");
        return true;
    }
}
