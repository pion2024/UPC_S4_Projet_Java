package model.command;

import model.entity.Items;

public class GoToCommand extends Command {
    public GoToCommand(Items target) {
        super(target);
    }
}