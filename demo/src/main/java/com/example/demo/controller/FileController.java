package com.example.demo.controller;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * class responsible for writing and reading to files
 */
public class FileController {
    private HashMap<String, HashMap<String, Integer>> winnerHistory = new HashMap<>();
    HashMap<String, HashMap<String, Integer>> resultsToSend;

    /**
     * writes winner name and winning hand results to winnerHistory.txt file.
     */
    public void saveWinnerHistory(String winnerOfRound, String winnerHand, boolean userWin) {
        String filePath = "demo/src/main/resources/com/example/demo/files/winnerHistory.txt";
        try {
            FileWriter fileWriter = new FileWriter(filePath, true); //add to existing file
            BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);
            if (userWin) { //if user won
                bufferedWriter.write(winnerOfRound + " " + winnerHand);
                System.out.println(winnerOfRound + " " + winnerHand);
            } else { //if user lost
                bufferedWriter.write(winnerOfRound + " lost");
                System.out.println(winnerOfRound + " lost");
            }
            bufferedWriter.newLine();
            bufferedWriter.close();
            System.out.println("Game results saved successfully.");
        } catch (IOException e) {
            System.err.println("Error saving game results to file: " + e.getMessage());
        }
    }

    /**
     * reads winner results from winnerHistory.txt file.
     */
    public HashMap<String, HashMap<String,Integer>> readWinnerHistory() {
        resultsToSend = new HashMap<>();
        String filePath = "src/main/resources/com/example/demo/files/winnerHistory.txt";
        try {
            FileReader fileReader = new FileReader(filePath);
            BufferedReader bufferedReader = new BufferedReader(fileReader);

            String line;
            while ((line = bufferedReader.readLine()) != null) {
                String[] parts = line.split("\\s+", 2); //limit to two-part split
                String name = parts[0];
                String winningHand = parts[1];
                resultsToSend = addToRegistry(name, winningHand); //save
            }
            bufferedReader.close();
        } catch (IOException e) {
            System.err.println("Error reading file: " + e.getMessage());
        }
        return resultsToSend;
    }

    /**
     * adds winner results to a HashMap containing name and all related winning hands in arrayList
     * @param name name of player
     * @param winningHand winning hand of given player
     */
    public HashMap<String, HashMap<String, Integer>> addToRegistry(String name, String winningHand) {
        if (winnerHistory.containsKey(name)) { //is existing player
            HashMap<String, Integer> hands = winnerHistory.get(name); //retrieve value hashmap
            if (hands.containsKey(winningHand)) { //if winning hand already exists
                int count = hands.get(winningHand);
                hands.put(winningHand, count + 1); //increment win count
            } else {
                hands.put(winningHand, 1); //if not, add new winning hand
            }
        } else { //if new player, add new
            HashMap<String, Integer> hands = new HashMap<>();
            hands.put(winningHand, 1);
            winnerHistory.put(name, hands);
        }
        return winnerHistory;
    }


}
