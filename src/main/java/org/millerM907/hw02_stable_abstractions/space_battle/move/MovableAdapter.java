package org.millerM907.hw02_stable_abstractions.space_battle.move;

import org.millerM907.hw02_stable_abstractions.space_battle.ConfigurableObject;
import org.millerM907.hw02_stable_abstractions.space_battle.core.Point;
import org.millerM907.hw02_stable_abstractions.space_battle.core.Vector;

public class MovableAdapter implements Movable {

    private final ConfigurableObject configurableObject;

    public MovableAdapter(ConfigurableObject configurableObject) {
        this.configurableObject = configurableObject;
    }

    @Override
    public Point getLocation() {
        Object value = configurableObject.getProperty("location");
        if (!(value instanceof Point)) {
            throw new IllegalStateException("Location is missing or invalid");
        }
        return (Point) value;
    }

    @Override
    public Vector getVelocity() {
        Object value = configurableObject.getProperty("velocity");
        if (!(value instanceof Vector)) {
            throw new IllegalStateException("Velocity is missing or invalid");
        }
        return (Vector) value;
    }

    @Override
    public void setLocation(Point newValue) {
        configurableObject.setProperty("location", newValue);
    }
}
