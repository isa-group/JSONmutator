package es.us.isa.restmutator.util;

import static es.us.isa.restmutator.util.PropertyManager.readProperty;

public class Utilities {

    public static float assignWeight(String type, String mutationOperator) {
        switch (type) {
            case "Long":
                return Float.parseFloat(readProperty("operator.value.long.weight." + mutationOperator));
            case "Double":
                return Float.parseFloat(readProperty("operator.value.double.weight." + mutationOperator));
            case "Boolean":
                return Float.parseFloat(readProperty("operator.value.boolean.weight." + mutationOperator));
            case "String":
                return Float.parseFloat(readProperty("operator.value.string.weight." + mutationOperator));
            default:
                throw new IllegalArgumentException("Wrong class: "+ type +". The class passed to the " +
                        "constructor must be Long, Double, Boolean or String.");
        }
    }
}
