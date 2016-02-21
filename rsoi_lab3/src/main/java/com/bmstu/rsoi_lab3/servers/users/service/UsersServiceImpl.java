package com.bmstu.rsoi_lab3.servers.users.service;

import com.bmstu.rsoi_lab3.servers.users.domain.UsersRepository;
import com.bmstu.rsoi_lab3.servers.users.domain.Users;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Created by Александр on 09.02.2016.
 */
@Service("usersService")
@Transactional
public class UsersServiceImpl implements UsersService {
    private final UsersRepository repo;

    @Autowired
    public UsersServiceImpl(UsersRepository repo) {
        this.repo = repo;
    }


    @Override
    public void deleteUser(long id) {
        repo.delete(new Long(id));
    }

    @Override
    public Users addUser(Users u) {
        Users user = repo.save(u);
        return user;
    }

    @Override
    public void updateUser(long id, Users u) {
        u.setId(new Long(id));
        repo.save(u);
    }

    @Override
    public Users getUser(long id) {
        return repo.findOne(new Long(id));
    }

    @Override
    public boolean hasUser(long id) {
        return repo.exists(new Long(id));
    }

}
