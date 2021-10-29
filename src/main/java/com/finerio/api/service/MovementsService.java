package com.finerio.api.service;

import java.util.List;

import com.finerio.api.model.entity.BinnacleAccountFinerio;
import com.finerio.api.model.out.UserOut;

public interface MovementsService {

    void process(UserOut out, String idUser , int offset);

    List<BinnacleAccountFinerio> getAll();
}
