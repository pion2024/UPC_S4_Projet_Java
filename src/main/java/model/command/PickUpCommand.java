package model.command;
import model.entity.Entity;

public class PickUpCommand extends Command {
    public PickUpCommand(Entity target) {
        super(target);
    }
}
