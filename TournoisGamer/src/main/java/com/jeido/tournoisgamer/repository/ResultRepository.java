package com.jeido.tournoisgamer.repository;

import com.jeido.tournoisgamer.entity.Result;
import com.jeido.tournoisgamer.entity.Tournament;
import com.jeido.tournoisgamer.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface ResultRepository extends JpaRepository<Result, UUID> {
    Result findByUserAndTournament(User user, Tournament tournament);
    List<Result> findByTournament(Tournament tournament);
    List<Result> findByUser(User user);
    List<Result> findByTournamentOrderByPlace(Tournament tournament);
}
