package view;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.URL;

import javax.imageio.ImageIO;

public class AssetManager {

    private static AssetManager instance;

    // ===== IMAGES =====
    private BufferedImage background;
    private BufferedImage playerUp;
    private BufferedImage playerRight;
    private BufferedImage playerDown;
    private BufferedImage playerLeft;
    private BufferedImage ground;
    private BufferedImage block;
    private BufferedImage robot;
    private BufferedImage propulsor;
    private BufferedImage switchBlock;
    private BufferedImage openedBridge;
    private BufferedImage closedBridge;
    private BufferedImage wall;
    private BufferedImage terminal; 

    private AssetManager() {
        loadAllAssets();
    }

    public static AssetManager getInstance() {
        if (instance == null) {
            instance = new AssetManager();
        }
        return instance;
    }

    private void loadAllAssets() {
        background = load("/assets/background.png");
        playerUp = load("/assets/playerUp.png");
        playerRight = load("/assets/playerRight.png");
        playerDown = load("/assets/playerDown.png");
        playerLeft = load("/assets/playerLeft.png");
        ground = load("/assets/ground.png");
        block = load("/assets/block.png");
        robot = load("/assets/robot.png");
        propulsor = load("/assets/propulsor.png");
        switchBlock = load("/assets/switchBlock.png");
        openedBridge = load("/assets/openedBridge.png");
        closedBridge = load("/assets/closedBridge.png");
        wall = load("/assets/wall.png");
        terminal = load("/assets/terminal.png");

        System.out.println(" Assets chargés !");
    }

    private BufferedImage load(String path) {
        try {
            URL url = getClass().getResource(path);
            if (url == null) {
                System.err.println(" Introuvable : " + path);
                return null;
            }
            return ImageIO.read(url);
        } catch (IOException e) {
            return null;
        }
    }

    // ===== GETTERS =====
    public BufferedImage getBackground() { return background; }
    public BufferedImage getPlayerUp() { return playerUp; }
    public BufferedImage getPlayerRight() { return playerRight; }
    public BufferedImage getPlayerDown() { return playerDown; }
    public BufferedImage getPlayerLeft() { return playerLeft; }
    public BufferedImage getGround() { return ground; }
    public BufferedImage getBlock() { return block; }
    public BufferedImage getRobot() { return robot; }
    public BufferedImage getPropulsor() { return propulsor; }
    public BufferedImage getSwitch() { return switchBlock; }
    public BufferedImage getOpenBridge() { return openedBridge; }
    public BufferedImage getClosedBridge() { return closedBridge; }
    public BufferedImage getWall(){ return wall;}
    public BufferedImage getTerminal() { return terminal; } 
}