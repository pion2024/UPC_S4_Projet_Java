package view;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.URL;

import javax.imageio.ImageIO;

public class AssetManager {
    public static BufferedImage BACKGROUND;
    public static BufferedImage LOGO; // Sera null car pas encore d'image

    public static void loadAssets() {
        BACKGROUND = loadImage("/assets/background.png");
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
}
