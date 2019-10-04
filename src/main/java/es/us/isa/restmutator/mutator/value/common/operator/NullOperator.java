package es.us.isa.restmutator.mutator.value.common.operator;

import es.us.isa.restmutator.mutator.AbstractOperator;

import static es.us.isa.restmutator.util.Utilities.assignWeight;

/**
 * Operator that mutates an element by returning null
 *
 * @author Alberto Martin-Lopez
 */
public class NullOperator extends AbstractOperator {

    public NullOperator(Class classType) {
        super();
        weight = assignWeight(classType.getSimpleName());
    }

    public Object mutate() {
        return null;
    }
}
