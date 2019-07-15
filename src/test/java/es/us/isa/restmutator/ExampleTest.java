package es.us.isa.restmutator;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Test;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

public class ExampleTest {

    @Test
    public void test() {
        System.out.println(System.getProperty("user.dir"));

        // Read JSON file
        String jsonPath = "src/test/resources/test-object.json";
        String jsonString = "";
        try {
            jsonString = new String(Files.readAllBytes(Paths.get(jsonPath)));
        } catch (IOException e) {
            e.printStackTrace();
        }

        // Create mutator object (optionally change default configuration with the setters)
        Mutator mutator = new Mutator();
//        mutator.setMutateValueProb(0.5f);

        // Create JsonNode and mutate it
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            JsonNode jsonNode = objectMapper.readTree(jsonString);
            System.out.println("Original JSON object:\n.\n.\n.\n");
            System.out.println(objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(objectMapper.readValue(jsonNode.toString(), Object.class)));
            JsonNode mutatedJsonNode = mutator.mutateJSON(jsonNode);
            System.out.println(".\n.\n.\nMutated JSON object:\n.\n.\n.\n");
            System.out.println(objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(objectMapper.readValue(mutatedJsonNode.toString(), Object.class)));
        } catch (IOException e) {
//            e.printStackTrace();
            System.out.println("Unable to get properties, it is not formatted in JSON");
        }

    }
}
