package com.bmstu.rsoi_lab3.servers.users;


import com.bmstu.rsoi_lab3.servers.users.domain.Users;
import com.bmstu.rsoi_lab3.servers.users.service.UsersService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
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
@RequestMapping("/users")
public class UsersController {

    private UsersService service;

    @Autowired
    public UsersController(UsersService service) {
        this.service = service;
    }

    @RequestMapping(value = "/{userId}", method= RequestMethod.GET)
    public Users getUser(@PathVariable Long userId){
        if(!service.hasUser(userId)){
            throw new UsersNotFoundException(userId);
        }

        return service.getUser(userId);
    }

    @RequestMapping(value = "/{userId}", method=RequestMethod.DELETE)
    public void deleteUser(@PathVariable long userId){
        if(!service.hasUser(userId)){
            throw new UsersNotFoundException(userId);
        }

        service.deleteUser(userId);
    }

    @RequestMapping(value = "/{userId}", method={RequestMethod.PATCH, RequestMethod.PUT})
    public void updateUser(@PathVariable long userId, @RequestBody String user){
        if(!service.hasUser(userId)){
            throw new UsersNotFoundException(userId);
        }

        Users newUser = getUsersFromJson(user);
        service.updateUser(userId, newUser);
    }

    @RequestMapping(method=RequestMethod.POST)
    public ResponseEntity<?> addUser(@RequestBody String user){
        Users s = service.addUser(getUsersFromJson(user));

        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setLocation(ServletUriComponentsBuilder
                .fromCurrentRequest().path("/{id}")
                .buildAndExpand(s.getId()).toUri());

        return new ResponseEntity<>(null, httpHeaders, HttpStatus.CREATED);
    }

    @ResponseStatus(HttpStatus.NOT_FOUND)
    class UsersNotFoundException extends RuntimeException {

        public UsersNotFoundException(long sailorId) {
            super("Could not find Sailor[" + sailorId + "]");
        }
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    class UsersBadRequest extends RuntimeException {

        public UsersBadRequest(IOException e) {
            super(e.getMessage());
        }
    }

    private Users getUsersFromJson(String json){
        Users result;

        try {
            result = new ObjectMapper().readValue(json, Users.class);
        } catch (IOException e) {
            throw new UsersBadRequest(e);
        }

        return result;
    }

    @ResponseStatus(value=HttpStatus.BAD_REQUEST, reason="Ship with sended id doesnt not exists")
    @ExceptionHandler(UnexpectedRollbackException.class)
    public void shipWithExpectedIdDoesntExists(){

    }


}
