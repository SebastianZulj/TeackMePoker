package com.example.demo.controller;

import java.io.IOException;
import java.util.Objects;

import com.example.demo.gui.ChangeScene;
import com.example.demo.gui.Sound;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;

/**
 * Controller for FXML-doc FirstMenu.fxml.
 * 
 * @author Lykke Levin
 * @version 1.0
 *
 */

public class FMController {
	private ChangeScene changeScene;
	private Sound sound = new Sound();
	@FXML
	private ImageView ivSound;
	@FXML
	private TextField tfNameInput;
	@FXML
	private ImageView ivNewGame;
	@FXML
	private ImageView ivLoadGame;

	/**
	 * Generated method for the FXML.
	 * @throws Exception
	 */
	public void initialize() throws Exception {
		soundSetting();
	}

	/**
	 * Sets the changeScene for this FMController
	 * @param sceneChanger an instance of the class ChangeScene
	 */
	public void setChangeScene(ChangeScene sceneChanger) {
		this.changeScene = sceneChanger;
	}

	/**
	 * Tells class changeScene to perform the swithScene-action. 
	 * @throws Exception
	 */
	public void NewGameClicked() throws Exception {
		changeScene.switchScenetoSetting();
	}

	/**
	 * Should load a saved game file. This method is currently a non-functional
	 * method that is not implemented for the final version.
	 * @throws IOException
	 */
	public void LoadGameClicked() throws IOException {
		// fileHandler = new FileHandler();
		// String pot = fileHandler.loadPot();
		// System.out.println(fileHandler.loadPot());

		System.out.println("LoadGame");
		sound = new Sound();
		sound.playSound("wrong");
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
}
