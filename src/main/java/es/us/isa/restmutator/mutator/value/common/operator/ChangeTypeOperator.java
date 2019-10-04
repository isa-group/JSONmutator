package es.us.isa.restmutator.mutator.value.common.operator;

import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;
import es.us.isa.restmutator.mutator.AbstractOperator;
import org.apache.commons.lang3.RandomStringUtils;

import static es.us.isa.restmutator.util.PropertyManager.readProperty;
import static es.us.isa.restmutator.util.Utilities.assignWeight;

/**
 * Operator that mutates an element by changing its type (int, string...)
 *
 * @author Alberto Martin-Lopez
 */
public class ChangeTypeOperator extends AbstractOperator {

    private String type;        // Class of the object to be mutated
    private long minLong;
    private long maxLong;
    private double minDouble;
    private double maxDouble;
    private int minLength;      // Minimum length of the randomly generated string
    private int maxLength;      // Maximum length of the randomly generated string

    public ChangeTypeOperator(Class classType) {
        super();
        type = classType.getSimpleName();
        weight = assignWeight(type);
        minLong = Long.parseLong(readProperty("operator.value.long.min"));
        maxLong = Long.parseLong(readProperty("operator.value.long.max"));
        minDouble = Double.parseDouble(readProperty("operator.value.double.min"));
        maxDouble = Double.parseDouble(readProperty("operator.value.double.max"));
        minLength = Integer.parseInt(readProperty("operator.value.string.length.min"));
        maxLength = Integer.parseInt(readProperty("operator.value.string.length.max"));
    }

    public Object mutate() {
        float randomValue = rand2.nextFloat();
        Object returnObject = null;

        while (returnObject == null) {
            if (randomValue <= 1f/6 && !type.equals("Long")) {
                returnObject = rand1.nextLong(minLong, maxLong); // Return random long
            } else if (randomValue <= 2f/6 && !type.equals("Double")) {
                returnObject = rand1.nextUniform(minDouble, maxDouble); // Return random double
            } else if (randomValue <= 3f/6 && !type.equals("Boolean")) {
                returnObject = rand2.nextBoolean(); // Return random boolean
            } else if (randomValue <= 4f/6 && !type.equals("String")) {
                returnObject = RandomStringUtils.random(rand1.nextInt(minLength, maxLength), true, true); // Return random string
            } else if (randomValue <= 5f/6) {
                returnObject = new ObjectNode(JsonNodeFactory.instance); // Return empty object
            } else if (randomValue <= 1) {
                returnObject = new ArrayNode(JsonNodeFactory.instance); // Return empty array
            }
        }

        return returnObject;
    }
}
