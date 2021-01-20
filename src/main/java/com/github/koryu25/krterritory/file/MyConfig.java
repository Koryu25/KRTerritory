package com.github.koryu25.krterritory.file;

import com.github.koryu25.krterritory.Main;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.configuration.file.FileConfiguration;

import java.util.List;

public class MyConfig {

    private FileConfiguration config;

    //MySQL
    public String host;
    public int port;
    public String username;
    public String password;
    public String database;
    //派閥の使用禁止名
    public List<String> bannedname;
    //使用ワールド
    public World world;
    //初期チャンクHP
    public int chunkHP;
    //領土のHPlevelの値段
    public int chunkLevel;
    //上の最大値
    public int chunkMaxLevel;
    //HP回復の値段
    public int chunkRecovery;
    //領土枠の値段
    public int chunkSlot;
    //上の最大値
    public int chunkMaxSlot;
    //領土主張金
    public int chunkClaim;

    //Constructor
    public MyConfig() {
        load();
    }
    private void load() {
        //ファイルが存在しなければデフォルトを保存
        Main.instance.saveDefaultConfig();
        if (config != null) Main.instance.reloadConfig();
        //ファイルの取得
        config = Main.instance.getConfig();
        //以下でロード
        //MySQL
        host = config.getString("MySQL.host");
        port = config.getInt("MySQL.port");
        username = config.getString("MySQL.username");
        password = config.getString("MySQL.password");
        database = config.getString("MySQL.database");
        //派閥の使用禁止名
        bannedname = config.getStringList("BannedName");
        //使用ワールド名
        world = Bukkit.getWorld(config.getString("WorldName"));
        //初期チャンクHP
        chunkHP = config.getInt("Chunk.HP");
        //領土のHPLevelの値段
        chunkLevel = config.getInt("Chunk.Level");
        //上の最大値
        chunkMaxLevel = config.getInt("Chunk.MaxLevel");
        //HP回復の値段
        chunkRecovery = config.getInt("Chunk.Recovery");
        //領土枠の値段
        chunkSlot = config.getInt("Chunk.Slot");
        //上の最大値
        chunkMaxSlot = config.getInt("Chunk.MaxSlot");
        //領土主張金
        chunkClaim = config.getInt("Chunk.Claim");
    }
}
