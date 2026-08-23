package com.HeadsUpMastersV2.game;

public class Card {
    private String symbol;
    private String suit;
    private int strength;
    private int prime;
    private int hexSuit;

    /**
     * Creates an standard Poker Card
     * @param symbol Symbol of the card {@code Eg: A, K, 7, 4}
     * @param suit The suit of the card {@code Eg: spade}
     * @param strength The strength of the card {@code Eg: 2 -> 0, A -> 12} (Used for game logic)
     */
    public Card(String symbol, String suit, int strength, int prime, int hexSuit){
        this.symbol = symbol;
        this.suit = suit;
        this.strength = strength;
        this.prime = prime;
        this.hexSuit = hexSuit;
    }

    public String getSymbol() {
        return symbol;
    }

    public String getSuit() {
        return suit;
    }

    public int getStrength() {
        return strength;
    }

    public int getPrime(){
        return prime;
    }
    
    public int getHexSuit() {
        return hexSuit;
    }

    public Character getEvalSuit(){
        return suit.charAt(0);
    }

    @Override
    public String toString(){
        return this.symbol;
    }

    /**
      * 
      * @return Card String format that matches the .png files for the frontend
      */
    public String toCardPngFormat(){
        return this.symbol + "-of-" + this.suit;
    }

}
