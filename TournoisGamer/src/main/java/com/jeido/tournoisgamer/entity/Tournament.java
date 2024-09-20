package com.jeido.tournoisgamer.entity;

import com.jeido.tournoisgamer.utils.Format;
import com.jeido.tournoisgamer.utils.TournamentStatus;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import jakarta.persistence.*;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;
import java.util.UUID;

@Data
@Entity
@Table(name="tournaments")
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Tournament {
    // --- ID ---
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "tournament_id")
    private UUID id;

    // --- VARIABLES ---
    @NotBlank(message = "This field can't be empty !")
    private String name;
    @NotNull(message = "This field can't be empty !")
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
    private LocalDateTime date;
    @NotBlank(message = "This field can't be empty !")
    private String game;
    private int playerLimit;
    @NotBlank(message = "This field can't be empty !")
    private String rules;

    // --- ENUM ---
    private Format format;
    private TournamentStatus status;

    // --- LISTS ---
    @ManyToMany(mappedBy = "subscribedTournament")
    private List<User> inCompetitionPlayers;

    @OneToMany(mappedBy = "tournament")
    private List<Match> results;

    @ManyToMany(mappedBy = "subscribedTournament")
    private List<User> players;

    //TODO
    public void init() {
        System.out.println(this.name + " has Started");
    }

    //TODO
    public void end() {
        System.out.println(this.name + " has Ended");
    }

    //TODO
    public void matchStart(List<User> usersInMatch) {
        System.out.println(this.name + " Match started");
    }

    //TODO
    public Result matchEnd(List<User> usersInMatch) {
        return null;
    }

    public void calcWinner() {
    }
}
