package com.starstuff.view;

import com.starstuff.model.*;
import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class TerminalPanel extends JPanel {
    private GameWorld world;
    private JPanel commandListPanel; 
    private List<CommandUI> addedCommands = new ArrayList<>();

    public TerminalPanel(GameWorld world) {
        this.world = world;
        setLayout(new BorderLayout());
        setPreferredSize(new Dimension(300, 600));
        setBorder(BorderFactory.createTitledBorder("Terminal Interface"));

        // Left: Palette
        JPanel palette = new JPanel();
        palette.setLayout(new GridLayout(4, 1, 5, 5));
        
        JButton btnGoTo = new JButton("Add: Go To");
        btnGoTo.addActionListener(e -> addCommand(Command.Type.GO_TO));
        
        JButton btnPick = new JButton("Add: Pick Up");
        btnPick.addActionListener(e -> addCommand(Command.Type.PICK_UP));
        
        JButton btnDrop = new JButton("Add: Drop At");
        btnDrop.addActionListener(e -> addCommand(Command.Type.DROP_AT));

        JButton btnRun = new JButton(">>> UPLOAD & RUN <<<");
        btnRun.setBackground(Color.GREEN);
        btnRun.addActionListener(e -> runProgram());

        palette.add(btnGoTo);
        palette.add(btnPick);
        palette.add(btnDrop);
        palette.add(btnRun);

        // Right: Stack
        commandListPanel = new JPanel();
        commandListPanel.setLayout(new BoxLayout(commandListPanel, BoxLayout.Y_AXIS));
        JScrollPane scroll = new JScrollPane(commandListPanel);

        JSplitPane split = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, palette, scroll);
        split.setDividerLocation(120);
        add(split, BorderLayout.CENTER);
    }

    private void addCommand(Command.Type type) {
        Command cmd = new Command(type);
        CommandUI ui = new CommandUI(cmd, world, this);
        addedCommands.add(ui);
        commandListPanel.add(ui);
        commandListPanel.revalidate();
        commandListPanel.repaint();
        updatePreview(); // Trigger preview update
    }

    public void removeCommandUI(CommandUI ui) {
        addedCommands.remove(ui);
        commandListPanel.remove(ui);
        commandListPanel.revalidate();
        commandListPanel.repaint();
        updatePreview(); // Trigger preview update
    }
    
    public void updatePreview() {
        List<Command> cmds = new ArrayList<>();
        for (CommandUI ui : addedCommands) {
            cmds.add(ui.getCommand());
        }
        world.updatePreview(cmds);
    }

    private void runProgram() {
        List<Command> finalCmds = new ArrayList<>();
        for (CommandUI ui : addedCommands) {
            finalCmds.add(ui.getCommand());
        }
        world.startRobotExecution(finalCmds);
        this.setVisible(false);
    }
}

class CommandUI extends JPanel {
    private Command command;
    private JButton paramBtn;
    private TerminalPanel parentPanel;

    public CommandUI(Command cmd, GameWorld world, TerminalPanel parent) {
        this.command = cmd;
        this.parentPanel = parent;
        
        setLayout(new FlowLayout(FlowLayout.LEFT));
        setBorder(BorderFactory.createLineBorder(Color.GRAY));
        
        JLabel lblType = new JLabel(cmd.getType().name());
        add(lblType);

        paramBtn = new JButton("[Select Target]");
        paramBtn.addActionListener(e -> {
            Entity selected = showTargetDialog(world);
            if (selected != null) {
                cmd.setTarget(selected);
                paramBtn.setText(selected.getType() + " " + selected.getId());
                parentPanel.updatePreview(); // Update preview when param changes
            }
        });
        add(paramBtn);
        
        JButton btnDel = new JButton("X");
        btnDel.setMargin(new Insets(0,2,0,2));
        btnDel.setForeground(Color.RED);
        btnDel.addActionListener(e -> parent.removeCommandUI(this));
        add(btnDel);
    }

    public Command getCommand() { return command; }

    private Entity showTargetDialog(GameWorld world) {
        List<Entity> options = new ArrayList<>();
        for (Entity e : world.getEntities()) {
            if (e instanceof Block || e instanceof Trigger) {
                options.add(e);
            }
        }
        String[] names = new String[options.size()];
        for (int i=0; i<options.size(); i++) {
            Entity e = options.get(i);
            names[i] = e.getType() + " (ID:" + e.getId() + ") at " + e.getPosition();
        }
        String choice = (String) JOptionPane.showInputDialog(
            this, "Select Target Position:", "Parameter",
            JOptionPane.QUESTION_MESSAGE, null, names, null);
        if (choice != null) {
            for (int i=0; i<names.length; i++) {
                if (names[i].equals(choice)) return options.get(i);
            }
        }
        return null;
    }
}