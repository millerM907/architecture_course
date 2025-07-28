package org.millerM907.hw02_stable_abstractions.space_battle.move;

import org.millerM907.hw02_stable_abstractions.space_battle.core.Point;
import org.millerM907.hw02_stable_abstractions.space_battle.core.Vector;

public interface Movable {
    Point getLocation();

    Vector getVelocity();

    void setLocation(Point newValue);
}
