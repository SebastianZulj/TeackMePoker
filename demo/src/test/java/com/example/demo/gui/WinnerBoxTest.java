package com.example.demo.gui;
import javafx.application.Platform;
import javafx.stage.Stage;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import org.mockito.Mockito;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

class WinnerBoxTest {

    @Test
    public void testDisplayWinnerPlayerWin() throws InterruptedException {
        CountDownLatch latch = new CountDownLatch(1);
        boolean[] result = new boolean[1]; // Array to store the result of the assertion

        Platform.startup(() -> {
            Platform.runLater(() -> {
                WinnerBox winnerBox = new WinnerBox();
                result[0] = winnerBox.displayWinner("Winner", "Player", 1, "Straight");
                latch.countDown(); // Release the latch after the assertion
            });
        });

        // Wait until the latch is released or timeout after 5 seconds
        latch.await(5, TimeUnit.SECONDS);

        // Assert that the result is true
        assertTrue(result[0]);
    }
    @Test
    public void testDisplayWinnerAIWin() throws InterruptedException {
        CountDownLatch latch = new CountDownLatch(1);
        boolean[] result = new boolean[1]; // Array to store the result of the assertion

        Platform.startup(() -> {
            Platform.runLater(() -> {
                WinnerBox winnerBox = new WinnerBox();
                result[0] = winnerBox.displayWinner("Winner", "AI", 2, "Full House");
                latch.countDown(); // Release the latch after the assertion
            });
        });

        // Wait until the latch is released or timeout after 5 seconds
        latch.await(5, TimeUnit.SECONDS);

        // Assert that the result is true
        assertTrue(result[0]);
    }
    /*@Test
    public void testDisplayWinnerAIWin(){
        Platform.startup(() -> {
            Platform.runLater(() -> {
                WinnerBox winnerBox = new WinnerBox();
                assertTrue(winnerBox.displayWinner("Winner", "AI", 2, "Full House"));
            });
        });
    }

     */
    @Test
    public void testDisplayWinnerPlayerWinAIFold() {
        Platform.startup(() -> {
            Platform.runLater(() -> {
                WinnerBox winnerBox = new WinnerBox();
                assertTrue(winnerBox.displayWinner("Winner", "Player", 3, "Two Pair"));
            });
        });
    }
    @Test
    public void testDisplayWinnerAIWinOthersFold() {
        Platform.startup(() -> {
            Platform.runLater(() -> {
                WinnerBox winnerBox = new WinnerBox();
                assertTrue(winnerBox.displayWinner("Winner", "AI", 4, "Flush"));
            });
        });
    }
    @Test
    public void testDisplayWinnerPlayerLose() {
        Platform.startup(() -> {
            Platform.runLater(() -> {
                WinnerBox winnerBox = new WinnerBox();
                assertTrue(winnerBox.displayWinner("Winner", "Player", 5, "High Card"));
            });
        });
    }
    @Test
    public void testDisplayWinnerInvalidInput() {
        Platform.startup(() -> {
            Platform.runLater(() -> {
                WinnerBox winnerBox = new WinnerBox();
                assertThrows(IllegalArgumentException.class, () -> winnerBox.displayWinner("Winner", "Invalid", 6, "Invalid"));
            });
        });
    }

    @Test
    public void testDisplayWinnerNullInput() {
        Platform.startup(() -> {
            Platform.runLater(() -> {
                WinnerBox winnerBox = new WinnerBox();
                assertThrows(NullPointerException.class, () -> winnerBox.displayWinner(null, null, 0, null));
                assertFalse(true);

            });
        });
    }
    }