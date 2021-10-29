package com.finerio.api.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.finerio.api.exception.BadRequestException;
import com.finerio.api.helper.RestTemplateHelper;
import com.finerio.api.model.in.UserIn;
import com.finerio.api.model.out.UserOut;
import com.finerio.api.service.FinerioService;
import com.finerio.api.util.Constants;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class FinerioServiceImpl implements FinerioService {

	@Autowired
	private RestTemplateHelper restTemplateHelper;

	@Value("${url.api.finerio}")
	private String api;

	@Override
	public UserOut login(UserIn in) {
		log.info("--- login --- ", in);

		if (in == null || in.getPassword().equals("") || in.getUsername().equals(""))
			throw new BadRequestException("Invalid request.");

		UserOut out = restTemplateHelper.postForEntity(UserOut.class, api + Constants.LOGIN_PATH, in);

		return out;
	}
	
	
	

}
