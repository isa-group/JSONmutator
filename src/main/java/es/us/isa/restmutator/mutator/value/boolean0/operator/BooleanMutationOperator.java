package es.us.isa.restmutator.mutator.value.boolean0.operator;

import es.us.isa.restmutator.mutator.AbstractOperator;

import static es.us.isa.restmutator.util.PropertyManager.readProperty;

/**
 * Operator that mutates a boolean by inverting its value
 *
 * @author Alberto Martin-Lopez
 */
public class BooleanMutationOperator extends AbstractOperator {

    public BooleanMutationOperator() {
        super();
        weight = Float.parseFloat(readProperty("operator.value.boolean.weight.mutate"));
    }

    public Boolean mutate(Boolean bool) {
        return !bool;
    }
}
