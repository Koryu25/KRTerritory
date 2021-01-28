package com.github.koryu25.krterritory.listener;

import com.github.koryu25.krterritory.Main;
import com.github.koryu25.krterritory.kr.KrChunk;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;

public class BlockBreakListener implements Listener {

    public BlockBreakListener() {
        Main.instance.getServer().getPluginManager().registerEvents(this, Main.instance);
    }

    @EventHandler
    public void onBlockBreak(BlockBreakEvent e) {
        e.setCancelled(new KrChunk(e.getBlock().getChunk()).onAttacked(e.getPlayer(), 1));
    }
}
