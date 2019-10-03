package es.us.isa.restmutator.operator.value.string0;

import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import es.us.isa.restmutator.operator.AbstractMutator;
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
    private final String changeTypeOperator = "changeType";

    public StringMutator() {
        super();
        prob = Float.parseFloat(readProperty("operator.value.string.prob"));
        operators.put(replaceOperator, new StringReplacementOperator());
        operators.put(mutateOperator, new StringMutationOperator());
        operators.put(changeTypeOperator, new StringChangeTypeOperator());
    }

    /**
     * Given an object and the name of a property, mutate the value of that property
     * with probability {@link StringMutator#prob}
     * @return True if the mutation was applied, false otherwise
     */
    public boolean mutate(ObjectNode objectNode, String propertyName) {
        if (shouldApplyMutation()) {
            String propertyValue = objectNode.get(propertyName).asText(); // Get string to mutate
            Object mutatedString = getMutatedString(propertyValue); // Mutate string
            insertElement(objectNode, propertyName, mutatedString); // Replace original string with mutated string
            return true;
        }
        return false;
    }

    /**
     * Given an array and the index of an element, mutate that element
     * with probability {@link StringMutator#prob}
     * @return True if the mutation was applied, false otherwise
     */
    public boolean mutate(ArrayNode arrayNode, int index) {
        if (shouldApplyMutation()) {
            String arrayElement = arrayNode.get(index).asText(); // Get string to mutate
            Object mutatedString = getMutatedString(arrayElement); // Mutate string
            insertElement(arrayNode, index, mutatedString); // Replace original string with mutated string
            return true;
        }
        return false;
    }

    /**
     * Mutates a string with one of the predefined mutation operators configured
     * in the properties file
     * @param string The string to mutate
     * @return The mutated object. ATTENTION: This can be a string or something
     * else, because the mutation can change the type of the element (int, bool...)
     */
    private Object getMutatedString(String string) {
        String operator = getOperator();
        switch (operator) {
            case replaceOperator:
                return ((StringReplacementOperator)operators.get(replaceOperator)).mutate();
            case mutateOperator:
                return ((StringMutationOperator)operators.get(mutateOperator)).mutate(string);
            case changeTypeOperator:
                return ((StringChangeTypeOperator)operators.get(changeTypeOperator)).mutate();
            default:
                throw new IllegalArgumentException("The operator '" + operator + "' is not allowed.");
        }
    }
}
