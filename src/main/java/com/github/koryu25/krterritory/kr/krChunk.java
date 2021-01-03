package com.github.koryu25.krterritory.kr;

import com.github.koryu25.krterritory.Main;
import com.github.koryu25.krterritory.kr.enums.OwnerType;
import org.bukkit.Chunk;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class krChunk {

    //InstanceField
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
    public krChunk(Chunk chunk) {
        this.chunk = chunk;
        this.coordinate = chunk.getX() + "," + chunk.getZ();
    }

    //Insert
    public void insert(String owner, OwnerType ownerType, int hitPoint) {
        if (isExists()) return;
        Main.instance.mysql().insertTerritory(coordinate, owner, ownerType.name(), hitPoint);
    }
    //Delete
    public void delete() {
        if (!isExists()) return;
        Main.instance.mysql().delete("territory", "coordinate", coordinate);
    }

    //isExists
    public boolean isExists() {
        return Main.instance.mysql().exists("territory", "coordinate", coordinate);
    }
    //attacked
    public boolean attacked(Player attacker, int damage) {
        if (!isExists()) return false;
        switch (getOwnerType()) {
            case Player:
                String uuid = attacker.getUniqueId().toString();
                if (getOwner().equals(uuid)) return false;
                //HP確認
                if (getHitPoint() == 1) {
                    setOwner(uuid);
                    setHitPoint(Main.instance.myConfig().chunkHP);
                } else {
                    setHitPoint(getHitPoint() - damage);
                }
                return true;
            case Faction:
            case NPC:
        }
        return false;
    }

    //Getter
    public Chunk getChunk() {
        return chunk;
    }
    public String getOwner() {
        return Main.instance.mysql().selectString("territory", "owner", "coordinate", coordinate);
    }
    public OwnerType getOwnerType() {
        return OwnerType.valueOf(Main.instance.mysql().selectString("territory", "owner_type", "coordinate", coordinate));
    }
    public int getHitPoint() {
        return Main.instance.mysql().selectInt("territory", "hit_point", "coordinate", coordinate);
    }
    //Setter
    public void setOwner(String owner) {
        Main.instance.mysql().update("territory", "owner", owner, "coordinate", coordinate);
    }
    public void setOwnerType(OwnerType ownerType) {
        Main.instance.mysql().update("territory", "owner_type", ownerType.name(), "coordinate", coordinate);
    }
    public void setHitPoint(int hitPoint) {
        Main.instance.mysql().update("territory", "hit_point", hitPoint, "coordinate", coordinate);
    }
}
