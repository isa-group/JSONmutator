package es.us.isa.jsonmutator;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.google.common.collect.Lists;
import java.util.Iterator;
import org.apache.commons.math3.random.RandomDataGenerator;

import es.us.isa.jsonmutator.mutator.array.ArrayMutator;
import es.us.isa.jsonmutator.mutator.array.operator.ArrayAddElementOperator;
import es.us.isa.jsonmutator.mutator.array.operator.ArrayRemoveElementOperator;
import es.us.isa.jsonmutator.mutator.object.ObjectMutator;
import es.us.isa.jsonmutator.mutator.object.operator.ObjectAddElementOperator;
import es.us.isa.jsonmutator.mutator.object.operator.ObjectRemoveElementOperator;
import es.us.isa.jsonmutator.mutator.value.boolean0.BooleanMutator;
import es.us.isa.jsonmutator.mutator.value.double0.DoubleMutator;
import es.us.isa.jsonmutator.mutator.value.long0.LongMutator;
import es.us.isa.jsonmutator.mutator.value.null0.NullMutator;
import es.us.isa.jsonmutator.mutator.value.string0.StringMutator;
import es.us.isa.jsonmutator.util.OperatorNames;

import static es.us.isa.jsonmutator.util.PropertyManager.readProperty;

/**
 * Class to manage mutation of JSON objects. Also works with JSON arrays.
 *
 * @author Alberto Martin-Lopez
 */
public class JsonMutator {

    private boolean firstIteration; // True when mutateJSON is called the first time, false when it's called recursively
    private int jsonSize; // For Single Order Mutation (SOM): Size of JSON (sum of all object properties and array elements)
    private int jsonSizeProgress; // For SOM: Progressive size of JSON (increases on each iteration)
    private Integer propertyIndex; // For SOM: Index of property (counting the whole JSON) to mutate
    private boolean mutationApplied; // For SOM: True if the mutation was applied. Used to stop iterating
    private boolean singleOrderActive; // True if single order mutation was used in the previous execution

    private StringMutator stringMutator;
    private LongMutator longMutator;
    private DoubleMutator doubleMutator;
    private BooleanMutator booleanMutator;
    private NullMutator nullMutator;
    private ObjectMutator objectMutator;
    private ArrayMutator arrayMutator;

    public JsonMutator() {
        firstIteration = true;
        jsonSize = 0;
        jsonSizeProgress = 0;
        propertyIndex = null;
        mutationApplied = false;
        singleOrderActive = false;

        resetMutators();
    }

    /**
     * Perform mutations on a JsonNode, either single or multiple order.
     *
     * @param jsonNode The JsonNode to mutate. Careful!: As of now, this parameter
     *                 gets mutated after the function is calles.
     * @param singleOrder True if you want to apply only one mutation.
     * @return The mutated JsonNode.
     */
    public JsonNode mutateJson(JsonNode jsonNode, boolean singleOrder) {
        if (singleOrder) {
            if (!singleOrderActive) // If the last call to this function was with singleOrder=false, then...
                setUpSingleOrderMutation(); // ... set up parameters for single order mutation...
            singleOrderActive = true; // ... and keep track of this update for next function call
            return singleOrderMutation(jsonNode);
        } else {
            if (singleOrderActive) // If the last call to this function was with singleOrder=true, then...
                resetMutators(); // ... set up parameters for multiple order mutation
            singleOrderActive = false; // ... and keep track of this update for next function call
            return multipleOrderMutation(jsonNode);
        }
    }

    /**
     * Auxiliary function to set up the JsonMutator for single order mutations.
     * Basically, probabilities of all mutators are set to 1, and for object and
     * array mutators, only one mutation is allowed and only one element can be
     * added or removed. That way, only one change is made at a time.
     */
    private void setUpSingleOrderMutation() {
        if (Boolean.parseBoolean(readProperty("operator.value.string.enabled")))
            stringMutator.setProb(1);
        if (Boolean.parseBoolean(readProperty("operator.value.long.enabled")))
            longMutator.setProb(1);
        if (Boolean.parseBoolean(readProperty("operator.value.double.enabled")))
            doubleMutator.setProb(1);
        if (Boolean.parseBoolean(readProperty("operator.value.boolean.enabled")))
            booleanMutator.setProb(1);
        if (Boolean.parseBoolean(readProperty("operator.value.null.enabled")))
            nullMutator.setProb(1);
        if (Boolean.parseBoolean(readProperty("operator.object.enabled"))) {
            objectMutator.setProb(1);
            objectMutator.setMinMutations(1);
            objectMutator.setMaxMutations(1);
            ((ObjectAddElementOperator)objectMutator.getOperators().get(OperatorNames.ADD_ELEMENT)).setMinAddedProperties(1);
            ((ObjectAddElementOperator)objectMutator.getOperators().get(OperatorNames.ADD_ELEMENT)).setMaxAddedProperties(1);
            ((ObjectRemoveElementOperator)objectMutator.getOperators().get(OperatorNames.REMOVE_ELEMENT)).setMinRemovedProperties(1);
            ((ObjectRemoveElementOperator)objectMutator.getOperators().get(OperatorNames.REMOVE_ELEMENT)).setMaxRemovedProperties(1);
        }
        if (Boolean.parseBoolean(readProperty("operator.array.enabled"))) {
            arrayMutator.setProb(1);
            arrayMutator.setMinMutations(1);
            arrayMutator.setMaxMutations(1);
            ((ArrayAddElementOperator)arrayMutator.getOperators().get(OperatorNames.ADD_ELEMENT)).setMinAddedElements(1);
            ((ArrayAddElementOperator)arrayMutator.getOperators().get(OperatorNames.ADD_ELEMENT)).setMaxAddedElements(1);
            ((ArrayRemoveElementOperator)arrayMutator.getOperators().get(OperatorNames.REMOVE_ELEMENT)).setMinRemovedElements(1);
            ((ArrayRemoveElementOperator)arrayMutator.getOperators().get(OperatorNames.REMOVE_ELEMENT)).setMaxRemovedElements(1);
        }
    }

    /**
     * Auxiliary function to set up the JsonMutator for multiple order mutations.
     * All mutators are re-instantiated, so that their properties are reset
     * according to the properties file.
     */
    private void resetMutators() {
        if (Boolean.parseBoolean(readProperty("operator.value.string.enabled")))
            stringMutator = new StringMutator();
        if (Boolean.parseBoolean(readProperty("operator.value.long.enabled")))
            longMutator = new LongMutator();
        if (Boolean.parseBoolean(readProperty("operator.value.double.enabled")))
            doubleMutator = new DoubleMutator();
        if (Boolean.parseBoolean(readProperty("operator.value.boolean.enabled")))
            booleanMutator = new BooleanMutator();
        if (Boolean.parseBoolean(readProperty("operator.value.null.enabled")))
            nullMutator = new NullMutator();
        if (Boolean.parseBoolean(readProperty("operator.object.enabled")))
            objectMutator = new ObjectMutator();
        if (Boolean.parseBoolean(readProperty("operator.array.enabled")))
            arrayMutator = new ArrayMutator();
    }

    /**
     * Apply a single mutation to a JSON object. This is done in the following way:
     * First, the JSON is fully iterated over, counting the total number of properties
     * among all objects and arrays. Then, a random number (propertyIndex) between
     * -1 and jsonSize is generated. If -1, mutate first-level JSON; otherwise,
     * start a second iteration phase where the property with that index will be
     * looked for and mutated.
     *
     * @param jsonNode The JSON to mutate.
     * @return The mutated JSON.
     */
    private JsonNode singleOrderMutation(JsonNode jsonNode) {
        boolean firstIterationOccurred = false; // Used to reset the state of firstIteration attribute
        JsonNode jsonNodeCopy = jsonNode;
        if (firstIteration) {
            firstIteration = false;
            firstIterationOccurred = true;
            jsonNodeCopy = jsonNode.deepCopy(); // Make a deep copy so that the input object is not altered
        }

        if (propertyIndex == null) { // If a property to mutate has not been selected yet
            jsonSize += jsonNodeCopy.size(); // Keep counting the size of the JSON
        } else {
            if (propertyIndex == -1) { // If what has to be mutated is the actual first-level JSON
                if (objectMutator != null && jsonNodeCopy.isObject())
                    jsonNodeCopy = objectMutator.getMutatedNode(jsonNodeCopy);
                else if (arrayMutator != null && jsonNodeCopy.isArray())
                    jsonNodeCopy = arrayMutator.getMutatedNode(jsonNodeCopy);
                mutationApplied = true;
            // If property to mutate is in the current object/array being iterated:
            } else if (propertyIndex >= jsonSizeProgress && propertyIndex < jsonSizeProgress + jsonNodeCopy.size()) {
                if (jsonNodeCopy.isObject())
                    mutateElement(jsonNodeCopy, Lists.newArrayList(jsonNodeCopy.fieldNames()).get(propertyIndex-jsonSizeProgress), null);
                else if (jsonNodeCopy.isArray())
                    mutateElement(jsonNodeCopy, null, propertyIndex-jsonSizeProgress);
                mutationApplied = true;
            }
            jsonSizeProgress += jsonNodeCopy.size(); // Add size of the current object/array
        }

        if (!mutationApplied) {
            Iterator<JsonNode> jsonIterator = jsonNodeCopy.elements();
            while (jsonIterator.hasNext()) { // Keep iterating the JSON...
                if (mutationApplied) // ... unless the mutation was applied, then stop iterating
                    break;
                JsonNode subJsonNode = jsonIterator.next();
                if (subJsonNode.isContainerNode()) { // Iterate over properties that are arrays or objects
                    singleOrderMutation(subJsonNode);
                }
            }
        }

        // At the end of the first iteration, jsonSize will have been populated with the total size of the full JSON:
        if (firstIterationOccurred) {
            propertyIndex = (new RandomDataGenerator()).nextInt(0, jsonSize) - 1; // If -1, mutate first level JSON
            singleOrderMutation(jsonNodeCopy); // Once propertyIndex is set, start iterating again, looking for the property
            if (propertyIndex == -1)
                singleOrderActive = false; // Reset because operators of object or array were set to default values
            // Reset variables for the next time this function will be called:
            firstIteration = true;
            jsonSize = 0;
            jsonSizeProgress = 0;
            propertyIndex = null;
            mutationApplied = false;
        }
        return jsonNodeCopy;
    }

    /**
     * Apply some random mutations to a JSON object. These mutations are applied to sub-objects and
     * sub-arrays recursively: add new properties, remove existing properties, mutate existing
     * properties (numbers, strings, booleans, etc.), make existing properties null, leave existing
     * objects and arrays empty ({} or []), etc.
     *
     * @param jsonNode The JSON to mutate
     * @return The mutated JSON
     */
    private JsonNode multipleOrderMutation(JsonNode jsonNode) {
        boolean firstIterationOccurred = false; // Used to reset the state of firstIteration attribute
        JsonNode jsonNodeCopy = jsonNode;
        if (firstIteration) {
            firstIteration = false; // Set to false so that this block is not entered again when recursively calling the function
            firstIterationOccurred = true; // Set to true so that firstIteration is reset to true at the end of this call
            jsonNodeCopy = jsonNode.deepCopy(); // Make a deep copy so that the input object is not altered
            if (objectMutator != null && jsonNodeCopy.isObject())
                jsonNodeCopy = objectMutator.getMutatedNode(jsonNodeCopy);
            else if (arrayMutator != null && jsonNodeCopy.isArray())
                jsonNodeCopy = arrayMutator.getMutatedNode(jsonNodeCopy);
        }

        if (jsonNodeCopy.isObject()) { // If node is object
            Iterator<String> keysIterator = jsonNodeCopy.fieldNames();
            String propertyName;
            while (keysIterator.hasNext()) { // Iterate over each object property
                propertyName = keysIterator.next();
                mutateElement(jsonNodeCopy, propertyName, null); // (Possibly) mutate each property and...
                if (jsonNodeCopy.get(propertyName).isObject() || jsonNodeCopy.get(propertyName).isArray()) // ...if property is object or array...
                    ((ObjectNode)jsonNodeCopy).replace(propertyName, multipleOrderMutation(jsonNodeCopy.get(propertyName))); // ...recursively call this function
            }
        } else if (jsonNodeCopy.isArray()) { // If node is array
            for (int arrayIndex=0; arrayIndex<jsonNodeCopy.size(); arrayIndex++) { // Iterate over each array element
                mutateElement(jsonNodeCopy, null, arrayIndex); // (Possibly) mutate each element and...
                if (jsonNodeCopy.get(arrayIndex).isObject() || jsonNodeCopy.get(arrayIndex).isArray()) // ...if element is object or array...
                    ((ArrayNode)jsonNodeCopy).set(arrayIndex, multipleOrderMutation(jsonNodeCopy.get(arrayIndex))); // ...recursively call this function
            }
        } else {
            throw new IllegalArgumentException("Wrong type: " + jsonNodeCopy.getNodeType() + ". The function" +
                    "'mutateJSON(JsonNode)' only accepts two parameter types: ObjectNode and ArrayNode");
        }

        if (firstIterationOccurred)
            firstIteration = true; // Reset for the next time this function will be called
        return jsonNodeCopy;
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
        } else if (doubleMutator!=null && element.isFloatingPointNumber()) {
            if (isObj) doubleMutator.mutate((ObjectNode)jsonNode, propertyName);
            else doubleMutator.mutate((ArrayNode) jsonNode, index);
        } else if (stringMutator!=null && element.isTextual()) {
            if (isObj) stringMutator.mutate((ObjectNode)jsonNode, propertyName);
            else stringMutator.mutate((ArrayNode) jsonNode, index);
        } else if (booleanMutator!=null && element.isBoolean()) {
            if (isObj) booleanMutator.mutate((ObjectNode)jsonNode, propertyName);
            else booleanMutator.mutate((ArrayNode) jsonNode, index);
        } else if (nullMutator!=null && element.isNull()) {
            if (isObj) nullMutator.mutate((ObjectNode)jsonNode, propertyName);
            else nullMutator.mutate((ArrayNode) jsonNode, index);
        } else if (objectMutator!=null && element.isObject()) {
            if (isObj) objectMutator.mutate((ObjectNode)jsonNode, propertyName);
            else objectMutator.mutate((ArrayNode) jsonNode, index);
        } else if (arrayMutator!=null && element.isArray()) {
            if (isObj) arrayMutator.mutate((ObjectNode) jsonNode, propertyName);
            else arrayMutator.mutate((ArrayNode) jsonNode, index);
        }
    }
}









