package com.example.gallery.service;

import com.example.gallery.models.User;
import com.example.gallery.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;

    @Transactional
    public User createUser(User user) {
        validateUser(user);
        if (userRepository.findById(user.getId()).isPresent())
            throw new IllegalStateException("User with id " + user.getId() + " already exists");
        return userRepository.save(user);
    }

    public Optional<User> getUser(Long id) {
        validateId(id);
        return userRepository.findById(id);
    }

    public List<User> showUsers() { return userRepository.findAll(); }

    @Transactional
    public User update(Long id, User user) {
        validateId(id);
        User existing = userRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));
        if (user.getUsername() != null) existing.setUsername(user.getUsername());
        if (user.getEmail() != null) existing.setEmail(user.getEmail());
        if (user.getFullName() != null) existing.setFullName(user.getFullName());
        return userRepository.save(existing);
    }

    @Transactional
    public void deleteUser(Long id) {
        validateId(id);
        if (userRepository.findById(id).isEmpty())
            throw new IllegalArgumentException("User not found");
        userRepository.deleteById(id);
    }

    private void validateUser(User user) {
        if (user == null) throw new IllegalArgumentException("User cannot be null");
        validateId(user.getId());
        if (user.getUsername() == null || user.getUsername().isBlank())
            throw new IllegalArgumentException("Username cannot be empty");
        if (user.getEmail() == null || user.getEmail().isBlank())
            throw new IllegalArgumentException("Email cannot be empty");
    }

    private void validateId(Long id) {
        if (id == null || id <= 0) throw new IllegalArgumentException("Id must be positive");
    }
}