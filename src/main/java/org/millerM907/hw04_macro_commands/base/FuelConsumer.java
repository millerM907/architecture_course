package org.millerM907.hw04_macro_commands.base;

/**
 * Represents an entity that consumes fuel.
 * <p>
 * Provides methods to get and set the current fuel level,
 * as well as to retrieve the rate at which fuel is consumed.
 * </p>
 */
public interface FuelConsumer {

    /**
     * Returns the current fuel level.
     *
     * @return the amount of fuel currently available
     */
    int getFuelLevel();

    /**
     * Sets the current fuel level.
     *
     * @param level the new fuel level to set
     */
    void setFuelLevel(int level);

    /**
     * Returns the rate at which fuel is consumed.
     *
     * @return the fuel consumption rate
     */
    int getFuelConsumptionRate();
}
