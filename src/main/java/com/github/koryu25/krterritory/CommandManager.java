package com.github.koryu25.krterritory;

import com.github.koryu25.krterritory.kr.KrChunk;
import com.github.koryu25.krterritory.kr.KrFaction;
import com.github.koryu25.krterritory.kr.KrPlayer;
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
            sender.sendMessage(Main.instance.messenger().getMsg("Command.Help.0"));
            sender.sendMessage(Main.instance.messenger().getMsg("Command.Help.1"));
            sender.sendMessage(Main.instance.messenger().getMsg("Command.Help.2"));
            sender.sendMessage(Main.instance.messenger().getMsg("Command.Help.3"));
            sender.sendMessage(Main.instance.messenger().getMsg("Command.Help.4"));
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
            //購入
            if (args[0].equalsIgnoreCase("Buy")) {
                if (args.length == 1) return false;
                //領土枠解放
                if (args[1].equalsIgnoreCase("Slot")) {
                    if (args.length != 2) {
                        player.sendMessage(Main.instance.messenger().getMsg("Command.Buy.Slot.Usage"));
                        return true;
                    }
                    return new KrPlayer(player).buySlot();
                }
                //領土レベルアップ
                if (args[1].equalsIgnoreCase("HP")) {
                    if (args.length != 2) {
                        player.sendMessage(Main.instance.messenger().getMsg("Command.Buy.Level.Usage"));
                        return true;
                    }
                    return new KrPlayer(player).buyHP();
                }
            }
            //個人として領土主張
            if (args[0].equalsIgnoreCase("Claim")) {
                if (args.length != 1) {
                    player.sendMessage(Main.instance.messenger().getMsg("Command.Claim.Usage"));
                    return true;
                }
                return new KrChunk(player.getLocation().getChunk()).claimPlayer(player);
            }
            //個人として領土放棄
            if (args[0].equalsIgnoreCase("UnClaim")) {
                if (args.length != 1) {
                    player.sendMessage(Main.instance.messenger().getMsg("Command.UnClaim.Usage"));
                    return true;
                }
                return new KrChunk(player.getLocation().getChunk()).unclaimPlayer(player);
            }
        }
        return false;
    }
}
