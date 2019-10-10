package es.us.isa.jsonmutator;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import es.us.isa.jsonmutator.mutator.object.ObjectMutator;
import es.us.isa.jsonmutator.mutator.value.boolean0.BooleanMutator;
import es.us.isa.jsonmutator.mutator.value.long0.LongMutator;
import es.us.isa.jsonmutator.mutator.value.string0.StringMutator;
import java.util.Iterator;

import static es.us.isa.jsonmutator.util.PropertyManager.readProperty;

/**
 * Class to manage mutation of JSON objects. Also works with JSON arrays.
 *
 * @author Alberto Martin-Lopez
 */
public class JsonMutator {

    private boolean firstIteration; // True when mutateJSON is called the first time, false when it's called recursively

    private StringMutator stringMutator;
    private LongMutator longMutator;
//    private DoubleMutator doubleMutator;
    private BooleanMutator booleanMutator;
    private ObjectMutator objectMutator;
//    private ArrayMutator arrayMutator;

    public JsonMutator() {
        firstIteration = true;

        if (Boolean.parseBoolean(readProperty("operator.value.string.enabled")))
            stringMutator = new StringMutator();
        if (Boolean.parseBoolean(readProperty("operator.value.long.enabled")))
            longMutator = new LongMutator();
//        if (Boolean.parseBoolean(readProperty("operator.value.double.enabled")))
//            doubleMutator = new DoubleMutator();
        if (Boolean.parseBoolean(readProperty("operator.value.boolean.enabled")))
            booleanMutator = new BooleanMutator();
        if (Boolean.parseBoolean(readProperty("operator.object.enabled")))
            objectMutator = new ObjectMutator();
//        if (Boolean.parseBoolean(readProperty("operator.array.enabled")))
//            arrayMutator = new ArrayMutator();
    }

    /**
     * Apply some random mutations to a JSON object. These mutations are applied to sub-objects and
     * sub-arrays recursively: add new properties, remove existing properties, mutate existing
     * properties (numbers, strings and booleans), make existing properties null, leave existing
     * objects and arrays empty ({} or []).
     *
     * @param jsonNode The object to mutate
     * @return The mutated object
     */
    public JsonNode mutateJSON(JsonNode jsonNode) {
        boolean firstIterationOccurred = false; // Used to reset the state of firstIteration attribute
        if (firstIteration) {
            firstIteration = false; // Set to false so that this block is not entered again when recursively calling the function
            firstIterationOccurred = true; // Set to true so that firstIteration is reset to true at the end of this call
            if (objectMutator!=null && jsonNode.isObject())
                jsonNode = objectMutator.getMutatedObject((ObjectNode)jsonNode);
        }

        if (jsonNode.isObject()) { // If node is object
            Iterator<String> keysIterator = jsonNode.fieldNames();
            String propertyName;
            while (keysIterator.hasNext()) { // Iterate over each object property
                propertyName = keysIterator.next();
                mutateElement(jsonNode, propertyName, null); // (Possibly) mutate each property and...
                if (jsonNode.get(propertyName).isObject() || jsonNode.get(propertyName).isArray()) // ...if property is object or array...
                    ((ObjectNode)jsonNode).replace(propertyName, mutateJSON(jsonNode.get(propertyName))); // ...recursively call this function
            }
        } else if (jsonNode.isArray()) { // If node is array
            for (int arrayIndex=0; arrayIndex<jsonNode.size(); arrayIndex++) { // Iterate over each array element
                mutateElement(jsonNode, null, arrayIndex); // (Possibly) mutate each element and...
                if (jsonNode.get(arrayIndex).isObject() || jsonNode.get(arrayIndex).isArray()) // ...if element is object or array...
                    ((ArrayNode)jsonNode).set(arrayIndex, mutateJSON(jsonNode.get(arrayIndex))); // ...recursively call this function
            }
        } else {
            throw new IllegalArgumentException("Wrong type: " + jsonNode.getNodeType() + ". The function" +
                    "'mutateJSON(JsonNode)' only accepts two parameter types: ObjectNode and ArrayNode");
        }

        if (firstIterationOccurred)
            firstIteration = true; // Reset for the next time this function will be called
        return jsonNode;
    }

    /**
     * Receives an ObjectNode or ArrayNode and the property name or index (respectively)
     * of an element, mutates the value of the element and inserts the mutated value
     * in the same position.
     */
    private void mutateElement(JsonNode jsonNode, String propertyName, Integer index) {
        boolean isObj = index==null; // If index==null, jsonNode is an object, otherwise it is an array
        JsonNode element = isObj ? jsonNode.get(propertyName) : jsonNode.get(index);
        if (longMutator!=null && element.isIntegralNumber()) {
            if (isObj) longMutator.mutate((ObjectNode) jsonNode, propertyName);
            else longMutator.mutate((ArrayNode) jsonNode, index);
//        } else if (doubleMutator!=null && element.isFloatingPointNumber()) {
//            if (isObj) doubleMutator.mutate((ObjectNode)jsonNode, propertyName);
//            else doubleMutator.mutate((ArrayNode) jsonNode, index);
        } else if (stringMutator!=null && element.isTextual()) {
            if (isObj) stringMutator.mutate((ObjectNode)jsonNode, propertyName);
            else stringMutator.mutate((ArrayNode) jsonNode, index);
        } else if (booleanMutator!=null && element.isBoolean()) {
            if (isObj) booleanMutator.mutate((ObjectNode)jsonNode, propertyName);
            else booleanMutator.mutate((ArrayNode) jsonNode, index);
        } else if (objectMutator!=null && element.isObject()) {
            if (isObj) objectMutator.mutate((ObjectNode)jsonNode, propertyName);
            else objectMutator.mutate((ArrayNode) jsonNode, index);
        }
    }
}









