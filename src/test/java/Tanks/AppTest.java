package Tanks;


import processing.core.PApplet;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.MethodOrderer; 
import org.junit.jupiter.api.Order; 
import org.junit.jupiter.api.TestMethodOrder; 
  
  
@TestMethodOrder(MethodOrderer.Alphanumeric.class) 
public class AppTest extends App{
    public static App app = new App();
    @BeforeAll
    public static void setupTest() {
        PApplet.runSketch(new String[]{"App"}, app);
        app.delay(500); //giving the time for the program to setup be4 testing
        app.loop(); //continuous draw()
        }
    @BeforeEach
    public void starter() {
        app.setDebugMode();
        app.setShowLeaderboard(false);
        delay(300);
    }
    @Test
    public void appSetupTest() {
        delay(100);
        assertTrue(app.getStartScreen());
        delay(50);
        app.key = 'a';
        app.keyPressed(null);
        delay(80);
        assertFalse(app.getStartScreen());
        assertTrue(app.getGameMaps().size() == 3);
        for (Map map: app.getGameMaps()){
            assertTrue(map.getPlayers().size() == 4);
            assertEquals(map.getPlayers().get(0), map.getCurrentTank());

        }
    }
    @Test
    public void tankDeath() {
        app.key = 'm';
        app.keyPressed(null);
        app.key = 'b';
        app.keyPressed(null);
        delay(1000);
        assertTrue(app.getGameMap().getPlayers().size() == 4);
        assertTrue(app.getGameMap().getTankls().size() == 3);   
        assertNotEquals(app.getGameMap().getCurrentTank(), app.getGameMap().getPlayers().get(0));  
    }
    @Test
    public void tankFallTest() {
        System.out.println("start test");
        app.key = 'm';
        app.keyPressed(null);
        float[] heightMap = app.getGameMap().getYheight();
        int x = app.getGameMap().getCurrentTank().getPositionX();
        int initialHealth = app.getGameMap().getCurrentTank().getHealth();
        for (int i = Math.max(0, x - 10); i < Math.min(863, x + 10); i++) {
            heightMap[i] -= 10;
        }
        delay(70);
        assertTrue(app.getGameMap().getCurrentTank().getHealth() == initialHealth);
        delay(70);
        app.key = 'v';
        app.keyPressed(null);
        app.key = 'l';
        app.keyPressed(null);
        delay(500);
        x = app.getGameMap().getCurrentTank().getPositionX();
        initialHealth = app.getGameMap().getCurrentTank().getHealth();
        for (int i = Math.max(0, x - 10); i < Math.min(863, x + 10); i++) {
            heightMap[i] -= 10;
        }
        delay(300);
        assertTrue(app.getGameMap().getCurrentTank().getHealth() != initialHealth);
        delay(60);
        app.key = 'm';
        app.keyPressed(null);
        delay(100);
        Tank tank = app.getGameMap().getCurrentTank();
        x = tank.getPositionX();
        heightMap = app.getGameMap().getYheight();
        initialHealth = app.getGameMap().getCurrentTank().getHealth();
        for (int i = Math.max(0, x - 10); i < Math.min(863, x + 10); i++) {
            heightMap[i] -= heightMap[i];
            System.out.println(heightMap[i]);
        }
        delay(6000);
        assertTrue(tank.getHealth() == 0);
        assertTrue(tank.getVoidDeath());

    }
    @Test
    public void testMovement() {
        app.key = 'm';
        app.keyPressed(null);
        Tank currentTank = app.getGameMap().getCurrentTank();
        int currentPosX = currentTank.getPositionX();
        app.key = CODED;
        app.keyCode = RIGHT;
        app.keyPressed(null);
        delay(50);
        app.keyCode = RIGHT;
        app.keyReleased(null);
        delay(50);
        assertEquals(currentTank.getPositionX(), currentPosX + 1);
        currentPosX = currentTank.getPositionX();
        app.keyCode = LEFT;
        app.keyPressed(null);
        delay(50);
        app.keyCode = LEFT;
        app.keyReleased(null);
        assertEquals(currentTank.getPositionX(), currentPosX - 1);

        app.key = 'm';
        app.keyPressed(null);
        currentTank = app.getGameMap().getCurrentTank();
        float currentAngle = currentTank.getAngleRad();
        app.key = CODED;
        app.keyCode = UP;
        app.keyPressed(null);
        delay(40);
        app.keyCode = UP;
        app.keyReleased(null);
        delay(40);
        assertTrue(currentTank.getAngleRad() - (currentAngle - 0.1) < 1e-4);
        app.keyCode = DOWN;
        app.keyPressed(null);
        delay(40);
        app.keyCode = DOWN;
        app.keyReleased(null);
        assertTrue(currentTank.getAngleRad() - currentAngle < 1e-4);

        float currentPower = currentTank.getPower();
        app.key = 'w';
        app.keyPressed(null);
        delay(40);
        app.key = 'w';
        app.keyReleased(null);
        delay(40);
        assertTrue(currentTank.getPower() - (currentPower + 1.25) < 1e-4);
        app.key = 's';
        app.keyPressed(null);
        delay(40);
        app.key = 's';
        app.keyReleased(null);
        delay(40);
        System.out.println(currentTank.getPower());
        System.out.println(currentPower);
        assertTrue(currentTank.getPower() - currentPower < 1e-4);
        app.key = 'm';
        app.keyPressed(null);
        delay(500);
        for (int i = 0; i<100; i++){
            app.key = CODED;
            app.keyCode = LEFT;
            app.keyPressed(null);
            delay(50);
        }
        
    }
    @Test 
    public void testLevelChange() {
        app.key = 'm';
        app.keyPressed(null);
        delay(250);
        app.key = 'b';
        app.keyPressed(null);
        delay(50);
        app.key = 'b';
        app.keyPressed(null);
        delay(50);
        app.key = 'b';
        app.keyPressed(null);
        delay(1200);
        assertEquals(app.getGameMap(), app.getGameMaps().get(1));
        delay(50);
        app.key = 'b';
        app.keyPressed(null);
        delay(50);
        app.key = 'b';
        app.keyPressed(null);
        delay(50);
        app.key = 'b';
        app.keyPressed(null);
        delay(1200);
        assertEquals(app.getGameMap(), app.getGameMaps().get(2));
    }
    @Test
    public void projectileCreationTest(){
        app.key = 'm';
        app.keyPressed(null);
        delay(600);
        app.key = 'n';
        app.keyPressed(null);
        delay(50);
        Tank currentTank = app.getGameMap().getCurrentTank();
        app.key = ' ';
        app.keyPressed(null);
        delay(40);
        assertEquals(currentTank.getProjectile().size(), 1);
        Projectile proj = currentTank.getProjectile().get(0);
        delay(1500);
        assertEquals(currentTank, currentTank.getFaller());
        assertEquals(currentTank.getScore(), 0);
        currentTank = app.getGameMap().getCurrentTank();

        for (int i = 1; i < 6; i++){
            currentTank.decreaseTurretAngle();
            delay(35);
        }
        currentTank.setPower(100);
        delay(50);
        app.key = ' ';
        app.keyPressed(null);
        proj = currentTank.getProjectile().get(0);
        delay(3000);
        assertTrue(proj.getOut());
    }
    @Test
    public void startScreenMouseTest(){
        
        app.key = 'm';
        // drag out of bounds test
        app.keyPressed(null);
        app.setStartScreen(true);
        delay(100);
        app.mouseX = 410;
        app.mouseY = 397;
        delay(50);
        app.mousePressed(null);
        delay(50);
        app.mouseX = 410;
        app.mouseY = 597;
        app.mouseReleased(null);
        delay(100);
        assertTrue(app.getStartScreen());
        //click
        app.keyPressed(null);
        app.setStartScreen(true);
        delay(100);
        app.mouseX = 410;
        app.mouseY = 397;
        delay(50);
        app.mousePressed(null);
        delay(50);
        app.mouseX = 410;
        app.mouseY = 397;
        app.mouseReleased(null);
        delay(100);
        assertFalse(app.getStartScreen());
    }
    @Test
    public void powerUpsTest(){
        app.key = 'm';
        app.keyPressed(null);
        delay(250);

        Tank currentTank = app.getGameMap().getCurrentTank();
        //Test buying parachutes, insufficient points
        app.key = 'p';
        app.keyPressed(null);
        delay(100);
        assertTrue(currentTank.getParachute() == 3);
        assertTrue(currentTank.getScore() == 0);

        //Test buying parachutes, sufficient points
        app.key = 'x';
        app.keyPressed(null);
        delay(50);
        app.key = 'p';
        app.keyPressed(null);
        delay(100);
        assertTrue(currentTank.getParachute() == 4);
        assertTrue(currentTank.getScore() == 5);

        //Test buying fuel, insufficient points
        app.key = 'f';
        app.keyPressed(null);
        delay(100);
        assertTrue(currentTank.getFuel() == 250);
        assertTrue(currentTank.getScore() == 5);

        //Test buying fuel, sufficient points
        app.key = 'x';
        app.keyPressed(null);
        delay(50);
        app.key = 'f';
        app.keyPressed(null);
        delay(100);
        assertTrue(currentTank.getFuel() == 450);  
        assertTrue(currentTank.getScore() == 15);
        
        //Test buying repair kits, insufficient points, full health
        app.key = 'r';
        app.keyPressed(null);
        delay(100);
        assertTrue(currentTank.getHealth() == 100);
        assertTrue(currentTank.getScore() == 15);

        //Test buying repair kits, sufficient points, full health
        app.key = 'x';
        app.keyPressed(null);
        delay(50);
        app.key = 'r';
        app.keyPressed(null);
        delay(100);
        assertTrue(currentTank.getHealth() == 100);  
        delay(50);
        assertTrue(currentTank.getScore() == 35);

        //Test buying repair kits, sufficient points, low health
        currentTank.setHealth(80);   
        app.key = 'r';
        app.keyPressed(null);
        delay(100);
        assertTrue(currentTank.getHealth() == 100);  
        assertTrue(currentTank.getScore() == 15);
        
            
    }
    @Test
    public void shopInterfaceTest(){
        app.key = 'm';
        app.keyPressed(null);
        delay(300);
        // Open shop interface
        app.mouseX = 823;
        app.mouseY = 595;
        app.mousePressed(null);
        delay(50);
        app.mouseReleased(null);
        delay(100);
        assertTrue(app.getGameMap().getShowShop());
        
        Tank currentTank = app.getGameMap().getCurrentTank();
        
        //Test buying parachutes, insufficient points
        app.mouseX = 375;
        app.mouseY = 311;
        app.mousePressed(null);
        delay(50);
        app.mouseReleased(null);
        delay(100);
        assertTrue(currentTank.getParachute() == 3);

        //Test buying parachutes, sufficient points
        app.key = 'x';
        app.keyPressed(null);
        delay(50);
        app.mousePressed(null);
        delay(50);
        app.mouseReleased(null);
        delay(100);
        assertTrue(currentTank.getParachute() == 4);

        //Test buying fuel, insufficient points
        app.mouseX = 375;
        app.mouseY = 373;
        app.mousePressed(null);
        delay(50);
        app.mouseReleased(null);
        delay(100);
        assertTrue(currentTank.getFuel() == 250);

        //Test buying fuel, sufficient points
        app.key = 'x';
        app.keyPressed(null);
        delay(50);
        app.mousePressed(null);
        delay(50);
        app.mouseReleased(null);
        delay(100);
        assertTrue(currentTank.getFuel() == 450);  
        
        //Test buying repair kits, insufficient points, full health
        app.mouseX = 375;
        app.mouseY = 432;
        app.mousePressed(null);
        delay(50);
        app.mouseReleased(null);
        delay(100);
        assertTrue(currentTank.getHealth() == 100);

        //Test buying repair kits, sufficient points, full health
        app.key = 'x';
        app.keyPressed(null);
        delay(50);
        app.mousePressed(null);
        delay(50);
        app.mouseReleased(null);
        delay(100);
        assertTrue(currentTank.getHealth() == 100);  
        delay(50);

        //Test buying repair kits, sufficient points, low health
        currentTank.setHealth(80);   
        delay(50);
        app.mousePressed(null);
        delay(50);
        app.mouseReleased(null);
        delay(50);
        assertTrue(currentTank.getHealth() == 100);  

        //Closing the shop

        app.mouseX = 1;
        app.mouseY = 1;
        app.mousePressed(null);
        delay(50);
        app.mouseReleased(null);
        delay(100);
        assertFalse(app.getGameMap().getShowShop());

    }
    @Test
    public void leaderboardTesting() {
        app.key = 'm';
        app.keyPressed(null);
        delay(100);
        app.key = 'c';
        app.keyPressed(null);
        delay(100);
        app.key = 'c';
        app.keyPressed(null);
        delay(100);
        // app.key = 'b';
        // app.keyPressed(null);
        // delay(50);
        // app.key = 'b';
        // app.keyPressed(null);
        // delay(50);
        app.key = 'c';
        delay(50);
        app.keyPressed(null);
        delay(5000);
        assertTrue(app.getShowLeaderboard());
        while (!app.getArmRestart()){
            delay(1000);
        }
        app.key = 'r';
        delay(2300);

        app.keyPressed(null);
        delay(2300);
        
        assertFalse(app.getShowLeaderboard());
    }  
    @Test
    public void leaderboardTestingMouse() {
        app.key = 'm';
        app.keyPressed(null);
        delay(100);
        app.key = 'c';
        app.keyPressed(null);
        delay(100);
        app.key = 'c';
        app.keyPressed(null);
        delay(100);
        app.key = 'c';
        delay(100);
        app.keyPressed(null);
        delay(5000);
        assertTrue(app.getShowLeaderboard());
        //out of bounds
        while (!app.getArmRestart()){
            delay(1000);
        }
        app.mouseX = 375;
        app.mouseY = 232;
        app.mousePressed(null);
        delay(50);

        app.keyPressed(null);
        delay(200);
        assertTrue(app.getShowLeaderboard());

        // in bounds
        app.mouseX = 431;
        app.mouseY = 367;
        app.mousePressed(null);
        delay(50);
        app.keyPressed(null);
        delay(200);
        assertTrue(app.getShowLeaderboard());
    }  
    // @Test
    // public void constantShooting(){
    //     app.key = 'm';
    //     app.keyPressed(null);
    //     delay(200);
    //     while (!app.getArmRestart()) {
    //         app.key = ' ';
    //         app.keyPressed(null);
    //         delay(100);
    //     }
    // }
}

//gradle test jacocoTestReport
