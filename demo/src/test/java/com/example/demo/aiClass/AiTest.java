package com.example.demo.aiClass;

import com.example.demo.deck.Card;
import com.example.demo.deck.CardValue;
import com.example.demo.deck.Suit;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import javax.swing.*;

import static org.junit.jupiter.api.Assertions.*;

class AiTest {


    @Test
    void TF4() {
        Ai sebZul = new Ai(50, "Sebastian Zulj");
    }

    @Test
    void TF5() {
        Ai sebZul = new Ai(50, "Sebastian Zulj");
        Card card1 = new Card(Suit.HEARTS, CardValue.TWO, null);
        Card card2 = new Card(Suit.SPADES, CardValue.FOUR, null);
        sebZul.setStartingHand(card1, card2);
        sebZul.makeDecision(10);
        assertEquals("fold", sebZul.getDecision());
    }

    @Test
    void TF6() {
        Ai sebZul = new Ai(50, "Sebastian Zulj");
        Card card1 = new Card(Suit.HEARTS, CardValue.ACE, null);
        Card card2 = new Card(Suit.SPADES, CardValue.SIX, null);
        sebZul.setStartingHand(card1, card2);
        sebZul.makeDecision(10);
        assertEquals("call", sebZul.getDecision());
    }

    @Test
    void TF7() {
        Ai sebZul = new Ai(50, "Sebastian Zulj");
        Card card1 = new Card(Suit.HEARTS, CardValue.SIX, null);
        Card card2 = new Card(Suit.SPADES, CardValue.SIX, null);
        sebZul.setStartingHand(card1, card2);
        sebZul.makeDecision(10);
        String decision = sebZul.getDecision();
        if (decision.contains(",")){
            String[] temp = new String[4];
            temp = decision.split(",");
            decision = temp[0];
        }
        assertEquals("raise", decision);
    }

    @Test
    void TF8() {
        Ai sebZul = new Ai(50, "Sebastian Zulj");
        Card[] flop = new Card[3];
        Card card1 = new Card(Suit.HEARTS, CardValue.TWO, null);
        Card card2 = new Card(Suit.SPADES, CardValue.FOUR, null);
        flop[0] = new Card(Suit.SPADES, CardValue.TWO, null);
        flop[1] = new Card(Suit.CLUBS, CardValue.TWO, null);
        flop[2] = new Card(Suit.DIAMONDS, CardValue.THREE, null);
        sebZul.setStartingHand(card1, card2);
        sebZul.makeDecision(40 ,flop);
        String decision = sebZul.getDecision();
        if (decision.contains(",")){
            String[] temp = new String[4];
            temp = decision.split(",");
            decision = temp[0];
        }
        assertEquals("call", decision);
    }

    @Test
    void TF9() {
        Ai sebZul = new Ai(50, "Sebastian Zulj");

    }


}