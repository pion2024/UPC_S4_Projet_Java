package model.command;
import model.entity.MovableEntity;

public class DropAtCommand extends Command {
    public DropAtCommand(MovableEntity target) {
        super(target);
    }
}
