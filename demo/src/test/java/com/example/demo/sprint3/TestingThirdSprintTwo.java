package com.example.demo.sprint3;

import com.example.demo.controller.GameController;
import com.example.demo.gui.WinnerBox;
import javafx.application.Platform;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class TestingThirdSprintTwo {
    GameController gameController = new GameController();
    @BeforeAll
    static void setup() {
        // Nödvändigt för att initiera JavaFX-miljön
        Platform.startup(() -> {});
    }
    @BeforeEach
    void setUp() {
        gameController = new GameController();
    }

    @Test
    public void testSetPlayerPot() {
        gameController.setPlayerPot(2000); // Set player pot to 2000
        assertEquals(2000, gameController.getPotValue(), "Player pot should be 2000");
    }




}
