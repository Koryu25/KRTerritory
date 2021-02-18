package com.github.koryu25.krterritory.listener;

import com.github.koryu25.krterritory.Main;
import com.github.koryu25.krterritory.MySQLManager;
import com.github.koryu25.krterritory.kr.KrPlayer;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

public class PlayerQuitListener implements Listener {

    public PlayerQuitListener() {
        Main.instance.getServer().getPluginManager().registerEvents(this, Main.instance);
    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent e) {
        KrPlayer krp = new KrPlayer(e.getPlayer());
        krp.setLastDate(MySQLManager.now());
        e.setQuitMessage("§6" + e.getPlayer().getName() + "がログアウトしました! さようなら!");
    }
}
