package es.us.isa.jsonmutator.mutator.array.operator;

import com.fasterxml.jackson.databind.node.ArrayNode;
import es.us.isa.jsonmutator.mutator.AbstractOperator;
import es.us.isa.jsonmutator.util.OperatorNames;
import static es.us.isa.jsonmutator.util.PropertyManager.readProperty;

/**
 * Operator that mutates an array by removing a number of elements from it.
 *
 * @author Alberto Martin-Lopez
 */
public class ArrayRemovePropertyOperator extends AbstractOperator {

    private int maxRemovedProperties;     // Maximum number of elements to remove from the array
    private int minRemovedProperties;     // Minimum number of elements to remove from the array

    public ArrayRemovePropertyOperator() {
        super();
        weight = Float.parseFloat(readProperty("operator.array.weight." + OperatorNames.REMOVE_PROPERTY));
        maxRemovedProperties = Integer.parseInt(readProperty("operator.array.removedProperties.max"));
        minRemovedProperties = Integer.parseInt(readProperty("operator.array.removedProperties.min"));
    }

    public Object mutate(Object arrayNodeObject) {
        ArrayNode arrayNode = (ArrayNode)arrayNodeObject;
        int removedProperties = rand1.nextInt(minRemovedProperties, maxRemovedProperties); // Remove between min and max elements to array

        for (int i=1; i<=removedProperties; i++)
            if (arrayNode.size() > 0)
                arrayNode.remove(rand2.nextInt(arrayNode.size())); // Remove a random element

        return arrayNode;
    }
}
