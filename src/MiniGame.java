import java.util.Random;

public class MiniGame {
    public static int miniLevelCounter;
    private final int NUMLEVELS = 3;
    public MiniGame() {
        miniLevelCounter = 0;
        while (miniLevelCounter < NUMLEVELS) {
            runRandomGame();
            miniLevelCounter++;
        }
    }
    /** Runs a random mini-game */
    public void runRandomGame() {
        String[] games = new String[]{"BALLS", "DROPS", "SHOOTER"};
        Random r = new Random();
        String game = games[r.nextInt(0, 3)];
        switch (game) {
            case "BALLS":
                Balls balls = new Balls();
                break;
            case "DROPS":
                Drops drops = new Drops();
                break;
            case "SHOOTER":
                Shooter shooter = new Shooter();
                break;
            default:
                break;
        }
    }
}
