package com.bmstu.rsoi_lab3.servers.ships.service;


import com.bmstu.rsoi_lab3.servers.ships.domain.Ships;
import com.bmstu.rsoi_lab3.servers.ships.domain.ShipsPreview;
import org.springframework.data.domain.Page;

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
}
