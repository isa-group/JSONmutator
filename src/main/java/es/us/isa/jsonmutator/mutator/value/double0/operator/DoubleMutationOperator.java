package es.us.isa.jsonmutator.mutator.value.double0.operator;

import es.us.isa.jsonmutator.mutator.AbstractOperator;
import es.us.isa.jsonmutator.util.OperatorNames;
import org.apache.commons.lang3.RandomStringUtils;

import static es.us.isa.jsonmutator.util.PropertyManager.readProperty;

/**
 * Operator that mutates a double by adding or subtracting a delta number
 * to the original number
 *
 * @author Alberto Martin-Lopez
 */
public class DoubleMutationOperator extends AbstractOperator {

    private double delta;

    public DoubleMutationOperator() {
        super();
        weight = Float.parseFloat(readProperty("operator.value.double.weight." + OperatorNames.MUTATE));
        delta = Double.parseDouble(readProperty("operator.value.double.delta"));
    }

    public Object mutate(Object doubleObject) {
        Double doubleValue = (Double)doubleObject;
        float randomValue = rand2.nextFloat();

        if (randomValue <= 1f/2) { // Mutation: subtract delta
            return doubleValue + delta;
        } else { // Mutation: add delta
            return doubleValue - delta;
        }
    }
}
