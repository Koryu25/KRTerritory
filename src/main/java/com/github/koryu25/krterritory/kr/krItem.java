package com.github.koryu25.krterritory.kr;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;

public class krItem {

    public static ItemStack getWarTool() {
        ItemStack item = new ItemStack(Material.STICK);
        ItemMeta meta = item.getItemMeta();
        meta.setDisplayName("War Tool");
        List<String> lore = new ArrayList<>();
        lore.add("このアイテムで敵領土を攻撃できます。");
        meta.setLore(lore);
        item.setItemMeta(meta);
        return item;
    }
}
