package com.github.koryu25.krterritory;

import com.github.koryu25.krterritory.file.Messenger;
import com.github.koryu25.krterritory.file.MyConfig;
import com.github.koryu25.krterritory.listener.PlayerJoinListener;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

public final class Main extends JavaPlugin {

    private MyConfig myConfig;
    private Messenger messenger;
    private MySQLManager mysql;

    @Override
    public void onEnable() {
        //Config
        myConfig = new MyConfig(this);
        //Messenger
        messenger = new Messenger(this);
        //MySQL
        mysql = new MySQLManager(this, myConfig.host, myConfig.port, myConfig.database, myConfig.username, myConfig.password);
        if (!mysql.connectionTest()) {
            Bukkit.getLogger().severe(messenger.getMsg("MySQL.ConnectionFailure"));
            Bukkit.shutdown();
        }
        //Command
        new CommandManager(this);
        //Listener
        new PlayerJoinListener(this);
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }

    public MyConfig myConfig() {
        return this.myConfig;
    }
    public MySQLManager mysql() {
        return this.mysql;
    }
    public Messenger messenger() {
        return this.messenger;
    }
}
