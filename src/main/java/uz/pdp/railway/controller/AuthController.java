package uz.pdp.railway.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import uz.pdp.railway.model.dto.UserRegisterDto;
import uz.pdp.railway.service.UserService;

import java.util.Optional;

@Controller
@RequestMapping("/auth")
public class AuthController {
    private final UserService userService;

    public AuthController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/login")
    public String getLoginPage(@RequestParam("error") Optional<Boolean> error, Model model) {
        if (error.isPresent()) {
            model.addAttribute("users", false);
        }
        return "login";
    }

    @GetMapping("/register")
    public String getRegisterPage(Model model) {
        model.addAttribute("exists", null);
        return "register";
    }

    @PostMapping("/userAdd")
    public String userAdd(
            @ModelAttribute Optional<UserRegisterDto> userRegister,
            Model model) {
        System.out.println("userRegister = " + userRegister);
        boolean register = userService.register(userRegister);
        if (!register) {
            if (!userService.wrongPass(userRegister)) {
                model.addAttribute("wrongPass", true);
                model.addAttribute("exists", false);
            } else {
                model.addAttribute("exists", true);
            }
            return "register";
        } else {
            return "redirect:/";
        }
    }


}
