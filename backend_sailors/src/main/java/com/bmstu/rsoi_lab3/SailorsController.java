package com.bmstu.rsoi_lab3;


import com.bmstu.rsoi_lab3.domain.Sailors;
import com.bmstu.rsoi_lab3.domain.SailorsPreview;
import com.bmstu.rsoi_lab3.service.SailorsRepository;
import com.bmstu.rsoi_lab3.service.SailorsService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.hibernate.exception.ConstraintViolationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.UnexpectedRollbackException;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.io.IOException;

/**
 * Created by Александр on 09.02.2016.
 */

@RestController
@RequestMapping("/sailors")
public class SailorsController {

    private SailorsService service;

    @Autowired
    public SailorsController(SailorsService service) {
        this.service = service;
    }

    @RequestMapping(method= RequestMethod.GET)
    public Page<SailorsPreview> getSailors(@RequestParam("per_page") int perPage, @RequestParam("page") int pageNum){
        return service.getSailorsPageWithPreview(pageNum, perPage);
    }

    @RequestMapping(value = "/{sailorId}", method= RequestMethod.GET)
    public Sailors getSailor(@PathVariable Long sailorId){
        if(!service.hasSailor(sailorId)){
            throw new SailorsNotFoundException(sailorId);
        }

        return service.getSailor(sailorId);
    }

    @RequestMapping(value = "/{sailorId}", method=RequestMethod.DELETE)
    public void deleteSailor(@PathVariable long sailorId){
        if(!service.hasSailor(sailorId)){
            throw new SailorsNotFoundException(sailorId);
        }

        service.deleteSailor(sailorId);
    }

    @RequestMapping(value = "/{sailorId}", method={RequestMethod.PATCH, RequestMethod.PUT})
    public void updateSailor(@PathVariable long sailorId, @RequestBody String sailor){
        if(!service.hasSailor(sailorId)){
            throw new SailorsNotFoundException(sailorId);
        }

        Sailors newSailor = getSailorsFromJson(sailor);
        service.updateSailor(sailorId, newSailor);
    }

    @RequestMapping(method=RequestMethod.POST)
    public ResponseEntity<?> addSailor(@RequestBody String sailor){
        Sailors s = service.addSailor(getSailorsFromJson(sailor));

        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setLocation(ServletUriComponentsBuilder
                .fromCurrentRequest().path("/{id}")
                .buildAndExpand(s.getId()).toUri());

        return new ResponseEntity<>(null, httpHeaders, HttpStatus.CREATED);
    }

    @ResponseStatus(HttpStatus.NOT_FOUND)
    class SailorsNotFoundException extends RuntimeException {

        public SailorsNotFoundException(long sailorId) {
            super("Could not find Sailor[" + sailorId + "]");
        }
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    class SailorsBadRequest extends RuntimeException {

        public SailorsBadRequest(IOException e) {
            super(e.getMessage());
        }
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public static class SailorsForeignKeyToShipsDoesNotExists extends RuntimeException {
        public SailorsForeignKeyToShipsDoesNotExists(String msg) {
            super(msg);
        }
    }


    private Sailors getSailorsFromJson(String json){
        Sailors result;

        try {
            result = new ObjectMapper().readValue(json, Sailors.class);
        } catch (IOException e) {
            throw new SailorsBadRequest(e);
        }

        return result;
    }

    @ResponseStatus(value=HttpStatus.BAD_REQUEST, reason="Ship with sended id doesnt not exists")
    @ExceptionHandler(UnexpectedRollbackException.class)
    public void shipWithExpectedIdDoesntExists(){

    }


}
