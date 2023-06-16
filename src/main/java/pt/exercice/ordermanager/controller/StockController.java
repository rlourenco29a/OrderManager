package pt.exercice.ordermanager.controller;

import java.sql.Timestamp;
import java.util.Optional;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.crossstore.ChangeSetPersister.NotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import pt.exercice.ordermanager.entity.Item;
import pt.exercice.ordermanager.entity.Stock;
import pt.exercice.ordermanager.repository.ItemRepository;
import pt.exercice.ordermanager.repository.StockRepository;


@RestController
@RequestMapping("/stocks")
public class StockController {

	@Autowired
	public StockRepository stockRepository;
	@Autowired
	public ItemRepository itemRepository;

	private static final Logger LOGGER = LogManager.getLogger(StockController.class);
	
	@GetMapping
    public Iterable<Stock> getAllStocks() {
		LOGGER.info("Finding all stock movements!");
        return stockRepository.findAll();
    }

    @GetMapping("/get/{id}")
    public Stock getStockById(@PathVariable Long id) throws NotFoundException {
    	LOGGER.info("Finding stock movement {}!", id);
        return stockRepository.findById(id)
                .orElseThrow(() -> new NotFoundException());
    }

    @PostMapping("/create")
    public ResponseEntity<String> newStock(@RequestBody Stock stock) {
    	stock.setCreationDate(new Timestamp(System.currentTimeMillis()));
    	Optional<Item> itemAux = itemRepository.findById(stock.getItem().getId());
        if(itemAux.isPresent()) {
        	stock.setItem(itemAux.get());
        } else {
        	LOGGER.error("Failed to create a new stock movement - reason Item {} not found.", stock.getItem().getId());
        	return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Item not found.");
        }
        LOGGER.info("Created a new Stock Movement!");
        stockRepository.save(stock);
        return ResponseEntity.ok().build();
    }

    @PutMapping("/update/{id}")
    public Stock updateStock(@PathVariable Long id, @RequestBody Stock updatedStock) throws NotFoundException {
        return stockRepository.findById(id)
                .map(stock -> {
                	if (updatedStock.getItem() != null)
                		stock.setItem(updatedStock.getItem());
                	if (updatedStock.getQuantity() != -1)
                		stock.setQuantity(updatedStock.getQuantity());
                    LOGGER.info("Updated Stock Movement {}!", id);
                    return stockRepository.save(stock);
                })
                .orElseThrow(() -> new NotFoundException());
    }

    @DeleteMapping("/delete/{id}")
    public void deleteStock(@PathVariable Long id) {
        LOGGER.info("Deleting Stock Movement {}!", id);
    	stockRepository.deleteById(id);
    }
}
