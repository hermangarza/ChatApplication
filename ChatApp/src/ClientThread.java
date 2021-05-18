import java.io.*;
import java.net.Socket;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class ClientThread extends Thread {
    private DateFormat dateFormat = new SimpleDateFormat("E MMM yy hh:mm:ss aa");
    private Socket socket;
    private Server server;
    private PrintWriter writer;
    private DataInputStream input;
    private BufferedReader reader;
    private DataOutputStream output;

    public ClientThread(Socket socket, Server server) {
        this.socket = socket;
        this.server = server;
    }
    public void run() {
        try {
            input = new DataInputStream(new BufferedInputStream(socket.getInputStream()));
            reader = new BufferedReader(new InputStreamReader(input));
            output = new DataOutputStream(socket.getOutputStream());
            writer = new PrintWriter(output, true);

            String clientName = reader.readLine();
            server.add(clientName);

            String serverMSG = "New client connected. " + clientName;
            server.broadcast(serverMSG, this);

            String clientMSG = "";
            do {
                String formatDateTime = dateFormat.format(new Date());
                clientMSG = reader.readLine();
                serverMSG = "[" + clientName + "]: " + clientMSG + " (" + formatDateTime + ")";
                server.broadcast(serverMSG, this);

            } while (!clientMSG.equals("END"));

            server.remove(clientName, this);
            socket.close();
            serverMSG = clientName + " has been removed.";
            server.broadcast(serverMSG, this);

        } catch (IOException e) {
            System.out.println("Error. Could not use thread. " + e.getMessage());
            e.printStackTrace();
        }
    }
    public void messageSent(String message) {
        writer.println(message);

    }
}