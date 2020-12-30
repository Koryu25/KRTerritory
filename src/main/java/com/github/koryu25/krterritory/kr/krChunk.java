package com.github.koryu25.krterritory.kr;

import com.github.koryu25.krterritory.Main;
import com.github.koryu25.krterritory.kr.enums.OwnerType;
import org.bukkit.Chunk;

public class krChunk {

    //InstanceField
    private final Main plugin;
    //Chunk
    private final Chunk chunk;
    //座標
    private final String coordinate;
    //所有者
    private String owner;
    //所有者タイプ
    private OwnerType ownerType;
    //ヒットポイント
    private int hitPoint;
    //データに存在するか
    private boolean exists;

    //Constructor
    public krChunk(Main plugin, Chunk chunk) {
        this.plugin = plugin;
        this.chunk = chunk;
        this.coordinate = chunk.getX() + "," + chunk.getZ();
    }

    //Insert
    public void insert(String owner, OwnerType ownerType, int hitPoint) {
        if (isExists()) return;
        plugin.mysql().insertTerritory(coordinate, owner, ownerType.name(), hitPoint);
    }
    //Delete
    public void delete() {
        if (!isExists()) return;
        plugin.mysql().delete("territory", "coordinate", coordinate);
    }

    //isExists
    public boolean isExists() {
        return plugin.mysql().exists("territory", "coordinate", coordinate);
    }

    //Getter
    public Chunk getChunk() {
        return chunk;
    }
    public String getOwner() {
        return plugin.mysql().selectString("territory", "owner", "coordinate", coordinate);
    }
    public OwnerType getOwnerType() {
        return OwnerType.valueOf(plugin.mysql().selectString("territory", "owner_type", "coordinate", coordinate));
    }
    public int getHitPoint() {
        return plugin.mysql().selectInt("territory", "hit_point", "coordinate", coordinate);
    }
    //Setter
    public void setOwner(String owner) {
        plugin.mysql().update("territory", "owner", owner, "coordinate", coordinate);
    }
    public void setOwnerType(OwnerType ownerType) {
        plugin.mysql().update("territory", "owner_type", ownerType.name(), "coordinate", coordinate);
    }
    public void setPoint(int hitPoint) {
        plugin.mysql().update("territory", "point", hitPoint, "coordinate", coordinate);
    }
}
