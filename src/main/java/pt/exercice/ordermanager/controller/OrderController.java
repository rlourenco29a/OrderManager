package pt.exercice.ordermanager.controller;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Optional;

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
import pt.exercice.ordermanager.entity.Order;
import pt.exercice.ordermanager.entity.Stock;
import pt.exercice.ordermanager.entity.User;
import pt.exercice.ordermanager.repository.ItemRepository;
import pt.exercice.ordermanager.repository.OrderRepository;
import pt.exercice.ordermanager.repository.UserRepository;
import pt.exercice.ordermanager.service.OrderService;

@RestController
@RequestMapping("/orders")
public class OrderController {
	
	@Autowired
	public OrderRepository orderRepository;
	@Autowired
	public ItemRepository itemRepository;
	@Autowired
	public UserRepository userRepository;
	@Autowired
	public OrderService orderService;
	
	@GetMapping
    public Iterable<Order> getAllOrders() {
        return orderRepository.findAll();
    }

    @GetMapping("/get/{id}")
    public Order getOrderById(@PathVariable Long id) throws NotFoundException {
        return orderRepository.findById(id)
                .orElseThrow(() -> new NotFoundException());
    }

    @PostMapping("/create")
    public ResponseEntity<String> createOrder(@RequestBody Order order) {
        order.setCreationDate(new Timestamp(System.currentTimeMillis()));
        order.setComplete("false");
        order.setStocks(new ArrayList<Stock>());
        Optional<Item> itemAux = itemRepository.findById(order.getItem().getId());
        Optional<User> userAux = userRepository.findById(order.getUser().getId());
        if(itemAux.isPresent()) {
        	order.setItem(itemAux.get());
        } else {
        	return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Item not found.");
        }
        if(userAux.isPresent()) {
        	order.setUser(userAux.get());
        } else {
        	return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found.");
        }
        
        return orderService.createOrder(order);
    }

    @PutMapping("/update/{id}")
    public Order updateOrder(@PathVariable Long id, @RequestBody Order updatedOrder) throws NotFoundException {
        return orderRepository.findById(id)
                .map(order -> {
                	if (updatedOrder.getItem() != null)
                	order.setItem(updatedOrder.getItem());
                	if (updatedOrder.getQuantity() != -1)
                	order.setQuantity(updatedOrder.getQuantity());
                	if (updatedOrder.getUser() != null)
                	order.setUser(updatedOrder.getUser());
                    return orderRepository.save(order);
                })
                .orElseThrow(() -> new NotFoundException());
    }

    @DeleteMapping("/delete/{id}")
    public void deleteOrder(@PathVariable Long id) {
    	orderRepository.deleteById(id);
    }

}
