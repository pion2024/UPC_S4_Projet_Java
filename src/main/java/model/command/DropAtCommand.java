package model.command;
import model.Entity;

public class DropAtCommand extends Command {
    public DropAtCommand(Entity target) {
        super(target);
    }
}
