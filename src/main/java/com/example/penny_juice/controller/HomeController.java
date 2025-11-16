package com.example.penny_juice.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {

    @GetMapping("/panel")
    public String panelAdministrativo() {
        return "panel-administrativo";
    }

    @GetMapping("/us")
    public String us() {
        return "us";
    }

    @GetMapping("/learn-more")
    public String learnMore() {
        return "learn-more";
    }
}
