package com.example.demo.sprint2;

import com.example.demo.controller.FileController;
import com.example.demo.controller.GameController;
import com.example.demo.gui.ConfirmBox;
import com.example.demo.gui.Sound;
import static org.mockito.Mockito.*;
import javafx.application.Platform;

import javafx.application.Platform;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;
import org.mockito.Mockito;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.lang.reflect.Field;
import java.util.HashMap;

import static org.junit.jupiter.api.Assertions.*;

public class Testing {

    @BeforeAll
    static void setup() {
        // Nödvändigt för att initiera JavaFX-miljön
        Platform.startup(() -> {});
    }
    /**
     * Test case 30 for reading the winner history file.
     */

    @Test
    public void TF30() {
        FileController fileController = new FileController();
        HashMap<String, HashMap<String, Integer>> winnerHistoryTest = new HashMap<>();
        String filePath = "src/main/resources/com/example/demo/files/winnerHistory.txt";
        try {
            FileReader fileReader = new FileReader(filePath);
            BufferedReader bufferedReader = new BufferedReader(fileReader);

            String line;
            while ((line = bufferedReader.readLine()) != null) {
                String[] parts = line.split("\\s+", 2);
                String name = parts[0];
                String winningHand = parts[1];
                if (winnerHistoryTest.containsKey(name)) {
                    HashMap<String, Integer> hands = winnerHistoryTest.get(name);
                    if (hands.containsKey(winningHand)) {
                        int count = hands.get(winningHand);
                        hands.put(winningHand, count + 1);
                    } else {
                        hands.put(winningHand, 1);
                    }
                } else {
                    winnerHistoryTest.put(name, new HashMap<>() {{
                        put(winningHand, 1);
                    }});
                }
            }
            bufferedReader.close();
        } catch (IOException e) {
            System.err.println("Error reading file: " + e.getMessage());
        }
        Assertions.assertEquals( winnerHistoryTest, fileController.readWinnerHistory());
    }


    /**
     * Test case for mute and unmute background music.
     */

    @Test
    void TF28() {
        Sound musicPlayer = new Sound();
        musicPlayer.playBackgroundMusic();
        musicPlayer.stopBackgroundMusic();
        Assertions.assertEquals("Stopped", musicPlayer.getSoundStatus(), "Sound status should be 'Stopped' after stopBackgroundMusic is called.");
    }
    @Test
    public void TF285() {
        Sound musicPlayer = new Sound();
        musicPlayer.playBackgroundMusic();
        Assertions.assertEquals("Playing", musicPlayer.getSoundStatus(), "After playing background music, sound status should be 'Playing'.");
    }


    @Test
    void TF32() throws NoSuchFieldException, IllegalAccessException {
        ConfirmBox mockConfirmBox = mock(ConfirmBox.class);
        when(mockConfirmBox.display(anyString(), anyString())).thenReturn(true);

        GameController gameController = new GameController();
        //gameController.setConfirmBox(mockConfirmBox); // Använd setter-metoden för att injicera mocken

        boolean reply = gameController.askReplay();

        assertTrue(reply);
        /*

        ConfirmBox confirmBox = mock(ConfirmBox.class);
        when(confirmBox.display("Vill du spela igen?", "Vill du spela igen?")).thenReturn(true);

        GameController gameController = new GameController();

        Field confirmBoxField = GameController.class.getDeclaredField("confirmBox");
        confirmBoxField.setAccessible(true); // Gör fältet tillgängligt
        confirmBoxField.set(gameController, confirmBox); // Sätt mocken

        boolean reply = gameController.askReplay();

        assertTrue(reply);

        verify(confirmBox).display("Vill du spela igen?", "Vill du spela igen?");

         */

    }

    @Test
    void TF33() {
        ConfirmBox confirmBox = mock(ConfirmBox.class);
        when(confirmBox.display("Game Over", "Do you want to play again?")).thenReturn(false);
    }


}
