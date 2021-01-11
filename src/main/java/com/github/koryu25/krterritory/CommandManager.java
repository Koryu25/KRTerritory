package com.github.koryu25.krterritory;

import com.github.koryu25.krterritory.kr.enums.OwnerType;
import com.github.koryu25.krterritory.kr.KrChunk;
import com.github.koryu25.krterritory.kr.KrPlayer;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.UUID;

public class CommandManager implements CommandExecutor {

    private final String mainCommand = "krt";

    public CommandManager() {
        Main.instance.getCommand(mainCommand).setExecutor(this);
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        //ヘルプ表示
        if (args.length == 0) {
            sender.sendMessage(Main.instance.messenger().getMsg("Command.Help.1"));
            sender.sendMessage(Main.instance.messenger().getMsg("Command.Help.2"));
            sender.sendMessage(Main.instance.messenger().getMsg("Command.Help.3"));
            sender.sendMessage(Main.instance.messenger().getMsg("Command.Help.4"));
            sender.sendMessage(Main.instance.messenger().getMsg("Command.Help.5"));
            return true;
        }
        //sender確認
        if (sender instanceof Player) {
            Player player = (Player) sender;

            //メニュー表示
            if (args[0].equalsIgnoreCase("Menu")) {
                if (args.length != 1) {
                    player.sendMessage(Main.instance.messenger().getMsg("Command.Menu.Usage"));
                    return true;
                }
                //ここで表示
                Menu.main(player);
                return true;
            }
            //派閥コマンド
            else if (args[0].equalsIgnoreCase("Faction")) {
                //作成
                //削除
                //宣戦布告
                //領土主張
                //領土放棄
            }
            //領土枠解放
            else if (args[0].equalsIgnoreCase("Buy")) {
                if (args.length != 1) {
                    player.sendMessage(Main.instance.messenger().getMsg("Command.Buy.Usage"));
                    return true;
                }
                return new KrPlayer(player).buy();
            }
            //個人として領土主張
            else if (args[0].equalsIgnoreCase("Claim")) {
                if (args.length != 1) {
                    player.sendMessage(Main.instance.messenger().getMsg("Command.Claim.Usage"));
                    return true;
                }
                return new KrChunk(player.getLocation().getChunk()).claim(player);
            }

            //個人として領土放棄
            else if (args[0].equalsIgnoreCase("UnClaim")) {
                if (args.length != 1) {
                    player.sendMessage(Main.instance.messenger().getMsg("Command.UnClaim.Usage"));
                    return true;
                }
                return new KrChunk(player.getLocation().getChunk()).unclaim(player);
            }
        }
        return false;
    }
}
