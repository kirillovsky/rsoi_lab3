package com.bmstu.rsoi_lab3;


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
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.sql.Date;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;


/**
 * Created by Александр on 09.02.2016.
 */

@Controller
public class MVCController {

    private BackendsConnector service;

    @Autowired
    private ModelAndViewFactory mvFactory;
    private static final String SESSIONS_COOKIE = "LOGIN";
    private static final Logger log = LoggerFactory.getLogger(FrontendApplication.class);

    @Autowired
    public MVCController(BackendsConnector service) {
        this.service = service;
    }

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
        ModelAndView mv = mvFactory.getMV("redirect:/ships/" + shipsId, login);
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
        ModelAndView mv = mvFactory.getMV("redirect:/ships/", login);
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
        ModelAndView mv = mvFactory.getAuthorisedMV();
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

        Sailors obj =  service.createSailors(newSailor);
        redirect.addFlashAttribute("globalMessage", "Successfully created a new sailor");

        service.updateSessions(s);
        //mv.setViewName("redirect:/sailors/" + obj.getId());
        return new ModelAndView("redirect:/sailors/" + obj.getId());
    }

    @RequestMapping(value = "/sailors/{id}", method= RequestMethod.POST)
    public ModelAndView modifySailor(@Valid @ModelAttribute("newSailor") Sailors newSailor,  BindingResult result,
                                   RedirectAttributes redirect, @PathVariable("id") long sailorId,
                                     @CookieValue(value= SESSIONS_COOKIE, defaultValue = "") String login){

        ModelAndView mv = mvFactory.getAuthorisedMV("redirect:/sailors/" + sailorId);
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
        ModelAndView mv = mvFactory.getAuthorisedMV("redirect:/sailors");
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
        log.info("TST 11");
        ModelAndView mv = mvFactory.getMV("login/session", login);
        validateAuthorisation(mv, login);

        log.info("TST 11");
        Sessions s;
        try {
            s = service.getSession(login);
        }
        catch (BackendNotException e) {
            log.info("ALKI: " + e.getMessage());
            throw new NotAuthorisedException();
        }

        log.info("TST 11");

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
        redirect.addFlashAttribute("lastDeleteMsg", "Session for login: " + s.getLogin() + " successfully created!");
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
            log.info("ALKI " + login);
            mv = mvFactory.getMV("login/form", login);
            mv.addObject("formErrors", result.getAllErrors());
            return mv;
        }
        log.info("---------------------------------");
        log.info("TST 2");

        service.updateSessions(s);
        log.info("Cookie try to set");
        Cookie cook = new Cookie(SESSIONS_COOKIE, s.getLogin());
        cook.setPath("/");
        response.addCookie(cook);
        log.info("Cookie setted");

        log.info("TST 1");
        mv = new ModelAndView("layout");
        mv.addObject("isAuthorised", true);
        mv.addObject("lastDeleteMsg", "Welcome, " + s.getLogin());
        return mv;
    }

    private Sessions validateLoginEntity(Sessions newLoginEntity, BindingResult result) {

        Sessions ses;
        try {
            ses = service.getSession(newLoginEntity.getLogin());
        }
        catch (BackendNotException e){
            ses = null;
        }
        if(ses == null) {
            result.addError(new FieldError("LoginEntity", "login", "Users with login: " + newLoginEntity.getLogin() + " not exists"));
            return ses;
        }
        else if(!ses.getLogin().equals(newLoginEntity.getLogin()) && ses.getPassword().equals(newLoginEntity.getPassword())) {
            result.addError(new FieldError("LoginEntity", "password", "Wrong password. Try again!"));
            return ses;
        }

        return ses;

    }

    private void validateCreateSession(Sessions newLoginEntity, BindingResult result) {

        Sessions ses;
        try {
            ses = service.getSession(newLoginEntity.getLogin());
        }
        catch (BackendNotException e){
            ses = null;
        }
        if(ses != null) {
            result.addError(new FieldError("LoginEntity", "login", "Users with login: " + newLoginEntity.getLogin() + " already exists"));
        }

    }

    private List<Long> getListShipsIdsFromSailorsPage(SailorsPage pg){
        List<Long> lst = new ArrayList<>(pg.getSize());

        for(SailorsPreview s: pg.getContent())
            lst.add(s.getShipEmpl());

        return lst;
    }


    @ExceptionHandler(NotAuthorisedException.class)
    public ModelAndView handleAuthError(NotAuthorisedException exception) {

        ModelAndView mav =  mvFactory.getNotAuthorisedMV("layout");
        mav.addObject("errorMessage", exception.getMessage());
        return mav;
    }



    @ExceptionHandler(RuntimeException.class)
    public ModelAndView handleError(RuntimeException exception) {

        ModelAndView mav = new ModelAndView("layout");
        mav.addObject("errorMessage", exception.getMessage());
        return mav;
    }



//    @RequestMapping(value = "/{userId}", method={RequestMethod.PATCH, RequestMethod.PUT})
//    public void updateUser(@PathVariable long userId, @RequestBody String user){
//        if(!service.hasUser(userId)){
//            throw new UsersNotFoundException(userId);
//        }
//
//        Users newUser = getUsersFromJson(user);
//        service.updateUser(userId, newUser);
//    }
//
//    @RequestMapping(method=RequestMethod.POST)
//    public ResponseEntity<?> addUser(@RequestBody String user){
//        Users s = service.addUser(getUsersFromJson(user));
//
//        HttpHeaders httpHeaders = new HttpHeaders();
//        httpHeaders.setLocation(ServletUriComponentsBuilder
//                .fromCurrentRequest().path("/{id}")
//                .buildAndExpand(s.getId()).toUri());
//
//        return new ResponseEntity<>(null, httpHeaders, HttpStatus.CREATED);
//    }
//
//    @ResponseStatus(HttpStatus.NOT_FOUND)
//    class UsersNotFoundException extends RuntimeException {
//
//        public UsersNotFoundException(long sailorId) {
//            super("Could not find Sailor[" + sailorId + "]");
//        }
//    }
//
//    @ResponseStatus(HttpStatus.BAD_REQUEST)
//    class UsersBadRequest extends RuntimeException {
//
//        public UsersBadRequest(IOException e) {
//            super(e.getMessage());
//        }
//    }
//
//    private Users getUsersFromJson(String json){
//        Users result;
//
//        try {
//            result = new ObjectMapper().readValue(json, Users.class);
//        } catch (IOException e) {
//            throw new UsersBadRequest(e);
//        }
//
//        return result;
//    }
//
//    @ResponseStatus(value=HttpStatus.BAD_REQUEST, reason="Ship with sended id doesnt not exists")
//    @ExceptionHandler(UnexpectedRollbackException.class)
//    public void shipWithExpectedIdDoesntExists(){
//
//    }



}
