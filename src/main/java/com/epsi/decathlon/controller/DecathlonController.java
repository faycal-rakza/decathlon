package com.epsi.decathlon.controller;

import com.epsi.decathlon.entity.ResultEntity;
import com.epsi.decathlon.entity.SportEntity;
import com.epsi.decathlon.service.DecathlonService;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.io.IOException;

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
    public ModelAndView getSports(@ModelAttribute SportEntity sport, Model model) throws IOException {
        System.out.println("imageUrl: " + sport);
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("result");
        ResultEntity result = new ResultEntity();
        result.setName("Il est fort probable que le sport sur cette photo soit : " + decathlonService.getSports(BASE_URL + "sportclassifier/predict/", sport.getImageUrl()));
        modelAndView.addObject("result", result.getName());
        modelAndView.addObject("src", sport.getImageUrl());
        return modelAndView;
    }
}
