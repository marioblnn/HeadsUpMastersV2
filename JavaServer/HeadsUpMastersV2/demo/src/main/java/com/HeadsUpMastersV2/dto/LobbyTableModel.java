package com.HeadsUpMastersV2.dto;



import java.util.List;
import com.HeadsUpMastersV2.game.GameState;
import com.HeadsUpMastersV2.game.GuestPlayer;
import lombok.Data;


@Data
public class LobbyTableModel {
    private final String tableId;
    private final long smallBlind;
    private final long bigBlind;
    private final long minbuyIn;
    private final long maxBuyIn;
    private int buttonPositionIndex;
    private List<Boolean> availableSeats;
    private GuestPlayer[] players;
    private GameState gameState;

    public LobbyTableModel(String tableId, 
        long smallBlind, 
        long bigBlind, 
        long minBuyIn, 
        long maxBuyIn, 
        int buttonPositionIndex, 
        List<Boolean> availableSeats, 
        GuestPlayer[] players, 
        GameState gameState)
        {
        this.tableId = tableId;
        this.smallBlind = smallBlind;
        this.bigBlind = bigBlind;
        this.minbuyIn = minBuyIn;
        this.maxBuyIn = maxBuyIn;
        this.buttonPositionIndex = buttonPositionIndex;
        this.availableSeats = availableSeats;
        this.players = players;
        this.gameState = gameState;
    }

}