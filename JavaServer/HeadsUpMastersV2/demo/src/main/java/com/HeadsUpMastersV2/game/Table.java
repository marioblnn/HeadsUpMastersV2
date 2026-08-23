package com.HeadsUpMastersV2.game;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;

import com.HeadsUpMastersV2.service.HandMapService;

/**
 * Represents a Poker Table
 */
public class Table {
    private final String tableID;
    private List<Card> deck;
    private final int smallBlind;
    private final int bigBlind;
    private List<Card> board;
    private double pot;
    private double highestBet;
    private double minRaise;
    private int buttonPositionIndex;
    private int actionPositionIndex;
    private List<Boolean> availableSeats;
    private Player[] players;
    private GameState gameState;
    private boolean isRunning;


    /**
     * The constructor for a poker table
     * @param tableID the table unique Id, the table will be identified by its ID
     * @param smallBlind the table small blind
     * @param bigBlind the table Big Blind
     */
    public Table(String tableID, int smallBlind, int bigBlind) {
        this.tableID = tableID;
        this.smallBlind = smallBlind;
        this.bigBlind = bigBlind;
        this.board = new ArrayList<>();
        this.pot = 0;
        this.highestBet = 0;
        this.deck = Deck.createDeck();

        
        this.minRaise = bigBlind;
        this.buttonPositionIndex = 0;
        this.actionPositionIndex = 0;
        this.availableSeats = new ArrayList<>(List.of(true, true));
        this.players = new Player[2];
        this.isRunning = false;
        this.gameState = GameState.WAITING;
        HandMapService.generateHashTable();
    }



    /**
     * Function to start a new round
     * <br> </br>
     * The function will clean the table for safety, shuffle the deck, and move the dealer button
     */
    public void startRound(){
        clearHands();
        clearBoard();
        moveButton();
        collectBlinds();
        actionPositionIndex = buttonPositionIndex; 
        gameState = GameState.PREFLOP;
        this.deck = Deck.createDeck();
        DealCards(deck);
    }
    

    /**
     * Function to trigger a table all-in state
     * <br> </br>
     * This loop handles the automated dealing of remaining board cards and triggers the showdown
     * @param broadcaster A {@link Runnable} that will broadcast the game state to the front end 
     */
    public void allIn(Runnable broadcaster){
        changeGameState(GameState.ALL_IN);
        if(broadcaster != null) broadcaster.run();
        
        while (!gameState.equals(GameState.SHOWDOWN)) {
            try{
                Thread.sleep(Duration.ofMillis(2500));
            } catch (Exception e){
                System.out.println(e.getMessage());
            }
            
            if(board.size() == 5 && !gameState.equals(GameState.SHOWDOWN)) {
                changeGameState(GameState.SHOWDOWN);
                showdown(broadcaster);
                break;
            }
            endBettingRound(broadcaster);
            if(broadcaster != null && !gameState.equals(GameState.SHOWDOWN)) broadcaster.run();
        }
    }

    /**
     * Automatically extracts blinds from the Small Blind and Big Blind players
     * <br> </br>
     * Updates the pot and sets the initial betting floor for the pre-flop round
     */
    public void collectBlinds(){
        Player sb = getSmallBlindPlayer();
        Player bb = getBigBlindPlayer();
        
        if(sb != null) {
            sb.bet(smallBlind);
            addPot(smallBlind); 
        }
        if(bb != null) {
            bb.bet(bigBlind);
            addPot(bigBlind);
        }
        highestBet = bigBlind;
        minRaise = bigBlind; 
    }

    
    /**
     * Function to trigger a showdown, after all betting rounds are over, reveal the cards and pay the winner
     * <br> </br>
     * Uses Cactus Kev Algorithm, see {@link HandMapService}
     * @param broadcaster A {@link Runnable} that will broadcast the game state to the front end 
     */
    public void showdown(Runnable broadcaster){
        int[] hand = new int[7];
        int[] suits = new int[7];
            for(int i = 0; i<5 ; i++){
                hand[i] = board.get(i).getStrength();
                suits[i] = board.get(i).getHexSuit();
            }

        for(Player player : players){
            if (player != null && !player.getPlayerCards().isEmpty()) {
                hand[5] = player.getPlayerCards().get(0).getStrength();
                suits[5] = player.getPlayerCards().get(0).getHexSuit();
                hand[6] = player.getPlayerCards().get(1).getStrength();
                suits[6] = player.getPlayerCards().get(1).getHexSuit();
                player.setHandScore(HandMapService.sevenCardEvaluator(hand, suits));
            }
        }
        
        if(broadcaster != null) broadcaster.run();

        try{
            Thread.sleep(Duration.ofSeconds(4));
        } catch (Exception e){
            System.out.println(e.getMessage());
        }
        
        if(players[0] != null && players[1] != null) {
            if(players[0].getHandScore() > players[1].getHandScore()){
                givePotToPlayer(players[0]);
            } else if(players[0].getHandScore() < players[1].getHandScore()){
                givePotToPlayer(players[1]);
            } else{
                splitPot();
            }
        } else if (players[0] != null) {
            givePotToPlayer(players[0]);
        } else if (players[1] != null) {
            givePotToPlayer(players[1]);
        }
        
        endRound();
    }

    /**
     * Function to split the pot when there is a draw
     */
    public void splitPot(){
        if(players[0] != null) players[0].addToPlayerStack(pot/2);
        if(players[1] != null) players[1].addToPlayerStack(pot/2);
        clearPot();
    }

    /**
     * Function to end the round. This will clear the table and check for game-over conditions
     * <br> </br>
     * If players still have chips and the table is full, it automatically starts a new round
     */
    public void endRound(){
        clearHands();
        clearBets();
        clearBoard();
        clearPot();
        try{
            Thread.sleep(Duration.ofSeconds(1));
        } catch (Exception e){
            System.out.println(e.getMessage());
        }
        if(getPlayersCount() >= 2 && players[0].getPlayerStack() > 0 && players[1].getPlayerStack() > 0){
            startRound();
            changeGameState(GameState.PREFLOP);
        } else {
            for(int i=0; i<players.length; i++){
                if(players[i] != null && players[i].getPlayerStack() <= 0) {
                    players[i] = null;
                    availableSeats.set(i, true);
                }
            }
            changeGameState(GameState.WAITING);
        }
    }

    /**
     * Function that will trigger an end of a betting round and reveal the next card/s or go to showdown
     * @param broadcaster A {@link Runnable} that will broadcast the game state to the front end 
     */
    public void endBettingRound(Runnable broadcaster){
        clearBets();
        resetActionToPostFlop(); 
        minRaise = bigBlind;

        switch (board.size()) {
            case 0:
                getFlop();
                changeGameState(GameState.FLOP);
                break;
            case 3:
                getTurn();
                changeGameState(GameState.TURN);
                break;
            case 4:
                getRiver();
                changeGameState(GameState.RIVER);
                break;
            case 5:
                changeGameState(GameState.SHOWDOWN); 
                showdown(broadcaster);  
                break;
        }
    }


    /**
     * Function to trigger waiting game State.
     * Mostly used after a player left a table
     */
    public void waiting(){
        gameState = GameState.WAITING;
        clearBets();
        clearBoard();
        clearPot();
    }


    /**
     * Funtion to trigger the end of a betting round
     */
    public void endBettingRound(){
        endBettingRound(null);
    }


    /**
     * Function to clear the bets and actions of all players
     */
    public void clearBets(){
        for(Player player : players){
            if(player != null) {
                player.setBet(0);
                player.setAction(null); 
            }
        }
        highestBet = 0;
    }
    
    /**
     * Moves the action position index to the next player
     * <br> </br>
     * Toggles between seat 0 and seat 1
     */
    public void moveActionPosition(){
        if(actionPositionIndex == 0) {
            actionPositionIndex = 1;
        } else {
            actionPositionIndex = 0;
        }
    }


    public void changeGameState(GameState state){
        gameState = state;
    }

    /**
     * Checks if a seat is free
     * @param seat the seat Id
     * @return a boolean, true / false depending on the seat availability
     */
    public boolean checkSeatAvailability(Integer seat) {
        if (seat > 1 || seat < 0) {
            return false;
        }
        return availableSeats.get(seat);
    }

    public void occupySeat(Integer seat) {
        availableSeats.set(seat, false);
    }

    public void leaveSeat(Integer seat) {
        availableSeats.set(seat, true);
    }

    @Override
    public String toString() {
        return tableID;
    }

    /**
     * Attempts to add a player to the table at their preferred seat
     * <br> </br>
     * This method is synchronized to prevent race conditions during seat assignment
     * @param player The {@link Player} attempting to join
     * @return true if the seat was available and the player joined
     */
    public synchronized boolean TryToJoinTable(Player player) {
        if (player.getSeat() < 0 || player.getSeat() >= players.length) return false;
        if (players[player.getSeat()] != null) return false; 
        if (!isTableFull()) {
            players[player.getSeat()] = player;
            System.out.println("Joined table " + getTableId());
            return true;
        } else {
            System.out.println("Table is full");
        }
        return false;
    }

    /**
     * Removes a player from the table and resets the table state to WAITING
     * @param player the {@link Player} who is standing up
     */
    public synchronized void standUpPlayer(Player player){
        for(int i=0; i<players.length; i++) {
            if(players[i] != null && players[i].getUsername().equals(player.getUsername())) {
                players[i] = null;
                availableSeats.set(i, true);
            }
        }
        gameState = GameState.WAITING;
    }

    private boolean isTableFull() {
        int count = 0;
        for(int i = 0 ; i<2; i++){
            if(players[i] != null) count++;
        }
        return count == 2;
    }

    /**
     * Distributes cards to players from the top of the deck
     * @param deck the current deck list
     */
    public void DealCards(List<Card> deck){
        for(int i = 0; i<2; i++){
            for(Player player : players){
                if(player != null && !deck.isEmpty()){
                    player.setCard(deck.remove(0));
                    player.checkAndSetHasCards();
                }
            }
        }
    }

    public void clearHands(){
        for(Player player : getPlayers()){
            if(player != null)
            player.clearHand();
        }
    }

    /**
     * Transfers the pot amount to a specific player's stack
     * @param player the winner of the pot
     */
    public void givePotToPlayer(Player player){
        player.addToPlayerStack(getPot());
        clearPot();
    }

    /**
     * Shifts the Dealer Button index. 
     * <br> </br>
     * In heads-up, this also determines the initial Small Blind position
     */
    public void moveButton(){
        int playersAtTable = getPlayersCount();
        if(playersAtTable < 2) return;
        
        if(buttonPositionIndex == 0) {
            buttonPositionIndex = 1;
        } else {
            buttonPositionIndex = 0;
        }
    }

    /**
     * Resets the action to the player who is not the dealer for post-flop play
     */
    private void resetActionToPostFlop(){
        actionPositionIndex = (buttonPositionIndex + 1) % 2;
    }

    /**
     * Returns the Small Blind player based on button position
     * @return {@link Player} in the SB seat
     */
    public Player getSmallBlindPlayer(){
        if(players == null  || players.length == 0) return null;
        if(getPlayersCount() == 2) {
            return players[buttonPositionIndex];
        }

        return players[0];
    }

    /**
     * Returns the Big Blind player based on button position
     * @return {@link Player} in the BB seat
     */
    public Player getBigBlindPlayer(){
        if(players == null || players.length == 0) return null;
        if(getPlayersCount() == 2) {
            return players[(buttonPositionIndex + 1) % 2];
        }
        return players[1]; 
    }

    public String getTableId() {
        return tableID;
    }

    public Player getFirstSeat(){
        return players[0];
    }

    public Player getSecondSeat(){
        return players[1];
    }

    public List<Card> getDeck(){
        return deck;
    }

    public int getBigBlind() {
        return bigBlind;
    }

    public int getSmallBlind() {
        return smallBlind;
    }

    public List<Card> getBoard(){
        return board;
    }

    public void clearBoard(){
        board.clear();
    }

    public double getPot(){
        return pot;
    }

    public void addPot(double amount){
        pot += amount;
    }

    public void clearPot(){
        pot = 0;
    }

    public int getButtonIndex(){
        return buttonPositionIndex;
    }

    public int getActionPositionIndex(){
        return actionPositionIndex;
    }

    public void setActionPositionIndex(int position){
        actionPositionIndex = position;
    }

    public List<Boolean> getAvailableSeats() {
        return availableSeats;
    }

    public Player[] getPlayers() {
        return players;
    }

    public int getPlayersCount() {
        int count = 0;
        for(Player player : players){
            if(player != null) count++;
        }
        return count;
    }

    public boolean getIsRunning() {
        return isRunning;
    }

    public GameState getGameState(){
        return gameState;
    }

     public void getFlop(){
        for(int i = 0; i<3 ; i++){
            if(!deck.isEmpty()) board.add(deck.remove(0));
        }
    }

     public void getTurn(){
        if(!deck.isEmpty()) board.add(deck.remove(0));
    }

    public void getRiver(){
        if(!deck.isEmpty()) board.add(deck.remove(0));
    }

    /**
     * Determines the highest bet currently on the table by checking all players
     * @return the maximum bet value
     */
    public double getHighestBet(){
        double highestBet = 0;
        for(Player player : players){
            if(player != null){
                if(player.getBet() > highestBet) highestBet = player.getBet();
            }
        }
        return highestBet;
    }

    public void setHighestBet(double amount){
        highestBet = amount;
    }

    public double getMinRaise() {
        return minRaise;
    }

    public void setMinRaise(double minRaise) {
        this.minRaise = minRaise;
    }
}