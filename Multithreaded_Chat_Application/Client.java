package Multithreaded_Chat_Application;

import java.io.*;
import java.net.*;
import java.util.Scanner;

public class Client {
    private static final String SERVER_ADDRESS = "localhost"; 
    private static final int PORT = 5000;
    
    public static void main(String[] args) {
        System.out.println("--- CODTECH Chat Client ---");

        try (Socket socket = new Socket(SERVER_ADDRESS, PORT)) {
            
            BufferedReader serverInput = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            PrintWriter serverOutput = new PrintWriter(socket.getOutputStream(), true);
            Scanner scanner = new Scanner(System.in);
            
            
            
            String prompt = serverInput.readLine();
            System.out.print(prompt); 
            
            String username = scanner.nextLine();
            serverOutput.println(username);

            Thread receiverThread = new Thread(new ServerReceiver(serverInput));
            receiverThread.start();

            System.out.println("\nWelcome to the chat, " + username + "! Type /quit to exit.");

            while (scanner.hasNextLine()) {
                String message = scanner.nextLine();
                serverOutput.println(message);
                
                if (message.equalsIgnoreCase("/quit")) {
                    break;
                }
            }

            receiverThread.interrupt();
            scanner.close();
            
        } catch (ConnectException e) {
            System.err.println("\n[ERROR] Connection refused. Make sure the ChatServer is running on " + SERVER_ADDRESS + ":" + PORT);
        } catch (IOException e) {
            System.err.println("[ERROR] I/O Error: " + e.getMessage());
        }
        System.out.println("Client disconnected.");
    }
    
    private static class ServerReceiver implements Runnable {
        private BufferedReader input;

        public ServerReceiver(BufferedReader input) {
            this.input = input;
        }

        @Override
        public void run() {
            try {
                String message;
                while (!Thread.currentThread().isInterrupted() && (message = input.readLine()) != null) {
                    System.out.println(message);
                }
            } catch (IOException e) {
                if (!Thread.currentThread().isInterrupted()) {
                    System.err.println("[ERROR] Connection lost: " + e.getMessage());
                }
            }
        }
    }
}