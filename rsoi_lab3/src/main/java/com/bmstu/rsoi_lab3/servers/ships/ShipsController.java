package com.bmstu.rsoi_lab3.servers.ships;


import com.bmstu.rsoi_lab3.servers.ships.domain.Ships;
import com.bmstu.rsoi_lab3.servers.ships.domain.ShipsPreview;
import com.bmstu.rsoi_lab3.servers.ships.service.ShipsService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.UnexpectedRollbackException;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.io.IOException;

/**
 * Created by Александр on 09.02.2016.
 */

@RestController
@RequestMapping("/ships")
public class ShipsController {

    private ShipsService service;

    @Autowired
    public ShipsController(ShipsService service) {
        this.service = service;
    }

    @RequestMapping(method= RequestMethod.GET)
    public Page<ShipsPreview> getSailors(@RequestParam("per_page") int perPage, @RequestParam("page") int pageNum){
        return service.getShipsPageWithPreview(pageNum, perPage);
    }

    @RequestMapping(value = "/{shipsId}", method= RequestMethod.GET)
    public Ships getSailor(@PathVariable Long shipsId){
        if(!service.hasShips(shipsId)){
            throw new ShipsNotFoundException(shipsId);
        }

        return service.getShips(shipsId);
    }

    @RequestMapping(value = "/{shipsId}", method=RequestMethod.DELETE)
    public void deleteSailor(@PathVariable long shipsId){
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
    public ResponseEntity<?> addShips(@RequestBody String sailor){
        Ships s = service.addShips(getShipsFromJson(sailor));

        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setLocation(ServletUriComponentsBuilder
                .fromCurrentRequest().path("/{id}")
                .buildAndExpand(s.getId()).toUri());

        return new ResponseEntity<>(null, httpHeaders, HttpStatus.CREATED);
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

//    @ResponseStatus(HttpStatus.BAD_REQUEST)
//    public static class SailorsForeignKeyToShipsDoesNotExists extends RuntimeException {
//        public SailorsForeignKeyToShipsDoesNotExists(String msg) {
//            super(msg);
//        }
//    }


    private Ships getShipsFromJson(String json){
        Ships result;

        try {
            result = new ObjectMapper().readValue(json, Ships.class);
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
