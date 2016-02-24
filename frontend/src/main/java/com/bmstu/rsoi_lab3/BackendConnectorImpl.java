package com.bmstu.rsoi_lab3;

import com.bmstu.rsoi_lab3.exception.BackendConnectionException;
import com.bmstu.rsoi_lab3.exception.BackendNotException;
import com.bmstu.rsoi_lab3.markers.SailorBackend;
import com.bmstu.rsoi_lab3.markers.SessionBackend;
import com.bmstu.rsoi_lab3.markers.ShipBackend;
import com.bmstu.rsoi_lab3.models.*;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.RequestEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import javax.validation.constraints.NotNull;
import java.io.IOException;
import java.net.URI;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Александр on 17.02.2016.
 */

@Component("backendConnector")
public class BackendConnectorImpl implements BackendsConnector {
    protected static String SAILORS_BACKEND_URL = "http://127.0.0.1:8001/sailors";
    protected static String SHIPS_BACKEND_URL = "http://127.0.0.1:8010/ships";
    protected static String SESSIONS_BACKEND_URL = "http://127.0.0.1:8100/sessions";

    private static final Logger log = LoggerFactory.getLogger(FrontendApplication.class);

    @Override
    public Sailors getSailor(long id) {
        String request = SAILORS_BACKEND_URL + "/" + id;
        return getSailor(request);
    }

    @Override
    public Sailors getSailor(String url) {
        return getObjectViaConnect(url, Sailors.class);

    }

    @Override
    public Ships getShip(long id) {
        String request = SHIPS_BACKEND_URL + "/" + id;
        return getShip(request);
    }

    @Override
    public Ships getShip(String url) {
        return getObjectViaConnect(url, Ships.class);
    }

    @Override
    public SailorsPage getSailors(int pageNum, int pageSize) {
        String request = SAILORS_BACKEND_URL + "?page="+pageNum+"&per_page="+pageSize;
        return getObjectViaConnect(request, SailorsPage.class);
    }

    @Override
    public ShipsPage getShips(int pageNum, int pageSize) {
        String request = SHIPS_BACKEND_URL + "?page="+pageNum+"&per_page="+pageSize;
        return getObjectViaConnect(request, ShipsPage.class);
    }

    @Override
    public Map<Long, String> getShipsNames(List<Long> ids) {
        String request = SHIPS_BACKEND_URL + "/names";
        String params = "?id=" + ids.get(0);

        for(int i = 1; i < ids.size(); i++)
            params += "," + ids.get(i);

        String resp = getObjectViaConnect(request + params, String.class);
        List<NameMapWrapper> retLst;
        try {
            retLst = new ObjectMapper().readValue(resp,
                    new TypeReference<List<NameMapWrapper>>(){});
        } catch (IOException e) {
            throw new HttpClientErrorException(HttpStatus.BAD_REQUEST, "Bad request was sended to Ships backend");
        }

        Map<Long, String> resultMap = new HashMap<>(retLst.size());

        for(NameMapWrapper retVal: retLst){
            resultMap.put(retVal.getId(), retVal.getName());
        }

        return resultMap;
    }

    @Override
    public Ships createShips(Ships s) {
        return createObjectViaConnect(SHIPS_BACKEND_URL, s, Ships.class);
    }

    @Override
    public Sailors createSailors(Sailors s) {
        return createObjectViaConnect(SAILORS_BACKEND_URL, s, Sailors.class);
    }

    @Override
    public void updateSailors(Sailors s) {
        String request = SAILORS_BACKEND_URL + "/" + s.getId();
        updateObjectViaConnect(request, s, Sailors.class);

    }

    @Override
    public void updateShips(Ships s) {
        String request = SHIPS_BACKEND_URL + "/" + s.getId();
        updateObjectViaConnect(request, s, Ships.class);

    }

    @Override
    public void deleteShips(long id) {
        String request = SHIPS_BACKEND_URL + "/" + id;
        sendDeleteRequest(request, Ships.class);

    }

    @Override
    public void deleteSailor(long id) {
        String request = SAILORS_BACKEND_URL + "/" + id;
        sendDeleteRequest(request, Sailors.class);
    }

    @Override
    public boolean existsShip(long shipId) {
        String request = SHIPS_BACKEND_URL + "/" + shipId;
        return existsObject(request, Ships.class);
    }

    @Override
    public Sessions getSession(String login) {
        String request = SESSIONS_BACKEND_URL + "/" + login;
        return getObjectViaConnect(request, Sessions.class);
    }

    @Override
    public Sessions createSession(Sessions s) {
        String request = SESSIONS_BACKEND_URL;
        return createObjectViaConnect(request, s, Sessions.class);
    }

    @Override
    public void updateSessions(Sessions s) {
        String request = SESSIONS_BACKEND_URL + "/" + s.getSessionId();
        updateObjectViaConnect(request, s, Sessions.class);

    }

    @Override
    public void deleteSessions(long sessionId) {
        String request = SESSIONS_BACKEND_URL + "/" + sessionId;
        sendDeleteRequest(request, Sessions.class);
    }

    private <T> boolean existsObject(String request, Class<T> responseType) {
        boolean result = true;
        try {
            getObjectViaConnect(request, responseType);
        }
        catch (BackendNotException e){
            result = false;
        }

        return result;
    }

    private <T> T getObjectViaConnect(@NotNull String request, Class<T> responseType, Map<String, ?> urlParams){
        try {
            RestTemplate restTemplate = new RestTemplate();
            T object = restTemplate.getForObject(request, responseType, urlParams);
            return object;
        }
        catch (HttpClientErrorException e) {

            if(e.getStatusCode() == HttpStatus.BAD_REQUEST || e.getStatusCode() == HttpStatus.NOT_FOUND)
                throw new BackendNotException(getBackendName(responseType), e.getStatusCode());
            else
                throw new BackendConnectionException(getBackendName(responseType));
        }
        catch (RuntimeException e){
            throw new BackendConnectionException(getBackendName(responseType));
        }
    }

    private <T> T getObjectViaConnect(@NotNull String request, Class<T> responseType){
        return getObjectViaConnect(request, responseType, new HashMap<>());
    }


    private <T> T createObjectViaConnect(@NotNull String url, Object request, Class<T> clazz){
        try {
            RestTemplate restTemplate = new RestTemplate();
            return restTemplate.postForObject(url, request, clazz);
        }
        catch (HttpClientErrorException e) {

            if(e.getStatusCode() == HttpStatus.BAD_REQUEST || e.getStatusCode() == HttpStatus.NOT_FOUND)
                throw new BackendNotException(getBackendName(clazz), e.getStatusCode());
            else
                throw new BackendConnectionException(getBackendName(clazz));
        }
        catch (RuntimeException e){
            throw new BackendConnectionException(getBackendName(clazz));
        }
    }

    private <T> void updateObjectViaConnect(@NotNull String url, Object request, Class<T> clazz){
        try {
            RestTemplate restTemplate = new RestTemplate();
            restTemplate.put(url, request);
        }
        catch (HttpClientErrorException e) {

            if(e.getStatusCode() == HttpStatus.BAD_REQUEST || e.getStatusCode() == HttpStatus.NOT_FOUND)
                throw new BackendNotException(getBackendName(clazz), e.getStatusCode());
            else
                throw new BackendConnectionException(getBackendName(clazz));
        }
        catch (RuntimeException e){
            throw new BackendConnectionException(getBackendName(clazz));
        }
    }

    private <T> void sendDeleteRequest(@NotNull String url, Class<T> clazz){
        try {
            RestTemplate restTemplate = new RestTemplate();
            restTemplate.delete(url);
        }
        catch (HttpClientErrorException e) {

            if(e.getStatusCode() == HttpStatus.BAD_REQUEST || e.getStatusCode() == HttpStatus.NOT_FOUND)
                throw new BackendNotException(getBackendName(clazz), e.getStatusCode());
            else
                throw new BackendConnectionException(getBackendName(clazz));
        }
        catch (RuntimeException e){
            throw new BackendConnectionException(getBackendName(clazz));
        }
    }

    private String getBackendName(Class<?> clazz){
        if(SessionBackend.class.isAssignableFrom(clazz)){
            return "Sessions backend";
        }
        else if(SailorBackend.class.isAssignableFrom(clazz)){
            return "Sailors backend";
        }
        else if(ShipBackend.class.isAssignableFrom(clazz)){
            return "Ships backend";
        }
        else{
            return "Unknown backend";
        }
    }

//    private ResponseEntity<T> T exchange(@NotNull String request, Class<T> responseType){
//        RestTemplate restTemplate = new RestTemplate();
//        T object = restTemplate.getForObject(request, responseType);
//        return object;
//    }
}
