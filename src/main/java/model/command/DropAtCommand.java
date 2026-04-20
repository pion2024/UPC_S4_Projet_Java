package model.command;

import model.entity.Items;

public class DropAtCommand extends Command {
    public DropAtCommand(Items target) {
        super(target);
    }
}