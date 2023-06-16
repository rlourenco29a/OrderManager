package pt.exercice.ordermanager.service;

import java.sql.Date;
import java.sql.Timestamp;
import java.util.List;

import javax.persistence.EntityManager;

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
public class OrderService {
	
	@Autowired
	private OrderRepository orderRepository;
	@Autowired
	private StockRepository stockRepository;
	@Autowired
	private StockService stockService;
	@Autowired
	private EmailService emailService;
	
	private static final Logger LOGGER = LogManager.getLogger(OrderService.class);
	
	public ResponseEntity<String> createOrder(Order order) {
		orderRepository.save(order);

		List<Stock> unassociatedStockMovements = stockRepository.findByOrderIdIsNull();
		if (!unassociatedStockMovements.isEmpty()) {
			updateStockMovements(order, unassociatedStockMovements);

		} else {
			LOGGER.info("No stock movement with the item are available at the moment.");
		}
		return ResponseEntity.ok("Order created!");
	}

	public void updateStockMovements(Order order, List<Stock> unassociatedStockMovements) {

		for (Stock stock : unassociatedStockMovements) {
			stock.setOrder(order);
			order.getStocks().add(stock);
			stockRepository.save(stock);
		}
		validateQuantity(order);
		orderRepository.save(order);
		LOGGER.info("The stock movements were updated to match its corresponding order!");
	}
	
	public void validateQuantity(Order order) {
		int sum = 0;
		
		List<Stock> listAux = order.getStocks();

		for (Stock stock : listAux) {
			sum += stock.getQuantity();
		}

		if (sum >= order.getQuantity()) {

			if (sum >= order.getQuantity()) {
				LOGGER.info("The item quantity exceeded the quantity of the order, a new stock movement will be created with the excess!");
				
				Stock stockAux = listAux.get(listAux.size() - 1);
				int excess = sum - order.getQuantity();

				stockAux.setQuantity(stockAux.getQuantity() - excess);
				stockRepository.save(stockAux);

				stockService.createStock(new Stock(new Timestamp(System.currentTimeMillis()), excess, stockAux.getItem()));
				
				LOGGER.info("New Stock movement created successfully!");
			}
			
			order.setComplete("true");;
			orderRepository.save(order);
			sendEmail(order);

		} 

	}
	
	public void sendEmail(Order order) {
		String to = order.getUser().getEmail();
		String subject = "Order " + order.getId() + " for the item " + order.getItem().getName();
		String text = "The order " + order.getId() + " has been completed!";
		emailService.sendEmail(to, subject, text);
	}

}
