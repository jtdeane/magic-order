package ws.cogito.magic;

import static org.junit.Assert.assertTrue;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URI;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.embedded.LocalServerPort;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT, classes={Application.class})
public class MagicOrderTest {
	
	private static final Logger logger = LoggerFactory.getLogger
			(MagicOrderTest.class);
	
    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;	
	
    @Configuration
    static class ContextConfiguration {
    
    	//configure any beans...
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
    			("http://localhost:"+ port + "/order", entity);
    	
    	assertTrue(actualLocation.equals(expectedLocation));
    	
    	//Part II. Retrieve the Order
    	ResponseEntity<String> response = restTemplate.getForEntity
    			("http://localhost:"+ port + "/order/X1351", String.class);
    	
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