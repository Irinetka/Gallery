package com.example.gallery.service;

import com.example.gallery.exception.DuplicateEntityException;
import com.example.gallery.exception.EntityNotFoundException;
import com.example.gallery.exception.ValidationException;
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
            throw new DuplicateEntityException("User with id " + user.getId() + " already exists",
                    "Use a free ID or update the existing user");
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
                .orElseThrow(() -> new EntityNotFoundException("User not found",
                        "Check the user ID; it may have been deleted"));
        if (user.getUsername() != null) existing.setUsername(user.getUsername());
        if (user.getEmail() != null) existing.setEmail(user.getEmail());
        if (user.getFullName() != null) existing.setFullName(user.getFullName());
        return userRepository.save(existing);
    }

    @Transactional
    public void deleteUser(Long id) {
        validateId(id);
        if (userRepository.findById(id).isEmpty())
            throw new EntityNotFoundException("User not found",
                    "Nothing to delete: user with ID " + id + " does not exist");
        userRepository.deleteById(id);
    }

    private void validateUser(User user) {
        if (user == null) throw new ValidationException("User cannot be null",
                "Provide a complete user object");
        validateId(user.getId());
        if (user.getUsername() == null || user.getUsername().isBlank())
            throw new ValidationException("Username cannot be empty", "Fill the 'username' field");
        if (user.getEmail() == null || user.getEmail().isBlank())
            throw new ValidationException("Email cannot be empty", "Fill the 'email' field");
    }

    private void validateId(Long id) {
        if (id == null || id <= 0) throw new ValidationException("Id must be positive",
                "ID must be a positive number");
    }
}