package org.millerM907.hw02_stable_abstractions.space_battle.rotate;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.millerM907.hw02_stable_abstractions.space_battle.SpaceShip;
import org.millerM907.hw02_stable_abstractions.space_battle.ConfigurableObject;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class RotateTest {

    @Test
    @DisplayName("Direction is 3, angular velocity is 2, total directions are 8. After rotation, new direction is 5")
    void givenDirectionAndVelocity_whenRotated_thenNewDirectionIsCorrect() {
        Map<String, Object> properties = new HashMap<>();
        properties.put("direction", 3);
        properties.put("directionsNumber", 8);
        properties.put("angularVelocity", 2);

        ConfigurableObject ship = new SpaceShip(properties);
        Rotatable rotatable = new RotatableAdapter(ship);
        Rotate rotate = new Rotate(rotatable);


        rotate.execute();


        Integer expectedDirection = 5;
        Integer actualDirection = (Integer) ship.getProperty("direction");
        assertEquals(expectedDirection, actualDirection);
    }

    @Test
    @DisplayName("Direction property is missing")
    void givenNoDirection_whenRotated_thenThrowsException() {
        Map<String, Object> properties = new HashMap<>();
        properties.put("angularVelocity", 2);
        properties.put("directionsNumber", 8);

        ConfigurableObject ship = new SpaceShip(properties);
        Rotatable rotatable = new RotatableAdapter(ship);
        Rotate rotate = new Rotate(rotatable);


        assertThrows(IllegalStateException.class, rotate::execute);
    }

    @Test
    @DisplayName("Angular velocity property is missing")
    void givenNoAngularVelocity_whenRotated_thenThrowsException() {
        Map<String, Object> properties = new HashMap<>();
        properties.put("direction", 3);
        properties.put("directionsNumber", 8);

        ConfigurableObject ship = new SpaceShip(properties);
        Rotatable rotatable = new RotatableAdapter(ship);
        Rotate rotate = new Rotate(rotatable);


        assertThrows(IllegalStateException.class, rotate::execute);
    }

    @Test
    @DisplayName("Total directions property is missing")
    void givenNoDirectionsNumber_whenRotated_thenThrowsException() {
        Map<String, Object> properties = new HashMap<>();
        properties.put("direction", 3);
        properties.put("angularVelocity", 2);

        ConfigurableObject ship = new SpaceShip(properties);
        Rotatable rotatable = new RotatableAdapter(ship);
        Rotate rotate = new Rotate(rotatable);


        assertThrows(IllegalStateException.class, rotate::execute);
    }

    @Test
    @DisplayName("Failed to set new direction")
    void givenCannotSetDirection_whenRotated_thenThrowsException() {
        Map<String, Object> properties = new HashMap<>();
        properties.put("direction", 3);
        properties.put("angularVelocity", 2);
        properties.put("directionsNumber", 8);

        ConfigurableObject ship = new SpaceShip(properties) {
            @Override
            public void setProperty(String property, Object value) {
                throw new UnsupportedOperationException("Cannot modify property");
            }
        };

        Rotatable rotatable = new RotatableAdapter(ship);
        Rotate rotate = new Rotate(rotatable);


        assertThrows(UnsupportedOperationException.class, rotate::execute);
    }
}
