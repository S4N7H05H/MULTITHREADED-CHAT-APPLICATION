package Multithreaded_Chat_Application;

import java.io.*;
import java.net.*;
import java.util.*;
import java.util.concurrent.*;

public class Server {

    private static final List<ClientHandler> clients = new ArrayList<>();
    
    private static ExecutorService pool = Executors.newFixedThreadPool(20); 

    private static final int PORT = 5000;

    public static void main(String[] args) {
        System.out.println("--- CODTECH Chat Server ---");
        System.out.println("Starting server on port " + PORT + "...");

        try (ServerSocket serverSocket = new ServerSocket(PORT)) {
            
            
            while (true) {
                Socket clientSocket = serverSocket.accept();
                System.out.println("\n[SERVER] New client connected: " + clientSocket.getInetAddress().getHostAddress());

                ClientHandler clientThread = new ClientHandler(clientSocket);
                
                pool.execute(clientThread);
            }

        } catch (IOException e) {
            System.err.println("[SERVER] Error starting server: " + e.getMessage());
            pool.shutdown();
        }
    }

    public static void broadcast(String message, ClientHandler sender) {
        synchronized (clients) {
            for (ClientHandler client : clients) {
                if (client != sender) {
                    client.sendMessage(message);
                }
            }
        }
    }

    public static void removeClient(ClientHandler client) {
        synchronized (clients) {
            clients.remove(client);
        }
        System.out.println("[SERVER] Client " + client.getUsername() + " disconnected.");
        broadcast("SERVER: " + client.getUsername() + " has left the chat.", client);
    }
    
    private static class ClientHandler implements Runnable {
        private Socket clientSocket;
        private BufferedReader input;
        private PrintWriter output;
        private String username;

        public ClientHandler(Socket socket) {
            this.clientSocket = socket;
        }
        
        public String getUsername() {
            return username != null ? username : "Unknown";
        }

        @Override
        public void run() {
            try {

                input = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
                output = new PrintWriter(clientSocket.getOutputStream(), true);

                synchronized (clients) {
                    clients.add(this);
                }

                output.println("SERVER: Welcome! Please enter your username: ");
                username = input.readLine();

                if (username == null || username.trim().isEmpty()) {
                    username = "User" + new Random().nextInt(1000); 
                }
                
                System.out.println("[SERVER] " + username + " joined the chat.");
                
                broadcast("SERVER: " + username + " has joined the chat.", this);
                
                String message;

                while ((message = input.readLine()) != null) {
                    if (message.equalsIgnoreCase("/quit")) {
                        break;
                    }
                    String fullMessage = username + ": " + message;
                    System.out.println("[LOG] " + fullMessage);
                    broadcast(fullMessage, this);
                }

            } catch (IOException e) {
                System.err.println("[ERROR] Handler for " + username + " failed or client disconnected.");
            } finally {
                try {
                    clientSocket.close();
                } catch (IOException e) {
                }
                removeClient(this);
            }
        }
        public void sendMessage(String message) {
            if (output != null) {
                output.println(message);
            }
        }
    }
}