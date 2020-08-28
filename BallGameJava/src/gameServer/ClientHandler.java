package gameServer;

import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;

public class ClientHandler implements Runnable {
    private final Socket socket;
    private Game game;

    public ClientHandler(Socket socket, Game game) {
        this.socket = socket;
        this.game = game;
    }

    @Override
    public void run() {
        int playerID = 0;

        try (
                // this reads from the client
                Scanner scanner = new Scanner(socket.getInputStream());
                // this prints to the client
                PrintWriter writer = new PrintWriter(socket.getOutputStream(), true)
                ) {
            try {
                playerID = game.createPlayerID(); // create a unique player ID
                System.out.println("New connection; player ID " + playerID + " has joined.");
                game.printListOfCurrentPlayers();

                writer.println("SUCCESSFULLY connection player ID " + playerID); // printing to client program

                while (true) {
                    String line = scanner.nextLine(); // read from client's line
                    String[] substrings = line.split(" "); // split client's line into substrings
                    switch(substrings[0].toLowerCase()) {
                        case "info":
                            System.out.println("Player " + playerID + " has requested for some information.");
                            writer.println("Info test");
                            break;
                        case "online":
                            writer.println(game.getListOfCurrentPlayers());
                            break;
                        case "hasball":
                            writer.println(game.getHasBall(playerID));
                            break;
                        case "throw":
                            int fromPlayerID = Integer.parseInt(substrings[1]);
                            int toPlayerID = Integer.parseInt(substrings[2]);
                            game.throwBall(fromPlayerID, toPlayerID);
                            break;
                        default:
                            throw new Exception("Unknown command: " + substrings[0]);
                    }
                }
            } catch (Exception e) { // second try
                writer.println("ERROR " + e.getMessage()); // printing to client
                socket.close();
            }
        } catch (Exception e) { }
        finally {
            System.out.println("Player " + playerID + " disconnected.");

            boolean checkHasBall = game.getHasBall(playerID);
            game.playerLeft(playerID);
            if (checkHasBall && game.getListOfCurrentPlayers().length() != 0) {
                game.throwBall(playerID);
                game.printListOfCurrentPlayers();
            }
        }
    }

    // end
}
