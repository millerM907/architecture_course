package org.millerM907.hw02_stable_abstractions.space_battle.move;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.millerM907.hw02_stable_abstractions.space_battle.SpaceShip;
import org.millerM907.hw02_stable_abstractions.space_battle.ConfigurableObject;
import org.millerM907.hw02_stable_abstractions.space_battle.core.Point;
import org.millerM907.hw02_stable_abstractions.space_battle.core.Vector;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class MoveTest {

    @Test
    @DisplayName("Object at point (12,5) with velocity (-7,3)")
    void givenObjectAtPositionAndVelocity_whenMoved_thenNewLocationIsCorrect() {
        Map<String, Object> properties = new HashMap<>();
        properties.put("location", new Point(12, 5));
        properties.put("velocity", new Vector(-7, 3));

        ConfigurableObject ship = new SpaceShip(properties);
        Movable movable = new MovableAdapter(ship);
        Move move = new Move(movable);


        move.execute();


        Point expectedPosition = new Point(5, 8);
        Point actualPosition = (Point) ship.getProperty("location");
        assertEquals(expectedPosition, actualPosition);
    }

    @Test
    @DisplayName("Object has no location set")
    void givenMissingLocation_whenMoved_thenThrowException() {
        Map<String, Object> props = new HashMap<>();
        props.put("velocity", new Vector(1, 2));

        ConfigurableObject ship = new SpaceShip(props);
        Movable movable = new MovableAdapter(ship);
        Move move = new Move(movable);

        assertThrows(IllegalStateException.class, move::execute);
    }

    @Test
    @DisplayName("Object has no velocity set")
    void givenMissingVelocity_whenMoved_thenThrowException() {
        Map<String, Object> props = new HashMap<>();
        props.put("location", new Point(0, 0));

        ConfigurableObject ship = new SpaceShip(props);
        Movable movable = new MovableAdapter(ship);
        Move move = new Move(movable);


        assertThrows(IllegalStateException.class, move::execute);
    }

    @Test
    @DisplayName("Object does not allow location update")
    void givenRestrictedSetProperty_whenMoved_thenThrowException() {
        Map<String, Object> props = new HashMap<>();
        props.put("location", new Point(0, 0));
        props.put("velocity", new Vector(1, 1));

        ConfigurableObject configurableObject = new SpaceShip(props) {
            @Override
            public void setProperty(String property, Object value) {
                throw new UnsupportedOperationException("Cannot modify property");
            }
        };
        Movable movable = new MovableAdapter(configurableObject);
        Move move = new Move(movable);


        assertThrows(UnsupportedOperationException.class, move::execute);
    }
}
