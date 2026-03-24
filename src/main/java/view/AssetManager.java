package view;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.URL;

import javax.imageio.ImageIO;

public class AssetManager {
    public static BufferedImage BACKGROUND;
    public static BufferedImage LOGO; // Sera null car pas encore d'image
    public static BufferedImage PLAYER;
    public static BufferedImage GROUND;
    public static BufferedImage SWITCH;
    public static BufferedImage CLOSED_BRIDGE;
    public static BufferedImage OPENED_BRIDGE;

    public static void loadAssets() {
        BACKGROUND = loadImage("/assets/background.png");
        PLAYER = loadImage("/assets/player.png");
        GROUND = loadImage("/assets/ground_block.png");
        SWITCH = loadImage("/assets/block_switch.png");
        CLOSED_BRIDGE = loadImage("/assets/closed_bridge.png");
        OPENED_BRIDGE = loadImage("/assets/opened_bridge.png");
    }

    private static BufferedImage loadImage(String path) {
        try {
            URL url = AssetManager.class.getResource(path);
            if (url == null) {
                System.out.println("Info: Image " + path + " non trouvée, utilisation du mode texte.");
                return null;
            }
            return ImageIO.read(url);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }



    // public void render(Graphics2D g, GameModel model) {
    //     //Sol
    //     for (Ground ground : model.getGrounds()) {
    //         if (GROUND != null)
    //             g.drawImage(GROUND, ground.getI(), ground.getJ(), null);
    //         else
    //             g.fillRect(ground.getI(), ground.getJ(), 32, 16);
    //     }

    //     //Joueur
    //     Robot p = model.getPlayer();
    //     if (PLAYER != null)
    //         g.drawImage(PLAYER, p.getI(), p.getJ(), null);

    //     //Pont : la VIEW choisit l'image selon l'état logique du MODEL
    //     for (Bridge bridge : model.getBridges()) {
    //         BufferedImage img = bridge.getIsActive() ? OPENED_BRIDGE : CLOSED_BRIDGE;
    //         if (img != null)
    //             g.drawImage(img, bridge.getI(), bridge.getJ(), null);
    //     }

    //     //Switch
    //     for (Switch sw : model.getSwitches()) {
    //         if (SWITCH != null)
    //             g.drawImage(SWITCH, sw.getI(), sw.getJ(), null);
    //     }
    // }
}
