package com.starstuff.view;

import com.starstuff.model.Agent;
import com.starstuff.model.Block;
import com.starstuff.model.Bridge;
import com.starstuff.model.Entity;
import com.starstuff.model.GameWorld;
import com.starstuff.model.Trigger;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.PerspectiveCamera;
import javafx.scene.Scene;
import javafx.scene.paint.Color;
import javafx.scene.paint.PhongMaterial;
import javafx.scene.shape.Box;
import javafx.scene.shape.DrawMode;
import javafx.scene.transform.Rotate;
import javafx.scene.transform.Translate;

import java.util.HashMap;
import java.util.Map;

/**
 * Responsible for constructing the 3D scene and updating visuals.
 */
public class GameView {
    private final Group root = new Group();
    private final Group worldGroup = new Group();
    private final Scene scene;
    private final Map<Integer, Node> entityVisuals = new HashMap<>();
    private static final double TILE_SIZE = 50.0;

    public GameView(GameWorld world) {
        root.getChildren().add(worldGroup);
        
        // --- Camera Setup ---
        PerspectiveCamera camera = new PerspectiveCamera(true);
        camera.setNearClip(0.1);
        camera.setFarClip(10000.0);
        
        // 1. Calculate World Center (in Pixels)
        // We include +1 to max coord to account for the full tile width
        double minX = world.getMinX() * TILE_SIZE;
        double maxX = (world.getMaxX() + 1) * TILE_SIZE; 
        double minZ = world.getMinZ() * TILE_SIZE;
        double maxZ = (world.getMaxZ() + 1) * TILE_SIZE;

        double centerX = (minX + maxX) / 2.0;
        double centerZ = (minZ + maxZ) / 2.0;
        
        // Determine the "size" of the map to calculate how far back we need to be
        double worldWidth = maxX - minX;
        double worldDepth = maxZ - minZ;
        double maxDim = Math.max(worldWidth, worldDepth);

        // 2. Camera Positioning Strategy: "Pivot -> Rotate -> Zoom"
        // This ensures the object is ALWAYS perfectly centered.
        
        // Step A: Pivot - Move camera origin to the exact center of the map
        Translate pivot = new Translate(centerX, 0, centerZ);
        
        // Step B: Rotate - Tilt the camera mechanism
        // -55 degrees gives a better "top-down" view similar to the reference image
        // than the previous -45.
        Rotate rx = new Rotate(-55, Rotate.X_AXIS); 
        
        // Step C: Zoom - Move camera BACKWARDS along its own viewing axis
        // The factor 2.2 is heuristic: larger number = further away
        double zoomDistance = maxDim * 2.2;
        Translate zoom = new Translate(0, 0, -zoomDistance);

        // Apply transforms in order: 
        // 1. It virtually moves to center.
        // 2. It rotates.
        // 3. It pulls back.
        // (Note: In JavaFX getTransforms(), the logic applies in reverse order of matrix multiplication, 
        // but visually it acts as: Translate Pivot -> Rotate -> Translate Back)
        camera.getTransforms().addAll(pivot, rx, zoom);

        this.scene = new Scene(root, 1024, 768, true);
        this.scene.setCamera(camera);
        this.scene.setFill(Color.LIGHTBLUE); // Sky color
        
        buildTerrain(world);
    }

    private void buildTerrain(GameWorld world) {
        PhongMaterial floorMat = new PhongMaterial(Color.LIGHTGRAY); // Or Color.web("#808080")
        
        for (String key : world.getFloorPositions()) {
            String[] parts = key.split(",");
            int x = Integer.parseInt(parts[0]);
            int z = Integer.parseInt(parts[1]);

            Box floorTile = new Box(TILE_SIZE, 10, TILE_SIZE);
            floorTile.setMaterial(floorMat);
            
            // Coordinates
            floorTile.setTranslateX(x * TILE_SIZE);
            floorTile.setTranslateZ(z * TILE_SIZE);
            floorTile.setTranslateY(TILE_SIZE / 2); // Floor thickness adjustment
            
            worldGroup.getChildren().add(floorTile);
        }
    }

    public void render(GameWorld world) {
        // Update Entities
        for (Entity entity : world.getEntities()) {
            if (!entityVisuals.containsKey(entity.getId())) {
                createVisual(entity);
            }
            updateVisual(entity, entityVisuals.get(entity.getId()));
        }

        // Handle Held Block (floating)
        Agent player = world.getPlayer();
        if (player.isCarrying()) {
            Block b = player.getHeldBlock();
            Node bNode = entityVisuals.get(b.getId());
            if (bNode != null) {
                bNode.setTranslateX(player.getPosition().x * TILE_SIZE);
                bNode.setTranslateZ(player.getPosition().z * TILE_SIZE);
                bNode.setTranslateY(-TILE_SIZE); // Floating high
                bNode.setVisible(true);
                if (bNode instanceof Box) ((Box)bNode).setDrawMode(DrawMode.FILL);
            }
        }
    }

    private void createVisual(Entity e) {
        Node node;
        
        if (e instanceof Agent) {
            Box box = new Box(TILE_SIZE * 0.7, TILE_SIZE * 0.7, TILE_SIZE * 0.7);
            box.setMaterial(new PhongMaterial(Color.ORANGE));
            node = box;
        } 
        else if (e instanceof Block) {
            Box box = new Box(TILE_SIZE * 0.6, TILE_SIZE * 0.6, TILE_SIZE * 0.6);
            box.setMaterial(new PhongMaterial(Color.CYAN));
            node = box;
        } 
        else if (e instanceof Bridge) {
            // Bridge visual
            Box box = new Box(TILE_SIZE, 5, TILE_SIZE);
            node = box;
        } 
        else if (e instanceof Trigger) {
            // Trigger: A flat pad
            Box box = new Box(TILE_SIZE * 0.6, 2, TILE_SIZE * 0.6);
            box.setMaterial(new PhongMaterial(Color.MAGENTA));
            node = box;
        } 
        else {
            node = new Box(10,10,10);
        }
        
        worldGroup.getChildren().add(node);
        entityVisuals.put(e.getId(), node);
    }

    private void updateVisual(Entity e, Node node) {
        // --- Position Updates ---
        if (e.getPosition().x >= 0) {
            node.setTranslateX(e.getPosition().x * TILE_SIZE);
            node.setTranslateZ(e.getPosition().z * TILE_SIZE);
            
            if (e instanceof Trigger) {
                node.setTranslateY((TILE_SIZE / 2) - 6); 
            } else if (e instanceof Bridge) {
                node.setTranslateY(TILE_SIZE / 2);
            } else {
                node.setTranslateY(0);
            }
            
            // Visibility Check
            if (e instanceof Block && ((Block)e).getPosition().x == -1) {
                // Handled in render()
            } else {
                node.setVisible(true);
            }
        }

        // --- Visual State Updates ---

        if (e instanceof Trigger) {
            Trigger t = (Trigger) e;
            Box b = (Box) node;
            PhongMaterial mat = (PhongMaterial) b.getMaterial();
            mat.setDiffuseColor(t.isPressed() ? Color.DARKMAGENTA : Color.MAGENTA);
            b.setScaleY(t.isPressed() ? 0.2 : 1.0);
        }

        if (e instanceof Bridge) {
            Bridge b = (Bridge) e;
            Box box = (Box) node;
            
            if (b.isActive()) {
                box.setMaterial(new PhongMaterial(Color.TOMATO));
                box.setDrawMode(DrawMode.FILL);
            } else {
                box.setMaterial(new PhongMaterial(Color.BLACK));
                box.setDrawMode(DrawMode.LINE); 
            }
        }
    }

    public Scene getScene() { return scene; }
}