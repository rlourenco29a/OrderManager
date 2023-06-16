package pt.exercice.ordermanager.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import pt.exercice.ordermanager.entity.Stock;

@Repository
public interface StockRepository extends JpaRepository<Stock, Long> {
	List<Stock> findByOrderIdIsNullAndItem_Id(Long itemId);
	@Query("SELECT s FROM Stock s JOIN FETCH s.order")
	List<Stock> findAll();
}
