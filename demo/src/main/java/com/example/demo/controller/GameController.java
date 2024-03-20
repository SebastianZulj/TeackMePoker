package com.example.demo.controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.concurrent.CompletableFuture;

import com.example.demo.aiClass.Ai;
import com.example.demo.deck.Card;
import com.example.demo.gui.*;
import com.example.demo.hand.Hand;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;

import javax.swing.*;

/**
 *
 * @author Amin Harirchian, Vedrana Zeba, Lykke Levin, Rikard Almgren
 * @version 1.0
 *
 */
public class GameController {

  @FXML
  private ImageView btCheck;
  @FXML
  private ImageView btCall;
  @FXML
  private ImageView btFold;
  @FXML
  private ImageView btRaise;
  @FXML
  public TextField raiseAmount;
  //@FXML
  //private Slider slider;
  @FXML
  private Label lbPlayerAction;
  @FXML
  private Label lbPotValue;
  @FXML
  private Label lbAllIn;
  @FXML
  private Pane powerBarArea;
  @FXML
  private ImageView cardOne;
  @FXML
  private Pane playerCardsArea;
  @FXML
  private Label adviceLabel;
  @FXML
  private Label helpLabel;
  @FXML
  private Label userName;
  @FXML
  private Label raiseLabel;
  @FXML
  private Pane tabelCardArea;
  @FXML
  private AnchorPane AnchorPaneAll;

  @FXML
  private ImageView imgRoundStatus;
  @FXML
  private Pane paneRounds;


  @FXML
  private ImageView imgPlayerOneCards;
  @FXML
  private ImageView imgPlayerTwoCards;
  @FXML
  private ImageView imgPlayerThreeCards;
  @FXML
  private ImageView imgPlayerFourCards;
  @FXML
  private ImageView imgPlayerFiveCards;


  @FXML
  private Label labelPlayerOneName;
  @FXML
  private Label labelPlayerTwoName;
  @FXML
  private Label labelPlayerThreeName;
  @FXML
  private Label labelPlayerFourName;
  @FXML
  private Label labelPlayerFiveName;

  @FXML
  private Label labelPlayerOnePot;
  @FXML
  private Label labelPlayerTwoPot;
  @FXML
  private Label labelPlayerThreePot;
  @FXML
  private Label labelPlayerFourPot;
  @FXML
  private Label labelPlayerFivePot;

  @FXML
  private Label labelPlayerOneAction;
  @FXML
  private Label labelPlayerTwoAction;
  @FXML
  private Label labelPlayerThreeAction;
  @FXML
  private Label labelPlayerFourAction;
  @FXML
  private Label labelPlayerFiveAction;

  @FXML
  private ImageView imgCard1;
  @FXML
  private ImageView imgCard2;
  @FXML
  private ImageView imgCard3;
  @FXML
  private ImageView imgCard4;
  @FXML
  private ImageView imgCard5;
  @FXML
  private ImageView imgCard6;
  @FXML
  private ImageView imgCard7;

  @FXML
  private ImageView ivBigBlind;
  @FXML
  private ImageView ivSmallBlind;
  @FXML
  private ImageView ivDealer;
  @FXML
  private TitledPane tpHandRanking;
  @FXML
  public ImageView ivSound;
  @FXML
  public MenuItem miNewGame;
  @FXML
  public MenuItem miClose;
  @FXML
  public MenuItem miSettings;
  @FXML
  public MenuItem miAbout;
  @FXML
  public MenuItem miTutorial;

  @FXML
  public Pane panePot;
  @FXML
  public Label subPotOne;
  @FXML
  public Label subPotTwo;
  @FXML
  public Label subPotThree;
  @FXML
  public Label subPotFour;
  @FXML
  public Label subPotFive;
  @FXML
  public Label subPotSix;
  @FXML
  public Label mainPot;
  private WinnerBox winnerBox;
  private ConfirmBox confirmBox;
  private ChangeScene changeScene;
  private int powerBarValue = 0;
  private Image image;
  private ArrayList<Card> cards = new ArrayList<Card>();
  private Hand hand;
  private int tablePotValue = 2000;
  private int playerPot = 0;
  private int alreadyPaid = 0;
  private ImageView imgPowerBar = new ImageView();
  private SPController spController;
  private boolean playerMadeDecision = false;
  private boolean isReady = false;
  private String decision;
  private Card card1;
  private Card card2;
  private int handStrength;
  private LinkedList<Ai> aiPlayers;
  private Label[][] collectionOfLabelsAi;
  private ImageView[] collectionOfCardsAi;
  private ImageView[] collectionOfCardsTable;
  private int[][] aiPositions;
  private int highCard;
  private int prevPlayerActive;
  private String winnerHand = " ";
  private Sound sound = new Sound();
  private TutorialController tutorialWindow;
  private int AllInViability = 0;
  private Label[] collectionOfPots;
  private static final String BASE_PATH = "/com/example/demo/";
  private FileController fileController = new FileController(); //for I/O operations
  private AIController aiController;
  private String raisedAmountString = null;


  /**
   * Method for initializing FXML
   */
  public void initialize() {
    initializeAIController(); //initialize ai controller
    // Groups together labels for each AI-position.
    Label[][] aiLabels = new Label[][] {{labelPlayerOneName, labelPlayerOnePot, labelPlayerOneAction},
            {labelPlayerTwoName, labelPlayerTwoPot, labelPlayerTwoAction},
            {labelPlayerThreeName, labelPlayerThreePot, labelPlayerThreeAction},
            {labelPlayerFourName, labelPlayerFourPot, labelPlayerFourAction},
            {labelPlayerFiveName, labelPlayerFivePot, labelPlayerFiveAction}};
    aiController.setCollectionOfLabelsAi(aiLabels);

    // Placeholders for the AI (based on their position). Shows their cardbacks/no cards or
    // highlighted cards (AI-frame).
    this.collectionOfPots = new Label[6];

    this.collectionOfCardsAi = new ImageView[] {imgPlayerOneCards, imgPlayerTwoCards,
            imgPlayerThreeCards, imgPlayerFourCards, imgPlayerFiveCards};
    aiController.setCollectionOfCardsAi(collectionOfCardsAi);

    // Used to place AI-players into the right position depending on the chosen number of AI:s.
    this.aiPositions = new int[][] {{2}, {0, 2, 4}, {0, 1, 2, 3, 4, 5}};
    aiController.setAiPositions(aiPositions);

    // Table cards placeholders.
    this.collectionOfCardsTable = new ImageView[] {imgCard3, imgCard4, imgCard5, imgCard6, imgCard7};

    // Used by method: inactivateAllAiCardGlows and aiAction.
    this.prevPlayerActive = -1;
  }

  public void initializeAIController() {
    aiController = new AIController();
    aiController.setGameController(this);
  }

  /**
   * @return nbr of previous active players
   */
  public int getPrevPlayerActive() {
    return this.prevPlayerActive;
  }

  public void setPrevPlayerActive(int i) {
    this.prevPlayerActive = i;
  }

  /**
   * Sets the SPController for this gameController
   * @param spController an instance of the class SPController
   */
  public void setSPController(SPController spController) {
    this.spController = spController;
    aiController.setSPController(spController);
    spController.setGameController(this);
  }

  /**
   * Sets the changeScene for this gameController
   * @param sceneChanger an instance of the class ChangeScene
   */
  public void setChangeScene(ChangeScene sceneChanger) {
    this.changeScene = sceneChanger;
  }

  /**
   * Disables all buttons and shows player-frame's action as check.
   */
  public void playerCheck() {
    disableButtons();
    this.decision = "check";
    lbPlayerAction.setText("check");
    playerMadeDecision = true;
    updatePlayerValues("Check");
    sound.playSound("check");
  }

  /**
   * Disables all buttons and shows player-frame's action as fold.
   */
  public void playerFold() {
    disableButtons();
    this.decision = "fold";
    lbPlayerAction.setText("fold");
    playerMadeDecision = true;
    updatePlayerValues("Fold");
    sound.playSound("fold");
  }


  /**
   * Disables all buttons and shows player-frame's action as call, and the called amount. Calculates
   * and withdraws amount from player-pot.
   */
  public void playerCall() {
    disableButtons();
    /*
     * Player's pot - (Current maxbet - already paid (prev rounds)) THE PLAYER'S POT
     */
    this.playerPot -= (spController.getCurrentMaxBet() - alreadyPaid);
    /*
     * Already paid + (Current maxbet - already paid) = WHAT THE PLAYER HAS ALREADY PAID
     */
    this.alreadyPaid += (spController.getCurrentMaxBet() - alreadyPaid);
    this.decision = "call," + Integer.toString(alreadyPaid);
    playerMadeDecision = true;
    sound.playSound("chipSingle");
    updatePlayerValues("Call, §" + Integer.toString(alreadyPaid));

  }


  /**
   * Disables all buttons and shows player-frame's action as raise, and the raised amount.
   * Calculates and withdraws amount from player-pot and adjusts already paid.
   */
  public void playerRaise() {
    // If the player hasn't matched the current maxbet
    if (spController.getCurrentMaxBet() != alreadyPaid) {
    }
    int raisedBet = 0;
    boolean validateRaise = validatePlayerRaise();
    if (validateRaise){
      disableButtons();
      raisedBet = getRaisedAmount();
    }
    else {
      return;
    }
    //int raisedBet = (int) (slider.getValue());
    this.playerPot -= raisedBet;
    /*
     * (raised amount + the amount the player has to match(if the player has to match)) = THE
     * PLAYER'S POT
     */

    this.decision = "raise," + (raisedBet + spController.getCurrentMaxBet()); // Chosen raised amount

    playerMadeDecision = true;
    sound.playSound("chipMulti");

    updatePlayerValues("Raise, §" + raisedBet);

    try {
      if (playerPot == 0) { // Checks if the player has gone all in.
        updatePlayerValues("All-In, §" + raisedBet);
        this.decision = "allin," + (raisedBet) + "," + alreadyPaid;
        this.alreadyPaid += raisedBet;
        //slider.setDisable(true);
        showAllIn();
        disableButtons();

      } else {
        updatePlayerValues("Raise, §" + raisedBet);
        this.alreadyPaid += raisedBet;

        /*
         * Already paid + (raised amount + the amount the player has to match(if the player has to
         * match)) = WHAT THE PLAYER HAS ALREADY PAID
         */
      }
    } catch (Exception e) {
    }

  }

  /**
   * Takes the input text from the textField and checks weather it contains only digits or not. If there
   * is only digits then true, otherwise false.
   * @Author Nicklas Svensson
   * @return validateRaise, true if it is a valid raise amount, false otherwise.
   */

  public boolean validatePlayerRaise(){
      boolean validateRaise = false;
      raisedAmountString = raiseAmount.getText().trim();

      System.out.println("Input received: " + raisedAmountString); // Debugging statement

      if (raisedAmountString.matches("-?\\d+")) {
        int tempRaise = Integer.parseInt(raisedAmountString);

        if (tempRaise <= playerPot && tempRaise > 0) {
          validateRaise = true;
        } else {
          System.err.println("Wrong input! Please enter a positive amount within player's pot.");
          showAlert("Wrong input! Please enter a positive amount within player's pot.");
        }
      } else {
        System.err.println("Wrong input! Please enter a valid amount (Note: No letters). You entered: " + raisedAmountString);
        showAlert("Wrong input! Please enter a valid amount (Note: No letters). You entered: " + raisedAmountString);
      }
      return validateRaise;
    }


  /**
   * The method shows an alert with the message that is passed as a parameter.
   * @Author Nicklas Svensson
   * @param message
   */
      private void showAlert(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Input Error");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
      }
  /**
   * Parses the input from the textField into an integer.
   * @return the amount the player want to raise.
   */
  private int getRaisedAmount(){
    return Integer.parseInt(raisedAmountString);
  }

  /**
   * Updates player-frame's labels (action and player pot) based on action.
   * @param action Call, Check, Raise or Fold
   */
  public void updatePlayerValues(String action) {
    lbPotValue.setText("§" + Integer.toString(playerPot));
    lbPlayerAction.setText(action);
    setSliderValues();
  }

  /**
   * DEPRECATED. Never successfully implemented. //TODO: possible future implementation
   */
  public void saveGame() {}

  /**
   * Sets the slider's min and max values based on the player-pot. Sets minimum sliderValue based on
   * BigBlind.
   */
  public void setSliderValues() {
    /*
    int calcWithdraw = 0;
    if (spController.getCurrentMaxBet() != alreadyPaid) {
      // If the player hasn't matched the current max bet
      calcWithdraw = spController.getCurrentMaxBet() - alreadyPaid;
      // Calculates how much the player has to pay to match it
    }

    slider.setMax(playerPot);
    if (calcWithdraw > spController.getBigBlind()) {
      slider.setMin(calcWithdraw);
    } else if (spController.getBigBlind() <= playerPot) { // Sets minimum value
      // required in order to
      // raise.
      slider.setMin(spController.getBigBlind());
    } else {
      slider.setMin(0);
    }

    if ((slider.getMax() - slider.getMin()) > 4) {
      slider.setMajorTickUnit((slider.getMax() - slider.getMin()) / 4);
    } else {
      slider.setMajorTickUnit(25);
    }
    slider.setMinorTickCount(4);

     */
  }


  /**
   * Mutes the sound on and off.
   */
  public void soundSetting() {
    if(sound.cardFold.getVolume() == 1) {
      sound.cardFold.setVolume(0);
      sound.checkSound.setVolume(0);
      sound.chipMulti.setVolume(0);
      sound.shuffleSound.setVolume(0);
      sound.singleCard.setVolume(0);
      sound.chipSingle.setVolume(0);
      sound.chipMulti.setVolume(0);
      sound.coinSound.setVolume(0);
      sound.wrongSound.setVolume(0);
      Image soundOff = new Image(getClass().getResourceAsStream("/com/example/demo/images/soundButtonMute.png"));
      ivSound.setImage(soundOff);
    } else {
      sound.cardFold.setVolume(1);
      sound.checkSound.setVolume(1);
      sound.chipMulti.setVolume(1);
      sound.shuffleSound.setVolume(1);
      sound.singleCard.setVolume(1);
      sound.chipSingle.setVolume(1);
      sound.chipMulti.setVolume(1);
      sound.coinSound.setVolume(1);
      sound.wrongSound.setVolume(1);
      Image soundOn = new Image(getClass().getResourceAsStream("/com/example/demo/images/soundButton.png"));
      ivSound.setImage(soundOn);

    }
  }

  /**
   * Creates a new ruleController.
   * @throws IOException
   */
  public void rulesState() throws IOException {
    RuleController rc = new RuleController();
    rc.rules();
  }

  /**
   * Method which returns the potValue for the table.
   * @return tablePotValue the potValue for the table.
   */
  public double getPotValue() {
    return tablePotValue;
  }

  /**
   * Sets the player's name.
   * @param name Used to sets the players name on the UI.
   */
  public void setUsername(String name) {
    userName.setText(name);
  }

  /**
   * Returns the players name
   * @return userName the players name.
   */
  public String getUsername() {
    return userName.getText();
  }

  /**
   * Set Allin label visible
   */
  public void showAllIn() {
    lbAllIn.setVisible(true);
  }

  /**
   * Set Allin label deactive
   */
  public void hideAllIn() {
    lbAllIn.setVisible(false);
  }

  /**
   * Set slider active
   */
  public void activeSlider() {
    //slider.setDisable(false);
  }


  /**
   * Sets the starting hand pre-flop for the player.
   * @param card1 First playercard in the hand.
   * @param card2 Second playercard in the hand.
   */
  public void setStartingHand(Card card1, Card card2) {
    isReady = false;
    Platform.runLater(() -> {
      clearFlopTurnRiver(); // Clears the table cards
    });

    Platform.runLater(() -> {
      for (int i = 0; i < 5; i++) { // Resets AI labels and removes all
        // previous glow-effects.
        aiController.setUIAiStatus(i, "idle");
        aiController.setLabelUIAiBarAction(i, "");
      }
    });

    cards.clear(); // Clears previous hand
    this.card1 = card1;
    this.card2 = card2;

    highCard = card1.getCardValue();
    if (card2.getCardValue() > highCard) {
      highCard = card2.getCardValue();
    }
    cards.add(card1); // Adds two cards to hand.
    cards.add(card2);
    this.hand = new Hand(cards);

    isReady = true;
    checkHand();
    handHelp();
  }


  /**
   * Checks the player's hand and gives tips and highlights cards based on the method
   * getHighlightedCards (important during pre-flop situation).
   * @Author Nicklas Svensson
   */
  public void checkHand() {
    Platform.runLater(() -> {
      //try {
      hand.reCalc();
      playerCardsArea.requestLayout();
      playerCardsArea.getChildren().clear();

      String cardOne = "/com/example/demo/images/" + card1.getCardValue() + card1.getCardSuit().charAt(0) + ".png";
      String cardTwo = "/com/example/demo/images/" + card2.getCardValue() + card2.getCardSuit().charAt(0) + ".png";

      if (hand.getHighlightedCards().contains(Integer.toString(card1.getCardValue()) + "," + card1.getCardSuit().charAt(0))) {
        cardOne = "/com/example/demo/images/" + card1.getCardValue() + card1.getCardSuit().charAt(0) + "O.png";
      }

      if (hand.getHighlightedCards().contains(Integer.toString(card2.getCardValue()) + "," + card2.getCardSuit().charAt(0))) {
        cardTwo = "/com/example/demo/images/" + card2.getCardValue() + card2.getCardSuit().charAt(0) + "O.png";
      }

      Image image = new Image(getClass().getResource(cardOne).toExternalForm(), 114, 148, true, true);
      imgCard1 = new ImageView(image);
      playerCardsArea.getChildren().add(imgCard1);
      imgCard1.setX(0);
      imgCard1.setY(0);

      imgCard1 = new ImageView(image);
      playerCardsArea.getChildren().add(imgCard1);
      imgCard1.setX(0);
      imgCard1.setY(0);

      image = new Image(getClass().getResource(cardTwo).toExternalForm(), 114, 148, true, true);
      imgCard2 = new ImageView(image);
      playerCardsArea.getChildren().add(imgCard2);
      imgCard2.setX(105);
      imgCard2.setY(0);
      updatePlayerValues("");
    });
  }


  /**
   * Uses the getHighlightedCards to highlight and show cards on the table.
   * @param setOfCards Set of cards shown on the table.
   * @Author Nicklas Svensson
   */
  public void setFlopTurnRiver(Card[] setOfCards) {
    this.cards = new ArrayList<Card>(); // Clears the cards list
    cards.add(card1); // Adds card one and card two (player's cards in the hand)
    cards.add(card2);

    for (Card c : setOfCards) {
      cards.add(c); // Adds cards from flop/turn/river
    }
    // Debug print to check all cards
    for (Card card : cards) {
      System.out.println("CardAI: " + card.getCardValue() + card.getCardSuit());
    }

    this.hand = new Hand(cards);
    hand.reCalc(); // Recalculates so the "new" set of cards gets highlighted

    Platform.runLater(() -> {
      tabelCardArea.getChildren().clear(); // Clears if there's cards on the table (UI)
      tabelCardArea.requestLayout();
      int xCord = 0;
      // TEST
      for (int i = 0; i < setOfCards.length; i++) {
        String baseCard = "";
        String cardValue = Integer.toString(setOfCards[i].getCardValue());
        String cardSuit = setOfCards[i].getCardSuit().charAt(0) + "";

        if (hand.getHighlightedCards().contains(cardValue + "," + cardSuit)) {
          baseCard = "/com/example/demo/images/" + cardValue + cardSuit + "O.png";
        } else {
          baseCard = "/com/example/demo/images/" + cardValue + cardSuit + ".png";
        }

        if (i == 1) {
          xCord = 110; // First card
        } else if (i > 1) {
          xCord += 105;
        }
        System.out.println("BaseCard Path: " + baseCard);
        //Fixed
        Image imageTemp = new Image(getClass().getResourceAsStream(baseCard), 114, 148, true, true);

        collectionOfCardsTable[i] = new ImageView(imageTemp);
        tabelCardArea.getChildren().add(collectionOfCardsTable[i]);
        collectionOfCardsTable[i].setX(xCord);
        collectionOfCardsTable[i].setY(0);
      }
      handHelp();
      checkHand();
    });
  }

  /**
   * Clears the cards on the table.
   */
  public void clearFlopTurnRiver() {
    Platform.runLater(() -> {
      tabelCardArea.getChildren().clear();
    });
  }

  /**
   * Method which makes the player the smallblind.
   * @param i the amount to pay
   */
  public void playerSmallBlind(int i) {
    this.alreadyPaid += i;
    this.playerPot -= i;
    Platform.runLater(() -> {
      ivSmallBlind.relocate(520, 425);
    });
    updatePots(new int[1][0], spController.getPotSize());

  }

  /**
   * Method which makes the player the bigBlind
   * @param i the amount to pay
   */
  public void playerBigBlind(int i) {
    this.alreadyPaid += i;
    this.playerPot -= i;
    Platform.runLater(() -> {
      ivBigBlind.relocate(520, 425);

    });
    updatePots(new int[1][0], spController.getPotSize());
  }

  /**
   * Returns the amount of money that the player has already bet
   * @return The amount of money that the player has already bet
   */
  public int getPlayerAlreadyPaid() {
    return this.alreadyPaid;
  }

  /**
   * Method which sets the player as dealer
   * @param i not used.
   */
  public void playerIsDealer(int i) {
    if ((int) ivBigBlind.getLayoutX() == 520 || (int) ivSmallBlind.getLayoutX() == 520) {
      ivDealer.setLayoutX(500);
      ivDealer.setLayoutY(425);
    } else {
      ivDealer.setLayoutX(520);
      ivDealer.setLayoutY(425);
    }
  }

  /**
   * Method which fetches the advice for the player and displays it in the bottom left pane
   * @Author Nicklas Svensson
   */
  public void handHelp() {
    Platform.runLater(() -> {
      try {
        String helpText = hand.theHelp();
        helpLabel.setText("Du har: \n" + helpText);
        String adviceText = hand.theAdvice();
        adviceLabel.setText("Råd: \n" + adviceText);

        powerBarValue = hand.toPowerBar();

        ImageView imgPowerBar = new ImageView();
        imgPowerBar.setFitWidth(120);
        imgPowerBar.setFitHeight(166);
        imgPowerBar.setPreserveRatio(true);

        Image image;
        switch (powerBarValue) {
          case 1:
            image = new Image(getClass().getResourceAsStream("/com/example/demo/images/weakHand.png"), 120, 166, true, true);
            break;
          case 2:
            image = new Image(getClass().getResourceAsStream("/com/example/demo/images/mediumWeakHand.png"), 120, 166, true, true);
            break;
          case 3:
            image = new Image(getClass().getResourceAsStream("/com/example/demo/images/mediumStrongHand.png"), 120, 166, true, true);
            break;
          case 4:
            image = new Image(getClass().getResourceAsStream("/com/example/demo/images/strongHand.png"), 120, 166, true, true);
            break;
          default:
            return;
        }
        imgPowerBar.setImage(image);
        powerBarArea.getChildren().remove(imgPowerBar);
        powerBarArea.getChildren().add(imgPowerBar);
        imgPowerBar.setX(15);
        imgPowerBar.setY(0);
        this.handStrength = hand.getHandStrenght();
      }catch (IllegalArgumentException e) {
        e.printStackTrace();
      }
    });
  }


  /**
   * Returns the players decision.
   * @return The players decision.
   */
  public String getPlayerDecision() {
    return decision;
  }


  /**
   * Method which controls the players decision
   * @return The players decision
   */
  public String askForPlayerDecision() {
    handleButtons();
    playerMadeDecision = false;
    while (!playerMadeDecision) {
      try {
        SPController.sleep(100);
      } catch (InterruptedException e) {
        e.printStackTrace();
      }
    }
    return decision;
  }


  /**
   * Method which resets the players cards, amount paid and decision.
   * @param resetDecision the new decision
   */
  public void playerReset(String resetDecision) {
    decision = resetDecision;
    alreadyPaid = 0;
    cards = new ArrayList<Card>();
  }


  /**
   * Sets the new player-pot.
   * @param newValue The value to add/remove from the player-pot.
   */
  public void setPlayerPot(int newValue) {
    this.playerPot += newValue;
  }


  /**
   * Shows/hides player-buttons based on allowed actions.
   */
  public void handleButtons() {
    if (alreadyPaid == spController.getCurrentMaxBet()) {
      // show check, hide all other
      btCheck.setVisible(true);
      btCall.setVisible(false);
      btRaise.setVisible(true);
      btFold.setVisible(true);
    } else {
      if (alreadyPaid < spController.getCurrentMaxBet()
              && (playerPot + alreadyPaid) >= spController.getCurrentMaxBet()) {
        // hide check, show call
        btCheck.setVisible(false);
        btCall.setVisible(true);
        btFold.setVisible(true);
      } else {
        // hide call, hide check
        btCheck.setVisible(false);
        btCall.setVisible(false);
        btFold.setVisible(true);
      }

      if ((spController.getCurrentMaxBet() - alreadyPaid) + spController.getBigBlind() <= playerPot
              && playerPot != 0) {
        btRaise.setVisible(true); // show raise
      } else {
        btRaise.setVisible(false); // hide raise
      }
    }
    aiController.inactivateAllAiCardGlows();
  }


  /**
   * Disables all player-buttons.
   */
  public void disableButtons() {
    btCall.setVisible(false);
    btRaise.setVisible(false);
    btCheck.setVisible(false);
    btFold.setVisible(false);
  }

  /**
   * Method which returns the players handStrength as an integer
   * @return the handStrength
   */
  public int getHandStrength() {
    return handStrength;
  }

  /**
   * Method which returns the players pot
   * @return the playerpot
   */
  public int getPlayerPot() {
    return playerPot;
  }

  /** //TODO: has been moved, now connected
   * Places the AI-players in the correct position depending on chosen number of players.
   * @param aiPlayers All the AI-players that are active.
   * @param notFirstRound
   * @param deadAIIndex
   */
  public void setAiPlayers(LinkedList<Ai> aiPlayers, boolean notFirstRound, int deadAIIndex) {
    aiController.setAiPlayers(aiPlayers, notFirstRound, deadAIIndex);
  }


  /** //TODO: has been moved, now connected
   * Updates AI-frame based on currentAI-position and decision with the method setUIAiStatus.
   * @param currentAI Chosen AI to update AI-frame
   * @param decision Check, call, fold, raise or lost
   */
  public void aiAction(int currentAI, String decision) {
    aiController.aiAction(currentAI, decision);
  }

  /**
   * communicates from SPController to AIController
   * @param position
   * @param state
   */
  public void setUIAiStatus(int position, String state) {
    aiController.setUIAiStatus(position, state);
  }

  /**
   * Force closes the program
   */
  public void closeProgram() {
    System.exit(0);
  }


  /**
   * Method which returns to the main menu
   * @throws InstantiationException
   * @throws IllegalAccessException
   */
  public void goToMainMenu() throws InstantiationException, IllegalAccessException {
    try {
      changeScene.switchToMainMenu();
      changeScene.prepGame();
    } catch (IOException e) {
      e.printStackTrace();
    }
  }


  /**
   * Method which creates an "about" box.
   */
  public void aboutBox() {
    confirmBox = new ConfirmBox();
    confirmBox.display("Om projektet",
            "Detta projekt är format och skapat av "
                    + "Vedrana Zeba, Rikard Almgren, Amin Harirchian, Max Frennessen och Lykke Levin under "
                    + "vårterminen 2017 som en del av kursen Systemutveckling och projekt 1. Nu uppdaterar och förbättrad" +
                    "av Nicklas, Alexandra,Tiffany, Fabian, Ludvig och Sebastian under vårterminen 2024 ");
  }

  /**
   * Method which creates a popup to inform the player that s/he lost.
   */
  public void playerLost() {
    Platform.runLater(() -> {
      try {
        winnerBox = new WinnerBox();
        winnerBox.displayWinner("Förlust",
                "Tyvärr, du förlorade och dina pengar är slut. Bättre lycka nästa gång!", 5,
                winnerHand);

        changeScene.switchToMainMenu();

      } catch (IOException | InstantiationException | IllegalAccessException e) {
        e.printStackTrace();
      }
    });
  }


  /**
   * Method which returns the players highCard
   * @return highCard
   */
  public int getGetHighCard() {
    return highCard;
  }


  public void setBlindsMarker(int dealer, int smallBlindPlayer, int bigBlindPlayer) {
    int[][] markerPos = new int[5][2];
    Platform.runLater(() -> {

      // set MarkerPos TEST
      markerPos[0][0] = 300;
      markerPos[0][1] = 360;

      markerPos[1][0] = 375;
      markerPos[1][1] = 172;

      markerPos[2][0] = 745;
      markerPos[2][1] = 172;

      markerPos[3][0] = 1010;
      markerPos[3][1] = 220;

      markerPos[4][0] = 1010;
      markerPos[4][1] = 360;

      if (dealer <= 4) {
        ivDealer.relocate(markerPos[dealer][0], markerPos[dealer][1]);
      }
      if (smallBlindPlayer <= 4) {
        ivSmallBlind.relocate(markerPos[smallBlindPlayer][0], markerPos[smallBlindPlayer][1]);
      }
      if (bigBlindPlayer <= 4) {
        ivBigBlind.relocate(markerPos[bigBlindPlayer][0], markerPos[bigBlindPlayer][1]);
      }


    });
  }


  /**
   * Shows current round.
   * @param round int between 0-3 ("roundPreFlop", "roundFlop", "roundTurn", "roundRiver").
   * @Author Nicklas Svensson
   */
  public void roundStatus(int round) {
    String[] roundStatus = new String[] {"roundPreFlop", "roundFlop", "roundTurn", "roundRiver"};

    Platform.runLater(() -> {

      paneRounds.getChildren().remove(imgRoundStatus);
      String imagePath = "/com/example/demo/images/" + roundStatus[round] + ".png";
      Image tempImage = new Image(getClass().getResource(imagePath).toExternalForm(),
              175, 56, true, true);
      imgRoundStatus = new ImageView(tempImage);
      imgRoundStatus.setImage(tempImage);
      imgRoundStatus.setPreserveRatio(false);
      paneRounds.getChildren().add(imgRoundStatus);
    });
  }


  /**
   * Creates a winnerWindow that displays the winner of the round.
   * @param winner Name of the winner from spController.
   * @param hand Int number from spController that represent the value of the winning hand.
   */

  public void setWinnerLabel(String winner, int hand, WinnerCallback callback) {
    String winnerOfRound = winner;
    System.out.println("setting winner label now");

    if (hand == 0) {
      winnerHand = "högsta kort";
    }
    if (hand == 1) {
      winnerHand = "ett par";
    }
    if (hand == 2) {
      winnerHand = "två par";
    }
    if (hand == 3) {
      winnerHand = "triss";
    }
    if (hand == 4) {
      winnerHand = "straight";
    }
    if (hand == 5) {
      winnerHand = "flush";
    }
    if (hand == 6) {
      winnerHand = "full house";
    }
    if (hand == 7) {
      winnerHand = "four of a kind";
    }
    if (hand == 8) {
      winnerHand = "straight flush";
    }
    if (hand == 99) {
      winnerHand = "Du vann när resten av spelarna foldade!";
    }
    if (hand == 98) {
      winnerHand = "när resterande spelare foldade.";
    }
    if (hand == 97) {
      winnerHand = "Du förlorade!";
    }

    if (!winnerOfRound.equals(getUsername()) && (hand < 10)) {
      Platform.runLater(() -> {
        winnerBox = new WinnerBox();
        boolean okClicked = winnerBox.displayWinner("Rundans vinnare", winnerOfRound, 2, winnerHand);
        callback.onWinnerBoxClosed(okClicked);
        fileController.saveWinnerHistory(getUsername(), winnerHand, false); //store game results in txt file
      });
    } else if (winnerOfRound.equals(getUsername()) && (hand < 10)) {
      Platform.runLater(() -> {
        sound.playSound("coinSound");
        winnerBox = new WinnerBox();
        boolean okClicked = winnerBox.displayWinner("Rundans vinnare", winnerOfRound, 1, winnerHand);
        callback.onWinnerBoxClosed(okClicked);
        fileController.saveWinnerHistory(winnerOfRound, winnerHand, true); //store game results in txt file
      });
    } else if (winnerOfRound.equals(getUsername()) && (hand > 10)) {
      Platform.runLater(() -> {
        sound.playSound("coinSound");
        winnerBox = new WinnerBox();
        boolean okClicked = winnerBox.displayWinner("Rundans vinnare", winnerOfRound, 3, winnerHand);
        callback.onWinnerBoxClosed(okClicked);
        fileController.saveWinnerHistory(winnerOfRound, winnerHand, true); //store game results in txt file
      });
    } else if (!winnerOfRound.equals(getUsername()) && (hand > 10)) {
      Platform.runLater(() -> {
        winnerBox = new WinnerBox();
        boolean okClicked = winnerBox.displayWinner("Rundans vinnare", winnerOfRound, 4, winnerHand);
        callback.onWinnerBoxClosed(okClicked);
        fileController.saveWinnerHistory(getUsername(), winnerHand, false); //store game results in txt file
      });
    }
  }

  /**
   * Method which creates a new tutorial and shows it.
   * @throws IOException
   */
  public void goToTutorial() throws IOException {
    Platform.runLater(() -> {
      this.tutorialWindow = new TutorialController(this);
      try {
        tutorialWindow.setupUIinGame();
      } catch (IOException e) {
        e.printStackTrace();
      }
    });
  }


  /**
   * Method which returns the player viabilitylevel for potSplits
   * @return AllInViability viabilityLevel
   */
  public int getAllInViability() {
    return AllInViability;
  }


  /**
   * Method which sets a viabilitylevel
   * @param allInViability
   */
  public void setAllInViability(int allInViability) {
    if (allInViability < AllInViability) {
      AllInViability = allInViability;
    }
  }


  /**
   * Method which updates the various pots in the UI
   * @param potSplits an Array of subPots during All-ins
   * @param tablePot the main tablePot
   */
  public void updatePots(int[][] potSplits, int tablePot) {
    if (spController.getFixedNrOfAIs() == 5) {
      this.collectionOfPots =
              new Label[] {subPotOne, subPotTwo, subPotThree, subPotFour, subPotFive, subPotSix};
    } else if (spController.getFixedNrOfAIs() == 3) {
      this.collectionOfPots = new Label[] {subPotOne, subPotTwo, subPotThree, subPotFour};
    } else if (spController.getFixedNrOfAIs() == 1) {
      this.collectionOfPots = new Label[] {subPotOne, subPotTwo};
    }
    Platform.runLater(() -> {
      String[] potOrder = {"Sub-Pot One: ", "Sub-Pot Two: ", "Sub-Pot Three: ", "Sub-Pot Four: ",
              "Sub-Pot Five: ", "Sub-Pot Six: "};
      for (int i = 0; i < collectionOfPots.length; i++) {
        if (potSplits[i][0] > 0) {
          collectionOfPots[i].setText(potOrder[i] + "§" + potSplits[i][0]);
          collectionOfPots[i].setVisible(true);
          collectionOfPots[i].setLayoutX(10);
          collectionOfPots[i].setLayoutY(30 * (i + 1) + 70);
        } else {
          collectionOfPots[i].setVisible(false);
        }
      }
      mainPot.setText("Table Pot: §" + tablePot);
      mainPot.setLayoutX(295.0);
      mainPot.setLayoutY(290.0);
      mainPot.setVisible(true);
    });
  }

  /**
   * This method is used to ask the player if he/she wants to play again.
   * @return
   */
  public boolean askReplay() {
    CompletableFuture<Boolean> futureResponse = new CompletableFuture<>();

    Platform.runLater(() -> {
      ConfirmBox cfBox = new ConfirmBox();
      boolean response = cfBox.display("Vill du spela igen?", "Vill du spela igen?");
      futureResponse.complete(response); //wait for user response
    });

    try {
      return futureResponse.get();
    } catch (Exception e) {
      e.printStackTrace();
      System.out.println("exception here");
      return false;
    }
  }

  /**
   * This method changes scene to the main menu.
   */
  public void changeToMainMenu() {
    try {
      changeScene.switchToMainMenu();
    } catch (IOException | InstantiationException | IllegalAccessException e) {
      System.out.println(e);
    }
  }

  /***
   * This method was tried to initialise the needed values for test cases, but came out unsuccessful
   *
   * @author Fabian Kjellberg
   * @param decision
   */
  public void initTestCase(String decision){
    this.decision = decision;
    this.userName = new Label();
    this.aiController = new AIController();
    btCheck = new ImageView();
    btCall = new ImageView();
    btRaise = new ImageView();
    btFold = new ImageView();
  }

}
