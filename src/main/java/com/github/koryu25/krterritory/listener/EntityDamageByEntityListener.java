package com.github.koryu25.krterritory.listener;

import com.github.koryu25.krterritory.Main;
import com.github.koryu25.krterritory.kr.KrChunk;
import com.github.koryu25.krterritory.kr.KrFaction;
import com.github.koryu25.krterritory.kr.KrPlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

public class EntityDamageByEntityListener implements Listener {

    public EntityDamageByEntityListener() {
        Main.instance.getServer().getPluginManager().registerEvents(this, Main.instance);
    }

    @EventHandler
    public void onEntityDamageByEntity(EntityDamageByEntityEvent e) {
        if (e.getEntity() instanceof Player) {
            if (e.getDamager() instanceof Player) {
                String faction1 = new KrPlayer((Player) e.getEntity()).getFaction();
                if (faction1 == null) {
                    e.setCancelled(false);
                    return;
                }
                e.setCancelled(faction1.equals(new KrPlayer((Player) e.getDamager()).getFaction()));
            }
        } else {
            if (e.getDamager() instanceof Player) {
                e.setCancelled(new KrChunk(e.getEntity().getLocation().getChunk()).isProtected((Player) e.getDamager()));
            }
        }
    }
}
