package es.us.isa.restmutator.mutator.value.string0.operator;

import es.us.isa.restmutator.mutator.AbstractOperator;
import es.us.isa.restmutator.util.OperatorNames;
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
        weight = Float.parseFloat(readProperty("operator.value.string.weight." + OperatorNames.REPLACE));
        minLength = Integer.parseInt(readProperty("operator.value.string.length.min"));
        maxLength = Integer.parseInt(readProperty("operator.value.string.length.max"));
    }

    public Object mutate(Object stringObject) {
        return RandomStringUtils.random(rand1.nextInt(minLength, maxLength), true, true);
    }
}
