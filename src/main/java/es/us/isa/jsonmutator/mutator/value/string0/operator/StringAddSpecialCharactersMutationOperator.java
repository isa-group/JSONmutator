package es.us.isa.jsonmutator.mutator.value.string0.operator;

import es.us.isa.jsonmutator.mutator.AbstractOperator;
import es.us.isa.jsonmutator.util.OperatorNames;
import org.apache.commons.lang3.RandomStringUtils;

import static es.us.isa.jsonmutator.util.PropertyManager.readProperty;

import java.util.ArrayList;
import java.util.List;

/**
 * Operator that mutates a string by adding special characters like "/", "*", and ",".
 *
 * @author Ana Belén Sánchez
 */
public class StringAddSpecialCharactersMutationOperator extends AbstractOperator {

    public StringAddSpecialCharactersMutationOperator() {
    	 super();
         weight = Float.parseFloat(readProperty("operator.value.string.weight." + OperatorNames.ADD_SPECIAL_CHARACTERS));
     }

     public Object mutate(Object stringObject) {
         String string = (String)stringObject;
         StringBuilder sb = new StringBuilder(string);
         if(string.length()>0) {
         
         	List<String> specialCharacters = new ArrayList<String>();
         	specialCharacters.add("/"); specialCharacters.add("*"); specialCharacters.add(","); 
         	specialCharacters.add("´"); specialCharacters.add("´*");specialCharacters.add("/*"); 
         	specialCharacters.add("/,"); specialCharacters.add("*,"); specialCharacters.add("´/"); 
         	specialCharacters.add("´,");
         	
         	int charPosition = rand1.nextInt(0, string.length()-1);
         	int posRandomCharacter = rand1.nextInt(0, specialCharacters.size()-1);

         	//Inserting special character
         	sb.insert(charPosition, specialCharacters.get(posRandomCharacter));
     
         }

         return sb.toString();
     }
 } 