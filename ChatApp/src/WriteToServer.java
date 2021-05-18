import java.io.Console;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;

public class WriteToServer extends Thread {
    private Socket socket;
    private Client client;
    private DataOutputStream output;
    private PrintWriter writer;

    public WriteToServer(Socket socket, Client client) {
        this.socket = socket;
        this.client = client;
        write();
    }
    private void write() {
        try {
            output = new DataOutputStream(socket.getOutputStream());
            writer = new PrintWriter(output, true);
        } catch (IOException e) {
            System.out.println("Error. Could not get output stream. " + e.getMessage());
            e.printStackTrace();
        }
    }
    public void run() {
        Console console = System.console();
        String clientName = "";
        clientName= console.readLine("\nEnter client name: ");
        client.setClientName(clientName);
        writer.println(clientName);
        
        String text = "";
        do {
            text = console.readLine("[" + clientName + "]: ");
            writer.println(text);
        } while(!text.equals("END"));
        close();
    }
    private void close() {
        try {
            socket.close();
        } catch (IOException e) {
            System.out.println("Error. Could not write to server. " + e.getMessage());
            e.printStackTrace();
        }
    }
}
