package com.finerio.api.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.finerio.api.helper.CallFinerioWS;
import com.finerio.api.model.Movement;
import com.finerio.api.model.entity.BinnacleAccountFinerio;
import com.finerio.api.model.out.MovementOut;
import com.finerio.api.model.out.UserOut;
import com.finerio.api.repository.BinnacleAccountFinerioRepository;
import com.finerio.api.service.MovementsService;

import lombok.extern.slf4j.Slf4j;


@Service
@Slf4j
public class MovementsServiceImpl implements MovementsService {


    @Value("${url.api.finerio}")
    private String api;

    @Autowired
    private BinnacleAccountFinerioRepository repository;


    @Autowired
    private CallFinerioWS callFinerioWS;

    @Override
    public void process(UserOut out, String idUser , int offset ) {
        log.debug(" --- process --- ",out,idUser,offset);
        if( offset <= 10){
            List<Movement> list = getMovements(out.getAccessToken(), idUser, offset);

            if (list != null && list.size() > 0)
                insertOrUpdateMovements(list);

            process(out,  idUser , offset + 10 );
        }

    }

    @Override
    public List<BinnacleAccountFinerio> getAll() {
        return repository.findAll();
    }


    private List<Movement> getMovements(String token,String idUser,int offset){
        MovementOut movements = callFinerioWS.getMovements(token,idUser,offset);
        return movements.getData();
    }

    private void insertOrUpdateMovements(List<Movement> movements){
        movements.forEach((item) -> {
            System.out.println(item);

            BinnacleAccountFinerio resp = repository.save( BinnacleAccountFinerio.builder()
                                .id(item.getId())
                                .amount(item.getAmount())
                                .description(item.getDescription())
                                .date(item.getDate())
                                .build());

            System.out.println(resp);


        } );

    }


}
