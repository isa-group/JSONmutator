package es.us.isa.restmutator.mutator.value.string0;

import es.us.isa.restmutator.mutator.AbstractMutator;
import es.us.isa.restmutator.mutator.value.common.operator.ChangeTypeOperator;
import es.us.isa.restmutator.mutator.value.common.operator.NullOperator;
import es.us.isa.restmutator.mutator.value.string0.operator.StringMutationOperator;
import es.us.isa.restmutator.mutator.value.string0.operator.StringReplacementOperator;
import es.us.isa.restmutator.util.OperatorNames;

import static es.us.isa.restmutator.util.PropertyManager.readProperty;

/**
 * Given a set of string mutation operators, the StringMutator selects one based
 * on their weights and returns the mutated string.
 *
 * @author Alberto Martin-Lopez
 */
public class StringMutator extends AbstractMutator {

    public StringMutator() {
        super();
        prob = Float.parseFloat(readProperty("operator.value.string.prob"));
        operators.put(OperatorNames.REPLACE, new StringReplacementOperator());
        operators.put(OperatorNames.MUTATE, new StringMutationOperator());
        operators.put(OperatorNames.NULL, new NullOperator(String.class));
        operators.put(OperatorNames.CHANGE_TYPE, new ChangeTypeOperator(String.class));
    }
}
