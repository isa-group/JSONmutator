package es.us.isa.jsonmutator.mutator.value.double0;

import es.us.isa.jsonmutator.mutator.AbstractMutator;
import es.us.isa.jsonmutator.mutator.value.common.operator.ChangeTypeOperator;
import es.us.isa.jsonmutator.mutator.value.common.operator.NullOperator;
import es.us.isa.jsonmutator.mutator.value.double0.operator.DoubleMutationOperator;
import es.us.isa.jsonmutator.mutator.value.double0.operator.DoubleReplacementOperator;
import es.us.isa.jsonmutator.util.OperatorNames;

import static es.us.isa.jsonmutator.util.PropertyManager.readProperty;

/**
 * Given a set of double mutation operators, the DoubleMutator selects one based
 * on their weights and returns the mutated double.
 *
 * @author Alberto Martin-Lopez
 */
public class DoubleMutator extends AbstractMutator {

    public DoubleMutator() {
        super();
        prob = Float.parseFloat(readProperty("operator.value.string.prob"));
        operators.put(OperatorNames.REPLACE, new DoubleReplacementOperator());
        operators.put(OperatorNames.MUTATE, new DoubleMutationOperator());
        operators.put(OperatorNames.NULL, new NullOperator(Double.class));
        operators.put(OperatorNames.CHANGE_TYPE, new ChangeTypeOperator(Double.class));
    }
}
