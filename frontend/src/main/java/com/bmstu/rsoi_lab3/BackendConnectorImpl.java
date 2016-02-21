package com.bmstu.rsoi_lab3;

import com.bmstu.rsoi_lab3.exception.BackendConnectionException;
import com.bmstu.rsoi_lab3.exception.BackendNotException;
import com.bmstu.rsoi_lab3.models.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import javax.validation.constraints.NotNull;

/**
 * Created by Александр on 17.02.2016.
 */

@Component("backendConnector")
public class BackendConnectorImpl implements BackendsConnector {
    protected static String SAILORS_BACKEND_URL = "http://127.0.0.1:8001/sailors";
    protected static String SHIPS_BACKEND_URL = "http://127.0.0.1:8010/ships";
    protected static String USERS_BACKEND_URL = "http://127.0.0.1:8011/users";
    protected static String SESSIONS_BACKEND_URL = "http://127.0.0.1:8100/sessions";
    protected static String USER_COOKIE_NAME = "USER_ID";

    private static final Logger log = LoggerFactory.getLogger(FrontendApplication.class);

    @Override
    public Sailors getSailor(long id) {
        String request = SAILORS_BACKEND_URL + "/" + id;
        return getSailor(request);
    }

    @Override
    public Sailors getSailor(String url) {
        try {
            return getObjectViaConnect(url, Sailors.class);
        }
        catch (HttpClientErrorException e) {
            if(e.getStatusCode() == HttpStatus.BAD_REQUEST || e.getStatusCode() == HttpStatus.NOT_FOUND)
                throw new BackendNotException("Sailor service", e.getStatusCode());
            else
                throw new BackendConnectionException("Sailor service");
        }
        catch (RuntimeException e){
            throw new BackendConnectionException("Sailor service");
        }

    }

    @Override
    public Ships getShip(long id) {
        String request = SHIPS_BACKEND_URL + "/" + id;
        return getShip(request);
    }

    @Override
    public Ships getShip(String url) {
        try {
            return getObjectViaConnect(url, Ships.class);
        }
        catch (HttpClientErrorException e) {
            if(e.getStatusCode() == HttpStatus.BAD_REQUEST || e.getStatusCode() == HttpStatus.NOT_FOUND)
                throw new BackendNotException("Ships service", e.getStatusCode());
            else
                throw new BackendConnectionException("Ships service");
        }
        catch (RuntimeException e){
            throw new BackendConnectionException("Ships service");
        }
    }

    @Override
    public SailorsPage getSailors(int pageNum, int pageSize) {
        String request = SAILORS_BACKEND_URL + "?page="+pageNum+"&per_page="+pageSize;
        try {
            return getObjectViaConnect(request, SailorsPage.class);
        }
        catch (HttpClientErrorException e) {
            if(e.getStatusCode() == HttpStatus.BAD_REQUEST || e.getStatusCode() == HttpStatus.NOT_FOUND)
                throw new BackendNotException("Sailors service", e.getStatusCode());
            else
                throw new BackendConnectionException("Sailors service");
        }
        catch (RuntimeException e){
            throw new BackendConnectionException("Sailors service");
        }
    }

    @Override
    public ShipsPage getShips(int pageNum, int pageSize) {
        String request = SHIPS_BACKEND_URL + "?page="+pageNum+"&per_page="+pageSize;
        try {
            return getObjectViaConnect(request, ShipsPage.class);
        }
        catch (HttpClientErrorException e) {
            if(e.getStatusCode() == HttpStatus.BAD_REQUEST || e.getStatusCode() == HttpStatus.NOT_FOUND)
                throw new BackendNotException("Ships service", e.getStatusCode());
            else
                throw new BackendConnectionException("Ships service");
        }
        catch (RuntimeException e){
            throw new BackendConnectionException("Ships service");
        }
    }

    @Override
    public String createShips(Ships s) {
        return createObjectViaConnect(SHIPS_BACKEND_URL, s);
    }

    @Override
    public String createSailors(Sailors s) {
        return createObjectViaConnect(SAILORS_BACKEND_URL, s);
    }

    @Override
    public void updateSailors(Sailors s) {
        String request = SAILORS_BACKEND_URL + "/" + s.getId();
        try {
            updateObjectViaConnect(request, s);
        }
        catch (HttpClientErrorException e) {
            if(e.getStatusCode() == HttpStatus.BAD_REQUEST || e.getStatusCode() == HttpStatus.NOT_FOUND)
                throw new BackendNotException("Sailors service", e.getStatusCode());
            else
                throw new BackendConnectionException("Sailors service");
        }
        catch (RuntimeException e){
            throw new BackendConnectionException("Sailors service");
        }

    }

    @Override
    public void updateShips(Ships s) {
        String request = SHIPS_BACKEND_URL + "/" + s.getId();
        try {
            updateObjectViaConnect(request, s);
        }
        catch (HttpClientErrorException e) {
            if(e.getStatusCode() == HttpStatus.BAD_REQUEST || e.getStatusCode() == HttpStatus.NOT_FOUND)
                throw new BackendNotException("Ships service", e.getStatusCode());
            else
                throw new BackendConnectionException("Ships service");
        }
        catch (RuntimeException e){
            throw new BackendConnectionException("Ships service");
        }

    }

    @Override
    public void deleteShips(long id) {
        String request = SHIPS_BACKEND_URL + "/" + id;
        try {
            sendDeleteRequest(request);
        }
        catch (HttpClientErrorException e) {
            if(e.getStatusCode() == HttpStatus.BAD_REQUEST || e.getStatusCode() == HttpStatus.NOT_FOUND)
                throw new BackendNotException("Ships service", e.getStatusCode());
            else
                throw new BackendConnectionException("Ships service");
        }
        catch (RuntimeException e){
            throw new BackendConnectionException("Ships service");
        }

    }

    @Override
    public void deleteSailor(long id) {
        String request = SAILORS_BACKEND_URL + "/" + id;
        try {
            sendDeleteRequest(request);
        }
        catch (HttpClientErrorException e) {
            if(e.getStatusCode() == HttpStatus.BAD_REQUEST || e.getStatusCode() == HttpStatus.NOT_FOUND)
                throw new BackendNotException("Ships service", e.getStatusCode());
            else
                throw new BackendConnectionException("Ships service");
        }
        catch (RuntimeException e){
            throw new BackendConnectionException("Ships service");
        }
    }

    @Override
    public boolean existsShip(long shipId) {
        String request = SHIPS_BACKEND_URL + "/" + shipId;
        try {
            return existsObject(request, Ships.class);
        }
        catch (HttpClientErrorException e) {
            if(e.getStatusCode() == HttpStatus.BAD_REQUEST || e.getStatusCode() == HttpStatus.NOT_FOUND)
                return false;
            else
                throw new BackendConnectionException("Ships service");
        }
        catch (RuntimeException e){
            throw new BackendConnectionException("Ships service");
        }
    }

    @Override
    public Users getUser(long userId) {
        String request = USERS_BACKEND_URL + "/" + userId;
        return getUser(request);
    }

    @Override
    public void updateUser(Users newUser) {
        String request = USERS_BACKEND_URL + "/" + newUser.getId();
        try {
            updateObjectViaConnect(request, newUser);
        }
        catch (HttpClientErrorException e) {
            if(e.getStatusCode() == HttpStatus.BAD_REQUEST || e.getStatusCode() == HttpStatus.NOT_FOUND)
                throw new BackendNotException("Users service", e.getStatusCode());
            else
                throw new BackendConnectionException("Users service");
        }
        catch (RuntimeException e){
            throw new BackendConnectionException("Users service");
        }
    }

    @Override
    public String createUser(Users newUser) {
        return createObjectViaConnect(USERS_BACKEND_URL, newUser);
    }

    @Override
    public Users getUser(String user) {
        try {
            return getObjectViaConnect(user, Users.class);
        }
        catch (HttpClientErrorException e) {
            if(e.getStatusCode() == HttpStatus.BAD_REQUEST || e.getStatusCode() == HttpStatus.NOT_FOUND)
                throw new BackendNotException("Users service", e.getStatusCode());
            else
                throw new BackendConnectionException("Users service");
        }
        catch (RuntimeException e){
            throw new BackendConnectionException("Users service");
        }
    }

    @Override
    public boolean existsUserWithLogin(String login) {
        String request = USERS_BACKEND_URL + "/?login=" + login;
        try {
            return existsObject(request, Users.class);
        }
        catch (HttpClientErrorException e) {
            if(e.getStatusCode() == HttpStatus.BAD_REQUEST || e.getStatusCode() == HttpStatus.NOT_FOUND)
                return false;
            else
                throw new BackendConnectionException("Users service");
        }
        catch (RuntimeException e){
            throw new BackendConnectionException("Users service");
        }
    }

    @Override
    public Users getUserViaLogin(String login) {
        String request = USERS_BACKEND_URL + "/?login=" + login;
        return getUser(request);
    }

    @Override
    public Sessions createOrRefreshSession(Users u, HttpServletResponse response) {
        Sessions s;

        if(testSessionForUserId(u.getId())) {
            log.info("ALKI1");
            s = getSessionForUserId(u.getId());
            log.info(s.toString());
            refreshSession(s);
            log.info(s.toString());
        }
        else {
            log.info("ALKI111");
            s = createSession(u.getId());
        }
        setUserCookee(u.getId(), response);

        log.info(s.toString());

        return s;
    }

    @Override
    public void setUserCookee(Long userId, HttpServletResponse response) {
        response.addCookie(new Cookie(USER_COOKIE_NAME, userId.toString()));
    }

    @Override
    public void refreshSession(Sessions s) {
        String request = SESSIONS_BACKEND_URL + "/" +s.getUserId();
        try {
            updateObjectViaConnect(request, null);
        }
        catch (HttpClientErrorException e) {
            if(e.getStatusCode() == HttpStatus.BAD_REQUEST || e.getStatusCode() == HttpStatus.NOT_FOUND)
                throw new BackendNotException("Sessions service", e.getStatusCode());
            else
                throw new BackendConnectionException("Sessions service");
        }
        catch (RuntimeException e){
            throw new BackendConnectionException("Sessions service");
        }
    }

    @Override
    public void refreshSessionForUserId(Long id) {
        Sessions s = getSessionForUserId(id);
        refreshSession(s);
    }

    @Override
    public Sessions createSession(Long id) {
        String request = SESSIONS_BACKEND_URL + "/" +id;
        try {
            String loc = createObjectViaConnect(request, null);
            log.info(loc);
            return getSession(loc);
        }
        catch (HttpClientErrorException e) {
            if(e.getStatusCode() == HttpStatus.BAD_REQUEST || e.getStatusCode() == HttpStatus.NOT_FOUND)
                throw new BackendNotException("Sessions service", e.getStatusCode());
            else
                throw new BackendConnectionException("Sessions service");
        }
        catch (RuntimeException e){
            throw new BackendConnectionException("Sessions service");
        }
    }

    @Override
    public boolean testSessionForUserId(Long id) {
        try{
            log.info("ALKI");
            getSessionForUserId(id);
        }
        catch (BackendNotException e){
            return false;
        }
        log.info("ALKI1");
        return true;
    }

    @Override
    public String getUserCookieName() {
        return USER_COOKIE_NAME;
    }

    @Override
    public void logout(long userId) {
        String request;
        try{
            Sessions s = getSessionForUserId(userId);
            request = SESSIONS_BACKEND_URL + "/" + s.getSessionId();
            sendDeleteRequest(request);
        }
        catch (HttpClientErrorException e) {
            if(e.getStatusCode() == HttpStatus.BAD_REQUEST || e.getStatusCode() == HttpStatus.NOT_FOUND)
                throw new BackendNotException("Sessions service", e.getStatusCode());
            else
                throw new BackendConnectionException("Sessions service");
        }
        catch (RuntimeException e){
            throw new BackendConnectionException("Sessions service");
        }
    }

    @Override
    public Sessions getSessionForUserId(Long id) {
        String request = SESSIONS_BACKEND_URL + "/" +id;
        return getSession(request);
    }

    @Override
    public Sessions getSession(String str) {
        try {
            return getObjectViaConnect(str, Sessions.class);
        }
        catch (HttpClientErrorException e) {
            if(e.getStatusCode() == HttpStatus.BAD_REQUEST || e.getStatusCode() == HttpStatus.NOT_FOUND)
                throw new BackendNotException("Sessions service", e.getStatusCode());
            else
                throw new BackendConnectionException("Sessions service");
        }
        catch (RuntimeException e){
            throw new BackendConnectionException("Sessions service");
        }
    }


    private <T> boolean existsObject(String request, Class<T> responseType) {
        RestTemplate restTemplate = new RestTemplate();
        boolean result = true;

        try {
            restTemplate.getForEntity(request, responseType);
        }
        catch (HttpClientErrorException e){
            if(e.getStatusCode() == HttpStatus.NOT_FOUND)
                result = false;
            else
                throw e;
        }

        return result;
    }

    private <T> T getObjectViaConnect(@NotNull String request, Class<T> responseType){
        RestTemplate restTemplate = new RestTemplate();
        T object = restTemplate.getForObject(request, responseType);
        return object;
    }

    private String createObjectViaConnect(@NotNull String url, Object request){
        RestTemplate restTemplate = new RestTemplate();
        return restTemplate.postForLocation(url, request).toString();
    }

    private void updateObjectViaConnect(@NotNull String url, Object request){
        RestTemplate restTemplate = new RestTemplate();
        restTemplate.put(url, request);
    }

    private void sendDeleteRequest(@NotNull String url){
        RestTemplate restTemplate = new RestTemplate();
        restTemplate.delete(url);
    }

//    private ResponseEntity<T> T exchange(@NotNull String request, Class<T> responseType){
//        RestTemplate restTemplate = new RestTemplate();
//        T object = restTemplate.getForObject(request, responseType);
//        return object;
//    }
}
