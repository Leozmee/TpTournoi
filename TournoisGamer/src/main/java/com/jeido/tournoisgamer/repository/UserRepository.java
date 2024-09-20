package com.jeido.tournoisgamer.repository;
import com.jeido.tournoisgamer.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import com.jeido.tournoisgamer.utils.Role;

public interface UserRepository extends JpaRepository<User, UUID> {
    Optional<User> findByName(String name);
    Optional<User> findByEmail (String email);
    List<User> findByNameContainingIgnoreCase(String name);
    List<User> findByRole (Role role);
    void deleteByName(String name);
    boolean existsByName(String name);
    boolean existsByEmail (String email);

}
