package model.command;
import model.entity.Entity;
/**
 * Represents a command for the Robot.
 */
public class Command {         
    private Entity targetEntity;  
    public Command(Entity target) {
        setTarget(target);
    }
    
    public void setTarget(Entity e) {
        this.targetEntity = e;
    }
    public Entity getTarget() {
        return this.targetEntity;
    }

    // mode d'emploi pour les sous classess (i.e. DropAtCommand, PickUpCommand, GoToCommand) :
    // à la création de la (liste de ) commande(s), on peut faire :
    // List<Command> commandList = new ArrayList<>();
    // commandList.add(new DropAtCommand(targetEntity)); // quand le joueur ajoute une commande à la liste
    // commandList.remove(0);                           // quand le joueur supprime le paramètre d'une commande déjà dans la liste
    // commandList.get(0).setTarget(newTargetEntity); // quand le joueur modifie le paramètre d'une commande déjà dans la liste

    // à l'execution de la commande, on peut faire :
    // for (Command cmd : commandList) {
    //     if (cmd instanceof GoToCommand) { xxx }
    //     else if (cmd instanceof PickUpCommand) { xxx }
    //     else if (cmd instanceof DropAtCommand) { xxx }
    // regarder le code dans preview si besoin d'exemples. la seule difference est que 
    // ici on teste le type de la commande par le biais de l'instance de la classe, 
    // alors que dans preview on testait le type de la commande par une enum Type.
}