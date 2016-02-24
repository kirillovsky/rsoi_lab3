package com.bmstu.rsoi_lab3;

import org.springframework.stereotype.Component;
import org.springframework.web.servlet.ModelAndView;

/**
 * Created by Александр on 24.02.2016.
 */

@Component("MVFactory")
public class ModelAndViewFactoryImpl implements ModelAndViewFactory{

    @Override
    public ModelAndView getAuthorisedMV() {
        return getMV(null);
    }

    @Override
    public ModelAndView getMV(String login) {
        ModelAndView mv = new ModelAndView();

        if(login != null && !login.equals(""))
            mv.addObject("isAuthorised", true);
        else
            mv.addObject("isAuthorised", false);

        return mv;
    }

    @Override
    public ModelAndView getNotAuthorisedMV() {
        return getMV(null);
    }

    @Override
    public ModelAndView getAuthorisedMV(String viewName) {
        return getMV(viewName, "1");
    }

    @Override
    public ModelAndView getMV(String viewName, String login) {
        ModelAndView mv = new ModelAndView(viewName);

        if(login != null && !login.equals(""))
            mv.addObject("isAuthorised", true);
        else
            mv.addObject("isAuthorised", false);

        return mv;
    }

    @Override
    public ModelAndView getNotAuthorisedMV(String viewName) {
        return getMV(viewName, null);
    }
}
