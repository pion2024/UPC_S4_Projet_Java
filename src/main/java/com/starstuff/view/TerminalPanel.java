package com.starstuff.view;

import com.starstuff.model.*;
import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class TerminalPanel extends JPanel {
    private GameWorld world;
    private JPanel commandListPanel; 
    private List<CommandUI> addedCommands = new ArrayList<>();
    private JFrame parentFrame; 

    public TerminalPanel(GameWorld world) {
        this.world = world;
        setLayout(new BorderLayout());
        setPreferredSize(new Dimension(320, 600));
        setBorder(BorderFactory.createTitledBorder("Terminal Interface"));

        // --- Top Bar: Hide & Speed ---
        JPanel topBar = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        
        // Step Delay Spinner
        JLabel lblStepDelay = new JLabel("Step Delay (ms):");
        
        // Model: Initial 500, Min 50, Max 5000, Step 50
        SpinnerNumberModel stepDelayModel = new SpinnerNumberModel(500, 50, 5000, 50);
        JSpinner stepDelay = new JSpinner(stepDelayModel);
        stepDelay.setPreferredSize(new Dimension(70, 24));
        
        // Add Listener with Safe Casting
        stepDelay.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                // Use Number to be safe (handles Integer, Long, etc.)
                Number val = (Number) stepDelay.getValue();
                world.setRobotStepDelay(val.intValue());
                // Force focus back to main frame so WASD works immediately after clicking spinner
                if (parentFrame != null) parentFrame.requestFocusInWindow();
            }
        });

        topBar.add(lblStepDelay);
        topBar.add(stepDelay);

        // Hide Button
        JButton btnHide = new JButton("Hide >>");
        btnHide.setMargin(new Insets(2,5,2,5));
        btnHide.addActionListener(e -> setVisible(false));
        topBar.add(btnHide);

        add(topBar, BorderLayout.NORTH);

        // --- Left: Palette ---
        JPanel palette = new JPanel();
        palette.setLayout(new GridLayout(5, 1, 5, 5)); 
        
        JButton btnGoTo = new JButton("Add: Go To");
        btnGoTo.addActionListener(e -> addCommand(Command.Type.GO_TO));
        
        JButton btnPick = new JButton("Add: Pick Up");
        btnPick.addActionListener(e -> addCommand(Command.Type.PICK_UP));
        
        JButton btnDrop = new JButton("Add: Drop At");
        btnDrop.addActionListener(e -> addCommand(Command.Type.DROP_AT));

        JButton btnRun = new JButton(">>> RUN <<<");
        btnRun.setBackground(Color.GREEN);
        btnRun.addActionListener(e -> runProgram());

        JButton btnReset = new JButton("RESET SYSTEM");
        btnReset.setBackground(new Color(255, 100, 100));
        btnReset.addActionListener(e -> resetSystem());

        palette.add(btnGoTo);
        palette.add(btnPick);
        palette.add(btnDrop);
        palette.add(btnRun);
        palette.add(btnReset);

        // --- Right: Stack ---
        commandListPanel = new JPanel();
        commandListPanel.setLayout(new BoxLayout(commandListPanel, BoxLayout.Y_AXIS));
        JScrollPane scroll = new JScrollPane(commandListPanel);

        JSplitPane split = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, palette, scroll);
        split.setDividerLocation(130);
        add(split, BorderLayout.CENTER);
    }
    
    public void setParentFrame(JFrame f) { this.parentFrame = f; }

    @Override
    public void setVisible(boolean aFlag) {
        super.setVisible(aFlag);
        if (parentFrame != null) parentFrame.pack();
    }

    private void addCommand(Command.Type type) {
        Command cmd = new Command(type);
        CommandUI ui = new CommandUI(cmd, world, this);
        addedCommands.add(ui);
        commandListPanel.add(ui);
        commandListPanel.revalidate();
        commandListPanel.repaint();
    }

    public void removeCommandUI(CommandUI ui) {
        addedCommands.remove(ui);
        commandListPanel.remove(ui);
        commandListPanel.revalidate();
        commandListPanel.repaint();
    }
    
    private void resetSystem() {
        world.resetRobot();
        addedCommands.clear();
        commandListPanel.removeAll();
        commandListPanel.revalidate();
        commandListPanel.repaint();
    }

    private void runProgram() {
        List<Command> finalCmds = new ArrayList<>();
        for (CommandUI ui : addedCommands) {
            finalCmds.add(ui.getCommand());
        }
        world.startRobotExecution(finalCmds);
        // Force focus back to game so player can move while robot runs
        if (parentFrame != null) parentFrame.requestFocusInWindow();
    }
}