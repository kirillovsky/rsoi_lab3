package com.bmstu.rsoi_lab3;


import com.bmstu.rsoi_lab3.domain.Sessions;
import com.bmstu.rsoi_lab3.service.SessionsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.transaction.UnexpectedRollbackException;
import org.springframework.web.bind.annotation.*;

/**
 * Created by Александр on 09.02.2016.
 */

@RestController
@RequestMapping("/sessions")
public class SessionsController {

    private SessionsRepository service;

    @Autowired
    public SessionsController(SessionsRepository service) {
        this.service = service;
    }

    @RequestMapping(value = "/{userId}", method= RequestMethod.GET)
    public Sessions getSession(@PathVariable Long userId){
        if(service.findByUserId(userId) == null){
            throw new SessionsNotFoundException(userId);
        }

        return service.findByUserId(userId);
    }


    @RequestMapping(value = "/{sessionId}", method=RequestMethod.DELETE)
    public void deleteSessions(@PathVariable long sessionId){
        if(!service.exists(sessionId)){
            throw new SessionsNotFoundException(sessionId);
        }

        service.delete(sessionId);
    }

    @RequestMapping(value = "/{userId}", method={RequestMethod.PATCH, RequestMethod.PUT})
    public void updateSessions(@PathVariable long userId){
        Sessions newS = service.findByUserId(userId);

        if(newS == null)
            throw new SessionsNotFoundException(userId);

        newS.refreshExpiredTime();
        service.save(newS);
    }

    @RequestMapping(value = "/{userId}", method=RequestMethod.POST)
    public Sessions addSessions(@PathVariable long userId){
        Sessions s = new Sessions(new Long(userId));

        s = service.save(s);
        return s;
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
