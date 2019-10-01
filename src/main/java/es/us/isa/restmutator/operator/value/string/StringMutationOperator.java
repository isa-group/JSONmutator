package es.us.isa.restmutator.operator.value.string;

import static es.us.isa.restmutator.util.PropertyManager.*;

public class StringMutationOperator {

    private float replaceProb;      // Probability to completely replace a string
    private float addCharProb;      // Probability to add a char to a string
    private float removeCharProb;   // Probability to remove a char from a string
    private float replaceCharProb;  // Probability to replace a char in a string

    public StringMutationOperator() {
        replaceProb = Float.parseFloat(readProperty("operator.value.string.prob.replace"));
        addCharProb = Float.parseFloat(readProperty("operator.value.string.prob.addChar"));
        removeCharProb = Float.parseFloat(readProperty("operator.value.string.prob.removeChar"));
        replaceCharProb = Float.parseFloat(readProperty("operator.value.string.prob.replaceChar"));
    }
}
