package uz.pdp.railway.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.joda.time.DateTime;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import uz.pdp.railway.model.dto.PermissionDto;
import uz.pdp.railway.model.dto.TrainDto;
import uz.pdp.railway.service.CitiesService;
import uz.pdp.railway.service.TrainService;
import uz.pdp.railway.service.UserService;

import javax.servlet.http.HttpServletRequest;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Date;
import java.util.Timer;

@Controller
@RequestMapping("/super/admin")
public class SuperAdminController {
    private final UserService userService;
    private final TrainService trainService;
    private final CitiesService citiesService;

    public SuperAdminController(UserService userService, TrainService trainService, CitiesService citiesService) {
        this.userService = userService;
        this.trainService = trainService;
        this.citiesService = citiesService;
    }

    @GetMapping("/add")
    public String addSuperAdmin() {
        userService.addSuperAdmin();
        return "redirect:/";
    }

    @GetMapping("/add/cities")
    public String addCities() {
        citiesService.addAll();
        return "redirect:/";
    }

    @GetMapping("/permission")
    @PreAuthorize("hasRole('ROLE_SUPER_ADMIN')")
    public String getPermission() {
        return "permission";
    }

    @PostMapping("/permission/add")
    @PreAuthorize("hasRole('ROLE_SUPER_ADMIN')")
    public String addPermission(@RequestBody PermissionDto permissionDto, Model model) throws JsonProcessingException {
        boolean isSuccess = userService.addPermission(permissionDto);
        model.addAttribute("isSuccess", isSuccess);
        return "permission";
    }

    @RequestMapping("/default")
    public String defaultAfterLogin(HttpServletRequest request) {
        if (request.isUserInRole("ROLE_SUPER_ADMIN")) {
            return "redirect:/super/admin/permission";
        }
        return "redirect:/home";
    }

    @GetMapping("/train")
    @PreAuthorize("hasRole('ROLE_SUPER_ADMIN') or (hasRole('ROLE_ADMIN') and hasAuthority('TRAIN_ADD'))")
    public String getTrain(Model model) {
        model.addAttribute("cities", citiesService.getList());
        model.addAttribute("now", LocalDate.now());
        model.addAttribute("plus", LocalDate.now().plusMonths(1));
        model.addAttribute("times", LocalTime.now());
        return "trains";
    }

    @PostMapping("/train/add")
    @PreAuthorize("hasRole('ROLE_SUPER_ADMIN') or (hasRole('ROLE_ADMIN') and hasAuthority('TRAIN_ADD'))")
    public String addTrain(@RequestBody TrainDto trainDto, Model model) throws JsonProcessingException {
        System.out.println("trainDto = " + trainDto);
        boolean isSuccess = trainService.addTrain(trainDto);
        model.addAttribute("isSuccess", isSuccess);
        return "trains";
    }

}
