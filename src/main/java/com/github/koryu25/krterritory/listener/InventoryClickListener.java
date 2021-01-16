package com.github.koryu25.krterritory.listener;

import com.github.koryu25.krterritory.Main;
import com.github.koryu25.krterritory.Menu;
import com.github.koryu25.krterritory.kr.KrChunk;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;

public class InventoryClickListener implements Listener {

    public InventoryClickListener() {
        Main.instance.getServer().getPluginManager().registerEvents(this, Main.instance);
    }

    @EventHandler
    public void onInventoryClick(InventoryClickEvent e) {
        Player player = (Player) e.getWhoClicked();
        if (e.getCurrentItem() == null) return;
        String title = e.getView().getTitle();
        //メインメニュー
        if (title.equals(Menu.mainName)) {
            e.setCancelled(true);
            if (e.getSlot() == 20) {
                //派閥
            } else if (e.getSlot() == 22) {
                //領土
                Menu.chunk(player);
            } else if (e.getSlot() == 24) {
                //プレイヤー
            }
        }
        //領土主張メニュー
        else if (title.equals(Menu.chunkName)) {
            e.setCancelled(true);
            KrChunk krc = new KrChunk(player.getLocation().getChunk());
            if (e.getSlot() == 3) {
                //claim, unclaim
                if (krc.isOwner(player)) {
                    krc.unclaimPlayer(player);
                } else {
                    if (krc.isExists()) {}
                    else krc.claimPlayer(player);
                }
                player.closeInventory();
            } else if (e.getSlot() == 5) {
                //HP回復
                if (krc.isOwner(player)) {
                    if (!krc.isHPMax()) {
                        krc.setHP(krc.getMaxHP());
                        player.closeInventory();
                    }
                }
            } else if (e.getSlot() == 7) {
                //メインメニュー
                Menu.main(player);
            }
        }
    }
}
