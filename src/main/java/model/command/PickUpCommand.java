package model.command;
import model.entity.MovableEntity;

public class PickUpCommand extends Command {
    public PickUpCommand(MovableEntity target) {
        super(target);
    }
}
