package es.us.isa.restmutator;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.*;
import com.google.common.collect.Lists;
import org.apache.commons.lang3.RandomStringUtils;

import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Random;

public class Mutator {

    private float addPropertyProb;      // Probability to add a new property to an object or array
    private float removePropertyProb;   // Probability to remove a property from an object or array
    private float mutateValueProb;      // Probability to mutate a property
    private float makeNullProb;         // Probability to make a property null
    private float leaveEmptyProb;       // Probability to leave an object ({}) or array ([]) empty
    private int minInt;                 // Minimum int that can be randomly generated
    private int maxInt;                 // Maximum int that can be randomly generated
    private int maxStringLength;        // Maximum length of a randomly generated string
    private int deltaInt;               // Int to add or subtract when mutating an int

    public Mutator() {
        addPropertyProb = 0.1f;
        removePropertyProb = 0.1f;
        mutateValueProb = 0.1f;
        makeNullProb = 0.1f;
        leaveEmptyProb = 0.1f;
        minInt = -1000000;
        maxInt = 1000000;
        maxStringLength = 10;
        deltaInt = 1;
    }

    public float getAddPropertyProb() {
        return addPropertyProb;
    }

    public void setAddPropertyProb(float addPropertyProb) {
        this.addPropertyProb = addPropertyProb;
    }

    public float getRemovePropertyProb() {
        return removePropertyProb;
    }

    public void setRemovePropertyProb(float removePropertyProb) {
        this.removePropertyProb = removePropertyProb;
    }

    public float getMutateValueProb() {
        return mutateValueProb;
    }

    public void setMutateValueProb(float mutateValueProb) {
        this.mutateValueProb = mutateValueProb;
    }

    public float getMakeNullProb() {
        return makeNullProb;
    }

    public void setMakeNullProb(float makeNullProb) {
        this.makeNullProb = makeNullProb;
    }

    public float getLeaveEmptyProb() {
        return leaveEmptyProb;
    }

    public void setLeaveEmptyProb(float leaveEmptyProb) {
        this.leaveEmptyProb = leaveEmptyProb;
    }

    public int getMinInt() {
        return minInt;
    }

    public void setMinInt(int minInt) {
        this.minInt = minInt;
    }

    public int getMaxInt() {
        return maxInt;
    }

    public void setMaxInt(int maxInt) {
        this.maxInt = maxInt;
    }

    public int getMaxStringLength() {
        return maxStringLength;
    }

    public void setMaxStringLength(int maxStringLength) {
        this.maxStringLength = maxStringLength;
    }

    public int getDeltaInt() {
        return deltaInt;
    }

    public void setDeltaInt(int deltaInt) {
        this.deltaInt = deltaInt;
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
        Random random = new Random();

        if (jsonNode.isObject()) {
            // First, add and/or remove a property to the JSON with a certain probability
            addProperty((ObjectNode)jsonNode);
            removeProperty((ObjectNode)jsonNode);

            // Then, iterate over each property
            Iterator<Map.Entry<String,JsonNode>> objectPropertiesIterator = jsonNode.fields();
            Map.Entry<String,JsonNode> jsonProperty;
            while (objectPropertiesIterator.hasNext()) {
                jsonProperty = objectPropertiesIterator.next();

                // With a certain probability, make the object null or empty
                if (random.nextFloat() <= makeNullProb) {
                    ((ObjectNode)jsonNode).putNull(jsonProperty.getKey());
                } else if (random.nextFloat() <= leaveEmptyProb) {
                    if (jsonProperty.getValue().isObject()) {
                        ((ObjectNode)jsonNode).putObject(jsonProperty.getKey());
                    } else if (jsonProperty.getValue().isArray()) {
                        ((ObjectNode)jsonNode).putArray(jsonProperty.getKey());
                    }
                }

                // Otherwise, mutate it as usual with a certain probability
                else if (jsonProperty.getValue().isObject() || jsonProperty.getValue().isArray()) {
                    // If the property is an object or array, recursively call this function
                    jsonProperty.setValue(mutateJSON(jsonProperty.getValue()));
                } else {
                    // If the property is a number, string, etc., mutate it with a certain probability
                    mutateProperty((ObjectNode)jsonNode, jsonProperty);
                }
            }
        } else if (jsonNode.isArray()) {
            // First, add and/or remove an element to the array with a certain probability
            addProperty((ArrayNode)jsonNode);
            removeProperty((ArrayNode)jsonNode);

            // Then, iterate over each element
            Iterator<JsonNode> arrayElements = jsonNode.elements();
            JsonNode arrayElement;
            int arrayIndex = -1;
            while (arrayElements.hasNext()) {
                arrayElement = arrayElements.next();
                arrayIndex++;

                // With a certain probability, make the array null or empty
                if (random.nextFloat() <= makeNullProb) {
                    ((ArrayNode)jsonNode).set(arrayIndex, NullNode.getInstance());
                } else if (random.nextFloat() <= leaveEmptyProb) {
                    if (arrayElement.isObject()) {
                        ((ArrayNode)jsonNode).set(arrayIndex, new ObjectNode(JsonNodeFactory.instance));
                    } else if (arrayElement.isArray()) {
                        ((ArrayNode)jsonNode).set(arrayIndex, new ArrayNode(JsonNodeFactory.instance));
                    }
                }

                // Otherwise, mutate it as usual with a certain probability
                else if (arrayElement.isObject() || arrayElement.isArray()) {
                    // If the element is an object or array, recursively call this function
                    mutateJSON(arrayElement);
                } else {
                    // If the property is a number, string, etc., mutate it with a certain probability
                    mutateProperty((ArrayNode)jsonNode, arrayElement, arrayIndex);
                }
            }
        }

        return jsonNode;
    }

    /**
     * Add a property to a JSON object with a certain probability.
     *
     * @param objectNode The object where to add the property
     * @return The (possibly) mutated object
     */
    private ObjectNode addProperty(ObjectNode objectNode) {
        Random random = new Random();
        if (random.nextFloat() <= addPropertyProb) {
            float randomValue = random.nextFloat(); // This random value decides on the type of value to add (int, string, etc.)
            if (randomValue <= 1f/7) {
                objectNode.put("randomInt", random.nextInt((maxInt-minInt)+1) + minInt);
            } else if (randomValue <= 2f/7) {
                objectNode.put("randomFloat", random.nextInt((maxInt-minInt)+1) + minInt + random.nextFloat());
            } else if (randomValue <= 3f/7) {
                objectNode.put("randomBoolean", random.nextBoolean());
            } else if (randomValue <= 4f/7) {
                objectNode.put("randomString", RandomStringUtils.random(random.nextInt(maxStringLength)+1, true, true));
            } else if (randomValue <= 5f/7) {
                objectNode.putNull("randomNull");
            } else if (randomValue <= 6f/7) {
                objectNode.putObject("randomObject");
            } else if (randomValue <= 1) {
                objectNode.putArray("randomArray");
            }
        }

        return objectNode;
    }

    /**
     * Add an element to an array with a certain probability.
     *
     * @param arrayNode The array where to add the element
     * @return The (possibly) mutated array
     */
    private ArrayNode addProperty(ArrayNode arrayNode) {
        Random random = new Random();
        if (random.nextFloat() <= addPropertyProb) {
            float randomValue = random.nextFloat(); // This random value decides on the type of value to add (int, string, etc.)
            if (randomValue <= 1f/7) {
                arrayNode.add(random.nextInt((maxInt-minInt)+1) + minInt);
            } else if (randomValue <= 2f/7) {
                arrayNode.add(random.nextInt((maxInt-minInt)+1) + minInt + random.nextFloat());
            } else if (randomValue <= 3f/7) {
                arrayNode.add(random.nextBoolean());
            } else if (randomValue <= 4f/7) {
                arrayNode.add(RandomStringUtils.random(random.nextInt(maxStringLength)+1, true, true));
            } else if (randomValue <= 5f/7) {
                arrayNode.addNull();
            } else if (randomValue <= 6f/7) {
                arrayNode.addObject();
            } else if (randomValue <= 1) {
                arrayNode.addArray();
            }
        }

        return arrayNode;
    }

    /**
     * Remove a property from an object with a certain probability.
     *
     * @param objectNode The object where to remove the property from
     * @return The (possibly) mutated object
     */
    private ObjectNode removeProperty(ObjectNode objectNode) {
        Random random = new Random();
        if (random.nextFloat() <= removePropertyProb) {
            List<String> propertyNames = Lists.newArrayList(objectNode.fieldNames());
            if (propertyNames.size() > 0)
                objectNode.remove(propertyNames.get(random.nextInt(propertyNames.size()))); // Remove a random property
        }

        return objectNode;
    }

    /**
     * Remove an element from an array with a certain probability.
     *
     * @param arrayNode The array where to remove the element from
     * @return The (possibly) mutated array
     */
    private ArrayNode removeProperty(ArrayNode arrayNode) {
        Random random = new Random();
        if (random.nextFloat() <= removePropertyProb) {
            if (arrayNode.size() > 0)
                arrayNode.remove(random.nextInt(arrayNode.size())); // Remove a random element
        }

        return arrayNode;
    }

    /**
     * Mutate a property of a JSON object with a certain probability.
     *
     * @param objectNode The JSON object where the property is contained
     * @param jsonProperty The JSON property to mutate
     * @return The JSON object passed in with the (possibly) mutated property
     */
    private ObjectNode mutateProperty(ObjectNode objectNode, Map.Entry<String,JsonNode> jsonProperty) {
        Random random = new Random();
        if (random.nextFloat() <= mutateValueProb) {
            if (jsonProperty.getValue().isIntegralNumber()) {
                objectNode.put(jsonProperty.getKey(), jsonProperty.getValue().longValue() + (random.nextBoolean() ? deltaInt : -deltaInt));
            } else if (jsonProperty.getValue().isFloatingPointNumber()) {
                objectNode.put(jsonProperty.getKey(), jsonProperty.getValue().doubleValue() + (random.nextBoolean() ? random.nextFloat() : -random.nextFloat()));
            } else if (jsonProperty.getValue().isTextual()) {
                objectNode.put(jsonProperty.getKey(), mutateString(jsonProperty.getValue().asText()));
            } else if (jsonProperty.getValue().isBoolean()) {
                objectNode.put(jsonProperty.getKey(), !jsonProperty.getValue().asBoolean());
            }
        }

        return objectNode;
    }

    /**
     * Mutate an element of an array with a certain probability.
     *
     * @param arrayNode The array where the element is contained
     * @param arrayElement The array element to mutate
     * @param index Position of the element in the array (needed to access it)
     * @return The array passed in with the (possibly) mutated element
     */
    private ArrayNode mutateProperty(ArrayNode arrayNode, JsonNode arrayElement, int index) {
        Random random = new Random();
        if (random.nextFloat() <= mutateValueProb) {
            if (arrayElement.isIntegralNumber()) {
                arrayNode.set(index, new LongNode(arrayElement.longValue() + (random.nextBoolean() ? deltaInt : -deltaInt)));
            } else if (arrayElement.isFloatingPointNumber()) {
                arrayNode.set(index, new DoubleNode(arrayElement.doubleValue() + (random.nextBoolean() ? random.nextFloat() : -random.nextFloat())));
            } else if (arrayElement.isTextual()) {
                arrayNode.set(index, new TextNode(mutateString(arrayElement.asText())));
            } else if (arrayElement.isBoolean()) {
                arrayNode.set(index, arrayElement.asBoolean() ? BooleanNode.FALSE : BooleanNode.TRUE);
            }
        }

        return arrayNode;
    }

    /**
     * Apply a random mutation to a string. Only one of the following mutations at a time: remove
     * character, add character, change character.
     *
     * @param string The string to mutate
     * @return The mutated string
     */
    private String mutateString(String string) {
        Random random = new Random();
        int charPosition = random.nextInt(string.length());
        StringBuilder sb = new StringBuilder(string);

        float randomValue = random.nextFloat();
        if (randomValue <= 1f/3) { // Remove char
            sb.deleteCharAt(charPosition);
        } else if (randomValue <= 2f/3) { // Add char
            sb.insert(charPosition, RandomStringUtils.random(1, true, true));
        } else if (randomValue <= 1) { // Change char
            sb.deleteCharAt(charPosition);
            sb.insert(charPosition, RandomStringUtils.random(1, true, true));
        }

        return sb.toString();
    }

}
