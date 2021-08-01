package property.app.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import property.app.beans.Adds;
import property.app.beans.User;
import property.app.service.AddsService;
import property.app.service.UserService;

import javax.validation.Valid;
import java.util.Collections;
import java.util.List;

@Controller
public class AuthController {

    private final Logger logger = LoggerFactory.getLogger(AuthController.class);
    private UserService userService;
    private AddsService addsService;



    public AuthController(UserService userService, AddsService addsService) {
        this.userService = userService;
        this.addsService = addsService;
    }

    @GetMapping("/login")
    public String login() {
        return "auth/login";
    }

    @GetMapping("/adminlogin")
    public String adminlogin() {
        return "auth/adminlogin";
    }

    @GetMapping("/profile")
    public String profile(Model model) {
        User u  = ((User) SecurityContextHolder.getContext().getAuthentication().getPrincipal());
        model.addAttribute("user", u);
        List<Adds> adds = addsService.findByUserId(u.getId());
        if(null!= adds){
            Collections.reverse(adds);
        }
        model.addAttribute("adds", adds);
        return "auth/profile";
    }

    @GetMapping("/u/{id}")
    public String profile(@PathVariable Long id, Model model) {
        User u  = userService.getById(id).get();
        model.addAttribute("user", u);
        List<Adds> adds = addsService.findByUserId(u.getId());
        if(null!= adds){
            Collections.reverse(adds);
        }
        model.addAttribute("adds", adds);
        return "auth/other-profile";
    }

    @GetMapping("/register")
    public String register(Model model) {
        model.addAttribute("user", new User());
        return "auth/register";
    }

    @PostMapping("/register")
    public String registerNewUser(@Valid User user, BindingResult bindingResult, Model model, RedirectAttributes redirectAttributes) {
        if(bindingResult.hasErrors()) {
            // Show validation errors.
            logger.info("Errors registering a new user.");
            model.addAttribute("user", user);
            model.addAttribute("validationErrors", bindingResult.getAllErrors());
            return "auth/register";
        } else {
            // Register new user.
            User newUser = userService.register(user);
            redirectAttributes
                    .addFlashAttribute("id", newUser.getId())
                    .addFlashAttribute("success", true);
            return "redirect:/register";
        }
    }

    @GetMapping("/users")
    public String users(Model model) {
        model.addAttribute("users", userService.getAllUsers());
        return "auth/users";
    }

    @GetMapping("/user/delete/{id}")
    public String delete(@PathVariable Long id, Model model) {
        userService.deleteById(id);
        return "redirect:/ ";
    }
}
