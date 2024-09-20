package com.jeido.tournoisgamer.service;


import com.jeido.tournoisgamer.entity.Tournament;
import com.jeido.tournoisgamer.entity.User;
import com.jeido.tournoisgamer.repository.TournamentRepository;
import com.jeido.tournoisgamer.repository.UserRepository;
import com.jeido.tournoisgamer.utils.Role;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

import static org.springframework.data.jpa.domain.AbstractPersistable_.id;

@Service
public class UserService {
    private final UserRepository userRepository;
    private final TournamentRepository tournamentRepository;

    public UserService(UserRepository userRepository, TournamentRepository tournamentRepository) {
        this.userRepository = userRepository;
        this.tournamentRepository = tournamentRepository;
    }

    public User save(User user) {
        return userRepository.save(user);
    }

    public User update(User user) {
        return userRepository.save(user);
    }

    public User findById(UUID id) {
        return userRepository.findById(id).orElse(null);
    }

    public List<User> findAll() {
        return userRepository.findAll();
    }

    public void delete(UUID id) {
        userRepository.deleteById(id);
    }

    public User findByUsername(String username) {
        return userRepository.findByName(username).orElse(null);
    }

    public User findByEmail(String email) {
        return userRepository.findByEmail(email).orElse(null);
    }

    public List<User> search(String name) {
        return userRepository.findByNameContainingIgnoreCase(name);
    }

    public List<User> getAdmins() {
        return userRepository.findByRole(Role.ADMIN);
    }

    public void delete(String name) {
        userRepository.deleteByName(name);
    }

    public boolean exist(String nameOrEmail) {
        return userRepository.existsByName(nameOrEmail) || userRepository.existsByEmail(nameOrEmail);
    }

    //TODO refactor to User
    public void setAdminRole(UUID id) {
        User user = userRepository.findById(id).orElse(null);
        if (user != null) {
            user.setRole(Role.ADMIN);
            userRepository.save(user);
        }
    }

    //TODO refactor to User
    public void setPlayerRole(UUID id) {
        User user = userRepository.findById(id).orElse(null);
        if (user != null) {
            user.setRole(Role.PLAYER);
            userRepository.save(user);
        }
    }

    //TODO refactor to User
    public boolean isAdmin(UUID id) {
        User user = findById(id);
        return (user.getRole() != null && user.getRole() == Role.ADMIN);
    }

    public boolean isSubscribedTo(User user, Tournament tournament) {
        System.out.println("SERVICE USER : isSubscribedTo");
        User chkUser = findById(user.getId());
        if (chkUser == null) return false;

        Tournament chkTournament = tournamentRepository.findById(tournament.getId()).orElse(null);

        if (chkTournament == null) return false;

        return chkUser.getSubscribedTournament().contains(chkTournament);
    }
}
