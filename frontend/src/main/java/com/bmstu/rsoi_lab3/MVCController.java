package com.bmstu.rsoi_lab3;


import com.bmstu.rsoi_lab3.exception.BackendConnectionException;
import com.bmstu.rsoi_lab3.exception.BackendNotException;
import com.bmstu.rsoi_lab3.exception.NotAuthorisedException;
import com.bmstu.rsoi_lab3.models.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.sql.Date;
import java.time.LocalDate;
import java.util.*;


/**
 * Created by Александр on 09.02.2016.
 */

@Controller
public class MVCController {

    private BackendsConnector service;

    @Autowired
    private ModelAndViewFactory mvFactory;
    private static final String SESSIONS_COOKIE = "LOGIN";
    private static final Logger log = LoggerFactory.getLogger(MVCController.class);

    @Autowired
    public MVCController(BackendsConnector service) {
        this.service = service;
    }

    @Autowired
    private SecurityTools securityTools;

    @RequestMapping(value = "/", method= RequestMethod.GET)
    public ModelAndView getStartPage(@CookieValue(value= SESSIONS_COOKIE, defaultValue = "") String login){

        ModelAndView mv = mvFactory.getMV("layout", login);
        return mv;
    }


    @RequestMapping(value = "/sailors/{sailorId}", method= RequestMethod.GET)
    public ModelAndView getSailor(@PathVariable Long sailorId,
                                  @CookieValue(value= SESSIONS_COOKIE, defaultValue = "") String login){
        ModelAndView mv = mvFactory.getMV("sailors/sailor", login);
        Sailors s = service.getSailor(sailorId);
        mv.addObject("sailor", s);
        return mv;
    }

    @RequestMapping(value = "/ships/{shipsId}", method= RequestMethod.GET)
    public ModelAndView getShip(@PathVariable long shipsId,
                                @CookieValue(value= SESSIONS_COOKIE, defaultValue = "") String login){
        ModelAndView mv = mvFactory.getMV("ships/ship", login);
        Ships s = service.getShip(shipsId);
        mv.addObject("ship", s);
        return mv;
    }

    @RequestMapping(value = "/sailors", method= RequestMethod.GET)
    public ModelAndView getSailors(@RequestParam(value="per_page", defaultValue = "10") int perPage, @RequestParam(value="page", defaultValue = "1") int pageNum,
                                   @CookieValue(value= SESSIONS_COOKIE, defaultValue = "") String login){
        ModelAndView mv = mvFactory.getMV("sailors/list", login);
        SailorsPage s = service.getSailors(pageNum, perPage);
        mv.addObject("list", s);
        mv.addObject("names", (!s.getContent().isEmpty())? service.getShipsNames(getListShipsIdsFromSailorsPage(s)): null);
        return mv;
    }

    @RequestMapping(value = "/ships", method= RequestMethod.GET)
    public ModelAndView getShips(@RequestParam(value="per_page", defaultValue = "10") int perPage, @RequestParam(value="page", defaultValue = "1") int pageNum,
                                 @CookieValue(value= SESSIONS_COOKIE, defaultValue = "") String login){
        ModelAndView mv = mvFactory.getMV("ships/list", login);
        ShipsPage s = service.getShips(pageNum, perPage);
        mv.addObject("list", s);
        return mv;
    }

    @RequestMapping(value = "/shipForm", method= RequestMethod.GET)
    public ModelAndView getShipCreateForm(@ModelAttribute("newShip") Ships newShip,
                                          @CookieValue(value= SESSIONS_COOKIE, defaultValue = "") String login){
        ModelAndView mv = mvFactory.getMV("ships/form", login);
        validateAuthorisation(mv, login);
        return mv;
    }

    @RequestMapping(value = "/shipForm/{id}", method= RequestMethod.GET)
    public ModelAndView getShipModifyForm(@ModelAttribute("newShip") Ships newShip, @PathVariable("id") long shipsId,
                                          @CookieValue(value= SESSIONS_COOKIE, defaultValue = "") String login){
        ModelAndView mv = mvFactory.getMV("ships/form", login);
        validateAuthorisation(mv, login);
        newShip = service.getShip(shipsId);
        mv.addObject("newShip", newShip);
        mv.addObject("id", shipsId);
        return mv;
    }

    @RequestMapping(value = "/ships", method= RequestMethod.POST)
    public ModelAndView createShip(@Valid @ModelAttribute("newShip") Ships newShip,
                                   BindingResult result, RedirectAttributes redirect,
                                   @CookieValue(value= SESSIONS_COOKIE, defaultValue = "") String login){
        ModelAndView mv = mvFactory.getMV(login);
        validateAuthorisation(mv, login);
        Sessions s = service.getSession(login);

        if (result.hasErrors()) {
            mv = mvFactory.getAuthorisedMV("ships/form");
            mv.addObject("formErrors", result.getAllErrors());
            return mv;
        }

        Ships obj = service.createShips(newShip);
        redirect.addFlashAttribute("globalMessage", "Successfully created a new ship");

        service.updateSessions(s);
        mv.setViewName("redirect:/ships/" + obj.getId());
        return mv;
    }

    @RequestMapping(value = "/ships/{id}", method= RequestMethod.POST)
    public ModelAndView modifyShip(@Valid @ModelAttribute("newShip") Ships newShip,  BindingResult result,
                                   RedirectAttributes redirect, @PathVariable("id") long shipsId,
                                   @CookieValue(value= SESSIONS_COOKIE, defaultValue = "") String login){
        ModelAndView mv = mvFactory.getNotAuthorisedMV("redirect:/ships/" + shipsId);
        validateAuthorisation(mv, login);
        Sessions s = service.getSession(login);

        if (result.hasErrors()) {
            mv = mvFactory.getAuthorisedMV("ships/form");
            mv.addObject("formErrors", result.getAllErrors());
            return mv;
        }
        service.updateShips(newShip);
        redirect.addFlashAttribute("globalMessage", "Successfully modified a new ship");
        service.updateSessions(s);
        return mv;
    }

    @RequestMapping(value = "/deleteShip/{id}", method= RequestMethod.POST)
    public ModelAndView deleteShip(@PathVariable("id") long shipsId, RedirectAttributes redirect,
                                   @CookieValue(value= SESSIONS_COOKIE, defaultValue = "") String login){
        ModelAndView mv = mvFactory.getNotAuthorisedMV("redirect:/ships/");
        validateAuthorisation(mv, login);
        Sessions s = service.getSession(login);

        service.deleteShips(shipsId);
        redirect.addFlashAttribute("lastDeleteMsg", "Successfully deleted ship with id: " + shipsId);

        service.updateSessions(s);
        return mv;
    }

    @RequestMapping(value = "/sailorForm", method= RequestMethod.GET)
    public ModelAndView getSailorCreateForm(@ModelAttribute("newSailor") Sailors newSailor,
                                            @CookieValue(value= SESSIONS_COOKIE, defaultValue = "") String login){
        ModelAndView mv = mvFactory.getMV("sailors/form", login);
        validateAuthorisation(mv, login);

        return mv;
    }

    @RequestMapping(value = "/sailorForm/{id}", method= RequestMethod.GET)
    public ModelAndView getSailorModifyForm(@ModelAttribute("newSailor") Sailors newSailor, @PathVariable("id") long sailorId,
                                            @CookieValue(value= SESSIONS_COOKIE, defaultValue = "") String login){
        newSailor = service.getSailor(sailorId);
        ModelAndView mv = mvFactory.getMV("sailors/form", login);
        validateAuthorisation(mv, login);

        mv.addObject("newSailor", newSailor);
        mv.addObject("id", sailorId);
        return mv;
    }

    @RequestMapping(value = "/sailors", method= RequestMethod.POST)
    public ModelAndView createSailor(@Valid @ModelAttribute("newSailor") Sailors newSailor,
                                     BindingResult result, RedirectAttributes redirect,
                                     @CookieValue(value= SESSIONS_COOKIE, defaultValue = "") String login){
        ModelAndView mv = mvFactory.getMV(login);
        validateAuthorisation(mv, login);
        Sessions s = service.getSession(login);

        if (!result.hasErrors()) {
            newSailor.setHiredate(Date.valueOf(LocalDate.now()));
            validateSailors(newSailor, result);
        }

        if (result.hasErrors()) {
            mv = mvFactory.getAuthorisedMV("sailors/form");
            mv.addObject("formErrors", result.getAllErrors());
            return mv;
        }

        Sailors obj = service.createSailors(newSailor);
        redirect.addFlashAttribute("lastDeleteMsg", "Successfully created a new sailor");

        service.updateSessions(s);
        mv.setViewName("redirect:/sailors/" + obj.getId());
        return mv;
    }

    @RequestMapping(value = "/sailors/{id}", method= RequestMethod.POST)
    public ModelAndView modifySailor(@Valid @ModelAttribute("newSailor") Sailors newSailor,  BindingResult result,
                                   RedirectAttributes redirect, @PathVariable("id") long sailorId,
                                     @CookieValue(value= SESSIONS_COOKIE, defaultValue = "") String login){

        ModelAndView mv = mvFactory.getNotAuthorisedMV("redirect:/sailors/" + sailorId);
        validateAuthorisation(mv, login);
        Sessions s = service.getSession(login);

        if (!result.hasErrors()) {
            newSailor.setHiredate(Date.valueOf(LocalDate.now()));
            validateSailors(newSailor, result);
        }

        if (result.hasErrors()) {
            mv =  mvFactory.getAuthorisedMV("sailors/form");
            mv.addObject("formErrors", result.getAllErrors());
            return mv;
        }
        service.updateSailors(newSailor);
        redirect.addFlashAttribute("globalMessage", "Successfully modified a new sailor");

        service.updateSessions(s);
        return mv;
    }

    @RequestMapping(value = "/deleteSailor/{id}", method= RequestMethod.POST)
    public ModelAndView deleteSailor(@PathVariable("id") long sailorId, RedirectAttributes redirect,
                                     @CookieValue(value= SESSIONS_COOKIE, defaultValue = "") String login){
        ModelAndView mv = mvFactory.getNotAuthorisedMV("redirect:/sailors");
        validateAuthorisation(mv, login);

        Sessions s = service.getSession(login);

        service.deleteSailor(sailorId);
        redirect.addFlashAttribute("lastDeleteMsg", "Successfully deleted sailor with id: " + sailorId);

        service.updateSessions(s);
        return mv;
    }

    private void validateSailors(Sailors newSailor, BindingResult result) {
        if(!service.existsShip(newSailor.getShipEmpl()))
            result.addError(new FieldError("Sailors", "shipEmpl", "Ships with id: " + newSailor.getShipEmpl() + " does not exists"));

    }

    @RequestMapping(value = "/me", method= RequestMethod.GET)
    public ModelAndView getSession(@CookieValue(value= SESSIONS_COOKIE, defaultValue = "") String login){
        ModelAndView mv = mvFactory.getMV("login/session", login);
        validateAuthorisation(mv, login);

        Sessions s;
        try {
            s = service.getSession(login);
        }
        catch (BackendNotException e) {
            throw new NotAuthorisedException();
        }

        s.setLogin(securityTools.decrypt(s.getLogin()));
        mv.addObject("sess", s);

        service.updateSessions(s);
        return mv;
    }

    @RequestMapping(value = "/logout", method= RequestMethod.GET)
    public ModelAndView logout(@CookieValue(value= SESSIONS_COOKIE, defaultValue = "") String login, HttpServletResponse response){
        ModelAndView mv = mvFactory.getNotAuthorisedMV("redirect:/");
        validateAuthorisation(mv, login);

        Sessions s = service.getSession(login);
        service.deleteSessions(s.getSessionId());

        Cookie cook = new Cookie(SESSIONS_COOKIE, "");
        cook.setPath("/");
        response.addCookie(cook);
        return mv;
    }

    private void addAuthAttribute(ModelAndView mv, boolean tst){
        mv.addObject("isAuthorised", tst);
    }

    private void validateAuthorisation(ModelAndView mv, String userId) {
        if(userId == null || userId.equals(""))
            throw new NotAuthorisedException();
        addAuthAttribute(mv, true);
    }

    @RequestMapping(value = "/signUp", method= RequestMethod.GET)
    public ModelAndView getLoginCreateForm(@ModelAttribute("newLoginEntity") Sessions newSession){
        return mvFactory.getNotAuthorisedMV("login/createForm");
    }

    @RequestMapping(value = "/signUp", method= RequestMethod.POST)
    public ModelAndView signUp(@CookieValue(value= SESSIONS_COOKIE, defaultValue = "") String login,
                               @Valid @ModelAttribute("newLoginEntity") Sessions newSession,
                               BindingResult result, RedirectAttributes redirect){
        ModelAndView mv = mvFactory.getMV(login);

        if (!result.hasErrors()) {
            validateCreateSession(newSession, result);
        }

        if (result.hasErrors()) {
            mv.setViewName("login/createForm");
            mv.addObject("formErrors", result.getAllErrors());
            return mv;
        }

        Sessions s = service.createSession(newSession);
        redirect.addFlashAttribute("lastDeleteMsg", "Session for login: " + securityTools.decrypt(s.getLogin()) + " successfully created!");
        mv.setViewName("redirect:/");
        return mv;
    }

    @RequestMapping(value = "/login", method= RequestMethod.GET)
    public ModelAndView getLoginForm(@ModelAttribute("newLoginEntity") Sessions newSession){
        return mvFactory.getNotAuthorisedMV("login/form");
    }

    @RequestMapping(value = "/login", method= RequestMethod.POST)
    public ModelAndView login(@CookieValue(value= SESSIONS_COOKIE, defaultValue = "") String login, @Valid @ModelAttribute("newLoginEntity") Sessions newSession,
                              BindingResult result, RedirectAttributes redirect, HttpServletResponse response){

        ModelAndView mv;
        Sessions s = null;
        if (!result.hasErrors()) {
            s = validateLoginEntity(newSession, result);
        }

        if (result.hasErrors()) {
            mv = mvFactory.getMV("login/form", login);
            mv.addObject("formErrors", result.getAllErrors());
            return mv;
        }


        service.updateSessions(s);
        Cookie cook = new Cookie(SESSIONS_COOKIE, s.getLogin());
        cook.setPath("/");
        response.addCookie(cook);
        redirect.addFlashAttribute("lastDeleteMsg", "Welcome, " + securityTools.decrypt(s.getLogin()));

        mv = mvFactory.getAuthorisedMV("redirect:/");
        return mv;
    }

    /**
     *
     * @param newLoginEntity - not encrypted
     * @param result
     * @return
     */
    private Sessions validateLoginEntity(Sessions newLoginEntity, BindingResult result) {

        Sessions ses;
        try {
            ses = service.getSession(securityTools.encrypt(newLoginEntity.getLogin()));
            log.info(ses.toString());
        }
        catch (BackendNotException e){
            ses = null;
        }
        if(ses == null) {
            result.addError(new FieldError("LoginEntity", "login", "Users with login: " + newLoginEntity.getLogin() + " not exists"));
            return ses;
        }

        else if(!ses.getLogin().equals(securityTools.encrypt(newLoginEntity.getLogin()))
                || !ses.getPassword().equals(securityTools.encrypt(newLoginEntity.getPassword()))) {
            result.addError(new FieldError("LoginEntity", "password", "Wrong password. Try again!"));
            return ses;
        }

        return ses;

    }

    private void validateCreateSession(Sessions newLoginEntity, BindingResult result) {

        Sessions ses;
        try {
            ses = service.getSession(securityTools.encrypt(newLoginEntity.getLogin()));
        }
        catch (BackendNotException e){
            ses = null;
        }
        if(ses != null) {
            result.addError(new FieldError("LoginEntity", "login", "Users with login: " + newLoginEntity.getLogin() + " already exists"));
        }

    }

    private List<Long> getListShipsIdsFromSailorsPage(SailorsPage pg){
        Set<Long> uniqueLst = new HashSet<>(pg.getSize());

        for(SailorsPreview s: pg.getContent())
            uniqueLst.add(s.getShipEmpl());

        return new ArrayList<>(uniqueLst);
    }


    @ExceptionHandler(NotAuthorisedException.class)
    public ModelAndView handleAuthError(NotAuthorisedException exception, HttpServletResponse response) {

        ModelAndView mav =  mvFactory.getNotAuthorisedMV("layout");

        Cookie cook = new Cookie(SESSIONS_COOKIE, "");
        cook.setPath("/");
        response.addCookie(cook);

        mav.addObject("errorMessage", exception.getMessage());
        return mav;
    }


    @ExceptionHandler(RuntimeException.class)
    public ModelAndView handleError(RuntimeException exception, HttpServletRequest request) {
        String auth = "";

        throw exception;
//        if(request.getCookies() != null)
//            for(Cookie c: request.getCookies())
//                if(c.getName().equals(SESSIONS_COOKIE))
//                    auth = (c.getValue() != null)? c.getValue(): "";
//
//        ModelAndView mv = mvFactory.getMV("layout", auth);
//        mv.addObject("errorMessage", exception.getMessage());
//        return mv;
    }






}
