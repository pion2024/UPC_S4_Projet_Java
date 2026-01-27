package com.starstuff.model;

import com.starstuff.common.Vector3;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * The Model. Contains all game state and logic.
 */
public class GameWorld {
    private final List<Entity> entities = new CopyOnWriteArrayList<>();
    private final Set<String> floorPositions = new HashSet<>();
    private Agent player;

    // World Bounds for Camera calculation
    private int minX = Integer.MAX_VALUE;
    private int maxX = Integer.MIN_VALUE;
    private int minZ = Integer.MAX_VALUE;
    private int maxZ = Integer.MIN_VALUE;

    // Configuration
    private final int REGION_SIZE = 6;
    private int gapSize = 1; // Default gap size, will be set in initialization

    // Keep track of region bounds to calculate connections
    private final List<RegionBounds> regions = new ArrayList<>();
    private record RegionBounds(int minX, int maxX, int minZ, int maxZ) {}

    public GameWorld() {
        // Initialize with 3 regions and a gap of 1 block
        // The gap of 1 ensures the standard 1x1 bridge fits perfectly.
        initializeLevel(3, 1); 
    }

    /**
     * @param n Number of regions
     * @param gap The distance between regions (in blocks)
     */
    private void initializeLevel(int n, int gap) {
        this.gapSize = gap;
        
        // 1. Generate Regions
        for (int i = 0; i < n; i++) {
            generateRegion(i, n);
        }

        // 2. Generate Connections (Triggers & Bridges)
        // Connect Region i to Region i+1
        for (int i = 0; i < n - 1; i++) {
            createConnection(i, i + 1);
        }

        // 3. Create Player (Safe spawn in Region 0)
        player = new Agent(new Vector3(2, 0, 2));
        entities.add(player);

        // 4. Create a or many movable Block 
        // The axis: the plafonds are on the x-z plane, 0 at bottom left
        // x represents distance to left side, z represents distance to bottom size
        // y axis is orthogonal to x-z plane, representing the height, so should REMAIN 0
        entities.add(new Block(new Vector3(3, 0, 1)));
        entities.add(new Block(new Vector3(1, 0, 4))); //add one more block
    }

    private void generateRegion(int index, int total) {
        // Layout Logic: Max 2 columns.
        // Row 0: Indices 0, 1
        // Row 1: Indices 2, 3 ...
        int row = index / 2;
        int col = index % 2;

        int startX = col * (REGION_SIZE + gapSize);
        int startZ = row * (REGION_SIZE + gapSize);

        // Center Alignment for the last row if it has only 1 item (T-shape layout)
        boolean isLastItem = (index == total - 1);
        boolean isRowSingle = (total % 2 != 0); 
        
        if (isLastItem && isRowSingle && row > 0) {
            // Shift X to center it relative to the 2 items above
            startX = (REGION_SIZE + gapSize) / 2;
        }

        // Add Floor Tiles
        for (int x = startX; x < startX + REGION_SIZE; x++) {
            for (int z = startZ; z < startZ + REGION_SIZE; z++) {
                floorPositions.add(x + "," + z);
                updateBounds(x, z);
            }
        }

        // Store bounds for connection logic
        regions.add(new RegionBounds(startX, startX + REGION_SIZE, startZ, startZ + REGION_SIZE));
    }

    private void createConnection(int fromIdx, int toIdx) {
        RegionBounds r1 = regions.get(fromIdx);
        RegionBounds r2 = regions.get(toIdx);

        Vector3 bridgePos = null;
        Vector3 triggerPos = null;

        // Determine if connection is Horizontal or Vertical based on bounds
        
        // Horizontal: r2 is to the right of r1
        if (r2.minX >= r1.maxX) {
            // Calculate mid-Z for alignment
            int midZ = (Math.max(r1.minZ, r2.minZ) + Math.min(r1.maxZ, r2.maxZ)) / 2;
            
            // Bridge Position: In the gap. 
            // Since gap is 1, r1.maxX is the coordinate of the gap.
            // (Note: region maxX is exclusive in record usually, but here loop was < so it's exclusive)
            bridgePos = new Vector3(r1.maxX, 0, midZ);
            
            // Trigger Position: Inside R1
            triggerPos = new Vector3(r1.maxX - 2, 0, midZ);
        }
        // Vertical: r2 is below r1
        else {
            // Calculate mid-X for alignment
            int midX = (Math.max(r1.minX, r2.minX) + Math.min(r1.maxX, r2.maxX)) / 2;
            
            // Bridge Position: In the gap (at r1.maxZ)
            bridgePos = new Vector3(midX, 0, r1.maxZ);
            
            // Trigger Position: Inside R1
            triggerPos = new Vector3(midX, 0, r1.maxZ - 2);
        }

        if (bridgePos != null) {
            Bridge bridge = new Bridge(bridgePos);
            entities.add(bridge);

            Trigger trigger = new Trigger(triggerPos, bridge.getId());
            entities.add(trigger);
        }
    }

    private void updateBounds(int x, int z) {
        if (x < minX) minX = x;
        if (x > maxX) maxX = x;
        if (z < minZ) minZ = z;
        if (z > maxZ) maxZ = z;
    }

    public void updateWorldLogic() {
        for (Entity e : entities) {
            if (e instanceof Trigger) {
                Trigger t = (Trigger) e;
                boolean pressed = checkTriggerPressed(t);
                t.setPressed(pressed);
                
                entities.stream()
                    .filter(b -> b instanceof Bridge && b.getId() == t.getLinkedBridgeId())
                    .map(b -> (Bridge) b)
                    .forEach(b -> b.setActive(pressed));
            }
        }
    }

    private boolean checkTriggerPressed(Trigger t) {
        return entities.stream()
                .anyMatch(e -> e != t && e.getPosition().equals(t.getPosition()) && e.getPosition().x != -1);
    }

    public synchronized void moveAgent(Agent agent, int dx, int dz) {
        Vector3 target = agent.getPosition().add(dx, 0, dz);
        agent.setFacing(dx, dz);
        
        if (!isValidMove(target)) return;

        boolean blocked = entities.stream()
                .anyMatch(e -> e.getPosition().equals(target) 
                        && e != agent 
                        && !isWalkable(e));

        if (!blocked) {
            agent.setPosition(target);
        }
    }

    public synchronized void interact(Agent agent) {
        Vector3 targetPos = agent.getPosition().add(agent.getFacingDirection());

        if (agent.isCarrying()) {
            boolean occupied = entities.stream().anyMatch(e -> e.getPosition().equals(targetPos) && !isWalkable(e));
            if (!occupied && isValidMove(targetPos)) {
                Block b = agent.drop();
                b.setPosition(targetPos);
            }
        } else {
            entities.stream()
                    .filter(e -> e instanceof Block && e.getPosition().equals(targetPos))
                    .findFirst()
                    .ifPresent(e -> {
                        agent.hold((Block) e);
                        e.setPosition(new Vector3(-1, -1, -1));
                    });
        }
    }

    private boolean isWalkable(Entity e) {
        if (e instanceof Trigger) return true;
        if (e instanceof Bridge) return ((Bridge)e).isActive();
        return false;
    }

    private boolean isValidMove(Vector3 p) {
        if (floorPositions.contains(p.x + "," + p.z)) return true;
        return entities.stream()
                .anyMatch(e -> e instanceof Bridge && ((Bridge)e).isActive() && e.getPosition().equals(p));
    }

    public Agent getPlayer() { return player; }
    public List<Entity> getEntities() { return entities; }
    public Set<String> getFloorPositions() { return floorPositions; }
    
    public int getMinX() { return minX; }
    public int getMaxX() { return maxX; }
    public int getMinZ() { return minZ; }
    public int getMaxZ() { return maxZ; }
}