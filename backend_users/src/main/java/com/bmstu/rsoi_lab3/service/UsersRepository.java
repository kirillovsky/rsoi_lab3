package com.bmstu.rsoi_lab3.service;

import com.bmstu.rsoi_lab3.domain.Users;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

/**
 * Created by Александр on 08.02.2016.
 */

@Repository
public interface UsersRepository extends PagingAndSortingRepository<Users, Long> {

    Page<Users> findAll(Pageable pageable);

    Users save(Users s);

    Users findOne(Long aLong);

    boolean exists(Long aLong);

    Iterable<Users> findAll();

    long count();

    void delete(Long aLong);
}
