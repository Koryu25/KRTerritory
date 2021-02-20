package com.github.koryu25.krterritory.listener;

import com.github.koryu25.krterritory.Main;
import com.github.koryu25.krterritory.kr.KrChunk;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;

import java.util.ArrayList;
import java.util.List;

public class BlockBreakListener implements Listener {

    private static List<Material> bannedMaterials = new ArrayList<>();

    public BlockBreakListener() {
        Main.instance.getServer().getPluginManager().registerEvents(this, Main.instance);
        bannedMaterials.add(Material.WHEAT);
        bannedMaterials.add(Material.BEETROOT);
        bannedMaterials.add(Material.PUMPKIN_SEEDS);
        bannedMaterials.add(Material.MELON_SEEDS);
        bannedMaterials.add(Material.CARROT);
        bannedMaterials.add(Material.POTATO);
        bannedMaterials.add(Material.OAK_SAPLING);
        bannedMaterials.add(Material.SPRUCE_SAPLING);
        bannedMaterials.add(Material.BIRCH_SAPLING);
        bannedMaterials.add(Material.JUNGLE_SAPLING);
        bannedMaterials.add(Material.ACACIA_SAPLING);
        bannedMaterials.add(Material.DARK_OAK_SAPLING);
        bannedMaterials.add(Material.GRASS);
        bannedMaterials.add(Material.FERN);
        bannedMaterials.add(Material.DEAD_BUSH);
        bannedMaterials.add(Material.SEAGRASS);
        bannedMaterials.add(Material.SEA_PICKLE);
        bannedMaterials.add(Material.DANDELION);
        bannedMaterials.add(Material.POPPY);
        bannedMaterials.add(Material.BLUE_ORCHID);
        bannedMaterials.add(Material.ALLIUM);
        bannedMaterials.add(Material.AZURE_BLUET);
        bannedMaterials.add(Material.RED_TULIP);
        bannedMaterials.add(Material.ORANGE_TULIP);
        bannedMaterials.add(Material.WHITE_TULIP);
        bannedMaterials.add(Material.PINK_TULIP);
        bannedMaterials.add(Material.OXEYE_DAISY);
        bannedMaterials.add(Material.CORNFLOWER);
        bannedMaterials.add(Material.LILY_OF_THE_VALLEY);
        bannedMaterials.add(Material.WITHER_ROSE);
        bannedMaterials.add(Material.BROWN_MUSHROOM);
        bannedMaterials.add(Material.RED_MUSHROOM);
        bannedMaterials.add(Material.CRIMSON_FUNGUS);
        bannedMaterials.add(Material.WARPED_FUNGUS);
        bannedMaterials.add(Material.CRIMSON_ROOTS);
        bannedMaterials.add(Material.WARPED_ROOTS);
        bannedMaterials.add(Material.NETHER_SPROUTS);
        bannedMaterials.add(Material.KELP);
        bannedMaterials.add(Material.SNOW);
        bannedMaterials.add(Material.TORCH);
        bannedMaterials.add(Material.SOUL_TORCH);
        bannedMaterials.add(Material.LILY_PAD);
        bannedMaterials.add(Material.SLIME_BLOCK);
        bannedMaterials.add(Material.SUNFLOWER);
        bannedMaterials.add(Material.LILAC);
        bannedMaterials.add(Material.ROSE_BUSH);
        bannedMaterials.add(Material.PEONY);
        bannedMaterials.add(Material.TALL_GRASS);
        bannedMaterials.add(Material.LARGE_FERN);
        bannedMaterials.add(Material.TUBE_CORAL);
        bannedMaterials.add(Material.BRAIN_CORAL);
        bannedMaterials.add(Material.BUBBLE_CORAL);
        bannedMaterials.add(Material.FIRE_CORAL);
        bannedMaterials.add(Material.HORN_CORAL);
        bannedMaterials.add(Material.DEAD_BRAIN_CORAL);
        bannedMaterials.add(Material.DEAD_BUBBLE_CORAL);
        bannedMaterials.add(Material.DEAD_FIRE_CORAL);
        bannedMaterials.add(Material.DEAD_HORN_CORAL);
        bannedMaterials.add(Material.DEAD_TUBE_CORAL);
        bannedMaterials.add(Material.TUBE_CORAL_FAN);
        bannedMaterials.add(Material.BRAIN_CORAL_FAN);
        bannedMaterials.add(Material.BUBBLE_CORAL_FAN);
        bannedMaterials.add(Material.FIRE_CORAL_FAN);
        bannedMaterials.add(Material.HORN_CORAL_FAN);
        bannedMaterials.add(Material.DEAD_TUBE_CORAL_FAN);
        bannedMaterials.add(Material.DEAD_BRAIN_CORAL_FAN);
        bannedMaterials.add(Material.DEAD_BUBBLE_CORAL_FAN);
        bannedMaterials.add(Material.DEAD_FIRE_CORAL_FAN);
        bannedMaterials.add(Material.DEAD_HORN_CORAL_FAN);
        bannedMaterials.add(Material.SCAFFOLDING);
        bannedMaterials.add(Material.PAINTING);//絵画
        bannedMaterials.add(Material.ITEM_FRAME);//額縁
        bannedMaterials.add(Material.FLOWER_POT);
        bannedMaterials.add(Material.REDSTONE_TORCH);
        bannedMaterials.add(Material.REDSTONE);
    }

    @EventHandler
    public void onBlockBreak(BlockBreakEvent e) {
        //ブロックのMaterial取得用
        if (e.getPlayer().getName().equals("Koryu25")) e.getPlayer().sendMessage("破壊したブロックのMaterial→" + e.getBlock().getType().name());
        e.setCancelled(new KrChunk(e.getBlock().getChunk()).onAttacked(e.getPlayer(), e.getBlock(), 1));
    }

    public static List<Material> getBannedMaterial() {
        return bannedMaterials;
    }
}
