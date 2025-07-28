package org.millerM907.hw02_stable_abstractions.space_battle;

/**
 * An interface representing an object whose properties can be configured dynamically.
 * <p>
 * Allows setting (adding or updating) and retrieving properties at runtime
 * without changing the object's structure or type.
 */
public interface ConfigurableObject {

    /**
     * Retrieves the value of a property by its name.
     *
     * @param property the name of the property
     * @return the value of the property, or {@code null} if the property is not set
     */
    Object getProperty(String property);

    /**
     * Sets or adds a property with the given name and value.
     *
     * @param property the name of the property
     * @param value    the value to assign to the property
     */
    void setProperty(String property, Object value);
}
