package es.us.isa.jsonmutator.util;

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
     * Replace element in an object or array. NOTE: In the case of an object, if the
     * property didn't exist, it creates it (unlike with arrays, where the element is
     * always replaced).
     *
     * @param jsonNode The object or array where to replace the element
     * @param element The value of the element after being replaced
     * @param propertyName The name of the property to replace. Must be null if jsonNode
     *                     is an ArrayNode
     * @param index The index position in the array where to replace the element. Must
     *              be null if jsonNode is an ObjectNode
     */
    public static void insertElement(JsonNode jsonNode, Object element, String propertyName, Integer index) {
        boolean isObj = index==null; // If index==null, jsonNode is an object, otherwise it is an array
        if (element instanceof String) {
            if (isObj) ((ObjectNode)jsonNode).put(propertyName, (String)element);
            else ((ArrayNode)jsonNode).set(index, new TextNode((String)element));
        } else if (element instanceof Long) {
            if (isObj) ((ObjectNode)jsonNode).put(propertyName, (Long)element);
            else ((ArrayNode)jsonNode).set(index, new LongNode((Long)element));
        } else if (element instanceof Double) {
            if (isObj) ((ObjectNode)jsonNode).put(propertyName, (Double)element);
            else ((ArrayNode)jsonNode).set(index, new DoubleNode((Double)element));
        } else if (element instanceof Boolean) {
            if (isObj) ((ObjectNode)jsonNode).put(propertyName, (Boolean)element);
            else ((ArrayNode)jsonNode).set(index, (Boolean)element ? BooleanNode.TRUE : BooleanNode.FALSE);
        } else if (element instanceof ObjectNode) {
            if (isObj) ((ObjectNode)jsonNode).replace(propertyName, (ObjectNode)element);
            else ((ArrayNode)jsonNode).set(index, (ObjectNode)element);
        } else if (element instanceof ArrayNode) {
            if (isObj) ((ObjectNode)jsonNode).replace(propertyName, (ArrayNode)element);
            else ((ArrayNode)jsonNode).set(index, (ArrayNode)element);
        } else if (element == null) {
            if (isObj) ((ObjectNode)jsonNode).putNull(propertyName);
            else ((ArrayNode)jsonNode).set(index, null);
        } else {
            throw new IllegalArgumentException("The element to insert must be a string, int, float, boolean, " +
                    "object, array or null value.");
        }
    }
}
