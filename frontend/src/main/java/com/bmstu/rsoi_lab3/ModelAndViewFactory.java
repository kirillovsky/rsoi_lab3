package com.bmstu.rsoi_lab3;

import org.springframework.web.servlet.ModelAndView;

/**
 * Created by Александр on 24.02.2016.
 */
public interface ModelAndViewFactory {

    ModelAndView getAuthorisedMV();
    ModelAndView getMV(String login);
    ModelAndView getNotAuthorisedMV();

    ModelAndView getAuthorisedMV(String viewName);
    ModelAndView getMV(String viewName, String login);
    ModelAndView getNotAuthorisedMV(String viewName);


}
