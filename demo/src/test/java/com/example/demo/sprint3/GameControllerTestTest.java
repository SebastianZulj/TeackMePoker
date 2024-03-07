package com.example.demo.sprint3;
import com.example.demo.controller.GameController;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class GameControllerTestTest {
    private GameController gameController;

    @BeforeEach
    void setUp() {
        // Initialize the GameController object before each test
        gameController = new GameController();
    }

    @Test
    void testPlayerCheck() {
        gameController.playerCheck();

        // Assert the expected changes in the state of GameController

        // Add more assertions if needed
    }

}
