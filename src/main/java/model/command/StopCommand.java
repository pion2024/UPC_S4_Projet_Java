package model.command;

// Représente le symbole de fin d'une liste de commandes
// car on utilise pas les queque mais les tableaux, 
// on a besoin d'un symbole de fin pour savoir quand arrêter l'exécution
public class StopCommand extends Command {
    public StopCommand() {
        super();
    }
}