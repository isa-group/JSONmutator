package es.us.isa.restmutator.operator.value.string0;

import es.us.isa.restmutator.operator.AbstractOperator;
import org.apache.commons.lang3.RandomStringUtils;

import static es.us.isa.restmutator.util.PropertyManager.readProperty;

/**
 * Operator that mutates a string by completely replacing it.
 *
 * @author Alberto Martin-Lopez
 */
public class StringReplacementOperator extends AbstractOperator {

    private int minLength;      // Minimum length of the randomly generated string
    private int maxLength;      // Maximum length of the randomly generated string

    public StringReplacementOperator() {
        super();
        minLength = Integer.parseInt(readProperty("operator.value.string.length.min"));
        maxLength = Integer.parseInt(readProperty("operator.value.string.length.max"));
        weight = Float.parseFloat(readProperty("operator.value.string.weight.replace"));
    }

    public String mutate() {
        return RandomStringUtils.random(minLength+rand.nextInt(maxLength), true, true);
    }
}
