import java.io.*;
import java.net.Socket;

public class ReadToServer extends Thread {
    private Socket socket;
    private BufferedReader reader;
    private DataInputStream input;
    private Client client;

    public ReadToServer(Socket socket, Client client) {
        this.socket = socket;
        this.client = client;
        read();
    }

    private void read() {
        try {
            input = new DataInputStream(new BufferedInputStream(socket.getInputStream()));
            reader = new BufferedReader(new InputStreamReader(input));
        } catch (IOException e) {
            System.out.println("Error. Could not get input stream. " + e.getMessage());
            e.printStackTrace();

        }
    }
    public void run() {
        while (true) {
            try {
                String text = reader.readLine();
                System.out.println("\n" + text);

                if (client.getClientName() != null) {
                    System.out.print("[" + client.getClientName() + "]: ");
                }
            } catch (IOException e) {
                System.out.println("Error. Could not read from server. " + e.getMessage());
                e.printStackTrace();
                break;
            }
        }
    }
}