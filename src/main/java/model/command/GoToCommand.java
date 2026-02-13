package model.command;
import model.Entity;

public class GoToCommand extends Command {
    public GoToCommand(Entity target) {
        super(target);
    }
}

