package ru.skubatko.dev.otus.spring.hw24.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class PublicController {

    @GetMapping({"/", "/index"})
    public String showMainPage() {
        return "index";
    }

    @GetMapping("/error")
    public String errorPage() {
        return "error";
    }
}
