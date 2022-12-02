package com.epsi.decathlon.controller;

import com.epsi.decathlon.service.DecathlonService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

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
    public void ping() throws IOException, URISyntaxException {
        decathlonService.ping(BASE_URL + "ping");
    }

    @PostMapping(value = "/getSports")
    public String getSports(@RequestParam String imageUrl) throws IOException {
        return decathlonService.getSports(BASE_URL + "sportclassifier/predict/", imageUrl);
    }
}
