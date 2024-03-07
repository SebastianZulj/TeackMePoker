package com.example.demo.gui;
import javafx.application.Platform;
import javafx.stage.Stage;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import org.mockito.Mockito;

class WinnerBoxTest {

    @Test
   public void testDisplayWinnerPlayerWin() {
        Platform.startup(() -> {
            Platform.runLater(() -> {
                WinnerBox winnerBox = new WinnerBox();
                assertTrue(winnerBox.displayWinner("Winner", "Player", 1, "Straight"));
            });
        });
    }
    @Test
    public void testDisplayWinnerAIWin(){
        Platform.startup(() -> {
            Platform.runLater(() -> {
                WinnerBox winnerBox = new WinnerBox();
                assertTrue(winnerBox.displayWinner("Winner", "AI", 2, "Full House"));
            });
        });
    }
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