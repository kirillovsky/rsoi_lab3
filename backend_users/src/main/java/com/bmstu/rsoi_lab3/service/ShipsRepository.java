package com.bmstu.rsoi_lab3.service;

import com.bmstu.rsoi_lab3.domain.Ships;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

/**
 * Created by Александр on 08.02.2016.
 */

@Repository
public interface ShipsRepository extends PagingAndSortingRepository<Ships, Long> {

    Page<Ships> findAll(Pageable pageable);

    Ships save(Ships s);

    Ships findOne(Long aLong);

    boolean exists(Long aLong);

    Iterable<Ships> findAll();

    long count();

    void delete(Long aLong);
}
