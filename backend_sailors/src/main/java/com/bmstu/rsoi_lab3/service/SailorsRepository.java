package com.bmstu.rsoi_lab3.service;

import com.bmstu.rsoi_lab3.domain.Sailors;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

/**
 * Created by Александр on 08.02.2016.
 */

@Repository
public interface SailorsRepository extends PagingAndSortingRepository<Sailors, Long> {

    Page<Sailors> findAll(Pageable pageable);

    Sailors save(Sailors s);

    Sailors findOne(Long aLong);

    boolean exists(Long aLong);

    Iterable<Sailors> findAll();

    long count();

    void delete(Long aLong);
}
