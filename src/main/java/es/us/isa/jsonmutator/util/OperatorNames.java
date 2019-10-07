package es.us.isa.jsonmutator.util;

/**
 * Name of all the possible mutation operators to apply to the JSON elements.
 * NOTE: These names should be the same as those used in the properties file
 * when assigning weights to the mutation operators of a mutator (e.g.
 * "operator.value.string.weight.replace").
 */
public class OperatorNames {
    public static final String REPLACE = "replace";
    public static final String MUTATE = "mutate";
    public static final String CHANGE_TYPE = "changeType";
    public static final String NULL = "null";
}
