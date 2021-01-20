package com.github.koryu25.krterritory;

import com.github.koryu25.krterritory.kr.KrChunk;
import com.github.koryu25.krterritory.kr.KrFaction;
import com.github.koryu25.krterritory.kr.KrPlayer;
import com.github.koryu25.krterritory.kr.enums.OwnerType;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.Arrays;

public class Menu {

    public static final String mainName = "[krt]MainMenu";
    public static final String chunkName = "[krt]ChunkMenu";
    public static final String buyName = "[krt]BuyMenu";

    //MainMenu
    public static void main(Player player) {
        Inventory inv = Bukkit.createInventory(null, 45, mainName);
        final int fIndex = 20, cIndex = 22, pIndex = 24;
        final String fName = "§f<<§6派閥の情報§f>>", cName = "§f<<§a現在チャンクの情報§f>>", pName = "§f<<§bプレイヤー情報§f>>";
        //派閥の情報
        KrPlayer krp = new KrPlayer(player);
        if (krp.getFaction() == null) {
            inv.setItem(fIndex, item(Material.WRITABLE_BOOK, fName, "§f派閥所属なし"));
        } else {
            KrFaction krf = new KrFaction(krp.getFaction());
            inv.setItem(fIndex, item(Material.WRITABLE_BOOK, fName,
                    "§7名前§f: " + krf.getColorName(),
                    "§7所持金§f: " + krf.getMoney(),
                    "§7頭首§f: " + krf.getLeader().getName(),
                    "§7所有領土数§f: " + krf.getTerritory()
            ));
        }
        //現在チャンクの情報
        KrChunk krc = new KrChunk(player.getLocation().getChunk());
        Material material;
        if (krc.isExists()) {
            if (krc.isOwner(player)) {
                material = Material.LIGHT_BLUE_STAINED_GLASS_PANE;
            } else if (krc.getOwnerType() == OwnerType.Gathering) {
                material = Material.YELLOW_STAINED_GLASS_PANE;
            } else {
                material = Material.RED_STAINED_GLASS_PANE;
            }
            inv.setItem(cIndex, item(Material.GRASS_BLOCK, cName,
                    "§7座標§f: " + krc.getCoordinate(),
                    "§7所有者§f: " + krc.getOwner(),
                    "§7所有者タイプ§f: " + krc.getOwnerType().getLabel(),
                    "§7HP: §f" + krc.getHP() + "/" + krc.getMaxHP(),
                    "§a領土メニューへ"
            ));
        } else {
            material = Material.LIME_STAINED_GLASS_PANE;
            inv.setItem(cIndex, item(Material.GRASS_BLOCK, cName,
                    "§7座標§f: " + krc.getCoordinate(),
                    "§7所有者§f: 所有者なし",
                    "§a領土メニューへ"
            ));
        }
        inv = frame(inv, item(material, " "));
        //プレイヤーの情報
        String faction = krp.getFaction();
        if (faction == null) faction = "所属なし";
        inv.setItem(pIndex, item(Material.FLOWER_BANNER_PATTERN, pName,
                "§7名前§f: " + krp.getName(),
                "§7所持金§f: " + krp.getMoney(),
                "§7所有領土数§f: " + krp.getTerritory() + "/" + krp.getMaxTerritory(),
                "§7所属派閥§f: " + faction,
                "§c購入メニューへ"
                ));
        player.openInventory(inv);
    }
    //領土メニュー
    public static void chunk(Player player) {
        Inventory inv = Bukkit.createInventory(null, 9, chunkName);
        KrChunk krc = new KrChunk(player.getLocation().getChunk());
        //詳細
        if (krc.isExists()) {
            inv.setItem(1, item(Material.GRASS_BLOCK, "チャンク詳細:",
                    "§7座標§f: " + krc.getCoordinate(),
                    "§7所有者§f: " + krc.getOwner(),
                    "§7所有者タイプ§f: " + krc.getOwnerType().getLabel(),
                    "§7HP: §f" + krc.getHP() + "/" + krc.getMaxHP()
            ));
        } else {
            inv.setItem(1, item(Material.GRASS_BLOCK, "チャンク詳細:",
                    "§7座標§f: " + krc.getCoordinate(),
                    "§7所有者§f: 所有者なし"
            ));
        }
        //claim, unclaim, (war)
        if (krc.isOwner(player)) {//プレイヤーが所有者
            inv.setItem(3, item(Material.RED_TERRACOTTA, "§c領土の所有権を捨てる。"));
        } else {
            if (krc.isExists()) {//プレイヤー以外が所有者
                inv.setItem(3, item(Material.RED_CONCRETE, "§4宣戦布告する。", "未実装"));
            } else {//所有者なし
                inv.setItem(3, item(Material.LIGHT_BLUE_TERRACOTTA, "§3領土の所有権を主張する。", "§7消費資金§f: " + Main.instance.myConfig().chunkClaim));
            }
        }
        //HP
        String name = "§7HP: §f" + krc.getHP() + "/" + krc.getMaxHP();
        if (krc.isOwner(player)) {
            if (krc.isHPMax()) inv.setItem(5, item(Material.LIME_TERRACOTTA, name, "§a領土のHPは全回復しています。"));
            else inv.setItem(5, item(Material.RED_TERRACOTTA, name, "§2領土のHPを回復させる。", "§7消費資金§f: " + Main.instance.myConfig().chunkRecovery));
        } else {
            if (krc.isExists()) inv.setItem(5, item(Material.CYAN_TERRACOTTA, name));
            else inv.setItem(5, item(Material.CYAN_TERRACOTTA, "HPなし"));
        }
        //メインメニュ－
        inv.setItem(7, item(Material.REDSTONE_LAMP, "§6メインメニューに戻る。"));
        player.openInventory(inv);
    }
    //ショップメニュー
    public static void buy(Player player) {
        Inventory inv = Bukkit.createInventory(null, 9, buyName);
        KrPlayer krp = new KrPlayer(player);
        //領土枠
        inv.setItem(2, item(Material.GRASS_BLOCK, "§2領土枠を購入する。",
                "§7現在の領土枠§f: " + krp.getMaxTerritory(),
                "§7現在の所持金§f: " + krp.getMoney(),
                "§b値段§f: " + Main.instance.myConfig().chunkSlot
        ));
        //領土HP
        inv.setItem(4, item(Material.LIME_TERRACOTTA, "§a領土HPレベルを購入する。",
                "§7現在の領土HPレベル§f: " + krp.getMaxHP(),
                "§7現在の所持金§f: " + krp.getMoney(),
                "§b値段§f: " + Main.instance.myConfig().chunkLevel
        ));
        //メインメニューへ
        inv.setItem(6, item(Material.REDSTONE_LAMP, "メインメニューへ"));
        player.openInventory(inv);
    }

    //フレームセッター
    private static Inventory frame(Inventory inv, ItemStack item) {
        for (int i = 0; i < inv.getSize(); i++) {
            if (i < 10 || i == 17 || i == 18 ||
                    i == 26 || i == 27 ||
                    i == 35 || i == 36 ||
                    i > inv.getSize() - 10
            ) inv.setItem(i, item);
        }
        return inv;
    }
    //アイテム
    private static ItemStack item(Material material, String name, String... lore) {
        ItemStack item = new ItemStack(material);
        ItemMeta meta = item.getItemMeta();
        meta.setDisplayName(name);
        meta.setLore(Arrays.asList(lore));
        item.setItemMeta(meta);
        return item;
    }
}
