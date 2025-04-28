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
            in = new ObjectInputStream(socket.getInputStream());
            socket.setTcpNoDelay(true);
            System.out.println("Connected to server at " + host + ":" + port);

            // Start the listener thread
            connection = new ClientConnection(socket, out, in);
            listener = new Thread(connection);
            listener.start();

        } catch (IOException e) {
            System.out.println("Connection failed: " + e.getMessage());
        }
    }

    public String authenticate(String username, String password, int type, AtomicBoolean res) {
        this.username = username;
        Communication request = new Communication(username, "server", type, password);

        try {
            out.writeObject(request);


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
            return "Error during communication: " + e.getMessage();
        }
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
