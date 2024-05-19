package org.example;

import java.io.*;
import java.net.*;
import java.util.*;
import java.util.concurrent.*;

public class P2PNetwork {
    private List<Node> nodes;
    private ServerSocket serverSocket;
    private ExecutorService executor;

    public P2PNetwork(int port) throws IOException {
        this.nodes = new ArrayList<>();
        this.serverSocket = new ServerSocket(port);
        this.executor = Executors.newCachedThreadPool();
        startServer();
    }

    public void addNode(Node node) {
        if (!nodes.contains(node)) {
            nodes.add(node);
        }
    }

    public List<Node> getNodes() {
        return nodes;
    }

    private void startServer() {
        executor.submit(() -> {
            while (true) {
                try {
                    Socket clientSocket = serverSocket.accept();
                    handleClient(clientSocket);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private void handleClient(Socket clientSocket) {
        executor.submit(() -> {
            try (BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
                 PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true)) {
                String message;
                while ((message = in.readLine()) != null) {
                    System.out.println("Received: " + message);
                    processMessage(message);
                }
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                try {
                    clientSocket.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private void processMessage(String message) {
        // Implement message processing logic
        // e.g., handle new transactions or blocks
    }

    public void broadcast(String message) {
        for (Node node : nodes) {
            try (Socket socket = new Socket(node.getHost(), node.getPort());
                 PrintWriter out = new PrintWriter(socket.getOutputStream(), true)) {
                out.println(message);
            } catch (IOException e) {
                System.err.println("Failed to send message to " + node + ": " + e.getMessage());
            }
        }
    }
}
