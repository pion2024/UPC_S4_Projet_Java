package com.starstuff.view;

import com.starstuff.model.Command;
import com.starstuff.model.Entity;
import com.starstuff.model.Block;
import com.starstuff.model.Trigger;
import com.starstuff.model.GameWorld;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class CommandUI extends JPanel {
    private Command command;
    private JButton paramBtn;

    public CommandUI(Command cmd, GameWorld world, TerminalPanel parent) {
        this.command = cmd;
        setLayout(new FlowLayout(FlowLayout.LEFT));
        setBorder(BorderFactory.createLineBorder(Color.GRAY));
        
        JLabel lblType = new JLabel(cmd.getType().name());
        add(lblType);

        // Initial text for the button
        String btnText = (cmd.getTarget() != null) ? 
            cmd.getTarget().getType() + " " + cmd.getTarget().getId() : 
            "[Select Target]";
        paramBtn = new JButton(btnText);

        paramBtn.addActionListener(e -> {
            Entity selected = showTargetDialog(world);
            if (selected != null) {
                cmd.setTarget(selected);
                paramBtn.setText(selected.getType() + " " + selected.getId());
                // Since we removed the real-time preview requirement for Terminal, 
                // we don't need to trigger parent update here.
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
        // Filter entities that can be targets (Block or Trigger)
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