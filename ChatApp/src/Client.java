import java.io.*;
import java.net.Socket;
import java.net.UnknownHostException;

public class Client {
    private Socket socket;
    private final String address;
    private final int port;
    private String clientName;

    public Client(String address, int port) {
        this.address = address;
        this.port = port;
        connect();
    }

    //establish a connection between client and server
    private void connect() {
        try {
            socket = new Socket(address, port);
            System.out.println("Connected");
            System.out.println("Enter \"END\" to disconnect.");

            new ReadToServer(socket, this).start();
            new WriteToServer(socket, this).start();


        } catch (UnknownHostException e) {
            System.out.println("Error. Server not found. " + e.getMessage());
            e.printStackTrace();
        } catch (IOException e) {
            System.out.println("IO Error. " + e.getMessage());
            e.printStackTrace();

        }
    }

    public void setClientName(String clientName) {
        this.clientName = clientName;

    }

    public String getClientName() {
        return clientName;

    }

    public static void main(String[] args) {
        Client client = new Client("127.0.0.1", 8000);
    }
}