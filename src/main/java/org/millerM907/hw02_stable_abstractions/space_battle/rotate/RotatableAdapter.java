package org.millerM907.hw02_stable_abstractions.space_battle.rotate;

import org.millerM907.hw02_stable_abstractions.space_battle.ConfigurableObject;

public class RotatableAdapter implements Rotatable {

    private final ConfigurableObject configurableObject;

    public RotatableAdapter(ConfigurableObject configurableObject) {
        this.configurableObject = configurableObject;
    }

    @Override
    public int getDirection() {
        Object value = configurableObject.getProperty("direction");
        if (!(value instanceof Integer)) {
            throw new IllegalStateException("Location is missing or invalid");
        }
        return (Integer) value;
    }

    @Override
    public void setDirection(int newDirection) {
        configurableObject.setProperty("direction", newDirection);
    }

    @Override
    public int getAngularVelocity() {
        Object value = configurableObject.getProperty("angularVelocity");
        if (!(value instanceof Integer)) {
            throw new IllegalStateException("Location is missing or invalid");
        }
        return (Integer) value;
    }

    @Override
    public int getDirectionsNumber() {
        Object value = configurableObject.getProperty("directionsNumber");
        if (!(value instanceof Integer)) {
            throw new IllegalStateException("Location is missing or invalid");
        }
        return (Integer) value;
    }
}
