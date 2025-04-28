package org.connect4;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;

public class Server {

    private static final int PORT = 5555;
    private static final String USER_CSV_FILE = "user_data.csv";

    // Stores clients - username -> ClientHandler
    public static final Map<String, ClientHandler> activeClients = new HashMap<>();

    // Stores users info - username -> password
    public static final Map<String, String> userInfo = new HashMap<>();

    public static void main(String[] args) {
        loadUsersFromCSV();
        System.out.println(userInfo);

        Runtime.getRuntime().addShutdownHook(new Thread(Server::saveUsersToCSV));

        try (ServerSocket serverSocket = new ServerSocket(PORT)) {
            System.out.println("Server is running -> "+ serverSocket.toString());

            while (true) {
                Socket clientSocket = serverSocket.accept();
                System.out.println("New client connected: " + clientSocket.getInetAddress());

                ClientHandler clientHandler = new ClientHandler(clientSocket);
                Thread thread = new Thread(clientHandler);
                thread.start();
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void loadUsersFromCSV() {
        try (BufferedReader br = new BufferedReader(new FileReader(USER_CSV_FILE))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length == 2) {
                    userInfo.put(parts[0].trim(), parts[1].trim());
                }
            }
            System.out.println("Loaded user data from CSV.");
        } catch (IOException e) {
            System.out.println("No existing user data found. Starting fresh.");
        }
    }

    private static void saveUsersToCSV() {
        try (PrintWriter writer = new PrintWriter(new FileWriter(USER_CSV_FILE))) {
            for (Map.Entry<String, String> entry : userInfo.entrySet()) {
                writer.println(entry.getKey() + "," + entry.getValue());
            }
            System.out.println("Saved user data to CSV.");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
