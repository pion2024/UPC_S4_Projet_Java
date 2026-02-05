package com.starstuff.view;

import com.starstuff.common.Vector2;
import com.starstuff.model.*;
import com.starstuff.model.Robot; 

import javax.swing.*;
import java.awt.*;
import java.util.Queue;

public class GamePanel extends JPanel {
    private GameWorld world;
    private final int TILE_SIZE = 40; 

    public GamePanel(GameWorld world) {
        this.world = world;
        setPreferredSize(new Dimension(world.getWidth() * TILE_SIZE, world.getHeight() * TILE_SIZE));
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        // 1. Draw Void
        g2.setColor(new Color(25, 25, 50)); 
        g2.fillRect(0, 0, getWidth(), getHeight());

        // 2. Draw Terrain
        for (int x = 0; x < world.getWidth(); x++) {
            for (int y = 0; y < world.getHeight(); y++) {
                int tileType = world.getTile(x, y);
                if (tileType == 1) { // Floor
                    g2.setColor(new Color(110, 110, 140)); 
                    g2.fillRect(x * TILE_SIZE, y * TILE_SIZE, TILE_SIZE, TILE_SIZE);
                    g2.setColor(new Color(90, 90, 120));
                    g2.drawRect(x * TILE_SIZE, y * TILE_SIZE, TILE_SIZE, TILE_SIZE);
                }
            }
        }

        // 3. Wires
        g2.setStroke(new BasicStroke(3));
        for (Entity e : world.getEntities()) {
            if (e instanceof Trigger) {
                Trigger t = (Trigger) e;
                Entity bridge = findEntity(t.getLinkedBridgeId());
                if (bridge != null) {
                    drawManhattanWire(g2, t.getPosition(), bridge.getPosition(), t.isPressed());
                }
            }
        }
        g2.setStroke(new BasicStroke(1));

        // 4. Entities (Bottom Layer)
        for (Entity e : world.getEntities()) {
            if (e instanceof Agent || e instanceof Block) continue;
            drawEntity(g2, e);
        }
        
        // 5. Draw Active Robot Path (Only when executing)
        if (world.isRobotExecuting()) {
            Queue<Vector2> path = world.getCurrentRobotPath();
            if (path != null && !path.isEmpty()) {
                g2.setColor(new Color(255, 255, 0, 200)); // Bright Yellow
                Stroke oldStroke = g2.getStroke();
                g2.setStroke(new BasicStroke(3, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND, 0, new float[]{5}, 0));
                
                Robot r = world.getRobot();
                Vector2 prev = (r != null) ? r.getPosition() : null;
                
                for (Vector2 next : path) {
                    if (prev != null) {
                        int x1 = prev.x * TILE_SIZE + TILE_SIZE/2;
                        int y1 = prev.y * TILE_SIZE + TILE_SIZE/2;
                        int x2 = next.x * TILE_SIZE + TILE_SIZE/2;
                        int y2 = next.y * TILE_SIZE + TILE_SIZE/2;
                        g2.drawLine(x1, y1, x2, y2);
                        g2.fillOval(x2-3, y2-3, 6, 6);
                    }
                    prev = next;
                }
                g2.setStroke(oldStroke);
            }
        }

        // 6. Agents & Blocks
        for (Entity e : world.getEntities()) {
            if (e instanceof Agent || e instanceof Block) {
                drawEntity(g2, e);
            }
        }
        
        // 7. Robot Speech Bubble
        Robot robot = world.getRobot();
        if (robot != null && robot.getSpeechBubble() != null) {
            drawSpeechBubble(g2, robot, robot.getSpeechBubble());
        }
    }

    private void drawSpeechBubble(Graphics2D g2, Robot r, String text) {
        int x = r.getPosition().x * TILE_SIZE + TILE_SIZE/2;
        int y = r.getPosition().y * TILE_SIZE; 
        
        FontMetrics fm = g2.getFontMetrics();
        int w = fm.stringWidth(text) + 20;
        int h = fm.getHeight() + 10;
        int bx = x - w/2;
        int by = y - h - 10;
        
        g2.setColor(Color.WHITE);
        g2.fillRoundRect(bx, by, w, h, 10, 10);
        g2.setColor(Color.BLACK);
        g2.drawRoundRect(bx, by, w, h, 10, 10);
        
        int[] tx = {x, x-5, x+5};
        int[] ty = {y, by+h, by+h};
        g2.setColor(Color.WHITE);
        g2.fillPolygon(tx, ty, 3);
        g2.setColor(Color.BLACK);
        g2.drawLine(x, y, x-5, by+h);
        g2.drawLine(x, y, x+5, by+h);
        
        g2.setColor(Color.RED);
        g2.drawString(text, bx + 10, by + h - 8);
    }

    private void drawEntity(Graphics2D g2, Entity e) {
        int px = e.getPosition().x * TILE_SIZE;
        int py = e.getPosition().y * TILE_SIZE;

        if (e instanceof TargetZone) {
            g2.setColor(new Color(0, 255, 0, 100)); // Glowy Green
            g2.fillOval(px + 2, py + 2, TILE_SIZE - 4, TILE_SIZE - 4);
            g2.setColor(Color.WHITE);
            g2.setStroke(new BasicStroke(2));
            g2.drawOval(px + 5, py + 5, TILE_SIZE - 10, TILE_SIZE - 10);
            g2.drawString("WIN", px+10, py+25);
            g2.setStroke(new BasicStroke(1));
        }
        else if (e instanceof Trigger) {
            Trigger t = (Trigger) e;
            g2.setColor(Color.GRAY);
            g2.fillRect(px + 5, py + 5, TILE_SIZE - 10, TILE_SIZE - 10);
            g2.setColor(t.isPressed() ? new Color(200, 0, 0) : Color.RED);
            g2.fillOval(px + 10, py + 10, TILE_SIZE - 20, TILE_SIZE - 20);
        } 
        else if (e instanceof Obstacle) {
            g2.setColor(new Color(50, 50, 50));
            g2.fillRect(px + 5, py + 5, TILE_SIZE - 10, TILE_SIZE - 10);
            g2.setColor(Color.YELLOW);
            g2.drawLine(px + 5, py + 5, px + TILE_SIZE - 5, py + TILE_SIZE - 5);
            g2.drawLine(px + TILE_SIZE - 5, py + 5, px + 5, py + TILE_SIZE - 5);
            g2.drawRect(px + 5, py + 5, TILE_SIZE - 10, TILE_SIZE - 10);
        }
        else if (e instanceof Bridge) {
            Bridge b = (Bridge) e;
            if (b.isActive()) {
                g2.setColor(new Color(210, 105, 30)); 
                g2.fillRect(px, py, TILE_SIZE, TILE_SIZE);
                g2.setColor(new Color(139, 69, 19));
                g2.drawRect(px, py, TILE_SIZE, TILE_SIZE);
            } else {
                g2.setColor(new Color(255, 255, 255, 50));
                g2.drawRect(px + 5, py + 5, TILE_SIZE - 10, TILE_SIZE - 10);
            }
        } 
        else if (e instanceof Terminal) {
            g2.setColor(Color.DARK_GRAY);
            g2.fillRect(px + 2, py + 2, TILE_SIZE - 4, TILE_SIZE - 4);
            g2.setColor(Color.GREEN);
            g2.setFont(new Font("Monospaced", Font.BOLD, 12));
            g2.drawString(">_", px+8, py+25);
        }
        else if (e instanceof Block) {
            boolean isHeld = false;
            if (world.getPlayer() != null && world.getPlayer().getHeldBlock() == e) isHeld = true;
            for(Entity ent : world.getEntities()) {
                if (ent instanceof Robot && ((Robot)ent).getHeldBlock() == e) isHeld = true;
            }
            if (!isHeld) {
                g2.setColor(Color.CYAN);
                g2.fillRoundRect(px + 8, py + 8, TILE_SIZE - 16, TILE_SIZE - 16, 10, 10);
                g2.setColor(Color.WHITE);
                g2.drawRoundRect(px + 8, py + 8, TILE_SIZE - 16, TILE_SIZE - 16, 10, 10);
            }
        } 
        else if (e instanceof Agent) { 
            if (e instanceof Robot) {
                g2.setColor(new Color(220, 20, 60)); 
            } else {
                g2.setColor(new Color(30, 144, 255)); 
            }
            g2.fillOval(px + 6, py + 6, TILE_SIZE - 12, TILE_SIZE - 12);
            
            Vector2 facing = ((Agent)e).getFacing();
            int cx = px + TILE_SIZE/2;
            int cy = py + TILE_SIZE/2;
            g2.setColor(Color.WHITE);
            g2.fillOval(cx + facing.x * 10 - 2, cy + facing.y * 10 - 2, 4, 4);

            if (((Agent)e).isCarrying()) {
                g2.setColor(Color.CYAN);
                g2.fillRect(px + 12, py - 5, 16, 16); 
                g2.setColor(Color.BLACK);
                g2.drawRect(px + 12, py - 5, 16, 16);
            }
        }
    }

    private void drawManhattanWire(Graphics2D g2, Vector2 start, Vector2 end, boolean active) {
        g2.setColor(active ? Color.ORANGE : new Color(100, 100, 100));
        int startX = start.x * TILE_SIZE + TILE_SIZE / 2;
        int startY = start.y * TILE_SIZE + TILE_SIZE / 2;
        int endX = end.x * TILE_SIZE + TILE_SIZE / 2;
        int endY = end.y * TILE_SIZE + TILE_SIZE / 2;

        g2.drawLine(startX, startY, endX, startY);
        g2.drawLine(endX, startY, endX, endY);
        int r = 4;
        g2.fillOval(startX-r, startY-r, r*2, r*2);
        g2.fillOval(endX-r, endY-r, r*2, r*2);
        g2.fillOval(endX-r, startY-r, r*2, r*2);
    }

    private Entity findEntity(int id) {
        for(Entity e : world.getEntities()) {
            if(e.getId() == id) return e;
        }
        return null;
    }
}