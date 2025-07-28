package org.millerM907.hw02_stable_abstractions.space_battle;

import java.util.Map;

public class SpaceShip implements ConfigurableObject {

    private final Map<String, Object> properties;

    public SpaceShip(Map<String, Object> properties) {
        this.properties = properties;
    }

    @Override
    public Object getProperty(String property) {
        return properties.get(property);
    }

    @Override
    public void setProperty(String property, Object value) {
        properties.put(property, value);
    }
}
