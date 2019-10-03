package es.us.isa.restmutator.operator.value.string0;

import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;
import es.us.isa.restmutator.operator.AbstractOperator;

import static es.us.isa.restmutator.util.PropertyManager.readProperty;

/**
 * Operator that mutates a string by changing its type (int, bool...)
 *
 * @author Alberto Martin-Lopez
 */
public class StringChangeTypeOperator extends AbstractOperator {

    private long minLong;
    private long maxLong;
    private double minDouble;
    private double maxDouble;

    public StringChangeTypeOperator() {
        super();
        weight = Float.parseFloat(readProperty("operator.value.string.weight.changeType"));
        minLong = Long.parseLong(readProperty("operator.value.long.min"));
        maxLong = Long.parseLong(readProperty("operator.value.long.max"));
        minDouble = Double.parseDouble(readProperty("operator.value.double.min"));
        maxDouble = Double.parseDouble(readProperty("operator.value.double.max"));
    }

    public Object mutate() {
        float randomValue = rand2.nextFloat();

        if (randomValue <= 1f/5) {
            return rand1.nextLong(minLong, maxLong); // Return random long
        } else if (randomValue <= 2f/5) {
            return rand1.nextUniform(minDouble, maxDouble); // Return random double
        } else if (randomValue <= 3f/5) {
            return rand2.nextBoolean(); // Return random boolean
        } else if (randomValue <= 4f/5) {
            return new ObjectNode(JsonNodeFactory.instance); // Return empty object
        } else {
            return new ArrayNode(JsonNodeFactory.instance); // Return empty array
        }
    }
}
