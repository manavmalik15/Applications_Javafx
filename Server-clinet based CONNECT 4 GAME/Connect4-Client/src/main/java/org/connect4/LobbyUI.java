package org.connect4;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.layout.*;
import javafx.util.Callback;
import javafx.util.Duration;

import java.io.IOException;

public class LobbyUI {

    static Timeline refresher = null;

    public static Scene getLobbyHomeScene() {
        // Load background image at original size
        Image image = new Image("lobby.png");
        BackgroundSize size = new BackgroundSize(1280, 820, false, false, false, false);
        BackgroundImage bgImage = new BackgroundImage(
                image,
                BackgroundRepeat.NO_REPEAT,
                BackgroundRepeat.NO_REPEAT,
                BackgroundPosition.CENTER,
                size
        );
        Background background = new Background(bgImage);
        TextField spacing = new TextField();
        spacing.setStyle(
                "-fx-font-size: 37px; -fx-background-color: transparent; -fx-border-color: transparent; -fx-text-fill: transparent;"
        );
        spacing.setEditable(false);
        // Create buttons
        Button onlineBtn = new Button("Online Players");
        Button AIBtn = new Button("Friends");

        Button backBtn = new Button("Rules");

        for (Button btn : new Button[]{onlineBtn, AIBtn, backBtn}) {
            btn.setPrefWidth(320);
            btn.setStyle("-fx-background-color: transparent; -fx-text-fill: transparent; -fx-font-size: 30px; -fx-font-weight: bold;");
        }

        onlineBtn.setOnAction(e -> {
            SceneManager.switchSceneWithSplit(getLobbyDetailScene());
            try {
                Client.out.writeObject(new Communication(Client.username, "server",
                    Communication.REQUEST_ONLINE_PLAYERS,
                    "online players pls"));
            } catch (IOException ea) {
            throw new RuntimeException(ea);
            }
        });



//        AIBtn.setOnAction(e -> SceneManager.switchSceneWithSplit(getLobbyDetailScene()));
        backBtn.setOnAction(e -> SceneManager.switchSceneWithSplit(getLobbyrulesScene()));

        VBox buttonBox = new VBox(27,spacing, AIBtn,onlineBtn, backBtn);
        buttonBox.setAlignment(Pos.CENTER);

        StackPane root = new StackPane(buttonBox);
        root.setBackground(background);

        // Scene size matches image size (optional, adjust to your image)
        return new Scene(root,1280,820);
    }



// …

    public static Scene getLobbyDetailScene() {

        String size_st = "-fx-font-size: 16px;";

        // 1) Backing ObservableLists, initially seeded from Client static lists:
        ObservableList<String> onlinePlayersData  = FXCollections.observableArrayList(Client.onlineplayers);
        ObservableList<String> friendsData        = FXCollections.observableArrayList(Client.friends);
        ObservableList<String> playRequestsData   = FXCollections.observableArrayList(Client.Play_requests);
        ObservableList<String> friendRequestsData = FXCollections.observableArrayList(Client.friends_requests);

        // 2) ListViews bound to those lists:
        ListView<String> onlinePlayersList   = new ListView<>(onlinePlayersData);
        ListView<String> friendsList         = new ListView<>(friendsData);
        ListView<String> playRequestsList    = new ListView<>(playRequestsData);
        ListView<String> friendRequestsList  = new ListView<>(friendRequestsData);

        // 3) CellFactory for “Online Players” → [Name] …… [Play] [Add Friend]
        onlinePlayersList.setCellFactory(lv -> new ListCell<String>() {
            private final Label nameLabel     = new Label();
            private final Button playBtn      = new Button("Play");
            private final Button addFriendBtn = new Button("Add Friend");

            private final Region spacer       = new Region();
            private final HBox container      = new HBox(8, nameLabel, spacer, playBtn, addFriendBtn);

            {
                // push spacer to take all extra space:
                HBox.setHgrow(spacer, Priority.ALWAYS);

                playBtn.setOnAction(evt -> {
                    String target = getItem();
                    sendToServer(Communication.REQUEST_PLAY, target, "Hi, let's play!");
                });
                playBtn.setStyle(size_st);
                addFriendBtn.setOnAction(evt -> {
                    String target = getItem();
                    sendToServer(Communication.ADD_FRIEND, target, "Please add me");
                });
                addFriendBtn.setStyle(size_st);
            }

            @Override
            protected void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setGraphic(null);
                } else {
                    nameLabel.setText(item);
                    nameLabel.setStyle(size_st);
                    setGraphic(container);
                }
            }
        });




        friendsList.setCellFactory(lv -> new ListCell<String>() {
            private final Label nameLabel     = new Label();
            private final Button playBtn      = new Button("Play");
            private final Label isOnline   = new Label();
            private final Region spacer       = new Region();
            private final HBox container      = new HBox(8, nameLabel, spacer, playBtn, isOnline);

            {
                // push spacer to take all extra space:
                HBox.setHgrow(spacer, Priority.ALWAYS);

                playBtn.setOnAction(evt -> {
                    String target = getItem();
                    sendToServer(Communication.REQUEST_PLAY, target, "Hi, let's play!");
                });


            }


            @Override
            protected void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setGraphic(null);
                } else {
                    nameLabel.setText(item);
                    nameLabel.setStyle(size_st);
                    // update the online-status icon
                    boolean online = Client.onlineplayers.contains(item);
                    isOnline.setText(online ? "🟢" : "⛔️");
                    isOnline.setStyle(size_st);
                    setGraphic(container);
                }
            }
        });



        // 4) CellFactory for “Requests” lists → [Name] …… [✅]
        Callback<ListView<String>, ListCell<String>> acceptFactory = lv -> new ListCell<String>() {
            private final Label nameLabel = new Label();

            private final Button acceptBtn = new Button("✅");
            private final Button rejectBtn = new Button("❌");
            private final Region spacer    = new Region();
            private final HBox container   = new HBox(8, nameLabel, spacer, acceptBtn,rejectBtn);

            {
                HBox.setHgrow(spacer, Priority.ALWAYS);

                acceptBtn.setOnAction(evt -> {
                    String who = getItem();
                    int type = (lv == playRequestsList
                            ? Communication.ACCEPT_GAME
                            : Communication.ACCEPT_FRIND);

                    // 1) notify server
                    sendToServer(type, who, "");

                    // 2) remove from both Client.* and UI list immediately
                    if (lv == playRequestsList) {
                        Client.Play_requests.remove(who);
                        playRequestsData.remove(who);




                    } else {
                        if (!Client.friends.contains(who)) {
                            Client.friends.add(who);
                        }
                        Client.friends_requests.remove(who);
                        friendRequestsData.remove(who);
                    }
                });
                acceptBtn.setStyle("-fx-background-color: transparent; "+size_st);

                rejectBtn.setOnAction(evt -> {
                    String who = getItem();
                    if (lv == playRequestsList) {
                        Client.Play_requests.remove(who);
                        playRequestsData.remove(who);
                    } else {
                        Client.friends_requests.remove(who);
                        friendRequestsData.remove(who);
                    }
                });
                rejectBtn.setStyle("-fx-background-color: transparent; " +size_st);
            }

            @Override
            protected void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setGraphic(null);
                } else {
                    nameLabel.setText(item);
                    nameLabel.setStyle(size_st);
                    setGraphic(container);
                }
            }
        };

        playRequestsList.setCellFactory(acceptFactory);
        friendRequestsList.setCellFactory(acceptFactory);

        // 5) Layout panes & back button:
        Button back = new Button("Back");
        back.setPrefWidth(300);
        back.setStyle("-fx-background-color: black; -fx-text-fill: white; -fx-font-size: 22px;");
        Timeline finalRefresher = refresher;
        back.setOnAction(e -> {
            SceneManager.switchSceneWithSplit(getLobbyHomeScene());
            finalRefresher.stop();
        });

        VBox leftPane = new VBox(10,
                back,
                new Label("Online Players"),
                onlinePlayersList
        );
        leftPane.setPadding(new Insets(20));
        leftPane.setPrefWidth(600);
        leftPane.setStyle("-fx-background-color: #E6E6FA;");

        VBox friendsSection        = new VBox(5, new Label("Friends"),        friendsList);
        VBox playReqSection        = new VBox(5, new Label("Play Requests"),   playRequestsList);
        VBox friendReqSection      = new VBox(5, new Label("Friend Requests"), friendRequestsList);

        VBox rightPane = new VBox(20,
                friendsSection, playReqSection, friendReqSection
        );
        rightPane.setPadding(new Insets(20));
        rightPane.setPrefWidth(600);
        rightPane.setStyle("-fx-background-color: #F8F8FF;");

        HBox root = new HBox(leftPane, rightPane);
        Scene scene = new Scene(root, 1280, 820);

        // 6) Every 5s, copy from your Client.* lists into the ObservableLists:

            refresher = new Timeline(
                    new KeyFrame(Duration.seconds(5), ev -> {
//
//
//                        try {
//                            Client.out.writeObject(new Communication(Client.username, "server",
//                                    Communication.REQUEST_ONLINE_PLAYERS,
//                                    "online players pls"));
//                        } catch (IOException ea) {
//                            throw new RuntimeException(ea);
//                        }
//
//
//                        onlinePlayersData.setAll(Client.onlineplayers);
//                        friendsData.setAll(Client.friends);
//                        playRequestsData.setAll(Client.Play_requests);
//                        friendRequestsData.setAll(Client.friends_requests);
//

                            // ask the server…
                        try {
                            Client.out.writeObject(new Communication(
                                    Client.username, "server",
                                    Communication.REQUEST_ONLINE_PLAYERS,
                                    "online players pls"));
                        } catch (IOException e) {
                            throw new RuntimeException(e);
                        }

                        onlinePlayersData.setAll(Client.onlineplayers);
                            friendsData.setAll(Client.friends);
                            playRequestsData.setAll(Client.Play_requests);
                            friendRequestsData.setAll(Client.friends_requests);

                    })
            );
            refresher.setCycleCount(Timeline.INDEFINITE);
            refresher.play();



        return scene;
    }

    // Helper to send any one-off command to the server:
    private static void sendToServer(int type, String to, String payload) {
        try {
            Client.out.writeObject(
                    new Communication(Client.username, to, type, payload)
            );
        } catch(IOException e) {
            e.printStackTrace();
        }
    }

    public static Scene getLobbyrulesScene() {
        // Load background image with full cover scaling
        Image image = new Image("rules.png");
        BackgroundSize size = new BackgroundSize(1285, 820, false, false, false, false);
        BackgroundImage bgImage = new BackgroundImage(
                image,
                BackgroundRepeat.NO_REPEAT,
                BackgroundRepeat.NO_REPEAT,
                BackgroundPosition.CENTER,
                size
        );
        Background background = new Background(bgImage);

        Button back = new Button("Back");
        back.setPrefWidth(100);
        back.setStyle("-fx-background-color: transparent; -fx-text-fill: transparent; -fx-font-size: 25px; -fx-font-weight: bold;");
        back.setOnAction(e -> SceneManager.switchSceneWithSplit(getLobbyHomeScene()));

        TextField spacing = new TextField();
        spacing.setStyle(
                "-fx-font-size: 2px; -fx-background-color: transparent; -fx-border-color: transparent; -fx-text-fill: transparent;"
        );
        spacing.setEditable(false);

        VBox buttonBox = new VBox(18, back,spacing);

        buttonBox.setAlignment(Pos.BOTTOM_CENTER);
        buttonBox.setPadding(new Insets(100));

        StackPane root = new StackPane(buttonBox);
        root.setBackground(background);

        return new Scene(root, 1285, 820);
    }




    public static Scene winlosepage(boolean lose) {
        // Load background image at original size
        Image image = (lose)? new Image("lose.png") : new Image("win.png");
        BackgroundSize size = new BackgroundSize(1285, 820, false, false, false, false);
        BackgroundImage bgImage = new BackgroundImage(image, BackgroundRepeat.NO_REPEAT, BackgroundRepeat.NO_REPEAT, BackgroundPosition.CENTER, size);
        Background background = new Background(bgImage);



        Button back = new Button("Quit");
        back.setPrefWidth(100);
        back.setStyle("-fx-background-color: transparent; -fx-text-fill: transparent; -fx-font-size: 25px; -fx-font-weight: bold;");
        back.setOnAction(e -> SceneManager.switchSceneWithSplit(getLobbyHomeScene()));


        Button play_again = new Button("play_again");
        play_again.setPrefWidth(100);
        play_again.setStyle("-fx-background-color: transparent; -fx-text-fill: transparent; -fx-font-size: 25px; -fx-font-weight: bold;");
        String who = BoardModel.opponent;
        play_again.setOnAction(e -> {
            if (Client.Play_requests.contains( who )){
                sendToServer(Communication.ACCEPT_GAME, who, "");
                Client.Play_requests.remove(who);
            }else{
                sendToServer(Communication.REQUEST_PLAY, who, "Hi, let's play Again!");
            }

        });


        TextField spacing2 = new TextField();
        spacing2.setMinWidth(210);
        spacing2.setStyle("-fx-background-color: transparent; -fx-border-color: transparent;");
        spacing2.setEditable(false);

        TextField spacing = new TextField();
        spacing.setStyle(
                "-fx-font-size: 2px; -fx-background-color: transparent; -fx-border-color: transparent; -fx-text-fill: transparent;"
        );
        spacing.setEditable(false);
        back.setPrefWidth(220);
        back.setPrefHeight(80);
        play_again.setPrefWidth(280);
        play_again.setPrefHeight(80);

        HBox but = new HBox(60,spacing2,back,play_again);
        VBox buttonBox = new VBox(18, but,spacing);

        buttonBox.setAlignment(Pos.BOTTOM_CENTER);
        buttonBox.setPadding(new Insets(60));

        StackPane root = new StackPane(buttonBox);
        root.setBackground(background);

        return new Scene(root, 1285, 820);
    }



}
