package com.github.koryu25.krterritory.listener;

import com.github.koryu25.krterritory.Main;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import sun.jvm.hotspot.ui.ObjectHistogramPanel;

public class PlayerJoinListener implements Listener {

    private Main plugin;

    public PlayerJoinListener(Main plugin) {
        this.plugin = plugin;
        plugin.getServer().getPluginManager().registerEvents(this, plugin);
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent e) {
        e.getPlayer().sendMessage("PlayerJoinEventが発火しました。");
    }
}
