package com.HeadsUpMastersV2.game;

import java.util.ArrayList;
import java.util.List;


public class Player {
    private String tableId;
    private String username;
    private double bet;
    private ActionType action;
    private boolean hasCards;
    private String flag;
    private String avatar;
    private List<Card> playerCards;
    private Double playerStack;
    private int seat;
    private int handScore;


    public Player(){
        this.playerCards = new ArrayList<>();
    }

    /**
     * For scalability: <br>
     * <br>
     * Player objects will be created at run time.<br>
     * <br>
     * Static data will be fetched from the database.
     * @param username      Player username
     * @param flag          Player country of origin
     * @param avatar        Player avatar
     * @param playerCards   Player cards on game
     * @param playerStack   Player current stack
     * @param seat          Player seat at the table
     * @param tablePosition Player position at the table
     */


    public Player(
        String username, 
        String flag, 
        String avatar, 
        Double playerStack, 
        int seat) 
        {
        this.username = username;
        this.bet = 0;
        this.flag = flag;
        this.avatar = avatar;
        this.hasCards = false;
        this.action = null;
        this.playerCards = new ArrayList<>();
        this.seat = seat;
        this.playerStack = playerStack;
        this.handScore = 0;

    }


    //Comments created for code readability
    //Start of Getters and setters
    public String getTableId(){
        return tableId;
    }

     public void setTableId(String tableID) {
        this.tableId = tableID;
    }

    public String getUsername() {
        return username;
    }

    public int getHandScore(){
        return handScore;
    }

    public void setHandScore(int score){
        handScore = score;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public double getBet(){
        return bet;
    }

    public void setBet(double amount){
        bet = amount;
    }

    public void bet(double amount){
        playerStack -= amount;
        bet += amount;
    }

    public String getFlag() {
        return flag;
    }

    public void setFlag(String flag) {
        this.flag = flag;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public List<Card> getPlayerCards() {
        return playerCards;
    }

    public boolean hasCards(){
        return hasCards;
    }

    public void fold(){
        hasCards = false;
    }

    public void checkAndSetHasCards(){
        if (playerCards.size() == 2){
            hasCards = true;
        }
    }

    public ActionType getAction(){
        return action;
    }

    public void setAction(ActionType actionType){
        action = actionType;
    }

    public Double getPlayerStack() {
        return playerStack;
    }

    public void setPlayerStack(double amount){
        playerStack = amount;
    }

    public Integer getSeat(){
        return seat;
    }



    //End of Getters and Setters
    ////Comments created for code readability

    /**
     * Updates a player table stack in real time.<br>
     * <br>
     * @param amount the amount type is a double. Can be both negative or an positive;
     */
    public void addToPlayerStack(double amount){
        this.playerStack += amount;
    }

   

    public void clearBet(){
        bet = 0;
    }


    public void clearHand(){
        this.playerCards.clear();
    }


    @Override
    public String toString(){
        return this.username;
    }


    
    /**
     * Void funtion to give a card to a player.<br>
     * <br>
     * Maily used in {@link Game}<br>
     * <br>
     * @param card The param will be an {@link Card} object
     */
    public void setCard(Card card){
        int cardCount = this.getPlayerCards().size();
        if(cardCount < 2){
            playerCards.add(cardCount, card);
        }
        else{
            System.out.println("Player has 2 cards already");
        }
    }

    public void setPublicCard(Card card){
        int cardCount = this.getPlayerCards().size();
        if(cardCount < 2){
            playerCards.add(cardCount, card);
        }
        else{
            System.out.println("Player has 2 cards already");
        }
    }

}
