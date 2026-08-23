package com.HeadsUpMastersV2.dto;
import lombok.Data;


@Data
public class LobbyTableModel {
    private final String tableId;
    private final long smallBlind;
    private final long bigBlind;
    private final long minbuyIn;
    private final long maxBuyIn;
    private final int playerCount;


    public LobbyTableModel(
        String tableId, 
        long smallBlind, 
        long bigBlind, 
        long minBuyIn, 
        long maxBuyIn
        )
        {
        this.tableId = tableId;
        this.smallBlind = smallBlind;
        this.bigBlind = bigBlind;
        this.minbuyIn = minBuyIn;
        this.maxBuyIn = maxBuyIn;
        this.playerCount = 0;
    }

}