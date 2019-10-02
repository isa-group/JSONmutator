package es.us.isa.restmutator.operator.value.string0;

import es.us.isa.restmutator.operator.AbstractOperator;
import org.apache.commons.lang3.RandomStringUtils;

import static es.us.isa.restmutator.util.PropertyManager.readProperty;

/**
 * Operator that mutates a string by adding, removing or replacing one
 * single character.
 *
 * @author Alberto Martin-Lopez
 */
public class StringMutationOperator extends AbstractOperator {

    public StringMutationOperator() {
        super();
        weight = Float.parseFloat(readProperty("operator.value.string.weight.mutate"));
    }

    public String mutate(String string) {
        StringBuilder sb = new StringBuilder(string);
        int charPosition = rand.nextInt(string.length());

        if (rand.nextFloat() <= 1f/3) { // Remove char
            sb.deleteCharAt(charPosition);
        } else if (rand.nextFloat() <= 2f/3)  { // Add char
            sb.insert(charPosition, RandomStringUtils.random(1, true, true));
        } else { // Replace char
            sb.deleteCharAt(charPosition);
            sb.insert(charPosition, RandomStringUtils.random(1, true, true));
        }

        return sb.toString();
    }
}
