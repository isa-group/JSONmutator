package es.us.isa.restmutator.operator;

import java.util.*;

import static es.us.isa.restmutator.util.PropertyManager.readProperty;

/**
 * Superclass for mutators. A mutator decides on the type of mutation to be applied
 * to an element (string, int, etc.) based on the different mutation operators
 * enabled for that element (boundary, replacement, etc.).
 */
public abstract class AbstractMutator extends RandomManager {

    protected HashMap<String, AbstractOperator> operators;
    protected float prob;

    public AbstractMutator() {
        super();
    }

    public boolean shouldApplyMutation() {
        return rand.nextFloat() <= prob;
    }

    /**
     * Given a list (map) of mutation operators, selects one. To do so, it generates
     * a random float between 0 and {@code sum(weights_of_operators)} and chooses the
     * operator whose weight is in that range. Example: <br>
     * 1.- StringReplacement. {@code weight=0.1}. <br>
     * 2.- StringMutation. {@code weight=0.4}. <br>
     * 3.- StringBoundary. {@code weight=0.2}. <br>
     * - {@code randomFloat=0.367} => StringMutation is selected, because the float
     * falls in the range 0.1-0.5 (0.1+0.4)
     * @return The name of the mutation operator selected
     */
    public String getOperator() {
        // Sum all weights and multiply the result by a random float between 0 and 1
        float randomFloat = rand.nextFloat() *
                operators.values().stream()
                .map(AbstractOperator::getWeight)
                .reduce(0f, Float::sum);

        float acc = 0;
        String operatorName = null;
        Iterator<Map.Entry<String,AbstractOperator>> operatorIterator = operators.entrySet().iterator();
        Map.Entry<String,AbstractOperator> currentOperator;
        while (operatorIterator.hasNext()) {
            currentOperator = operatorIterator.next();
            acc += currentOperator.getValue().weight;
            if (randomFloat <= acc) {
                operatorName = currentOperator.getKey();
            }
        }

        return operatorName;
    }
}
