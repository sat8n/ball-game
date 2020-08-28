package gameClient;

import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;

public class Client implements AutoCloseable {
    final int port = 6666;

    private final Scanner reader; // to read from server
    private final PrintWriter writer; // to write to server

    private int playerID;

    public Client() throws Exception {
        // connecting to the server and creating objects for communications
        Socket socket = new Socket("localhost", port);
        reader = new Scanner(socket.getInputStream());

        // automatically flushed the stream with every command
        writer = new PrintWriter(socket.getOutputStream(), true);

        // parsing the response
        String line = reader.nextLine();
        playerID = Integer.parseInt(line.split(" ")[4]); // instead of sending an ID number we want the server to assign the client a unique ID
        if (line.split(" ")[0].trim().compareToIgnoreCase("successfully") != 0)
            throw new Exception(line);

    // client() end
    }

    public int getPlayerID() { return playerID; }

    public void getListOfCurrentPlayers() {
        // sending command
        writer.println("ONLINE");

        // reading response
        String line = reader.nextLine();
        System.out.println(line);
    }

    public boolean getHasBall() {
        // sending command
        writer.println("HASBALL");

        return Boolean.parseBoolean(reader.nextLine());
    }

    public void throwBall(int fromPlayerID, int toPlayerID) {
        // sending command
        writer.println("THROW " + fromPlayerID + " " + toPlayerID);
    }

    @Override
    public void close() throws Exception {
        reader.close();
        writer.close();
    }

    // end
}
