package ws.cogito.magic.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.cache.CacheManager;
import org.springframework.stereotype.Component;

/**
 * Stores submitted orders in cache.
 */
import ws.cogito.magic.model.Order;

@Component
public class OrderRepository {
	
	private static final String ORDERS = "ws.cogito.magic.orders";
	
	private static final Logger logger = LoggerFactory.getLogger
			(OrderRepository.class);
	
	@Autowired
	@Qualifier("ehCacheCacheManager")
    private CacheManager cacheManager;
	
	/**
	 * returns null if not found; otherwise value is returned from cache.
	 * @param orderId
	 * @return order
	 */
	public Order retrieveOrder (String orderId) {
		
		logger.debug("Retrieving Order " + orderId);
	
		return cacheManager.getCache(ORDERS).get(orderId, Order.class);
	}
	
	/**
	 * Puts the the order in cache
	 * @param order
	 */
	public void storeOrder (Order order) {
		
		logger.debug("Storing Order " + order.getId());
		
		cacheManager.getCache(ORDERS).put(order.getId(), order);
	}
}