package es.us.isa.restmutator.util;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.*;

/**
 * Class to manage insertion, deletion and replacement of elements in JSON (objects
 * and arrays).
 *
 * @author Alberto Martin-Lopez
 */
public class JsonManager {

    /**
     * Insert (or replace) element in a JSON object
     *
     * @param objectNode The object where to insert the property
     * @param propertyName The name of the property to insert
     * @param element The value of the inserted property
     */
    public static void insertElement(ObjectNode objectNode, String propertyName, Object element) {
        if (element instanceof String) {
            objectNode.put(propertyName, (String)element);
        } else if (element instanceof Long) {
            objectNode.put(propertyName, (Long)element);
        } else if (element instanceof Double) {
            objectNode.put(propertyName, (Double)element);
        } else if (element instanceof Boolean) {
            objectNode.put(propertyName, (Boolean)element);
        } else if (element instanceof ObjectNode) {
            objectNode.replace(propertyName, (ObjectNode)element);
        } else if (element instanceof ArrayNode) {
            objectNode.replace(propertyName, (ArrayNode)element);
        } else if (element == null) {
            objectNode.putNull(propertyName);
        } else {
            throw new IllegalArgumentException("The element to insert must be a string, int, float, boolean, " +
                    "object, array or null value.");
        }
    }

    /**
     * Replace element in an array. NOTE: This does not insert a new element like
     * {@link JsonManager#insertElement} can do, it always replaces an old one.
     *
     * @param arrayNode The array where to replace the element
     * @param index The index position in the array where to replace the element
     * @param element The value of the element after being replaced
     */
    public static void insertElement(ArrayNode arrayNode, int index, Object element) {
        if (element instanceof String) {
            arrayNode.set(index, new TextNode((String)element));
        } else if (element instanceof Long) {
            arrayNode.set(index, new LongNode((Long)element));
        } else if (element instanceof Double) {
            arrayNode.set(index, new DoubleNode((Double)element));
        } else if (element instanceof Boolean) {
            arrayNode.set(index, (Boolean)element ? BooleanNode.TRUE : BooleanNode.FALSE);
        } else if (element instanceof ObjectNode) {
            arrayNode.set(index, (ObjectNode)element);
        } else if (element instanceof ArrayNode) {
            arrayNode.set(index, (ArrayNode)element);
        } else if (element == null) {
            arrayNode.set(index, null);
        } else {
            throw new IllegalArgumentException("The element to insert must be a string, int, float, boolean, " +
                    "object, array or null value.");
        }
    }
}
