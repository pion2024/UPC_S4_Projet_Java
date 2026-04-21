package model.entity;

import java.util.ArrayList;
import java.util.List;

import model.command.Command;
import model.physic.Direction;


// la logique du terminal : stocker les commandes temporairement, les transmettre au robot lors de l'interaction, et les effacer après exécution
public class Terminal extends Items {
    
    // Liste des commandes stockées temporairement dans le terminal
    private List<Command> commands;

    public Terminal() {
        // Le joueur doit être à gauche du terminal pour l'activer (faire face à droite)
        super(false, CellType.TERMINAL, Direction.LEFT); 
        this.commands = new ArrayList<>();
    }

    // Récupérer la liste des commandes
    public List<Command> getCommands() {
        return commands;
    }

    // Vider la liste après exécution
    public void clearCommands() {
        commands.clear();
    }

    @Override
    public void onSteppedOn(MovableEntity stepper) {
    // Un terminal n'est pas traversable, donc cette méthode n'est normalement jamais appelée
    }
}