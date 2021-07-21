package com.fedorova.anna.openspasex;

import com.fedorova.anna.openspasex.model.Launch;
import com.fedorova.anna.openspasex.model.Rocket;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.function.Supplier;
import java.util.logging.Level;
import java.util.logging.Logger;

@Controller
public class InsightsController {
    private final InsightsProvider provider = new InsightsProvider();
    private final Logger logger = Logger.getLogger(InsightsController.class.getName());
    private final static String ERROR = "An error occurred during handling the server response. " +
            "Probably the SpaceX API or the response format has changed.";

    @RequestMapping(method = RequestMethod.GET, path = "/ui")
    public String loadUi() {
        return "openspacex_ui";
    }

    private <T> T handleErrors(Supplier<T> request, T emptyResponse) {
        try {
            return request.get();
        } catch (IllegalStateException | NullPointerException exception) {
            logger.log(Level.SEVERE, ERROR, exception);
            return emptyResponse;
        }
    }

    @RequestMapping(method = RequestMethod.GET, path = "/next_launch")
    @ResponseBody
    public Launch getNextLaunch() {
        return handleErrors(provider::getNextLaunch, new Launch());
    }

    @RequestMapping(method = RequestMethod.GET, path = "/launches_count")
    @ResponseBody
    public int getLaunchesCnt(@RequestParam(value = "year", required = false) Integer year) {
        return handleErrors(() -> provider.getLaunchesCnt(year), 0);
    }

    @RequestMapping(method = RequestMethod.GET, path = "/rockets")
    @ResponseBody
    public Rocket[] getRockets() {
        return handleErrors(provider::getAllRockets, new Rocket[0]);
    }

    @RequestMapping(method = RequestMethod.GET, path = "/success_rate")
    @ResponseBody
    public double getSuccessRate(@RequestParam(value = "year", required = false) Integer year) {
        return handleErrors(() -> provider.getSuccessRate(year), 0.);
    }

    @RequestMapping(method = RequestMethod.GET, path = "/cost")
    @ResponseBody
    public long getCost(@RequestParam(value = "year", required = false) Integer year) {
        return handleErrors(() -> provider.getCost(year), 0L);
    }

    @RequestMapping(method = RequestMethod.GET, path = "/mass")
    @ResponseBody
    public long getLaunchedMass(@RequestParam(value = "year", required = false) Integer year) {
        return handleErrors(() -> provider.getLaunchedMass(year), 0L);
    }

    @RequestMapping(method = RequestMethod.GET, path = "/crew")
    @ResponseBody
    public long getCrewSize(@RequestParam(value = "year", required = false) Integer year) {
        return handleErrors(() -> provider.getCrewSize(year), 0L);
    }
}
