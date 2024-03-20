package com.example.demo.controller;

import java.io.IOException;
import java.util.HashMap;

import com.example.demo.gui.*;
import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;

/**
 * Controller for FXML-doc GameSettingMenu.fxml
 * 
 * @author Lykke Levin
 * @version 1.0
 *
 */
public class SettingsController {
	private SPController spController;
	private FileController fileController = new FileController();
	HashMap<String, HashMap<String, Integer>> historyMap = new HashMap<>();
	private ChangeScene changeScene;
	private ConfirmBox confirmBox;
	private ConfirmBoxOK confirmBoxOK;
	private String name;
	private int aiValue;
	private int potValue;

	@FXML
	private TextField tfNameInput;
	@FXML
	private TextField tfPot;
	@FXML
	private Slider aiSlider;
	@FXML
	private Slider potSlider;
	@FXML
	private CheckBox cbOn;
	@FXML
	private CheckBox cbOff;
	@FXML
	private ImageView ivStartGame;
	@FXML
	private ImageView ivQuestionAi;
	@FXML
	private ImageView ivQuestionPot;
	@FXML
	private ImageView ivQuestionTutorial;
	@FXML
	private ImageView ivSound;
	@FXML
	private Label lblAiInfo;
	@FXML
	private Label lblPotInfo;
	@FXML
	private Label lblTutorialInfo;
	@FXML
	private ImageView ivBack;

	@FXML
	private ImageView imgTutorial;
	@FXML
	private Pane tutorialPane;
	@FXML
	private ImageView btnNext;

	private Sound sound = new Sound();
	private TutorialController tutorialWindow;

	/**
	 * Method for initializing FXML. 
	 * @throws Exception
	 */
	public void initialize() throws Exception {
		potSlider.setSnapToTicks(true);
		potSlider.setValue(5000);
		aiSlider.setSnapToTicks(true);
		checkIfMuted();
	}

	/**
	 * Stores the name from the TextField that the user has inserted. 
	 */
	public void tfNameInputChange() {
		this.name = tfNameInput.getText();
	}

	public void tfPotChanged() {
		this.potValue = Integer.parseInt(tfPot.getText());
	}

	/**
	 * Sets the changeScene for this SettingsController
	 * @param sceneChanger an instance of the class ChangeScene
	 */
	public void setChangeScene(ChangeScene sceneChanger) {
		this.changeScene = sceneChanger;
	}

	/**
	 * Stores the value from the Slider that the user has chosen. 
	 */
	public void aiSliderChange() {
		Double val = aiSlider.getValue();
		aiValue = val.intValue();

	}

	/**
	 * Stores the value from the Slider that the user has chosen. 
	 */
	public void potSliderChange() {
		Double val = potSlider.getValue();
		potValue = val.intValue();

	}

	/**
	 * If ComboBox is selected by the user, disable the button true. 
	 */
	public void cbOnClicked() {
		if (cbOff.isSelected()) {
			cbOff.setSelected(false);
			cbOff.setDisable(false);
			cbOn.setSelected(true);
			cbOn.setDisable(true);
		}
	}

	/**
	 * If ComboBox is selected by the user, disable the button true. 
	 */
	public void cbOffClicked() {
		if (cbOn.isSelected()) {
			cbOn.setSelected(false);
			cbOn.setDisable(false);
			cbOff.setSelected(true);
			cbOff.setDisable(true);
		}
	}

	/**
	 * calls to shows history from settings menu
	 * @author Tiffany Dizdar, HT24.
	 */
	public void showHistory() {
		if (tfNameInput.getText().isEmpty()) {
			Platform.runLater(() -> {
				sound.playSound("wrong");
				confirmBoxOK = new ConfirmBoxOK();
				confirmBoxOK.display("Varning", "Du måste ange ett användarnamn för att visa historik");
			});
		} else {
			String playerName = tfNameInput.getText(); //get name from settings text field
			showPlayerHistory(playerName);
		}
	}

	/**
	 * shows win and loss history for specific player
	 * @param playerName name of player input through settings menu
	 * @author Tiffany Dizdar, HT24.
	 */
	public void showPlayerHistory(String playerName) {
		int totalWins = 0;
		int losses = 0;
		historyMap.clear();
		historyMap = fileController.readWinnerHistory();

		if (historyMap.containsKey(playerName)) {
			System.out.println("Player: " + playerName);
			HashMap<String, Integer> innerMap = historyMap.get(playerName);
			for (String winningHand : innerMap.keySet()) {
				int count = innerMap.get(winningHand);
				if (!winningHand.equals("lost")) {
					totalWins += count;
				} else {
					losses = count;
				}
			}
			double winPercentage = calculateWinPercentage(totalWins, losses);
			confirmBoxOK = new ConfirmBoxOK();
			confirmBoxOK.display("Historik för " + playerName, "Vinster - " + totalWins + "\n" + "Förluster - "
					+ losses + "\n" + "Vinstprocent - " + String.format("%.2f", winPercentage) + "%");
		} else {
			sound.playSound("wrong");
			confirmBoxOK = new ConfirmBoxOK();
			confirmBoxOK.display("Varning", "Ingen historik funnen för denna spelaren");
		}
	}

	/**
	 * calculates win percentage for player
	 * @param totalWins total wins for player
	 * @param losses total losses for player
	 * @return win percentage
	 * @author Tiffany Dizdar, Nicklas Svensson, HT24.
	 */
	public double calculateWinPercentage(int totalWins, int losses) {
		if (totalWins < 0 || losses < 0) {
			System.out.println("Total wins and losses must be non-negative.");
			return 0;
		}
		int totalGames = totalWins + losses;

		if (totalGames == 0) {
			System.out.println("No games played yet.");
			return 0.0;
		} else {
			double winPercentage = ((double) totalWins / totalGames) * 100;
			System.out.println("Win percentage: " + winPercentage + "%");
			System.out.println("Total wins: " + totalWins);
			System.out.println("Total losses: " + losses);
			return winPercentage;
		}
	}

	/**
	 * Starts the game and checks so the Username it not empty and checks if the Tutorial should be playing at the beginning.
	 */
	public void startGame() {
		potSliderChange();
		aiSliderChange();
		if (!tfNameInput.getText().isEmpty()) {
			name = tfNameInput.getText();
			spController = new SPController();
			changeScene.setSPController(spController);

			if (cbOn.isSelected()) {
				System.out.println("Tutorial ska visas");
				Platform.runLater(() -> {

				try {
					this.tutorialWindow = new TutorialController(this, 1);
					tutorialWindow.setupUI();
				} catch (IOException e) {
					e.printStackTrace();
				}
				});

			} else{
				startGameWindow();
			}
		} else if (tfNameInput.getText().isEmpty()) {
			sound.playSound("wrong");
			confirmBox = new ConfirmBox();
			boolean result =
					confirmBox.display("Varning", "Du måste välja ett användarnamn för att starta spelet");
			System.out.println("Du måste välja ett användarnamn");
			System.out.println(result);
		}

	}
	
	/**
	 * Creates the progressForm and the loadingbar. 
	 */
	public void startGameWindow(){
		ProgressForm pForm = new ProgressForm();
		// In real life this task would do something useful and return
		// some meaningful result:
		Task<Void> task = new Task<Void>() {
			@Override
			public Void call() throws InterruptedException {
				for (int i = 0; i < 10; i++) {
					updateProgress(i += 1, 10);
					Thread.sleep(200);
				}
				updateProgress(10, 10);
				return null;
			}
		};
		Thread thread = new Thread(task);
		thread.start();
		// binds progress of progress bars to progress of task:
		pForm.activateProgressBar(task);

		// in real life this method would get the result of the task
		// and update the UI based on its value:
		task.setOnSucceeded(event -> {
			pForm.getDialogStage().close();

			try {
				changeScene.switchScenetoGame();
				ConfirmBox cfBox = new ConfirmBox();

				if (cfBox.display("Snart börjar spelet", "Är du redo att spela poker?")) {
					spController.startGame(aiValue, potValue, name);
					Sound.mp.stop();
					sound.playSound("shuffle");
				} else {
					changeScene.switchToMainMenu();
				}
			} catch (IOException | InstantiationException | IllegalAccessException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		});
		System.out.println("Spel startas!");
	}
	
	/**
	 * Shows a label if question mark is hovered. 
	 */
	public void ivQuestionAiHovered() {
		lblAiInfo.setVisible(true);
		ivQuestionAi.setOnMouseExited(e -> lblAiInfo.setVisible(false));
	}

	/**
	 * Shows a label if question mark is hovered. 
	 */
	public void ivQuestionPotHovered() {
		lblPotInfo.setVisible(true);
		ivQuestionPot.setOnMouseExited(e -> lblPotInfo.setVisible(false));
	}

	/**
	 * Shows a label if question mark is hovered. 
	 */
	public void ivQuestionTutorialHovered() {
		lblTutorialInfo.setVisible(true);
		ivQuestionTutorial.setOnMouseExited(e -> lblTutorialInfo.setVisible(false));
	}

	/**
	 * Tells class changeScene to perform the swithScene-action.
	 * @throws InstantiationException
	 * @throws IllegalAccessException
	 */
	public void back() throws InstantiationException, IllegalAccessException {
		try {
			changeScene.switchToMainMenu();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		// Main.window.setScene(changeScene.sceneMenu);
	}

	/**
	 * Name of the user.
	 * @return String name of the user. 
	 */
	public String getName() {
		return name;
	}

	public void checkIfMuted() {
		switch (sound.getSoundStatus()) {
			case "Stopped":
				Image soundOff = new Image(getClass().getResourceAsStream("/com/example/demo/images/soundButtonMute.png"));
				ivSound.setImage(soundOff);
				break;
			case "Playing":
				Image soundOn = new Image(getClass().getResourceAsStream("/com/example/demo/images/soundButton.png"));
				ivSound.setImage(soundOn);
				break;
		}
	}

	public void soundSetting() {
		switch (sound.getSoundStatus()) {
			case "Playing":
				sound.stopBackgroundMusic();
				Image soundOff = new Image(getClass().getResourceAsStream("/com/example/demo/images/soundButtonMute.png"));
				ivSound.setImage(soundOff);
				break;
			case "Stopped":
				sound.playBackgroundMusic();
				Image soundOn = new Image(getClass().getResourceAsStream("/com/example/demo/images/soundButton.png"));
				ivSound.setImage(soundOn);
				break;
		}
	}

	/***
	 * This method is used to set the values needed to test the mute button in the settings.
	 *
	 * @param sound
	 * @param cbOn
	 * @param cbOff
	 * @param ivSound
	 * @author Fabian Kjellberg
	 */
	public void initMockController(Sound sound, CheckBox cbOn, CheckBox cbOff, ImageView ivSound) {
		this.sound = sound;
		this.cbOn = cbOn;
		this.cbOff = cbOff;
		this.ivSound = ivSound;
	}
}
