package com.bmstu.rsoi_lab3;


import com.bmstu.rsoi_lab3.domain.Sessions;
import com.bmstu.rsoi_lab3.service.SessionsRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.UnexpectedRollbackException;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.servlet.http.HttpServletRequest;
import java.sql.Date;
import java.time.LocalDate;

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

    @RequestMapping(value = "/{userId}", method=RequestMethod.GET)
    public Sessions getSession(@PathVariable Long userId, HttpServletRequest request){
        if(service.findByUserId(userId) == null){
            throw new SessionsNotFoundException(userId);
        }

        log.info("ALKI: " + request.getMethod() + " " + request.getRequestURI());

        Sessions s = service.findByUserId(userId);

        if(s.getExpiredTime() < System.currentTimeMillis()) {
            service.delete(s.getSessionId());
            throw new SessionsNotFoundException(userId);
        }

        return service.findByUserId(userId);
    }


    @RequestMapping(value = "/{sessionId}", method=RequestMethod.DELETE)
    public void deleteSessions(@PathVariable long sessionId, HttpServletRequest request){
        if(!service.exists(sessionId)){
            throw new SessionsNotFoundException(sessionId);
        }

        log.info("ALKI: " + request.getMethod() + " " + request.getRequestURI());

        service.delete(sessionId);
    }

    @RequestMapping(value = "/{userId}", method={RequestMethod.PATCH, RequestMethod.PUT})
    public void updateSessions(@PathVariable long userId, HttpServletRequest request){
        Sessions newS = service.findByUserId(userId);
        log.info("ALKI: " + request.getMethod() + " " + request.getRequestURI());
        if(newS == null)
            throw new SessionsNotFoundException(userId);

        newS.refreshExpiredTime();
        service.save(newS);
    }

    @RequestMapping(value = "/{userId}", method=RequestMethod.POST)
    public ResponseEntity<?> addSessions(@PathVariable long userId, HttpServletRequest request){
        Sessions s = new Sessions(new Long(userId));
        s = service.save(s);

        log.info("ALKI: " + request.getMethod() + " " + request.getRequestURI());

        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setLocation(ServletUriComponentsBuilder.fromCurrentRequest()
                .buildAndExpand(s.getSessionId()).toUri());

        return new ResponseEntity<>(null, httpHeaders, HttpStatus.CREATED);

    }

    @ResponseStatus(HttpStatus.NOT_FOUND)
    class SessionsNotFoundException extends RuntimeException {

        public SessionsNotFoundException(long sessionId) {
            super("Could not find Sessions[" + sessionId + "]");
        }
    }



    @ResponseStatus(value=HttpStatus.BAD_REQUEST, reason="Sessions with sended id doesnt not exists")
    @ExceptionHandler(UnexpectedRollbackException.class)
    public void shipWithExpectedIdDoesntExists(){

    }


}
