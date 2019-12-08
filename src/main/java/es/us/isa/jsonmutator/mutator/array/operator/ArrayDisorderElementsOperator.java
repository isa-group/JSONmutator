package es.us.isa.jsonmutator.mutator.array.operator;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;
import es.us.isa.jsonmutator.mutator.AbstractOperator;
import es.us.isa.jsonmutator.util.OperatorNames;

import static es.us.isa.jsonmutator.util.PropertyManager.readProperty;

/**
 * Operator that mutates an array by disordering the elements in it.
 *
 * @author Alberto Martin-Lopez
 */
public class ArrayDisorderElementsOperator extends AbstractOperator {

    public ArrayDisorderElementsOperator() {
        super();
        weight = Float.parseFloat(readProperty("operator.array.weight." + OperatorNames.DISORDER_ELEMENTS));
    }

    public Object mutate(Object arrayNodeObject) {
        ArrayNode arrayNode = (ArrayNode)arrayNodeObject;
        if (arrayNode.size() > 1) { // Apply this mutation only if array contains more than 1 element (doesn't make sense otherwise)
            int elementToDisorderIndex = rand1.nextInt(0, arrayNode.size() - 1);
            int whereToInsert;
            do whereToInsert = rand1.nextInt(0, arrayNode.size() - 1);
            while(whereToInsert == elementToDisorderIndex);
            JsonNode elementToDisorder = arrayNode.remove(elementToDisorderIndex); // Remove an element
            arrayNode.insert(whereToInsert, elementToDisorder); // Insert it elsewhere
        }

        return arrayNode;
    }
}
