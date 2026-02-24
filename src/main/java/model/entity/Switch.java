package model.entity;
import model.physic.Position;

public class Switch extends Interactions{
    
    //private Activatable target; // l'objet que switch contrôle
    private boolean isPressed; // état actuelle


    public Switch(Position pos, boolean traversable, CellType targetType, int targetId){
        super(pos, traversable, CellType.SWITCH, targetType, targetId);
        //this.target = target;
        this.isPressed = false;
    }

    // @Override
    // public void onSteppedOn(MovableEntity stepper){
    //     if(target != null){
    //         target.toggle(); // active le pont par exemple auquelle il est relié avec l'id, quand quelque chose est sur switch
    //     }
    // }

    // public void onSteppedOn(MovableEntity stepper){
    //     if(isConnected(type, id)){
    //         target.toggle(); // active le pont par exemple auquelle il est relié avec l'id, quand quelque chose est sur switch
    //     }
    // }

    public void onEntityEnter(MovableEntity stepper) {
        // Quand on entre, si ce n'était pas déjà pressé (évite les doubles clics)
        if (!isPressed) {
            isPressed = true;
            notifyTarget(); 
        }
    }

    public void onEntityLeave(MovableEntity stepper) {
        // Dès qu'on quitte la dalle, l'interrupteur remonte
        if (isPressed) {
            isPressed = false;
            notifyTarget(); // On re-bascule la cible à son état d'origine
        }
    }

    private void notifyTarget() {
        // L'utilité est ici : on vérifie que la cible existe bien
        if (target != null) {
            target.toggle();
        }
    }
}
