package com.example.demo.gui;

import java.io.*;

/**
 * class responsible for writing and reading to files
 */
public class FileController {

    /**
     * method which saves winner results to txt file.
     * saves name & winning hand to winnerHistory.txt
     */
    public void saveWinnerHistory(String winnerOfRound, String winnerHand) {
        System.out.println("Saving game results to file");
        String filePath = "demo/src/main/resources/com/example/demo/files/winnerHistory.txt";
        try {
            FileWriter fileWriter = new FileWriter(filePath, true); //add to existing file
            BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);
            bufferedWriter.write(winnerOfRound + " " + winnerHand);
            System.out.println(winnerOfRound + " " + winnerHand);
            bufferedWriter.newLine();
            bufferedWriter.close();
            System.out.println("Game results saved successfully.");
        } catch (IOException e) {
            System.err.println("Error saving game results to file: " + e.getMessage());
        }
    }

    /**
     * method which reads winner results from winnerHistory.txt file.
     */
    public void readWinnerHistory() {
        System.out.println("Reading game results from file");
        String filePath = "demo/src/main/resources/com/example/demo/files/winnerHistory.txt";
        try {
            FileReader fileReader = new FileReader(filePath);
            BufferedReader bufferedReader = new BufferedReader(fileReader);

            String line;
            while ((line = bufferedReader.readLine()) != null) {
                String[] parts = line.split("\\s+", 2); // Split by whitespace, limit to 2 parts
                String name = parts[0];
                String winningHand = parts[1];
                System.out.println("Name: " + name + ", Winning Hand: " + winningHand);
            }
            bufferedReader.close();
        } catch (IOException e) {
            System.err.println("Error reading file: " + e.getMessage());
        }
        //TODO: store results in hashmap or arraylist?
        // count number of winns and winning hands for each player
    }


}
