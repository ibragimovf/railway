package uz.pdp.railway.controller;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import uz.pdp.railway.model.dto.NewsDto;
import uz.pdp.railway.model.entity.UserEntity;
import uz.pdp.railway.service.NewsService;

import java.io.IOException;
import java.util.Optional;

@Controller
@RequestMapping("/")
public class HomeController {

    private final NewsService newsService;

    public HomeController(NewsService newsService) {
        this.newsService = newsService;
    }

    @GetMapping("")
    public String getHome() {
        return "redirect:/home";
    }

    @GetMapping("/home")
    public String getHome(@AuthenticationPrincipal UserEntity userEntity,
                          Model model,
                          @RequestParam(name = "pageSize") Optional<Integer> pageSize) {
        int page = pageSize.orElse(1);
        if (userEntity != null) {
            model.addAttribute("userRole", userEntity.getRoleEntityList().get(0).getAuthority());
        }
        model.addAttribute("newsList", newsService.getNewsPage(page));
        model.addAttribute("isEmpty", newsService.getNewsPage(page + 1).isEmpty());
        model.addAttribute("pageSize", page);
        return "home";
    }

    @PostMapping("/news/add")
    public String addNews(@ModelAttribute NewsDto newsDto) throws IOException {
        newsService.addNews(newsDto);
        return "redirect:/home";
    }
}
