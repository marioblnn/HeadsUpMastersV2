package com.HeadsUpMastersV2.dto;


import com.HeadsUpMastersV2.game.GameState;
import com.HeadsUpMastersV2.game.GuestPlayer;
import com.HeadsUpMastersV2.game.Card;
import java.util.List;
import lombok.Data;

@Data
public class GameTableModel {
    private final String tableId;
    private List<Card> board;
    private long pot;
    private long highestBet;
    private long minRaise;
    private int buttonPositionIndex;
    private int actionPositionIndex;
    private List<Boolean> availableSeats;
    private GuestPlayer[] players;
    private GameState gameState;
    private boolean isRunning;


    public GameTableModel(
        String tableId, 
        List<Card> board, 
        long pot, 
        long highestBet, 
        long minRaise, 
        int buttonPositionIndex, 
        int actionPositionIndex, 
        GuestPlayer[] players, 
        GameState gameState)
        {
        this.tableId = tableId;
        this.board = board;
        this.pot = pot;
        this.highestBet = highestBet;
        this.minRaise = minRaise;
        this.buttonPositionIndex = buttonPositionIndex;
        this.actionPositionIndex = actionPositionIndex;
        this.players = players;
        this.gameState = gameState;
    }
}
