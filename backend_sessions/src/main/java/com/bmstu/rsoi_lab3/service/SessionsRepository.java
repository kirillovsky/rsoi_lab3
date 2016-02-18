package com.bmstu.rsoi_lab3.service;

import com.bmstu.rsoi_lab3.domain.Sessions;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

/**
 * Created by Александр on 08.02.2016.
 */

@Repository
public interface SessionsRepository extends PagingAndSortingRepository<Sessions, Long> {

    Sessions save(Sessions s);

    Sessions findOne(Long aLong);

    boolean exists(Long aLong);
    
    void delete(Long aLong);

    Sessions findByUserId(Long userId);
}
