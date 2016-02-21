package com.bmstu.rsoi_lab3.servers.sailors.service;

import com.bmstu.rsoi_lab3.servers.sailors.domain.Sailors;
import com.bmstu.rsoi_lab3.servers.sailors.domain.SailorsPreview;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by Александр on 09.02.2016.
 */
@Component("sailorService")
@Transactional
public class SailorsServiceImpl implements SailorsService {
    private final SailorsRepository repo;

    @Autowired
    public SailorsServiceImpl(SailorsRepository repo) {
        this.repo = repo;
    }

    @Override
    public Page<SailorsPreview> getSailorsPageWithPreview(int pageNum, int pageSize) {
        Page<Sailors> p = repo.findAll(new PageRequest(pageNum - 1, pageSize));

        List<SailorsPreview> content = new ArrayList<>(p.getSize());

        content.addAll(p.getContent().stream().map(SailorsPreview::new).collect(Collectors.toList()));

        return new PageImpl<>(content, new PageRequest(pageNum, pageSize), p.getTotalElements());
    }

    @Override
    public void deleteSailor(long id) {
        repo.delete(new Long(id));
    }

    @Override
    public Sailors addSailor(Sailors s) {
        Sailors sailor = repo.save(s);
        return sailor;
    }

    @Override
    public void updateSailor(long id, Sailors s) {
        s.setId(new Long(id));
        repo.save(s);
    }

    @Override
    public Sailors getSailor(long id) {
        return repo.findOne(new Long(id));
    }

    @Override
    public boolean hasSailor(long id) {
        return repo.exists(new Long(id));
    }

//    private boolean hasShip(long id){
//        //Todo: request to static
//        String request = "http://localhost:8080/ships/" + id;
//
//        RestTemplate rst = new RestTemplate();
//        ResponseEntity<Object> response = rst.exchange(request, HttpMethod.GET, null, Object.class);
//
//        if (response.getStatusCode() == HttpStatus.OK) {
//            return true;
//        } else
//            return false;
//    }

//    private Sailors secureSaveSailor(Sailors s){
//        Sailors result;
//        try {
//            result = repo.save(s);
//        }
//        catch(UnexpectedRollbackException sqlEx) {
//            throw new SailorsController.SailorsForeignKeyToShipsDoesNotExists("Ships["+s.getId()+"] for " + s + " does not exists: " + sqlEx.getClass());
//        }
//        return result;
//    }

}
