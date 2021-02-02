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

    //派閥作成資金
    public int factionCreate;
    //派閥の使用禁止名
    public List<String> factionBannedName;
    //派閥メンバー枠値段
    public int factionSlot;
    //派閥最大メンバー数
    public int factionMember;
    //派閥最大味方派閥数
    public int factionAlly;
    //派閥最大敵派閥数
    public int factionEnemy;

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

        //派閥作成資金
        factionCreate = config.getInt("FactionCreate");
        //派閥の使用禁止名
        factionBannedName = config.getStringList("FactionBannedName");
        //派閥メンバー枠値段
        factionSlot = config.getInt("FactionSlot");
        //派閥最大メンバー数
        factionMember = config.getInt("FactionMember");
        //派閥最大味方派閥数
        factionAlly = config.getInt("FactionAlly");
        //派閥最大敵派閥数
        factionEnemy = config.getInt("FactionEnemy");

        //使用ワールド名
        world = Bukkit.getWorld(config.getString("WorldName"));

        //初期チャンクHP
        chunkHP = config.getInt("ChunkHP");
        //領土のHPLevelの値段
        chunkLevel = config.getInt("ChunkLevel");
        //上の最大値
        chunkMaxLevel = config.getInt("ChunkMaxLevel");
        //HP回復の値段
        chunkRecovery = config.getInt("ChunkRecovery");
        //領土枠の値段
        chunkSlot = config.getInt("ChunkSlot");
        //上の最大値
        chunkMaxSlot = config.getInt("ChunkMaxSlot");
        //領土主張金
        chunkClaim = config.getInt("ChunkClaim");
    }
}
