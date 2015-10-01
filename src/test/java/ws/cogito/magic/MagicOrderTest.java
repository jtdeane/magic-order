package ws.cogito.magic;

import static org.junit.Assert.assertTrue;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URI;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.boot.test.TestRestTemplate;
import org.springframework.boot.test.WebIntegrationTest;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.web.client.RestTemplate;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
@WebIntegrationTest
public class MagicOrderTest {
	
	private static final Logger logger = LoggerFactory.getLogger
			(MagicOrderTest.class);
	
	private RestTemplate restTemplate = new TestRestTemplate();
	
	
    @Configuration
    static class ContextConfiguration {
    
    	//configure any beans...
    }
    
    @Test
    public void testHealthCheck() throws Exception {
    	
    	String expectedResponse = "All Systems Go";
    	
    	ResponseEntity<String> response = restTemplate.getForEntity
    			("http://localhost:8080/health", String.class);
    	
    	assertTrue(response.getBody().equals(expectedResponse));
    }    
    
    @Test
    public void testOrderProcessing() throws Exception {
    	
    	//Part I. Create the Order
    	URI expectedLocation = new URI ("http://localhost:8080/order/X1351");
    	
    	//retrieve the test file
    	String json = getJSONFromFile("order.json");
    	
    	HttpHeaders headers = new HttpHeaders();
    	headers.setContentType(MediaType.APPLICATION_JSON);
    	
    	HttpEntity<String> entity = new HttpEntity<String>(json,headers);

    	URI actualLocation = restTemplate.postForLocation
    			("http://localhost:8080/order", entity);
    	
    	assertTrue(actualLocation.equals(expectedLocation));
    	
    	//Part II. Retrieve the Order
    	ResponseEntity<String> response = restTemplate.getForEntity
    			("http://localhost:8080/order/X1351", String.class);
    	
    	assertTrue(response.getBody().equals(json));
    	
    }
    
    /**
     * Helper method for retrieving json from file
     * @param fileName
     * @return String
     */
    private static String getJSONFromFile(String fileName) {
        String json = null;
        StringBuffer text = new StringBuffer();
        String line = null;
        
        try (BufferedReader in = new BufferedReader
        		(new InputStreamReader(MagicOrderTest.class
                .getResourceAsStream(fileName)))) {
        	
            while ((line = in.readLine()) != null) {
                text.append(line);
            }

            json = text.toString();
			
		} catch (Exception e) {
			
			logger.debug("Failed to read test file " + fileName + " " + e.toString());
		}

        return json;
    }    

}