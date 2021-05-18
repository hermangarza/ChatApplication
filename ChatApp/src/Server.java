import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashSet;
import java.util.Set;

public class Server {
    private Socket socket;
    private ServerSocket server;
    private final int port;

    private Set<String> clients = new HashSet<String>();
    private Set<ClientThread> clientThreads = new HashSet<ClientThread>();

    public Server(int port) {
        this.port = port;
        connect();
        close();
    }

    private void connect() {
        try {
            server = new ServerSocket(port);
            System.out.println("Server started.");
            System.out.println("Waiting for a client...");
            while (true) {
                socket = null;
                try {
                    socket = server.accept();
                    System.out.println("Client accepted.");
                    System.out.println("Address & Host: " + socket.getInetAddress().getHostName());
                    ClientThread clientThread = new ClientThread(socket, this);
                    clientThreads.add(clientThread);
                    clientThread.start();

                } catch (IOException e) {
                    System.out.println("Server ended. " + e.getMessage());
                    e.printStackTrace();
                }

            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void close() {
        try {
            System.out.println("Closing connection");
            socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void add(String client) {
        clients.add(client);

    }

    public void remove(String client, ClientThread clientThread) {
        if (clients.remove(client)) {
            clients.remove(clientThread);
            System.out.println("Client " + client + " has been removed.");
        }

    }

    public void broadcast(String message, ClientThread excludedClient) {
        for (ClientThread client : clientThreads) {
            if (client != excludedClient) {
                client.messageSent(message);
            }
        }

    }
    public static void main(String[] args) {
        Server server = new Server(8000);
    }
}