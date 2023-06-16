package pt.exercice.ordermanager.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import pt.exercice.ordermanager.entity.Item;
import pt.exercice.ordermanager.entity.Order;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {
	
	List<Order> findIncompleteOrderByItem(Item item);

}
