package com.example.demo.sprint2;

import com.example.demo.controller.FileController;
import com.example.demo.gui.Sound;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;

import static org.junit.jupiter.api.Assertions.*;

public class Testing {
    public static void setup() {
    ;
    }
/*
    @Test
    public void TF301() {
        FileController fileController = new FileController();
        HashMap<String, HashMap<String, Integer>> winnerHistoryTest = new HashMap<>();
        String filePath = "demo/src/main/resources/com/example/demo/files/winnerHistory.txt";
        try {
            FileReader fileReader = new FileReader(filePath);
            BufferedReader bufferedReader = new BufferedReader(fileReader);

            String line;
            while ((line = bufferedReader.readLine()) != null) {
                String[] parts = line.split("\\s+", 2); //limit to two-part split
                String name = parts[0];
                String winningHand = parts[1];

            }
            //for (String name : winnerHistoryTest.keySet()) {
                //assertTrue(winnerHistoryTest.containsKey(name));
                //assertTrue(winnerHistoryTest.get(name).containsKey("winningHand"));

            //}
            bufferedReader.close();

        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        assertEquals(winnerHistoryTest, fileController.readWinnerHistory());
    }

 */




    /*
    @Test
    public void TF30() {
        FileController fileController = new FileController();

        // Mockito????
        HashMap<String, HashMap<String, Integer>> winnerHistoryTest = new HashMap<>();
        String filePath = "src/main/resources/com/example/demo/files/winnerHistory.txt";
        //String line = "";
        //boolean firstLineChecked = false;
        try (BufferedReader bufferedReader = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                String[] parts = line.split("\\s+", 2);
                String name = parts[0];
                String winningHand = parts[1];
                //assertEquals("tiffany lost", line);
                //firstLineChecked = true;
                if (winnerHistoryTest.containsKey(name)) { //is existing player
                    HashMap<String, Integer> hands = winnerHistoryTest.get(name); //retrieve value hashmap
                    if (hands.containsKey(winningHand)) {
                        int count = hands.get(winningHand);
                        hands.put(winningHand, count + 1);
                    } else {
                        hands.put(winningHand, 1);
                    }
                } else { //if new player, add new
                    winnerHistoryTest.put(name, new HashMap<>() {{
                        put(winningHand, 1);
                    }});
                }

            }
            bufferedReader.close();
        } catch (IOException e) {
            System.err.println("Error reading file: " + e.getMessage());
        }
        String result = fileController.readWinnerHistory().toString();
        assertEquals(result, winnerHistoryTest );

    }

     */

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
                if (winnerHistoryTest.containsKey(name)) { //is existing player
                    HashMap<String, Integer> hands = winnerHistoryTest.get(name); //retrieve value hashmap
                    if (hands.containsKey(winningHand)) {
                        int count = hands.get(winningHand);
                        hands.put(winningHand, count + 1);
                    } else {
                        hands.put(winningHand, 1);
                    }
                } else { //if new player, add new
                    winnerHistoryTest.put(name, new HashMap<>() {{
                        put(winningHand, 1);
                    }});
                }
            }
            bufferedReader.close();
        } catch (IOException e) {
            System.err.println("Error reading file: " + e.getMessage());
        }
        Assertions.assertEquals(fileController.readWinnerHistory(), winnerHistoryTest );
    }



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


}
