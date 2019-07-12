package es.us.isa.restmutator;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.*;
import com.google.common.collect.Lists;
import org.apache.commons.lang3.RandomStringUtils;
import java.util.*;

public class Mutator {

    private static final float addPropertyProb = 0.2f;
    private static final float removePropertyProb = 0.2f;
    private static final float mutateValueProb = 0.2f;
    private static final int minInt = -1000000;
    private static final int maxInt = 1000000;
    private static final int maxStringLength = 10;
    private static final int deltaInt = 1;

    public static JsonNode mutateJSON(JsonNode jsonNode) {

        if (jsonNode.isObject()) {
            // First, add and/or remove a property to the JSON with a certain probability
            addProperty((ObjectNode)jsonNode);
            removeProperty((ObjectNode)jsonNode);

            // Then, iterate over each property
            Iterator<Map.Entry<String,JsonNode>> objectPropertiesIterator = jsonNode.fields();
            Map.Entry<String,JsonNode> jsonProperty;
            while (objectPropertiesIterator.hasNext()) {
                jsonProperty = objectPropertiesIterator.next();
                if (jsonProperty.getValue().isObject() || jsonProperty.getValue().isArray()) {
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
                if (arrayElement.isObject() || arrayElement.isArray()) {
                    // If the property is an object or array, recursively call this function
                    mutateJSON(arrayElement);
                } else {
                    // If the property is a number, string, etc., mutate it with a certain probability
                    mutateProperty((ArrayNode)jsonNode, arrayElement, arrayIndex);
                }
            }
        }

        return jsonNode;
    }

    private static ObjectNode addProperty(ObjectNode objectNode) {
        Random random = new Random();
        if (random.nextFloat() <= addPropertyProb) {
            float randomValue = random.nextFloat();
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

    private static ArrayNode addProperty(ArrayNode arrayNode) {
        Random random = new Random();
        if (random.nextFloat() <= addPropertyProb) {
            float randomValue = random.nextFloat();
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

    private static ObjectNode removeProperty(ObjectNode objectNode) {
        Random random = new Random();
        if (random.nextFloat() <= removePropertyProb) {
            List<String> propertyNames = Lists.newArrayList(objectNode.fieldNames());
            objectNode.remove(propertyNames.get(random.nextInt(propertyNames.size()))); // Remove a random property
        }

        return objectNode;
    }

    private static ArrayNode removeProperty(ArrayNode arrayNode) {
        Random random = new Random();
        if (random.nextFloat() <= removePropertyProb) {
            arrayNode.remove(random.nextInt(arrayNode.size())); // Remove a random property
        }

        return arrayNode;
    }

    private static ObjectNode mutateProperty(ObjectNode objectNode, Map.Entry<String,JsonNode> jsonProperty) {
        Random random = new Random();
        if (random.nextFloat() <= mutateValueProb) {
            if (jsonProperty.getValue().isIntegralNumber()) {
                objectNode.put(jsonProperty.getKey(), jsonProperty.getValue().longValue() + (random.nextBoolean() ? deltaInt : -deltaInt));
            } else if (jsonProperty.getValue().isFloatingPointNumber()) {
                objectNode.put(jsonProperty.getKey(), jsonProperty.getValue().longValue() + (random.nextBoolean() ? random.nextFloat() : -random.nextFloat()));
            } else if (jsonProperty.getValue().isTextual()) {
                objectNode.put(jsonProperty.getKey(), mutateString(jsonProperty.getValue().asText()));
            } else if (jsonProperty.getValue().isBoolean()) {
                objectNode.put(jsonProperty.getKey(), !jsonProperty.getValue().asBoolean());
            }
        }

        return objectNode;
    }

    private static ArrayNode mutateProperty(ArrayNode arrayNode, JsonNode jsonNode, int index) {
        Random random = new Random();
        if (random.nextFloat() <= mutateValueProb) {
            if (jsonNode.isIntegralNumber()) {
                arrayNode.set(index, new LongNode(jsonNode.longValue() + (random.nextBoolean() ? deltaInt : -deltaInt)));
            } else if (jsonNode.isFloatingPointNumber()) {
                arrayNode.set(index, new FloatNode(jsonNode.longValue() + (random.nextBoolean() ? random.nextFloat() : -random.nextFloat())));
            } else if (jsonNode.isTextual()) {
                arrayNode.set(index, new TextNode(mutateString(jsonNode.asText())));
            } else if (jsonNode.isBoolean()) {
                arrayNode.set(index, jsonNode.asBoolean() ? BooleanNode.FALSE : BooleanNode.TRUE);
            }
        }

        return arrayNode;
    }

    private static String mutateString(String string) {
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
