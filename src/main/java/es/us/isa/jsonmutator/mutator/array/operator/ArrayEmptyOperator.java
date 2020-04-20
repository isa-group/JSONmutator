package es.us.isa.jsonmutator.mutator.array.operator;

import static es.us.isa.jsonmutator.util.PropertyManager.readProperty;

import com.fasterxml.jackson.databind.node.ArrayNode;
import es.us.isa.jsonmutator.mutator.AbstractOperator;
import es.us.isa.jsonmutator.util.OperatorNames;

/**
 * Operator that converts an array to empty by removing all elements from it.
 *
 * @author Ana Belén Sánchez
 */
public class ArrayEmptyOperator extends AbstractOperator {
	    
	public ArrayEmptyOperator() {
		super();
	    weight = Float.parseFloat(readProperty("operator.array.weight." + OperatorNames.EMPTY));
	}

	public Object mutate(Object arrayNodeObject) {
	    ArrayNode arrayNode = (ArrayNode)arrayNodeObject;

	    if (arrayNode.size() > 0)
	    	arrayNode.removeAll(); // Remove all elements in the array
	    
	    return arrayNode;
	    }
}
