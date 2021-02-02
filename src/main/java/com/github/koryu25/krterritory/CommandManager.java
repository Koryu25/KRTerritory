package com.github.koryu25.krterritory;

import com.github.koryu25.krterritory.kr.KrChunk;
import com.github.koryu25.krterritory.kr.KrFaction;
import com.github.koryu25.krterritory.kr.KrPlayer;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class CommandManager implements CommandExecutor {

    private final String mainCommand = "krt";

    public CommandManager() {
        Main.instance.getCommand(mainCommand).setExecutor(this);
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        //ヘルプ表示
        if (args.length == 0) {
            sender.sendMessage("====== KRTerritory [Command Help] ======");
            sender.sendMessage("/krt claim → 現在チャンクを占領");
            sender.sendMessage("/krt unclaim → 現在チャンクを放棄");
            sender.sendMessage("/krt faction → 派閥コマンド(未実装)");
            sender.sendMessage("======================================");
            return true;
        }
        //sender確認
        if (sender instanceof Player) {
            Player player = (Player) sender;

            //メニュー表示
            if (args[0].equalsIgnoreCase("Menu")) {
                if (args.length != 1) {
                    player.sendMessage("/krt menu");
                    return true;
                }
                //ここで表示
                Menu.main(player);
                player.sendMessage("メインメニューを開きました。");
                return true;
            }
            //購入
            if (args[0].equalsIgnoreCase("Buy")) {
                if (args.length == 1) return false;
                //領土枠解放
                if (args[1].equalsIgnoreCase("Slot")) {
                    if (args.length != 2) {
                        player.sendMessage("/krt buy slot");
                        return true;
                    }
                    return new KrPlayer(player).buySlot();
                }
                //領土レベルアップ
                if (args[1].equalsIgnoreCase("HP")) {
                    if (args.length != 2) {
                        player.sendMessage("/krt buy hp");
                        return true;
                    }
                    return new KrPlayer(player).buyHP();
                }
                //派閥員枠購入
                if (args[1].equalsIgnoreCase("Member")) {
                    if (args.length != 2) {
                        player.sendMessage("/krt buy member");
                        return true;
                    }
                    return new KrFaction(new KrPlayer(player).getFaction()).buySlot(player);
                }
            }
            //領土主張
            if (args[0].equalsIgnoreCase("Claim")) {
                if (args.length != 1) {
                    player.sendMessage("/krt claim");
                    return true;
                }
                return new KrChunk(player.getLocation().getChunk()).claim(player);
            }
            //領土放棄
            if (args[0].equalsIgnoreCase("UnClaim")) {
                if (args.length != 1) {
                    player.sendMessage("/krt unclaim");
                    return true;
                }
                return new KrChunk(player.getLocation().getChunk()).unclaim(player);
            }
            //HP回復
            if (args[0].equalsIgnoreCase("Recovery")) {
                if (args.length != 1) {
                    player.sendMessage("/krt recovery");
                    return true;
                }
                return new KrChunk(player.getLocation().getChunk()).recovery(player);
            }
            //派閥作成
            if (args[0].equalsIgnoreCase("Create")) {
                if (args.length != 2) {
                    player.sendMessage("/krt create [ 派閥の名前 ]");
                    return true;
                }
                return new KrFaction(args[1]).create(player);
            }
            //派閥削除
            if (args[0].equalsIgnoreCase("Remove")) {
                if (args.length != 2) {
                    player.sendMessage("/krt remove [ 派閥の名前 ]");
                    return true;
                }
                return new KrFaction(args[1]).remove(player);
            }
            //派閥メンバー追加
            if (args[0].equalsIgnoreCase("Add")) {
                if (args.length != 2) {
                    player.sendMessage("/krt add [ プレイヤーの名前 ]");
                    return true;
                }
                return new KrFaction(new KrPlayer(player).getFaction()).add(player, Bukkit.getPlayer(args[1]));
            }
            //派閥メンバー除外
            if (args[0].equalsIgnoreCase("Kick")) {
                if (args.length != 2) {
                    player.sendMessage("/krt kick [ プレイヤーの名前 ]");
                    return true;
                }
                return new KrFaction(new KrPlayer(player).getFaction()).kick(player, Bukkit.getPlayer(args[1]));
            }
            //派閥離脱
            if (args[0].equalsIgnoreCase("Leave")) {
                if (args.length != 1) {
                    player.sendMessage("/krt leave");
                    return true;
                }
                return new KrPlayer(player).leave();
            }
            //派閥頭首交代
            if (args[0].equalsIgnoreCase("Change")) {
                if (args.length != 2) {
                    player.sendMessage("/krt change [ プレイヤーの名前 ]");
                    return true;
                }
                return new KrFaction(new KrPlayer(player).getFaction()).change(player, Bukkit.getPlayer(args[1]));
            }
            //派閥リネーム
            //味方派閥追加、除外
            //敵派閥追加、除外
        }
        return false;
    }
}
