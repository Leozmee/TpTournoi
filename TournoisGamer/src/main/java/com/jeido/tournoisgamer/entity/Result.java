package com.jeido.tournoisgamer.entity;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;


@Entity
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name= "result")
public class Result {
    @Id@GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    private int nbWin;
    private int nbDefeat;
    private int place;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;
    @ManyToOne
    @JoinColumn(name = "tournament_id")
    private Tournament tournament;

    public double getRatio() {
        return (nbDefeat == 0)? nbWin : (double) nbWin / nbDefeat;
    }
}
