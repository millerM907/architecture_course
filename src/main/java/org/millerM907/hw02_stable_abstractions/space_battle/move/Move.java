package org.millerM907.hw02_stable_abstractions.space_battle.move;

public class Move {

    private final Movable movable;

    public Move(Movable movable) {
        this.movable = movable;
    }

    public void execute() {
        movable.setLocation(movable.getLocation().plus(movable.getVelocity()));
    }
}
