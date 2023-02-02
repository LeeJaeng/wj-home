package org.woojeong.web;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
import org.woojeong.api.v1.hidden.PrayerDao;
import org.woojeong.api.v1.hidden.PrayerService;

import java.util.HashMap;
import java.util.Map;

@Controller
@RequiredArgsConstructor
public class WebController {

    private final PrayerDao prayerDao;

    @GetMapping("/")
    public ModelAndView index() {
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("page/home");

        Map<String, Object> map = new HashMap<>();
        modelAndView.addObject("data", map);

        return modelAndView;
    }

    @GetMapping("/m")
    public ModelAndView indexM() {
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("page/m/m_home");

        Map<String, Object> map = new HashMap<>();
        modelAndView.addObject("data", map);

        return modelAndView;
    }

    @GetMapping("/introduce")
    public ModelAndView introduce(
            @RequestParam(name="type", defaultValue = "introduce") String boardType,
            @RequestParam (name="idx", required = false) Long idx
    ) {
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("page/introduce");

        Map<String, Object> map = new HashMap<>();
        map.put("board_type", boardType);
        map.put("board_idx", idx);
        modelAndView.addObject("data", map);

        return modelAndView;
    }
    @GetMapping("/m/introduce")
        public ModelAndView introduceM(
                @RequestParam(name="type", defaultValue = "introduce") String boardType,
                @RequestParam (name="idx", required = false) Long idx
        ) {
            ModelAndView modelAndView = new ModelAndView();
            modelAndView.setViewName("page/m/m_introduce");

            Map<String, Object> map = new HashMap<>();
            map.put("board_type", boardType);
            map.put("board_idx", idx);
            modelAndView.addObject("data", map);

            return modelAndView;
        }
    @GetMapping("/department")
    public ModelAndView department(
            @RequestParam(name="type", defaultValue = "infant") String boardType,
            @RequestParam (name="idx", required = false) Long idx
    ) {
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("page/department");

        Map<String, Object> map = new HashMap<>();
        map.put("board_type", boardType);
        map.put("board_idx", idx);
        modelAndView.addObject("data", map);

        return modelAndView;
    }
    @GetMapping("/m/department")
    public ModelAndView departmentM(
            @RequestParam(name="type", defaultValue = "infant") String boardType,
            @RequestParam (name="idx", required = false) Long idx
    ) {
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("page/m/m_department");

        Map<String, Object> map = new HashMap<>();
        map.put("board_type", boardType);
        map.put("board_idx", idx);
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

    @GetMapping("/m/worship")
    public ModelAndView worshipM(
            @RequestParam(name="type", defaultValue = "head-pastor") String boardType,
            @RequestParam (name="idx", required = false) Long idx) {
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("page/m/m_worship");

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


    @GetMapping("/m/community")
    public ModelAndView communityM(
            @RequestParam(name="type", defaultValue = "paper") String boardType,
            @RequestParam (name="idx", required = false) Long idx) {
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("page/m/m_community");

        Map<String, Object> map = new HashMap<>();
        map.put("board_type", boardType);
        map.put("board_idx", idx);
        modelAndView.addObject("data", map);

        return modelAndView;
    }




    @GetMapping("/hidden/prayer/{group_key}")
    public ModelAndView prayer(
            @PathVariable (name="group_key") String groupKey) {
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("page/hidden/prayer");

        Map<String, Object> map = new HashMap<>();
        map.put("group_key", groupKey);
        map.put("group_name", prayerDao.getPrayerGroupName(groupKey));
        modelAndView.addObject("data", map);

        return modelAndView;
    }
}
