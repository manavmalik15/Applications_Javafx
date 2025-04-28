package org.connect4;

import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;

import java.io.IOException;

public class GameBoard {
    private static final int COLUMNS = 7;
    private static final int ROWS = 6;

    Image bgImage = new Image("backboard2.jpg");
    Image redDisc = new Image("player1.png");
    Image yellowDisc = new Image("player2.png");
    private final Scene scene;
    public static GridPane grid;
    public static Boolean can_move = true;

    public static TextField whos_turn;

    // Chat UI components
    private static VBox chatBox = null;
    private static ScrollPane chatScroll = null;
    private TextField messageField;

    public GameBoard() {
        // Root container
        HBox root = new HBox(20);
        root.setPadding(new Insets(20));
        BackgroundSize bsz = new BackgroundSize(1280, 800, false, false, false, false);
        root.setBackground(new Background(new BackgroundImage(
                bgImage,
                BackgroundRepeat.NO_REPEAT,
                BackgroundRepeat.NO_REPEAT,
                BackgroundPosition.CENTER,
                bsz
        )));

        TextField name = new TextField(Client.username);
        TextField opp_name = new TextField(BoardModel.opponent);
        whos_turn = new TextField((can_move)? Client.username:BoardModel.opponent );

        // Create grid pane for discs
        grid = new GridPane();
        grid.setHgap(14.5);
        grid.setVgap(14.5);

        // Column & row constraints
        for (int i = 0; i < COLUMNS; i++) {
            ColumnConstraints cc = new ColumnConstraints();
            cc.setPrefWidth(60);
            grid.getColumnConstraints().add(cc);
        }
        for (int i = 0; i < ROWS; i++) {
            RowConstraints rc = new RowConstraints();
            rc.setPrefHeight(60);
            grid.getRowConstraints().add(rc);
        }

        // Wrap grid in a Pane for overlays
        Pane boardPane = new Pane();
        grid.setLayoutX(0);
        grid.setLayoutY(0);
        boardPane.getChildren().add(grid);

        // Overlay transparent click zones
        double cellSize = 60;
        double gap = 14.5;
        double boardHeight = ROWS * cellSize + (ROWS - 1) * gap;

        for (int i = 0; i < COLUMNS; i++) {
            final int col = i;
            Pane overlay = new Pane();
            overlay.setPrefWidth(cellSize);
            overlay.setPrefHeight(boardHeight);
            overlay.setLayoutX(i * (cellSize + gap));
            overlay.setLayoutY(0);
            overlay.setBackground(new Background(new BackgroundFill(
                    Color.TRANSPARENT, null, null
            )));
            // Hover highlight
            overlay.setOnMouseEntered(e -> overlay.setBackground(new Background(new BackgroundFill(
                    Color.rgb(255, 255, 255, 0.25), null, null
            ))));
            overlay.setOnMouseExited(e -> overlay.setBackground(new Background(new BackgroundFill(
                    Color.TRANSPARENT, null, null
            ))));
            // Click handler
            overlay.setOnMouseClicked(e -> {
                if (can_move) {

                    int row = Client.game.makeMove(col, 1);
                    if (row >= 0) {
                        whos_turn.setText(BoardModel.opponent);
                        can_move = false;
                        adddot(col, row, true);
                    }
                }
            });
            boardPane.getChildren().add(overlay);
        }

        // UI controls
        Button quit = new Button("Quit");
        quit.setPrefWidth(50);
        quit.setStyle("-fx-background-color: transparent; -fx-text-fill: black; -fx-font-size: 15px;");
        quit.setOnAction(e -> {
            SceneManager.switchSceneWithSplit(LobbyUI.winlosepage(true));
            try {
                Client.out.writeObject(new Communication(Client.username,"server" , Communication.END_GAME,"i quit"));
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }
        });

        TextField spacing = new TextField();
        spacing.setMaxWidth(123);
        spacing.setEditable(false);
        spacing.setStyle(
                "-fx-font-size: 37px; -fx-background-color: transparent; -fx-border-color: transparent; -fx-text-fill: transparent;"
        );


        opp_name.setStyle(
                "-fx-font-size: 30px; -fx-background-color: transparent; -fx-border-color: transparent; -fx-text-fill: black;"
        );
        name.setStyle(
                "-fx-font-size: 30px; -fx-background-color: transparent; -fx-border-color: transparent; -fx-text-fill: black;"
        );
        whos_turn.setStyle(
                "-fx-font-size: 30px; -fx-background-color: transparent; -fx-border-color: transparent; -fx-text-fill: black;"
        );
        whos_turn.setAlignment(Pos.CENTER);
        name.setMaxWidth(200);
        opp_name.setMaxWidth(200);
        HBox names = new HBox(80, name, opp_name);

        TextField spacing2 = new TextField();
        spacing2.setStyle(
                "-fx-font-size: 7px; -fx-background-color: transparent; -fx-border-color: transparent; -fx-text-fill: transparent;"
        );
        spacing2.setEditable(false);
        TextField spacing3 = new TextField();
        spacing3.setStyle(
                "-fx-font-size: 17.5px; -fx-background-color: transparent; -fx-border-color: transparent; -fx-text-fill: transparent;"
        );
        spacing3.setEditable(false);
        TextField spacing4 = new TextField();
        spacing4.setStyle(
                "-fx-font-size: 29px; -fx-background-color: transparent; -fx-border-color: transparent; -fx-text-fill: transparent;"
        );
        spacing4.setEditable(false);
        TextField spacing5 = new TextField();
        spacing5.setStyle(
                "-fx-font-size: 30px; -fx-background-color: transparent; -fx-border-color: transparent; -fx-text-fill: transparent;"
        );
        spacing5.setEditable(false);

        // Assemble layout
        VBox name_grid = new VBox(spacing2, quit, spacing3, names, spacing4, boardPane, spacing5, whos_turn);

        root.getChildren().add(spacing);
        root.getChildren().add(name_grid);



        TextField spaceing2 = new TextField();
        spaceing2.setEditable(false);
        spaceing2.setMaxWidth(120);
        spaceing2.setStyle(
                " -fx-background-color: transparent; -fx-border-color: transparent; -fx-text-fill: transparent;"
        );
        root.getChildren().add(spaceing2);


        TextField pushup = new TextField();
        pushup.setEditable(false);
        pushup.setMaxWidth(120);
        pushup.setStyle(
                "-fx-font-size: 15px; -fx-background-color: transparent; -fx-border-color: transparent; -fx-text-fill: transparent;"
        );



        // the code for messeging

        // --- Right side: chat panel
        chatBox = new VBox(8);
        chatBox.setPadding(new Insets(10));
        chatBox.setStyle("-fx-background-color: transparent;");
        chatScroll = new ScrollPane(chatBox);
        chatScroll.setBackground(new Background(
                new BackgroundFill(Color.TRANSPARENT, CornerRadii.EMPTY, Insets.EMPTY)
        ));
        chatScroll.setStyle(
                "-fx-background-color: transparent;" +
                        "-fx-background: transparent;"
        );

        chatScroll.setFitToWidth(true);
        chatScroll.setVbarPolicy(ScrollPane.ScrollBarPolicy.ALWAYS);
        chatScroll.setPrefViewportHeight(400);
        VBox.setVgrow(chatScroll, Priority.ALWAYS);

        messageField = new TextField();
        messageField.setPromptText("Type a message...");
        messageField.setStyle(
                "-fx-font-size: 18px; -fx-background-color: transparent; -fx-border-color: transparent; -fx-text-fill: black;"
        );
        messageField.setMinWidth(260);
        messageField.setOnKeyPressed(e -> { if (e.getCode() == KeyCode.ENTER) sendMessage(); });
        Button sendButton = new Button("Send");
        sendButton.setMinHeight(40);
        sendButton.setStyle(" -fx-background-color: transparent; -fx-border-color: transparent; -fx-text-fill: transparent;");
        sendButton.setOnAction(e -> sendMessage());
        HBox messageBar = new HBox(15, messageField, sendButton);
        messageBar.setPadding(new Insets(5));
        messageBar.setAlignment(Pos.BOTTOM_RIGHT);

        VBox chatPanel = new VBox(35, chatScroll, messageBar,pushup);
        chatPanel.setPrefWidth(400);
        chatPanel.setAlignment(Pos.CENTER);
        chatPanel.setPadding(new Insets(10));

        root.getChildren().add(chatPanel);

        scene = new Scene(root, 1280, 800);
    }


    private void sendMessage() {
        String msg = messageField.getText().trim();
        if (msg.isEmpty()) return;
        addChatBubble(msg, true);
        //addChatBubble(msg, false);
        messageField.clear();
        Communication comm = new Communication(
                Client.username,
                BoardModel.opponent,
                Communication.MESSAGE,
                msg
        );
        try {
            Client.out.writeObject(comm);
        }catch (Exception e){
            System.out.println("faild to send message");
        }

    }

    public static void addChatBubble(String text, boolean isOwn) {
        Label lbl = new Label(text);
        lbl.setWrapText(true);
        lbl.setPadding(new Insets(6));
        lbl.setMaxWidth(200);
        lbl.setStyle("-fx-font-size: 25px; -fx-font-weight: bold; -fx-text-fill: black;");
        lbl.getStyleClass().add(isOwn ? "my-bubble" : "their-bubble");
        HBox bubbleHolder = new HBox(lbl);
        bubbleHolder.setAlignment(isOwn ? Pos.CENTER_RIGHT : Pos.CENTER_LEFT);
        bubbleHolder.setPadding(new Insets(2));
        chatBox.getChildren().add(bubbleHolder);
        chatScroll.layout(); chatScroll.setVvalue(1.0);
    }

    public void adddot(int col, int row, Boolean isRed) {
        ImageView iv = new ImageView(isRed ? redDisc : yellowDisc);
        iv.setFitWidth(60);
        iv.setFitHeight(60);
        iv.setPreserveRatio(true);
        grid.add(iv, col, row);
    }

    public Scene getScene() {
        return scene;
    }

    public void handleOpponentMove(int col, int row) {
        can_move = true;
        whos_turn.setText(Client.username);
        adddot(col, row, false);
    }
}
