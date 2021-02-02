package com.github.koryu25.krterritory.kr;

import com.github.koryu25.krterritory.Main;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.Bukkit;
import org.bukkit.Chunk;
import org.bukkit.entity.Player;

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
        if (player.getWorld() != Main.instance.myConfig().world) {
            player.sendMessage("現在ワールドの領土は所有できません。");
            return true;
        }
        //チャンクが所有されてないか
        if (isExists()) {
            if (isOwner(player)) {
                player.sendMessage("現在チャンクは既に所有しています。");
            } else {
                player.sendMessage("現在チャンクはプレイヤー:" + getOwner().getName() + "が所有しています。");
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
        if (player.getWorld() != Main.instance.myConfig().world) {
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
            player.sendMessage("現在チャンクはプレイヤー:" + getOwner().getName() + "に所有されています。");
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
        if (getOwner().equals(player.getUniqueId().toString())) return true;
        //派閥が同じか
        KrPlayer owner = new KrPlayer(getOwner());
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
        if (player.getWorld() != Main.instance.myConfig().world) return false;
        //所有者確認
        if (isOwner(player)) return false;
        //報告
        String msg = "現在のチャンクは保護されているため、干渉できません。";
        player.spigot().sendMessage(ChatMessageType.ACTION_BAR, TextComponent.fromLegacyText(msg));
        return true;
    }
    //onAttacked
    public boolean onAttacked(Player attacker, int damage) {
        if (attacker.getWorld() != Main.instance.myConfig().world) return false;
        if (!isExists()) return false;
        //自分の領土か
        if (isOwner(attacker)) return false;
        //所有者がオンラインか
        KrPlayer krp = new KrPlayer(getOwner());
        if (krp.isBelong()) {
            if (!new KrFaction(krp.getFaction()).isOnline()) {
                String msg = "所有者がオフラインなので攻撃できません。";
                attacker.spigot().sendMessage(ChatMessageType.ACTION_BAR, TextComponent.fromLegacyText(msg));
                return true;
            }
        } else if (!getOwner().isOnline()) {
            String msg = "所有者がオフラインなので攻撃できません。";
            attacker.spigot().sendMessage(ChatMessageType.ACTION_BAR, TextComponent.fromLegacyText(msg));
            return true;
        }
        //ここで攻撃
        return onDamage(attacker, damage);
    }
    //ダメージ
    public boolean onDamage(Player attacker, int damage) {
        int hp = getHP() - damage;
        if (hp <= 0) {
            //オーナー変更
            setOwner(attacker.getUniqueId().toString());
            //HP
            setHP(new KrPlayer(attacker).getMaxHP());
            //攻撃者にメッセージ
            attacker.sendMessage("領土を奪いました。");
            //所有者にメッセージ
            getOwner().sendMessage("領土を奪われました。");
            return false;
        } else {
            //ダメージ付与
            setHP(hp);
            //攻撃者にメッセージ
            String msg = "§c領土攻撃中　HP: " + hp;
            attacker.spigot().sendMessage(ChatMessageType.ACTION_BAR, TextComponent.fromLegacyText(msg));
            //所有者にメッセージ
            msg = "§4領土が攻撃されています！";
            getOwner().spigot().sendMessage(ChatMessageType.ACTION_BAR, TextComponent.fromLegacyText(msg));
            return true;
        }
    }
    //回復
    public boolean recovery(Player player) {
        //ワールド確認
        if (player.getWorld() != Main.instance.myConfig().world) {
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
    public Player getOwner() {
        return Bukkit.getPlayer(UUID.fromString(Main.instance.mysql().selectString("territory", "owner", "coordinate", coordinate)));
    }
    public int getHP() {
        return Main.instance.mysql().selectInt("territory", "hp", "coordinate", coordinate);
    }
    public int getMaxHP() {
        if (!isExists()) return Main.instance.myConfig().chunkHP;
        return new KrPlayer(getOwner()).getMaxHP();
    }
    //Setter
    public void setOwner(String owner) {
        Main.instance.mysql().update("territory", "owner", owner, "coordinate", coordinate);
    }
    public void setHP(int hitPoint) {
        Main.instance.mysql().update("territory", "hp", hitPoint, "coordinate", coordinate);
    }
}
