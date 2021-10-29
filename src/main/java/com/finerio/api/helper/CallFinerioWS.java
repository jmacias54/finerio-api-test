package com.finerio.api.helper;


import com.finerio.api.exception.ServiceException;
import com.finerio.api.model.Movement;
import com.finerio.api.model.User;
import com.finerio.api.model.out.MovementOut;
import com.finerio.api.util.Constants;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.http.client.ClientHttpRequestFactory;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@Component
@Slf4j
public class CallFinerioWS {


    @Value("${url.api.finerio}")
    private String api;


    private RestTemplate restTemplate;
    private HttpHeaders headers = new HttpHeaders();

    public CallFinerioWS()
    {
        super();
        restTemplate = new RestTemplate();
        ClientHttpRequestFactory factory = restTemplate.getRequestFactory();

        if ( factory instanceof SimpleClientHttpRequestFactory)
        {
            ((SimpleClientHttpRequestFactory) factory).setConnectTimeout( 50 * 1000 );
            ((SimpleClientHttpRequestFactory) factory).setReadTimeout( 50 * 1000 );
        }
        else if ( factory instanceof HttpComponentsClientHttpRequestFactory)
        {
            ((HttpComponentsClientHttpRequestFactory) factory).setReadTimeout( 50 * 1000);
            ((HttpComponentsClientHttpRequestFactory) factory).setConnectTimeout( 50 * 1000);

        }
        restTemplate.setRequestFactory( factory );
        headers.setContentType(MediaType.APPLICATION_JSON);
    }


    public MovementOut getMovements(String token,String idUser,int offset) {
        log.debug(" --- getMovements ----");
        List<Movement> list = null;
        MovementOut movements = null ;


        try {

            HttpHeaders headers = new HttpHeaders();
            headers.add("Authorization", "Bearer " + token);
            String URL_WS = api +"users/"+idUser+"/movements?offset="+offset+"&max=10";

            log.debug("URL_WS: "+URL_WS);
            HttpEntity<String> request = new HttpEntity<String>(headers);
            ResponseEntity<MovementOut> response = restTemplate.exchange(URL_WS, HttpMethod.GET, request, MovementOut.class);
            movements = response.getBody();


        } catch(Exception e) {
            log.error("Error getMovements : ",e);
            throw new ServiceException(e.getMessage());
        }
        return movements;
    }


    public User getUser( String token) {
        log.debug(" --- getUser ----");
        User user = null ;

        try {

            HttpHeaders headers = new HttpHeaders();
            headers.add("Authorization", "Bearer " + token);
            String URL_WS = api + Constants.ME_PATH;

            log.debug("URL_WS: "+URL_WS);
            HttpEntity<String> request = new HttpEntity<String>(headers);
            ResponseEntity<User> response = restTemplate.exchange(URL_WS, HttpMethod.GET, request, User.class);
            user = response.getBody();


        } catch(Exception e) {
            log.error("Error getUser : ",e);
            throw new ServiceException(e.getMessage());
        }
        return user;
    }
}
