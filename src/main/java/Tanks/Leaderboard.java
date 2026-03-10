package Tanks;
import java.util.*;

/**
 * Object representing Leaderboards
 */
public class Leaderboard extends Elements{
    private int leaderboardTick;
    private final App app;
    private ArrayList<Tank> playerRanks = new ArrayList<>();
    private int buttonPosY;
    /**
     * Initializes the leaderboard object
     * @param app : app
     * @param playerRanks : game player ranks
     */
    public Leaderboard(App app, ArrayList<Tank> playerRanks) {
        this.startTicker();
        this.app = app;
        this.playerRanks = playerRanks;
    }
    /**
     * Starts the Ticker
     */
    public void startTicker() {
        this.leaderboardTick = 1;
    }
    /**
     * Increments the ticker by 1
     */
    public void updateTicker() {
        this.leaderboardTick++;
    }
   
    public int getButtonPosY() {
        return buttonPosY;
    }
    /**
     * Draws the leaderboard to the screen
     */
    public void drawElement() {
        app.rectMode(CORNER);
        app.fill(55, 55);
        app.stroke(0);
        app.strokeWeight(5);
        app.rect(281, 250, 300, Math.min(this.leaderboardTick, 30) * (this.playerRanks.size() + 1), 10);
        this.buttonPosY = Math.min(280 + Math.min(this.leaderboardTick, 30) * (this.playerRanks.size() + 1), 600);
        app.rectMode(CORNER);
        Tank currentTank;
        for (int i = 0; i + 1 <= Math.min(this.leaderboardTick / 21, playerRanks.size()); i++) {
            currentTank = playerRanks.get(i);
            app.fill(currentTank.getColor()[0], currentTank.getColor()[1], currentTank.getColor()[2]);
            app.textSize(25);
            app.textAlign(LEFT, TOP);
            app.text("Player " + currentTank.getName(), 291, 265 + i * 30);
            app.textAlign(RIGHT, TOP);
            app.text(currentTank.getScore(), 565, 265 + i * 30); 
            app.textAlign(LEFT, TOP);
            if (i + 1 == playerRanks.size()) {
                app.textAlign(CENTER);
                Tank winner = playerRanks.get(0);
                app.fill(winner.getColor()[0], winner.getColor()[1], winner.getColor()[2]);
                app.text("Player " + winner.getName() + " wins!", 431, 210);
                app.rectMode(CENTER);

                app.fill(0);
                app.noStroke();
                app.rect(432, buttonPosY + 1, 298, 38);
                app.fill(180);
                app.stroke(130);
                app.strokeWeight(2);
                app.rect(431, buttonPosY, 296, 36);
                app.textAlign(CENTER, CENTER);
                
                app.fill(0);
                app.textSize(15);
                app.text("RESTART (R)", 433, buttonPosY);
                app.fill(255);
                app.textSize(15);
                app.text("RESTART (R)", 431, buttonPosY - 2);
                app.textAlign(LEFT, TOP);
                app.armRestart();
                app.rectMode(CORNER);
            }
        }
        updateTicker();
    }
}
 