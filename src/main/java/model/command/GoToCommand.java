package model.command;

import model.entity.MovableEntity;

public class GoToCommand extends Command {
    public GoToCommand(MovableEntity target) {
        super(target);
    }
}

