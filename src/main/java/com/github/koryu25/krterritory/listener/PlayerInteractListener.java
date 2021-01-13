package com.github.koryu25.krterritory.listener;

import com.github.koryu25.krterritory.Main;
import com.github.koryu25.krterritory.kr.KrChunk;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;

public class PlayerInteractListener implements Listener {

    public PlayerInteractListener() {
        Main.instance.getServer().getPluginManager().registerEvents(this, Main.instance);
    }

    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent e) {
        //ワールド確認
        if (e.getPlayer().getWorld() != Main.instance.myConfig().world) return;
        //所有者確認
        e.setCancelled(!new KrChunk(e.getClickedBlock().getChunk()).isOwner(e.getPlayer()));
    }
}
