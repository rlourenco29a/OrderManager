package pt.exercice.ordermanager.service;

import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import pt.exercice.ordermanager.entity.Order;
import pt.exercice.ordermanager.entity.Stock;
import pt.exercice.ordermanager.repository.OrderRepository;
import pt.exercice.ordermanager.repository.StockRepository;

@Service
public class StockService {
	
	@Autowired
	private StockRepository stockRepository;
	@Autowired
	private OrderRepository orderRepository;

	private static final Logger LOGGER = LogManager.getLogger(StockService.class);
	
	public ResponseEntity<String> createStock(Stock stock) {
		try {
		List<Order> incompleteOrders = orderRepository.findIncompleteOrderByItem(stock.getItem());

		if (!incompleteOrders.isEmpty()) {

			Order incompleteOrder = incompleteOrders.get(0);
			stock.setOrder(incompleteOrder);
			incompleteOrder.getStocks().add(stock);
			stockRepository.save(stock);
			orderRepository.save(incompleteOrder);

			LOGGER.info("The stock  {} was created successfully and was associated to the order {}.",
					stock.getId(), incompleteOrder.getId());
		} else {
			stockRepository.save(stock);

			LOGGER.info("The stock  {} was created successfully and does not have an order currently.",
					stock.getId());
		}
		} catch (Exception e) {
			LOGGER.info("Something went wrong while trying to create a new stock .",
					e);
		}
		return ResponseEntity.ok("Stock  created!");
	}

}
