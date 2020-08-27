package gameServer;

public class Player {
    private final int playerID;
    private boolean hasBall;

    public Player(int playerID, boolean hasBall) {
        this.playerID = playerID;
        this.hasBall = hasBall;
    }

    public int getPlayerID() {
        return playerID;
    }

    public boolean getHasBall() {
        return hasBall;
    }

    public void setHasBall(boolean ownership) {
        hasBall = ownership;
    }
}
