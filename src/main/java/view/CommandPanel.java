package view;

import java.awt.*;
import java.util.List;
import javax.swing.*;

import model.GameModel;
import model.command.*;
import model.entity.Robot;
import model.entity.*;

// sous-fenêtre pour configurer les commandes du robot
public class CommandPanel extends JPanel {
    private GameModel model;
    private Terminal terminal;
    private Robot robot;
    private Runnable onClose;

    private JComboBox<String> actionCombo;
    private JComboBox<TargetWrapper> targetCombo;
    private JPanel listPanel;

    public CommandPanel(GameModel model, Terminal terminal, Robot robot, Runnable onClose) {
        this.model = model;
        this.terminal = terminal;
        this.robot = robot;
        this.onClose = onClose;

        setLayout(new BorderLayout());
        setPreferredSize(new Dimension(300, 600));
        setBackground(new Color(40, 50, 70)); // Fond bleu foncé

        initUI();
    }

    private void initUI() {
        removeAll();
        
        // Titre
        JLabel title = new JLabel("Terminal de Commande");
        title.setForeground(Color.WHITE);
        title.setFont(new Font("Arial", Font.BOLD, 18));
        title.setHorizontalAlignment(SwingConstants.CENTER);
        title.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 0));
        add(title, BorderLayout.NORTH);

        // Formulaire de sélection + Liste des commandes
        JPanel centerPanel = new JPanel();
        centerPanel.setLayout(new BoxLayout(centerPanel, BoxLayout.Y_AXIS));
        centerPanel.setOpaque(false);

        // Formulaire d'ajout ---
        JPanel formPanel = new JPanel(new GridLayout(3, 1, 5, 5));
        formPanel.setOpaque(false);
        formPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        actionCombo = new JComboBox<>(new String[]{"Go To", "Pick Up", "Drop At"});
        targetCombo = new JComboBox<>();
        updateTargetCombo(); // Met à jour les cibles en fonction de l'action choisie

        actionCombo.addActionListener(e -> updateTargetCombo());

        JButton btnAdd = new JButton("Ajouter");
        btnAdd.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnAdd.addActionListener(e -> addCommand());

        formPanel.add(actionCombo);
        formPanel.add(targetCombo);
        formPanel.add(btnAdd);
        
        centerPanel.add(formPanel);
        centerPanel.add(Box.createRigidArea(new Dimension(0, 10)));

        // --- Liste des commandes déjà ajoutées ---
        listPanel = new JPanel();
        listPanel.setLayout(new BoxLayout(listPanel, BoxLayout.Y_AXIS));
        listPanel.setOpaque(false);
        refreshList(); // Charge les commandes existantes du terminal
        
        JScrollPane scroll = new JScrollPane(listPanel);
        scroll.setOpaque(false);
        scroll.getViewport().setOpaque(false);
        scroll.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(Color.WHITE), "Programme actuel", 
                0, 0, new Font("Arial", Font.PLAIN, 12), Color.WHITE));
        
        centerPanel.add(scroll);
        add(centerPanel, BorderLayout.CENTER);

        // Bas : Boutons Exécuter et Fermer
        JPanel bottomPanel = new JPanel(new FlowLayout());
        bottomPanel.setOpaque(false);

        JButton btnExec = new JButton("Exécuter");
        btnExec.setBackground(new Color(50, 150, 50));
        btnExec.setForeground(Color.WHITE);
        btnExec.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnExec.addActionListener(e -> executeCommands());

        JButton btnCancel = new JButton("Fermer");
        btnCancel.setBackground(new Color(150, 50, 50));
        btnCancel.setForeground(Color.WHITE);
        btnCancel.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnCancel.addActionListener(e -> onClose.run());

        bottomPanel.add(btnExec);
        bottomPanel.add(btnCancel);
        add(bottomPanel, BorderLayout.SOUTH);
    }

    // Mise à jour de la liste déroulante des cibles
    private void updateTargetCombo() {
        targetCombo.removeAllItems();
        String action = (String) actionCombo.getSelectedItem();
        
        if ("Pick Up".equals(action)) {
            // Pour PickUp, on cherche les Blocs
            for (MovableEntity e : model.getBoard().getMovableEntities()) {
                if (e instanceof Block) {
                    targetCombo.addItem(new TargetWrapper(e, "Block (" + e.getPos().getI() + "," + e.getPos().getJ() + ")"));
                }
            }
        } else {
            // Pour GoTo et DropAt, on cherche les Switches
            for (int i = 0; i < model.getBoard().getNbLines(); i++) {
                for (int j = 0; j < model.getBoard().getNbColumns(); j++) {
                    Items item = model.getBoard().getItemAt(i, j);
                    if (item instanceof Switch) {
                        targetCombo.addItem(new TargetWrapper(item, "Switch (" + i + "," + j + ")"));
                    }
                }
            }
        }
    }

    // Ajoute la commande choisie à la liste du Terminal
    private void addCommand() {
        if (terminal.getCommands().size() >= 9) { // 9 pour laisser la place au StopCommand
            JOptionPane.showMessageDialog(this, "Maximum 9 commandes autorisées !");
            return;
        }

        String action = (String) actionCombo.getSelectedItem();
        TargetWrapper wrapper = (TargetWrapper) targetCombo.getSelectedItem();
        
        if (wrapper == null) return;

        Command cmd = null;
        if ("Go To".equals(action)) {
            cmd = new GoToCommand((Items) wrapper.obj);
        } else if ("Pick Up".equals(action)) {
            cmd = new PickUpCommand((MovableEntity) wrapper.obj);
        } else if ("Drop At".equals(action)) {
            cmd = new DropAtCommand((Items) wrapper.obj);
        }

        if (cmd != null) {
            terminal.getCommands().add(cmd);
            refreshList(); // Actualise l'affichage
        }
    }

    // Actualise la zone d'affichage des commandes avec bouton de suppression
    private void refreshList() {
        listPanel.removeAll();
        List<Command> cmds = terminal.getCommands();
        for (int i = 0; i < cmds.size(); i++) {
            Command c = cmds.get(i);
            JPanel row = new JPanel(new BorderLayout());
            row.setMaximumSize(new Dimension(280, 35));
            row.setBorder(BorderFactory.createEmptyBorder(2, 5, 2, 5));
            row.setOpaque(false);
            
            String text = (i+1) + ". ";
            if (c instanceof GoToCommand) text += "Go To";
            else if (c instanceof PickUpCommand) text += "Pick Up";
            else if (c instanceof DropAtCommand) text += "Drop At";
            
            JLabel lbl = new JLabel(text);
            lbl.setForeground(Color.WHITE);
            
            JButton btnDel = new JButton("X");
            btnDel.setMargin(new Insets(0, 2, 0, 2));
            btnDel.setBackground(Color.RED);
            btnDel.setForeground(Color.WHITE);
            
            final int index = i;
            btnDel.addActionListener(e -> {
                terminal.getCommands().remove(index);
                refreshList();
            });

            row.add(lbl, BorderLayout.CENTER);
            row.add(btnDel, BorderLayout.EAST);
            listPanel.add(row);
        }
        listPanel.revalidate();
        listPanel.repaint();
    }

    // Transfère les commandes au robot, et déclenche l'exécution
    private void executeCommands() {
        if (robot != null && !terminal.getCommands().isEmpty()) {
            // On vide le programme actuel du robot pour le remplacer
            robot.stopExecution(); 
            for (Command c : terminal.getCommands()) {
                robot.addCommand(c);
            }
            // startExecution ajoute automatiquement le StopCommand à la fin !
            robot.startExecution();
        }
        terminal.clearCommands(); // On vide le terminal
        onClose.run(); // Ferme le panneau
    }

    // Wrapper pour afficher un joli nom dans la liste déroulante
    private static class TargetWrapper {
        Object obj;
        String name;
        TargetWrapper(Object obj, String name) {
            this.obj = obj;
            this.name = name;
        }
        @Override
        public String toString() { return name; }
    }
}