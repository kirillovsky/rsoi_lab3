package com.bmstu.rsoi_lab3;

import com.bmstu.rsoi_lab3.models.Sailors;
import com.bmstu.rsoi_lab3.models.SailorsPage;
import com.bmstu.rsoi_lab3.models.Ships;


/**
 * Created by Александр on 17.02.2016.
 */
public interface BackendsConnector {

    Sailors getSailor(long id);
    Ships   getShips(long id);
    SailorsPage getSailors(int pageNum, int pageSize);
}
