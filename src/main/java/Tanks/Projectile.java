package Tanks;

import processing.core.PApplet;

/**
 * Objects representing Projectiles
 */
public class Projectile{
    private float positionX;

    private float positionY;
    private final float startVol;
    private float wind;
    private final float angle;
    private float t;
    private final Tank orgin;
    private boolean hit;
    private final int[] color;
    private boolean out;
    private final Map map;


    private int explosionTick;
    /**
     * Initializes a projectile object
     * @param positionX x position of the projectile starting point
     * @param positionY y position of the projectile starting point
     * @param startPower initial power of projectile
     * @param angle angle the projectile is shot at
     * @param orgin tank of orgin
     * @param color color of the projectile
     * @param map map that displays the projectile
     */
    public Projectile(int positionX, float positionY, float startPower, float angle, Tank orgin, int[] color, Map map) {
        this.positionX = (float) positionX;
        this.positionY = positionY;
        this.startVol = 2 + startPower*4/25;
        this.angle = angle;
        this.explosionTick = 0;
        this.color = color;
        this.map = map;
        this.orgin = orgin;
    }
    public int getExplosionTick() {
        return this.explosionTick;
    }
    public boolean getHit() {
        return this.hit;
    }
    public boolean getOut() {
        return this.out;
    }
    public float getPositionX() {
        return this.positionX;
    }
    public float getPositionY() {
        return this.positionY;
    }
    public Tank getOrgin() {
        return this.orgin;
    }


    private void updatePosProjectile() {
        if (!(this.hit ^ this.out)){
            this.wind = this.map.getWind();

            this.positionX = this.positionX + this.startVol*(float) Math.sin(angle) +  wind * (float) 0.001 * t;
            this.positionY = this.positionY + this.startVol*(float) Math.cos(angle) - (float) 0.24 * t;
            this.t += 1;
            // System.out.println(this.startVol);
        }
    }

    /**
     * @param out
     * Updates whether the projectile is out or has hit terrain
     */
    public void hit(boolean out) {
        if (out){
            this.out = true;
        } else {
            this.hit = true;
        }
        
    }

    
    private float explosionRadius() {
        this.explosionTick += 1;
        float radius = this.explosionTick*(float) 5;
        if (this.explosionTick < 7){
            return radius;
        } else {return 0;}
        
    }

    /**
     * @param app
     * Draws the projectile and explosion based on the current position
     */
    public void drawProjectile(PApplet app) {
        updatePosProjectile();
        if (!this.hit){
            app.noStroke();
            app.fill(this.color[0], this.color[1], this.color[2]);
            app.ellipse(positionX, 640 - positionY, 9, 9);
            app.fill(0);
            app.ellipse(positionX, 640 - positionY, 2, 2);
        } else {
            float a = explosionRadius()*2;
            app.noStroke();
            app.fill(255, 29, 16);
            app.ellipse(positionX, 640 - positionY, a, a);
            app.fill(255, 138, 51);
            app.ellipse(positionX, 640 - positionY, a * (float) 0.5, a * (float) 0.5);
            app.fill(232, 234, 160);
            app.ellipse(positionX, 640 - positionY, a * (float) 0.2, a * (float) 0.2);
            
        }
    }
}
