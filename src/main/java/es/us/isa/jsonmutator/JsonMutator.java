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

    private StringMutator stringMutator;
    private LongMutator longMutator;
//    private DoubleMutator doubleMutator;
    private BooleanMutator booleanMutator;
    private ObjectMutator objectMutator;
//    private ArrayMutator arrayMutator;

    public JsonMutator() {
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
        if (jsonNode.isObject()) { // If node is object
            Iterator<String> keysIterator = jsonNode.fieldNames();
            String propertyName;
            while (keysIterator.hasNext()) { // Iterate over each object property
                propertyName = keysIterator.next();
                mutateProperty((ObjectNode)jsonNode, propertyName); // (Possibly) mutate each property and...
                if (jsonNode.get(propertyName).isObject() || jsonNode.get(propertyName).isArray()) // ...if property is object or array...
                    ((ObjectNode)jsonNode).replace(propertyName, mutateJSON(jsonNode.get(propertyName))); // ...recursively call this function
            }
        } else if (jsonNode.isArray()) { // If node is array
            for (int arrayIndex=0; arrayIndex<jsonNode.size(); arrayIndex++) { // Iterate over each array element
                mutateProperty((ArrayNode)jsonNode, arrayIndex); // (Possibly) mutate each element and...
                if (jsonNode.get(arrayIndex).isObject() || jsonNode.get(arrayIndex).isArray()) // ...if element is object or array...
                    ((ArrayNode)jsonNode).set(arrayIndex, mutateJSON(jsonNode.get(arrayIndex))); // ...recursively call this function
            }
        } else {
            throw new IllegalArgumentException("Wrong type: " + jsonNode.getNodeType() + ". The function" +
                    "'mutateJSON(JsonNode)' only accepts two parameter types: ObjectNode and ArrayNode");
        }

        return jsonNode;
    }


    // NOTE: The following two methods are exactly the same, but one of them is for objects
    // and the other is for arrays, i.e. one is passed a String (object key) and the
    // other is passed an integer (array index). I don't know if there's any way to
    // optimize this (right now it's like duplicated code, even though it's actually not).

    /**
     * Receives an ObjectNode and the name of a property, mutates the value of the
     * property and inserts the mutated value in the same position.
     */
    private void mutateProperty(ObjectNode objectNode, String propertyName) {
        if (longMutator!=null && objectNode.get(propertyName).isIntegralNumber()) {
            longMutator.mutate(objectNode, propertyName);
//        } else if (doubleMutator!=null && jsonProperty.getValue().isFloatingPointNumber()) {
//            doubleMutator.mutate(objectNode, propertyName);
        } else if (stringMutator!=null && objectNode.get(propertyName).isTextual()) {
            stringMutator.mutate(objectNode, propertyName);
        } else if (booleanMutator!=null && objectNode.get(propertyName).isBoolean()) {
            booleanMutator.mutate(objectNode, propertyName);
        } else if (objectMutator!=null && objectNode.get(propertyName).isObject()) {
            objectMutator.mutate(objectNode, propertyName);
        }
    }

    /**
     * Receives an ArrayNode and the index of an element, mutates the value of the
     * element and inserts the mutated value in the same position.
     */
    private void mutateProperty(ArrayNode arrayNode, int index) {
        if (longMutator!=null && arrayNode.get(index).isIntegralNumber()) {
            longMutator.mutate(arrayNode, index);
//        } else if (doubleMutator!=null && arrayNode.get(index).isFloatingPointNumber()) {
//            doubleMutator.mutate(objectNode, jsonProperty.getKey());
        } else if (stringMutator!=null && arrayNode.get(index).isTextual()) {
            stringMutator.mutate(arrayNode, index);
        } else if (booleanMutator!=null && arrayNode.get(index).isBoolean()) {
            booleanMutator.mutate(arrayNode, index);
        } else if (objectMutator!=null && arrayNode.get(index).isObject()) {
            objectMutator.mutate(arrayNode, index);
        }
    }
}









