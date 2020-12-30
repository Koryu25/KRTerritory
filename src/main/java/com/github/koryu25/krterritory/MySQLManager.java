package com.github.koryu25.krterritory;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class MySQLManager {

    //InstanceField
    private final Main plugin;
    private Connection connection;
    private final String host;
    private final int port;
    private final String database;
    private final String username;
    private final String password;

    //Constructor
    public MySQLManager(Main plugin, String host, int port, String database, String username, String password) {
        this.plugin = plugin;
        this.host = host;
        this.database = database;
        this.username = username;
        this.password = password;
        this.port = port;
        if (!connectionTest()) {
            plugin.getLogger().severe(plugin.messenger().getMsg("MySQL.ConnectionFailure"));
            Bukkit.shutdown();
        }
    }
    public Boolean connectionTest() {
        try {
            openConnection();
            return true;
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
            return false;
        }
    }
    private void openConnection() throws SQLException, ClassNotFoundException {
        if (connection != null && !connection.isClosed()) return;
        synchronized (this) {
            if (connection != null && !connection.isClosed())return;
            Class.forName("com.mysql.jdbc.Driver");
            connection = DriverManager.getConnection("jdbc:mysql://"+ host +":"+ port +"/"+ database, username, password);
        }
    }

    //レコード登録
    public void insertPlayer(String name, String uuid) {
        try {
            openConnection();
            String s = "INSERT INTO player (name, uuid) VALUES (?, ?)";
            PreparedStatement ps = connection.prepareStatement(s);
            ps.setString(1, name);
            ps.setString(2, uuid);
            ps.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public void insertTerritory(String coordinate, String owner, String ownerType, int point) {
        try {
            openConnection();
            String s = "INSERT INTO territory (coordinate, owner, owner_type, hit_point) VALUES (?, ?, ?, ?)";
            PreparedStatement ps = connection.prepareStatement(s);
            ps.setString(1, coordinate);
            ps.setString(2, owner);
            ps.setString(3, ownerType);
            ps.setInt(4, point);
            ps.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    //レコード削除
    public void delete(String table, String column, String value) {
        try {
            openConnection();
            String s = "DELETE FROM "+ table +" WHERE "+ column +" = ?";
            PreparedStatement ps = connection.prepareStatement(s);
            ps.setString(1, value);
            ps.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    //存在確認
    public boolean exists(String table, String where, String value) {
        try {
            openConnection();
            String s = "SELECT * FROM "+ table +" WHERE "+ where +" = ? ";
            PreparedStatement ps = connection.prepareStatement(s);
            ps.setString(1, value);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) return true;
            else return false;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
    public boolean exists(String table, String where, int value) {
        try {
            openConnection();
            String s = "SELECT * FROM "+ table +" WHERE "+ where +" = ? ";
            PreparedStatement ps = connection.prepareStatement(s);
            ps.setInt(1, value);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) return true;
            else return false;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
    //値取得
    public String selectString(String table, String column, String where, String value) {
        try {
            openConnection();
            String  s = "SELECT * FROM "+ table +" WHERE "+ where +" = ?";
            PreparedStatement ps = connection.prepareStatement(s);
            ps.setString(1, value);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return rs.getString(column);
            }
            return null;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
    public List<String> selectStringList(String table, String column, String where, String value) {
        try {
            openConnection();
            String  s = "SELECT * FROM "+ table +" WHERE "+ where +" = ?";
            PreparedStatement ps = connection.prepareStatement(s);
            ps.setString(1, value);
            ResultSet rs = ps.executeQuery();
            List<String> list = new ArrayList<>();
            while (rs.next()) {
                list.add(rs.getString(column));
            }
            return list;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
    public int selectInt(String table, String column, String where, String value) {
        try {
            openConnection();
            String  s = "SELECT * FROM "+ table +" WHERE "+ where +" = ?";
            PreparedStatement ps = connection.prepareStatement(s);
            ps.setString(1, value);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return rs.getInt(column);
            }
            return 0;
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
    }
    public boolean selectBoolean(String table, String column, String where, String value) {
        try {
            openConnection();
            String  s = "SELECT * FROM "+ table +" WHERE "+ where +" = ?";
            PreparedStatement ps = connection.prepareStatement(s);
            ps.setString(1, value);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                int i = rs.getInt(column);
                if (i == 0) return false;
                else if (i == 1) return true;
            }
            return false;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
    public int selectQuantity(String table, String where, String value) {
        try {
            openConnection();
            String  s = "SELECT * FROM "+ table +" WHERE "+ where +" = ?";
            PreparedStatement ps = connection.prepareStatement(s);
            ps.setString(1, value);
            ResultSet rs = ps.executeQuery();
            int quantity = 0;
            while (rs.next()) {
                quantity++;
            }
            return quantity;
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
    }
    //値更新
    public void update(String table, String update_column, String update_value, String where_column, String where_value) {
        try {
            openConnection();
            String queryString = "UPDATE "+ table +" SET "+ update_column +" = ? WHERE "+ where_column +" = ?";
            PreparedStatement ps = connection.prepareStatement(queryString);
            ps.setString(1, update_value);
            ps.setString(2, where_value);
            ps.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public void update(String table, String update_column, int update_value, String where_column, String where_value) {
        try {
            openConnection();
            String queryString = "UPDATE "+ table +" SET "+ update_column +" = ? WHERE "+ where_column +" = ?";
            PreparedStatement ps = connection.prepareStatement(queryString);
            ps.setInt(1, update_value);
            ps.setString(2, where_value);
            ps.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public void update(String table, String update_column, String update_value, String where_column, int where_value) {
        try {
            openConnection();
            String queryString = "UPDATE "+ table +" SET "+ update_column +" = ? WHERE "+ where_column +" = ?";
            PreparedStatement ps = connection.prepareStatement(queryString);
            ps.setString(1, update_value);
            ps.setInt(2, where_value);
            ps.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public void update(String table, String update_column, int update_value, String where_column, int where_value) {
        try {
            openConnection();
            String queryString = "UPDATE "+ table +" SET "+ update_column +" = ? WHERE "+ where_column +" = ?";
            PreparedStatement ps = connection.prepareStatement(queryString);
            ps.setInt(1, update_value);
            ps.setInt(2, where_value);
            ps.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
