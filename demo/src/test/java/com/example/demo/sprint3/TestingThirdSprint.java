package com.example.demo.sprint3;

import com.example.demo.controller.SettingsController;
import javafx.application.Platform;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 * This class contains test cases for the method that converts wins and losses to a percentage
 * @Author Nicklas Svensson
 */

public class TestingThirdSprint {
    SettingsController settingsController = new SettingsController();

    @BeforeAll
    static void setup() {
        // Nödvändigt för att initiera JavaFX-miljön
        Platform.startup(() -> {});
    }
    @Test
    public void testCalculateWinPercentageWinAndLosses(){
    int totalWins = 10;
    int totalLosses = 5;
    double expectedWinPercentage = 66.66666666666667;

    double winPercentage = settingsController.calculateWinPercentage(totalWins, totalLosses);

    assertEquals(expectedWinPercentage, winPercentage, 0.001);

}
    @Test
    public void testCalculateWinPercentageWinAndLossesSecond() {
        int totalWins = 15;
        int totalLosses = 3;
        double expectedWinPercentage = 83.33333333333334;

        double winPercentage = settingsController.calculateWinPercentage(totalWins, totalLosses);

        assertEquals(expectedWinPercentage, winPercentage, 0.001);
    }
    @Test
    public void testCalculateWinPercentageNoWins(){
        int totalWins = 0;
        int totalLosses = 5;
        double expectedWinPercentage = 0;

        double winPercentage = settingsController.calculateWinPercentage(totalWins, totalLosses);

        assertEquals(expectedWinPercentage, winPercentage, 0.001);
    }

    @Test
    public void testCalculateWinPercentageWinAndNoLosses(){
        int totalWins = 10;
        int totalLosses = 0;
        double expectedWinPercentage = 100;

        double winPercentage = settingsController.calculateWinPercentage(totalWins, totalLosses);

        assertEquals(expectedWinPercentage, winPercentage, 0.001);
    }

    @Test
    public void testCalculateWinPercentageEqualWinAndLoss(){
        int totalWins = 10;
        int totalLosses = 10;
        double expectedWinPercentage = 50;

        double winPercentage = settingsController.calculateWinPercentage(totalWins, totalLosses);

        assertEquals(expectedWinPercentage, winPercentage, 0.001);

    }
    // Fixa felhantering kring testfallet
    @Test
    public void testCalculateWinPercentageMinusWinAndLoss(){
        int totalWins = -10;
        int totalLosses = -10;
        double expectedWinPercentage = 0;

        double winPercentage = settingsController.calculateWinPercentage(totalWins, totalLosses);

        assertEquals(expectedWinPercentage, winPercentage, 0.001);

    }
    @Test
    public void testCalculateWinPercentageNoWinsNoLosses(){
        int totalWins = 0;
        int totalLosses = 0;
        double expectedWinPercentage = 0;

        double winPercentage = settingsController.calculateWinPercentage(totalWins, totalLosses);

        assertEquals(expectedWinPercentage, winPercentage, 0.001);
    }



}
