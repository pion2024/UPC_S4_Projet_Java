package model.command;
import model.entity.Entity;

public class DropAtCommand extends Command {
    public DropAtCommand(Entity target) {
        super(target);
    }
}
