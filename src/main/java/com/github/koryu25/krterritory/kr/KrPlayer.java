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
        if (player == null) this.uuid = null;
        else this.uuid = player.getUniqueId().toString();
    }

    //領土枠購入
    public boolean buySlot() {
        //お金が足りてるか
        int money = getMoney() - Main.instance.myConfig().chunkSlot;
        if (money <= 0) {
            player.sendMessage("お金が足りません。");
            return true;
        }
        //最大値でないか
        if (getMaxTerritory() == Main.instance.myConfig().chunkMaxSlot) {
            player.sendMessage("既に最大値です。");
            return true;
        }
        //ここで解放
        setMoney(money);
        setMaxTerritory(getMaxTerritory() + 1);
        player.sendMessage("領土所有枠を買いました。");
        return true;
    }
    //領土HP購入
    public boolean buyHP() {
        //お金が足りてるか
        int money = getMoney() - Main.instance.myConfig().chunkLevel;
        if (money <= 0) {
            player.sendMessage("お金が足りません。");
            return true;
        }
        //最大値でないか
        if (getHPLevel() == Main.instance.myConfig().chunkMaxLevel) {
            player.sendMessage("既に最大値です。");
            return true;
        }
        //ここでレベルアップ
        setMoney(money);
        setHPLevel(getHPLevel() + 1);
        player.sendMessage("領土HPレベルを買いました。");
        return true;
    }
    //派閥離脱
    public boolean leave() {
        //派閥に所属しているか
        if (!isBelong()) {
            player.sendMessage("あなたは派閥に所属していません。");
            return true;
        }
        //頭首でないか
        if (isLeader()) {
            player.sendMessage("派閥の頭首は離脱できません。");
            return true;
        }
        //ここで離脱
        setFaction(null);
        player.sendMessage("派閥から離脱しました。");
        return true;
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
    public boolean isBelong() {
        if (getFaction() != null) return true;
        else return false;
    }
    public boolean isLeader() {
        if (getFaction() == null) return false;
        return new KrFaction(getFaction()).isLeader(player);
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
    public int getHPLevel() {
        return Main.instance.mysql().selectInt("player", "hp_level", "uuid", uuid);
    }
    public int getMaxHP() {
        return getHPLevel() * Main.instance.myConfig().chunkHP;
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
    public void setHPLevel(int level) {
        Main.instance.mysql().update("player", "hp_level", level, "uuid", uuid);
    }
}
