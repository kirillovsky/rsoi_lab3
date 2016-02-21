package com.bmstu.rsoi_lab3.servers.users.service;


import com.bmstu.rsoi_lab3.servers.users.domain.Users;

/**
 * Created by Александр on 09.02.2016.
 */

public interface UsersService {

    void deleteUser(long id);

    Users addUser(Users s);

    void updateUser(long id, Users s);

    Users getUser(long id);

    boolean hasUser(long id);
}
