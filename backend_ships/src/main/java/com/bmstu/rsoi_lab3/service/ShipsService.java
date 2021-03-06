package com.bmstu.rsoi_lab3.service;


import com.bmstu.rsoi_lab3.domain.Ships;
import com.bmstu.rsoi_lab3.domain.ShipsPreview;
import org.springframework.data.domain.Page;

import java.util.List;
import java.util.Map;

/**
 * Created by Александр on 09.02.2016.
 */

public interface ShipsService {

    Page<ShipsPreview> getShipsPageWithPreview(int pageNum, int pageSize);

    void deleteShips(long id);

    Ships addShips(Ships s);

    void updateShips(long id, Ships s);

    Ships getShips(long id);

    boolean hasShips(long id);

    List<Map<Long, String>> getShipsNames(List<Long> ids);
}
