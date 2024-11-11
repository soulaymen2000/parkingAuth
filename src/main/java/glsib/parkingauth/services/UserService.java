package glsib.parkingauth.services;

import glsib.parkingauth.entities.User;
import glsib.parkingauth.repositories.UserRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserService  {

    @Autowired
    private UserRepository userRepository;

  
    public List<User> findAll() {
        return (List<User>) userRepository.findAll();
    }

  
    public User findById(Long id) {
        return userRepository.findById(id).orElse(null); // Handle not found case as needed
    }

    public void deactivateUser(Long id) {
        Optional<User> user = userRepository.findById(id);
        user.ifPresent(u -> {
            u.setActive(false); // Assuming you have an `active` field in your User entity
            userRepository.save(u);
        });
    }

    public User save(User user) {
        return userRepository.save(user);
    }

    public void deleteById(Long id) {
        userRepository.deleteById(id);
    }


    public Optional <User> findByEmail(String email) {
        return  userRepository.findByEmail(email);
    }
}
