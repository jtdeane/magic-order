package ws.cogito.magic.web;

import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import ws.cogito.magic.model.Order;
import ws.cogito.magic.service.OrderRepository;

/**
 * Handles submissions of magic orders
 * @author jeremydeane
 *
 */
@RestController
@RequestMapping("/")
public class OrderProcessingController {
	
	private static final Logger logger = LoggerFactory.getLogger
			(OrderProcessingController.class);
	
	@Autowired
	private OrderRepository orderRepository;
	
	/**
	 * Create an order
	 * @param order
	 * @param response
	 * @throws Exception
	 */
	@RequestMapping(value = "order", method=RequestMethod.POST)
	public void postOrder (@RequestBody Order order, 
			HttpServletResponse response) throws Exception {
		
		logger.debug("Processing Order " + order.getId());
		
		//in a production this URL would be generated based on the environment..
		response.setHeader(HttpHeaders.LOCATION, 
				"http://localhost:8080/order/" + order.getId());
		
		orderRepository.storeOrder(order);
	}
	
	/**
	 * Retrieve a batch total
	 * @param orderId
	 * @param response
	 * @return order
	 * @throws Exception
	 */
	@RequestMapping(value = "order/{orderId}", method=RequestMethod.GET)
	public @ResponseBody Order getOrderById(@PathVariable String orderId, 
			HttpServletResponse response) throws Exception {
		
		Order order = null;
		
		logger.debug("Retrieving Order Total " + orderId);

		order = orderRepository.retrieveOrder(orderId);
		
		if (order == null) {
			
			response.setStatus(HttpStatus.NOT_FOUND.value());
	
		} else {
			
			response.setStatus(HttpStatus.OK.value());
		}
		
		return order;
	}	
}