package com.jeido.tournoisgamer.controller;

import com.jeido.tournoisgamer.entity.Tournament;
import com.jeido.tournoisgamer.entity.User;
import com.jeido.tournoisgamer.service.AuthService;
import com.jeido.tournoisgamer.service.TournamentService;
import com.jeido.tournoisgamer.service.UserService;
import com.jeido.tournoisgamer.utils.Format;
import com.jeido.tournoisgamer.utils.Role;
import com.jeido.tournoisgamer.utils.TournamentStatus;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.ui.Model;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Controller
public class TournamentController {


    private final TournamentService tournamentService;

    private final UserService userService;
    private final AuthService authService;

    @Autowired
    public TournamentController(TournamentService tournamentService, UserService userService, AuthService authService) {
        this.tournamentService = tournamentService;
        this.userService = userService;
        this.authService = authService;
    }

    @GetMapping("/tournaments")
    public String getAllTournaments(@RequestParam(value = "date", required = false) LocalDateTime date, @RequestParam(value = "name", required = false) String name, Model model) {
        if (authService.isLogged()) {

            User user = authService.getUser();
            if (user != null && user.getRole() == Role.ADMIN) {
                System.out.println("TATA");
                model.addAttribute("isAdmin", true);
            } else {
                System.out.println("MICHEL");
            }
        } else {
            System.out.println("TOTO");
            model.addAttribute("isAdmin", false);
        }

        List<Tournament> tournaments;
        if (date != null && name != null) {
            tournaments = tournamentService.findTournamentsByDateAndByName(date, name);
        } else if (date != null) {
            tournaments = tournamentService.findTournamentsByDate(date);
        } else if (name != null) {
            tournaments = tournamentService.findTournamentsByName(name);
        } else {
            tournaments = tournamentService.findAllTournaments();
        }
        model.addAttribute("tournaments", tournaments);
        return "tournaments/listTournaments";
    }

    @GetMapping("/tournaments/{id}")
    public String getTournamentDetails(@PathVariable("id") UUID id, Model model) {
        if (authService.isLogged()) {

            User user = authService.getUser();
            if (user != null && user.getRole() == Role.ADMIN) {
                model.addAttribute("isAdmin", true);
            }
        } else {
            model.addAttribute("isAdmin", false);
        }

        Tournament tournament = tournamentService.findTournamentById(id);
        if (tournament == null) {
            //TODO error(custom) not found
            return "redirect:/tournaments";
        }
        model.addAttribute("tournament", tournament);
        model.addAttribute("isSubscribe", userService.isSubscribedTo(authService.getUser(), tournament));
        return "tournaments/detailTournaments";
    }

    @GetMapping("/tournaments/add")
    public String addTournament(Model model) {
        if(userService.isAdmin(authService.getUser().getId())) {
            model.addAttribute("tournament", Tournament.builder().game("none").format(Format.TOURNAMENT).date(LocalDateTime.now()).playerLimit(2).status(TournamentStatus.NOT_STARTED).build());
            model.addAttribute("formats", Format.values());
            model.addAttribute("status", TournamentStatus.values());
            return "tournaments/addTournament";
        } return "redirect:/tournaments";
    }

    @PostMapping("/tournaments/add")
    public String addPostTournament(@Valid @ModelAttribute("tournament") Tournament tournament, BindingResult bindingResult, Model model) {
        if(!authService.isLogged()) return "redirect:/login";
        if(!userService.isAdmin(authService.getUser().getId())) return "redirect:/tournaments";

        if (bindingResult.hasErrors()) {
            model.addAttribute("tournament", Tournament.builder().game("none").format(Format.TOURNAMENT).date(LocalDateTime.now()).playerLimit(2).status(TournamentStatus.NOT_STARTED).build());
            model.addAttribute("formats", Format.values());
            model.addAttribute("status", TournamentStatus.values());

            return "redirect:/tournaments/add";
        }

        Tournament savedTournament = tournamentService.create(tournament);


        return "redirect:/tournaments/" + savedTournament.getId();
    }

    @GetMapping("/tournaments/update/{id}")
    public String updateTournament(@PathVariable("id")UUID id, Model model) {
        if (!authService.isLogged()) {
            return "redirect:/login";
        }
        if(userService.isAdmin(authService.getUser().getId())) {
            Tournament tournamentToUpdate = tournamentService.findTournamentById(id);
            if(tournamentToUpdate == null) {
                //TODO error(Custom) not found
                return "redirect:/tournaments";
            }

            model.addAttribute("tournament", tournamentToUpdate);
            model.addAttribute("formats", Format.values());
            model.addAttribute("status", TournamentStatus.values());
            return "tournaments/addTournament";
        } else {
            return "redirect:/tournaments/" + id;
        }

    }

    @PostMapping("/tournaments/update")
    public String updateTournament(@Valid @ModelAttribute("tournament")Tournament tournament, BindingResult bindingResult, Model model) {
        if(!authService.isLogged()) return "redirect:/login";
        if(!userService.isAdmin(authService.getUser().getId())) return "redirect:/tournaments";

        if (bindingResult.hasErrors()) {
            return "redirect:/tournaments/update";
        }

        Tournament updatedTour = tournamentService.update(tournament);
        return "redirect:/tournaments/" + updatedTour.getId();
    }

    @RequestMapping("/tournaments/delete/{id}")
    public String delete(@PathVariable("id") UUID id) {
        if (authService.isLogged()) {
            if (authService.getUser().getRole() == Role.ADMIN) {
                Tournament tournamentToDelete = tournamentService.findTournamentById(id);
                if (tournamentToDelete == null) {
                    //TODO error(Custom) notfound
                    return "redirect:/tournaments";
                }
                tournamentService.delete(tournamentToDelete);
                return "redirect:/tournaments";
            } else {
                return "redirect:/tournaments/" + id;
            }
        }
        return "redirect:/login";
    }

    @GetMapping("/tournaments/subscription/{id}")
    public String subscribeTournament(@PathVariable("id")UUID id, Model model) {
        if (authService.isLogged()) {
            User user = authService.getUser();

            if (user == null) {
                model.addAttribute("error", "You're not logged in !");
                return "redirect:/login";
            }


            Tournament tournament = tournamentService.findTournamentById(id);

            if (tournament == null) {
                model.addAttribute("error", "There is no tournament yet !");
                return "redirect:/tournaments";
            }

            if (tournament.getInCompetitionPlayers().size() >= tournament.getPlayerLimit()) {
                model.addAttribute("error", "Tournament is full.");
                return "redirect:/tournaments/" + id;
            }

            switch (tournament.getStatus()) {
                case CANCELLED:
                    System.out.println("Tournament canceled.");
                    model.addAttribute("error", "Tournament canceled.");
                    break;
                case COMPLETED:
                    System.out.println("Tournament finished.");
                    model.addAttribute("error", "Tournament finished.");
                    break;
                case STARTED:
                    System.out.println("Tournament already began.");
                    model.addAttribute("error", "Tournament already began.");
                    break;
                case NOT_STARTED:
                    System.out.println("OK");
                    tournament.getInCompetitionPlayers().add(user);
                    tournament.getPlayers().add(user);
                    tournamentService.update(tournament);
                    user.getSubscribedTournament().add(tournament);
                    userService.update(user);
                    break;
                default:
                    System.out.println("???");
                    model.addAttribute("error", "incorrect Status");
                    break;
            }

            return "redirect:/tournaments/" + id;

        } else {
            model.addAttribute("error", "You're not logged in !");
            return "redirect:/login";
        }
    }

    @GetMapping("/tournaments/unsubscription/{id}")
    public String unsubscribeTournament(@PathVariable("id")UUID id, Model model) {
        if (authService.isLogged()) {
            User user = authService.getUser();

            if (user == null) {
                model.addAttribute("error", "You're not logged in !");
                return "redirect:/login";
            }


            Tournament tournament = tournamentService.findTournamentById(id);

            if (tournament == null) {
                model.addAttribute("error", "There is no tournament yet !");
                return "redirect:/tournaments";
            }

//            if (tournament.getInCompetitionPlayers().size() >= tournament.getPlayerLimit()) {
//                model.addAttribute("error", "Tournament is full.");
//                return "redirect:/tournaments/" + id;
//            }

            switch (tournament.getStatus()) {
                case CANCELLED:
                    model.addAttribute("error", "Tournament canceled.");
                    break;
                case COMPLETED:
                    model.addAttribute("error", "Tournament finished.");
                    break;
                case STARTED:
                    model.addAttribute("error", "Tournament already began.");
                    break;
                case NOT_STARTED:
                    tournament.getInCompetitionPlayers().remove(user);
                    tournament.getPlayers().remove(user);
                    tournamentService.update(tournament);
                    user.getSubscribedTournament().remove(tournament);
                    userService.update(user);
                    break;
                default:
                    model.addAttribute("error", "incorrect Status");
                    break;
            }

            return "redirect:/tournaments/" + id;

        } else {
            model.addAttribute("error", "You're not logged in !");
            return "redirect:/login";
        }
    }
}