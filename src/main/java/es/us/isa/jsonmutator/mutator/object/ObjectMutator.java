package es.us.isa.jsonmutator.mutator.object;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import es.us.isa.jsonmutator.mutator.AbstractMutator;
import es.us.isa.jsonmutator.mutator.AbstractOperator;
import es.us.isa.jsonmutator.mutator.object.operator.ObjectAddPropertyOperator;
import es.us.isa.jsonmutator.mutator.object.operator.ObjectRemovePropertyOperator;
import es.us.isa.jsonmutator.mutator.value.common.operator.ChangeTypeOperator;
import es.us.isa.jsonmutator.mutator.value.common.operator.NullOperator;
import es.us.isa.jsonmutator.util.OperatorNames;

import java.util.Map;

import static es.us.isa.jsonmutator.util.JsonManager.insertElement;
import static es.us.isa.jsonmutator.util.PropertyManager.readProperty;

/**
 * Given a set of object mutation operators, the ObjectMutator selects one based
 * on their weights and returns the mutated object.
 *
 * @author Alberto Martin-Lopez
 */
public class ObjectMutator extends AbstractMutator {

    private int minMutations;
    private int maxMutations;

    public ObjectMutator() {
        super();
        prob = Float.parseFloat(readProperty("operator.object.prob"));
        minMutations = Integer.parseInt(readProperty("operator.object.mutations.min"));
        maxMutations = Integer.parseInt(readProperty("operator.object.mutations.max"));
        resetOperators();
    }

    /**
     * Auxiliary function to clear the map of operators and add all of them again.
     * Needed because when one type of mutation is applied, it is removed from the
     * map so that it is not selected again.
     */
    private void resetOperators() {
        operators.clear();
        operators.put(OperatorNames.REMOVE_PROPERTY, new ObjectRemovePropertyOperator());
        operators.put(OperatorNames.ADD_PROPERTY, new ObjectAddPropertyOperator());
        operators.put(OperatorNames.NULL, new NullOperator(ObjectNode.class));
        operators.put(OperatorNames.CHANGE_TYPE, new ChangeTypeOperator(ObjectNode.class));
    }

    /**
     * The mutate method of the ObjectMutator is a bit different from others, since
     * multiple mutations (between {@link ObjectMutator#minMutations} and
     * {@link ObjectMutator#maxMutations}) can be applied in the same call.
     * Given an object and the name of a property, mutate the value of that property
     * with probability {@link ObjectMutator#prob}
     *
     * @return True if at least one mutation was applied, false otherwise
     */
    @Override
    public boolean mutate(ObjectNode objectNode, String propertyName) {
        return mutate(objectNode, propertyName, null);
    }

    /**
     * The mutate method of the ObjectMutator is a bit different from others, since
     * multiple mutations (between {@link ObjectMutator#minMutations} and
     * {@link ObjectMutator#maxMutations}) can be applied in the same call.
     * Given an array and the index of an element, mutate that element
     * with probability {@link ObjectMutator#prob}
     *
     * @return True if at least one mutation was applied, false otherwise
     */
    @Override
    public boolean mutate(ArrayNode arrayNode, int index) {
        return mutate(arrayNode, null, index);
    }

    /**
     * Auxiliary function to manage object mutations regardless of whether the
     * element to mutate is inside an object or inside an array.
     *
     * @param jsonNode ObjectNode or ArrayNode where the property containing an
     *                 object is located
     * @param propertyName Name of the property of the jsonNode (ObjectNode)
     *                     containing an object. Must be null if jsonNode is an
     *                     ArrayNode
     * @param index Index of the element of the jsonNode (ArrayNode) that is an
     *              object. Must be null if jsonNode is an ObjectNode
     * @return True if at least one mutation was applied, false otherwise
     */
    private boolean mutate(JsonNode jsonNode, String propertyName, Integer index) {
        boolean isObj = index==null; // If index==null, jsonNode is an object, otherwise it is an array
        int nMutations = rand1.nextInt(minMutations, maxMutations);
        boolean wasMutated = false;
        for (int i=0; i<nMutations; i++) {
            JsonNode elementToMutate = isObj ? jsonNode.get(propertyName) : jsonNode.get(index);
            if (elementToMutate.isObject()) {
                if (shouldApplyMutation()) {
                    // Mutate element by randomly choosing one mutation operator among 'operators' and applying the mutation:
                    String operator = getOperator();
                    if (operator != null) {
                        Object mutatedElement = operators.get(operator).mutate(elementToMutate);
                        operators.remove(operator); // Remove that operator so that the mutation isn't applied twice
                        // Replace original element with mutated element:
                        if (isObj) insertElement((ObjectNode) jsonNode, propertyName, mutatedElement);
                        else insertElement((ArrayNode) jsonNode, index, mutatedElement);
                        if (!wasMutated)
                            wasMutated = true;
                    }
                }
            } else {
                break;
            }
        }
        resetOperators(); // After all mutations are applied, reset operators to have all of them in the map again

        return wasMutated;
    }
}
