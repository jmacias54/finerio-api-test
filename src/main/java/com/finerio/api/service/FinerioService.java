package com.finerio.api.service;
import com.finerio.api.model.in.UserIn;
import com.finerio.api.model.out.LoginOut;

public interface FinerioService {

	LoginOut login(UserIn in);

}
