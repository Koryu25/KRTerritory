package com.github.koryu25.krterritory.kr;

import com.github.koryu25.krterritory.Main;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.sql.Date;
import java.util.*;

public class KrFaction {

    //InstanceField
    //名前
    private final String name;
    //リーダー
    //private Player leader;
    //最大メンバー数(システム限界を25に設定(DB的に))
    //private int maxMember;
    //味方派閥
    //private List<String> ally;
    //敵派閥
    //private List<String> enemy;

    //Constructor
    public KrFaction(String name) {
        this.name = name;
    }

    //Create
    public boolean create(Player player) {
        //文字制限の確認
        if (name.length() < 3 || name.length() > 24) {
            player.sendMessage("派閥の名前は3文字以上、24文字以下です。");
            return true;
        }
        //同じ名前が存在してないか
        if (isExists()) {
            player.sendMessage("入力した名前の派閥は既に存在します。");
            return true;
        }
        //プレイヤーがすでに所属してないか
        KrPlayer krp = new KrPlayer(player);
        if (krp.isBelong()) {
            player.sendMessage("あなたは既に派閥に所属しています。");
            return true;
        }
        //ここで作成
        insert(player);
        krp.setFaction(name);
        player.sendMessage("派閥:" + name + "を作成しました。");
        return true;
    }
    //Remove
    public boolean remove(Player player) {
        //存在するか
        if (!isExists()) {
            player.sendMessage("入力した派閥は存在しません。");
            return true;
        }
        //プレイヤーが派閥に所属しているか
        KrPlayer krp = new KrPlayer(player);
        if (!krp.isBelong()) {
            player.sendMessage("あなたは派閥に所属していません。");
            return true;
        }
        //プレイヤーの派閥と一致するか
        if (!krp.getFaction().equals(name)) {
            player.sendMessage("あなたは入力した派閥に所属していません。");
            return true;
        }
        //プレイヤーが派閥の頭首か
        if (!isLeader(player)) {
            player.sendMessage("あなたは派閥の頭首ではありません。");
            return true;
        }
        //ここで削除
        delete();
        krp.setFaction(null);
        player.sendMessage("派閥:" + name + "を削除しました。");
        return true;
    }
    //Add
    public boolean add(Player sender, Player target) {
        //派閥が存在するか
        if (!isExists()) {
            sender.sendMessage("あなたは派閥に所属していません。");
            return true;
        }
        //派閥の頭首か
        if (!isLeader(sender)) {
            sender.sendMessage("あなたは派閥の頭首ではありません。");
            return true;
        }
        //枠が足りてるか
        if (getMaxMember() == getMember().size()) {
            sender.sendMessage("メンバー枠が足りません。");
            return true;
        }
        KrPlayer krp = new KrPlayer(target);
        //DBにプレイヤーが存在するか
        if (!krp.isExists()) {
            sender.sendMessage("プレイヤーが存在しません。");
            return true;
        }
        //ここで追加
        krp.setFaction(name);
        sender.sendMessage("プレイヤー:" + target.getName() + "を派閥に追加しました。");
        target.sendMessage("派閥:" + name + "に加入しました。離脱するには→/krt leave");
        return true;
    }
    //Kick
    public boolean kick(Player sender, Player target) {
        //派閥が存在するか
        if (!isExists()) {
            sender.sendMessage("あなたは派閥に所属していません。");
            return true;
        }
        //派閥の頭首か
        if (!isLeader(sender)) {
            sender.sendMessage("あなたは派閥の頭首ではありません。");
            return true;
        }
        //対象が派閥に所属しているか
        if (!name.equals(new KrPlayer(target).getFaction())) {
            sender.sendMessage("入力されたプレイヤーは派閥に所属していません。");
            return true;
        }
        KrPlayer krp = new KrPlayer(target);
        //DBにプレイヤーが存在するか
        if (!krp.isExists()) {
            sender.sendMessage("プレイヤーが存在しません。");
            return true;
        }
        //ここで除外
        krp.setFaction(null);
        sender.sendMessage("プレイヤー:" + target.getName() + "を派閥から除外しました。");
        target.sendMessage("派閥:" + name + "から除外されました。");
        return true;
    }
    //BuySlot
    public boolean buySlot(Player player) {
        //派閥が存在するか
        if (!isExists()) {
            player.sendMessage("あなたは派閥に所属していません。");
            return true;
        }
        //お金が足りてるか
        KrPlayer krp = new KrPlayer(player);
        int money = krp.getMoney() - Main.instance.myConfig().factionSlot;
        if (money <= 0) {
            player.sendMessage("お金が足りません。");
            return true;
        }
        //最大値でないか
        if (getMaxMember() == Main.instance.myConfig().factionMember) {
            player.sendMessage("既に最大値です。");
            return true;
        }
        //ここで解放
        krp.setMoney(money);
        setMaxMember(getMaxMember() + 1);
        player.sendMessage("メンバー枠を購入しました。");
        return true;
    }
    //Change
    public boolean change(Player sender, Player target) {
        //派閥が存在するか
        if (!isExists()) {
            sender.sendMessage("あなたは派閥に所属していません。");
            return true;
        }
        //派閥の頭首か
        if (!isLeader(sender)) {
            sender.sendMessage("あなたは派閥の頭首ではありません。");
            return true;
        }
        //対象が派閥に所属しているか
        if (!name.equals(new KrPlayer(target).getFaction())) {
            sender.sendMessage("入力されたプレイヤーは派閥に所属していません。");
            return true;
        }
        //ここで交代
        setLeader(target);
        sender.sendMessage("プレイヤー:" + target.getName() + "を派閥の頭首にしました。");
        target.sendMessage("派閥:" + name + "の頭首に任命されました。");
        return true;
    }

    //Insert
    public void insert(Player leader) {
        if (isExists()) return;
        Main.instance.mysql().insertFaction(name, leader.getUniqueId().toString());
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
    public boolean isLeader(Player player) {
        if (getLeader().getName().equals(player.getName())) return true;
        else return false;
    }

    //Getter
    public String getName() {
        return name;
    }
    public int getTerritory() {
        int territory = 0;
        for (Player one : getMember()) territory += new KrPlayer(one).getTerritory();
        return territory;
    }
    public int getMaxTerritory() {
        int max = 0;
        for (Player one : getMember()) max += new KrPlayer(one).getMaxTerritory();
        return max;
    }
    public Player getLeader() {
        return Bukkit.getPlayer(UUID.fromString(Main.instance.mysql().selectString("faction", "leader", "name", name)));
    }
    public int getMaxMember() {
        return Main.instance.mysql().selectInt("faction", "max_member", "name", name);
    }
    public List<Player> getMember() {
        return stringToPlayer(Main.instance.mysql().selectStringList("player", "uuid", "faction", name));
    }
    public List<String> getAlly() {
        return stringToList(Main.instance.mysql().selectString("faction", "ally", "name", name));
    }
    public List<String> getEnemy() {
        return stringToList(Main.instance.mysql().selectString("faction", "enemy", "name", name));
    }
    //Setter
    public void setLeader(Player leader) {
        Main.instance.mysql().update("faction", "leader", leader.getUniqueId().toString(), "name", name);
    }
    public void setMaxMember(int maxMember) {
        Main.instance.mysql().update("faction", "max_member", maxMember, "name", name);
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