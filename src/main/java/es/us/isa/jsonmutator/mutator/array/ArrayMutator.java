package es.us.isa.jsonmutator.mutator.array;

import com.fasterxml.jackson.databind.node.ObjectNode;
import es.us.isa.jsonmutator.mutator.AbstractObjectOrArrayMutator;
import es.us.isa.jsonmutator.mutator.array.operator.ArrayRemovePropertyOperator;
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
        operators.put(OperatorNames.REMOVE_PROPERTY, new ArrayRemovePropertyOperator());
//        operators.put(OperatorNames.ADD_PROPERTY, new ObjectAddPropertyOperator());
        operators.put(OperatorNames.NULL, new NullOperator(ObjectNode.class));
        operators.put(OperatorNames.CHANGE_TYPE, new ChangeTypeOperator(ObjectNode.class));
    }

    protected void resetFirstLevelOperators() {
        operators.clear();
        operators.put(OperatorNames.REMOVE_PROPERTY, new ArrayRemovePropertyOperator());
//        operators.put(OperatorNames.ADD_PROPERTY, new ObjectAddPropertyOperator());
    }
}
