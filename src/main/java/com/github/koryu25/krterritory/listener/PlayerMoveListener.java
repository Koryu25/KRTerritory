package com.github.koryu25.krterritory.listener;

import com.github.koryu25.krterritory.Main;
import com.github.koryu25.krterritory.kr.KrChunk;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;
import sun.jvm.hotspot.ui.ObjectHistogramPanel;

public class PlayerMoveListener implements Listener {

    public PlayerMoveListener() {
        Main.instance.getServer().getPluginManager().registerEvents(this, Main.instance);
    }

    @EventHandler
    public void onPlayerMove(PlayerMoveEvent e) {
        //チャンク移動してたら
        if (e.getFrom().getChunk() != e.getTo().getChunk()) {
            KrChunk krcFrom = new KrChunk(e.getFrom().getChunk());
            KrChunk krcTo = new KrChunk(e.getTo().getChunk());
            String msg;
            if (krcTo.isExists()) {
                if (krcFrom.isExists()) {
                    if (krcFrom.getOwnerUUID().equals(krcTo.getOwnerUUID())) return;
                }
                msg = krcTo.getOwnerName() + "の所有する領域に入りました。";
            } else {
                if (!krcFrom.isExists()) return;
                msg = krcFrom.getOwnerName() + "の所有する領域から出ました。";
            }
            e.getPlayer().spigot().sendMessage(ChatMessageType.ACTION_BAR, TextComponent.fromLegacyText(msg));
        }
    }
}
