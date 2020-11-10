package es.us.isa.jsonmutator;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

import static junit.framework.TestCase.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.fail;

public class JsonMutatorTest {

    private JsonNode jsonNode = null;
    private ObjectMapper objectMapper;
    private JsonMutator jsonMutator;

    @Before
    public void setUp() {
        // Read JSON file
        String jsonString = "";
        try {
            jsonString = new String(Files.readAllBytes(Paths.get("src/test/resources/test-object.json")));
        } catch (IOException e) {
            System.out.println("Unable to read JSON");
            e.printStackTrace();
            fail();
        }

        // Create JsonNode
        objectMapper = new ObjectMapper();
        try {
            jsonNode = objectMapper.readTree(jsonString);
        } catch (IOException e) {
            System.out.println("Unable to get properties, it is not formatted in JSON");
            e.printStackTrace();
            fail();
        }

        // Create mutator
        jsonMutator = new JsonMutator();
    }

    @Test
    @Ignore
    public void test() throws IOException {
        System.out.println("Original JSON object:\n.\n.\n.\n");
        System.out.println(objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(objectMapper.readValue(jsonNode.toString(), Object.class)));
        JsonNode mutatedJsonNode1 = jsonMutator.mutateJson(jsonNode, true);
        System.out.println(".\n.\n.\nSingle order mutation:\n.\n.\n.\n");
        System.out.println(objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(objectMapper.readValue(mutatedJsonNode1.toString(), Object.class)));
        JsonNode mutatedJsonNode2 = jsonMutator.mutateJson(jsonNode, false);
        System.out.println(".\n.\n.\nMultiple order mutation:\n.\n.\n.\n");
        System.out.println(objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(objectMapper.readValue(mutatedJsonNode2.toString(), Object.class)));
        JsonNode mutatedJsonNode3 = jsonMutator.mutateJson(jsonNode, true);
        System.out.println(".\n.\n.\nSingle order mutation:\n.\n.\n.\n");
        System.out.println(objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(objectMapper.readValue(mutatedJsonNode3.toString(), Object.class)));
    }

    @Test
    public void singleOrderMutation() throws IOException {
        jsonMutator.setProperty("operator.value.long.enabled", "true"); // Make sure at least one mutator is enabled
        JsonNode mutatedJsonNode1 = jsonMutator.mutateJson(jsonNode, true);
//        System.out.println("Original JSON object:\n.\n.\n.\n");
//        System.out.println(objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(objectMapper.readValue(jsonNode.toString(), Object.class)));
//        System.out.println(".\n.\n.\nSingle order mutation:\n.\n.\n.\n");
//        System.out.println(objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(objectMapper.readValue(mutatedJsonNode1.toString(), Object.class)));
        assertNotEquals("The mutated JSON should be different to the original JSON", jsonNode, mutatedJsonNode1);
    }

    @Test
    public void multipleOrderMutation() throws IOException {
        jsonMutator.setProperty("operator.value.long.enabled", "true"); // Make sure at least one mutator is enabled and its probability is set to 1
        jsonMutator.setProperty("operator.value.long.prob", "1");
        JsonNode mutatedJsonNode1 = jsonMutator.mutateJson(jsonNode, false);
//        System.out.println("Original JSON object:\n.\n.\n.\n");
//        System.out.println(objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(objectMapper.readValue(jsonNode.toString(), Object.class)));
//        System.out.println(".\n.\n.\Multiple order mutation:\n.\n.\n.\n");
//        System.out.println(objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(objectMapper.readValue(mutatedJsonNode1.toString(), Object.class)));
        assertNotEquals("The mutated JSON should be different to the original JSON", jsonNode, mutatedJsonNode1);
    }

    @Test
    public void singleOrderMutationCannotBeApplied() throws IOException {
        deactivateAllMutators(); // Deactivate all mutators
        JsonNode mutatedJsonNode1 = jsonMutator.mutateJson(jsonNode, true);
//        System.out.println("Original JSON object:\n.\n.\n.\n");
//        System.out.println(objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(objectMapper.readValue(jsonNode.toString(), Object.class)));
//        System.out.println(".\n.\n.\nSingle order mutation:\n.\n.\n.\n");
//        System.out.println(objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(objectMapper.readValue(mutatedJsonNode1.toString(), Object.class)));
        assertEquals("The mutated JSON should be equal to the original JSON", jsonNode, mutatedJsonNode1);
    }

    @Test
    public void multipleOrderMutationCannotBeApplied() throws IOException {
        deactivateAllMutators(); // Deactivate all mutators
        JsonNode mutatedJsonNode1 = jsonMutator.mutateJson(jsonNode, false);
//        System.out.println("Original JSON object:\n.\n.\n.\n");
//        System.out.println(objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(objectMapper.readValue(jsonNode.toString(), Object.class)));
//        System.out.println(".\n.\n.\Multiple order mutation:\n.\n.\n.\n");
//        System.out.println(objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(objectMapper.readValue(mutatedJsonNode1.toString(), Object.class)));
        assertEquals("The mutated JSON should be equal to the original JSON", jsonNode, mutatedJsonNode1);
    }

    @Test
    public void resetProperties() {
        deactivateAllMutators(); // Deactivate all mutators
        jsonMutator.resetProperties();
        JsonNode mutatedJsonNode1 = jsonMutator.mutateJson(jsonNode, false);
//        System.out.println("Original JSON object:\n.\n.\n.\n");
//        System.out.println(objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(objectMapper.readValue(jsonNode.toString(), Object.class)));
//        System.out.println(".\n.\n.\Multiple order mutation:\n.\n.\n.\n");
//        System.out.println(objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(objectMapper.readValue(mutatedJsonNode1.toString(), Object.class)));
        assertNotEquals("The mutated JSON should be different to the original JSON", jsonNode, mutatedJsonNode1);
    }

    private void deactivateAllMutators() {
        jsonMutator.setProperty("operator.value.long.enabled", "false");
        jsonMutator.setProperty("operator.value.double.enabled", "false");
        jsonMutator.setProperty("operator.value.string.enabled", "false");
        jsonMutator.setProperty("operator.value.boolean.enabled", "false");
        jsonMutator.setProperty("operator.value.null.enabled", "false");
        jsonMutator.setProperty("operator.object.enabled", "false");
        jsonMutator.setProperty("operator.array.enabled", "false");
    }
}
