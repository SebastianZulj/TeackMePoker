package com.example.demo.controller;

import java.util.*;

import com.example.demo.aiClass.Ai;
import com.example.demo.deck.Card;
import com.example.demo.deck.CardValue;
import com.example.demo.deck.Deck;
import com.example.demo.deck.Suit;
import com.example.demo.gui.WinnerCallback;
import javafx.application.Platform;

import javax.swing.plaf.SeparatorUI;


/**
 *
 * 
 * @author Rikard Almgren
 * @version 1.0
 *
 */
public class SPController extends Thread {

  private Deck deck;
  private LinkedList<Ai> aiPlayers = new LinkedList<Ai>();
  private int noOfAi;
  private int playTurn = 0;
  private int dealer = 0;
  private int currentPlayer = 0;
  private int bigBlindPlayer;
  private int smallBlindPlayer;
  private int smallBlind;
  private int bigBlind = 10;
  private int potSize;
  private int currentPotSize;
  private int currentMaxBet;
  private int blindCounter;
  private Card card1;
  private Card card2;
  private Card turn;
  private Card river;
  private Card[] flop = new Card[3];
  private int noOfPlayers = 0;
  private boolean allCalledorFolded = false;
  private boolean winnerDeclared = false;
  private ArrayList<String> name = new ArrayList<String>();
  private GameController gController;
  private int fixedNrOfAIs;
  private int[][] potSplits;
  private boolean doAllInCheck;
  private int psCounter = 0;
  private boolean isOkButtonClicked = false;

  /**
   * Method which receives and sets a number of starting variables and for the game to be set up.
   * 
   * @param noOfAi Number of AI-players
   * @param potSize The potsize for the table(game).
   * @param playerName The players' name.
   */
  public void startGame(int noOfAi, int potSize, String playerName) {
    this.fixedNrOfAIs = noOfAi;
    gController.disableButtons();
    this.potSize = potSize;
    this.noOfAi = noOfAi;
    setNames();
    noOfPlayers = noOfAi + 1;
    bigBlind = (int) (potSize / noOfPlayers * 0.02); // Calculates bigBlind
    if (bigBlind < 2) {
      bigBlind = 2;
    }
    currentMaxBet = bigBlind;
    this.smallBlind = bigBlind / 2;
    gController.setPlayerPot((potSize / noOfPlayers));
    // create aiPlayers
    for (int i = 0; i < noOfAi; i++) {
      aiPlayers.add(new Ai(potSize / (noOfPlayers), name.remove(0)));
    }
    gController.setAiPlayers(aiPlayers, false, 69);
    potSplits = new int[noOfPlayers][1];

    try {
      setupPhase();
    } catch (InstantiationException | IllegalAccessException e) {
      e.printStackTrace();
    }
  }

  /**
   * Method which sets a GameController, the controller that controls the GUI while the game is
   * running.
   * 
   * @param gController An instance of GameController
   */
  public void setGameController(GameController gController) {
    this.gController = gController;
  }

  /**
   * Method which returns the current max bet for the table.
   * @return currentMaxbet the current max bet
   */
  public int getCurrentMaxBet() {
    return currentMaxBet;
  }

  /**
   * Method which returns the current potsize.
   * @return potSize The pot.
   */
  public int getPotSize() {
    return potSize;
  }

  /**
   * Method that creates a list of names for AI-Players to pull from
   */
  public void setNames() {
    name.add("Max");
    name.add("Vedrana");
    name.add("Lykke");
    name.add("Amin");
    name.add("Rikard");
    name.add("Kristina");
    name.add("Rolf");
    Collections.shuffle(name);
  }

  /**
   * Method which prepares a new gameround.
   * @throws IllegalAccessException
   * @throws InstantiationException
   */
  private void setupPhase() throws InstantiationException, IllegalAccessException {
    // Check if the player lost last turn
    if (gController.getPlayerPot() > bigBlind) {
      /*
       * if not, reset the all-in check and potsplit counter Create a new deck, shuffle it and deal
       * cards
       */
      doAllInCheck = false;
      psCounter = 0;
      deck = new Deck();
      deck.shuffle();
      card1 = deck.getCard();
      card2 = deck.getCard();
      gController.setStartingHand(card1, card2);
      this.currentPotSize = 0;
      potSplits = new int[noOfPlayers][1];
      gController.updatePots(potSplits, currentPotSize);
      gController.playerReset("");
      /*
       * Reset the AI players unless they've lost
       */
      for (Ai ai : aiPlayers) {
        System.out.println(ai.getName() + " : " + ai.getDecision() + (ai.aiPot() < bigBlind));
        ai.setBigBlind(0, false);
        ai.setSmallBlind(0, false);
        ai.setPaidThisTurn(0);
        ai.setAllInViability(99);
        if (!ai.getDecision().contains("lost")) {
          ai.setDecision("");
          card1 = deck.getCard();
          card2 = deck.getCard();
          ai.setStartingHand(card1, card2);
        }
      }
      // set the blinds
      setBlinds(noOfPlayers);
      // Generate a flop, turn and river.
      for (int i = 0; i < flop.length; i++) {
        flop[i] = deck.getCard();
      }
      turn = deck.getCard();
      river = deck.getCard();
      // If thread isn't active, start, else run it again.
      if (!this.isAlive()) {
        start();
      } else {
        run();
      }
      // If the player did lose, make sure he knows it.
    } else {
      gController.playerLost();
    }
  }

  /**
   * implements callback to know when winner box has been closed
   */
  WinnerCallback winnerCallback = new WinnerCallback() {
    @Override
    public void onWinnerBoxClosed(boolean okButtonClicked) {
      synchronized (this) {
        isOkButtonClicked = okButtonClicked;
        notify(); //notify waiting thread (run) that callback has been called
      }
    }
  };

  /**
   * Method that runs the gameround itself
   */
  public void run() {
    gController.hideAllIn();
    gController.activeSlider();
    String winner = "";

    Card[] turnCards = {flop[0], flop[1], flop[2], turn};
    Card[] riverCards = {flop[0], flop[1], flop[2], turn, river};
    while (playTurn < 4) {
      gController.roundStatus(playTurn);
      // set dealer, smallBlind and bigBlind.
      if (playTurn == 0) {
        int playerNr = noOfPlayers - 1;
        if (playerNr != 1) {
          try {
            if (dealer != playerNr) {
              Thread.sleep(1000);
              gController.aiAction(dealer, "Dealer");
            }
            if (smallBlindPlayer != playerNr) {
              Thread.sleep(1000);
              gController.aiAction(smallBlindPlayer, "SmallBlind");
            }
            if (bigBlindPlayer != playerNr) {
              Thread.sleep(1000);
              gController.aiAction(bigBlindPlayer, "BigBlind");
            }
          } catch (InterruptedException e) {
            e.printStackTrace();
          }
        }
      } else if (playTurn == 1) {
        gController.setFlopTurnRiver(flop);
      } else if (playTurn == 2) {
        gController.setFlopTurnRiver(turnCards);
      } else if (playTurn == 3) {
        gController.setFlopTurnRiver(riverCards);
      }

      while (!allCalledorFolded) {
        // Check if it's the players turn.
        if (currentPlayer == noOfPlayers - 1) {
          if (!gController.getPlayerDecision().equals("fold")
              && !gController.getPlayerDecision().contains("allin")) {
            if (!(checkLivePlayers() > 1)) {
              gController.setPlayerPot(currentPotSize);
              winner = gController.getUsername();
              System.out.println("set winner lbl 1");
              gController.setWinnerLabel(winner, 99, winnerCallback);
              winnerDeclared = true;
              break;
            }
            try {
              Thread.sleep(1000);
            } catch (InterruptedException e) {
              e.printStackTrace();
            }
            askForPlayerDecision(currentMaxBet);
          }
          // if it isn't the players turn, let the AI do their turn
        } else {
          if (!aiPlayers.get(currentPlayer).getDecision().contains("lost")) {
            if (!aiPlayers.get(currentPlayer).getDecision().contains("fold")
                && !aiPlayers.get(currentPlayer).getDecision().contains("all-in")) {
              if (!(checkLivePlayers() > 1)) {
                aiPlayers.get(currentPlayer).updateWinner(currentPotSize);
                winner = aiPlayers.get(currentPlayer).getName();
                System.out.println("set winner lbl 2");
                gController.setWinnerLabel(winner, 98, winnerCallback);
                winnerDeclared = true;
                break;
              }

              askForAiDecision();
              try {
                Thread.sleep(1000);
              } catch (InterruptedException e) {
                e.printStackTrace();
              }
            }
          }
        }
        // After each player(AI or real), update the pot(s)
        gController.updatePots(potSplits, currentPotSize);
        // Prevent AI from thinking it's a new turn.
        if (currentPlayer != noOfPlayers - 1) {
          aiPlayers.get(currentPlayer).setSameTurn(true);
        }
        // move on to the next player
        currentPlayer = (currentPlayer + 1) % noOfPlayers;
        // check if everyone has checked, called or folded.
        allCallorFold(aiPlayers, gController.getPlayerDecision());
      }
      // Next turn
      playTurn++;
      allCalledorFolded = false;
      // if a player Hasn't folded, gone all in or lost, reset their decision
      for (Ai ai : aiPlayers) {
        if (!ai.getDecision().contains("fold") && !ai.getDecision().contains("lost")
            && !ai.getDecision().contains("all-in")) {
          ai.setDecision("");
          ai.setSameTurn(false);
        }
      }
      // if winner was declared earlier, break the loop
      if (winnerDeclared) {
        break;
      }
    }
    // If the game goes to the final round and no one has won yet, check the winner.
    if (playTurn >= 4 && !winnerDeclared) {
      checkWinner();
      winnerDeclared = true;
    }
    // If an AI player has run out of money, they have lost.
    for (Ai ai : aiPlayers) {
      if (ai.aiPot() < bigBlind && !ai.getDecision().contains("lost")) {
        ai.setDecision("lost");
        ai.updateWinner(-ai.aiPot());
        gController.setUIAiStatus(aiPlayers.indexOf(ai), "inactive"); //TODO: fix
      }
      System.out.println(ai.getName() + " : " + ai.getDecision() + (ai.aiPot() < bigBlind));
    }

    synchronized (winnerCallback) {
      try {
        winnerCallback.wait(); //wait for callback
      } catch (InterruptedException e) {
        e.printStackTrace();
      }
    }

    //ask if player wants to play again after waiting for callback
    if (winnerDeclared && isOkButtonClicked) { //if winner declared and winner box closed
      if (askReplay()) { //wants to play again
        // Reset values
        winnerDeclared = false;
        playTurn = 0;
        blindCounter++;
        // update the blinds
        if (blindCounter >= 15) {
          bigBlind += (int) (potSize / noOfPlayers * 0.02);
          currentMaxBet = bigBlind;
          smallBlind = bigBlind / 2;
          blindCounter = 0;
        }
        // Set new dealer
        dealer = (dealer + 1) % noOfPlayers;

        try {
          setupPhase(); //prepares new game round
        } catch (InstantiationException e) {
          e.printStackTrace();
        } catch (IllegalAccessException e) {
          e.printStackTrace();
        }
      } else { //don't want to play again
        Platform.runLater(() -> gController.changeToMainMenu());
      }
    }
  }

  public boolean askReplay() {
    return gController.askReplay();
  }


  /**
   * Method which checks who the winner is.
   */
  private void checkWinner() {
    // if someone has gone all in, check winners through the all-in method instead.
    if (doAllInCheck) {
      checkAllInWinners();
    } else {
      // List of "second winners", on the rare occasion of people having the same handstrength and
      // highcard.
      ArrayList<Integer> secWin = new ArrayList<Integer>();

      String winner = "";
      int bestHand = 0;
      Ai bestHandPlayer = new Ai(0, "");
      /*
       * Go through all AI players that have not folded, check which player has the best hand. That
       * player is now the bestHandPlayer
       */
      for (Ai ai : aiPlayers) {
        if (!ai.getDecision().equals("fold")) {
          if (ai.handStrength() > bestHand) {
            bestHandPlayer = ai;
            bestHand = ai.handStrength();
            secWin.clear();
          } else if (ai.handStrength() == bestHand) {
            if (ai.getHighCard() > bestHandPlayer.getHighCard()) {
              bestHandPlayer = ai;
              bestHand = ai.handStrength();
              secWin.clear();
            } else if (ai.getHighCard() == bestHandPlayer.getHighCard()) {
              secWin.add(aiPlayers.indexOf((ai)));
            }
          }
        }
      }
      // If the player hasn't folded, compare the players hand to that of the best AI player.
      if (!gController.getPlayerDecision().contains("fold")) {
        // Player wins
        if (gController.getHandStrength() > bestHand) {
          gController.setPlayerPot(currentPotSize);
          winner = gController.getUsername() + " med " + getWinnerCards(bestHandPlayer);
          System.out.println("set winner lbl 3");
          System.out.println(getWinnerCards(bestHandPlayer) + " was the winning hand");

          gController.setWinnerLabel(winner, gController.getHandStrength(), winnerCallback);
          // draw
        } else if (gController.getHandStrength() == bestHand) {
          // Player wins
          if (gController.getGetHighCard() > bestHandPlayer.getHighCard()) {
            gController.setPlayerPot(currentPotSize);
            winner = gController.getUsername() + " med " + getWinnerCards(bestHandPlayer);
            System.out.println("set winner lbl 4");
            System.out.println(getWinnerCards(bestHandPlayer) + " was the winning hand");
            gController.setWinnerLabel(winner, gController.getHandStrength(), winnerCallback);
            // Draw
          } else if (gController.getGetHighCard() == bestHandPlayer.getHighCard()) {
            bestHandPlayer.updateWinner(currentPotSize / 2);
            gController.setPlayerPot(currentPotSize / 2);
            winner = gController.getUsername() + " och " + bestHandPlayer.getName() + " med " + getWinnerCards(bestHandPlayer);
            System.out.println("set winner lbl 5");
            System.out.println(getWinnerCards(bestHandPlayer) + " was the winning hand");
            gController.setWinnerLabel(winner, bestHand, winnerCallback);
            // AI wins and there are second winners.
          } else {
            if (!secWin.isEmpty()) {
              int divBy = currentPotSize = secWin.size();
              for (int i : secWin) {
                aiPlayers.get(i).updateWinner(divBy);
              }
              // Ai wins and there aren't
            } else {
              bestHandPlayer.updateWinner(currentPotSize);
              winner = bestHandPlayer.getName() + " med " + getWinnerCards(bestHandPlayer);
              System.out.println("set winner lbl 6");
              System.out.println(getWinnerCards(bestHandPlayer) + " was the winning hand");
              gController.setWinnerLabel(winner, bestHand, winnerCallback);
            }
          }
          // Same thing as above but the player lost and no draw.
        } else {
          if (!secWin.isEmpty()) {
            int divBy = currentPotSize = secWin.size();
            for (int i : secWin) {
              aiPlayers.get(i).updateWinner(divBy);
            }
          } else {
            bestHandPlayer.updateWinner(currentPotSize);
            winner = bestHandPlayer.getName() + " med " + getWinnerCards(bestHandPlayer);
            System.out.println("set winner lbl 7");
            System.out.println(getWinnerCards(bestHandPlayer) + " was the winning hand");
            gController.setWinnerLabel(winner, bestHand, winnerCallback);
          }
        }
        // Same thing as above but the player had folded.
      } else {
        if (!secWin.isEmpty()) {
          int divBy = currentPotSize = secWin.size();
          for (int i : secWin) {
            aiPlayers.get(i).updateWinner(divBy);
          }
        } else {
          bestHandPlayer.updateWinner(currentPotSize);
          winner = bestHandPlayer.getName() + " med " + getWinnerCards(bestHandPlayer);
          System.out.println("set winner lbl 8");
          System.out.println(getWinnerCards(bestHandPlayer) + " was the winning hand");

          gController.setWinnerLabel(winner, bestHand, winnerCallback);
        }
      }
    }

  }

  /***
   * This method is a more testabe variant of the method created by the original authors. It returns a value to be
   * able to easier test it aswell as it takes in a GameController and the AI players in the parameters so that they
   * don't need to be initialised before.
   *
   * @author Fabian Kjellberg
   * @param gController
   * @param aiPlayers
   * @return
   */
  public String checkWinner(GameController gController, List<Ai> aiPlayers) {
    // if someone has gone all in, check winners through the all-in method instead.
    if (doAllInCheck) {
      checkAllInWinners();
    } else {
      // List of "second winners", on the rare occasion of people having the same handstrength and
      // highcard.
      ArrayList<Integer> secWin = new ArrayList<Integer>();

      String winner = "";
      int bestHand = 0;
      Ai bestHandPlayer = new Ai(0, "");
      /*
       * Go through all AI players that have not folded, check which player has the best hand. That
       * player is now the bestHandPlayer
       */
      for (Ai ai : aiPlayers) {
        if (!ai.getDecision().equals("fold")) {
          if (ai.handStrength() > bestHand) {
            bestHandPlayer = ai;
            bestHand = ai.handStrength();
            secWin.clear();
          } else if (ai.handStrength() == bestHand) {
            if (ai.getHighCard() > bestHandPlayer.getHighCard()) {
              bestHandPlayer = ai;
              bestHand = ai.handStrength();
              secWin.clear();
            } else if (ai.getHighCard() == bestHandPlayer.getHighCard()) {
              secWin.add(aiPlayers.indexOf((ai)));
            }
          }
        }
      }
      // If the player hasn't folded, compare the players hand to that of the best AI player.
      if (!gController.getPlayerDecision().contains("fold")) {
        // Player wins
        if (gController.getHandStrength() > bestHand) {
          gController.setPlayerPot(currentPotSize);
          winner = gController.getUsername() + " med " + getWinnerCards(bestHandPlayer);
          System.out.println("set winner lbl 3");
          System.out.println(getWinnerCards(bestHandPlayer) + " was the winning hand");

          gController.setWinnerLabel(winner, gController.getHandStrength(), winnerCallback);
          return winner;
          // draw
        } else if (gController.getHandStrength() == bestHand) {
          // Player wins
          if (gController.getGetHighCard() > bestHandPlayer.getHighCard()) {
            gController.setPlayerPot(currentPotSize);
            winner = gController.getUsername() + " med " + getWinnerCards(bestHandPlayer);
            System.out.println("set winner lbl 4");
            System.out.println(getWinnerCards(bestHandPlayer) + " was the winning hand");
            gController.setWinnerLabel(winner, gController.getHandStrength(), winnerCallback);
            return winner;
            // Draw
          } else if (gController.getGetHighCard() == bestHandPlayer.getHighCard()) {
            bestHandPlayer.updateWinner(currentPotSize / 2);
            gController.setPlayerPot(currentPotSize / 2);
            winner = gController.getUsername() + " och " + bestHandPlayer.getName() + " med " + getWinnerCards(bestHandPlayer);
            System.out.println("set winner lbl 5");
            System.out.println(getWinnerCards(bestHandPlayer) + " was the winning hand");
            gController.setWinnerLabel(winner, bestHand, winnerCallback);
            return winner;
            // AI wins and there are second winners.
          } else {
            if (!secWin.isEmpty()) {
              int divBy = currentPotSize = secWin.size();
              for (int i : secWin) {
                aiPlayers.get(i).updateWinner(divBy);
              }
              // Ai wins and there aren't
            } else {
              bestHandPlayer.updateWinner(currentPotSize);
              winner = bestHandPlayer.getName() + " med " + getWinnerCards(bestHandPlayer);
              System.out.println("set winner lbl 6");
              System.out.println(getWinnerCards(bestHandPlayer) + " was the winning hand");
              gController.setWinnerLabel(winner, bestHand, winnerCallback);
              return winner;
            }
          }
          // Same thing as above but the player lost and no draw.
        } else {
          if (!secWin.isEmpty()) {
            int divBy = currentPotSize = secWin.size();
            for (int i : secWin) {
              aiPlayers.get(i).updateWinner(divBy);
            }
          } else {
            bestHandPlayer.updateWinner(currentPotSize);
            winner = bestHandPlayer.getName() + " med " + getWinnerCards(bestHandPlayer);
            System.out.println("set winner lbl 7");
            System.out.println(getWinnerCards(bestHandPlayer) + " was the winning hand");
            gController.setWinnerLabel(winner, bestHand, winnerCallback);
            return winner;
          }
        }
        // Same thing as above but the player had folded.
      } else {
        if (!secWin.isEmpty()) {
          int divBy = currentPotSize = secWin.size();
          for (int i : secWin) {
            aiPlayers.get(i).updateWinner(divBy);
          }
        } else {
          bestHandPlayer.updateWinner(currentPotSize);
          winner = bestHandPlayer.getName() + " med " + getWinnerCards(bestHandPlayer);
          System.out.println("set winner lbl 8");
          System.out.println(getWinnerCards(bestHandPlayer) + " was the winning hand");

          gController.setWinnerLabel(winner, bestHand, winnerCallback);
          return winner;
        }
      }
    }
    return "null";
  }



  /**
   * Method which returns the winning cards for the winner.
   * @param winner winner of the round
   * @return winningCards the winning cards
   */
  public String getWinnerCards(Ai winner) {
    String winningCards = "";
    String[] cardOneArray = winner.getAiCards().get(0).split(","); // 0 = value, 1 = suit
    char suitOne = Character.toLowerCase(cardOneArray[1].charAt(0));
    int valueOne = Integer.parseInt(cardOneArray[0]);
    Card cardOne = new Card(Suit.fromSuitCode(suitOne), CardValue.fromCardValueCode(valueOne), null);
    String cardOneString = cardOne.toSwedishString();

    String[] cardTwoArray = winner.getAiCards().get(1).split(",");
    char suitTwo = Character.toLowerCase(cardTwoArray[1].charAt(0));
    int valueTwo = Integer.parseInt(cardTwoArray[0]);
    Card cardTwo = new Card(Suit.fromSuitCode(suitTwo), CardValue.fromCardValueCode(valueTwo), null);
    String cardTwoString = cardTwo.toSwedishString();

    return cardOneString + " och " + cardTwoString;
  }

  /**
   * Method which checks the winners if there was one or more all-ins.
   * This method does the same thing as checkWinners except the pot is split over multiple subpots
   * and one winner is declared for each subpot.
   */
  private void checkAllInWinners() {
    HashMap<String, String> checkMap = new HashMap<>(); //to check if a given winner has been declared already

    int allInPotSize;
    for (int i = potSplits.length - 1; i >= 0; i--) {
      if (potSplits[i][0] > 0) {
        allInPotSize = potSplits[i][0];
        for (Ai test : aiPlayers) {
          if (test.getAllInViability() <= i && !test.getDecision().contains("fold")
              && !test.getDecision().contains("lost")) {
            potSplits[i][0] += potSplits[i][0];
          }
        }
        potSplits[i][0] -= potSplits[i][0];

        currentPotSize -= potSplits[i][0];
        ArrayList<Integer> secWin = new ArrayList<Integer>();

        String winner = "";
        int bestHand = 0;
        Ai bestHandPlayer = new Ai(0, "");
        for (Ai ai : aiPlayers) {
          if ((!ai.getDecision().equals("fold") && !ai.getDecision().contains("lost"))
              && ai.getAllInViability() <= i) {
            if (ai.handStrength() > bestHand) {
              bestHandPlayer = ai;
              bestHand = ai.handStrength();
              secWin.clear();
            } else if (ai.handStrength() == bestHand) {
              if (ai.getHighCard() > bestHandPlayer.getHighCard()) {
                bestHandPlayer = ai;
                bestHand = ai.handStrength();
                secWin.clear();
              } else if (ai.getHighCard() == bestHandPlayer.getHighCard()) {
                secWin.add(aiPlayers.indexOf((ai)));
              }
            }
          }
        }
        if (!gController.getPlayerDecision().contains("fold")
            && gController.getAllInViability() <= i) {
          if (gController.getHandStrength() > bestHand) {
            gController.setPlayerPot(allInPotSize);
            winner = gController.getUsername();
            System.out.println("set winner lbl 9");
            if (!checkMap.containsKey("9")) {
              gController.setWinnerLabel(winner, gController.getHandStrength(), winnerCallback);
              checkMap.put("9", null);
            }
          } else if (gController.getHandStrength() == bestHand) {
            if (gController.getGetHighCard() > bestHandPlayer.getHighCard()) {
              gController.setPlayerPot(allInPotSize);
              winner = gController.getUsername();
              System.out.println("set winner lbl 10");
              if (!checkMap.containsKey("10")) {
                gController.setWinnerLabel(winner, gController.getHandStrength(), winnerCallback);
                checkMap.put("10", null);
              }
            } else if (gController.getGetHighCard() == bestHandPlayer.getHighCard()) {
              bestHandPlayer.updateWinner(allInPotSize / 2);
              gController.setPlayerPot(allInPotSize / 2);
              winner = gController.getUsername() + " och " + bestHandPlayer.getName();
              System.out.println("set winner lbl 11");
              if (!checkMap.containsKey("11")) {
                gController.setWinnerLabel(winner, gController.getHandStrength(), winnerCallback);
                checkMap.put("11", null);
              }
            } else {
              if (!secWin.isEmpty()) {
                int divBy = allInPotSize = secWin.size();
                for (int x : secWin) {
                  aiPlayers.get(x).updateWinner(divBy);
                }
              } else {
                bestHandPlayer.updateWinner(allInPotSize);
                winner = bestHandPlayer.getName();
                System.out.println("set winner lbl 12");
                if (!checkMap.containsKey("12")) {
                  gController.setWinnerLabel(winner, gController.getHandStrength(), winnerCallback);
                  checkMap.put("12", null);
                }
              }
            }
          } else {
            if (!secWin.isEmpty()) {
              int divBy = allInPotSize = secWin.size();
              for (int x : secWin) {
                aiPlayers.get(x).updateWinner(divBy);
              }
            } else {
              bestHandPlayer.updateWinner(allInPotSize);
              winner = bestHandPlayer.getName();
              System.out.println("set winner lbl 13");
              if (!checkMap.containsKey("13")) {
                gController.setWinnerLabel(winner, gController.getHandStrength(), winnerCallback);
                checkMap.put("13", null);
              }
            }
          }
        } else {
          if (!secWin.isEmpty()) {
            int divBy = allInPotSize = secWin.size();
            for (int x : secWin) {
              aiPlayers.get(x).updateWinner(divBy);
            }
          } else {
            bestHandPlayer.updateWinner(allInPotSize);
            winner = bestHandPlayer.getName();
            System.out.println("set winner lbl 14");
            if (!checkMap.containsKey("14")) {
              gController.setWinnerLabel(winner, gController.getHandStrength(), winnerCallback);
              checkMap.put("14", null);
            }
          }
        }
      }
    }

  }


  /**
   * Method which checks the amount of "living" players. The amount of players whose decision is not
   * fold.
   * @return Number of "living" players
   */
  private int checkLivePlayers() {
    int livePlayers = 0;
    for (Ai ai : aiPlayers) {
      if (!ai.getDecision().equals("fold") && !ai.getDecision().contains("lost")) {
        livePlayers++;
      }
    }
    if (!gController.getPlayerDecision().equals("fold")) {
      livePlayers++;
    }
    return livePlayers;
  }


  /**
   * Method which asks the GUi to give the player a choice and calls an action when a decision has
   * been made.
   * @param currentMaxBet2 the currentmaxbet.
   */
  private void askForPlayerDecision(int currentMaxBet2) {
    if (!gController.getPlayerDecision().contains("allin")) {
      gController.askForPlayerDecision();
      playerAction();
    } else {
      allCallorFold(aiPlayers, gController.getPlayerDecision());
    }
  }

  /***
   * This method was suggested to change to make the method more testable. It is currently not working due to the
   * variables needed to be initialized in the gController. gController needs to be set up better to more easily test
   *
   * @param gController
   * @param aiPlayers
   * @author Fabian Kjellberg
   * @return
   */
  private String askForPlayerDecision(GameController gController, ArrayList<Ai> aiPlayers) {
    if (!gController.getPlayerDecision().contains("allin")) {
      gController.askForPlayerDecision();
      playerAction();
      return gController.getPlayerDecision();
    } else {
      allCallorFold(aiPlayers, gController.getPlayerDecision());
      return gController.getPlayerDecision();
    }
  }


  /**
   * A method which controls what to do depending on the players' action.
   */
  private void playerAction() {
    String playerDecision = gController.getPlayerDecision();
    playerDecision.toLowerCase();

    String[] split;
    if (playerDecision.contains("raise")) {
      split = playerDecision.split(",");
      currentMaxBet = Integer.parseInt(split[1]);
      currentPotSize += Integer.parseInt(split[1]);
    } else if (playerDecision.contains("fold")) {
      // do nothing. Handled elsewhere.
    } else if (playerDecision.contains("call")) {
      split = playerDecision.split(",");
      currentPotSize += currentMaxBet;
    } else if (playerDecision.contains("check")) {
      // do nothing. Handled elsewhere.
    } else if (playerDecision.contains("allin")) {
      split = playerDecision.split(",");
      int allin = Integer.parseInt(split[1]);
      // if all-in
      if (currentMaxBet < allin) {

        if ((Integer.parseInt(split[1]) + Integer.parseInt(split[2])) > currentMaxBet) {
          currentMaxBet = Integer.parseInt(split[1]) + Integer.parseInt(split[2]);
        }
        currentPotSize += allin;
        allin = currentPotSize;
        doAllInCheck = true;
        potSplits[psCounter][0] = allin;
        gController.setAllInViability(psCounter);
        // Check if AiPlayers are viable for the same subpot
        for (Ai aips : aiPlayers) {
          if ((aips.getPaidThisTurn() + aips.aiPot()) > allin) {
            aips.setAllInViability(psCounter);
          }
        }
        psCounter++;
      } else {
        if ((Integer.parseInt(split[1]) + Integer.parseInt(split[2])) > currentMaxBet) {
          currentMaxBet = Integer.parseInt(split[1]) + Integer.parseInt(split[2]);
        }

        currentPotSize += allin;
        allin = currentPotSize;
        doAllInCheck = true;
        potSplits[psCounter][0] = allin;
        gController.setAllInViability(psCounter);

        // Check if AiPlayers are viable for the same subpot
        for (Ai aips : aiPlayers) {
          if ((aips.getPaidThisTurn() + aips.aiPot()) > allin) {
            aips.setAllInViability(psCounter);
          }
        }
        psCounter++;
      }
    }
    // Check all call or fold
    allCallorFold(aiPlayers, gController.getPlayerDecision());
  }


  /**
   * Method which asks the current AIplayer to make a decision based on the current max bet.
   */
  private void askForAiDecision() {

    Ai ai = aiPlayers.get(currentPlayer);
    // Starting Hand
    if (playTurn == 0) {
      ai.makeDecision(currentMaxBet);
      aiAction(currentPlayer);
      // Flop
    } else if (playTurn == 1) {
      ai.makeDecision(currentMaxBet, flop);
      aiAction(currentPlayer);
      // Turn
    } else if (playTurn == 2) {
      ai.makeDecision(currentMaxBet, turn);
      aiAction(currentPlayer);
      // River
    } else if (playTurn == 3) {
      ai.makeDecision(currentMaxBet, river);
      aiAction(currentPlayer);
    }
    // Check all call or fold
    allCallorFold(aiPlayers, gController.getPlayerDecision());
  }


  /**
   * Method which controls what to do depending on the Ai players' action.
   * @param currentPlayer current AI player
   */
  private void aiAction(int currentPlayer) {
    Ai ai = aiPlayers.get(currentPlayer);

    String aiDecision = ai.getDecision();
    String[] split;
    if (aiDecision.contains("raise")) {
      split = aiDecision.split(",");
      currentMaxBet = Integer.parseInt(split[1]);
      currentPotSize += Integer.parseInt(split[1]);
      gController.aiAction(currentPlayer, aiDecision);

    } else if (aiDecision.contains("fold")) {
      gController.aiAction(currentPlayer, aiDecision);
    } else if (aiDecision.contains("call")) {

      split = aiDecision.split(",");
      if (Integer.parseInt(split[1]) > currentMaxBet) {
        currentMaxBet = Integer.parseInt(split[1]);
        currentPotSize += Integer.parseInt(split[1]);
      } else {
        currentPotSize += Integer.parseInt(split[1]);
      }

      if (Integer.parseInt(split[1]) <= 0) {
        gController.aiAction(currentPlayer, "check");
      } else {
        gController.aiAction(currentPlayer, split[0]);
      }

    } else if (aiDecision.contains("check")) {
      gController.aiAction(currentPlayer, aiDecision);
    } else if (aiDecision.contains("all-in")) {
      split = aiDecision.split(",");
      int allin;
      if (playTurn > 0) {
        if (!doAllInCheck) {
          allin = Integer.parseInt(split[1]) + currentMaxBet;
        } else {
          allin =
              Integer.parseInt(split[1]) + (ai.getPaidThisTurn() - (Integer.parseInt(split[1])));
        }
      } else {
        allin = Integer.parseInt(split[1]);
      }
      if (currentMaxBet < allin) {


        currentMaxBet = allin;

        currentPotSize += allin;

        doAllInCheck = true;
        potSplits[psCounter][0] = allin;
        // Check if the player is viable for the same subpot
        if (gController.getPlayerPot() + gController.getPlayerAlreadyPaid() > allin) {
          gController.setAllInViability(psCounter);
        }
        // Check if AiPlayers are viable for the same subpot
        for (Ai aips : aiPlayers) {
          if ((aips.getPaidThisTurn() + aips.aiPot()) > allin) {
            aips.setAllInViability(psCounter);
          }
        }
        psCounter++;
      } else {


        currentPotSize += allin;
        doAllInCheck = true;
        potSplits[psCounter][0] = allin;
        if (gController.getPlayerPot() + gController.getPlayerAlreadyPaid() > allin) {
          gController.setAllInViability(psCounter);
        }
        // Check if AiPlayers are viable for the same subpot
        for (Ai aips : aiPlayers) {
          if ((aips.getPaidThisTurn() + aips.aiPot()) > allin) {
            aips.setAllInViability(psCounter);
          }
        }
        psCounter++;
      }
      gController.aiAction(currentPlayer, aiDecision);
    }
  }


  /**
   * Method which sets who the small and big blind players are. Depending on who the dealer is.
   * @param noOfPlayers Number of players in the game
   */
  private void setBlinds(int noOfPlayers) {


    currentMaxBet = bigBlind;
    smallBlind = bigBlind / 2;
    // In heads-up play
    if (noOfPlayers == 2) {
      currentPlayer = dealer;
      smallBlindPlayer = dealer;
      bigBlindPlayer = (dealer + 1) % noOfPlayers;
      // in "not" heads up play.
    } else if (noOfPlayers >= 3) {
      currentPlayer = (dealer + 3) % noOfPlayers;
      smallBlindPlayer = (dealer + 1) % noOfPlayers;
      bigBlindPlayer = (dealer + 2) % noOfPlayers;
    }
    // If the intended dealer has lost, shift one step over until a player(AI or otherwise) has not
    // lost.
    while (dealer != noOfPlayers - 1 && aiPlayers.get(dealer).getDecision().contains("lost")) {
      dealer = (dealer + 1) % noOfPlayers;
      smallBlindPlayer = (smallBlindPlayer + 1) % noOfPlayers;
      bigBlindPlayer = (bigBlindPlayer + 1) % noOfPlayers;
    }
    // if the intended smallblind has lost, shift one step over until a player(AI or otherwise) has
    // not lost.
    while (smallBlindPlayer != (noOfPlayers - 1)
        && aiPlayers.get(smallBlindPlayer).getDecision().contains("lost")) {
      smallBlindPlayer = (smallBlindPlayer + 1) % noOfPlayers;
      bigBlindPlayer = (bigBlindPlayer + 1) % noOfPlayers;
    }
    // if the intended bigblind has lost, shift one step over until a player(AI or otherwise) has
    // not lost.
    while (bigBlindPlayer != (noOfPlayers - 1)
        && aiPlayers.get(bigBlindPlayer).getDecision().contains("lost")) {
      bigBlindPlayer = (bigBlindPlayer + 1) % noOfPlayers;
    }
    // set small and bigBlind
    if (smallBlindPlayer == noOfPlayers - 1) {
      gController.playerSmallBlind(smallBlind);
      aiPlayers.get(bigBlindPlayer).setBigBlind(bigBlind, true);
    } else if (bigBlindPlayer == noOfPlayers - 1) {
      aiPlayers.get(smallBlindPlayer).setSmallBlind(smallBlind, true);
      gController.playerBigBlind(bigBlind);
    } else {

      aiPlayers.get(smallBlindPlayer).setSmallBlind(smallBlind, true);
      aiPlayers.get(bigBlindPlayer).setBigBlind(bigBlind, true);
      aiPlayers.get(smallBlindPlayer).setDecision("SmallBlind");
      aiPlayers.get(bigBlindPlayer).setDecision("BigBlind");

      // sets dealer as well
    }
    if (dealer != noOfPlayers - 1) {
    } else {
      gController.playerIsDealer(dealer);
    }
    // update GUI.
    gController.setBlindsMarker(dealer, smallBlindPlayer, bigBlindPlayer);
    this.currentPotSize = smallBlind + bigBlind;
    gController.updatePots(potSplits, currentPotSize);
  }


  /**
   * Method which checks if everyone has folded or checked/called.
   * UNUSED METHOD SUGGESTED UPDATE
   */
  public void allCallorFold() {

    int noOfAIFoldedorCalled = 0;
    // For each AI player
    for (Ai ai : aiPlayers) {
      // Check if folded.
      if (ai.getDecision().contains("fold") || ai.getDecision().contains("lost")) {
        noOfAIFoldedorCalled++;
        // if not folded, check if checked or called.
      } else if (ai.getDecision().contains("call") && ai.getPaidThisTurn() == currentMaxBet
          || ai.getDecision().contains("check") && ai.getPaidThisTurn() == currentMaxBet
          || ai.getDecision().contains("all-in")) {
        noOfAIFoldedorCalled++;
        // if neither checked, called or folded, at least one AI is live.
      } else {
        allCalledorFolded = false;
      }
    }
    // If all AI have folded or called, check if player has folded or called.
    if (noOfAIFoldedorCalled >= noOfAi) {
      String[] split = gController.getPlayerDecision().split(",");

      if (gController.getPlayerDecision().contains("fold")
          || gController.getPlayerDecision().contains("call")) {
        allCalledorFolded = true;
      } else if (gController.getPlayerDecision().contains("raise")
          && Integer.parseInt(split[1]) == currentMaxBet) {
        allCalledorFolded = true;
      } else if (gController.getPlayerDecision().contains("check")
          || gController.getPlayerDecision().contains("allin")) {
        allCalledorFolded = true;
      } else {
        allCalledorFolded = false;
      }
    }
  }

  /***
   * Suggested update to the already existing allCallOrFold method which is testable.
   *
   * @param aiPlayers
   * @param playerDecision
   * @return returns a boolean for test purposes aswell as changes the allCalledOrFolded variable.
   * @author Fabian Kjellberg
   */
  public boolean allCallorFold(List<Ai> aiPlayers, String playerDecision){

    //If any player is not call or fold, return false
    for (Ai aiPlayer: aiPlayers){
      if (isFoldedOrLost(aiPlayer));
      else if (isCalledOrCheckedOrAllIn(aiPlayer));
      else {
        allCalledorFolded = false;
        return false;
      }
    }

    //If all ai players are call or fold, and player is call or fold return true
    if(isPlayerDecisionValid(playerDecision)) {
      allCalledorFolded = true;
      return true;
    }

    //if player is not call or fold, return false
    allCalledorFolded = false;
    return false;
  }

  /***
   * This method checks if an AI has decided to choose fold or lost.
   *
   * @param ai
   * @return returns true if ai decision is fold or lost
   * @author Fabian Kjellberg
   */
  private boolean isFoldedOrLost(Ai ai) {
    return ai.getDecision().contains("fold") || ai.getDecision().contains("lost");
  }

  /***
   * this method checks if an AI has decided to choose call, check or all-in
   *
   * @param ai
   * @return returns true if ai decision is call, check or all-in
   * @author Fabian Kjellberg
   */
  private boolean isCalledOrCheckedOrAllIn(Ai ai) {
     if (ai.getDecision().contains("call") && ai.getPaidThisTurn() == currentMaxBet
            || ai.getDecision().contains("check") && ai.getPaidThisTurn() == currentMaxBet
            || ai.getDecision().contains("all-in")) {
      return true;
    } else {
      return false;
    }
  }

  /**
   * Checks to see if the player decision is valid.
   *
   * @param playerDecision
   * @return returns true if player decision is fold, call, raise, check, or all-in
   * @author Fabian Kjellberg
   */
  private boolean isPlayerDecisionValid(String playerDecision) {
      String[] split = playerDecision.split(",");

      if (playerDecision.contains("fold")
              || playerDecision.contains("call")) {
        return true;
      } else if (playerDecision.contains("raise")
              && Integer.parseInt(split[1]) == currentMaxBet) {
        return true;
      } else if (playerDecision.contains("check")
              || playerDecision.contains("allin")) {
        return true;
      } else {
        return false;
      }
  }

  /**
   * Method which returns the small blind value.
   * @return Current small blind
   */
  public int getSmallBlind() {
    return smallBlind;
  }

  /**
   * Method which returns the big blind value.
   * @return Current big blind
   */
  public int getBigBlind() {
    return bigBlind;
  }

  /**
   * Method which Saves chosen number of AIs
   * @return Number of chosen AIs as int
   */
  public int getFixedNrOfAIs() {
    return this.fixedNrOfAIs;
  }

}

