package com.github.koryu25.krterritory.file;

import com.github.koryu25.krterritory.Main;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.configuration.file.FileConfiguration;

import java.util.List;

public class MyConfig {

    private final Main plugin;
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

    //Constructor
    public MyConfig(Main plugin) {
        this.plugin = plugin;
        this.load();
    }
    private void load() {
        //ファイルが存在しなければデフォルトを保存
        plugin.saveDefaultConfig();
        if (config != null) plugin.reloadConfig();
        //ファイルの取得
        config = plugin.getConfig();

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
        chunkHP = config.getInt("ChunkHP");
    }
}
