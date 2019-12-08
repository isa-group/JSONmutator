package es.us.isa.jsonmutator.mutator.value.common.operator;

import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.NullNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import es.us.isa.jsonmutator.mutator.AbstractOperator;
import es.us.isa.jsonmutator.util.OperatorNames;
import org.apache.commons.lang3.RandomStringUtils;

import static es.us.isa.jsonmutator.util.PropertyManager.readProperty;
import static es.us.isa.jsonmutator.util.Utilities.assignWeight;

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
        weight = assignWeight(type, OperatorNames.CHANGE_TYPE);
        minLong = Long.parseLong(readProperty("operator.value.long.min"));
        maxLong = Long.parseLong(readProperty("operator.value.long.max"));
        minDouble = Double.parseDouble(readProperty("operator.value.double.min"));
        maxDouble = Double.parseDouble(readProperty("operator.value.double.max"));
        minLength = Integer.parseInt(readProperty("operator.value.string.length.min"));
        maxLength = Integer.parseInt(readProperty("operator.value.string.length.max"));
    }

    public Object mutate(Object elementObject) {
        float randomValue = rand2.nextFloat();
        Object returnObject = null;

        while (returnObject == null) {
            if (randomValue > 0 && randomValue <= 1f/7 && !type.equals("Long")) {
                returnObject = rand1.nextLong(minLong, maxLong); // Return random long
            } else if (randomValue > 1f/7 && randomValue <= 2f/7 && !type.equals("Double")) {
                returnObject = rand1.nextUniform(minDouble, maxDouble); // Return random double
            } else if (randomValue > 2f/7 && randomValue <= 3f/7 && !type.equals("Boolean")) {
                returnObject = rand2.nextBoolean(); // Return random boolean
            } else if (randomValue > 3f/7 && randomValue <= 4f/7 && !type.equals("String")) {
                returnObject = RandomStringUtils.random(rand1.nextInt(minLength, maxLength), true, true); // Return random string
            } else if (randomValue > 4f/7 && randomValue <= 5f/7 && !type.equals("NullNode")) {
                returnObject = NullNode.getInstance(); // Return null
            } else if (randomValue > 5f/7 && randomValue <= 6f/7 && !type.equals("ObjectNode")) {
                returnObject = new ObjectNode(JsonNodeFactory.instance); // Return empty object
            } else if (randomValue > 6f/7 && randomValue <= 1 && !type.equals("ArrayNode")) {
                returnObject = new ArrayNode(JsonNodeFactory.instance); // Return empty array
            } else { // This happens when an element is attempted to be converted into the same type (e.g. Long to Long)
                randomValue = rand2.nextFloat(); // In that case, regenerate randomValue and try again
            }
        }

        return returnObject;
    }
}
