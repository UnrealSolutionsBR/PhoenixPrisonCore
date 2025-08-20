package com.phoenixpickaxes.commands;

import com.phoenixpickaxes.PhoenixPickaxes;
import com.phoenixpickaxes.managers.PickaxeManager;
import com.phoenixpickaxes.managers.SkinManager;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class PhoenixCommand implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

        if (args.length == 0) {
            sender.sendMessage("§bPhoenixPickaxes §7- Usa:");
            sender.sendMessage(" §f/phoenixpickaxes reload §7→ Recargar configuración");
            sender.sendMessage(" §f/phoenixpickaxes setskin <skin> §7→ Cambiar skin del pico");
            return true;
        }

        if (args[0].equalsIgnoreCase("reload")) {
            PhoenixPickaxes.getInstance().reloadConfig();
            SkinManager.loadSkins();
            sender.sendMessage("§a[PhoenixPickaxes] Configuración recargada correctamente.");
            return true;
        }

        if (args[0].equalsIgnoreCase("setskin")) {
            if (!(sender instanceof Player player)) {
                sender.sendMessage("§cEste comando solo puede usarse en el juego.");
                return true;
            }

            if (args.length < 2) {
                player.sendMessage("§cUso: /phoenixpickaxes setskin <skin>");
                return true;
            }

            String skinId = args[1].toLowerCase();

            // Validar que la skin exista
            SkinManager.SkinData skinData = SkinManager.getSkin(skinId);
            if (skinData == null) {
                player.sendMessage("§cLa skin '" + skinId + "' no existe.");
                return true;
            }

            // Validar que tenga un pico custom en mano
            ItemStack item = player.getInventory().getItemInMainHand();
            if (!PickaxeManager.isCustomPickaxe(item)) {
                player.sendMessage("§cDebes tener tu pico custom en la mano para cambiar la skin.");
                return true;
            }

            // Recuperar datos actuales del pico
            int blocks = PickaxeManager.getBlocksBroken(item);
            int level = 1; // más adelante se puede guardar el nivel real
            int fortune = 0;
            int explosive = 0;

            // Crear el nuevo pico con la skin aplicada
            ItemStack newPickaxe = PickaxeManager.createPickaxe(player, blocks, level, fortune, explosive, skinId);
            player.getInventory().setItemInMainHand(newPickaxe);

            player.sendMessage("§aHas cambiado la skin de tu pico a: §f" + skinData.display());
            return true;
        }

        sender.sendMessage("§cUso: /phoenixpickaxes <reload|setskin>");
        return true;
    }
}
