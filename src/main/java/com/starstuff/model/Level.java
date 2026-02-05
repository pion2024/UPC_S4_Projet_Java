package com.starstuff.model;

import com.starstuff.common.Vector2;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * Defines the configuration and data for each game level.
 * Now supports explicit definition of regions and entity positions.
 */
public enum Level {

    // --- LEVEL TEST: Explicit definition of all elements ---
    LEVEL_TEST(
        20, 15, // Map Width, Height
        
        // 1. Regions (Defined by Top-Left X,Y and Width,Height)
        Arrays.asList(
            new Region(2, 7, 6, 6), // Region 1 (Bottom Left)
            new Region(9, 3, 7, 8), // Region 2 (Hub)
            new Region(2, 2, 6, 4)  // Region 3 (Storage)
        ),
        
        // 2. Single Entities
        new Vector2(3, 9),          // Player Start
        new Vector2(10, 5),         // Robot Start
        new Vector2(5, 11), 3,      // Terminal Pos, Facing (3=Left)
        
        // 3. Blocks (List of positions)
        Arrays.asList(
            new Vector2(4, 9),
            new Vector2(3, 3)
        ),
        
        // 4. Obstacles
        Arrays.asList(
            new Vector2(11, 6), 
            new Vector2(11, 7), 
            new Vector2(12, 6)
        ),
        
        // 5. Bridges & Triggers
        Arrays.asList(
            // Bridge 1: Connects Region 1 to Hub (Gap at x=8, y=9)
            new BridgeDef(new Vector2(8, 9), new Vector2(8, 9), new Vector2(5, 8)),
            
            // Bridge 2: Connects Hub to Storage (Gap at x=8, y=3)
            new BridgeDef(new Vector2(8, 3), new Vector2(8, 3), new Vector2(12, 7))
        )
    );

    // --- Data Fields ---
    private final int width;
    private final int height;
    private final List<Region> regions;
    private final Vector2 playerStart;
    private final Vector2 robotStart;
    private final Vector2 terminalPos;
    private final int terminalFacing;
    private final List<Vector2> blockPositions;
    private final List<Vector2> obstaclePositions;
    private final List<BridgeDef> bridges;

    Level(int width, int height, 
        List<Region> regions,
        Vector2 playerStart, Vector2 robotStart, 
        Vector2 terminalPos, int terminalFacing,
        List<Vector2> blocks,
        List<Vector2> obstacles,
        List<BridgeDef> bridges) {
        this.width = width;
        this.height = height;
        this.regions = regions;
        this.playerStart = playerStart;
        this.robotStart = robotStart;
        this.terminalPos = terminalPos;
        this.terminalFacing = terminalFacing;
        this.blockPositions = blocks;
        this.obstaclePositions = obstacles;
        this.bridges = bridges;
    }

    // --- Getters ---
    public int getWidth() { return width; }
    public int getHeight() { return height; }
    public List<Region> getRegions() { return regions; }
    public Vector2 getPlayerStart() { return playerStart; }
    public Vector2 getRobotStart() { return robotStart; }
    public Vector2 getTerminalPos() { return terminalPos; }
    public int getTerminalFacing() { return terminalFacing; }
    public List<Vector2> getBlockPositions() { return blockPositions; }
    public List<Vector2> getObstaclePositions() { return obstaclePositions; }
    public List<BridgeDef> getBridges() { return bridges; }

    // --- Helper Classes ---

    /**
     * Defines a rectangular floor area.
     */
    public static class Region {
        public int x, y, w, h;
        public Region(int x, int y, int w, int h) {
            this.x = x; this.y = y; this.w = w; this.h = h;
        }
    }

    /**
     * Defines a bridge and its controlling button.
     */
    public static class BridgeDef {
        public Vector2 start;
        public Vector2 end;
        public Vector2 triggerPos;
        public BridgeDef(Vector2 start, Vector2 end, Vector2 triggerPos) {
            this.start = start; this.end = end; this.triggerPos = triggerPos;
        }
    }
}