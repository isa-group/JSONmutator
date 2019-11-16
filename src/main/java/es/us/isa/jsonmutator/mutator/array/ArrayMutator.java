package es.us.isa.jsonmutator.mutator.array;

import com.fasterxml.jackson.databind.node.ObjectNode;
import es.us.isa.jsonmutator.mutator.AbstractObjectOrArrayMutator;
import es.us.isa.jsonmutator.mutator.array.operator.ArrayRemoveElementOperator;
import es.us.isa.jsonmutator.mutator.value.common.operator.ChangeTypeOperator;
import es.us.isa.jsonmutator.mutator.value.common.operator.NullOperator;
import es.us.isa.jsonmutator.util.OperatorNames;

/**
 * Given a set of array mutation operators, the ArrayMutator selects one based
 * on their weights and returns the mutated array.
 *
 * @author Alberto Martin-Lopez
 */
public class ArrayMutator extends AbstractObjectOrArrayMutator {

    public ArrayMutator() {
        super();
    }

    protected void resetOperators() {
        operators.clear();
        operators.put(OperatorNames.REMOVE_ELEMENT, new ArrayRemoveElementOperator());
//        operators.put(OperatorNames.ADD_ELEMENT, new ArrayAddElementOperator());
        operators.put(OperatorNames.NULL, new NullOperator(ObjectNode.class));
        operators.put(OperatorNames.CHANGE_TYPE, new ChangeTypeOperator(ObjectNode.class));
        operators.put(OperatorNames.NULL, new NullOperator(ArrayNode.class));
        operators.put(OperatorNames.CHANGE_TYPE, new ChangeTypeOperator(ArrayNode.class));
    }

    protected void resetFirstLevelOperators() {
        operators.clear();
        operators.put(OperatorNames.REMOVE_ELEMENT, new ArrayRemoveElementOperator());
//        operators.put(OperatorNames.ADD_ELEMENT, new ArrayAddElementOperator());
    }
}
