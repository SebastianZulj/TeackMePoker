package com.example.demo.sprint2;

import com.example.demo.gui.FileController;
import org.junit.jupiter.api.Test;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

public class Testing {
/*
    @Test
    public void TF30() {
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
            for (String name : winnerHistoryTest.keySet()) {
                assertTrue(winnerHistoryTest.containsKey(name));
                assertTrue(winnerHistoryTest.get(name).containsKey("winningHand"));

            }
            bufferedReader.close();

        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        //assertEquals(winnerHistoryTest, fileController.readWinnerHistory());
    }

 */
/*
    @Test
    public void TF300() {
        FileController fileController = new FileController();

        // Assuming readWinnerHistory() does not need arguments and reads from a fixed file path
        HashMap<String, HashMap<String, Integer>> actualResults = fileController.readWinnerHistory();

        // Expected results based on the known content of the test file
        HashMap<String, HashMap<String, Integer>> expectedResults = new HashMap<>();
        expectedResults.put("PlayerName1", new HashMap<>() {{
            put("WinningHand1", 1);
        }});
        expectedResults.put("PlayerName2", new HashMap<>() {{
            put("WinningHand2", 1);
        }});
        // Add more entries to expectedResults based on your test file content

        // Verify that all expected keys (players) and their winning hands are present with correct counts
        for (String name : expectedResults.keySet()) {
            assertTrue(actualResults.containsKey(name), "Expected player not found: " + name);

            HashMap<String, Integer> expectedHands = expectedResults.get(name);
            HashMap<String, Integer> actualHands = actualResults.get(name);

            for (String hand : expectedHands.keySet()) {
                assertTrue("Expected hand not found for player " + name + ": " + hand, actualHands.containsKey(hand));
                assertEquals("Incorrect count for " + hand + " of player " + name,
                        expectedHands.get(hand), actualHands.get(hand));
            }
        }
    }

 */
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
    /*
    @Test
    public void TF31() {
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
        assertEquals(fileController.readWinnerHistory(), winnerHistoryTest );
    }

     */

}
