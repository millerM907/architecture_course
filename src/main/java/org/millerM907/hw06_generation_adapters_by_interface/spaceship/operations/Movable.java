package org.millerM907.hw06_generation_adapters_by_interface.spaceship.operations;

import org.millerM907.hw02_stable_abstractions.space_battle.core.Vector;

public interface Movable {
    Vector getPosition();
    void setPosition(Vector newValue);
    Vector getVelocity();
}
