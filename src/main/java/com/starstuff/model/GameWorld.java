package com.starstuff.model;

import com.starstuff.common.Vector2;
import javax.swing.JOptionPane;
import java.util.*;

public class GameWorld {
    private int[][] terrain; 
    private int width, height;
    private Level currentLevel; 

    private Agent player;
    private Terminal terminal;
    private List<Entity> entities = new ArrayList<>();
    
    // Robot Execution State
    private Stack<Command> commandStack = new Stack<>();
    private boolean robotExecuting = false;
    private boolean robotRetreating = false;
    private Command currentRobotCommand = null;
    
    // Path & Timing
    private Queue<Vector2> currentRobotPath = new LinkedList<>(); 
    private List<Vector2> previewPath = new ArrayList<>();
    
    // Speed Control
    private int robotStepDelay = 500; 
    private long lastRobotStepTime = 0;

    public GameWorld(Level level) {
        this.currentLevel = level;
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
        // Always update buttons first to ensure environment is valid
        updateButtons();
        
        long now = System.currentTimeMillis();
        if (now - lastRobotStepTime > robotStepDelay) {
            if (robotExecuting || robotRetreating) {
                updateRobotLogic();
                lastRobotStepTime = now;
            }
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
    
    public void moveAgent(Agent ag, int dx, int dy) {
        if (ag == null) return;
        if (dx != 0 || dy != 0) ag.setFacing(new Vector2(dx, dy));
        Vector2 target = ag.getPosition().add(dx, dy);
        
        if (isValidMove(target, false)) {
            ag.setPosition(target);
            if (ag.isCarrying()) ag.getHeldBlock().setPosition(target);

            // FIX: Check Victory immediately upon moving (if Player)
            if (ag == player) {
                checkVictory(ag);
            }
        }
    }

    public void interact(Agent ag) {
        if (ag == null) return;

        // 1. Check Target Zone Victory (Manual Trigger)
        // Allow triggering if standing ON it OR NEXT to it
        if (!ag.isCarrying()) {
            checkVictory(ag);
        }

        // 2. Normal Interaction
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

    private void checkVictory(Agent ag) {
        for (Entity e : entities) {
            if (e instanceof TargetZone) {
                // Win if on top OR adjacent (Distance <= 1)
                int dist = Math.abs(ag.getPosition().x - e.getPosition().x) + 
                           Math.abs(ag.getPosition().y - e.getPosition().y);
                
                if (dist <= 0) {
                    // Show dialog safely
                    javax.swing.SwingUtilities.invokeLater(() -> {
                        JOptionPane.showMessageDialog(null, "VICTORY! You reached the Target Zone!");
                        System.exit(0); 
                    });
                    return;
                }
            }
        }
    }

    // --- RESET LOGIC ---
    public void resetRobot() {
        Robot r = getRobot();
        if (r == null) return;

        // 1. Drop held item if any
        if (r.isCarrying()) {
            r.drop(); 
        }

        // 2. Clear State
        r.clearHistory();
        commandStack.clear();
        currentRobotPath.clear();
        previewPath.clear();
        currentRobotCommand = null;
        robotExecuting = false;
        robotRetreating = false;
        r.say(null, 0);

        // DO NOT reset position. Robot stays where it is.
    }

    public void setRobotStepDelay(int delayMs) {
        this.robotStepDelay = delayMs;
    }

    // --- ROBOT LOGIC ---

    public void updatePreview(List<Command> cmds) {
        previewPath.clear();
        Robot robot = getRobot();
        if (robot == null) return;
        Vector2 simPos = robot.getPosition();
        
        for (Command cmd : cmds) {
            if (cmd.getTarget() == null) continue;
            Vector2 target = cmd.getTarget().getPosition();
            List<Vector2> segment = null;
            if (cmd.getType() == Command.Type.GO_TO) {
                 if (cmd.getTarget() instanceof Block || cmd.getTarget() instanceof Obstacle) {
                     segment = findPathToAdjacent(simPos, target, true);
                 } else {
                     segment = findPath(simPos, target, true);
                 }
            } else {
                segment = findPathToAdjacent(simPos, target, true);
            }
            if (segment != null) {
                previewPath.addAll(segment);
                if (!segment.isEmpty()) simPos = segment.get(segment.size() - 1);
            }
        }
    }

    public void startRobotExecution(List<Command> cmds) {
        Robot r = getRobot();
        if (r != null) r.clearHistory();
        commandStack.clear();
        for (int i = cmds.size() - 1; i >= 0; i--) {
            commandStack.push(cmds.get(i));
        }
        robotExecuting = true;
        robotRetreating = false;
        currentRobotCommand = null;
        currentRobotPath.clear();
        lastRobotStepTime = 0;
    }

    private void updateRobotLogic() {
        Robot robot = getRobot();
        if(robot == null) return;

        // MODE 1: RETREATING
        if (robotRetreating) {
            if (robot.hasHistory()) {
                Robot.RobotSnapshot snapshot = robot.popHistory();
                
                // Correct Undo Facing
                if (!snapshot.pos.equals(robot.getPosition())) {
                    int dx = snapshot.pos.x - robot.getPosition().x;
                    int dy = snapshot.pos.y - robot.getPosition().y;
                    robot.setFacing(new Vector2(dx, dy));
                } else {
                    robot.setFacing(snapshot.facing);
                }

                robot.setPosition(snapshot.pos);
                
                // Restore Block State
                if (robot.isCarrying() && snapshot.heldBlock == null) {
                    Block b = robot.drop();
                    if (snapshot.blockWorldPos != null) b.setPosition(snapshot.blockWorldPos);
                }
                else if (!robot.isCarrying() && snapshot.heldBlock != null) {
                    robot.hold(snapshot.heldBlock);
                    snapshot.heldBlock.setPosition(robot.getPosition());
                }
                else if (robot.isCarrying()) {
                    robot.getHeldBlock().setPosition(snapshot.pos);
                }

                // Immediate Terrain Update
                updateButtons();

            } else {
                // Back at start
                robotRetreating = false;
                robot.say(null, 0); 
                // Continue if commands remain
                if (commandStack.isEmpty()) {
                    robotExecuting = false;
                }
            }
            return;
        }

        // MODE 2: EXECUTING
        if (currentRobotCommand == null) {
            if (commandStack.isEmpty()) {
                robotExecuting = false;
                return;
            }
            currentRobotCommand = commandStack.pop();
            // Re-plan path from CURRENT position
            if (currentRobotCommand.getTarget() != null) {
                calculateExecutionPath(robot, currentRobotCommand);
            }
        }

        boolean stepFinished = executeStep(robot, currentRobotCommand);
        if (stepFinished) {
            currentRobotCommand = null;
            currentRobotPath.clear();
        }
    }
    
    private void calculateExecutionPath(Robot robot, Command cmd) {
        currentRobotPath.clear();
        Vector2 targetPos = cmd.getTarget().getPosition();
        Vector2 startPos = robot.getPosition();
        List<Vector2> path = null;
        if (cmd.getType() == Command.Type.GO_TO) {
            if (cmd.getTarget() instanceof Block || cmd.getTarget() instanceof Obstacle) {
                 path = findPathToAdjacent(startPos, targetPos, true);
            } else {
                 path = findPath(startPos, targetPos, true);
            }
        } else {
            path = findPathToAdjacent(startPos, targetPos, true);
        }
        if (path != null) currentRobotPath.addAll(path);
    }

    private boolean executeStep(Robot r, Command cmd) {
        if (cmd.getTarget() == null) return true;
        Vector2 currentPos = r.getPosition();
        Vector2 targetPos = cmd.getTarget().getPosition();
        boolean isAdjacent = isAdjacent(currentPos, targetPos);
        boolean isOnTop = currentPos.equals(targetPos);

        if (cmd.getType() == Command.Type.PICK_UP && isAdjacent) {
             Block targetB = (Block)cmd.getTarget();
             r.pushHistory(targetB.getPosition());
             r.setFacing(targetPos.add(currentPos.x * -1, currentPos.y * -1));
             if (!r.isCarrying()) {
                 r.hold(targetB);
                 targetB.setPosition(r.getPosition());
             }
             return true; 
        }
        else if (cmd.getType() == Command.Type.DROP_AT && isAdjacent) {
             r.pushHistory(null);
             r.setFacing(targetPos.add(currentPos.x * -1, currentPos.y * -1));
             if (r.isCarrying()) {
                 Block b = r.drop();
                 b.setPosition(targetPos);
             }
             return true; 
        }
        else if (cmd.getType() == Command.Type.GO_TO) {
             if (isOnTop) return true;
             if ((cmd.getTarget() instanceof Block) && isAdjacent) return true;
        }

        if (!currentRobotPath.isEmpty()) {
            Vector2 nextStep = currentRobotPath.peek();
            r.pushHistory(null);

            // REAL-TIME CHECK
            if (!isValidMove(nextStep, false)) { 
                triggerRobotError(r, "Blocked!");
                return true; 
            }

            Vector2 dir = nextStep.add(currentPos.x * -1, currentPos.y * -1);
            r.setFacing(dir);
            r.setPosition(nextStep);
            if (r.isCarrying()) r.getHeldBlock().setPosition(nextStep);
            
            currentRobotPath.poll();
            return false; 
        }
        
        return true; 
    }
    
    private void triggerRobotError(Robot r, String msg) {
        r.say(msg, 3000); 
        robotRetreating = true; 
        currentRobotPath.clear();
        currentRobotCommand = null; 
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

    public boolean isAdjacent(Vector2 a, Vector2 b) {
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
                if (e instanceof TargetZone) continue;
                if (e instanceof Bridge) {
                    if (ignoreBridgeState) continue; 
                    if (((Bridge)e).isActive()) continue; 
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
    
    public Queue<Vector2> getCurrentRobotPath() { return currentRobotPath; }
    public List<Vector2> getPreviewPath() { return previewPath; }
    public boolean isRobotExecuting() { return robotExecuting; }
    public Robot getRobot() { for(Entity e : entities) if(e instanceof Robot) return (Robot)e; return null; }
    public Agent getPlayer() { return player; }
    public void setPlayer(Agent p) { this.player = p; }
    public Terminal getTerminal() { return terminal; }
    public void setTerminal(Terminal t) { this.terminal = t; }
    public List<Entity> getEntities() { return entities; }
    public int getWidth() { return width; }
    public int getHeight() { return height; }
}