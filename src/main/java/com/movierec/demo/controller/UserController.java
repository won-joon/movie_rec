package com.movierec.demo.controller;

import com.movierec.demo.domain.User;
import com.movierec.demo.dto.UserDto;
import com.movierec.demo.service.UserService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @GetMapping("/users/signup")
    public String createForm(Model model) {
        model.addAttribute("userForm", new UserDto());
        return "signup";
    }

    @PostMapping("/users/signup")
    public String create(@ModelAttribute("userForm") @Valid UserDto form, BindingResult result) {
        if (result.hasErrors()) {
            return "signup";
        }

        userService.join(form);

        return "redirect:/";
    }
}
