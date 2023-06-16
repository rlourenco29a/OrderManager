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

import pt.exercice.ordermanager.entity.User;
import pt.exercice.ordermanager.repository.UserRepository;

@RestController
@RequestMapping("/users")
public class UserController {

	@Autowired
	public UserRepository userRepository;

	private static final Logger LOGGER = LogManager.getLogger(UserController.class);
	
	@GetMapping
	public Iterable<User> getAllUsers() {
		LOGGER.info("Finding all users!");
		return userRepository.findAll();
	}

	@GetMapping("/get/{id}")
	public User getUserById(@PathVariable Long id) throws NotFoundException {
    	LOGGER.info("Finding user {}!", id);
		return userRepository.findById(id).orElseThrow(() -> new NotFoundException());
	}

	@PostMapping("/create")
	public User createUser(@RequestBody User user) {
        LOGGER.info("Created a new User!");
		return userRepository.save(user);
	}

	@PutMapping("/update/{id}")
	public User updateUser(@PathVariable Long id, @RequestBody User updatedUser) throws NotFoundException {
		return userRepository.findById(id).map(user -> {
			if (updatedUser.getName() != null)
				user.setName(updatedUser.getName());
			if (updatedUser.getEmail() != null)
				user.setEmail(updatedUser.getEmail());
            LOGGER.info("Updated user {}!", id);
			return userRepository.save(user);
		}).orElseThrow(() -> new NotFoundException());
	}

	@DeleteMapping("/delete/{id}")
	public void deleteItem(@PathVariable Long id) {
        LOGGER.info("Deleting user {}!", id);
		userRepository.deleteById(id);
	}

}
