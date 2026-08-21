package com.HeadsUpMastersV2.game;

import java.time.Duration;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import com.HeadsUpMastersV2.dto.GameTableModel;
import com.HeadsUpMastersV2.dto.LobbyTableModel;
import com.HeadsUpMastersV2.service.HandMapService;

import lombok.Data;

@Data
public class TableUpdate {

    private final String tableId;
    private List<Card> deck;
    private final long smallBlind;
    private final long bigBlind;
    private final long minbuyIn;
    private final long maxBuyIn;
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

    public TableUpdate(String tableID, long smallBlind, long bigBlind, long minBuyIn, long maxBuyIn) {
        this.tableId = tableID;
        this.smallBlind = smallBlind;
        this.bigBlind = bigBlind;
        this.minbuyIn = minBuyIn;
        this.maxBuyIn = maxBuyIn;
        this.board = new ArrayList<>();
        this.pot = 0;
        this.highestBet = 0;
        this.deck = Deck.createDeck();

        this.minRaise = bigBlind;
        this.buttonPositionIndex = 0;
        this.actionPositionIndex = 0;
        this.availableSeats = Arrays.asList(true, true);
        this.players = new GuestPlayer[2];
        this.isRunning = false;
        this.gameState = GameState.WAITING;
        HandMapService.generateHashTable();
    }

    /**
     * Function to start a new round
     * <br>
     * </br>
     * The function will clean the table for safety, shuffle the deck, and move the
     * dealer button
     */
    public void startRound() {
        clearState();
        collectBlinds();
        actionPositionIndex = buttonPositionIndex;
        gameState = GameState.PREFLOP;
        this.deck = Deck.createDeck();
        DealCards();
    }

    /**
     * Function that will trigger an end of a betting round and reveal the next
     * card/s or go to showdown
     * 
     * @param broadcaster A {@link Runnable} that will broadcast the game state to
     *                    the front end
     */
    public void endBettingRound(Runnable broadcaster) {
        clearBets();
        resetActionIndex();
        minRaise = bigBlind;

        switch (board.size()) {
            case 0:
                getBoard(3);
                setGameState(GameState.FLOP);
                break;
            case 3:
                getBoard(1);
                setGameState(GameState.TURN);
                break;
            case 4:
                getBoard(1);
                setGameState(GameState.RIVER);
                break;
            case 5:
                setGameState(GameState.SHOWDOWN);
                showdown(broadcaster);
                break;
        }
    }

    /**
     * Function to trigger a showdown, after all betting rounds are over, reveal the
     * cards and pay the winner
     * <br>
     * </br>
     * Uses Cactus Kev Algorithm, see {@link HandMapService}
     * 
     * @param broadcaster A {@link Runnable} that will broadcast the game state to
     *                    the front end
     */
    public void showdown(Runnable broadcaster) {
        int[] hand = new int[7];
        int[] suits = new int[7];
        for (int i = 0; i < 5; i++) {
            hand[i] = board.get(i).getStrength();
            suits[i] = board.get(i).getHexSuit();
        }

        for (GuestPlayer player : players) {
            if (player != null && !player.getHand().isEmpty()) {
                hand[5] = player.getHand().get(0).getStrength();
                suits[5] = player.getHand().get(0).getHexSuit();
                hand[6] = player.getHand().get(1).getStrength();
                suits[6] = player.getHand().get(1).getHexSuit();
                player.setHandScore(HandMapService.sevenCardEvaluator(hand, suits));
            }
        }

        if (broadcaster != null)
            broadcaster.run();

        try {
            Thread.sleep(Duration.ofSeconds(4));
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        decideWinner();
        endRound();
    }

    /**
     * Function to trigger a table all-in state
     * <br>
     * </br>
     * This loop handles the automated dealing of remaining board cards and triggers
     * the showdown
     * 
     * @param broadcaster A {@link Runnable} that will broadcast the game state to
     *                    the front end
     */
    public void allIn(ScheduledExecutorService scheduler, Runnable broadcaster) {
        setGameState(GameState.ALL_IN);
        if (broadcaster != null)
            broadcaster.run();

        scheduler.schedule(new Runnable() {
            @Override
            public void run() {
                endBettingRound(broadcaster);
                if (broadcaster != null && gameState != GameState.SHOWDOWN) {
                    broadcaster.run();
                }

                if (gameState != GameState.SHOWDOWN) {
                    scheduler.schedule(this, 2500, TimeUnit.MILLISECONDS);
                }
            }
        }, 2500, TimeUnit.MILLISECONDS);
    }

    /**
     * Function to end the round. This will clear the table and check for game-over
     * conditions
     * <br>
     * </br>
     * If players still have chips and the table is full, it automatically starts a
     * new round
     */
    public void endRound() {
        try {
            Thread.sleep(Duration.ofSeconds(1));
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }

        int readyPlayersCount = 0;
        for (int i = 0; i < players.length; i++) {
            if (players[i] != null) {
                if (players[i].getStack() <= 0.0) {
                    players[i] = null;
                    availableSeats.set(i, true);
                } else {
                    readyPlayersCount++;
                }
            }
        }

        if (readyPlayersCount >= 2) {
            startRound();
        } else {
            setGameState(GameState.WAITING);
        }
    }
    // ==================================================================================
    // Seat Management Functions

    /**
     * Checks if a seat is free
     * 
     * @param seat the seat Id
     * @return a boolean, true / false depending on the seat availability
     */
    public boolean checkSeatAvailability(int seat) {
        if (seat > 1 || seat < 0) {
            return false;
        }
        return players[seat] == null;
    }

    public void seatPlayer(GuestPlayer player) {
        availableSeats.set(player.getSeat(), false);
        players[player.getSeat()] = player;
    }

    private boolean isTableFull() {
        int count = 0;
        for (int i = 0; i < 2; i++) {
            if (players[i] != null)
                count++;
        }
        return count == 2;
    }

    /**
     * Removes a player from the table and resets the table state to WAITING
     * 
     * @param player the {@link GuestPlayer} who is standing up
     */
    public void removePlayer(GuestPlayer player) {
        if (player != null) {
            availableSeats.set(player.getSeat(), true);
            players[player.getSeat()] = null;
        }
        gameState = GameState.WAITING;
    }

    // ==================================================================================


    // ==================================================================================
    // DTO Conversion


    public LobbyTableModel getLobbyTableDTO(){
        return new LobbyTableModel(tableId,
            smallBlind, 
            bigBlind,
            minbuyIn, 
            maxBuyIn, 
            buttonPositionIndex, 
            availableSeats, 
            players, 
            gameState);
    }


    public GameTableModel getGameTableDTO(){
        return new GameTableModel(tableId, 
            board, 
            pot, 
            highestBet, 
            minRaise, 
            buttonPositionIndex, 
            actionPositionIndex, 
            players, 
            gameState);
    }


















    // ==================================================================================


    private void decideWinner() {
        if (players[0] != null && players[1] != null) {
            if (players[0].getHandScore() > players[1].getHandScore()) {
                awardPot(players[0]);
            } else if (players[0].getHandScore() < players[1].getHandScore()) {
                awardPot(players[1]);
            } else {
                splitPot();
            }
        } else if (players[0] != null) {
            awardPot(players[0]);
        } else if (players[1] != null) {
            awardPot(players[1]);
        }
    }

    private void splitPot() {
        long splitAmount = pot / 2;
        players[0].setStack(players[0].getStack() + splitAmount);
        players[1].setStack(players[1].getStack() + splitAmount);
    }

    private void awardPot(GuestPlayer guestPlayer) {
        guestPlayer.setStack(guestPlayer.getStack() + pot);
        this.pot = 0;
    }

    public void clearState() {
        // Clear players hands and reset their bets
        for (GuestPlayer player : getPlayers()) {
            if (player != null)
                player.clearHand();
        }

        // Clear the board and reset the pot and highest bet
        board.clear();

        // Move the dealer button
        int playersAtTable = players.length;
        if (playersAtTable < 2)
            return;

        if (buttonPositionIndex == 0) {
            buttonPositionIndex = 1;
        } else {
            buttonPositionIndex = 0;
        }

    }

    /**
     * Function to clear the bets and actions of all players
     */
    public void clearBets() {
        for (GuestPlayer player : players) {
            if (player != null) {
                player.setBet(0);
                // player.setAction(null);
            }
        }
        highestBet = 0;
    }

    public void collectBlinds() {

        if (getSmallBlindPlayer() != null) {
            getSmallBlindPlayer().bet(smallBlind);
            this.pot += smallBlind;
        }
        if (getBigBlindPlayer() != null) {
            getBigBlindPlayer().bet(bigBlind);
            this.pot += bigBlind;
        }
        highestBet = bigBlind;
        minRaise = bigBlind;
    }

    /**
     * Returns the Small Blind player based on button position
     * 
     * @return {@link GuestPlayer} in the SB seat
     */
    public GuestPlayer getSmallBlindPlayer() {
        if (players == null || players.length == 0)
            return null;
        if (players.length == 2) {
            return players[buttonPositionIndex];
        }

        return players[0];
    }

    /**
     * Returns the Big Blind player based on button position
     * 
     * @return {@link GuestPlayer} in the BB seat
     */
    public GuestPlayer getBigBlindPlayer() {
        if (players == null || players.length == 0)
            return null;
        if (players.length == 2) {
            return players[(buttonPositionIndex + 1) % 2];
        }
        return players[1];
    }

    /**
     * Resets the action to the player who is not the dealer for post-flop play
     */
    private void resetActionIndex() {
        actionPositionIndex = (buttonPositionIndex + 1) % 2;
    }

    public void getBoard(int numberOfCards) {
        for (int i = 0; i < numberOfCards; i++) {
            if (!deck.isEmpty())
                board.add(deck.remove(0));
        }
    }

    /**
     * Distributes cards to players from the top of the deck
     * 
     * @param deck the current deck list
     */
    public void DealCards() {
        for (int i = 0; i < 2; i++) {
            for (GuestPlayer player : players) {
                if (player != null && !deck.isEmpty()) {
                    player.giveCard(deck.remove(0));
                }
            }
        }
    }

}
