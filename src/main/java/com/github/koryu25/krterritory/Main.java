package com.github.koryu25.krterritory;

import com.github.koryu25.krterritory.file.Messenger;
import com.github.koryu25.krterritory.file.MyConfig;
import com.github.koryu25.krterritory.listener.BlockBreakListener;
import com.github.koryu25.krterritory.listener.InventoryClickListener;
import com.github.koryu25.krterritory.listener.PlayerJoinListener;
import com.github.koryu25.krterritory.listener.PlayerMoveListener;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

public final class Main extends JavaPlugin {

    public static Main instance;

    private MyConfig myConfig;
    private Messenger messenger;
    private MySQLManager mysql;

    @Override
    public void onEnable() {
        //Instance
        instance = this;
        //Config
        myConfig = new MyConfig();
        //Messenger
        messenger = new Messenger();
        //MySQL
        mysql = new MySQLManager(myConfig.host, myConfig.port, myConfig.database, myConfig.username, myConfig.password);
        if (!mysql.connectionTest()) {
            Bukkit.getLogger().severe(messenger.getMsg("MySQL.ConnectionFailure"));
            Bukkit.shutdown();
        }
        //Command
        new CommandManager();
        //Listener
        new PlayerJoinListener();
        new PlayerMoveListener();
        new BlockBreakListener();
        new InventoryClickListener();
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }

    public MyConfig myConfig() {
        return myConfig;
    }
    public MySQLManager mysql() {
        return mysql;
    }
    public Messenger messenger() {
        return messenger;
    }
}
