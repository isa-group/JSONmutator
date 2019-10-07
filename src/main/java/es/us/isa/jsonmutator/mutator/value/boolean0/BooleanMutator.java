package es.us.isa.jsonmutator.mutator.value.boolean0;

import es.us.isa.jsonmutator.mutator.AbstractMutator;
import es.us.isa.jsonmutator.mutator.value.boolean0.operator.BooleanMutationOperator;
import es.us.isa.jsonmutator.mutator.value.common.operator.ChangeTypeOperator;
import es.us.isa.jsonmutator.mutator.value.common.operator.NullOperator;
import es.us.isa.jsonmutator.util.OperatorNames;

import static es.us.isa.jsonmutator.util.PropertyManager.readProperty;

/**
 * Given a set of boolean mutation operators, the BooleamMutator selects one based
 * on their weights and returns the mutated boolean.
 *
 * @author Alberto Martin-Lopez
 */
public class BooleanMutator extends AbstractMutator {

    public BooleanMutator() {
        super();
        prob = Float.parseFloat(readProperty("operator.value.boolean.prob"));
        operators.put(OperatorNames.MUTATE, new BooleanMutationOperator());
        operators.put(OperatorNames.NULL, new NullOperator(Boolean.class));
        operators.put(OperatorNames.CHANGE_TYPE, new ChangeTypeOperator(Boolean.class));
    }
}
