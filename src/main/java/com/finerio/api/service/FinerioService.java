package com.finerio.api.service;
import com.finerio.api.model.in.UserIn;
import com.finerio.api.model.out.UserOut;

public interface FinerioService {
	
	 UserOut login(UserIn in);

}
