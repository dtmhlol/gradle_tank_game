package Tanks;


/**
 * Objects representing trees
 */
public class Trees{
    private final int positionX;
    private float positionY;
    private final Map currentMap;

    public Trees(int positionX, Map Map) {
        this.positionX = positionX;
        this.currentMap = Map;
        this.positionY = this.currentMap.getYheight()[positionX];
        // System.out.println("Tree created");
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
}
