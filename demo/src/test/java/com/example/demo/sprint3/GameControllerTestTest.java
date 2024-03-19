package com.example.demo.sprint3;

import com.example.demo.controller.GameController;
import javafx.application.Platform;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.*;

public class GameControllerTestTest {
    private GameController gameController;

    @Test
    public void testValidInputWithinPot() throws InterruptedException {
        CountDownLatch latch = new CountDownLatch(1);
        boolean[] result = new boolean[1]; // Array to store the result of the assertion

        Platform.startup(() -> {
            Platform.runLater(() -> {
                gameController = new GameController();
                gameController.raiseAmount = new javafx.scene.control.TextField(); // Initialize raiseAmount field
                gameController.raiseAmount.setText("100"); // Set raise amount within player's pot
                gameController.setPlayerPot(200); // Set playerPot to 200
                result[0] = gameController.validatePlayerRaise(); // Store the result of the assertion
                latch.countDown(); // Release the latch after the assertion
            });
        });

        // Wait until the latch is released or timeout after 5 seconds
        latch.await(5, TimeUnit.SECONDS);

        // Assert that the result is true
        assertTrue(result[0]);
    }
    @Test
    public void testInvalidInputWithinPotHigh() throws InterruptedException {
        CountDownLatch latch = new CountDownLatch(1);
        boolean[] result = new boolean[1]; // Array to store the result of the assertion

        Platform.startup(() -> {
            Platform.runLater(() -> {
                gameController = new GameController();
                gameController.raiseAmount = new javafx.scene.control.TextField(); // Initialize raiseAmount field
                gameController.raiseAmount.setText("300"); // Set raise amount within player's pot
                gameController.setPlayerPot(200); // Set playerPot to 200
                result[0] = gameController.validatePlayerRaise(); // Store the result of the assertion
                latch.countDown(); // Release the latch after the assertion
            });
        });

        // Wait until the latch is released or timeout after 5 seconds
        latch.await(5, TimeUnit.SECONDS);

        // Assert that the result is true
        assertFalse(result[0]);
    }
    @Test
    public void testInvalidInputFormat() throws InterruptedException {
        CountDownLatch latch = new CountDownLatch(1);
        boolean[] result = new boolean[1]; // Array to store the result of the assertion

        Platform.startup(() -> {
            Platform.runLater(() -> {
                gameController = new GameController();
                gameController.raiseAmount = new javafx.scene.control.TextField(); // Initialize raiseAmount field
                gameController.raiseAmount.setText("abc"); // Invalid input format (non-numeric)
                result[0] = gameController.validatePlayerRaise(); // Store the result of the assertion
                latch.countDown(); // Release the latch after the assertion
            });
        });

        // Wait until the latch is released or timeout after 5 seconds
        latch.await(5, TimeUnit.SECONDS);

        // Assert that the result is false
        assertFalse(result[0]);
    }
    @Test
    public void testInvalidInputValue() throws InterruptedException {
        CountDownLatch latch = new CountDownLatch(1);
        boolean[] result = new boolean[1]; // Array to store the result of the assertion

        Platform.startup(() -> {
            Platform.runLater(() -> {
                gameController = new GameController();
                gameController.raiseAmount = new javafx.scene.control.TextField(); // Initialize raiseAmount field
                gameController.raiseAmount.setText("-50"); // Invalid input value (negative)
                gameController.setPlayerPot(200); // Set playerPot to 200
                result[0] = gameController.validatePlayerRaise(); // Store the result of the assertion
                latch.countDown(); // Release the latch after the assertion
            });
        });

        // Wait until the latch is released or timeout after 5 seconds
        latch.await(5, TimeUnit.SECONDS);

        // Assert that the result is false
        assertFalse(result[0]);
    }

}