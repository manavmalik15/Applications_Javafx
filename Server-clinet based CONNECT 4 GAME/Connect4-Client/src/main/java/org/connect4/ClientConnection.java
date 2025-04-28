package org.connect4;

import javafx.application.Platform;

import java.io.*;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Arrays;

public class ClientConnection implements Runnable {
    public Socket socket;
    public ObjectOutputStream out;
    public ObjectInputStream in;
    public volatile boolean run = true;

    public ClientConnection(Socket socket, ObjectOutputStream out, ObjectInputStream in) {
        this.socket = socket;
        this.out = out;
        this.in = in;
    }

    public void stop() {
        run = false;
        try {
            if (Client.username != null) {
                out.writeObject(new Communication(Client.username, "server", Communication.BYE, "BYE-BYE"));
            }
        } catch (Exception ignored) {}
    }

    public boolean sendMessage(Communication message) {
        try {
            out.writeObject(message);
            out.flush();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public void run() {
        try {
            while (run) {
                Object obj = in.readObject();
                if (obj instanceof Communication comm) {
                    System.out.println("[Thread Listener] Received: " + comm);

                    switch (comm.whatIsIt) {

                        case Communication.REQUEST_ONLINE_PLAYERS -> {
                            if (comm.message != null && !comm.message.isBlank()) {
                                String[] nameArray = comm.message.split(",");
                                Client.onlineplayers = new ArrayList<>(Arrays.asList(nameArray));
                            }
                        }

                        case Communication.MESSAGE -> {
                            if (comm.sender != null && comm.message != null) {
//                                Client.messages.add(comm.sender + " : " + comm.message);
                                Platform.runLater(() -> {
                                    GameBoard.addChatBubble(comm.message,false);
                                });
                            }
                        }

                        case Communication.ADD_FRIEND -> {
                            if (comm.sender != null && comm.message != null) {
                                Client.friends_requests.add(comm.sender);
                            }
                        }

                        case Communication.ACCEPT_FRIND -> {
                            if (comm.sender != null && comm.message != null) {
                                Client.friends.add(comm.sender);
                            }
                        }

                        case Communication.REQUEST_PLAY -> {
                            if (comm.sender != null && comm.message != null) {
                                Client.Play_requests.add(comm.sender);
                            }
                        }

                        case Communication.ACCEPT_GAME -> {
                            String opp = comm.sender;
                            Client.game = new BoardModel();
                            Client.game.setOpponent(opp);
                            LobbyUI.refresher.stop();
                            Client.gameboard = new GameBoard();
                            Client.gameboard.can_move = comm.message.equals("1");
                            Platform.runLater(() -> {
                                SceneManager.switchSceneWithSplit(Client.gameboard.getScene());
                            });

                        }

                        case Communication.MOVE -> {
                            int col = Integer.parseInt(comm.message);
                            Platform.runLater(() -> {
                                Client.game.makeMove(col,2);
                            });
                        }


                        case Communication.END_GAME -> {
                            System.out.println("Game Ended: " + comm.message);
                            Platform.runLater(() -> {
                                SceneManager.switchSceneWithSplit(LobbyUI.winlosepage(comm.message.equals("lose")));
                            });
                        }



                        // Default case puts responses into the blocking queue for client to process
                        default -> {
                            try {
                                Client.responseQueue.put(comm);
                            } catch (InterruptedException e) {
                                System.out.println("[Thread Listener] Queue error: " + e.getMessage());
                            }
                        }
                    }
                }
            }
        } catch (Exception e) {
            System.out.println("[Thread Listener] Connection lost or error: " + e.getMessage());
        }
    }
}
