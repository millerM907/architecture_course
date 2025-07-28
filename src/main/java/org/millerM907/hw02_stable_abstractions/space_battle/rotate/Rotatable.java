package org.millerM907.hw02_stable_abstractions.space_battle.rotate;

public interface Rotatable {
    int getDirection();

    void setDirection(int newDirection);

    int getAngularVelocity();

    int getDirectionsNumber();
}
