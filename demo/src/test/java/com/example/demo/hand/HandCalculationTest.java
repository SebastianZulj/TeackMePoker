package com.example.demo.hand;

import com.example.demo.deck.Card;
import com.example.demo.deck.CardValue;
import com.example.demo.deck.Suit;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

class HandCalculationTest {

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
        Card card1 = new Card(Suit.HEARTS, CardValue.TWO, null);
        Card card2 = new Card(Suit.SPADES, CardValue.THREE, null);
        Card card3 = new Card(Suit.CLUBS, CardValue.FOUR, null);
        Card card4 = new Card(Suit.SPADES, CardValue.FIVE, null);
        Card card5 = new Card(Suit.DIAMONDS, CardValue.SIX, null);


        ArrayList<Card> cards = new ArrayList<>();
        cards.add(card1);
        cards.add(card2);
        cards.add(card3);
        cards.add(card4);
        cards.add(card5);

        Hand hand = new Hand(cards);
       // HandCalculation handCalculator = new HandCalculation(hand);
    }

    @Test
    void help() {
    }
}