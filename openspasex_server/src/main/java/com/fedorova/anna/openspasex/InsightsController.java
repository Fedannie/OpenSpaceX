package com.fedorova.anna.openspasex;

import com.fedorova.anna.openspasex.model.Rocket;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class InsightsController {
    private final InsightsProvider provider = new InsightsProvider();

    @RequestMapping(method = RequestMethod.GET, path = "/rockets")
    public Rocket[] getRockets() {
        return provider.getAllRockets();
    }

    @RequestMapping(method = RequestMethod.GET, path = "/crew")
    public long getCrewSize(@RequestParam(value = "year", required = false) Integer year) {
        return provider.getCrewSize(year);
    }
}
