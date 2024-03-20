package com.example.demo.controller;

import com.example.demo.aiClass.Ai;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.util.LinkedList;
import java.util.Objects;


/**
 * Break-out from GameController to handle AI actions and UI.
 * Methods were existent, but they have been separated and placed in this class.
 * @author Tiffany Dizdar, HT24.
 */
public class AIController {
    private SPController spController;
    private GameController gController;
    private LinkedList<Ai> aiPlayers;
    private int[][] aiPositions;
    private Label[][] collectionOfLabelsAi;
    private ImageView[] collectionOfCardsAi;


    //note that this method was never used to begin with
    /**
     * Method which dims an AI player
     * @param AI an AI player
     */
    public void removeAiPlayer(int AI) {
        Platform.runLater(() -> {
            collectionOfLabelsAi[AI][0].setVisible(false);
            collectionOfLabelsAi[AI][1].setVisible(false);
            collectionOfLabelsAi[AI][2].setVisible(false);
        });
    }

    /**
     * Places the AI-players in the correct position depending on chosen number of players.
     * @param aiPlayers All the AI-players that are active.
     * @param notFirstRound
     * @param deadAIIndex
     */
    public void setAiPlayers(LinkedList<Ai> aiPlayers, boolean notFirstRound, int deadAIIndex) {
        this.aiPlayers = aiPlayers;
        int totalAI = spController.getFixedNrOfAIs();
        if (!notFirstRound) {
            if (totalAI == 1) {
                setShowUIAiBar(2);
            } else if (totalAI == 3) {
                setShowUIAiBar(0);
                setShowUIAiBar(2);
                setShowUIAiBar(4);
            } else if (totalAI == 5) {
                setShowUIAiBar(0);
                setShowUIAiBar(1);
                setShowUIAiBar(2);
                setShowUIAiBar(3);
                setShowUIAiBar(4);
            }
        } else if (notFirstRound) {
            endOfRound(deadAIIndex);
        }
    }

    /**
     * Updates AI-frame based on currentAI-position and decision with the method setUIAiStatus.
     * @param currentAI Chosen AI to update AI-frame
     * @param decision Check, call, fold, raise or lost
     */
    public void aiAction(int currentAI, String decision) {
        int setAINr = spController.getFixedNrOfAIs();

        int setOfPlayers = 0; // Is used for choosing the correct set of positioning (see aiPositions[][])

        // Decides (based on chosen AI-players) which position to place the AI at
        if (setAINr == 1) {
            setOfPlayers = 0;
        } else if (setAINr == 3) {
            setOfPlayers = 1;
        } else if (setAINr == 5) {
            setOfPlayers = 2;
        }

        int currentAIPosition = aiPositions[setOfPlayers][currentAI];

        if (gController.getPrevPlayerActive() != -1) { // If there does exist a previous active AI-player
            setUIAiStatus(gController.getPrevPlayerActive(), "idle");
            // Resets the previous player's image from glowing(active) to non-glowning(idle)
        }

        Ai ai = aiPlayers.get(currentAI);

        if (decision.contains("fold") || decision.contains("lost") || decision.isEmpty()) {
            setUIAiStatus(currentAIPosition, "inactive");
        } else {
            setUIAiStatus(currentAIPosition, "active");
            gController.setPrevPlayerActive(currentAIPosition);
        }

        Platform.runLater(new Runnable() {
            private volatile boolean shutdown;

            @Override
            public void run() {
                /**
                 * Sets name, pot and action for the AI's (UI)
                 */
                while (!shutdown) {
                    setLabelUIAiBarName(currentAIPosition, ai.getName());
                    setLabelUIAiBarPot(currentAIPosition, Integer.toString(ai.aiPot()));
                    setLabelUIAiBarAction(currentAIPosition, getFormattedDecision(decision)); //TODO: fix
                    shutdown = true;
                }
            }
        });
    }

    /**
     * Formats action label for AI.
     * @param decision fold/lost/check/call/raise/all-in/Dealer/SmallBlind/BigBlind
     * @return Formatted decision
     */
    public String getFormattedDecision(String decision) {
        String actionText = "Error";

        if (decision.contains("fold")) {
            actionText = "Fold";
        } else if (decision.contains("lost")) {
            actionText = "Lost";
        } else if (decision.contains("check")) {
            actionText = "Check";
        } else if (decision.contains("call")) {
            actionText = "Call";
        } else if (decision.contains("raise")) {
            String[] decisionAi = decision.split(",");
            actionText = "Raise, §" + decisionAi[1];
        } else if (decision.contains("all-in")) {
            actionText = "All-In";
        } else if (decision.contains("Dealer")) {
            actionText = "Dealer";
        } else if (decision.contains("SmallBlind")) {
            actionText = "Small Blind, §" + spController.getSmallBlind();
        } else if (decision.contains("BigBlind")) {
            actionText = "Big Blind, §" + spController.getBigBlind();
        }

        return actionText;
    }


    /**
     * Changes the AI-frame based on position and state.
     * @param position Position on the screen (0-4).
     * @param state The state can either be inactive (folded/lost), idle (waiting for it's turn),
     *        active (currently it's turn).
     */
    public void setUIAiStatus(int position, String state) {
        String hideCardsPath = "/com/example/demo/images/aiBarWithoutCards.png";
        String showCardsPath = "/com/example/demo/images/aiBarWithCards.png";
        String showActiveCardsPath = "/com/example/demo/images/aiBarWithCardsCurrentPlayer.png";

        Image hideCards = new Image(getClass().getResourceAsStream(hideCardsPath), 122, 158, true, true);
        Image showCards = new Image(getClass().getResourceAsStream(showCardsPath), 122, 158, true, true);
        Image showActiveCards = new Image(getClass().getResourceAsStream(showActiveCardsPath), 122, 158, true, true);

        if (Objects.equals(state, "inactive")) {
            collectionOfCardsAi[position].setImage(hideCards);
        } else if (Objects.equals(state, "idle")) {
            collectionOfCardsAi[position].setImage(showCards);
        } else if (Objects.equals(state, "active")) {
            collectionOfCardsAi[position].setImage(showActiveCards);
        }
    }

    /**
     * This metod makes sure that during the players turn, the previous AI is considered idle
     */
    public void inactivateAllAiCardGlows() {
        if (gController.getPrevPlayerActive() != -1) {
            setUIAiStatus(gController.getPrevPlayerActive(), "idle");
            gController.setPrevPlayerActive(-1);
        }
    }

    /**
     * Used to show labels and AI-frame.
     * @param position Position on the screen (0-4).
     */
    public void setShowUIAiBar(int position) {
        collectionOfLabelsAi[position][0].setVisible(true);
        collectionOfLabelsAi[position][1].setVisible(true);
        collectionOfLabelsAi[position][2].setVisible(true);
        collectionOfCardsAi[position].setVisible(true);
    }

    /**
     * Used to change AI-label "name" based on position.
     * @param position Position on the screen (0-4).
     * @param name The label for the AI's name.
     */
    public void setLabelUIAiBarName(int position, String name) {
        collectionOfLabelsAi[position][0].setText(name);
    }

    /**
     * Used to change AI-label "pot" based on position.
     * @param position Position on the screen (0-4).
     * @param pot The label for the AI's pot.
     */
    public void setLabelUIAiBarPot(int position, String pot) {
        collectionOfLabelsAi[position][1].setText("§" + pot);
    }

    /**
     * Used to change AI-label "action" based on position.
     * @param position Position on the screen (0-4).
     * @param action The label for the AI's action.
     */
    public void setLabelUIAiBarAction(int position, String action) {
        collectionOfLabelsAi[position][2].setText(action);
    }


    /**
     * Clears AI action and updates the new and current AI-pot at the end of the round.
     * @param ai Which AI to update values for.
     */
    public void endOfRound(int ai) {
        Platform.runLater(new Runnable() {
            private volatile boolean shutdown;

            @Override
            public void run() {

                while (!shutdown) {
                    setLabelUIAiBarPot(ai, Integer.toString(aiPlayers.get(ai).aiPot()));
                    setLabelUIAiBarAction(ai, "");
                    shutdown = true;
                }
            }
        });
    }


    public void setSPController(SPController spController) {
        this.spController = spController;
    }

    public void setGameController(GameController gController) {
        this.gController = gController;
    }

    public void setAiPositions(int[][] aiPositions) {
        this.aiPositions = aiPositions;
    }

    public void setCollectionOfLabelsAi(Label[][] aiLabels) {
        this.collectionOfLabelsAi = aiLabels;
    }

    public void setCollectionOfCardsAi(ImageView[] aiCards) {
        this.collectionOfCardsAi = aiCards;
    }


}

