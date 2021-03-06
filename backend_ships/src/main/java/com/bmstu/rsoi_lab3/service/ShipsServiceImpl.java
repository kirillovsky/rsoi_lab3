package com.bmstu.rsoi_lab3.service;

import com.bmstu.rsoi_lab3.domain.Ships;
import com.bmstu.rsoi_lab3.domain.ShipsPreview;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Created by Александр on 09.02.2016.
 */
@Component("shipService")
@Transactional
public class ShipsServiceImpl implements ShipsService {
    private final ShipsRepository repo;

    private static final Logger log = LoggerFactory.getLogger(ShipsServiceImpl.class);


    @Autowired
    public ShipsServiceImpl(ShipsRepository repo) {
        this.repo = repo;
    }

    @Override
    public Page<ShipsPreview> getShipsPageWithPreview(int pageNum, int pageSize) {
        Page<Ships> p = repo.findAll(new PageRequest(pageNum - 1, pageSize));

        List<ShipsPreview> content = new ArrayList<>(p.getSize());

        content.addAll(p.getContent().stream().map(ShipsPreview::new).collect(Collectors.toList()));

        return new PageImpl<>(content, new PageRequest(pageNum, pageSize), p.getTotalElements());
    }

    @Override
    public void deleteShips(long id) {
        repo.delete(new Long(id));
    }

    @Override
    public Ships addShips(Ships s) {
        Ships sailor = repo.save(s);
        return sailor;
    }

    @Override
    public void updateShips(long id, Ships s) {
        s.setId(new Long(id));
        repo.save(s);
    }

    @Override
    public Ships getShips(long id) {
        return repo.findOne(new Long(id));
    }

    @Override
    public boolean hasShips(long id) {
        return repo.exists(new Long(id));
    }

    @Override
    public List<Map<Long, String>> getShipsNames(List<Long> ids) {
        List<Map<Long, String>> map = repo.getShipsNames(removeDuplicatesFromListList(ids));
        return map;
    }

    private List<Long> removeDuplicatesFromListList(List<Long> ids){
        Set<Long> set = new HashSet<>();
        for(Long id: ids)
            set.add(id);
        return new LinkedList<>(set);
    }


}
