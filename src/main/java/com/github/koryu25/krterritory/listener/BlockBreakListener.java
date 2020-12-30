package com.github.koryu25.krterritory.listener;

import com.github.koryu25.krterritory.Main;
import com.github.koryu25.krterritory.kr.enums.OwnerType;
import com.github.koryu25.krterritory.kr.krChunk;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;

public class BlockBreakListener implements Listener {

    private Main plugin;

    public BlockBreakListener(Main plugin) {
        this.plugin = plugin;
        plugin.getServer().getPluginManager().registerEvents(this, plugin);
    }

    @EventHandler
    public void onBlockBreak(BlockBreakEvent e) {
        if (e.getBlock().getWorld() != plugin.myConfig().world) return;
        //チャンクがDBに存在するか
        krChunk krc = new krChunk(plugin, e.getBlock().getChunk());
        if (!krc.isExists()) return;
        //チャンクの所有者が自分か自分の派閥であるか
        if (krc.getOwnerType() == OwnerType.Player) {

        } else if (krc.getOwnerType() == OwnerType.Faction) {

        } else if (krc.getOwnerType() == OwnerType.NPC) {

        } else {

        }
    }
}
