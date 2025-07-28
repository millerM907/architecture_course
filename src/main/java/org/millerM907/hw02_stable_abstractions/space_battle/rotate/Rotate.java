package org.millerM907.hw02_stable_abstractions.space_battle.rotate;

public class Rotate {

    private final Rotatable rotatable;

    public Rotate(Rotatable rotatable) {
        this.rotatable = rotatable;
    }

    public void execute() {
        rotatable.setDirection((rotatable.getDirection() + rotatable.getAngularVelocity()) % rotatable.getDirectionsNumber());
    }
}
