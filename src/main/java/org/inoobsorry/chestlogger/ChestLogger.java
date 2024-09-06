package org.inoobsorry.chestlogger;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.Chest;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.Location;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public final class ChestLogger extends JavaPlugin implements Listener {

    // Карта для хранения времени последнего открытия сундука игроком
    private final Map<String, Long> lastOpenedChests = new HashMap<>();

    private static final long DELAY_MS = 5000;

    @Override
    public void onEnable() {
        Bukkit.getPluginManager().registerEvents(this, this);
    }

    @Override
    public void onDisable() {
    }

    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent event) {
        Block block = event.getClickedBlock();
        if (block != null && block.getType() == Material.CHEST) {
            // Получаем информацию о сундуке
            Chest chest = (Chest) block.getState();
            Location loc = chest.getLocation();
            String playerName = event.getPlayer().getName();
            String playerIP = event.getPlayer().getAddress().getAddress().getHostAddress();

            String chestKey = playerName + ":" + loc.getBlockX() + "," + loc.getBlockY() + "," + loc.getBlockZ();

            long currentTime = System.currentTimeMillis();

            if (lastOpenedChests.containsKey(chestKey)) {
                long lastOpenedTime = lastOpenedChests.get(chestKey);
                if (currentTime - lastOpenedTime < DELAY_MS) {
                    return; // Пропускаем логирование, если прошло меньше времени, чем DELAY_MS
                }
            }

            lastOpenedChests.put(chestKey, currentTime);

            Bukkit.getLogger().info(playerName + " (" + playerIP + ") открыл сундук на координатах: " + loc.getBlockX() + " " + loc.getBlockY() + " " + loc.getBlockZ());
        }
    }
}
