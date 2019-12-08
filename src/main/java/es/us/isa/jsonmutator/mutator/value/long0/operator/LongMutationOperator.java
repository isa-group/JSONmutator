package es.us.isa.jsonmutator.mutator.value.long0.operator;

import es.us.isa.jsonmutator.mutator.AbstractOperator;
import es.us.isa.jsonmutator.util.OperatorNames;
import org.apache.commons.lang3.RandomStringUtils;

import static es.us.isa.jsonmutator.util.PropertyManager.readProperty;

/**
 * Operator that mutates a long by adding or subtracting a delta number
 * to the original number
 *
 * @author Alberto Martin-Lopez
 */
public class LongMutationOperator extends AbstractOperator {

    private long delta;

    public LongMutationOperator() {
        super();
        weight = Float.parseFloat(readProperty("operator.value.long.weight." + OperatorNames.MUTATE));
        delta = Long.parseLong(readProperty("operator.value.long.delta"));
    }

    public Object mutate(Object longObject) {
        Long longValue = (Long)longObject;
        float randomValue = rand2.nextFloat();

        if (randomValue <= 1f/2) { // Mutation: subtract delta
            return longValue + delta;
        } else { // Mutation: add delta
            return longValue - delta;
        }
    }
}
