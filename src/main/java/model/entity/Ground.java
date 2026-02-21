package model.entity;
import physic.Position;

// sol classique 
public class Ground extends Items{

    public Ground(int id, Position pos){
        super(id, pos, true);
    }

    public void onSteppedOn(Entity stepper){
        // ne fais rien 
    }
}