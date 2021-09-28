package org.woojeong.web;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
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

    @GetMapping("/department")
    public ModelAndView department() {
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("page/department");

        Map<String, Object> map = new HashMap<>();
        modelAndView.addObject("data", map);

        return modelAndView;
    }

    @GetMapping("/worship")
    public ModelAndView worship(
            @RequestParam(name="type", defaultValue = "head-pastor") String boardType,
            @RequestParam (name="idx", required = false) Long idx) {
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("page/worship");

        Map<String, Object> map = new HashMap<>();
        map.put("board_type", boardType);
        map.put("board_idx", idx);
        modelAndView.addObject("data", map);

        return modelAndView;
    }

    @GetMapping("/community")
    public ModelAndView community(
            @RequestParam(name="type", defaultValue = "paper") String boardType,
            @RequestParam (name="idx", required = false) Long idx) {
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("page/community");

        Map<String, Object> map = new HashMap<>();
        map.put("board_type", boardType);
        map.put("board_idx", idx);
        modelAndView.addObject("data", map);

        return modelAndView;
    }
}
