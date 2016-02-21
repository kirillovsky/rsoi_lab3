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
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.sql.Date;
import java.time.LocalDate;


/**
 * Created by Александр on 09.02.2016.
 */

@Controller
public class MVCController {

    private BackendsConnector service;
    private static final String USER_COOKIE = "USER_ID";
    private static final Logger log = LoggerFactory.getLogger(FrontendApplication.class);

    @Autowired
    public MVCController(BackendsConnector service) {
        this.service = service;
    }

    @RequestMapping(value = "/", method= RequestMethod.GET)
    public ModelAndView getStartPage(@CookieValue(value=USER_COOKIE, defaultValue = "-1") long userId){

        log.info("");
        ModelAndView mv = new ModelAndView("layout");
        addAuthAttribute(mv, testAuthorised(userId));
        return mv;
    }

    private void addAuthAttribute(ModelAndView mv, boolean tst){
        mv.addObject("isAuthorised", tst);
    }

    private boolean testAuthorised(long userId) {
        if(userId <= 0)
            return false;

        return service.testSessionForUserId(userId);
    }

    @RequestMapping(value = "/sailors/{sailorId}", method= RequestMethod.GET)
    public ModelAndView getSailor(@PathVariable Long sailorId){
        Sailors s = service.getSailor(sailorId);
        return new ModelAndView("sailors/sailor", "sailor", s);
    }

    @RequestMapping(value = "/ships/{shipsId}", method= RequestMethod.GET)
    public ModelAndView getShip(@PathVariable long shipsId){
        Ships s = service.getShip(shipsId);
        return new ModelAndView("ships/ship", "ship", s);
    }

    @RequestMapping(value = "/sailors", method= RequestMethod.GET)
    public ModelAndView getSailors(@RequestParam(value="per_page", defaultValue = "10") int perPage, @RequestParam(value="page", defaultValue = "1") int pageNum){
        SailorsPage s = service.getSailors(pageNum, perPage);
        log.info(s.getContent().toString());
        return new ModelAndView("sailors/list", "list", s);
    }

    @RequestMapping(value = "/ships", method= RequestMethod.GET)
    public ModelAndView getShips(@RequestParam(value="per_page", defaultValue = "10") int perPage, @RequestParam(value="page", defaultValue = "1") int pageNum){
        ShipsPage s = service.getShips(pageNum, perPage);
        log.info(s.getContent().toString());
        return new ModelAndView("ships/list", "list", s);
    }

    @RequestMapping(value = "/shipForm", method= RequestMethod.GET)
    public ModelAndView getShipCreateForm(@ModelAttribute("newShip") Ships newShip){
        ModelAndView mv = new ModelAndView("ships/form");
        return mv;
    }

    @RequestMapping(value = "/shipForm/{id}", method= RequestMethod.GET)
    public ModelAndView getShipModifyForm(@ModelAttribute("newShip") Ships newShip, @PathVariable("id") long shipsId){
        newShip = service.getShip(shipsId);
        ModelAndView mv = new ModelAndView("ships/form");
        mv.addObject("newShip", newShip);
        mv.addObject("id", shipsId);
        return mv;
    }

    @RequestMapping(value = "/ships", method= RequestMethod.POST)
    public ModelAndView createShip(@Valid @ModelAttribute("newShip") Ships newShip,  BindingResult result,
                                   RedirectAttributes redirect, @CookieValue(value=USER_COOKIE, defaultValue = "-1") long Id){
        ModelAndView mv = new ModelAndView();
        validateAuthorisation(mv, Id);

        if (result.hasErrors()) {
            return new ModelAndView("ships/form", "formErrors", result.getAllErrors());
        }

        Ships obj =  service.getShip(service.createShips(newShip));
        log.info(obj.toString());
        redirect.addFlashAttribute("globalMessage", "Successfully created a new ship");

        service.refreshSessionForUserId(Id);
        mv.setViewName("redirect:/ships/" + obj.getId());
        return mv;
    }

    @RequestMapping(value = "/ships/{id}", method= RequestMethod.POST)
    public ModelAndView modifyShip(@Valid @ModelAttribute("newShip") Ships newShip,  BindingResult result,
                                   RedirectAttributes redirect, @PathVariable("id") long shipsId,
                                   @CookieValue(value=USER_COOKIE, defaultValue = "-1") long Id){
        ModelAndView mv = new ModelAndView("redirect:/ships/" + shipsId);
        validateAuthorisation(mv, Id);

        if (result.hasErrors()) {
            return new ModelAndView("ships/form", "formErrors", result.getAllErrors());
        }
        service.updateShips(newShip);
        redirect.addFlashAttribute("globalMessage", "Successfully modified a new ship");

        service.refreshSessionForUserId(Id);
        return mv;
    }

    @RequestMapping(value = "/deleteShip/{id}", method= RequestMethod.POST)
    public ModelAndView deleteShip(@PathVariable("id") long shipsId, RedirectAttributes redirect,
                                   @CookieValue(value=USER_COOKIE, defaultValue = "-1") long Id){
        ModelAndView mv = new ModelAndView("redirect:/ships/");
        validateAuthorisation(mv, Id);

        service.deleteShips(shipsId);
        redirect.addFlashAttribute("lastDeleteMsg", "Successfully deleted ship with id: " + shipsId);

        service.refreshSessionForUserId(Id);
        return mv;
    }

    @RequestMapping(value = "/sailorForm", method= RequestMethod.GET)
    public ModelAndView getSailorCreateForm(@ModelAttribute("newSailor") Sailors newSailor){
        ModelAndView mv = new ModelAndView("sailors/form");
        return mv;
    }

    @RequestMapping(value = "/sailorForm/{id}", method= RequestMethod.GET)
    public ModelAndView getSailorModifyForm(@ModelAttribute("newSailor") Sailors newSailor, @PathVariable("id") long sailorId){
        newSailor = service.getSailor(sailorId);
        ModelAndView mv = new ModelAndView("sailors/form");
        mv.addObject("newSailor", newSailor);
        mv.addObject("id", sailorId);
        return mv;
    }

    @RequestMapping(value = "/sailors", method= RequestMethod.POST)
    public ModelAndView createSailor(@Valid @ModelAttribute("newSailor") Sailors newSailor,  BindingResult result,
                                   RedirectAttributes redirect, @CookieValue(value=USER_COOKIE, defaultValue = "-1") long Id){
        ModelAndView mv = new ModelAndView();
        validateAuthorisation(mv, Id);

        if (!result.hasErrors()) {
            newSailor.setHiredate(Date.valueOf(LocalDate.now()));
            validateSailors(newSailor, result);
        }

        if (result.hasErrors()) {
            return new ModelAndView("sailors/form", "formErrors", result.getAllErrors());
        }

        Sailors obj =  service.getSailor(service.createSailors(newSailor));
        log.info(obj.toString());
        redirect.addFlashAttribute("globalMessage", "Successfully created a new sailor");

        service.refreshSessionForUserId(Id);
        mv.setViewName("redirect:/sailors/" + obj.getId());
        return mv;
    }

    @RequestMapping(value = "/sailors/{id}", method= RequestMethod.POST)
    public ModelAndView modifySailor(@Valid @ModelAttribute("newSailor") Sailors newSailor,  BindingResult result,
                                   RedirectAttributes redirect, @PathVariable("id") long sailorId, @CookieValue(value=USER_COOKIE, defaultValue = "-1") long Id){

        ModelAndView mv = new ModelAndView("redirect:/sailors/" + sailorId);
        validateAuthorisation(mv, Id);

        if (!result.hasErrors()) {
            newSailor.setHiredate(Date.valueOf(LocalDate.now()));
            validateSailors(newSailor, result);
        }

        if (result.hasErrors()) {
            return new ModelAndView("sailors/form", "formErrors", result.getAllErrors());
        }
        service.updateSailors(newSailor);
        redirect.addFlashAttribute("globalMessage", "Successfully modified a new sailor");

        service.refreshSessionForUserId(Id);
        return mv;
    }

    @RequestMapping(value = "/deleteSailor/{id}", method= RequestMethod.POST)
    public ModelAndView deleteSailor(@PathVariable("id") long sailorId, RedirectAttributes redirect, @CookieValue(value=USER_COOKIE, defaultValue = "-1") long Id){
        ModelAndView mv = new ModelAndView("redirect:/sailors/");
        validateAuthorisation(mv, Id);
        service.deleteSailor(sailorId);
        redirect.addFlashAttribute("lastDeleteMsg", "Successfully deleted sailor with id: " + sailorId);
        service.refreshSessionForUserId(Id);
        return mv;
    }

    private void validateSailors(Sailors newSailor, BindingResult result) {
        if(!service.existsShip(newSailor.getShipEmpl()))
            result.addError(new FieldError("Sailors", "shipEmpl", "Ships with id: " + newSailor.getShipEmpl() + " does not exists"));

    }

    @RequestMapping(value = "/userForm", method= RequestMethod.GET)
    public ModelAndView getUsersCreateForm(@ModelAttribute("newUser") Users newSailor){
        ModelAndView mv = new ModelAndView("users/form");
        return mv;
    }

    @RequestMapping(value = "/userForm/{id}", method= RequestMethod.GET)
    public ModelAndView getUsersModifyForm(@ModelAttribute("newUser") Users newUser, @PathVariable("id") long userId){
        newUser = service.getUser(userId);
        ModelAndView mv = new ModelAndView("users/form");
        mv.addObject("newUser", newUser);
        mv.addObject("id", userId);
        return mv;
    }

    @RequestMapping(value = "/users", method= RequestMethod.POST)
    public ModelAndView createUser(@Valid @ModelAttribute("newUser") Users newUser,  BindingResult result,
                                     RedirectAttributes redirect){
        if (!result.hasErrors()) {
            validateUserCreate(newUser, result);
        }

        if (result.hasErrors()) {
            return new ModelAndView("users/form", "formErrors", result.getAllErrors());
        }

        Users obj =  service.getUser(service.createUser(newUser));
        log.info(obj.toString());
        redirect.addFlashAttribute("globalMessage", "Successfully created a new user");
        return new ModelAndView("redirect:/");
    }

    private void validateUserCreate(Users newUser, BindingResult result) {
        if(service.existsUserWithLogin(newUser.getLogin()))
            result.addError(new FieldError("Users", "login", "Users with login: " + newUser.getLogin() + " already exists"));

    }

    @RequestMapping(value = "/users/{id}", method= RequestMethod.POST)
    public ModelAndView modifyUser(@Valid @ModelAttribute("newUser") Users newUser,  BindingResult result,
                                     RedirectAttributes redirect, @PathVariable("id") long userId, @CookieValue(value=USER_COOKIE, defaultValue = "-1") long Id){
        ModelAndView mv = new ModelAndView("redirect:/me/");
        validateAuthorisation(mv, Id);
        if(Id != userId)
            throw new NotAuthorisedException();

        if (!result.hasErrors()) {
            validateUserUpdate(newUser, result);
        }

        if (result.hasErrors()) {
            return new ModelAndView("users/form", "formErrors", result.getAllErrors());
        }
        service.updateUser(newUser);
        redirect.addFlashAttribute("globalMessage", "Successfully modified a new sailor");
        service.refreshSessionForUserId(Id);
        return mv;
    }

    private void validateUserUpdate(Users newUser, BindingResult result) {
        if(!service.existsUserWithLogin(newUser.getLogin()))
            return;

        Users u = service.getUserViaLogin(newUser.getLogin());
        if(u.getId() != newUser.getId())
            result.addError(new FieldError("Users", "login", "Users with login: " + newUser.getLogin() + " already exists"));

    }

    @RequestMapping(value = "/me", method= RequestMethod.GET)
    public ModelAndView getUser(@CookieValue(value=USER_COOKIE, defaultValue = "-1") long userId){
        ModelAndView mv = new ModelAndView("users/user");
        validateAuthorisation(mv, userId);

        Users s = service.getUser(userId);
        mv.addObject("user", s);

        service.refreshSessionForUserId(userId);
        return mv;
    }

    @RequestMapping(value = "/logout", method= RequestMethod.GET)
    public ModelAndView logout(@CookieValue(value=USER_COOKIE, defaultValue = "-1") long userId,  HttpServletResponse response){
        ModelAndView mv = new ModelAndView("layout");
        validateAuthorisation(mv, userId);
        service.logout(userId);
        addAuthAttribute(mv, false);
        response.addCookie(new Cookie(USER_COOKIE, "-1"));
        return mv;
    }

    private void validateAuthorisation(ModelAndView mv, long userId) {
        if(!testAuthorised(userId))
            throw new NotAuthorisedException();
        addAuthAttribute(mv, true);
    }

    @RequestMapping(value = "/login", method= RequestMethod.GET)
    public ModelAndView getLoginForm(@ModelAttribute("newLoginEntity") LoginEntity newLoginEntity){
        ModelAndView mv = new ModelAndView("login/form");
        return mv;
    }

    @RequestMapping(value = "/login", method= RequestMethod.POST)
    public ModelAndView login(@Valid @ModelAttribute("newLoginEntity") LoginEntity newLoginEntity,
                              BindingResult result, RedirectAttributes redirect,  HttpServletResponse response){

        if (!result.hasErrors()) {
            validateLoginEntity(newLoginEntity, result);
        }

        if (result.hasErrors()) {
            return new ModelAndView("login/form", "formErrors", result.getAllErrors());
        }
        Users u = service.getUserViaLogin(newLoginEntity.getLogin());
        service.createOrRefreshSession(u, response);
        redirect.addFlashAttribute("globalMessage", "Welcome, " + u.getFirstName());


        return new ModelAndView("redirect:/me");
    }

    private void validateLoginEntity(LoginEntity newLoginEntity, BindingResult result) {
        if(!service.existsUserWithLogin(newLoginEntity.getLogin())) {
            result.addError(new FieldError("LoginEntity", "login", "Users with login: " + newLoginEntity.getLogin() + " not exists"));
            return;
        }

        Users u = service.getUserViaLogin(newLoginEntity.getLogin());
        if(!u.getPassword().equals(newLoginEntity.getPassword()))
            result.addError(new FieldError("LoginEntity", "password", "Wrong password. Try again!"));

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
