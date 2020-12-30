package com.github.koryu25.krterritory.file;

import com.github.koryu25.krterritory.Main;
import org.bukkit.entity.Player;

public class Messenger extends CustomConfig {

    public Messenger(Main plugin) {
        super(plugin, "message.yml");
    }

    public String getMsg(String name) {
        return getConfig().getString(name);
    }
    public String getMsg(String name, Player player) {
        return setPlayer(getConfig().getString(name), player);
    }
    public String getMsg(String name, String faction) {
        return setFaction(getConfig().getString(name), faction);
    }
    public String getMsg(String name, Player player, String faction) {
        return setFaction(setPlayer(getConfig().getString(name), player), faction);
    }

    private String setPlayer(String message, Player player) {
        if (message.contains("{player}")) {
            message.replace("{player}", player.getName());
        }
        return message;
    }
    private String setFaction(String message, String faction) {
        if (message.contains("{faction}")) {
            message.replace("{faction}", faction);
        }
        return message;
    }

}
