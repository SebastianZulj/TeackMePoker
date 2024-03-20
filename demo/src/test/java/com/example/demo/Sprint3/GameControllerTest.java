package com.example.demo.Sprint3;

import com.example.demo.controller.GameController;
import javafx.application.Platform;
import org.junit.jupiter.api.Test;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.*;

/**
 * This class tests the GameController class that the player pot is correctly set and that the player can raise the correct amount.
 * or else an error is shown.
 * @Author Nicklas Svensson
 */
public class GameControllerTest {
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
    public void testValidInputZero() throws InterruptedException {
        CountDownLatch latch = new CountDownLatch(1);
        boolean[] result = new boolean[1]; // Array to store the result of the assertion

        Platform.startup(() -> {
            Platform.runLater(() -> {
                gameController = new GameController();
                gameController.raiseAmount = new javafx.scene.control.TextField(); // Initialize raiseAmount field
                gameController.raiseAmount.setText("0"); // Set raise amount within player's pot
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
    @Test
    public void testInvalidInputWithinPotHigh() throws InterruptedException {
        CountDownLatch latch = new CountDownLatch(1);
        boolean[] result = new boolean[1];

        Platform.startup(() -> {
            Platform.runLater(() -> {
                gameController = new GameController();
                gameController.raiseAmount = new javafx.scene.control.TextField();
                gameController.raiseAmount.setText("300");
                gameController.setPlayerPot(200);
                result[0] = gameController.validatePlayerRaise();
                latch.countDown();
            });
        });

        latch.await(5, TimeUnit.SECONDS);

        assertFalse(result[0]); // Assert based on the return value of validatePlayerRaise()
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
