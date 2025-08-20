package com.phoenixpickaxes.listeners;

import com.phoenixpickaxes.PhoenixPickaxes;
import com.phoenixpickaxes.managers.PickaxeManager;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerDropItemEvent;

import java.util.HashMap;
import java.util.UUID;

public class DropListener implements Listener {

    // Guardar la última vez que el jugador intentó tirar el pico
    private final HashMap<UUID, Long> lastDropAttempt = new HashMap<>();

    @EventHandler
    public void onDrop(PlayerDropItemEvent event) {
        Player player = event.getPlayer();

        // Solo aplicar a picos custom
        if (!PickaxeManager.isCustomPickaxe(event.getItemDrop().getItemStack())) return;

        UUID uuid = player.getUniqueId();
        long now = System.currentTimeMillis();

        // Tiempo de confirmación desde config.yml (por defecto 3 segundos)
        int confirmSeconds = PhoenixPickaxes.getInstance().getConfig().getInt("drop-confirm-seconds", 3);

        // Si no hay registro previo o pasó más del tiempo configurado
        if (!lastDropAttempt.containsKey(uuid) || (now - lastDropAttempt.get(uuid)) > confirmSeconds * 1000L) {
            event.setCancelled(true); // cancelar dropeo
            lastDropAttempt.put(uuid, now);

            // Mostrar título
            player.sendTitle("§4[!] §cCUIDADO",
                    "§7Aprieta §cQ §7de nuevo para soltar tu pico",
                    10, 40, 10);

            // Mensaje en chat
            player.sendMessage("§4[!] §7Aprieta §cQ §7de nuevo para soltar tu pico!");
        } else {
            // Segunda vez en menos de X segundos → permitir soltar
            lastDropAttempt.remove(uuid);
            player.sendMessage("§cHas soltado tu pico.");
        }
    }
}
