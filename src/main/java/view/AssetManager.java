package view;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.URL;

import javax.imageio.ImageIO;

public class AssetManager {

    private static AssetManager instance;

    // ===== IMAGES =====
    private BufferedImage background;
    private BufferedImage player;
    private BufferedImage ground;
    private BufferedImage block;
    private BufferedImage robot;
    private BufferedImage propulsor;
    private BufferedImage logo;
    private BufferedImage switchBlock;
    private BufferedImage openedBridge;
    private BufferedImage closedBridge;

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
        player = load("/assets/player.png");
        ground = load("/assets/ground.png");
        block = load("/assets/block.png");
        robot = load("/assets/robot.png");
        propulsor = load("/assets/propulsor.png");
        logo = load("assets/logo.png");
        switchBlock = load("assets/switchBlock.png");
        openedBridge = load("assets/openedBridge.png");
        closedBridge = load("assets/closedBridge.png");

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
    public BufferedImage getPlayer() { return player; }
    public BufferedImage getGround() { return ground; }
    public BufferedImage getBlock() { return block; }
    public BufferedImage getRobot() { return robot; }
    public BufferedImage getPropulsor() { return propulsor; }
    public BufferedImage getLogo() { return logo; }
    public BufferedImage getSwitch() { return switchBlock; }
    public BufferedImage getOpenBridge() { return openedBridge; }
    public BufferedImage getClosedBridge() { return closedBridge; }
}