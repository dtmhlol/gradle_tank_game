package Tanks;

import processing.core.PImage;
import processing.core.PFont;

/**
 * Object of Start Screen
 */
public class StartScreen extends Elements{
    private int startScreenTick;
    private final App app;
    private final int[] buttonPos;
    private final PFont font;
    private final PImage startBackgroundImage;
    private final PImage logo;
    public StartScreen(App app, PImage logo, PFont font, PImage startBackgroundImage) {
        this.startTicker();
        this.logo = logo;
        this.app = app;
        this.font = font;
        this.startBackgroundImage = startBackgroundImage;
        this.buttonPos = new int[]{283, 383, 300, 40};
    }
    public void startTicker() {
        this.startScreenTick = 0;
    }
    public void updateTicker() {
        this.startScreenTick++;
    }
    public int[] getButtonPos() {
        return buttonPos;
    }

    private static int[] cornerToCenter(int a, int b, int c, int d) {
        int[] cornerArray = new int[4];
        cornerArray[0] = a + (int) c/2;
        cornerArray[1] = b + (int) d/2;
        cornerArray[2] = c;
        cornerArray[3] = d;
        return cornerArray;
    }
    /**
     * Draws the Start Screen and its buttons
     */
    public void drawElement() {
        app.textFont(font);
        app.background(this.startBackgroundImage);
        app.imageMode(CENTER);
        app.image(logo, 431, 200, 600, 300);
        app.rectMode(CENTER);
        app.fill(20);
        app.noStroke();
        int[] buttonPosCenter = cornerToCenter(buttonPos[0], buttonPos[1], buttonPos[2], buttonPos[3]);
        app.rect(buttonPosCenter[0] + 1, buttonPosCenter[1] + 1, buttonPosCenter[2] + 1, buttonPosCenter[3] + 1);
        app.fill(180);
        app.stroke(130);
        app.strokeWeight(2);
        app.rect(buttonPosCenter[0], buttonPosCenter[1], buttonPosCenter[2] - 1, buttonPosCenter[3] - 1);
        app.textAlign(CENTER, CENTER);
        
        app.fill(0);
        app.textSize(25);
        app.text("START", buttonPosCenter[0] + 2, buttonPosCenter[1] + 1);
        app.fill(255);
        app.textSize(25);
        app.text("START", buttonPosCenter[0], buttonPosCenter[1]);
        app.fill(155);
        app.text("Press START or any key to continue...", (float) 432, 501 + 5 * (float) Math.sin(Math.PI * 0.03 * startScreenTick));
        app.fill(255);
        app.text("Press START or any key to continue...", (float) 431, 500 + 5 * (float) Math.sin(Math.PI * 0.03 * startScreenTick));

            
        updateTicker();
    }
}
 