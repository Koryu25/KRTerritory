package com.github.koryu25.krterritory.listener;

import com.github.koryu25.krterritory.Main;
import com.github.koryu25.krterritory.kr.KrChunk;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPlaceEvent;

public class BlockPlaceListener implements Listener {

    public BlockPlaceListener() {
        Main.instance.getServer().getPluginManager().registerEvents(this, Main.instance);
    }

    @EventHandler
    public void onBlockPlace(BlockPlaceEvent e) {
        //ワールド確認
        if (e.getBlock().getWorld() != Main.instance.myConfig().world) return;
        //所有者確認
        e.setCancelled(!new KrChunk(e.getBlock().getChunk()).isOwner(e.getPlayer()));
    }
}
