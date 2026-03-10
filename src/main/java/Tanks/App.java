package Tanks;

import processing.core.PApplet;
import processing.core.PImage;
import processing.core.PFont;
import processing.data.JSONArray;
import processing.data.JSONObject;
import processing.event.KeyEvent;
import processing.event.MouseEvent;

import java.util.*;
/**
 * 
 */
public class App extends PApplet {

    public static final int CELLSIZE = 32; //8;
    public static final int CELLHEIGHT = 32;

    public static final int CELLAVG = 32;
    public static final int TOPBAR = 0;
    public static int WIDTH = 864; //CELLSIZE*BOARD_WIDTH;
    public static int HEIGHT = 640; //BOARD_HEIGHT*CELLSIZE+TOPBAR;f
    public static final int BOARD_WIDTH = WIDTH/CELLSIZE;
    public static final int BOARD_HEIGHT = 20;

    public static final int INITIAL_PARACHUTES = 3;

    public static final int FPS = 30;

    private final String configPath;

    public static Random random = new Random();

    private Map gameMap;

    private static JSONObject json;

    private ArrayList<Map> gameMaps = new ArrayList<Map>(); 

    private PFont startfont;

    private PFont gameFont;

    private boolean debugMode = false;
	
	// Feel free to add any additional methods or attributes you want. Please put classes in different files.

    public App() {
        this.configPath = "config.json";
    }

    /**
     * Initialise the setting of the window size.
     */
	@Override
    public void settings() {
        size(WIDTH, HEIGHT);
    }
        
    /**
     * Load all resources such as images. Initialise the elements such as the player and map elements.
     */
    
	@Override
    public void setup() {
        frameRate(FPS);
		//See PApplet javadoc:
		//loadJSONObject(configPath)
		//loadImage(this.getClass().getResource(filename).getPath().toLowerCase(Locale.ROOT).replace("%20", " "));
        initializeMaps();
        this.startfont = createFont("src/main/java/Tanks/VCR_OSD_MONO_1.001.ttf", 15);
        this.gameFont = createFont("src/main/java/Tanks/Lato-Regular.ttf", 15);
        this.logo = loadImage("src/main/resources/Tanks/logo.png");
    }
    public boolean getShowLeaderboard() {
        return this.showLeaderboard;
    }
    public void setShowLeaderboard(boolean showLeaderboard) {
        this.showLeaderboard = showLeaderboard;
    }
    public boolean getStartScreen() {
        return this.startScreen;
    }
    public void setStartScreen(boolean startScreen) {
        this.startScreen = startScreen;
    }
    public ArrayList<Map> getGameMaps() {
        return this.gameMaps;
    }
    public void setDebugMode() {
        this.debugMode = true;
    }
    public Map getGameMap() {
        return this.gameMap;
    }
    public boolean getArmRestart() {
        return this.restartArmed;
    }
    private void initializeMaps() {
        this.gameMaps = new ArrayList<>();
        json = loadJSONObject(this.configPath);
        JSONArray levels = json.getJSONArray("levels");

        String fileDir;
        int[] MapColor = new int[3];
        String[] color_str;
        JSONObject currentLevel;
        String mapFileDir;
        

        for (int i = 0; i < levels.size(); i++) {
            currentLevel = levels.getJSONObject(i);
            fileDir = currentLevel.getString("layout");
            color_str = currentLevel.getString("foreground-colour").split(",");
            for (int j = 0; j < color_str.length; j++) {
                MapColor[j] = Integer.parseInt(color_str[j]);
            }
            if (currentLevel.hasKey("trees")) {
                mapFileDir = "src/main/resources/Tanks/" + currentLevel.getString("trees");
            } else {
                mapFileDir = null;
            }
            this.gameMaps.add(new Map(fileDir, this.loadImage("src/main/resources/Tanks/" + currentLevel.getString("background")), MapColor.clone(), this, json, i + 1, mapFileDir));    
        }
        

        this.gameMap = gameMaps.get(0);
        this.startBackgroundImage = this.gameMap.getBackgroundImg();
        
    }
    private PImage startBackgroundImage;
    private boolean startScreen = true;
    private PImage logo;
    private Leaderboard leaderboard;

    private StartScreen startScreenObj;
    private int[] buttonPosStart;

    private void startScreen() {
        if (startScreenObj == null){
            this.startScreenObj = new StartScreen(this, logo, startfont, startBackgroundImage);
            this.startScreenObj.drawElement();
            buttonPosStart = this.startScreenObj.getButtonPos();
        } else {
            this.startScreenObj.drawElement();
        }
    }

    private void leaderboard() {
        if(showLeaderboard){
            if (leaderboard == null){
                this.playerRanksUpdate();
                this.leaderboard = new Leaderboard(this, playerRanks);
                this.leaderboard.drawElement();
                this.buttonPosY = this.leaderboard.getButtonPosY();
                this.gameMap.setShowShop(false);
            } else {
                leaderboard.drawElement();
                this.gameMap.setShowShop(false);
            }
        }
    }

    private boolean showLeaderboard;
    private ArrayList<Tank> playerRanks = new ArrayList<>();
    private boolean restartArmed;
    private int buttonPosY; 

    private void playerRanksUpdate() {
        this.playerRanks = new ArrayList<>();
        for (Tank i: this.gameMap.getPlayers()){
            playerRanks.add(i);       
        playerRanks.sort((o1, o2) -> new Integer(o2.getScore()).compareTo(new Integer(o1.getScore())));
        }      
    }

    /**
     * Enables the Restart button
     */
    public void armRestart() {
        restartArmed = true;
    }
    
    private void updateGameMap(boolean now) {
        ArrayList<Integer> tempScoreList = new ArrayList<>();
        ArrayList<Integer> parachute = new ArrayList<>();
        ArrayList<int[]> colorArrayList = new ArrayList<>();
        if (this.gameMap.getGameStateClock() > 30 || now){
            for (Tank i: this.gameMap.getPlayers()){
                tempScoreList.add(i.getScore());
                parachute.add(i.getParachute());
                colorArrayList.add(i.getColor());
                i.clearProjectiles();
            }
            if (gameMaps.indexOf(this.gameMap) + 1 != this.gameMaps.size()){
                this.gameMap.gameMapClear();
                this.gameMap = gameMaps.get(gameMaps.indexOf(this.gameMap) + 1);
            } else {
                showLeaderboard = true;
            }
            for (int j = 0; j < tempScoreList.size(); j++) {
                if (this.gameMap != this.gameMaps.get(0)) {
                    Tank player = this.gameMap.getPlayers().get(j);
                    player.setScore(tempScoreList.get(j));
                    player.setParachute(parachute.get(j)); 
                    if (player.getColorRandom() == true) {
                        player.setColor(colorArrayList.get(j));
                    }
                }
            }
        } else {
            this.gameMap.incrementGameStateClock();
        }

    }
    /**
     * Receive key pressed signal from the keyboard.
     */
	@Override
    public void keyPressed(KeyEvent event) {
        if (this.debugMode){
            if (key == 'z') {
                this.gameMap.resetWind();
            }
            if (key == 'x') {
                this.gameMap.getCurrentTank().setScore(this.gameMap.getCurrentTank().getScore() + 20);
            }
            if (key == 'v') {
                this.gameMap.updateCurrentTank();
            }
            if (key == 'b') {
                this.gameMap.getCurrentTank().setHealth(0);
            }
            if (key == 'n') {
                this.gameMap.getCurrentTank().setPower(0);
            }
            if (key == 'm') {
                initializeMaps();
                this.playerRanks = new ArrayList<>();
                restartArmed = false;
                showLeaderboard = false;
                this.leaderboard = null;
            }
            if (key == 'l') {
                this.gameMap.getCurrentTank().setParachute(0);
            }
        }
        if (!this.startScreen && !this.restartArmed){
            if (this.gameMap.getGameState()){
                if (!this.gameMap.getCurrentTank().getFalling()){
                    if (key == CODED) {
                        if (keyCode == RIGHT){
                            this.gameMap.getCurrentTank().moveRight();
                            
                        }
                        if (keyCode == LEFT){
                            this.gameMap.getCurrentTank().moveLeft();
                        }
                        if (keyCode == UP){
                            this.gameMap.getCurrentTank().decreaseTurretAngle();
                        }
                        if (keyCode == DOWN){
                            this.gameMap.getCurrentTank().increaseTurretAngle();
                        }
                    }
                    if (key == ' '){
                        this.gameMap.getCurrentTank().fireProjectile();
                        this.gameMap.updateCurrentTank();
                        this.gameMap.updateWind();
                        
                    }
                    
                }
                if (key == 'w' ||key== 'W'){
                    this.gameMap.getCurrentTank().increasePower();
                }
                if (key == 's'){
                    this.gameMap.getCurrentTank().decreasePower();
                }
                if (key == 'r'){
                    this.gameMap.getCurrentTank().buyHealth();
                }
                if (key == 'f'){
                    this.gameMap.getCurrentTank().buyFuel();
                }
                if (key == 'p'){
                    this.gameMap.getCurrentTank().buyParachute();
                }
            } else {
                if (key == ' '){
                    updateGameMap(true);
                }
            }
            if (key == 'c'){
                updateGameMap(true);
            }
        } else {
            this.startScreen = false;
        }
        if (restartArmed && key == 'r'){
            initializeMaps();
            restartArmed = false;
            showLeaderboard = false;
            this.playerRanks = new ArrayList<>();
            
            this.leaderboard = null;
        }
    }
    
    private boolean inBounds(int a, int b, int c, int d) {
        if (this.mouseXPressed > a && mouseXReleased > a && this.mouseXPressed < a + c && this.mouseXReleased < a + c){
            if (this.mouseYPressed > b && mouseYReleased > b && this.mouseYPressed < b + d && this.mouseYReleased < b + d){
                return true;
            }
        }
        return false;
    }

    /**
     * Receive key released signal from the keyboard.
     */
	@Override
    public void keyReleased() {
        
    }
    private int mouseXPressed;
    private int mouseYPressed;
    private int mouseXReleased;
    private int mouseYReleased;
    private float distance(int a, int b, int c, int d) {
        return (float) Math.sqrt((a - c) * (a - c) + (b - d) * (b - d));
    }
    /**
     * Receive key released signal from the mouse.
     */
    @Override
    public void mousePressed(MouseEvent e) {
        System.out.println(mouseX);
        System.out.println(mouseY);
        this.mouseXPressed = mouseX;
        this.mouseYPressed = mouseY;
        this.mouseXReleased = 0;
        this.mouseYReleased = 0;
    

    }

    @Override
    public void mouseReleased(MouseEvent e) {
        System.out.println(mouseX);
        System.out.println(mouseY);
        this.mouseXReleased = mouseX;
        this.mouseYReleased = mouseY;
        
        if (this.startScreen){
            if (this.inBounds(buttonPosStart[0], buttonPosStart[1], buttonPosStart[2], buttonPosStart[3])){
                this.startScreen = false;
            }
        } else {
            if (!this.showLeaderboard){
                if (distance(820, 600, mouseX, mouseY) < 20 && distance(820, 600, this.mouseXPressed, this.mouseYPressed) < 20){
                    this.gameMap.setShowShop(!this.gameMap.getShowShop());
                } else if (this.gameMap.getShowShop()){
                    if (this.inBounds(345, 295, 40, 40)){
                        this.gameMap.getCurrentTank().buyParachute();
                        System.out.println("bought");
                    }
                    if (this.inBounds(345, 355, 40, 40)){
                        this.gameMap.getCurrentTank().buyFuel();
                        System.out.println("bought 2");
                    }
                    if (this.inBounds(343, 420, 44, 30) ){
                        this.gameMap.getCurrentTank().buyHealth();
                    }
                    if (!this.inBounds(255, 160, 352, 320)){
                        this.gameMap.setShowShop(!this.gameMap.getShowShop());
                        System.out.println("bought 3");
                    }
                }
            }
        }
        if (restartArmed){
            if (this.inBounds(280, 640 - (int) buttonPosY + 40, 300, 40)){
                initializeMaps();
                this.playerRanks = new ArrayList<>();
                restartArmed = false;
                showLeaderboard = false;
                this.leaderboard = null;
                
            }
            
        }
    }
    
    /**
     * Draw all elements in the game by current frame.
     */
	@Override
    public void draw() {
      
        if (startScreen){
            startScreen();
        } else{
            textFont(gameFont);
            this.noStroke();
            gameMap.drawMap(this);
            if (!this.gameMap.getGameState()){
                this.updateGameMap(false);
            }
        }
        this.leaderboard();
        
    }



    public static void main(String[] args) {
        PApplet.main("Tanks.App");
        
    }

}
