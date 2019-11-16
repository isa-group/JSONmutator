package es.us.isa.jsonmutator.mutator.value.common.operator;

import com.fasterxml.jackson.databind.node.NullNode;
import es.us.isa.jsonmutator.mutator.AbstractOperator;
import es.us.isa.jsonmutator.util.OperatorNames;

import static es.us.isa.jsonmutator.util.Utilities.assignWeight;

/**
 * Operator that mutates an element by returning null
 *
 * @author Alberto Martin-Lopez
 */
public class NullOperator extends AbstractOperator {

    public NullOperator(Class classType) {
        super();
        weight = assignWeight(classType.getSimpleName(), OperatorNames.NULL);
    }

    public Object mutate(Object elementObject) {
        return NullNode.instance;
    }
}
