package com.starstuff.model;

import com.starstuff.common.Vector2;
import java.util.Arrays;
import java.util.List;

public enum Level {

    LEVEL_1(
        18, 14, 
        Arrays.asList(
            new Region(2, 7, 6, 6), 
            new Region(9, 3, 7, 8), 
            new Region(2, 2, 6, 4)  
        ),
        new Vector2(3, 9),          
        new Vector2(10, 5),         
        new Vector2(5, 11), 3,      
        new Vector2(2, 3),          // Target Zone Position (Top Left Room)
        Arrays.asList(new Vector2(4, 9), new Vector2(3, 3)),
        Arrays.asList(new Vector2(11, 6), new Vector2(11, 7), new Vector2(12, 6)),
        Arrays.asList(
            new BridgeDef(new Vector2(8, 9), new Vector2(8, 9), new Vector2(5, 8)),
            new BridgeDef(new Vector2(8, 3), new Vector2(8, 3), new Vector2(12, 7))
        )
    ),

    LEVEL_TEST(
        20, 15,
        Arrays.asList(
            new Region(2, 7, 6, 6),
            new Region(9, 3, 7, 8),
            new Region(2, 2, 6, 4)
        ),
        new Vector2(3, 9),
        new Vector2(10, 5),
        new Vector2(5, 11), 3, //agent should be at left to control terminal
        new Vector2(3, 2),          // Target Zone position 
        Arrays.asList(
            new Vector2(4, 9), new Vector2(3, 3) //block positions
        ),
        Arrays.asList(
            new Vector2(11, 6), new Vector2(12, 6), new Vector2(13, 6), //obstacles positions
            new Vector2(11, 7),                          new Vector2(13, 7),
            new Vector2(11, 8), 
            new Vector2(11, 9), new Vector2(12, 9), new Vector2(13, 9)
        ),
        Arrays.asList(  // bridges & triggers positions 
            new BridgeDef(new Vector2(8, 9), new Vector2(8, 9), new Vector2(5, 8)),
            new BridgeDef(new Vector2(8, 3), new Vector2(8, 3), new Vector2(12, 7))
        )
    );

    private final int width;
    private final int height;
    private final List<Region> regions;
    private final Vector2 playerStart;
    private final Vector2 robotStart;
    private final Vector2 terminalPos;
    private final int terminalFacing;
    private final Vector2 targetZonePos; // New Field
    private final List<Vector2> blockPositions;
    private final List<Vector2> obstaclePositions;
    private final List<BridgeDef> bridges;

    Level(int width, int height, 
        List<Region> regions,
        Vector2 playerStart, Vector2 robotStart, 
        Vector2 terminalPos, int terminalFacing,
        Vector2 targetZonePos,
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
        this.targetZonePos = targetZonePos;
        this.blockPositions = blocks;
        this.obstaclePositions = obstacles;
        this.bridges = bridges;
    }

    public int getWidth() { return width; }
    public int getHeight() { return height; }
    public List<Region> getRegions() { return regions; }
    public Vector2 getPlayerStart() { return playerStart; }
    public Vector2 getRobotStart() { return robotStart; }
    public Vector2 getTerminalPos() { return terminalPos; }
    public int getTerminalFacing() { return terminalFacing; }
    public Vector2 getTargetZonePos() { return targetZonePos; }
    public List<Vector2> getBlockPositions() { return blockPositions; }
    public List<Vector2> getObstaclePositions() { return obstaclePositions; }
    public List<BridgeDef> getBridges() { return bridges; }

    public static class Region {
        public int x, y, w, h;
        public Region(int x, int y, int w, int h) {
            this.x = x; this.y = y; this.w = w; this.h = h;
        }
    }

    public static class BridgeDef {
        public Vector2 start;
        public Vector2 end;
        public Vector2 triggerPos;
        public BridgeDef(Vector2 start, Vector2 end, Vector2 triggerPos) {
            this.start = start; this.end = end; this.triggerPos = triggerPos;
        }
    }
}