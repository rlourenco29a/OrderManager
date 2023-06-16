package pt.exercice.ordermanager.controller;


import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.crossstore.ChangeSetPersister.NotFoundException;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import pt.exercice.ordermanager.entity.Item;
import pt.exercice.ordermanager.repository.ItemRepository;

@RestController
@RequestMapping("/items")
public class ItemController {
	
	private static final Logger LOGGER = LogManager.getLogger(ItemController.class);
	
	@Autowired
	public ItemRepository itemRepository;
	
	@GetMapping
    public Iterable<Item> getAllItems() {
		LOGGER.info("Finding all items!");
        return itemRepository.findAll();
    }

    @GetMapping("/get/{id}")
    public Item getItemById(@PathVariable Long id) throws NotFoundException {
    	LOGGER.info("Finding item {}", id);
        return itemRepository.findById(id)
                .orElseThrow(() -> new NotFoundException());
    }

    @PostMapping("/create")
    public Item createItem(@RequestBody Item item) {
    	LOGGER.info("Created a new item!");
        return itemRepository.save(item);
    }

    @PutMapping("/update/{id}")
    public Item updateItem(@PathVariable Long id, @RequestBody Item updatedItem) throws NotFoundException {
        return itemRepository.findById(id)
                .map(item -> {
                    item.setName(updatedItem.getName());
                    LOGGER.info("Updated item {}!", id);
                    return itemRepository.save(item);
                })
                .orElseThrow(() -> new NotFoundException());
    }

    @DeleteMapping("/delete/{id}")
    public void deleteItem(@PathVariable Long id) {
    	LOGGER.info("Deleting item {}", id);
        itemRepository.deleteById(id);
    }

}
