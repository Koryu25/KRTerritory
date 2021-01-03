package com.github.koryu25.krterritory.kr;

import com.github.koryu25.krterritory.Main;
import org.bukkit.entity.Player;

public class krPlayer {

    //InstanceField
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
    public krPlayer(Player player) {
        this.player = player;
        this.uuid = player.getUniqueId().toString();
    }

    //Insert
    public void insert() {
        if (isExists()) return;
        Main.instance.mysql().insertPlayer(player.getName(), uuid);
    }
    //Delete
    public void delete() {
        if (!isExists()) return;
        Main.instance.mysql().delete("player", "uuid", uuid);
    }

    //isExists
    public boolean isExists() {
        return Main.instance.mysql().exists("player", "uuid", uuid);
    }
    //名前変更確認
    public void changedName() {
        if (!player.getName().equals(getName())) setName(player.getName());
    }

    //Getter
    public String getName() {
        return Main.instance.mysql().selectString("player", "name", "uuid", uuid);
    }
    public int getMoney() {
        return Main.instance.mysql().selectInt("player", "money", "uuid", uuid);
    }
    public int getMaxTerritory() {
        return Main.instance.mysql().selectInt("player", "max_territory", "uuid", uuid);
    }
    public String getFaction() {
        return Main.instance.mysql().selectString("player", "faction", "uuid", uuid);
    }
    //Setter
    public void setName(String name) {
        Main.instance.mysql().update("player", "name", name, "uuid", uuid);
    }
    public void setMoney(int money) {
        Main.instance.mysql().update("player", "money", money, "uuid", uuid);
    }
    public void setMaxTerritory(int maxTerritory) {
        Main.instance.mysql().update("player", "max_territory", maxTerritory, "uuid", uuid);
    }
    public void setFaction(String faction) {
        Main.instance.mysql().update("player", "faction", faction , "uuid", uuid);
    }
}
