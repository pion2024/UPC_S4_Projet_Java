package model.entity;

// sol classique 
public class Ground extends Items{

    public Ground(int id,int x, int y){
        super(id, x, y, true);
    }

    public void onSteppedOn(Entity stepper){
        // ne fais rien 
    }
}