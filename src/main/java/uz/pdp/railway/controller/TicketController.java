package uz.pdp.railway.controller;

import org.joda.time.LocalDate;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import uz.pdp.railway.model.dto.SelectTicketDto;
import uz.pdp.railway.model.entity.CitiesEntity;
import uz.pdp.railway.model.entity.TrainEntity;
import uz.pdp.railway.model.entity.UserEntity;
import uz.pdp.railway.service.CitiesService;
import uz.pdp.railway.service.TicketService;
import uz.pdp.railway.service.TrainService;

import java.util.Optional;

@Controller
@RequestMapping("/ticket")
public class TicketController {
    private final TicketService ticketService;
    private final TrainService trainService;
    private final CitiesService citiesService;

    public TicketController(TicketService ticketService, TrainService trainService, CitiesService citiesService) {
        this.ticketService = ticketService;
        this.trainService = trainService;
        this.citiesService = citiesService;
    }

    @GetMapping("/list")
    public String ticketList(Model model) {
        model.addAttribute("cities", citiesService.getList());
        model.addAttribute("now", LocalDate.now());
        return "search";
    }

    @GetMapping("/list/equally")
    public String ticketListEqually(Model model) {
        model.addAttribute("equally", true);
        model.addAttribute("cities", citiesService.getList());
        model.addAttribute("now", LocalDate.now());
        return "search";
    }

    @GetMapping("/list/{from}/{to}/{date}")
    public String ticketList(
            @AuthenticationPrincipal UserEntity userEntity,
            Model model,
            @PathVariable Optional<String> from,
            @PathVariable Optional<String> to,
            @PathVariable Optional<Long> date) {
        if (userEntity != null) {
            model.addAttribute("user", userEntity);
        }
        CitiesEntity fromLocation = citiesService.getById(from);
        CitiesEntity toLocation = citiesService.getById(to);
        LocalDate localDate = new LocalDate(date.get());
        model.addAttribute("cities", citiesService.getList());
        model.addAttribute("fromLocation", fromLocation);
        model.addAttribute("toLocation", toLocation);
        model.addAttribute("now", localDate);
        model.addAttribute("trainList", trainService.getDistinctTrainList(fromLocation, toLocation, localDate));
        return "trains";
    }

    @PostMapping("/search")
    public String ticketSearch(
            @ModelAttribute SelectTicketDto selectTicketDto) {
        System.out.println("selectTicketDto = " + selectTicketDto);
        if (citiesService.equally(selectTicketDto) || citiesService.isNull(selectTicketDto)) {
            return "redirect:/ticket/list/equally";
        }
        return "redirect:/ticket/list/" +
                selectTicketDto.getFromLocation() + "/" +
                selectTicketDto.getToLocation() + "/" +
                selectTicketDto.getDates().getTime() + "";
    }

    @GetMapping("/{trainId}")
    public String getTicket(
            @AuthenticationPrincipal UserEntity userEntity,
            @PathVariable Optional<Long> trainId, Model model) {
        TrainEntity train = trainService.getById(trainId);
        model.addAttribute("user", userEntity);
        model.addAttribute("trainInfo", trainService.info(train));
        model.addAttribute("train", train);
        return "seats";
    }

    @GetMapping("/seat/{seatId}")
    public String addTicket(
            @AuthenticationPrincipal UserEntity userEntity,
            @PathVariable Optional<Long> seatId, Model model) {
        ticketService.addTicket(seatId, userEntity);
        return "redirect:/ticket/my/tickets";
    }

    @GetMapping("/my/tickets")
    public String myTickets(
            @AuthenticationPrincipal UserEntity userEntity,
            Model model) {
        model.addAttribute("trainList", trainService.getByUserId(userEntity));
        model.addAttribute("ticketList", ticketService.getByUserId(userEntity));
        return "tickets";
    }


}
