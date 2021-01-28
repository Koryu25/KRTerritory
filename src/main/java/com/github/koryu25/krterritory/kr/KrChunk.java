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
    //Claim
    public boolean claim(Player player) {
        //ワールド確認
        if (player.getWorld() != Main.instance.myConfig().world) {
            player.sendMessage(Main.instance.messenger().getMsg("Command.Claim.World"));
            return true;
        }
        //チャンクが所有されてないか
        if (isExists()) {
            if (isOwner(player)) {
                player.sendMessage(Main.instance.messenger().getMsg("Command.Claim.Own"));
            } else {
                player.sendMessage(Main.instance.messenger().getMsg("Command.Claim.Other", getOwner()));
            }
            return true;
        }
        //領土枠が足りてるか
        KrPlayer krp = new KrPlayer(player);
        if (krp.getMaxTerritory() <= krp.getTerritory()) {
            player.sendMessage(Main.instance.messenger().getMsg("Command.Claim.Max"));
            return true;
        }
        //お金が足りてるか
        int money = krp.getMoney() - Main.instance.myConfig().chunkClaim;
        if (money < 0) {
            player.sendMessage(Main.instance.messenger().getMsg("Command.Claim.Money"));
            return true;
        }
        //ここで領土主張
        krp.setMoney(money);
        insert(player.getUniqueId().toString());
        player.sendMessage(Main.instance.messenger().getMsg("Command.Claim.Success"));
        return true;
    }
    //UnClaim
    public boolean unclaim(Player player) {
        //ワールド確認
        if (player.getWorld() != Main.instance.myConfig().world) {
            player.sendMessage(Main.instance.messenger().getMsg("Command.UnClaim.World"));
            return true;
        }
        //チャンクが所有されているか
        if (!isExists()) {
            player.sendMessage(Main.instance.messenger().getMsg("Command.UnClaim.NoOwner"));
            return true;
        }
        //チャンクが自分のものか
        if (!isOwner(player)) {
            player.sendMessage(Main.instance.messenger().getMsg("Command.UnClaim.Player", getOwner()));
            return true;
        }
        //ここで領土放棄
        delete();
        player.sendMessage(Main.instance.messenger().getMsg("Command.UnClaim.Success"));
        return true;
    }

    //isExists
    public boolean isExists() {
        return Main.instance.mysql().exists("territory", "coordinate", coordinate);
    }
    //isOwner
    public boolean isOwner(Player player) {
        if (!isExists()) return false;
        if (getOwner().equals(player.getUniqueId().toString())) return true;
        if (new KrFaction(new KrPlayer(player).getFaction()).getMember().contains(player)) return true;
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
        String msg = Main.instance.messenger().getMsg("Protected");
        player.spigot().sendMessage(ChatMessageType.ACTION_BAR, TextComponent.fromLegacyText(msg));
        return true;
    }
    //onAttacked
    public boolean onAttacked(Player attacker, int damage) {
        if (attacker.getWorld() != Main.instance.myConfig().world) return false;
        if (!isExists()) return false;
        KrPlayer krp = new KrPlayer(getOwner());
        //所有者がオンラインか
        if (krp.getFaction() != null) {
            if (!new KrFaction(krp.getFaction()).isOnline()) {
                String msg = Main.instance.messenger().getMsg("War.Offline", getOwner());
                attacker.spigot().sendMessage(ChatMessageType.ACTION_BAR, TextComponent.fromLegacyText(msg));
                return true;
            }
        } else if (!getOwner().isOnline()) {
            String msg = Main.instance.messenger().getMsg("War.Offline", getOwner());
            attacker.spigot().sendMessage(ChatMessageType.ACTION_BAR, TextComponent.fromLegacyText(msg));
            return true;
        }
        //自分の領土か
        if (isOwner(attacker)) return false;
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
            attacker.sendMessage(Main.instance.messenger().getMsg("War.Territory.Win"));
            //所有者にメッセージ
            getOwner().sendMessage(Main.instance.messenger().getMsg("War.Territory.Lose"));
            return false;
        } else {
            //ダメージ付与
            setHP(hp);
            //攻撃者にメッセージ
            String msg = Main.instance.messenger().getMsg("War.Territory.HP") + hp;
            attacker.spigot().sendMessage(ChatMessageType.ACTION_BAR, TextComponent.fromLegacyText(msg));
            //所有者にメッセージ
            msg = Main.instance.messenger().getMsg("War.Territory.Attacked");
            getOwner().spigot().sendMessage(ChatMessageType.ACTION_BAR, TextComponent.fromLegacyText(msg));
            return true;
        }
    }
    //回復
    public boolean recovery(Player player) {
        //ワールド確認
        if (player.getWorld() != Main.instance.myConfig().world) {
            player.sendMessage(Main.instance.messenger().getMsg("Command.Recovery.World"));
            return true;
        }
        //所有者確認
        if (!isOwner(player)) {
            player.sendMessage(Main.instance.messenger().getMsg("Command.Recovery.NotOwner"));
            return true;
        }
        //既に上限か確認
        if (getHP() == getMaxHP()) {
            player.sendMessage(Main.instance.messenger().getMsg("Command.Recovery.Max"));
            return true;
        }
        //お金が足りるか確認
        KrPlayer krp = new KrPlayer(player);
        int money = krp.getMoney() - Main.instance.myConfig().chunkRecovery;
        if (money < 0) {
            player.sendMessage(Main.instance.messenger().getMsg("Command.Recovery.NotEnough"));
            return true;
        }
        //ここで回復
        krp.setMoney(money);
        setHP(getMaxHP());
        player.sendMessage(Main.instance.messenger().getMsg("Command.Recovery.Success"));
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
