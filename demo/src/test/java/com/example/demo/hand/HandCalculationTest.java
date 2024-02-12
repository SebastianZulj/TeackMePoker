package com.example.demo.hand;

import com.example.demo.deck.Card;
import com.example.demo.deck.CardValue;
import com.example.demo.deck.Suit;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

class HandCalculationTest {
    private ArrayList<String> playingCards = new ArrayList<>();

    @Test
    void checkPairAndMore() {
    }

    @Test
    void checkHighCards() {
    }

    @Test
    void checkSuit() {
    }

    @Test
    void checkStraight() {
        //add cards required for straight to playingCards
        //registers a straight when any 5 cards are in a row
        playingCards.add("5,h");
        playingCards.add("4,d");
        playingCards.add("6,h");
        playingCards.add("7,d");
        playingCards.add("8,h");
        playingCards.add("14,d");
        playingCards.add("9,h");
        HandCalculation handCalculator = new HandCalculation(playingCards);
        int straight = handCalculator.checkStraight();
        assertEquals(5, straight);
    }

    @Test
    void help() {
    }
}