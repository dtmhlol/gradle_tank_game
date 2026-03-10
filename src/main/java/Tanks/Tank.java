package Tanks;

import java.util.*;

import processing.core.PApplet;


public class Tank extends PApplet{
    private final int powerMax = 100;
    private int score;

    private int health;
    private float power = 50;
    private final String name;
    private int positionX;
    private float positionY;
    private int[] color = new int[3];
    private int fuel;
    private final Map currentMap;
    private float angleRad = 0;
    private int parachute = 3;
    private boolean usingParachute;
    private boolean colorRandom;

    private boolean falling;
    private int fallingDmg;
    private Tank faller;


    private ArrayList<Projectile> projectile = new ArrayList<Projectile>();

    private int deathTick;
    private boolean voidDeath;
    /**
     * Initializes a Tank object
     * @param positionX X position of the tank
     * @param Name name of the tank
     * @param currentMap map that the tank is in
     * @param color color of tank  
     */
    public Tank(int positionX, String Name, Map currentMap, String color) {
        Random rand = new Random();
        this.positionX = positionX;
        this.currentMap = currentMap;
        this.positionY = this.currentMap.getYheight()[positionX];        
        this.health = 100;
        this.fuel = 250;
        this.name = Name;
        if ("random".equals(color)){
            this.colorRandom = true;
            int c1 = rand.nextInt(255);
            int c2 = rand.nextInt(255);
            int c3 = rand.nextInt(255);

            this.color = new int[]{c1, c2, c3};
        } else {
            String[] color_str = color.split(",");
            for (int i = 0; i < color_str.length; i++) {
                this.color[i] = Integer.parseInt(color_str[i]);
            }
        }
    }
    public int getScore() {
        return this.score;
    }
    public int getParachute() {
        return parachute;
    }
    public int[] getColor() {
        return color;
    }
    public void setParachute(int parachute) {
        this.parachute = parachute;
    }
    public boolean getColorRandom() {
        return this.colorRandom;
    }
    public void setColor(int[] color) {
        this.color = color;
    }
    public boolean getFalling() {
        return this.falling;
    }
    public void setFalling(boolean falling) {
        this.falling = falling;
    }
    public String getName() {
        return this.name;
    }
    public ArrayList<Projectile> getProjectile() {
        return this.projectile;
    }
    public int getHealth() {
        return this.health;
    }
    public void setHealth(int health) {
        this.health = health;
    }
    public float getPower() {
        return this.power;
    }
    public void setPower(float power) {
        this.power = power;
    }
    public int getPositionX() {
        return this.positionX;
    }
    public float getPositionY() {
        return this.positionY;
    }
    public void setPositionY(float positionY) {
        this.positionY = positionY;
    }
    public int getFallingDmg() {
        return fallingDmg;
    }
    public void addFallingDmg(int fallingDmg) {
        this.fallingDmg += fallingDmg;
    }
    public boolean getUsingParachute() {
        return usingParachute;
    }
    public void setUsingParachute(boolean usingParachute) {
        this.usingParachute = usingParachute;
    }
    public int getDeathTick() {
        return this.deathTick;
    }
    public Tank getFaller() {
        return this.faller;
    }
    public void setFaller(Tank faller) {
        this.faller = faller;
    }
    public int getFuel() {
        return this.fuel;
    }
    public void setVoidDeath(boolean voidDeath) {
        this.voidDeath = voidDeath;
    }
    public void setScore(int score) {
        this.score = score;
    }
    public float getAngleRad() {
        return this.angleRad;
    }
    public boolean getVoidDeath() {
        return voidDeath;
    }

    /**
     * Moves the tank left by 1 pixel
     */
    public void moveLeft() {
        if (this.positionX > 1 && this.fuel > 0){
           
            this.positionX -= 1;
            this.positionY = this.currentMap.getYheight()[positionX];
            this.fuel -= 1;
        }
    }

    /**
     * Moves the tank right by 1 pixel
     */
    public void moveRight() {
        if (this.positionX < 864 && this.fuel > 0){
            this.positionX += 1;
            this.positionY = this.currentMap.getYheight()[positionX];
            this.fuel -= 1;
        }
    }

    /**
     * Turns the turret clockwise by 0.1 rad
     */
    public void increaseTurretAngle() {
        this.angleRad = (float) Math.min(angleRad + 0.1, ((float) Math.PI) / 2);
    }

    /**
     * Turns the turret counterclockwise by 0.1 rad
     */
    public void decreaseTurretAngle() {
        this.angleRad = (float) Math.max(angleRad - 0.1, -(float) Math.PI / 2);
    }

    /**
     * Increases the power by 1.25 units
     */
    public void increasePower() {
        if (this.power < powerMax && this.power < this.health){
            this.power += 1.25;
        }
    }
    /**
     * Decreases the power by 1.25 units
     */
    public void decreasePower() {
        if (this.power > 0){
            this.power -= 1.25;
        }
    }

    /**
     * Increases the parachute count by 1 and decreases score by 15
     */
    public void buyParachute() {
        if (score >= 15){
            this.parachute += 1;
            this.score -= 15;
        }        
    }

    /**
     * Increases health by 20 or to max health and decreases score by 20
     */
    public void buyHealth() {
        if (score >= 20 && this.health < 100){
            this.health = Math.min(100, this.health + 20);
            this.score -= 20;
        }
    }

    /**
     * Increases fuel by 200 and decreases score by 10
     */
    public void buyFuel() {
        if (score >= 10){
            this.fuel += 200;
            this.score -= 10;
        }
    }

    /**
     * Gets the turret end relative to the tank
     *  @return turret end coordinates
     */
    public float[] getEndPointTurret() {
        float x = (float) Math.sin(this.angleRad);
        float y = (float) Math.cos(this.angleRad);
        // System.out.println(x);
        // System.out.println(y);
        return new float[]{x * 15, y * 15};
    }

    /**
     * Creates a projectile object at the turret base
     */
    public void fireProjectile() {
        Projectile a = new Projectile(this.positionX, this.positionY + 12, power, this.angleRad, this, this.color, currentMap);
        this.projectile.add(a);
    }

    /**
     * Increments the score
     * @param score: score
     */
    public void addScore(int score) {
        this.score += score;

    }
    private void updateHitter(Projectile proj) {
        int posX = (int) proj.getPositionX();
        for (Tank currentTank: currentMap.getTankls()){
            if ((int) currentTank.positionX < posX + 30 && (int) currentTank.positionX > posX - 30){
                currentTank.faller = proj.getOrgin();
            }
        }

    }

    /**
     * Checks if the projectile is out or has hit and updates the projectile accordingly. Also draw the projectile.
     * @param app: app
     */
    public void projectileAnalysis(PApplet app) {
        for (Projectile i: this.projectile){
            if (this.projectile != null){
                try{
                    if ( (int) i.getPositionY() < -2 ){
                        i.hit(true);
                    } else if ( (int) i.getPositionY() < this.currentMap.getYheight()[(int) i.getPositionX()]){
                        i.hit(false);
                        this.terrainEdit(i);
                        this.currentMap.hitDamage(i);
                        this.updateHitter(i);
                        
                    } 
                    i.drawProjectile(app);
                } catch (ArrayIndexOutOfBoundsException e) {
                    i.hit(true);
                    
                }
            } 
        }
    
        
    }

    private void terrainEdit(Projectile proj) {
        if (proj.getHit()){
            for (int i = - 29; i < 30; i++) {
                int projectileX = (int) proj.getPositionX();
                if (projectileX + i >= 0 && projectileX + i < this.currentMap.getYheight().length) {
                    float yMax = proj.getPositionY() + (float) Math.sqrt(900 - i * i);
                    float yMin = proj.getPositionY() - (float) Math.sqrt(900 - i * i);
                    float currentHeight = this.currentMap.getYheight()[projectileX + i];
                    if (currentHeight >= yMin && currentHeight <= yMax) {
                        this.currentMap.getYheight()[projectileX + i] = yMin;
                    } else if (yMax < currentHeight) {
                        this.currentMap.getYheight()[projectileX + i] = currentHeight - yMax + yMin;
                    }
                }
            }
        }
    }

    private void terrainEditDeath() {
        int radius;
        if (voidDeath){
            radius = 15;
        } else {radius = 30;}
        for (int i = -radius + 1; i < radius; i++) {
            int tankX = (int) this.getPositionX();
            if (tankX + i >= 0 && tankX + i < this.currentMap.getYheight().length) {
                float yMax = this.positionY + (float) Math.sqrt(radius * radius - i * i);
                float yMin = this.positionY - (float) Math.sqrt(radius * radius - i * i);
                float currentHeight = this.currentMap.getYheight()[tankX + i];
                if (currentHeight >= yMin && currentHeight <= yMax) {
                    this.currentMap.getYheight()[tankX + i] = yMin;
                } else if (yMax < currentHeight) {
                    this.currentMap.getYheight()[tankX + i] = currentHeight - yMax + yMin;
                }

            }
        }
    }

    private float explosionRadius() {
        float radius = 0;
        if (this.deathTick > 6){
            return radius;
        }
        if (this.voidDeath){
            radius = this.deathTick*(float) 2.5;
             
        } else {
            radius = this.deathTick*(float) 5;

        }
        return radius;
        
    }
    /**
     * Updates the player death status and returns the player that has died
     * @return this
     */
    public Tank playerDeath() { 
        if (this.deathTick == 0 ){
            this.deathTick = 1;
            this.terrainEditDeath();
            this.hitDamage();
            
        }
          
        // System.out.println(this.deathTick);
        if (this.deathTick == 1){
            return this;
        } else{return null;} 
        
    }
    private void hitDamage() {
        for (Tank tank: this.currentMap.getTankls()){
            float posX = this.positionX;
            float posY = this.positionY;
            float tPosX = tank.getPositionX();
            float tPosY = tank.getPositionY();
            float dist = (float) Math.sqrt((posX - tPosX) * (posX - tPosX) + (posY - tPosY) * (posY - tPosY));
            if (!this.voidDeath) {
                if (dist < 30){
                    tank.setHealth(Math.max(0, tank.getHealth() - (30 - (int) dist) * 2));
                }
            } else {
                if (dist < 15){
                    tank.setHealth(Math.max(0, tank.getHealth() - (15 - (int) dist) * 2));
                }
            }
        }
    }
    private void playerDeathDraw(PApplet app) {
        
        this.deathTick++;
        float a = this.explosionRadius()*2;
        // System.out.println(a);
        app.noStroke();
        app.fill(255, 29, 16);
        app.ellipse(positionX, 640 - positionY, a, a);
        app.fill(255, 138, 51);
        app.ellipse(positionX, 640 - positionY, a * (float) 0.5, a * (float) 0.5);
        app.fill(232, 234, 160);
        app.ellipse(positionX, 640 - positionY, a * (float) 0.2, a * (float) 0.2);
    }
    /**
     * Clears all projectiles
     */
    public void clearProjectiles() {
        this.projectile = new ArrayList<>();
    }
    
    /**
     * Draws the tank to the sceeen, including the explosion
     * @param app : app
     */
    public void drawTank(PApplet app) {
        float[] turretEnd = this.getEndPointTurret();  
        if (this.deathTick == 0){
            if (this.usingParachute){
                app.imageMode(CENTER);
                app.image(currentMap.getParachuteImage(), this.getPositionX(), 640 - this.positionY - 50, 55, 55);
                app.imageMode(CORNER);
            }
            app.stroke(Math.abs(0), Math.abs(0), Math.abs(0));
            app.strokeWeight(5);
            app.line(positionX, 640 - positionY - 12, positionX + turretEnd[0], 640 - positionY - 12 - turretEnd[1]);
            
            app.fill(color[0], color[1], color[2]);
            
            app.rectMode(CENTER);
            app.stroke(Math.abs(0), Math.abs(0), Math.abs(0));
            app.strokeWeight(2);
            
            app.rect(positionX, 640 - positionY - 12, 10, 7, 2);
            app.rect(positionX, 640 - positionY - 5, 25, 9, 4, 4, 7, 7);
            app.noStroke();
            app.rectMode(CORNER);
            app.fill(33, 33, 33);
            app.rect(positionX - 15, 640 - positionY + 5, 30, 3);
            app.fill(color[0], color[1], color[2]);
            app.rect(positionX - 15, 640 - positionY + 5, this.health * 3 / 10, 3);
        } else {
            this.playerDeathDraw(app);
        }        
    }
}
