package com.github.koryu25.krterritory.listener;

import com.github.koryu25.krterritory.Main;
import com.github.koryu25.krterritory.kr.enums.OwnerType;
import com.github.koryu25.krterritory.kr.krChunk;
import com.github.koryu25.krterritory.kr.krItem;
import com.github.koryu25.krterritory.kr.krPlayer;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;

public class BlockBreakListener implements Listener {

    public BlockBreakListener() {
        Main.instance.getServer().getPluginManager().registerEvents(this, Main.instance);
    }

    @EventHandler
    public void onBlockBreak(BlockBreakEvent e) {
        //ワールド確認
        if (e.getBlock().getWorld() != Main.instance.myConfig().world) return;
        //チャンクがDBに存在するか
        krChunk krc = new krChunk(e.getBlock().getChunk());
        if (!krc.isExists()) return;
        //持っているアイテム確認
        //戦争用のツール
        if (e.getPlayer().getInventory().getItemInMainHand().getItemMeta().getLore().contains("")) {
            krc.attacked(e.getPlayer(), 1);
        }
    }
}
