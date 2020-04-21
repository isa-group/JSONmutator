package es.us.isa.jsonmutator.mutator.object.operator;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.google.common.collect.Lists;

import es.us.isa.jsonmutator.mutator.AbstractOperator;
import es.us.isa.jsonmutator.util.OperatorNames;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map.Entry;
import static es.us.isa.jsonmutator.util.PropertyManager.readProperty;

/**
 * Operator that mutates an object by removing a number of object-type properties from it.
 *
 * @author Ana Belén Sánchez
 */
public class ObjectRemoveObjectTypeElementOperator extends AbstractOperator {

	 private int maxRemovedProperties;     // Maximum number of object-type properties to remove to the object
	 private int minRemovedProperties;     // Minimum number of object-type properties to remove to the object
	 
    public ObjectRemoveObjectTypeElementOperator() {
        super();
        weight = Float.parseFloat(readProperty("operator.object.weight." + OperatorNames.REMOVE_OBJECT_ELEMENT));
        maxRemovedProperties = Integer.parseInt(readProperty("operator.object.removeObjectElement.max"));
        minRemovedProperties = Integer.parseInt(readProperty("operator.object.removeObjectElement.min"));
    }
    
    public int getMaxRemovedProperties() {
        return maxRemovedProperties;
    }

    public void setMaxRemovedProperties(int maxRemovedProperties) {
        this.maxRemovedProperties = maxRemovedProperties;
    }

    public int getMinRemovedProperties() {
        return minRemovedProperties;
    }

    public void setMinRemovedProperties(int minRemovedProperties) {
        this.minRemovedProperties = minRemovedProperties;
    }


    public Object mutate(Object objectNodeObject) {
        ObjectNode objectNode = (ObjectNode)objectNodeObject;
        int randomProperty;
        int removedProperties = rand1.nextInt(minRemovedProperties, maxRemovedProperties); // Remove between min and max properties to object
        List<String> objectPropertyNames = new ArrayList<String>();
        Iterator<Entry<String, JsonNode>> it = objectNode.fields();
        Entry<String, JsonNode> property ;
        
        while(it.hasNext()) { //Identify the object-type properties
    		property = it.next();
    		if(property.getValue().isObject()){
    			objectPropertyNames = Lists.newArrayList(property.getKey());
    		}
        }
        
        for (int i=1; i<=removedProperties; i++) {
        	if (objectPropertyNames.size() > 0) { // If there are object-type properties identified
        		randomProperty = rand2.nextInt(objectPropertyNames.size());
        		objectNode.remove(objectPropertyNames.get(randomProperty)); // Remove a random object-type property
        		objectPropertyNames.remove(randomProperty); 
        	}
    	}
      return objectNode;
    }
}
