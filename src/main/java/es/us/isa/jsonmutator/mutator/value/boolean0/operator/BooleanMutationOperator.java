package es.us.isa.jsonmutator.mutator.value.boolean0.operator;

import es.us.isa.jsonmutator.mutator.AbstractOperator;
import es.us.isa.jsonmutator.util.OperatorNames;

import static es.us.isa.jsonmutator.util.PropertyManager.readProperty;

/**
 * Operator that mutates a boolean by inverting its value
 *
 * @author Alberto Martin-Lopez
 */
public class BooleanMutationOperator extends AbstractOperator {

    public BooleanMutationOperator() {
        super();
        weight = Float.parseFloat(readProperty("operator.value.boolean.weight." + OperatorNames.MUTATE));
    }

    public Object mutate(Object boolObject) {
        Boolean bool = (Boolean)boolObject;
        return !bool;
    }
}
