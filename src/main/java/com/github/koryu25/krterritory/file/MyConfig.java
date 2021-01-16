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
    //領土枠の値段
    public int chunkSlot;
    //領土のHPlevelの値段
    public int chunkLevel;
    //所持金上限
    public int moneyLimit;
    //領土主張金
    public int moneyClaim;

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
        //領土枠の値段
        chunkSlot = config.getInt("Chunk.Slot");
        //領土のHPLevelの値段
        chunkLevel = config.getInt("Chunk.Level");
        //所持金上限
        moneyLimit = config.getInt("Money.Limit");
        //領土主張金
        moneyClaim = config.getInt("Money.Claim");
    }
}
