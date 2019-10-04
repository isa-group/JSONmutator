package es.us.isa.restmutator.mutator.value.boolean0;

import es.us.isa.restmutator.mutator.AbstractMutator;
import es.us.isa.restmutator.mutator.value.boolean0.operator.BooleanMutationOperator;
import es.us.isa.restmutator.mutator.value.common.operator.ChangeTypeOperator;
import es.us.isa.restmutator.mutator.value.common.operator.NullOperator;

import static es.us.isa.restmutator.util.JsonManager.insertElement;
import static es.us.isa.restmutator.util.PropertyManager.readProperty;

/**
 * Given a set of boolean mutation operators, the BooleamMutator selects one based
 * on their weights and returns the mutated boolean.
 *
 * @author Alberto Martin-Lopez
 */
public class BooleanMutator extends AbstractMutator {

    private final String mutateOperator = "mutate";
    private final String nullOperator = "null";
    private final String changeTypeOperator = "changeType";

    public BooleanMutator() {
        super();
        prob = Float.parseFloat(readProperty("operator.value.boolean.prob"));
        operators.put(mutateOperator, new BooleanMutationOperator());
        operators.put(nullOperator, new NullOperator(Boolean.class));
        operators.put(changeTypeOperator, new ChangeTypeOperator(Boolean.class));
    }

    @Override
    protected Object getMutatedElement(Object booleanObject) {
        Boolean bool = (Boolean)booleanObject;
        String operator = getOperator();
        switch (operator) {
            case mutateOperator:
                return ((BooleanMutationOperator)operators.get(mutateOperator)).mutate(bool);
            case nullOperator:
                return ((NullOperator)operators.get(nullOperator)).mutate();
            case changeTypeOperator:
                return ((ChangeTypeOperator)operators.get(changeTypeOperator)).mutate();
            default:
                throw new IllegalArgumentException("The operator '" + operator + "' is not allowed.");
        }
    }
}
