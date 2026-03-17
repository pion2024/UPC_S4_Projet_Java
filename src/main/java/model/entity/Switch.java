package model.entity;
import model.physic.Direction;
import model.physic.Position;

public class Switch extends Items{
    private boolean isPressed; // état actuelle
    protected Position pos;

    public Switch(Position pos, boolean traversable, Activatable target, Direction dir){
        super(traversable, CellType.SWITCH, dir);
        this.isPressed = false;
        this.pos = pos;
    }

    public void updateStatus(boolean entityEnter){
        this.isPressed = entityEnter;
    }

    public boolean getIsPressed(){
        return this.isPressed;
    }

    public Position getPos() {
        return this.pos;
    }

    @Override
    public void onSteppedOn(MovableEntity stepper){
        /* c'est gameState qui s'occupe de vérifier si'il y a une entité et de marquer l'interrupteur comme activé  */
    }

}
