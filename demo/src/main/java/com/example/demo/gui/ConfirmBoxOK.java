
package com.example.demo.gui;

        import javafx.stage.*;
        import javafx.scene.*;
        import javafx.scene.layout.*;
        import javafx.scene.text.Font;
        import javafx.scene.control.*;
        import javafx.geometry.*;

/**
 * Window with text and button containing a message.
 *
 */

public class ConfirmBoxOK {
    public boolean answer = false;
    public Stage window = new Stage();
    public Font font = new Font("Tw Cen MT", 18);

    /**
     * @author Alexandra A Holter
     */
    public boolean display(String title, String message) {
        window.initModality(Modality.APPLICATION_MODAL);
        window.setTitle(title);
        window.setMinWidth(250);
        window.setMaxWidth(600);
        window.setHeight(275);
        window.setOnCloseRequest(e -> closeProgram());

        Label label = new Label();
        label.setFont(font);
        label.setText(message);
        label.setWrapText(true);

        Button buttonOk = new Button("Okej");
        buttonOk.setFont(font);

        buttonOk.setOnAction(e -> {
            answer = true;
            closeProgram();

        });

        VBox layout = new VBox(10);
        layout.setPadding(new Insets(10, 10, 10, 10));
        layout.getChildren().addAll(label, buttonOk);
        layout.setAlignment(Pos.CENTER);

        Scene scene = new Scene(layout);
        window.setScene(scene);
        window.showAndWait();

        return answer;
    }

    /**
     * Method that closes the window.
     */
    public void closeProgram() {
        window.close();
    }

}
