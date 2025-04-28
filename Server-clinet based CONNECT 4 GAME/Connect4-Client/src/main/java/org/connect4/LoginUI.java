package org.connect4;

import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.*;
import javafx.scene.layout.*;

import java.util.Locale;
import java.util.concurrent.atomic.AtomicBoolean;

public class LoginUI {

    public static Scene getLoginScene() {
        Image image = new Image("background.jpg");
        BackgroundSize size = new BackgroundSize(2401,1502, true, true, true, false);
        BackgroundImage bgImage = new BackgroundImage(image, BackgroundRepeat.NO_REPEAT, BackgroundRepeat.NO_REPEAT, BackgroundPosition.CENTER, size);
        Background background = new Background(bgImage);

        TextField spacing = new TextField();
        spacing.setStyle(
                "-fx-font-size: 36px; -fx-background-color: transparent; -fx-border-color: transparent; -fx-text-fill: transparent;"
        );
        spacing.setEditable(false);

        TextField usernameField = new TextField();
        usernameField.setPromptText("Username");
        usernameField.setMaxWidth(250);
        usernameField.setStyle(
                "-fx-font-size: 22px; -fx-background-color: transparent; -fx-border-color: transparent; -fx-text-fill: black;"
        );

        PasswordField passwordField = new PasswordField();
        passwordField.setPromptText("Password");
        passwordField.setMaxWidth(250);
        passwordField.setStyle(
                "-fx-font-size: 22px; -fx-background-color: transparent; -fx-border-color: transparent; "
        );

        Button loginButton = new Button("LOGIN");
        loginButton.setStyle("-fx-background-color: transparent; -fx-text-fill: transparent; -fx-font-size: 22px; -fx-font-weight: bold;");

        loginButton.setMinWidth(250);

        Button createAccountButton = new Button(" CREATE ACCOUNT");
        createAccountButton.setStyle("-fx-background-color: transparent; -fx-text-fill: transparent; -fx-font-size: 22px; -fx-font-weight: bold;");
        createAccountButton.setMinWidth(250);

        Label messageLabel = new Label();
        messageLabel.setStyle("-fx-text-fill: white; -fx-font-size: 16px;");

        VBox formBox = new VBox(20, spacing,usernameField, passwordField, loginButton, createAccountButton, messageLabel);
        formBox.setAlignment(Pos.CENTER);

        StackPane root = new StackPane(formBox);
        root.setBackground(background);

        // Login logic
        loginButton.setOnAction(e -> {
            String username = usernameField.getText();
            String password = passwordField.getText();
            AtomicBoolean res = new AtomicBoolean(false);
            String result = MainApp.client.authenticate(username, password, Communication.LOGIN, res);
            messageLabel.setText(result);
            if (res.get()) {
                SceneManager.switchToLobby();
            }
        });

        createAccountButton.setOnAction(e -> {
            String username = usernameField.getText().toLowerCase();
            String password = passwordField.getText();
            AtomicBoolean res = new AtomicBoolean(false);
            String result = MainApp.client.authenticate(username, password, Communication.CREATE_ACCOUNT, res);
            messageLabel.setText(result);
            if (res.get()) {
                Client.username = username;
                Client.loadFriends();
                SceneManager.switchToLobby();
            }
        });

        return new Scene(root, 1279, 800);
    }
}
