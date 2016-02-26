package com.bmstu.rsoi_lab3;

import com.bmstu.rsoi_lab3.models.*;

import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.Map;


/**
 * Created by Александр on 17.02.2016.
 */
public interface BackendsConnector {


    Sailors getSailor(long id);
    Sailors getSailor(String url);

    Ships getShip(long id);
    Ships getShip(String url);

    SailorsPage getSailors(int pageNum, int pageSize);
    ShipsPage getShips(int pageNum, int pageSize);
    Map<Long, String> getShipsNames(List<Long> ids);

    Ships createShips(Ships s);
    Sailors createSailors(Sailors s);

    void updateSailors(Sailors s);
    void updateShips(Ships s);

    void deleteShips(long id);
    void deleteSailor(long id);

    boolean existsShip(long shipId);

    Sessions getSession(String login);
    Sessions createSession(Sessions s);
    void updateSessions(Sessions s);
    void deleteSessions(long sessionId);


}
