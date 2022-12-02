package com.epsi.decathlon.controller;

import com.epsi.decathlon.entity.ResultEntity;
import com.epsi.decathlon.entity.SportEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class FormController {

    @RequestMapping("/")
    public String home(Model model) {
        model.addAttribute("sport", new SportEntity());
        return "index";
    }

    @RequestMapping("/result")
    public String result(String result, Model model) {
        model.addAttribute("result", result);
        return "result";
    }
}
