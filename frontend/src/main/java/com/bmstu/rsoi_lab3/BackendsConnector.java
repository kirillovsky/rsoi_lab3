package com.bmstu.rsoi_lab3;

import com.bmstu.rsoi_lab3.models.*;

import javax.servlet.http.HttpServletResponse;


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

    String createShips(Ships s);
    String createSailors(Sailors s);

    void updateSailors(Sailors s);
    void updateShips(Ships s);

    void deleteShips(long id);
    void deleteSailor(long id);

    boolean existsShip(long shipId);

    Users getUser(long userId);

    void updateUser(Users newUser);

    String createUser(Users newUser);

    Users getUser(String user);

    boolean existsUserWithLogin(String login);

    Users getUserViaLogin(String login);

    Sessions createOrRefreshSession(Users u, HttpServletResponse response);
    void setUserCookee(Long userId, HttpServletResponse response);
    void refreshSession(Sessions s);
    void refreshSessionForUserId(Long id);
    Sessions createSession(Long id);
    Sessions getSessionForUserId(Long id);
    Sessions getSession(String str);
    boolean testSessionForUserId(Long id);

    String getUserCookieName();

    void logout(long userId);
}
