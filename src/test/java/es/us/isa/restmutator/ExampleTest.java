package es.us.isa.restmutator;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Test;

import java.io.IOException;
import java.util.ArrayList;

import static es.us.isa.restmutator.Mutator.*;

public class ExampleTest {

    @Test
    public void test() {
        String jsonString = "{\n" +
                "  \"prop1\": \"value1\",\n" +
                "  \"prop2\": 20,\n" +
                "  \"prop3\": [\n" +
                "        \"a string\",\n" +
                "        10,\n" +
                "        false\n" +
                "  ],\n" +
                "  \"parameters\": [\n" +
                "          {\n" +
                "            \"name\": \"petId\",\n" +
                "            \"in\": \"path\",\n" +
                "            \"description\": \"ID of pet that needs to be updated\",\n" +
                "            \"required\": true,\n" +
                "            \"type\": \"integer\",\n" +
                "            \"format\": \"int64\"\n" +
                "          },\n" +
                "          {\n" +
                "            \"name\": \"name\",\n" +
                "            \"in\": \"formData\",\n" +
                "            \"description\": \"Updated name of the pet\",\n" +
                "            \"required\": false,\n" +
                "            \"type\": \"string\"\n" +
                "          },\n" +
                "          {\n" +
                "            \"name\": \"status\",\n" +
                "            \"in\": \"formData\",\n" +
                "            \"description\": \"Updated status of the pet\",\n" +
                "            \"required\": false,\n" +
                "            \"type\": \"string\"\n" +
                "          }\n" +
                "  ]," +
                "  \"responses\": {\n" +
                "          \"400\": {\n" +
                "            \"description\": \"Invalid ID supplied\"\n" +
                "          },\n" +
                "          \"404\": {\n" +
                "            \"description\": \"Pet not found\"\n" +
                "          },\n" +
                "          \"405\": {\n" +
                "            \"description\": \"Validation exception\"\n" +
                "          }\n" +
                "  }\n" +
                "}";

        ObjectMapper objectMapper = new ObjectMapper();
        try {
            JsonNode jsonNode = objectMapper.readTree(jsonString);
            System.out.println("Original JSON object:\n.\n.\n.\n");
            System.out.println(objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(objectMapper.readValue(jsonNode.toString(), Object.class)));
            JsonNode mutatedJsonNode = mutateJSON(jsonNode);
            System.out.println("Mutated JSON object:\n.\n.\n.\n");
            System.out.println(objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(objectMapper.readValue(mutatedJsonNode.toString(), Object.class)));
        } catch (IOException e) {
//            e.printStackTrace();
            System.out.println("Unable to get properties, it is not formatted in JSON");
        }

    }
}
