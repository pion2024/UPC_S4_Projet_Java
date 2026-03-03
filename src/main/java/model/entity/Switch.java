package model.entity;
import model.physic.*;

public class Switch extends Items{
    private boolean isPressed; // état actuelle


    public Switch(int id, Position pos, boolean traversable, Activatable target, Direction dir){
        super(id, pos, traversable, CellType.SWITCH, dir);
        this.isPressed = false;
    }

    public void updateStatus(boolean entityEnter){
        this.isPressed = entityEnter;
    }

    public boolean getIsPressed(){
        return this.isPressed;
    }

    @Override
    public void onSteppedOn(MovableEntity stepper){
        /* c'est gameState qui s'occupe de vérifier si'il y a une entité et de marquer l'interrupteur comme activé  */
    }

}
