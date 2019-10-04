package es.us.isa.restmutator.util;

import static es.us.isa.restmutator.util.PropertyManager.readProperty;

public class Utilities {

    public static float assignWeight(String type) {
        switch (type) {
            case "Long":
                return Float.parseFloat(readProperty("operator.value.long.weight.changeType"));
            case "Double":
                return Float.parseFloat(readProperty("operator.value.double.weight.changeType"));
            case "Boolean":
                return Float.parseFloat(readProperty("operator.value.boolean.weight.changeType"));
            case "String":
                return Float.parseFloat(readProperty("operator.value.string.weight.changeType"));
            default:
                throw new IllegalArgumentException("Wrong class: "+ type +". The class passed to the " +
                        "constructor must be Long, Double, Boolean or String.");
        }
    }
}
