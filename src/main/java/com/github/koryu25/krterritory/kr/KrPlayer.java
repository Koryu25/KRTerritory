package com.github.koryu25.krterritory.kr;

import com.github.koryu25.krterritory.Main;
import org.bukkit.entity.Player;

public class KrPlayer {

    //InstanceField
    //Player
    private final Player player;
    //uuid
    private final String uuid;
    //所持金
    //private int money;
    //最大所有可能チャンク数
    //private int maxTerritory;
    //派閥
    //private String faction;

    //Constructor
    public KrPlayer(Player player) {
        this.player = player;
        this.uuid = player.getUniqueId().toString();
    }

    //Insert
    public void insert() {
        if (isExists()) return;
        Main.instance.mysql().insertPlayer(player.getName(), uuid, Main.instance.myConfig().chunkHP);
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
    public boolean buy() {
        //お金が足りてるか
        int money = getMoney() - Main.instance.myConfig().chunkPrice;
        if (money <= 0) {
            player.sendMessage(Main.instance.messenger().getMsg("Command.Buy.NotEnough"));
            return true;
        }
        //ここで解放
        setMoney(money);
        setMaxTerritory(getMaxTerritory() + 1);
        return true;
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
    public int getTerritory() {
        return Main.instance.mysql().selectQuantity("territory", "owner", uuid);
    }
    public String getFaction() {
        return Main.instance.mysql().selectString("player", "faction", "uuid", uuid);
    }
    public int getMaxHP() {
        return Main.instance.mysql().selectInt("player", "max_hp", "uuid", uuid);
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
    public void setMaxHP(int maxHP) {
        Main.instance.mysql().update("player", "max_hp", maxHP, "uuid", uuid);
    }
}
