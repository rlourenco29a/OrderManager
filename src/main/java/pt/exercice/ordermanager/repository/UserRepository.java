package pt.exercice.ordermanager.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import pt.exercice.ordermanager.entity.User;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

}
