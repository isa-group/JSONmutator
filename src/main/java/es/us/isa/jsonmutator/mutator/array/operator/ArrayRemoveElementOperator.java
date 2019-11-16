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
public class ArrayRemoveElementOperator extends AbstractOperator {

    private int maxRemovedElements;     // Maximum number of elements to remove from the array
    private int minRemovedElements;     // Minimum number of elements to remove from the array

    public ArrayRemoveElementOperator() {
        super();
        weight = Float.parseFloat(readProperty("operator.array.weight." + OperatorNames.REMOVE_ELEMENT));
        maxRemovedElements = Integer.parseInt(readProperty("operator.array.removedElements.max"));
        minRemovedElements = Integer.parseInt(readProperty("operator.array.removedElements.min"));
    }

    public Object mutate(Object arrayNodeObject) {
        ArrayNode arrayNode = (ArrayNode)arrayNodeObject;
        int removedElements = rand1.nextInt(minRemovedElements, maxRemovedElements); // Remove between min and max elements to array

        for (int i=1; i<=removedElements; i++)
            if (arrayNode.size() > 0)
                arrayNode.remove(rand2.nextInt(arrayNode.size())); // Remove a random element

        return arrayNode;
    }
}
