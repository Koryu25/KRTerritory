package com.github.koryu25.krterritory;

import com.github.koryu25.krterritory.kr.enums.OwnerType;
import com.github.koryu25.krterritory.kr.krChunk;
import com.github.koryu25.krterritory.kr.krPlayer;
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
            }
            //派閥コマンド
            else if (args[0].equalsIgnoreCase("Faction")) {
                //作成
                //削除
                //宣戦布告
                //領土主張
                //領土放棄
            }
            //所有者の変更
            //個人として領土主張
            else if (args[0].equalsIgnoreCase("Claim")) {
                if (args.length != 1) {
                    player.sendMessage(Main.instance.messenger().getMsg("Command.Claim.Usage"));
                    return true;
                }
                //ワールド確認
                if (player.getWorld() != Main.instance.myConfig().world) {
                    player.sendMessage(Main.instance.messenger().getMsg("Command.Claim.World"));
                    return true;
                }
                //チャンクが所有されてないか
                krChunk krc = new krChunk(player.getLocation().getChunk());
                if (krc.isExists()) {
                    String owner = krc.getOwner();
                    if (krc.getOwnerType() == OwnerType.Player) {
                        //既に所有しているか
                        //他プレイヤーが所有しているか
                        if (owner.equals(player.getUniqueId().toString())) {
                            player.sendMessage(Main.instance.messenger().getMsg("Command.Claim.Player.Own"));
                        } else {
                            player.sendMessage(Main.instance.messenger().getMsg("Command.Claim.Player.Other"));
                        }
                        return true;
                    } else if (krc.getOwnerType() == OwnerType.Faction) {
                        //自分の派閥が所有しているか
                        //他の派閥が所有しているか
                        if (new krPlayer(player).getFaction().equals(owner)) {
                            player.sendMessage(Main.instance.messenger().getMsg("Command.Claim.Faction.Own"));
                        } else {
                            player.sendMessage(Main.instance.messenger().getMsg("Command.Claim.Faction.Other", owner));
                        }
                        return true;
                    } else if (krc.getOwnerType() == OwnerType.NPC) {
                        player.sendMessage(Main.instance.messenger().getMsg("Command.Claim.NPC", owner));
                        return true;
                    }
                }
                //ここで領土主張
                krc.insert(player.getUniqueId().toString(), OwnerType.Player, Main.instance.myConfig().chunkHP);
                player.sendMessage(Main.instance.messenger().getMsg("Command.Claim.Success"));
                return true;
            }

            //個人として領土放棄
            else if (args[0].equalsIgnoreCase("UnClaim")) {
                if (args.length != 1) {
                    player.sendMessage(Main.instance.messenger().getMsg("Command.UnClaim.Usage"));
                    return true;
                }
                //ワールド確認
                if (player.getWorld() != Main.instance.myConfig().world) {
                    player.sendMessage(Main.instance.messenger().getMsg("Command.UnClaim.World"));
                    return true;
                }
                //チャンクが所有されているか
                krChunk krc = new krChunk(player.getLocation().getChunk());
                if (!krc.isExists()) {
                    player.sendMessage(Main.instance.messenger().getMsg("Command.UnClaim.NoOwner"));
                    return true;
                }
                //チャンクが自分のものか
                String owner = krc.getOwner();
                if (krc.getOwnerType() != OwnerType.Player) {
                    if (krc.getOwnerType() == OwnerType.Faction) {
                        player.sendMessage(Main.instance.messenger().getMsg("Command.UnClaim.Faction", owner));
                    } else if (krc.getOwnerType() == OwnerType.NPC) {
                        player.sendMessage(Main.instance.messenger().getMsg("Command.UnClaim.NPC", owner));
                    } else if (krc.getOwnerType() == OwnerType.Gathering) {
                        player.sendMessage(Main.instance.messenger().getMsg("Command.UnClaim.Gathering"));
                    }
                    return true;
                } else if (!owner.equals(player.getUniqueId().toString())) {
                    player.sendMessage(Main.instance.messenger().getMsg("Command.UnClaim.Player", Bukkit.getPlayer(UUID.fromString(owner))));
                    return true;
                }
                //ここで領土放棄
                krc.delete();
                player.sendMessage(Main.instance.messenger().getMsg("Command.UnClaim.Success"));
                return true;
            }
        }
        return false;
    }
}
