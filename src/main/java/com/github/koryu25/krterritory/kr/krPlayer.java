package com.github.koryu25.krterritory.kr;

import com.github.koryu25.krterritory.Main;
import org.bukkit.entity.Player;

public class krPlayer {

    //InstanceField
    private final Main plugin;
    //Player
    private final Player player;
    //uuid
    private final String uuid;
    //所持金
    private int money;
    //最大所有可能チャンク数
    private int maxTerritory;
    //派閥
    private String faction;
    //データに存在するか
    private boolean exists;

    //Constructor
    public krPlayer(Main plugin, Player player) {
        this.plugin = plugin;
        this.player = player;
        this.uuid = player.getUniqueId().toString();
    }

    //Insert
    public void insert() {
        if (isExists()) return;
        plugin.mysql().insertPlayer(player.getName(), uuid);
    }
    //Delete
    public void delete() {
        if (!isExists()) return;
        plugin.mysql().delete("player", "uuid", uuid);
    }

    //isExists
    public boolean isExists() {
        return plugin.mysql().exists("player", "uuid", uuid);
    }
    //名前変更確認
    public void changedName() {
        if (!player.getName().equals(getName())) setName(player.getName());
    }

    //Getter
    public String getName() {
        return plugin.mysql().selectString("player", "name", "uuid", uuid);
    }
    public int getMoney() {
        return plugin.mysql().selectInt("player", "money", "uuid", uuid);
    }
    public int getMaxTerritory() {
        return plugin.mysql().selectInt("player", "max_territory", "uuid", uuid);
    }
    public String getFaction() {
        return plugin.mysql().selectString("player", "faction", "uuid", uuid);
    }
    //Setter
    public void setName(String name) {
        plugin.mysql().update("player", "name", name, "uuid", uuid);
    }
    public void setMoney(int money) {
        plugin.mysql().update("player", "money", money, "uuid", uuid);
    }
    public void setMaxTerritory(int maxTerritory) {
        plugin.mysql().update("player", "max_territory", maxTerritory, "uuid", uuid);
    }
    public void setFaction(String faction) {
        plugin.mysql().update("player", "faction", faction , "uuid", uuid);
    }
}
