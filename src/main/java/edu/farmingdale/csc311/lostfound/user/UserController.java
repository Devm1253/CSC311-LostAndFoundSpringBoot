package edu.farmingdale.csc311.lostfound.user;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class UserController {

    private final AppUserRepository repository;
    private final PasswordEncoder passwordEncoder;

    public UserController(AppUserRepository repository, PasswordEncoder passwordEncoder) {
        this.repository = repository;
        this.passwordEncoder = passwordEncoder;
    }

    @GetMapping("/register")
    public String showRegistrationPage() {
        return "register";
    }

    @PostMapping("/register")
    public String register(@RequestParam String username,
                           @RequestParam String password,
                           Model model) {
        String cleanedUsername = username.trim();

        if (cleanedUsername.length() < 3) {
            model.addAttribute("error", "Username must be at least 3 characters long.");
            model.addAttribute("username", cleanedUsername);
            return "register";
        }
        if (password.length() < 6) {
            model.addAttribute("error", "Password must be at least 6 characters long.");
            model.addAttribute("username", cleanedUsername);
            return "register";
        }
        if (repository.existsByUsername(cleanedUsername)) {
            model.addAttribute("error", "That username is already taken.");
            model.addAttribute("username", cleanedUsername);
            return "register";
        }

        AppUser user = new AppUser(cleanedUsername, passwordEncoder.encode(password));
        repository.save(user);
        return "redirect:/login?registered";
    }

    @GetMapping("/login")
    public String showLoginPage() {
        return "login";
    }
}
