package com.bmstu.rsoi_lab3;

import com.bmstu.rsoi_lab3.models.Sailors;
import com.bmstu.rsoi_lab3.models.SailorsPage;
import com.bmstu.rsoi_lab3.models.Ships;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import javax.validation.constraints.NotNull;

/**
 * Created by Александр on 17.02.2016.
 */

@Component("backendConnector")
public class BackendConnectorImpl implements BackendsConnector {
    protected static String SAILORS_BACKEND_URL = "http://127.0.0.1:8001/sailors";
    protected static String SHIPS_BACKEND_URL = "http://127.0.0.1:8010/ships";

    @Override
    public Sailors getSailor(long id) {
        String request = SAILORS_BACKEND_URL + "/" + id;
        return getObjectViaConnect(request, Sailors.class);
        //return new Sailors(new Long(1), "TSTNAME", "TSTLASTNAME", "TSTSPECIALITY", null/*new Date(Calendar.getInstance().getTime().getTime())*/, new Long(1));
    }

    @Override
    public Ships getShips(long id) {
        String request = SHIPS_BACKEND_URL + "/" + id;
        return getObjectViaConnect(request, Ships.class);
    }

    @Override
    public SailorsPage getSailors(int pageNum, int pageSize) {
        String request = SAILORS_BACKEND_URL + "?page="+pageNum+"&per_page="+pageSize;
        return getObjectViaConnect(request, SailorsPage.class);
    }

    private <T> T getObjectViaConnect(@NotNull String request, Class<T> responseType){
        RestTemplate restTemplate = new RestTemplate();
        T object = restTemplate.getForObject(request, responseType);
        return object;
    }

//    private ResponseEntity<T> T exchange(@NotNull String request, Class<T> responseType){
//        RestTemplate restTemplate = new RestTemplate();
//        T object = restTemplate.getForObject(request, responseType);
//        return object;
//    }
}
