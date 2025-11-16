package com.example.penny_juice.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import com.example.penny_juice.model.Producto;
import com.example.penny_juice.service.ProductoService;


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
