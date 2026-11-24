package com.example.gallery.controller;

import com.example.gallery.exception.EntityNotFoundException;
import com.example.gallery.models.User;
import com.example.gallery.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    @GetMapping
    public List<User> getAll() { return userService.showUsers(); }

    @GetMapping("/{id}")
    public ResponseEntity<User> getById(@PathVariable Long id) {
    //    return userService.getUser(id).map(ResponseEntity::ok)
    //            .orElse(ResponseEntity.notFound().build());
        return userService.getUser(id)
                .map(ResponseEntity::ok)
                .orElseThrow(() -> new EntityNotFoundException(
                        "User not found",
                        "Check the User ID; it may have been deleted"));
    }

    @PostMapping
    public ResponseEntity<User> create(@RequestBody User user) {
        return ResponseEntity.ok(userService.createUser(user));
    }

    @PutMapping("/{id}")
    public ResponseEntity<User> update(@PathVariable Long id, @RequestBody User user) {
        return ResponseEntity.ok(userService.update(id, user));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        userService.deleteUser(id);
        return ResponseEntity.noContent().build();
    }
}