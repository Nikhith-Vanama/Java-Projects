import java.io.*;
import java.net.*;

class ChatServer {
    public static void main(String[] args) {
        try (ServerSocket serverSocket = new ServerSocket(1234)) {
            System.out.println("Server started. Waiting for client...");
            Socket socket = serverSocket.accept();
            System.out.println("Client connected!");

            BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
            BufferedReader consoleInput = new BufferedReader(new InputStreamReader(System.in));

            // Thread to receive messages
            new Thread(() -> {
                String msgFromClient;
                try {
                    while ((msgFromClient = in.readLine()) != null) {
                        System.out.println("Client: " + msgFromClient);
                    }
                } catch (IOException e) {
                    System.out.println("Client disconnected.");
                }
            }).start();

            // Sending messages
            String msgToClient;
            while ((msgToClient = consoleInput.readLine()) != null) {
                out.println(msgToClient);
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
