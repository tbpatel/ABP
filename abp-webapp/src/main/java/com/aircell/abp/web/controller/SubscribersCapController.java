package com.aircell.abp.web.controller;

import com.aircell.abp.model.Flight;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.validation.BindException;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class SubscribersCapController extends ABPAbstractCommandController {

    private final Logger logger = LoggerFactory.getLogger(getClass());
    private Flight flight = null;

    @Override
    public ModelAndView handler(
    HttpServletRequest req, HttpServletResponse res, Object command,
    BindException errors
    ) {

        logger.debug("SubscribersCapController.handler - Start ");

        ModelAndView mv;

        String maxNoOfActiveSubscribers =
        req.getParameter("maxNoOfActiveSubscribers");
        int maxSubscribersAllowed = 50;
        if (maxNoOfActiveSubscribers != null) {
            maxSubscribersAllowed = Integer.parseInt(maxNoOfActiveSubscribers);
            // Retrieve flight information
            Flight flight = getFlight();

            int noOfActiveSubscribers = flight.getNoOfActiveSubscribers();
            if (noOfActiveSubscribers >= maxSubscribersAllowed) {
                logger.debug(
                "SubscribersCapController.handler - no of new active "
                + "subscribers reached max allowed : noOfActiveSubscribers = "
                + noOfActiveSubscribers + "   ,  maxSubscribersAllowed = "
                + maxSubscribersAllowed
                );
                mv = createFailureModelAndView(req);
            } else {
                mv = createSuccessModelAndView(req);
            }
        } else {
            logger.debug(
            "SubscribersCapController.handler - no maxNoOfActiveSubscribers "
            + "parameter passed, redirect to splash page"
            );
            mv = createFailureModelAndView(req);
        }

        logger.debug("SubscribersCapController.handler - End");
        return mv;
    }

    public Flight getFlight() {
        return this.flight;
    }

    public void setFlight(Flight flight) {
        this.flight = flight;
    }

}
