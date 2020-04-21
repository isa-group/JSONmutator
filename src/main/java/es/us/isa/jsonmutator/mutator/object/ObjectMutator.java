package es.us.isa.jsonmutator.mutator.object;

import com.fasterxml.jackson.databind.node.ObjectNode;
import es.us.isa.jsonmutator.mutator.AbstractObjectOrArrayMutator;
import es.us.isa.jsonmutator.mutator.object.operator.ObjectAddElementOperator;
import es.us.isa.jsonmutator.mutator.object.operator.ObjectRemoveElementOperator;
import es.us.isa.jsonmutator.mutator.object.operator.ObjectRemoveObjectTypeElementOperator;
import es.us.isa.jsonmutator.mutator.value.common.operator.ChangeTypeOperator;
import es.us.isa.jsonmutator.mutator.value.common.operator.NullOperator;
import es.us.isa.jsonmutator.util.OperatorNames;

/**
 * Given a set of object mutation operators, the ObjectMutator selects one based
 * on their weights and returns the mutated object.
 *
 * @author Alberto Martin-Lopez
 */
public class ObjectMutator extends AbstractObjectOrArrayMutator {

    public ObjectMutator() {
        super();
    }

    protected void resetOperators() {
        operators.clear();
        operators.put(OperatorNames.REMOVE_ELEMENT, new ObjectRemoveElementOperator());
        operators.put(OperatorNames.REMOVE_OBJECT_ELEMENT, new ObjectRemoveObjectTypeElementOperator());
        operators.put(OperatorNames.ADD_ELEMENT, new ObjectAddElementOperator());
        operators.put(OperatorNames.NULL, new NullOperator(ObjectNode.class));
        operators.put(OperatorNames.CHANGE_TYPE, new ChangeTypeOperator(ObjectNode.class));
    }

    protected void resetFirstLevelOperators() {
        operators.remove(OperatorNames.NULL);
        operators.remove(OperatorNames.CHANGE_TYPE);
    }
}
