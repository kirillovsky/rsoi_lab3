package com.bmstu.rsoi_lab3;


import com.bmstu.rsoi_lab3.domain.Ships;
import com.bmstu.rsoi_lab3.domain.ShipsPreview;
import com.bmstu.rsoi_lab3.service.ShipsService;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.UnexpectedRollbackException;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.io.IOException;
import java.util.List;
import java.util.Map;

/**
 * Created by Александр on 09.02.2016.
 */

@RestController
@RequestMapping("/ships")
public class ShipsController {

    private ShipsService service;

    private static final Logger log = LoggerFactory.getLogger(ShipsController.class);


    @Autowired
    public ShipsController(ShipsService service) {
        this.service = service;
    }

    @RequestMapping(method= RequestMethod.GET)
    public Page<ShipsPreview> getShips(@RequestParam("per_page") int perPage, @RequestParam("page") int pageNum){
        return service.getShipsPageWithPreview(pageNum, perPage);
    }

    @RequestMapping(value = "/names", method= RequestMethod.GET)
    public Iterable<Map<Long, String>> getShipsName(@RequestParam("id") List<Long> listValue){
        log.error(listValue.toString());
        return service.getShipsNames(listValue);

    }

    @RequestMapping(value = "/{shipsId}", method= RequestMethod.GET)
    public Ships getShip(@PathVariable Long shipsId){
        if(!service.hasShips(shipsId)){
            throw new ShipsNotFoundException(shipsId);
        }

        return service.getShips(shipsId);
    }

    @RequestMapping(value = "/{shipsId}", method=RequestMethod.DELETE)
    public void deleteShip(@PathVariable long shipsId){
        if(!service.hasShips(shipsId)){
            throw new ShipsNotFoundException(shipsId);
        }

        service.deleteShips(shipsId);
    }

    @RequestMapping(value = "/{shipsId}", method={RequestMethod.PATCH, RequestMethod.PUT})
    public void updateShips(@PathVariable long shipsId, @RequestBody String ship){
        if(!service.hasShips(shipsId)){
            throw new ShipsNotFoundException(shipsId);
        }

        Ships newS = getShipsFromJson(ship);
        service.updateShips(shipsId, newS);
    }

    @RequestMapping(method=RequestMethod.POST)
    public ResponseEntity<Ships> addShips(@RequestBody String sailor){
        Ships s = service.addShips(getShipsFromJson(sailor));

        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setLocation(ServletUriComponentsBuilder
                .fromCurrentRequest().path("/{id}")
                .buildAndExpand(s.getId()).toUri());

        return new ResponseEntity<>(s, httpHeaders, HttpStatus.CREATED);
    }

    @ResponseStatus(HttpStatus.NOT_FOUND)
    class ShipsNotFoundException extends RuntimeException {

        public ShipsNotFoundException(long sailorId) {
            super("Could not find Sailor[" + sailorId + "]");
        }
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    class ShipsBadRequest extends RuntimeException {

        public ShipsBadRequest(IOException e) {
            super(e.getMessage());
        }
    }



    private Ships getShipsFromJson(String json){
        Ships result;

        try {
            result = new ObjectMapper().readValue(json, Ships.class);
        } catch (IOException e) {
            throw new ShipsBadRequest(e);
        }

        return result;
    }

    private List<Long> getShipsIdsFromJson(String json){
        List<Long> result;

        try {
            result = new ObjectMapper().readValue(json, new TypeReference<List<Long>>(){});
        } catch (IOException e) {
            throw new ShipsBadRequest(e);
        }

        return result;
    }

    @ResponseStatus(value=HttpStatus.BAD_REQUEST, reason="Ship with sended id doesnt not exists")
    @ExceptionHandler(UnexpectedRollbackException.class)
    public void shipWithExpectedIdDoesntExists(){

    }


}
