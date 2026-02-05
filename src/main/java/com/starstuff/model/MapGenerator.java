package com.starstuff.model;

import com.starstuff.common.Vector2;
import java.util.List;

public class MapGenerator {

    public static void generateLevel(GameWorld world, Level level) {
        world.initTerrain(level.getWidth(), level.getHeight());
        List<Entity> entities = world.getEntities();
        entities.clear();

        // 1. Regions
        for (Level.Region r : level.getRegions()) {
            createRegion(world, r.x, r.y, r.w, r.h);
        }

        // 2. Entities
        if (level.getPlayerStart() != null) {
            world.setPlayer(new Agent(level.getPlayerStart()));
            entities.add(world.getPlayer());
        }
        if (level.getRobotStart() != null) {
            entities.add(new Robot(level.getRobotStart()));
        }
        if (level.getTerminalPos() != null) {
            Terminal t = new Terminal(level.getTerminalPos(), level.getTerminalFacing());
            world.setTerminal(t);
            entities.add(t);
        }
        
        // Target Zone
        if (level.getTargetZonePos() != null) {
            entities.add(new TargetZone(level.getTargetZonePos()));
        }

        // Blocks & Obstacles
        for (Vector2 pos : level.getBlockPositions()) entities.add(new Block(pos));
        for (Vector2 pos : level.getObstaclePositions()) entities.add(new Obstacle(pos));

        // Bridges
        for (Level.BridgeDef bd : level.getBridges()) {
            createBridgeConnection(world, bd.start, bd.end, bd.triggerPos);
        }
    }

    private static void createBridgeConnection(GameWorld world, Vector2 start, Vector2 end, Vector2 buttonPos) {
        int minX = Math.min(start.x, end.x);
        int maxX = Math.max(start.x, end.x);
        int minY = Math.min(start.y, end.y);
        int maxY = Math.max(start.y, end.y);
        int firstBridgeId = -1;
        
        for (int x = minX; x <= maxX; x++) {
            for (int y = minY; y <= maxY; y++) {
                if (world.getTile(x, y) == 0) { 
                    Bridge b = new Bridge(new Vector2(x, y));
                    world.getEntities().add(b);
                    if (firstBridgeId == -1) firstBridgeId = b.getId();
                }
            }
        }
        if (firstBridgeId != -1) {
            Trigger t = new Trigger(buttonPos, firstBridgeId);
            world.getEntities().add(t);
        }
    }

    private static void createRegion(GameWorld world, int startX, int startY, int w, int h) {
        for (int x = startX; x < startX + w; x++) {
            for (int y = startY; y < startY + h; y++) {
                world.setTile(x, y, 1);
            }
        }
    }
}