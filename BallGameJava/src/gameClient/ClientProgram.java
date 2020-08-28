package gameClient;

import java.util.Scanner;

public class ClientProgram {
    public static void main(String[] args) {
        try {
            Scanner in = new Scanner(System.in); // reading locally

            try (Client client = new Client()) {
                System.out.println("Connected successfully. Your player ID is " + client.getPlayerID() + ".");
                client.getListOfCurrentPlayers();

                while (true) {
                    System.out.println("What would you like to do?");
                    System.out.println("1. INFO\n2. THROW");

                    String choice = in.nextLine().trim().toUpperCase();
                    switch (choice) {
                        case "INFO":
                            client.getListOfCurrentPlayers();
                            break;
                        case "THROW" :
                            if (client.getHasBall()) {
                                client.getListOfCurrentPlayers();
                                System.out.println("Who would you like to throw the ball to?: ");
                                int fromPlayerID = client.getPlayerID();
                                int toPlayerID = Integer.parseInt(in.nextLine());
                                client.throwBall(fromPlayerID, toPlayerID);
                            }
                            else
                                System.out.println("You do not have possession of the ball.");
                            break;
                        default:
                            System.out.println("Unknown command: " + choice);
                            break;
                    }
                }
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }
}
