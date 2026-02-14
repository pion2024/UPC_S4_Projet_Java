package model.command;
import model.entity.Entity;

public class GoToCommand extends Command {
    public GoToCommand(Entity target) {
        super(target);
    }
}

