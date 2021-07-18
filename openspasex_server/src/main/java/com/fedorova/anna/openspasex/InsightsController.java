package com.fedorova.anna.openspasex;

import com.fedorova.anna.openspasex.model.Launch;
import com.fedorova.anna.openspasex.model.Rocket;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class InsightsController {
    private final InsightsProvider provider = new InsightsProvider();

    @RequestMapping(method = RequestMethod.GET, path = "/ui")
    public String loadUi() {
        return "openspacex_ui";
    }

    @RequestMapping(method = RequestMethod.GET, path = "/next_launch")
    @ResponseBody
    public Launch getNextLaunch() {
        return provider.getNextLaunch();
    }

    @RequestMapping(method = RequestMethod.GET, path = "/launches_count")
    @ResponseBody
    public int getLaunchesCnt(@RequestParam(value = "year", required = false) Integer year) {
        return provider.getLaunchesCnt(year);
    }

    @RequestMapping(method = RequestMethod.GET, path = "/rockets")
    @ResponseBody
    public Rocket[] getRockets() {
        return provider.getAllRockets();
    }

    @RequestMapping(method = RequestMethod.GET, path = "/success_rate")
    @ResponseBody
    public double getSuccessRate(@RequestParam(value = "year", required = false) Integer year) {
        return provider.getSuccessRate(year);
    }

    @RequestMapping(method = RequestMethod.GET, path = "/cost")
    @ResponseBody
    public long getCost(@RequestParam(value = "year", required = false) Integer year) {
        return provider.getCost(year);
    }

    @RequestMapping(method = RequestMethod.GET, path = "/mass")
    @ResponseBody
    public long getLaunchedMass(@RequestParam(value = "year", required = false) Integer year) {
        return provider.getLaunchedMass(year);
    }

    @RequestMapping(method = RequestMethod.GET, path = "/crew")
    @ResponseBody
    public long getCrewSize(@RequestParam(value = "year", required = false) Integer year) {
        return provider.getCrewSize(year);
    }
}
