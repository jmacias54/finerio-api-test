package com.finerio.api.service;

import com.finerio.api.model.out.UserOut;

public interface MovementsService {

    void process(UserOut out, String idUser , int offset);
}
