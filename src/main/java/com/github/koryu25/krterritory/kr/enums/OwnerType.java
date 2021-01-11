package com.github.koryu25.krterritory.kr.enums;

public enum OwnerType {
    Faction("派閥"),
    Gathering("採集エリア"),
    NPC_Player("NPCプレイヤー"),
    NPC_Faction("NPC派閥"),
    Player("プレイヤー");

    private String label;

    OwnerType(String label) {
        this.label = label;
    }
    public String getLabel() {
        return label;
    }
}
