package es.us.isa.restmutator;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import es.us.isa.restmutator.mutator.value.string0.StringMutator;

import java.util.Iterator;
import java.util.Map;

import static es.us.isa.restmutator.util.PropertyManager.readProperty;

/**
 * Class to manage mutation of JSON objects. Also works with JSON arrays.
 *
 * @author Alberto Martin-Lopez
 */
public class JsonMutator {

    private StringMutator stringMutator;
//    private LongMutator longMutator;
//    private DoubleMutator doubleMutator;
//    private BooleanMutator booleanMutator;
//    private ObjectMutator objectMutator;
//    private ArrayMutator arrayMutator;

    public JsonMutator() {
        if (Boolean.parseBoolean(readProperty("operator.value.string.enabled")))
            stringMutator = new StringMutator();
//        if (Boolean.parseBoolean(readProperty("operator.value.long.enabled")))
//            longMutator = new LongMutator();
//        if (Boolean.parseBoolean(readProperty("operator.value.double.enabled")))
//            doubleMutator = new DoubleMutator();
//        if (Boolean.parseBoolean(readProperty("operator.value.boolean.enabled")))
//            booleanMutator = new BooleanMutator();
//        if (Boolean.parseBoolean(readProperty("operator.object.enabled")))
//            objectMutator = new ObjectMutator();
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
        if (jsonNode.isObject()) {
            Iterator<Map.Entry<String,JsonNode>> objectPropertiesIterator = jsonNode.fields();
            Map.Entry<String,JsonNode> jsonProperty;
            while (objectPropertiesIterator.hasNext()) { // Iterate over each property
                jsonProperty = objectPropertiesIterator.next();
                if (jsonProperty.getValue().isObject() || jsonProperty.getValue().isArray()) { // If property is object or array...
                    jsonProperty.setValue(mutateJSON(jsonProperty.getValue())); // ... recursively call this function
                } else if (jsonProperty.getValue().isNull()) { // If property is null

                } else { // Otherwise mutate it
                    mutateProperty((ObjectNode)jsonNode, jsonProperty);
                }
            }
        } else if (jsonNode.isArray()) {
            Iterator<JsonNode> arrayElements = jsonNode.elements();
            JsonNode arrayElement;
            int arrayIndex = -1;
            while (arrayElements.hasNext()) { // Iterate over each element
                arrayElement = arrayElements.next();
                arrayIndex++;
                if (arrayElement.isObject() || arrayElement.isArray()) { // If property is object or array...
                    mutateJSON(arrayElement); // ... recursively call this function
                } else if (arrayElement.isNull()) { // If property is null

                } else { // Otherwise mutate it
                    mutateProperty((ArrayNode)jsonNode, arrayIndex);
                }
            }
        }

        return jsonNode;
    }

    public void mutateProperty(ObjectNode objectNode, Map.Entry<String,JsonNode> jsonProperty) {
        if (jsonProperty.getValue().isIntegralNumber()) {

        } else if (jsonProperty.getValue().isFloatingPointNumber()) {

        } else if (jsonProperty.getValue().isTextual()) {
            stringMutator.mutate(objectNode, jsonProperty.getKey());
        } else if (jsonProperty.getValue().isBoolean()) {

        }
    }

    public void mutateProperty(ArrayNode arrayNode, int index) {
        if (arrayNode.get(index).isIntegralNumber()) {

        } else if (arrayNode.get(index).isFloatingPointNumber()) {

        } else if (arrayNode.get(index).isTextual()) {
            stringMutator.mutate(arrayNode, index);
        } else if (arrayNode.get(index).isBoolean()) {

        }
    }
}









