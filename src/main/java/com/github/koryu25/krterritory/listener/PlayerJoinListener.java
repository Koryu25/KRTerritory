package com.github.koryu25.krterritory.listener;

import com.github.koryu25.krterritory.Main;
import com.github.koryu25.krterritory.kr.KrPlayer;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public class PlayerJoinListener implements Listener {

    public PlayerJoinListener() {
        Main.instance.getServer().getPluginManager().registerEvents(this, Main.instance);
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent e) {
        KrPlayer krp = new KrPlayer(e.getPlayer());
        if (krp.isExists()) {
            krp.changedName();
            int days = krp.getNonLoginDays();
            if (days == 0) e.setJoinMessage(e.getPlayer().getName() + "さんがログインしました!");
            else e.setJoinMessage("§e" + e.getPlayer().getName() + "さんがログインしました! " + days + "日ぶりです。");
        } else {
            krp.insert();
            e.setJoinMessage("§d" + e.getPlayer().getName() + "さんが初めてログインしました! はじめまして!");
        }
    }
}
