package com.bmstu.rsoi_lab3;


import com.bmstu.rsoi_lab3.models.Sailors;
import com.bmstu.rsoi_lab3.models.SailorsPage;
import com.bmstu.rsoi_lab3.models.SailorsPreview;
import com.bmstu.rsoi_lab3.models.Ships;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;


/**
 * Created by Александр on 09.02.2016.
 */

@Controller
public class MVCController {

    private BackendsConnector service;
    private static final Logger log = LoggerFactory.getLogger(FrontendApplication.class);

    @Autowired
    public MVCController(BackendsConnector service) {
        this.service = service;
    }


    @RequestMapping(value = "/sailors/{sailorId}", method= RequestMethod.GET)
    public ModelAndView getSailor(@PathVariable Long sailorId){
        Sailors s = service.getSailor(sailorId);
        return new ModelAndView("sailors/sailor", "sailor", s);
    }

    @RequestMapping(value = "/ships/{shipsId}", method= RequestMethod.GET)
    public ModelAndView getShip(@PathVariable long shipsId){
        Ships s = service.getShips(shipsId);
        return new ModelAndView("ships/ship", "ship", s);
    }

    @RequestMapping(value = "/sailors", method= RequestMethod.GET)
    public ModelAndView getSailors(@RequestParam("per_page") int perPage, @RequestParam("page") int pageNum){
        SailorsPage s = service.getSailors(pageNum, perPage);
        log.info(s.getContent().toString());
        return new ModelAndView("sailors/list", "list", s);
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
