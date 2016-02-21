package com.bmstu.rsoi_lab3.servers.sailors.service;


import com.bmstu.rsoi_lab3.servers.sailors.domain.Sailors;
import com.bmstu.rsoi_lab3.servers.sailors.domain.SailorsPreview;
import org.springframework.data.domain.Page;

/**
 * Created by Александр on 09.02.2016.
 */

public interface SailorsService {

    Page<SailorsPreview> getSailorsPageWithPreview(int pageNum, int pageSize);

    void deleteSailor(long id);

    Sailors addSailor(Sailors s);

    void updateSailor(long id, Sailors s);

    Sailors getSailor(long id);

    boolean hasSailor(long id);
}
