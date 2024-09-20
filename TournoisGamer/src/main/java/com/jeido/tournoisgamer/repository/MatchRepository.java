package com.jeido.tournoisgamer.repository;

import com.jeido.tournoisgamer.entity.Match;
import com.jeido.tournoisgamer.entity.Tournament;
import com.jeido.tournoisgamer.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface MatchRepository extends JpaRepository<Match, UUID> {
    List<Match> findByTournament(Tournament tournament);
    List<Match> findByUser(User user);
    List<Match> findByIsWinIsTrue();
    List<Match> findByIsWinIsFalse();
    void deleteMatchByTournament(Tournament tournament);

}
