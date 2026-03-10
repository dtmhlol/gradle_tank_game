package Tanks;
import processing.core.PApplet;
import processing.core.PImage;
import processing.data.JSONObject;

import java.io.*;
import java.util.*;


public class Map extends App{
    private final App app;
    private float[] yheight = new float[896];
    private String[][] mapImport = new String[20][28];
    private int[] MapColor;
    private PImage backgroundImg;
    private JSONObject json;
    private ArrayList<Tank> tankls = new ArrayList<Tank>();
    private ArrayList<Tank> players = new ArrayList<Tank>();
    private Tank currentTank;
    private boolean gameState = true;
    private int gameStateClock;
    private int wind;
    private ArrayList<Trees> treels = new ArrayList<Trees>();
    private PImage tree;

    private final Shop shop;
    
    private final PImage parachuteImage;
    private final PImage fuelImage;
    private final PImage healthImage;
    private final PImage shopImage;
    private final PImage skullImage;
    private final PImage wind1;
    private final PImage wind2;
    private boolean showShop;

    public Map(String fileDir, PImage backgroundImg, int[] MapColor, App app, JSONObject json, int level, String treeDir) {

        import_map(fileDir);
        this.backgroundImg = backgroundImg;
        this.MapColor = MapColor;
        this.app = app;
        this.json = json;
        this.createHeightMap();
        this.initiallizeTanksAndTrees();
        this.initializeWind();
        if (treeDir != null){
            tree = app.loadImage(treeDir);
        }

        this.fuelImage = app.loadImage("src/main/resources/Tanks/fuel.png");
        this.parachuteImage = app.loadImage("src/main/resources/Tanks/parachute.png");
        this.healthImage = app.loadImage("src/main/resources/Tanks/health.png");
        this.shopImage = app.loadImage("src/main/resources/Tanks/Shop.png");
        this.skullImage = app.loadImage("src/main/resources/Tanks/skull.png");
        this.wind1 = app.loadImage("src/main/resources/Tanks/wind.png");
        this.wind2 = app.loadImage("src/main/resources/Tanks/wind-1.png");

        this.shop = new Shop(app, this);
        // for(Tank i:tankls){System.out.println(i.getName() + i.positionX);}
    }
    public PImage getBackgroundImg() {
        return this.backgroundImg;
    }
    public PImage getParachuteImage() {
        return this.parachuteImage;
    }
    public ArrayList<Tank> getPlayers() {
        return this.players;
    }
    public int getGameStateClock() {
        return this.gameStateClock;
    }
    public void incrementGameStateClock() {
        this.gameStateClock++;
    }
    public boolean getGameState() {
        return this.gameState;
    }
    public Tank getCurrentTank() {
        return this.currentTank;
    }
    public void setShowShop(boolean showShop) {
        this.showShop = showShop;
    }
    public boolean getShowShop() {
        return this.showShop;
    }
    public float[] getYheight() {
        return this.yheight;
    }
    public ArrayList<Tank> getTankls() {
        return this.tankls;
    }
    public int getWind() {
        return this.wind;
    }
    /**
     * Sets wind to 0
     */
    public void resetWind() {
        this.wind = 0;
    }
    public PImage getFuelImage() {
        return this.fuelImage;
    }
    public PImage getHealthImage() {
        return this.healthImage;
    }
 
    private void import_map(String fileDir) {
        File myObj;
        String[][] temp_map = new String[20][28];
        try {
            myObj= new File(fileDir);
            
            Scanner reader = new Scanner(myObj);
            int i = 0;
            while (reader.hasNextLine()&& i < 20){
                
                String data = reader.nextLine();
                String[] dataList = data.split("");
                int k = 0;
                for (String j : dataList) {
                    temp_map[i][k] = j;
                    k++; 
                }
                i++;
        }           
            reader.close();
        } catch (FileNotFoundException e){
            System.out.println("An error occurred.");
            e.printStackTrace();
        }
        this.mapImport = temp_map;
    }

    private String tankColor(String name) {
        JSONObject players = json.getJSONObject("player_colours");
        return players.getString(name);
        
    }

    private void initiallizeTanksAndTrees() {
        Tank newTank;
        Random rand = new Random();
        for (int x = 0; x < 28; x++) {
            for (int y = 0; y < 20; y++) {
                String current_value = this.mapImport[y][x];  
                             
                if (current_value != null) {                    
                    if (!"X".equals(current_value) && !"T".equals(current_value) && !" ".equals(current_value) && current_value.matches("[A-Za-z0-9]+")) {
                        newTank = new Tank(x * 32, current_value, this, tankColor(current_value));
                        this.tankls.add(newTank);
                        this.players.add(newTank);
                    } 
                    if ("T".equals(current_value)) {
                        this.treels.add(new Trees(x * 32 + rand.nextInt(31), this));
                        // System.out.println("print");
                    }   
                    
                }
            }    
                        
        }
        this.tankls.sort((o1, o2) -> o1.getName().compareTo(o2.getName()));
        this.players.sort((o1, o2) -> o1.getName().compareTo(o2.getName()));
        this.currentTank = this.tankls.get(0);
    }
    /**
     * Initializes the wind variable
     */
    public void initializeWind() {
        Random rand = new Random(); 
        if(rand.nextInt(2)== 0){
            this.wind -= rand.nextInt(36);
        } else{
            this.wind +=rand.nextInt(36);
        }
        
    }
    /** 
     *Updates the wind variable
     */
    public void updateWind() {
        Random rand = new Random(); 
        if(rand.nextInt(2) == 0){
            this.wind -= rand.nextInt(5);
        } else{
            this.wind += rand.nextInt(5);
        }
    }

    private static float[] calculateMovingAverage(float[] data) {
        float[] movingAverage = new float[data.length];
        float sum = 0;
        
        for (int a = 0; a < 32; a++) {
            sum += data[a];
        }
        
        for (int i = 0; i < data.length; i++) {
            
          // Calculate the average for the current window
          movingAverage[i] = (float) sum / 32;
          // if applicable, add next element
          if (i + 32 < 896) {
            sum += data[i + 32];
            sum -= data[i];
          }

        }
    
        return movingAverage;
    }
 
    private void createHeightMap() {
        Arrays.fill(this.yheight, 0);
        int height = 0;
        for (String[] i: mapImport){
            int index = 0;
            for (String j : i){
                if (index == 29){
                    break;
                }
                if (j!= null){
                    String currentValue;
                    currentValue = j;
                    if ("X".equals(currentValue)){
                        for (int k = index * 32; k < (index + 1) * 32; k++) {
                            this.yheight[k] = 640 - height * 32;
                        }
                    }
                }
                index++;                
            }
            height++;
        }
        this.yheight = calculateMovingAverage(yheight).clone();
        
        this.yheight = calculateMovingAverage(yheight).clone();   
    }

    private void removeProj(Tank tank) {
        ArrayList<Projectile> toBeDeleted = new ArrayList<Projectile>();
        for (Projectile proj: tank.getProjectile()){
            if ((proj.getHit() && proj.getExplosionTick() > 9) || proj.getOut()){
                toBeDeleted.add(proj);
            }
        }
        for (Projectile ripInChat: toBeDeleted){
            tank.getProjectile().remove(ripInChat);
        }
    }
    /**
     * Calculates and adjust the health of active players based on distance to the projectile
     * @param i : projectile
     */
    public void hitDamage(Projectile i) {
        Tank ogTank = i.getOrgin();

        float dmg;
        for (Tank tank: this.tankls){
            float posX = i.getPositionX();
            float posY = i.getPositionY();
            float tPosX = tank.getPositionX();
            float tPosY = tank.getPositionY();
            dmg = 0;
            float dist = (float) Math.sqrt((posX - tPosX) * (posX - tPosX) + (posY - tPosY) * (posY - tPosY));
            if (dist < 30){
                dmg = Math.min(tank.getHealth(), (30 - (int) dist) * 2);
                tank.setHealth(Math.max(0, tank.getHealth() - (30 - (int) dist) * 2));
                if (tank != ogTank){
                    ogTank.addScore((int) dmg);
                    
                }
            }
        }

    }
    /**
     * Updates the current player
     */
    public void updateCurrentTank() {
        this.arrowTicker = 0;  
        int current_index = this.tankls.indexOf(currentTank);
        
        if (current_index == this.tankls.size() - 1){
            this.currentTank = this.tankls.get(0);
        } else {
            this.currentTank = this.tankls.get(current_index + 1); 
        }
        if (this.currentTank.getHealth() == 0){
            if (current_index == this.tankls.size() - 1){
                this.currentTank = this.tankls.get(0);
            } else {
                this.currentTank = this.tankls.get(current_index + 1); 
            }
        }
    }

    private void treeCheck() {
        for (Trees tree: this.treels){
            if (tree.getPositionY() > this.yheight[tree.getPositionX()]){
                tree.setPositionY(Math.max(tree.getPositionY() - 4, this.yheight[tree.getPositionX()]));
            }
        }
    }

    private void playerCheck() {
        ArrayList<Tank> toRemove = new ArrayList<Tank>();
        int pointDelta;
        for (Tank i: tankls){
            i.setPower(Math.min(i.getHealth(), i.getPower()));
            if (i.getDeathTick() == 0) {
                if (i.getPositionY() > this.yheight[i.getPositionX()]){
                    if (!i.getFalling()){
                        i.setFalling(true);
                        if (i.getParachute() > 0){
                            i.setParachute(i.getParachute() - 1);
                            i.setUsingParachute(true);
                            i.setPositionY(Math.max(i.getPositionY() - 2, this.yheight[i.getPositionX()]));
                        } else {
                            i.addFallingDmg((int) Math.min(4, i.getPositionY() - this.yheight[i.getPositionX()]));
                            i.setPositionY(Math.max(i.getPositionY() - 4, this.yheight[i.getPositionX()]));
                        }
                    } else {
                        if (i.getUsingParachute()){
                            i.setPositionY(Math.max(i.getPositionY() - 2, this.yheight[i.getPositionX()]));
                        } else {
                            i.addFallingDmg((int) Math.min(4, i.getPositionY() - this.yheight[i.getPositionX()]));
                            i.setPositionY(Math.max(i.getPositionY() - 4, this.yheight[i.getPositionX()]));

                        }
                        
                    }  
                }  else if (i.getFalling()) {
                    i.setFalling(false);
                    pointDelta = Math.min(i.getHealth(), i.getFallingDmg());
                    i.setHealth(Math.max(0, i.getHealth() - i.getFallingDmg()));
                    i.setUsingParachute(false);
                    if (i.getFaller() != i && i.getFaller() != null){
                        i.getFaller().addScore(pointDelta);
                    }
                }
                
            }
            if (i.getPositionY() <= 0){
                if (i.getFaller() != i && i.getFaller() != null){
                    i.getFaller().addScore(i.getHealth());
                }
                i.setHealth(0);
                i.setVoidDeath(true);
                i.setFalling(false);

            }
            if (i.getHealth() == 0){
                toRemove.add(i.playerDeath());
                if (i == this.currentTank){
                    this.updateCurrentTank();
                }
            }
            
        }
        for (Tank j: toRemove){
            if (j == this.currentTank && this.gameState){
                if (this.tankls.indexOf(currentTank) == this.tankls.size() - 1 && this.tankls.size() != 1){
                    this.currentTank = this.tankls.get(0);
                } else {
                    if (this.tankls.size() < 2) {
                        this.updateCurrentTank();
                    }; 
                }
                this.tankls.remove(j);
            
            } else {this.tankls.remove(j);}
        }
            
    }

    private void shop(PApplet app) {
        if (!this.showShop){
            return;
        }
        
        this.shop.drawElement();
        
        
    }
    /**
     * Resets the game map object to blank
     */
    
    public void gameMapClear() {
        
        this.yheight = new float[896];
        this.mapImport = new String[20][28];
        this.MapColor = null;
        this.backgroundImg = null;
        this.json = null;
        this.tankls = new ArrayList<Tank>();
        this.players = new ArrayList<Tank>();
        this.currentTank = null;
        this.gameState = true;
        this.wind = 0;
        this.treels = new ArrayList<Trees>();
    }

    private void checkGameState() {
        if (this.tankls.size()==1){
            this.gameState = false;
        }
    }

    private void tasks() {
        this.playerCheck(); //Check tanks falling status
        this.treeCheck(); //Check Tree status (falling)
        this.checkGameState(); // Check game State
    }

    private void drawTerrain(PApplet app) {
        app.imageMode(CORNER);
        app.background(this.backgroundImg); //Sets Background
        //Draws the terrain
        for (int y = 0; y < this.yheight.length; y++) {
            app.fill(this.MapColor[0], this.MapColor[1], this.MapColor[2]);
            app.rect(y, 640 - this.yheight[y], 1, this.yheight[y]);       
        }
        //Drawing Trees
        for (Trees tree: treels){
            app.image(this.tree, tree.getPositionX() - 18, 640 - tree.getPositionY() - 32, 36, 36);
        }
    }

    private void drawHUD(App app) {
        //Drawing HUD Elements
        if (!app.getShowLeaderboard()){
            String playerTurn = "Player "+ this.currentTank.getName() +"'s turn"; //Turn indicator
            app.textSize(15);
            app.fill(0, 0, 0);
            app.textAlign(LEFT, TOP);
            app.text(playerTurn, 22, 12);

            app.image(fuelImage, 145, 8, 22, 22); //Fuel
            app.textAlign(RIGHT, TOP);
            app.text(this.currentTank.getFuel(), 197, 12);

            app.image(parachuteImage, 145, 33, 22, 22);// Parachute

            app.textAlign(RIGHT, TOP);
            app.text(this.currentTank.getParachute(), 197, 35);

            
            app.fill(255, 255, 255); //health bar
            app.rect(440, 8, 150, 22);
            app.fill(this.currentTank.getColor()[0], this.currentTank.getColor()[1], this.currentTank.getColor()[2]);
            app.strokeWeight(4);
            app.stroke(55, 55, 55);
            app.rect(440, 8, (int) 150 * this.currentTank.getHealth() / 100, 22);

            // leaderboard
            if (!this.app.getShowLeaderboard()){
                int tankIndex = 0;
                app.stroke(0, 0, 0);
                app.fill(55, 55, 55, 55);
                app.rect(720, 70, 134, 20 + this.players.size() * 20, 10);
                for (Tank lmao: this.players){
                    app.textAlign(LEFT, TOP);
                    app.fill(lmao.getColor()[0], lmao.getColor()[1], lmao.getColor()[2]);
                    app.textSize(15);
                    app.text("Player " + lmao.getName(), 730, 80 + tankIndex * 20);
                    if (lmao.getHealth() == 0){
                        app.image(skullImage, 800, 80 + tankIndex * 20 + 2, 15, 15);
                    }
                    app.text(lmao.getScore(), 820, 80 + tankIndex * 20);
                    tankIndex++;
                }
            }
            

            // power and health elements
            app.fill(0, 0, 0); 
            app.textAlign(LEFT, TOP);
            app.text("Health:", 370, 12);
            app.text("Power:    " + (int) this.currentTank.getPower(), 370, 32);
            app.fill(255, 0, 0);
            app.stroke(0, 0, 0);
            app.strokeWeight(1);
            app.rect(440 + (int) (this.currentTank.getPower() * 1.5), 6, 2, 28);
            app.fill(0);
            app.text(currentTank.getHealth(), 600, 12);
            // wind 
            if (this.wind >= 0){
                app.image(wind1, 775, 4, 38, 38);
            } else {
                app.image(wind2, 775, 4, 38, 38);
            }
            app.fill(0, 0, 0);
            app.text(Math.abs(this.wind), 820, 12);
            //shop
            app.imageMode(CENTER);
        
            app.image(shopImage, 820, 600, 30, 30);
            this.shop(app);
        }
        
    }
    /**
     * Draw the tanks
     * @param app : app
     */
    public void drawPlayerElements(PApplet app) {
        //drawing tanks
        for (Tank current_tank: players){
            current_tank.drawTank(app);
        } 
        //draw projectiles
        for (Tank curreTank: players){
            curreTank.projectileAnalysis(app);
            removeProj(curreTank);
            
        }
    }
    private int arrowTicker;


    private void drawArrow(PApplet app) {
        app.fill(this.currentTank.getColor()[0], this.currentTank.getColor()[1], this.currentTank.getColor()[2]);
        if (this.arrowTicker < 60){
            float x = this.currentTank.getPositionX();
            float y = 640 - this.currentTank.getPositionY();
            float jiggle = 5* (float) Math.sin((float) this.arrowTicker * (float) Math.PI * (float) 0.08);
            app.triangle(x, y - 50 + jiggle, x - (float) 7.25, y - (float) 62.5 + jiggle, x + (float) 7.25, y - (float) 62.5 + jiggle);
        }
        arrowTicker++;

    }
    /**
     * Draws the map
     * @param app : app
     */
    public void drawMap(App app) {    
        tasks();
        drawTerrain(app);        
        drawPlayerElements(app);
        drawHUD(app);
        drawArrow(app);
    }    
}   
    