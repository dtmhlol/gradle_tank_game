package Tanks;

public class Shop extends Elements{
    private final App app;
    private final Map map;
    public Shop(App app, Map map) {
        this.app = app;
        this.map = map;
    }
    /**
     * Draws the shop interface with its elements. Unpurchasable
     * powerups will be grayed out
     */
    public void drawElement() {
        app.fill(155, 155, 155, 180);
        app.rect(255, 160, 352, 320, 20);
        app.fill(0, 0, 0);
        app.rect(432, 200 + 6, 60, 12);
        app.rectMode(CENTER);
        app.stroke(Math.abs(0), Math.abs(0), Math.abs(0));
        app.strokeWeight(8);
        
        app.fill(this.map.getCurrentTank().getColor()[0], this.map.getCurrentTank().getColor()[1], this.map.getCurrentTank().getColor()[2]);
        app.rect(431, 200 + 12, 40, 28, 8);
        app.rect(431, 200 + 42, 100, 36, 16, 16, 28, 28);
        app.noStroke();
        app.rectMode(CORNER);
        app.imageMode(CORNER);
        app.fill(250, 250, 250, 255);
        app.stroke(250, 250, 250, 255);

        app.rect(345, 295, 40, 40, 5);
        app.image(map.getParachuteImage(), 350, 300, 30, 30);
        
        if (map.getCurrentTank().getScore() < 15){
            app.stroke(55, 55, 55, 95);
            app.fill(55, 55, 55, 95);
            app.rect(345, 295, 40, 40, 5);
            app.fill(250, 250, 250, 255);
            app.stroke(250, 250, 250, 255);
        }
        app.textAlign(LEFT, TOP);
        app.textSize(15);
        app.text("Cost: 15", 420, 305);

        app.rect(345, 355, 40, 40, 5);
        app.image(map.getFuelImage(), 350, 360, 30, 30);
        if (map.getCurrentTank().getScore() < 10){
            app.stroke(55, 55, 55, 95);
            app.fill(55, 55, 55, 95);
            app.rect(345, 355, 40, 40, 5);
            app.fill(250, 250, 250, 255);
            app.stroke(250, 250, 250, 255);
        }
        app.textAlign(LEFT, TOP);
        app.textSize(15);
        app.text("Cost: 10", 420, 365);

        app.rect(340, 415, 48, 40, 5);
        app.image(map.getHealthImage(), 345, 421, 39, 29);
        if (map.getCurrentTank().getScore() < 20){
            app.stroke(55, 55, 55, 95);
            app.fill(55, 55, 55, 95);
            app.rect(340, 415, 48, 40, 5);
            app.fill(250, 250, 250, 255);
            app.stroke(250, 250, 250, 255);
        }
        app.textAlign(LEFT, TOP);
        app.textSize(15);
        app.text("Cost: 20", 420, 425);
        
        
    }


}
 