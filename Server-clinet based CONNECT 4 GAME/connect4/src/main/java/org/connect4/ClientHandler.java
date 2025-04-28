package org.connect4;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.Set;

public class ClientHandler implements Runnable {
    private Socket socket;
    private ObjectInputStream in;
    private ObjectOutputStream out;
    private String playerName;
    private GameSession gameSession;
    private boolean run = true;

    public ClientHandler(Socket socket) {
        try {
            this.socket = socket;
            this.out = new ObjectOutputStream(socket.getOutputStream());
            this.in = new ObjectInputStream(socket.getInputStream());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public boolean message(Communication message) {
        try {
            out.writeObject(message);
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("failed:" + message.toString());
            return false;
        }
        System.out.println(message.toString());
        return true;
    }


    @Override
    public void run() {
        try {
            while (run) {
                Object obj = in.readObject();
                if (obj instanceof Communication) {
                    Communication comm = (Communication) obj;

                    // all the functions based on the different messages

                    System.out.println("Received: " + comm);

                    switch (comm.whatIsIt) {
                        case Communication.LOGIN:
                            handleLogin(comm.sender, comm.message);
                            break;

                        case Communication.CREATE_ACCOUNT:
                            handleAccountCreation(comm.sender, comm.message);
                            break;

                        case Communication.MESSAGE:
                            ClientHandler receiverHandler = Server.activeClients.get(comm.receiver);

                            if (receiverHandler != null) {
                                boolean sent = receiverHandler.message(comm);
                                if (!sent) {
                                    System.out.println ("MESSAGE_FAILED: Could not deliver message to " + comm.receiver);
                                }
                            }
                            break;

                        case Communication.BYE:
                            System.out.println(playerName + " has left the server.");
                            break;


                        case Communication.MOVE:
                            try {
                                gameSession.move(this,Integer.valueOf(comm.message));
                            } catch (NumberFormatException e) {
                                System.out.println("Invalid string format");
                            }
                            break;

                        case Communication.REQUEST_ONLINE_PLAYERS:
                            Set<String> keys = Server.activeClients.keySet();
                            String result = "";
                            for (String key : keys) {
                                if (!key.equals(playerName)) {
                                    if (!result.isEmpty()) {
                                        result += ",";
                                    }
                                    // Add the key to the result string
                                    result += key;
                                }
                            }
                            sendResponse(result,Communication.REQUEST_ONLINE_PLAYERS);
                            break;

                        case Communication.START_RANDOM_MATCH:
                            for (ClientHandler other : Server.activeClients.values()) {
                                if (!other.equals(this) && other.getGameSession() == null) {
                                    other.message(new Communication(playerName,other.playerName,Communication.REQUEST_PLAY,""));
                                }
                            }
                            break;

                        case Communication.REQUEST_PLAY:
                            ClientHandler opponent = Server.activeClients.get(comm.receiver);
                            if (opponent != null && opponent.getGameSession() == null) {
                                opponent.message(comm);
                            } else {
                                sendResponse("MATCH_REQUEST_FAILED", Communication.FAILED);
                            }
                            break;

                        case Communication.ACCEPT_GAME:
                            ClientHandler the_person3 = Server.activeClients.get(comm.receiver);
                            if (the_person3 != null && the_person3.getGameSession() == null){
                                this.gameSession =  new GameSession(this, the_person3);
                                the_person3.message(comm);
                                this.message(new Communication(the_person3.playerName,this.playerName,Communication.ACCEPT_GAME,"1"));
                            }
                            break;

                        case Communication.ADD_FRIEND:
                            ClientHandler the_person = Server.activeClients.get(comm.receiver);
                           try {
                               the_person.message(comm);
                           }catch (Exception ignored) {}
                            break;
                        // add more cases as needed

                        case Communication.ACCEPT_FRIND:
                            try {
                                ClientHandler the_person2 = Server.activeClients.get(comm.receiver);
                                the_person2.message(comm);
                            }catch (Exception ignored) {}
                            break;

                        case Communication.END_GAME:
                            Communication endMsg = new Communication("server", "players", Communication.END_GAME,  "win");

                            if ( this.gameSession.player1 != this ) {
                                this.gameSession.player1.message(endMsg);
                                this.gameSession.player1.setGameSession(null);
                                this.gameSession.player2.setGameSession(null);
                            }else{
                                this.gameSession.player2.message(endMsg);
                                this.gameSession.player2.setGameSession(null);
                                this.gameSession.player1.setGameSession(null);
                            }
                            break;


                    }
                }
            }
        } catch (Exception e) {
            System.out.println(e);
            System.out.println("Connection with client lost.");
        } finally {
            try {

                if (playerName != null) {
                    Server.activeClients.remove(playerName);
                }
                if (this.gameSession != null){this.gameSession.endGame("ERROR: opponent left :( ");}

                socket.close();
            } catch (Exception ignored) {}
        }
    }

    private void handleLogin(String username, String password) {
        if (!Server.userInfo.containsKey(username)) {
            sendResponse("LOGIN_FAILED: User not found", Communication.FAILED);
        } else if (!Server.userInfo.get(username).equals(password)) {
            sendResponse("LOGIN_FAILED: Incorrect password", Communication.FAILED);
        } else if (Server.activeClients.containsKey(username)) {
            sendResponse("LOGIN_FAILED: User already logged in", Communication.FAILED);
        } else {
            this.playerName = username;
            Server.activeClients.put(username, this);
            sendResponse("LOGIN_SUCCESS", Communication.SUCCESS);
        }
    }

    private void handleAccountCreation(String username, String password) {
        if (username.isEmpty() || password.isEmpty()){
            sendResponse("ACCOUNT_FAILED: Username or Password can't be empty", Communication.FAILED);
        }
        if (Server.userInfo.containsKey(username)) {
            sendResponse("ACCOUNT_FAILED: Username already exists", Communication.FAILED);
        } else {
            Server.userInfo.put(username, password);
            this.playerName = username;
            Server.activeClients.put(username, this);
            sendResponse("ACCOUNT_SUCCESS: Account created successfully", Communication.SUCCESS);
        }
    }

    private void sendResponse(String message, int status) {
        try {
            Communication response = new Communication("server", playerName != null ? playerName : "unknown", status, message);
            out.writeObject(response);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void setPlayerName(String name) {
        this.playerName = name;
    }

    public String getPlayerName() {
        return this.playerName;
    }

    public void setGameSession(GameSession gameSession) {
        this.gameSession = gameSession;
    }

    public GameSession getGameSession() {
        return this.gameSession;
    }
}
