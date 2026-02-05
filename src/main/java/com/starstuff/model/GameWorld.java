package com.starstuff.model;

import com.starstuff.common.Vector2;
import java.util.*;

public class GameWorld {
    private int[][] terrain; 
    private int width, height;

    private Agent player;
    private Terminal terminal;
    private List<Entity> entities = new ArrayList<>();
    
    // Robot Execution State
    private Stack<Command> commandStack = new Stack<>();
    private boolean robotExecuting = false;
    private boolean robotRetreating = false; // NEW: Backtracking mode
    private Command currentRobotCommand = null;
    
    // Paths
    private Queue<Vector2> executionPath = new LinkedList<>(); // For actual movement
    private List<Vector2> previewPath = new ArrayList<>(); // For UI rendering

    public GameWorld(Level level) {
        MapGenerator.generateLevel(this, level);
    }

    public void initTerrain(int w, int h) {
        this.width = w;
        this.height = h;
        this.terrain = new int[w][h];
    }
    
    public void setTile(int x, int y, int type) {
        if (x >= 0 && x < width && y >= 0 && y < height) terrain[x][y] = type;
    }
    
    public int getTile(int x, int y) {
        if (x >= 0 && x < width && y >= 0 && y < height) return terrain[x][y];
        return 0; 
    }

    public void update() {
        updateButtons();
        if (robotExecuting || robotRetreating) {
            updateRobotLogic();
        }
    }

    private void updateButtons() {
        for (Entity e : entities) {
            if (e instanceof Trigger) {
                Trigger t = (Trigger) e;
                boolean pressed = false;
                for (Entity other : entities) {
                    if (other != t && other.getPosition().equals(t.getPosition())) {
                        pressed = true;
                        break;
                    }
                }
                t.setPressed(pressed);
                for (Entity b : entities) {
                    if (b instanceof Bridge && b.getId() == t.getLinkedBridgeId()) {
                        ((Bridge)b).setActive(pressed);
                    }
                }
            }
        }
    }
    
    // --- Agent Interaction (Player) ---
    public void moveAgent(Agent ag, int dx, int dy) {
        if (ag == null) return;
        if (dx != 0 || dy != 0) ag.setFacing(new Vector2(dx, dy));
        Vector2 target = ag.getPosition().add(dx, dy);
        
        // Player manual move: Strict check (Bridges must be active)
        if (isValidMove(target, false)) {
            ag.setPosition(target);
            if (ag.isCarrying()) ag.getHeldBlock().setPosition(target);
        }
    }

    public void interact(Agent ag) {
        if (ag == null) return;
        if (ag.isCarrying()) {
            Vector2 targetPos = ag.getPosition().add(ag.getFacing());
            if (canPlaceBlockAt(targetPos)) {
                Block b = ag.drop();
                b.setPosition(targetPos);
            }
        } else {
            Vector2 facingPos = ag.getPosition().add(ag.getFacing());
            Block target = getBlockAt(facingPos);
            if (target == null) {
                // Fallback adjacent check
                Vector2[] dirs = {new Vector2(0,1), new Vector2(0,-1), new Vector2(1,0), new Vector2(-1,0)};
                for (Vector2 d : dirs) {
                    Vector2 check = ag.getPosition().add(d);
                    target = getBlockAt(check);
                    if (target != null) { ag.setFacing(d); break; }
                }
            }
            if (target != null) {
                ag.hold(target);
                target.setPosition(ag.getPosition());
            }
        }
    }

    private Block getBlockAt(Vector2 pos) {
        for (Entity e : entities) {
            if (e instanceof Block && e.getPosition().equals(pos)) return (Block) e;
        }
        return null;
    }

    public boolean canPlaceBlockAt(Vector2 p) {
        if (p.x < 0 || p.x >= width || p.y < 0 || p.y >= height) return false;
        int tile = terrain[p.x][p.y];
        boolean isSafeGround = (tile == 1); 
        if (!isSafeGround) {
            for(Entity e : entities) {
                if (e instanceof Bridge && e.getPosition().equals(p) && ((Bridge)e).isActive()) {
                    isSafeGround = true; break;
                }
            }
        }
        if (!isSafeGround) return false;
        for (Entity e : entities) {
            if (e.getPosition().equals(p)) {
                if (e instanceof Trigger || e instanceof Bridge) continue;
                return false; 
            }
        }
        return true;
    }

    // --- ROBOT LOGIC & PREVIEW ---

    /**
     * Calculates the full path preview assuming bridges are ON.
     * Does NOT move the robot.
     */
    public void updatePreview(List<Command> cmds) {
        previewPath.clear();
        Robot robot = getRobot();
        if (robot == null) return;

        // Simulate from current position
        Vector2 simPos = robot.getPosition();
        // We don't simulate carrying logic fully for path preview, 
        // just the movement trace.
        
        for (Command cmd : cmds) {
            if (cmd.getTarget() == null) continue;
            Vector2 target = cmd.getTarget().getPosition();
            
            List<Vector2> segment = null;
            if (cmd.getType() == Command.Type.GO_TO) {
                 // If target is block/obstacle, go next to it
                 if (cmd.getTarget() instanceof Block || cmd.getTarget() instanceof Obstacle) {
                     segment = findPathToAdjacent(simPos, target, true); // true = ignore bridges
                 } else {
                     segment = findPath(simPos, target, true);
                 }
            } else {
                // Pick/Drop -> Adjacent
                segment = findPathToAdjacent(simPos, target, true);
            }

            if (segment != null) {
                previewPath.addAll(segment);
                // Update simPos to the end of this segment for next command
                if (!segment.isEmpty()) simPos = segment.get(segment.size() - 1);
            }
        }
    }

    public void startRobotExecution(List<Command> cmds) {
        Robot r = getRobot();
        if (r != null) r.clearHistory(); // Start fresh history

        commandStack.clear();
        for (int i = cmds.size() - 1; i >= 0; i--) {
            commandStack.push(cmds.get(i));
        }
        
        robotExecuting = true;
        robotRetreating = false;
        currentRobotCommand = null;
        executionPath.clear();
    }

    private void updateRobotLogic() {
        Robot robot = getRobot();
        if(robot == null) return;

        // --- MODE 1: RETREATING (Backtracking) ---
        if (robotRetreating) {
            if (robot.hasHistory()) {
                // Undo one step
                Robot.RobotSnapshot snapshot = robot.popHistory();
                
                // 1. Restore Position & Facing
                robot.setPosition(snapshot.pos);
                robot.setFacing(snapshot.facing);
                
                // 2. Restore Block State
                // Case A: We are holding something now, but snapshot says we held nothing (We picked it up) -> DROP IT back
                if (robot.isCarrying() && snapshot.heldBlock == null) {
                    Block b = robot.drop();
                    // Put it back where it was found (stored in snapshot?)
                    // The snapshot.blockWorldPos tells us where the block WAS.
                    // But wait, if we drop it, we need to know where to put it.
                    // Actually, if we just performed a PickUp, the block is now in hand.
                    // We need to restore it to 'snapshot.blockWorldPos' if that is not null.
                    if (snapshot.blockWorldPos != null) {
                         b.setPosition(snapshot.blockWorldPos);
                    }
                }
                // Case B: We hold nothing, but snapshot says we held something (We dropped it) -> GRAB IT back
                else if (!robot.isCarrying() && snapshot.heldBlock != null) {
                    robot.hold(snapshot.heldBlock);
                    snapshot.heldBlock.setPosition(robot.getPosition());
                }
                // Case C: Holding same block (Just moving) -> Update block pos
                else if (robot.isCarrying()) {
                    robot.getHeldBlock().setPosition(snapshot.pos);
                }

            } else {
                // History empty, we are back at start
                robotRetreating = false;
                robot.say(null, 0); // Clear bubble
            }
            return;
        }

        // --- MODE 2: EXECUTING ---
        
        if (currentRobotCommand == null) {
            if (commandStack.isEmpty()) {
                robotExecuting = false;
                return;
            }
            currentRobotCommand = commandStack.pop();
            
            // Plan Path (Optimistic: Ignore Bridges)
            if (currentRobotCommand.getTarget() != null) {
                calculateExecutionPath(robot, currentRobotCommand);
            }
        }

        // Execute Step
        boolean stepFinished = executeStep(robot, currentRobotCommand);
        if (stepFinished) {
            currentRobotCommand = null;
            executionPath.clear();
        }
    }
    
    private void calculateExecutionPath(Robot robot, Command cmd) {
        executionPath.clear();
        Vector2 targetPos = cmd.getTarget().getPosition();
        Vector2 startPos = robot.getPosition();
        List<Vector2> path = null;

        // Optimistic Planning (Ignore Bridges = true)
        if (cmd.getType() == Command.Type.GO_TO) {
            if (cmd.getTarget() instanceof Block || cmd.getTarget() instanceof Obstacle) {
                 path = findPathToAdjacent(startPos, targetPos, true);
            } else {
                 path = findPath(startPos, targetPos, true);
            }
        } else {
            path = findPathToAdjacent(startPos, targetPos, true);
        }

        if (path != null) executionPath.addAll(path);
    }

    private boolean executeStep(Robot r, Command cmd) {
        if (cmd.getTarget() == null) return true;
        Vector2 currentPos = r.getPosition();
        Vector2 targetPos = cmd.getTarget().getPosition();
        boolean isAdjacent = isAdjacent(currentPos, targetPos);
        boolean isOnTop = currentPos.equals(targetPos);

        // -- ACTION LOGIC --
        // Check if we need to perform the final action (Pick/Drop)
        if (cmd.getType() == Command.Type.PICK_UP && isAdjacent) {
             // Record state BEFORE picking up
             Block targetB = (Block)cmd.getTarget();
             r.pushHistory(targetB.getPosition()); // Save block pos
             
             r.setFacing(targetPos.add(currentPos.x * -1, currentPos.y * -1));
             if (!r.isCarrying()) {
                 r.hold(targetB);
                 targetB.setPosition(r.getPosition());
             }
             return true; // Command Done
        }
        else if (cmd.getType() == Command.Type.DROP_AT && isAdjacent) {
             // Record state BEFORE dropping
             r.pushHistory(null);
             
             r.setFacing(targetPos.add(currentPos.x * -1, currentPos.y * -1));
             if (r.isCarrying()) {
                 Block b = r.drop();
                 b.setPosition(targetPos);
             }
             return true; // Command Done
        }
        else if (cmd.getType() == Command.Type.GO_TO) {
             if (isOnTop) return true;
             if ((cmd.getTarget() instanceof Block) && isAdjacent) return true;
        }

        // -- MOVEMENT LOGIC --
        if (!executionPath.isEmpty()) {
            Vector2 nextStep = executionPath.peek();
            
            // Record History BEFORE moving
            // (blockWorldPos is irrelevant for pure move, so null/current)
            r.pushHistory(null);

            // REALITY CHECK: Is the move actually valid right now?
            if (!isValidMove(nextStep, false)) { // false = STRICT CHECK (bridges must be active)
                triggerRobotError(r, "Blocked!");
                return true; // Stop this command, switch to retreating
            }

            // Execute Move
            Vector2 dir = nextStep.add(currentPos.x * -1, currentPos.y * -1);
            r.setFacing(dir);
            r.setPosition(nextStep);
            if (r.isCarrying()) r.getHeldBlock().setPosition(nextStep);
            
            executionPath.poll();
            return false; // Still moving
        }
        
        return true; // Nothing left to do
    }
    
    private void triggerRobotError(Robot r, String msg) {
        r.say(msg, 5000); // Speech bubble for 5 sec or until cleared
        robotExecuting = false;
        robotRetreating = true; // Start backtracking
        commandStack.clear();
        executionPath.clear();
        currentRobotCommand = null;
    }

    // --- Pathfinding Utilities ---

    private boolean isAdjacent(Vector2 a, Vector2 b) {
        return (Math.abs(a.x - b.x) + Math.abs(a.y - b.y)) == 1;
    }

    private List<Vector2> findPathToAdjacent(Vector2 start, Vector2 target, boolean ignoreBridges) {
        Vector2[] dirs = {new Vector2(0,1), new Vector2(0,-1), new Vector2(1,0), new Vector2(-1,0)};
        List<Vector2> bestPath = null;
        for (Vector2 d : dirs) {
            Vector2 neighbor = target.add(d);
            if (isValidMove(neighbor, ignoreBridges) || neighbor.equals(start)) {
                List<Vector2> path = findPath(start, neighbor, ignoreBridges);
                if (path != null) {
                    if (bestPath == null || path.size() < bestPath.size()) bestPath = path;
                }
            }
        }
        return bestPath;
    }

    private List<Vector2> findPath(Vector2 start, Vector2 end, boolean ignoreBridges) {
        if (start.equals(end)) return new ArrayList<>();
        if (!isValidMove(end, ignoreBridges)) return null;

        Queue<Vector2> queue = new LinkedList<>();
        queue.add(start);
        Map<Vector2, Vector2> cameFrom = new HashMap<>();
        cameFrom.put(start, null);
        
        while (!queue.isEmpty()) {
            Vector2 current = queue.poll();
            if (current.equals(end)) break;
            Vector2[] dirs = {new Vector2(0,1), new Vector2(0,-1), new Vector2(1,0), new Vector2(-1,0)};
            for (Vector2 d : dirs) {
                Vector2 next = current.add(d);
                if (!cameFrom.containsKey(next) && isValidMove(next, ignoreBridges)) {
                    queue.add(next);
                    cameFrom.put(next, current);
                }
            }
        }
        if (!cameFrom.containsKey(end)) return null; 

        List<Vector2> path = new ArrayList<>();
        Vector2 current = end;
        while (current != null && !current.equals(start)) {
            path.add(current);
            current = cameFrom.get(current);
        }
        Collections.reverse(path);
        return path;
    }

    public boolean isValidMove(Vector2 p, boolean ignoreBridgeState) {
        if (p.x < 0 || p.x >= width || p.y < 0 || p.y >= height) return false;
        
        for (Entity e : entities) {
            if (e.getPosition().equals(p)) {
                if (e instanceof Trigger) continue; 
                if (e instanceof Bridge) {
                    if (ignoreBridgeState) continue; // Optimistic
                    if (((Bridge)e).isActive()) continue; // Realistic
                    continue; 
                }
                if (e instanceof Block) {
                    boolean isHeld = (player != null && player.getHeldBlock() == e);
                    if (getRobot() != null && getRobot().getHeldBlock() == e) isHeld = true;
                    if (isHeld) continue; 
                }
                return false; 
            }
        }

        int tile = terrain[p.x][p.y];
        if (tile == 0) { 
            boolean bridgeHere = false;
            for(Entity e : entities) {
                if (e instanceof Bridge && e.getPosition().equals(p)) {
                    if (ignoreBridgeState || ((Bridge)e).isActive()) bridgeHere = true;
                    break;
                }
            }
            if (!bridgeHere) return false;
        }
        return true;
    }
    
    // Getters
    public List<Vector2> getPreviewPath() { return previewPath; }
    public Robot getRobot() {
        for(Entity e : entities) if(e instanceof Robot) return (Robot)e;
        return null;
    }
    public Agent getPlayer() { return player; }
    public void setPlayer(Agent p) { this.player = p; }
    public Terminal getTerminal() { return terminal; }
    public void setTerminal(Terminal t) { this.terminal = t; }
    public List<Entity> getEntities() { return entities; }
    public int getWidth() { return width; }
    public int getHeight() { return height; }
}