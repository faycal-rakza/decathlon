package com.epsi.decathlon.controller;

import com.epsi.decathlon.entity.SportEntity;
import com.epsi.decathlon.service.DecathlonService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.net.URISyntaxException;

@RestController
public class DecathlonController {

    private final DecathlonService decathlonService;

    private final String BASE_URL = "https://api.decathlon.net/sport_vision_api/v1/";

    public DecathlonController(DecathlonService decathlonService) {
        this.decathlonService = decathlonService;
    }

    @GetMapping(value = "/ping")
    public void ping() throws IOException {
        decathlonService.ping(BASE_URL + "ping");
    }

    @PostMapping(value = "/getSport")
    public String getSports(@ModelAttribute SportEntity sport, Model model) throws IOException {
        System.out.println("imageUrl: " + sport);
        return "Il est fort probable que le sport sur cette photo soit : " + decathlonService.getSports(BASE_URL + "sportclassifier/predict/", sport.getImageUrl());
    }
}
