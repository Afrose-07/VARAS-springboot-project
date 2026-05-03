package com.varas.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class WebController {

    @GetMapping("/")
    public String index() {
        return "index";
    }

    @GetMapping("/report")
    public String reportPage() {
        return "report";
    }

    @GetMapping("/police")
    public String policePage() {
        return "police";
    }

    @GetMapping("/hospital")
    public String hospitalPage() {
        return "hospital";
    }

    @GetMapping("/admin")
    public String adminPage() {
        return "admin";
    }
}
