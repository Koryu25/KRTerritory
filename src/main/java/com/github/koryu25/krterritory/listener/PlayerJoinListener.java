package com.github.koryu25.krterritory.listener;

import com.github.koryu25.krterritory.Main;
import com.github.koryu25.krterritory.kr.krPlayer;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import sun.jvm.hotspot.ui.ObjectHistogramPanel;

public class PlayerJoinListener implements Listener {

    public PlayerJoinListener() {
        Main.instance.getServer().getPluginManager().registerEvents(this, Main.instance);
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent e) {
        krPlayer krp = new krPlayer(e.getPlayer());
        if (krp.isExists()) krp.changedName();
        else krp.insert();
    }
}
