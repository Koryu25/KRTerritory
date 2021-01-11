package com.github.koryu25.krterritory.kr;

import com.github.koryu25.krterritory.Main;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class KrFaction {

    //InstanceField
    //名前
    private final String name;
    //色
    //private String color;
    //所持金
    //private int money;
    //リーダー
    //private Player leader;
    //最大メンバー数(システム限界を25に設定(DB的に))
    //private int maxMember;
    //メンバー
    //private List<Player> member;
    //最大領土数
    //private int maxTerritory;
    //味方派閥
    //private List<String> ally;
    //敵派閥
    //private List<String> enemy;

    //Constructor
    public KrFaction(String name) {
        this.name = name;
    }

    //Insert
    public void insert(Player leader) {
        if (isExists()) return;
        Main.instance.mysql().insertFaction(name, leader.getUniqueId().toString(), Main.instance.myConfig().chunkHP);
    }
    //Delete
    public void delete() {
        if (!isExists()) return;
        Main.instance.mysql().delete("faction", "name", name);
    }

    //isExists
    public boolean isExists() {
        return Main.instance.mysql().exists("faction", "name", name);
    }
    //isOnline
    public boolean isOnline() {
        for (Player player : getMember()) {
            if (player.isOnline()) return true;
        }
        return false;
    }

    //Getter
    public String getName() {
        return name;
    }
    public String getColor() {
        return Main.instance.mysql().selectString("faction", "color", "name", name);
    }
    public String getColorName() {
        return getColor() + getName();
    }
    public int getMoney() {
        return Main.instance.mysql().selectInt("faction", "money", "name", name);
    }
    public int getTerritory() {
        return Main.instance.mysql().selectQuantity("territory", "owner", name);
    }
    public Player getLeader() {
        return Bukkit.getPlayer(UUID.fromString(Main.instance.mysql().selectString("faction", "leader", "name", name)));
    }
    public int getMaxMember() {
        return Main.instance.mysql().selectInt("faction", "max_member", "name", name);
    }
    public List<Player> getMember() {
        return stringToPlayer(stringToList(Main.instance.mysql().selectString("faction", "member", "name", name)));
    }
    public int getMaxTerritory() {
        return Main.instance.mysql().selectInt("faction", "max_territory", "name", name);
    }
    public int getMaxHP() {
        return Main.instance.mysql().selectInt("faction", "max_hp", "name", name);
    }
    public List<String> getAlly() {
        return stringToList(Main.instance.mysql().selectString("faction", "ally", "name", name));
    }
    public List<String> getEnemy() {
        return stringToList(Main.instance.mysql().selectString("faction", "enemy", "name", name));
    }
    //Setter
    public void setColor(String color) {
        Main.instance.mysql().update("faction", "color", color, "name", name);
    }
    public void setMoney(int money) {
        Main.instance.mysql().update("faction", "money", money, "name", name);
    }
    public void setLeader(Player leader) {
        Main.instance.mysql().update("faction", "leader", leader.getUniqueId().toString(), "name", name);
    }
    public void setMaxMember(int maxMember) {
        Main.instance.mysql().update("faction", "max_member", maxMember, "name", name);
    }
    public void setMember(List<Player> member) {
        Main.instance.mysql().update("faction", "member", listToString(playerToString(member)), "name", name);
    }
    public void setMaxTerritory(int maxTerritory) {
        Main.instance.mysql().update("faction", "max_territory", maxTerritory, "name", name);
    }
    public void setMaxHP(int maxHP) {
        Main.instance.mysql().update("faction", "max_hp", maxHP, "name", name);
    }
    public void setAlly(List<String> ally) {
        Main.instance.mysql().update("faction", "ally", listToString(ally), "name", name);
    }
    public void setEnemy(List<String> enemy) {
        Main.instance.mysql().update("faction", "enemy", listToString(enemy), "name", name);
    }

    //String and List<String>
    public String listToString(List<String> list) {
        String str = "";
        for (int i = 0; i < list.size(); i++) {
            str = str + list.get(i);
            if (i != list.size() - 1) str = str + ",";
        }
        return str;
    }
    public List<String> stringToList(String str) {
        List<String> list = new ArrayList<>();
        String one = "";
        for (int i = 0; i < str.length(); i++) {
            char c = str.charAt(i);
            if (c == ',') {
                list.add(one);
                one = "";
            }
            else if (i == str.length()) list.add(one);
            else one = one + c;
        }
        return list;
    }
    //List<String> and List<Player>
    public List<Player> stringToPlayer(List<String> strList) {
        List<Player> playerList = new ArrayList<>();
        for (String str : strList) {
            playerList.add(Bukkit.getPlayer(UUID.fromString(str)));
        }
        return playerList;
    }
    public List<String> playerToString(List<Player> playerList) {
        List<String> strList = new ArrayList<>();
        for (Player player : playerList) {
            strList.add(player.getUniqueId().toString());
        }
        return strList;
    }
}