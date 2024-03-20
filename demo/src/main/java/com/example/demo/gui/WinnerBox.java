package com.example.demo.gui;

import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.stage.Modality;
import javafx.stage.Stage;

/**
 * Box that shows the winner of the round.
 * @author Lykke Levin
 * version 1.0
 */
public class WinnerBox {

	public boolean answer = false;
	public Stage window = new Stage();
	public Font font = new Font("Tw Cen MT", 18);
	private ImageView back = new ImageView(getClass().getResource("/com/example/demo/images/background.png").toExternalForm());
	private ImageView btnOk = new ImageView(getClass().getResource("/com/example/demo/images/okButton.png").toExternalForm());

	/**
	 * Creates a window containing messages of who won or lost.
	 * @param title String title of the window from the method that uses WinnerBox.
	 * @param message String message to display in the window from the method that uses ConfirmBox.
	 * @param nr Int to check which message should be displayed.
	 * @param handStrength String to print the hand strength the player or AI won with.
	 * @return answer Boolean that returns an answer.
	 */
	public boolean displayWinner(String title, String message, int nr, String handStrength) {

		String aiWin = "Rundan vanns av " + message + " som hade " + handStrength;
		String playerWin = "Grattis " + message + ", du vann den här rundan! Du vann med " + handStrength;
		String playerWinAIFold = "Grattis " + message + ". " + handStrength;
		String aiWinOthersFold = "Rundan vanns av " + message + " " + handStrength;
		String playerLose = message;

		window.initModality(Modality.APPLICATION_MODAL);
		window.setTitle(title);
		window.setWidth(400);
		window.setHeight(200);
		window.setOnCloseRequest(e -> closeProgram());

		Pane pane = new Pane();

		Label messageText = new Label();
		messageText.setFont(font);
		messageText.setTextFill(Color.WHITE);
		messageText.setWrapText(true);

		String displayedMessage;
		switch (nr) {
			case 1:
				displayedMessage = playerWin;
				break;
			case 2:
				displayedMessage = aiWin;
				break;
			case 3:
				displayedMessage = playerWinAIFold;
				break;
			case 4:
				displayedMessage = aiWinOthersFold;
				break;
			case 5:
				displayedMessage = playerLose;
				break;
			default:
				displayedMessage = "";
				break;
		}
		messageText.setText(displayedMessage);

		btnOk.setOnMouseReleased(e -> {
			answer = true;
			closeProgram();
		});

		back.setFitHeight(window.getHeight());
		back.setFitWidth(window.getWidth());
		messageText.setPrefSize(200, 100);
		messageText.setLayoutX(100);
		messageText.setLayoutY(10);
		btnOk.setFitHeight(35);
		btnOk.setFitWidth(35);
		btnOk.setLayoutX(175);
		btnOk.setLayoutY(110);

		pane.getChildren().addAll(back, messageText, btnOk);

		Scene scene = new Scene(pane);
		window.setScene(scene);
		window.showAndWait();
		return answer;
	}

	/**
	 * This is an alternative method to the currently used method, its only a suggestion to make the method more
	 * testable. This method has a different return type
	 * @author Fabian Kjellberg
	 * @param title
	 * @param message
	 * @param handStrength
	 * @param nr
	 * @return returns a string instead of a boolean, returning the winning message.
	 */
	public String displayWinner(String title, String message , String handStrength, int nr) {

		String aiWin = "Rundan vanns av " + message + " som hade " + handStrength;
		String playerWin = "Grattis " + message + ", du vann den här rundan! Du vann med " + handStrength;
		String playerWinAIFold = "Grattis " + message + ". " + handStrength;
		String aiWinOthersFold = "Rundan vanns av " + message + " " + handStrength;
		String playerLose = message;

		window.initModality(Modality.APPLICATION_MODAL);
		window.setTitle(title);
		window.setWidth(400);
		window.setHeight(200);
		window.setOnCloseRequest(e -> closeProgram());

		Pane pane = new Pane();

		Label messageText = new Label();
		messageText.setFont(font);
		messageText.setTextFill(Color.WHITE);
		messageText.setWrapText(true);

		String displayedMessage;
		switch (nr) {
			case 1:
				displayedMessage = playerWin;
				break;
			case 2:
				displayedMessage = aiWin;
				break;
			case 3:
				displayedMessage = playerWinAIFold;
				break;
			case 4:
				displayedMessage = aiWinOthersFold;
				break;
			case 5:
				displayedMessage = playerLose;
				break;
			default:
				displayedMessage = "";
				break;
		}
		messageText.setText(displayedMessage);

		btnOk.setOnMouseReleased(e -> {
			answer = true;
			closeProgram();
		});

		back.setFitHeight(window.getHeight());
		back.setFitWidth(window.getWidth());
		messageText.setPrefSize(200, 100);
		messageText.setLayoutX(100);
		messageText.setLayoutY(10);
		btnOk.setFitHeight(35);
		btnOk.setFitWidth(35);
		btnOk.setLayoutX(175);
		btnOk.setLayoutY(110);

		pane.getChildren().addAll(back, messageText, btnOk);

		Scene scene = new Scene(pane);
		window.setScene(scene);
		window.showAndWait();
		return displayedMessage;
	}

	/**
	 * Closes the window.
	 */
	public void closeProgram() {
		window.close();
	}
}
