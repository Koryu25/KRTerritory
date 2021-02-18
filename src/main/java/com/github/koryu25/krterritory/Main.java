package com.github.koryu25.krterritory;

import com.github.koryu25.krterritory.file.MyConfig;
import com.github.koryu25.krterritory.kr.KrPlayer;
import com.github.koryu25.krterritory.listener.*;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

public final class Main extends JavaPlugin {

    public static Main instance;

    private MyConfig myConfig;
    private MySQLManager mysql;

    @Override
    public void onEnable() {
        //Instance
        instance = this;
        //Config
        myConfig = new MyConfig();
        //MySQL
        mysql = new MySQLManager(myConfig.host, myConfig.port, myConfig.database, myConfig.username, myConfig.password);
        //Command
        new CommandManager();
        //Listener
        //Player
        new PlayerJoinListener();
        new PlayerQuitListener();
        new PlayerMoveListener();
        //Protect
        new PlayerInteractListener();
        new BlockBreakListener();
        new EntityDamageByEntityListener();
        //Menu
        new InventoryClickListener();
    }

    @Override
    public void onDisable() {
        //LastDate
        Bukkit.getOnlinePlayers().forEach(player -> new KrPlayer(player).setLastDate(MySQLManager.now()));
    }

    public MyConfig myConfig() {
        return myConfig;
    }
    public MySQLManager mysql() {
        return mysql;
    }
}
