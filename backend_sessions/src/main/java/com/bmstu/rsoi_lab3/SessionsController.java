package com.bmstu.rsoi_lab3;


import com.bmstu.rsoi_lab3.domain.Sessions;
import com.bmstu.rsoi_lab3.service.SessionsRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.UnexpectedRollbackException;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

/**
 * Created by Александр on 09.02.2016.
 */

@RestController
@RequestMapping("/sessions")
public class SessionsController {

    private SessionsRepository service;
    private static final Logger log = LoggerFactory.getLogger(SessionsController.class);


    @Autowired
    public SessionsController(SessionsRepository service) {
        this.service = service;
    }

    @RequestMapping(value = "/{login}", method=RequestMethod.GET)
    public Sessions getSession(@PathVariable String login){
        Sessions s = service.findByLogin(login);

        if(s == null){
            throw new SessionsNotFoundException(login);
        }

        if(s.getExpiredTime() <= System.currentTimeMillis()) {
            service.delete(s.getSessionId());
            throw new SessionsNotFoundException(login);
        }

        return s;
    }


    @RequestMapping(value = "/{sessionId}", method=RequestMethod.DELETE)
    public void deleteSessions(@PathVariable long sessionId){
        if(!service.exists(sessionId)){
            throw new SessionsNotFoundException(sessionId);
        }

        service.delete(sessionId);
    }

    @RequestMapping(value = "/{sessionId}", method={RequestMethod.PATCH, RequestMethod.PUT})
    public void updateSessions(@PathVariable long sessionId){
        Sessions newS = service.findOne(sessionId);
        if(newS == null)
            throw new SessionsNotFoundException(sessionId);

        newS.refreshExpiredTime();
        service.save(newS);
    }

    @RequestMapping(method=RequestMethod.POST)
    public ResponseEntity<Sessions> addSessions(@RequestBody String str){
        Sessions s = getSessionsFromJson(str);

        if(service.findByLogin(s.getLogin()) != null)
            throw new SessionsAlreadyExists(s);

        s.refreshExpiredTime();
        s = service.save(s);

        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setLocation(ServletUriComponentsBuilder.fromCurrentRequest()
                .buildAndExpand(s.getSessionId()).toUri());

        return new ResponseEntity<>(s, httpHeaders, HttpStatus.CREATED);

    }

    @ResponseStatus(HttpStatus.NOT_FOUND)
    class SessionsNotFoundException extends RuntimeException {

        public SessionsNotFoundException(long sessionId) {
            super("Could not find Sessions[" + sessionId + "]");
        }

        public SessionsNotFoundException(String login) {
            super("Could not find Sessions for login=" + login);
        }
    }

    @ResponseStatus(HttpStatus.CONFLICT)
    class SessionsAlreadyExists extends RuntimeException {

        public SessionsAlreadyExists(Sessions s) {
            super("Sessions with such login:"+ s.getLogin() +" already exists");
        }
    }

    @ResponseStatus(HttpStatus.CONFLICT)
    class SessionsBadRequest extends RuntimeException {

        public SessionsBadRequest() {
            super("Wrong json format");
        }
    }



    @ResponseStatus(value=HttpStatus.BAD_REQUEST, reason="Sessions with sended id doesnt not exists")
    @ExceptionHandler(UnexpectedRollbackException.class)
    public void shipWithExpectedIdDoesntExists(){

    }

    private Sessions getSessionsFromJson(String json){
        Sessions result;

        try {
            result = new ObjectMapper().readValue(json, Sessions.class);
        } catch (IOException e) {
            throw new SessionsBadRequest();
        }

        return result;
    }



}
