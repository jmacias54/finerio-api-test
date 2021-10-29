package com.finerio.api.service.impl;

import com.finerio.api.exception.BadRequestException;
import com.finerio.api.exception.UnknownResourceException;
import com.finerio.api.helper.CallFinerioWS;
import com.finerio.api.helper.RestTemplateHelper;
import com.finerio.api.model.User;
import com.finerio.api.model.in.UserIn;
import com.finerio.api.model.out.LoginOut;
import com.finerio.api.model.out.UserOut;
import com.finerio.api.service.FinerioService;
import com.finerio.api.service.MovementsService;
import com.finerio.api.util.Constants;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.stereotype.Service;
import java.util.Date;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
public class FinerioServiceImpl implements FinerioService {

	@Autowired
	private RestTemplateHelper restTemplateHelper;

	@Autowired
	private CallFinerioWS callFinerioWS;

	@Value("${url.api.finerio}")
	private String api;

	@Autowired
	private MovementsService movementsService;



	@Override
	public LoginOut login(UserIn in) {
		log.info("--- login --- ", in);

		if (in == null || in.getPassword().equals("") || in.getUsername().equals(""))
			throw new BadRequestException("Invalid request.");

		UserOut out = restTemplateHelper.postForEntity(UserOut.class, api + Constants.LOGIN_PATH, in);

		if(out == null )
			throw new BadRequestException("Invalid credentials.");

		User user =  callFinerioWS.getUser(out.getAccessToken());

		if( user == null )
			throw new UnknownResourceException(" User not found or unknown");

		movementsService.process(out,user.getId(),0);

		return LoginOut.builder().userFinero(out).token(getJWTToken( in)).build();
	}

	private String getJWTToken(UserIn in) {
		log.info("--- getJWTToken --- ", in);

		String secretKey = "mySecretKey";
		List<GrantedAuthority> grantedAuthorities = AuthorityUtils.commaSeparatedStringToAuthorityList("ROLE_USER");

		String token = Jwts.builder().setId("api.jwt").setSubject(in.getUsername())
				.claim("authorities",
						grantedAuthorities.stream().map(GrantedAuthority::getAuthority).collect(Collectors.toList()))
				.setIssuedAt(new Date(System.currentTimeMillis()))
				.setExpiration(new Date(System.currentTimeMillis() + 600000))
				.signWith(SignatureAlgorithm.HS512, secretKey.getBytes()).compact();

		return "Bearer " + token;
	}
	

}
