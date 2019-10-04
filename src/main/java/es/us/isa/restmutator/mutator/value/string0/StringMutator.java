package es.us.isa.restmutator.mutator.value.string0;

import com.fasterxml.jackson.databind.node.ArrayNode;
import es.us.isa.restmutator.mutator.AbstractMutator;
import es.us.isa.restmutator.mutator.value.common.operator.ChangeTypeOperator;
import es.us.isa.restmutator.mutator.value.common.operator.NullOperator;
import es.us.isa.restmutator.mutator.value.string0.operator.StringMutationOperator;
import es.us.isa.restmutator.mutator.value.string0.operator.StringReplacementOperator;

import static es.us.isa.restmutator.util.JsonManager.*;
import static es.us.isa.restmutator.util.PropertyManager.readProperty;

/**
 * Given a set of string mutation operators, the StringMutator selects one based
 * on their weights and returns the mutated string.
 *
 * @author Alberto Martin-Lopez
 */
public class StringMutator extends AbstractMutator {

    private final String replaceOperator = "replace";
    private final String mutateOperator = "mutate";
    private final String nullOperator = "null";
    private final String changeTypeOperator = "changeType";

    public StringMutator() {
        super();
        prob = Float.parseFloat(readProperty("operator.value.string.prob"));
        operators.put(replaceOperator, new StringReplacementOperator());
        operators.put(mutateOperator, new StringMutationOperator());
        operators.put(nullOperator, new NullOperator(String.class));
        operators.put(changeTypeOperator, new ChangeTypeOperator(String.class));
    }

    @Override
    protected Object getMutatedElement(Object stringObject) {
        String string = (String)stringObject;
        String operator = getOperator();
        switch (operator) {
            case replaceOperator:
                return ((StringReplacementOperator)operators.get(replaceOperator)).mutate();
            case mutateOperator:
                return ((StringMutationOperator)operators.get(mutateOperator)).mutate(string);
            case nullOperator:
                return ((NullOperator)operators.get(nullOperator)).mutate();
            case changeTypeOperator:
                return ((ChangeTypeOperator)operators.get(changeTypeOperator)).mutate();
            default:
                throw new IllegalArgumentException("The operator '" + operator + "' is not allowed.");
        }
    }
}
