package org.woojeong.web;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.ModelAndView;

import java.util.HashMap;
import java.util.Map;

@Controller
public class WebController {
    @GetMapping("/")
    public ModelAndView index() {
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("page/home");

        Map<String, Object> map = new HashMap<>();
        modelAndView.addObject("data", map);

        return modelAndView;
    }

    @GetMapping("/introduce")
    public ModelAndView introduce() {
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("page/introduce");

        Map<String, Object> map = new HashMap<>();
        modelAndView.addObject("data", map);

        return modelAndView;
    }
}
