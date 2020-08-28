package gameServer;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Game {
    private final List<Player> players = new ArrayList<>(); // this will store a list of online players
    private int maxNumID;

    // start game
    public void startGame() {
        System.out.println("Starting a game...");
    }

    // method createPlayer creates a new player and adds them to the list of online players
    private void createPlayer(int playerID, boolean hasBall) {
        Player player = new Player(playerID, hasBall);
        players.add(player);
    }

    // method createPlayerID creates a unique player ID - this is used in the process of creating a new player
    public int createPlayerID() {
        synchronized (players) {
            maxNumID = 0;

            if (players.size() != 0) {
                for (Player player : players)
                    if (maxNumID < player.getPlayerID())
                        maxNumID = player.getPlayerID();
            }

            int uniquePlayerID = maxNumID + 1; // so numbers won't be reused

            if (players.size() < 1) // give the ball to the first player to join the game
                createPlayer(uniquePlayerID, true);
            else
                createPlayer(uniquePlayerID, false);

            return uniquePlayerID;
        }
    }

    // prints out a list of the current players
    public void printListOfCurrentPlayers() {
        if (players.size() != 0) {
            if (players.size() == 1)
                System.out.println("There is 1 player currently in the game:");
            else
                System.out.println("There are " + players.size() + " currently in the game:");

            System.out.print("[ ");
            for (Player player : players) {
                System.out.print("( Player ID " + player.getPlayerID() + ", has ball: " + player.getHasBall() + " )");
            }
            System.out.println(" ]");
        }
        else
            System.out.println("There are no players in the game.");
    }

    // method to get multi-line string for displaying to client
    public String getListOfCurrentPlayers() {
        String list = "";

        if (players.size() == 1)
            list = list + "1 player in the game: ";
        else
            list = list + players.size() + " players in the game: ";

        for (Player player : players) {
            list = list + "( Player ID " + player.getPlayerID() + ", has ball: " + player.getHasBall() + " )";
        }

        return list;
    }

    // updates list of online players when a player has left
    public void playerLeft(int playerID) {
        int indexRemove = 0;

        for (int i = 0; i < players.size(); i++) {
            if (playerID == players.get(i).getPlayerID())
                indexRemove = i;
        }

        players.remove(indexRemove);
    }

    // see if player has the ball
    public boolean getHasBall(int playerID) {
        boolean hasBall = false;

        for (Player player : players)
            if (playerID == player.getPlayerID())
                hasBall = player.getHasBall();

        return hasBall;
    }

    // pass the ball to another player
    public void throwBall(int fromPlayerID, int toPlayerID) {
        int fromIndex = 0;
        int toIndex = 0;

        // get index of fromPlayer
        for (int i = 0; i < players.size(); i++) {
            if (fromPlayerID == players.get(i).getPlayerID())
                fromIndex = i;
        }

        // get index of toPlayer
        for (int i = 0; i < players.size(); i++) {
            if (toPlayerID == players.get(i).getPlayerID())
                toIndex = i;
            else toIndex = fromIndex;
        }

        Player fromPlayer = players.get(fromIndex);
        Player toPlayer = players.get(toIndex);

        fromPlayer.setHasBall(false);
        toPlayer.setHasBall(true);

        if (fromPlayer != toPlayer)
            System.out.println("Player ID " + fromPlayerID + " has passed the ball to Player ID " + toPlayerID);
        else
            System.out.println("No valid player with that ID. The ball remains with Player ID " + fromPlayerID + ".");
    }

    // if the player with the ball has left the game, pass the ball to a random player
    public void throwBall(int fromPlayerID) {
        Random rand = new Random();
        int chosenPlayer = 0;

        if (players.size() != 0) {
            chosenPlayer = rand.nextInt(players.size());
            players.get(chosenPlayer).setHasBall(true);
            System.out.println("The ball has been passed to Player ID " + players.get(chosenPlayer).getPlayerID());
        }
    }

    // end
}
