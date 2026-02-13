package model.command;
import model.Entity;

public class PickUpCommand extends Command {
    public PickUpCommand(Entity target) {
        super(target);
    }
}
