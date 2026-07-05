package org.connect4;

import java.io.*;
import java.net.Socket;
import java.util.ArrayList;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.atomic.AtomicBoolean;

public class Client {
    public static String username;
    public static Socket socket;
    public static ObjectOutputStream out;
    public ObjectInputStream in;
    public static ArrayList<String> friends = new ArrayList<>();
    public static ArrayList<String> friends_requests = new ArrayList<>();
    public static ArrayList<String> Play_requests = new ArrayList<>();
    public static BlockingQueue<Communication> responseQueue = new LinkedBlockingQueue<>();
    public static ArrayList<String> onlineplayers = new ArrayList<>();
    public static ArrayList<String> messages = new ArrayList<>();
    public static ArrayList<String> messages_from_me = new ArrayList<>();

    private Thread listener;
    private static ClientConnection connection;
    private String connectionError;
    public static BoardModel game = new BoardModel();
    public static GameBoard gameboard;


    public static void end() {
        try {
            saveFriends();
            if (connection != null) connection.stop();
            if (socket != null) socket.close();
        } catch (IOException e) {
            System.out.println("Error while closing client: " + e.getMessage());
        }
    }

    public Client(String host, int port) {
        try {
            socket = new Socket(host, port);
            out = new ObjectOutputStream(socket.getOutputStream());
            out.flush();
            in = new ObjectInputStream(socket.getInputStream());
            socket.setTcpNoDelay(true);
            connectionError = null;
            System.out.println("Connected to server at " + host + ":" + port);

            // Start the listener thread
            connection = new ClientConnection(socket, out, in);
            listener = new Thread(connection);
            listener.start();

        } catch (IOException e) {
            connectionError = "Connection failed: " + e.getMessage();
            closeFailedConnection();
            System.out.println(connectionError);
        }
    }

    public String authenticate(String username, String password, int type, AtomicBoolean res) {
        if (!isConnected()) {
            res.set(false);
            return connectionError != null ? connectionError : "Not connected to server.";
        }

        this.username = username;
        Communication request = new Communication(username, "server", type, password);

        try {
            out.writeObject(request);
            out.flush();


            // Wait for the specific response related to this request
            Communication response = responseQueue.take();
            System.out.println("Server: " + response.toString());

            if (type == Communication.CREATE_ACCOUNT) {
                if (response.whatIsIt == Communication.SUCCESS) {
                    res.set(true);
                    loadFriends();
                    return "Account created successfully. Proceeding to lobby...";
                } else {
                    res.set(false);
                    return "Account creation failed. Try again.";
                }
            } else { // LOGIN
                if (response.whatIsIt == Communication.SUCCESS) {
                    res.set(true);
                    loadFriends();
                    return "Authentication successful. Proceeding to lobby...";
                } else {
                    res.set(false);
                    return "Authentication failed. Try again.";
                }
            }

        } catch (IOException | InterruptedException e) {
            res.set(false);
            return "Error during communication: " + e.getMessage();
        }
    }

    public boolean isConnected() {
        return socket != null && socket.isConnected() && !socket.isClosed() && out != null && in != null;
    }

    private void closeFailedConnection() {
        try {
            if (socket != null) {
                socket.close();
            }
        } catch (IOException ignored) {}
        socket = null;
        out = null;
        in = null;
        connection = null;
    }

    public static void loadFriends() {
        File file = new File("friends/" + username + ".csv");

        if (!file.exists()) {
            System.out.println("No friends file found for " + username);
            return;
        }

        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String friend = line.trim();
                if (!friend.isEmpty()) {
                    friends.add(friend);
                }
            }
        } catch (IOException e) {
            System.out.println("Error reading friends file: " + e.getMessage());
        }
    }
    private static void saveFriends() {
        if (username == null || username.isEmpty()) return;

        File dir = new File("friends");
        if (!dir.exists()) dir.mkdir();

        File file = new File("friends/" + username + ".csv");

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(file, false))) {
            for (String friend : friends) {
                writer.write(friend);
                writer.newLine();
            }
        } catch (IOException e) {
            System.out.println("Error writing friends file: " + e.getMessage());
        }
    }

//    public static void main(String[] args) {
//        Client client = new Client("localhost", 5555);
//        AtomicBoolean res = new AtomicBoolean(false);
//        String result = client.authenticate("Manav", "password", Communication.LOGIN, res);
////        Runtime.getRuntime().addShutdownHook(new Thread(Client::end));
//        System.out.println("Login result: " + result);
//        System.out.println("Friends loaded: " + client.friends);
//
//    }
}
