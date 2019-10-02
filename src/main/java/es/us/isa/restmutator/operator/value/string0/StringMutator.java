package es.us.isa.restmutator.operator.value.string0;

import com.fasterxml.jackson.databind.node.ArrayNode;
import es.us.isa.restmutator.operator.AbstractMutator;
import jdk.nashorn.internal.ir.ObjectNode;

import static es.us.isa.restmutator.util.PropertyManager.readProperty;

/**
 * Given a set of string mutation operators, the StringMutator selects one based
 * on their weights and returns the mutated string.
 *
 * @author Alberto Martin-Lopez
 */
public class StringMutator extends AbstractMutator {

    public StringMutator() {
        super();
        prob = Float.parseFloat(readProperty("operator.value.string.prob"));
        operators.put("replace", new StringReplacementOperator());
        operators.put("mutate", new StringMutationOperator());
    }

    public Object mutate(ObjectNode objectNode, String propertyName) {
        if (shouldApplyMutation()) {
            String mutatedString;
//            switch (getOperator()) {
//                case "replace":
//                    mutatedString = operators.get("replace").mutate("")
//            }
        }

        return null;
    }

    public Object mutate(ArrayNode arrayNode, int index) {

        return null;
    }
}
