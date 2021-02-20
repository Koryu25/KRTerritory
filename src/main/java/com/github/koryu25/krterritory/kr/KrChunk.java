package com.github.koryu25.krterritory.kr;

import com.github.koryu25.krterritory.Main;
import com.github.koryu25.krterritory.listener.BlockBreakListener;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.Bukkit;
import org.bukkit.Chunk;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class KrChunk {

    //InstanceField
    //Chunk
    private final Chunk chunk;
    //座標
    private final String coordinate;
    //所有者
    //private String owner;
    //ヒットポイント
    //private int hitPoint;

    //Constructor
    public KrChunk(Chunk chunk) {
        this.chunk = chunk;
        this.coordinate = chunk.getX() + "," + chunk.getZ();
    }

    //Claim
    public boolean claim(Player player) {
        //ワールド確認
        if (!player.getWorld().getName().equals(Main.instance.myConfig().world)) {
            player.sendMessage("現在ワールドの領土は所有できません。");
            return true;
        }
        //チャンクが所有されてないか
        if (isExists()) {
            if (isOwner(player)) {
                player.sendMessage("現在チャンクは既に所有しています。");
            } else {
                player.sendMessage("現在チャンクはプレイヤー:" + getOwnerName() + "が所有しています。");
            }
            return true;
        }
        //領土枠が足りてるか
        KrPlayer krp = new KrPlayer(player);
        if (krp.getMaxTerritory() <= krp.getTerritory()) {
            player.sendMessage("最大所有可能領土数が上限です。");
            return true;
        }
        //お金が足りてるか
        int money = krp.getMoney() - Main.instance.myConfig().chunkClaim;
        if (money < 0) {
            player.sendMessage("お金が足りません。");
            return true;
        }
        //ここで領土主張
        krp.setMoney(money);
        insert(player.getUniqueId().toString());
        player.sendMessage("現在チャンクの所有権を主張しました。");
        return true;
    }
    //UnClaim
    public boolean unclaim(Player player) {
        //ワールド確認
        if (!player.getWorld().getName().equals(Main.instance.myConfig().world)) {
            player.sendMessage("現在ワールドで領土の放棄はできません。");
            return true;
        }
        //チャンクが所有されているか
        if (!isExists()) {
            player.sendMessage("現在チャンクは所有されていません。");
            return true;
        }
        //チャンクが自分のものか
        if (!isOwner(player)) {
            player.sendMessage("現在チャンクはプレイヤー:" + getOwnerName() + "に所有されています。");
            return true;
        }
        //ここで領土放棄
        delete();
        player.sendMessage("現在チャンクを放棄しました。");
        return true;
    }

    //Insert
    public void insert(String owner) {
        if (isExists()) return;
        int hp = new KrPlayer(Bukkit.getPlayer(UUID.fromString(owner))).getMaxHP();
        Main.instance.mysql().insertTerritory(coordinate, owner, hp);
    }
    //Delete
    public void delete() {
        if (!isExists()) return;
        Main.instance.mysql().delete("territory", "coordinate", coordinate);
    }
    //isExists
    public boolean isExists() {
        return Main.instance.mysql().exists("territory", "coordinate", coordinate);
    }
    //isOwner
    public boolean isOwner(Player player) {
        //存在するか
        if (!isExists()) return false;
        //プレイヤーが同じか
        if (getOwnerUUID().equals(player.getUniqueId().toString())) return true;
        //派閥が同じか
        KrPlayer owner = new KrPlayer(getOwnerUUID(), getOwnerName());
        if (owner.isBelong()) {
            if (owner.getFaction().equals(new KrPlayer(player).getFaction())) return true;
        }
        return false;
    }
    //isHPMax
    public boolean isHPMax() {
        if (getHP() == getMaxHP()) return true;
        else return false;
    }
    //isProtected
    public boolean isProtected(Player player) {
        if (!isExists()) return false;
        //ワールド確認
        if (!player.getWorld().getName().equals(Main.instance.myConfig().world)) return false;
        //所有者確認
        if (isOwner(player)) return false;
        //報告
        String msg = "現在のチャンクは保護されているため、干渉できません。";
        player.spigot().sendMessage(ChatMessageType.ACTION_BAR, TextComponent.fromLegacyText(msg));
        return true;
    }
    //onAttacked
    public boolean onAttacked(Player attacker, Block block, int damage) {
        //ワールド確認
        if (!attacker.getWorld().getName().equals(Main.instance.myConfig().world)) return false;
        //DBに存在するか
        if (!isExists()) return false;
        //ブロック確認
        if (BlockBreakListener.getBannedMaterial().contains(block.getType())) return true;
        //自分の領土か
        if (isOwner(attacker)) return false;
        //非ログイン日数確認
        if (new KrPlayer(getOwnerUUID(), getOwnerName()).getNonLoginDays() >= Main.instance.myConfig().protectionDay) return onDamage(attacker, damage, false);
        //所有者がオンラインか
        if (!getOwnerIsOnline()) {
            String msg = "所有者がオフラインなので攻撃できません。";
            attacker.spigot().sendMessage(ChatMessageType.ACTION_BAR, TextComponent.fromLegacyText(msg));
            return true;
        }
        //ここで攻撃
        return onDamage(attacker, damage, true);
    }
    //ダメージ
    public boolean onDamage(Player attacker, int damage, boolean login) {
        int hp = getHP() - damage;
        Player owner = getOwner();
        if (hp <= 0) {
            //オーナー変更
            setOwner(attacker);
            //HP
            setHP(new KrPlayer(attacker).getMaxHP());
            //攻撃者にメッセージ
            attacker.sendMessage("領土を奪いました。");
            //所有者にメッセージ
            if (login) owner.sendMessage("領土を奪われました。");
            return false;
        } else {
            //ダメージ付与
            setHP(hp);
            //攻撃者にメッセージ
            String msg = "§c領土攻撃中 HP: " + hp;
            attacker.spigot().sendMessage(ChatMessageType.ACTION_BAR, TextComponent.fromLegacyText(msg));
            //所有者にメッセージ
            msg = "§4領土が攻撃されています!! 座標: " + coordinate;
            if (login) owner.spigot().sendMessage(ChatMessageType.ACTION_BAR, TextComponent.fromLegacyText(msg));
            return true;
        }
    }
    //回復
    public boolean recovery(Player player) {
        //ワールド確認
        if (!player.getWorld().getName().equals(Main.instance.myConfig().world)) {
            player.sendMessage("現在のワールドで領土HPの回復はできません。");
            return true;
        }
        //所有者確認
        if (!isOwner(player)) {
            player.sendMessage("あなたは現在チャンクの所有者ではありません。");
            return true;
        }
        //既に上限か確認
        if (getHP() == getMaxHP()) {
            player.sendMessage("領土HPは既に上限値です。");
            return true;
        }
        //お金が足りるか確認
        KrPlayer krp = new KrPlayer(player);
        int money = krp.getMoney() - Main.instance.myConfig().chunkRecovery;
        if (money < 0) {
            player.sendMessage("お金が足りません。");
            return true;
        }
        //ここで回復
        krp.setMoney(money);
        setHP(getMaxHP());
        player.sendMessage("領土HPを回復しました。");
        return true;
    }

    //Getter
    public String getCoordinate() {
        return coordinate;
    }
    public Chunk getChunk() {
        return chunk;
    }
    public String getOwnerUUID() {
        return Main.instance.mysql().selectString("territory", "owner", "coordinate", coordinate);
    }
    public String getOwnerName() {
        return Main.instance.mysql().selectString("player", "name", "uuid", getOwnerUUID());
    }
    public Player getOwner() {
        return Bukkit.getPlayer(UUID.fromString(getOwnerUUID()));
    }
    public boolean getOwnerIsOnline() {
        if (getOwner() != null) return true;
        else return false;
    }
    public int getHP() {
        return Main.instance.mysql().selectInt("territory", "hp", "coordinate", coordinate);
    }
    public int getMaxHP() {
        if (!isExists()) return Main.instance.myConfig().chunkHP;
        return new KrPlayer(getOwnerUUID(), getOwnerName()).getMaxHP();
    }
    //Setter
    public void setOwner(Player owner) {
        Main.instance.mysql().update("territory", "owner", owner.getUniqueId().toString(), "coordinate", coordinate);
    }
    public void setHP(int hitPoint) {
        Main.instance.mysql().update("territory", "hp", hitPoint, "coordinate", coordinate);
    }
}
